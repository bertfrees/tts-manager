package org.bertfrees.tts;

import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.xpath.XPathAPI;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.bertfrees.tts.mac.MacOSTTS;
import org.bertfrees.tts.win.SAPITTS;

/**
 *
 * @author Bert
 */
public class TTSBuilderConfiguration implements TTSManagerListener {
    
    private final static String SAPI_TTS_CLASSNAME = "se_tpb_speechgen2.external.win.DefaultSapiTTS";
    private final static String MACOS_TTS_CLASSNAME = "se_tpb_speechgen2.external.MacOS.MacSayTTS";
    private final static String ESPEAK_TTS_CLASSNAME = "se_tpb_speechgen2.external.linux.ESpeakTTS";
    
    private final File file;
    private final Document document;
    private final TTSManager manager;
    private final Map<Locale,Element> langElements;
    
    private Element os = null;
            
    public TTSBuilderConfiguration(File file) throws Exception {
        
        this.file = file;
        document = parse(file);
        manager = new TTSManager();
        langElements = new HashMap<Locale,Element>();
        readXML();
        manager.addListener(this);
    }
    
    public TTSManager getTTSManager() {
        return manager;
    }
        
    private void readXML() throws TTSBuilderConfigurationException,
                                  TransformerException,
                                  Exception {
        
        Properties systemProps = System.getProperties();
        NodeList osList = XPathAPI.selectNodeList(document.getDocumentElement(), "/ttsbuilder/os");

        for (int i=0; i<osList.getLength(); i++) {
            Element currentOS = (Element)osList.item(i);
            NodeList propertyList = XPathAPI.selectNodeList(currentOS, "property");
			boolean matching = true;
			for (int j=0; j<propertyList.getLength(); j++) {
				Element currentProperty = (Element)propertyList.item(j);
				String propertyName = currentProperty.getAttribute("name");
				String propertyMatch = currentProperty.getAttribute("match");
				String systemPropValue = systemProps.getProperty(propertyName);
                matching = (propertyName.length() > 0 &&
                            propertyMatch.length() > 0 &&
                            systemPropValue != null &&
                            systemPropValue.matches(propertyMatch));
				if (!matching) { break; }
			}
            if (matching) {
                os = currentOS;
                break;
            }
        }
        
        if (os == null) {
			throw new TTSBuilderConfigurationException(
                        "file does not contain any operating system entry matching " +
                        "the current environment: " + System.getProperty("os.name"));
        }
        
        NodeList langList = XPathAPI.selectNodeList(os, "lang");
        for (int i=0; i<langList.getLength(); i++) {
            Element currentLang = (Element)langList.item(i);
            String langAttr = currentLang.getAttribute("lang");
            if (langAttr.length() == 0) { throw new TTSBuilderConfigurationException(
                    "lang element without lang attribute"); }
            Locale locale = parseLocaleString(langAttr);
            if (langElements.containsKey(locale)) { throw new TTSBuilderConfigurationException(
                    "multiple lang element with same locale = " + locale.toString()); }
            Element currentTTS = (Element)XPathAPI.selectSingleNode(currentLang, "tts");
            if (currentTTS == null) { throw new TTSBuilderConfigurationException(
                    "lang element without tts child"); }
            TTSElement ttsElement = new TTSElement(currentTTS);
            TTS tts = ttsElement.getTTS();
            manager.put(locale, tts);
            langElements.put(locale, currentLang);
        }

        Element defaultTTS = (Element)XPathAPI.selectSingleNode(os, "lang/tts[@default='true']");
        if (defaultTTS == null) { throw new TTSBuilderConfigurationException(
                "No default TTS found"); }
        
    }
    
    public class TTSElement implements TTSListener {
        
        private final Element element;
        private final TTS tts;
        
