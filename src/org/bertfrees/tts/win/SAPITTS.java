package org.bertfrees.tts.win;

import java.util.Locale;
import net.sf.okapi.common.LCIDUtil;
import net.sf.okapi.common.LocaleId;
import org.bertfrees.tts.TTS;
import org.bertfrees.tts.Voice;
import org.concord.win.sapi53.ISpeechObjectTokens;

/**
 *
 * @author Bert Frees
 */
public class SAPITTS extends TTS {

    private final String sapiVoiceSelectionParam;
    private final SAPIVoice voice;
    
    public SAPITTS() {
        sapiVoiceSelectionParam = "";
        voice = SAPIVoice.getDefault();
    }
    
    public SAPITTS(String sapiVoiceSelectionParam) {
        this.sapiVoiceSelectionParam = sapiVoiceSelectionParam;
        voice = updateVoice(sapiVoiceSelectionParam);
    }
    
    public static SAPITTS newNameBasedSAPITTS(String name) {
        return new SAPITTS("Name=" + name);
    }
    
    public static SAPITTS newLocaleBasedSAPITTS(Locale locale) {
        int lcid = LCIDUtil.getLCID(new LocaleId(locale));
        if (lcid <= 0) { throw new RuntimeException("Unknown locale"); }
        return new SAPITTS("Language=" + Integer.toHexString(lcid).toUpperCase());
    }

    public String getSAPIVoiceSelectionParam() {
        return sapiVoiceSelectionParam;
    }
    
//    public void setSAPIVoiceSelectionParam(String sapiVoiceSelectionParam) {
//        if (this.sapiVoiceSelectionParam.equals(sapiVoiceSelectionParam)) { return; }
//        this.sapiVoiceSelectionParam = sapiVoiceSelectionParam;
//        voice = updateVoice();
//        notifyListeners();
//    }
//    
//    public void setNameBasedSAPIVoiceSelectionParam(String name) {
//        setSAPIVoiceSelectionParam("Name=" + name);
//    }
//    
//    public void setLocaleBasedSAPIVoiceSelectionParam(Locale locale) {
//        int lcid = LCIDUtil.getLCID(new LocaleId(locale));
//        if (lcid <= 0) { throw new RuntimeException("Unknown locale"); }
//        setSAPIVoiceSelectionParam("Language=" + Integer.toHexString(lcid).toUpperCase());
//    }
    
    private static SAPIVoice updateVoice(String sapiVoiceSelectionParam) {
        ISpeechObjectTokens tokens = SAPIVoice.iSpeechVoice.getVoices(sapiVoiceSelectionParam, "");
        return (tokens.count() > 0) ? SAPIVoice.get(tokens.item(0).getAttribute("Name"))
                                    : SAPIVoice.getDefault();
    }

    @Override
    public Voice getVoice() {
        return voice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof SAPITTS)) { return false; }
        try {
            SAPITTS that = (SAPITTS)o;
            return this.sapiVoiceSelectionParam.equals(that.sapiVoiceSelectionParam);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.sapiVoiceSelectionParam != null ? this.sapiVoiceSelectionParam.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "SAPI TTS [@sapiVoiceSelection='" + sapiVoiceSelectionParam + "']";
    }
}