        public TTSElement(Element element)
                   throws TTSBuilderConfigurationException,
                          TransformerException,
                          Exception {

            this.element = element;
            Map<String,String> parameters = new HashMap<String,String>();
            NodeList paramList = XPathAPI.selectNodeList(element, "param[@name!='command']");
            for (int i=0; i<paramList.getLength(); i++) {
                Element currentParam = (Element)paramList.item(i);
                String nameAttr = currentParam.getAttribute("name");
                String valueAttr = currentParam.getAttribute("value");
                if (nameAttr.length() == 0 || valueAttr.length() == 0) { 
                    throw new TTSBuilderConfigurationException(
                        "param element without name or value attribute"); }
                parameters.put(nameAttr, valueAttr);
            }
            String className = parameters.get("class");
            if (className == null) { throw new TTSBuilderConfigurationException(
                    "class name must be provided for every TTS Java wrapper implementation"); }

            if (className.equals(SAPI_TTS_CLASSNAME)) {
                String sapiVoiceSelectionParam = parameters.get("sapiVoiceSelection");
                if (sapiVoiceSelectionParam == null) {
                    tts = new SAPITTS();
                } else {
                    tts = new SAPITTS(sapiVoiceSelectionParam);
                }
            } else if (className.equals(MACOS_TTS_CLASSNAME)) {
                String voiceParam = parameters.get("voice");
                if (voiceParam == null) {
                    tts = new MacOSTTS();
                } else {
                    tts = new MacOSTTS(voiceParam);
                }
            } else if (className.equals(ESPEAK_TTS_CLASSNAME)) {
                throw new UnsupportedOperationException("Not supported yet.");
            } else {
                throw new TTSBuilderConfigurationException(
                        "unknown class name '" + className + "'");
            }
            tts.addListener(this);
        }

        public TTSElement(TTS tts) {
            this.tts = tts;
            element = document.createElement("tts");
            Element classParam = document.createElement("param");
            element.appendChild(classParam);
            classParam.setAttribute("name", "class");
            if (tts instanceof SAPITTS) {
                classParam.setAttribute("value", SAPI_TTS_CLASSNAME);
                String sapiVoiceSelectionParam = ((SAPITTS)tts).getSAPIVoiceSelectionParam();
                if (sapiVoiceSelectionParam.length() > 0) {
                    Element otherParam = document.createElement("param");
                    element.appendChild(otherParam);
                    otherParam.setAttribute("name", "sapiVoiceSelection");
                    otherParam.setAttribute("value", sapiVoiceSelectionParam);
                }
            } else if (tts instanceof MacOSTTS) {
                classParam.setAttribute("value", MACOS_TTS_CLASSNAME);
                String voiceParam = ((MacOSTTS)tts).getVoiceParam();
                if (voiceParam.length() > 0) {
                    Element otherParam = document.createElement("param");
                    element.appendChild(otherParam);
                    otherParam.setAttribute("name", "voice");
                    otherParam.setAttribute("value", voiceParam);
                }
            } else {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            tts.addListener(this);
        }
        
        public TTS getTTS() {
            return tts;
        }
        
        public Element getElement() {
            return element;
        }

        @Override
        public void ttsUpdated(TTS tts) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    
    @Override
    public void ttsAdded(TTSManager manager, Locale locale) {
	    if (manager != this.manager) { return; }
        TTSElement ttsElement = new TTSElement(manager.get(locale));
        Element lang = document.createElement("lang");
        langElements.put(locale, lang);
        lang.setAttribute("lang", locale.toString());
        lang.appendChild(ttsElement.getElement());
        os.appendChild(lang);
    }

    @Override
    public void ttsRemoved(TTSManager manager, Locale locale) {
        if (manager != this.manager) { return; }
        Element lang = langElements.remove(locale);
        if (lang == null) { throw new RuntimeException(); }
        lang.getParentNode().removeChild(lang);
    }
    
    public void close() throws TransformerConfigurationException, TransformerException {
        save(document, file);
    }
 
    private Document parse(File file)
                    throws ParserConfigurationException,
                           SAXException, 
                           IOException {
    
        DocumentBuilderFactory docFactory;
        docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setValidating(false);
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        docBuilder.setEntityResolver(new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
            }
        });
        return docBuilder.parse(file.getAbsolutePath());
        
    }
    
    private void save(Document doc, File file)
               throws TransformerConfigurationException,
                      TransformerException {
        
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "3");

        StreamResult result = new StreamResult(file.toURI().getPath());
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);
    }
    
    private static Locale parseLocaleString(String language) {
        
        int idx = language.indexOf("_");
        if (idx < 0) { return new Locale(language); }
        String country = language.substring(idx + 1);
        language = language.substring(0, idx);
        idx = country.indexOf("_");
        if (idx < 0) { return new Locale(language, country); }
        String variant = country.substring(idx + 1);
        country = country.substring(0, idx);
        return new Locale(language, country, variant);
    }
}
