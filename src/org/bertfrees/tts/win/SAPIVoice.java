package org.bertfrees.tts.win;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import org.bertfrees.tts.Voice;
import org.concord.win.sapi53.ClassFactory;
import org.concord.win.sapi53.ISpeechVoice;
import org.concord.win.sapi53.ISpeechObjectToken;
import org.concord.win.sapi53.ISpeechObjectTokens;
import org.concord.win.sapi53.SpeechVoiceSpeakFlags;


/**
 *
 * @author Bert Frees
 */
public class SAPIVoice extends Voice {
    
    private final static boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");
    static { if (!IS_WINDOWS) { throw new RuntimeException("Only available on Windows"); }}
    
    protected static final ISpeechVoice iSpeechVoice;
    
    private static final SAPIVoice defaultVoice;
    private static final Map<String,SAPIVoice> availableVoices;
    
    static {
        iSpeechVoice = ClassFactory.createSpVoice();
        defaultVoice = new SAPIVoice(iSpeechVoice.voice());
        availableVoices = new HashMap<String,SAPIVoice>();
        ISpeechObjectTokens allTokens = iSpeechVoice.getVoices("", "");
        for (int i=0; i<allTokens.count(); i++) {
            SAPIVoice v = new SAPIVoice(allTokens.item(i));
            availableVoices.put(v.getName(), v);
        }
    }
    
    public static Collection<SAPIVoice> list() {
        return availableVoices.values();
    }
    
    public static SAPIVoice get(String name) {
        return availableVoices.get(name);
    }
    
    public static SAPIVoice getDefault() {
        return defaultVoice;
    }
    
    private final ISpeechObjectToken token;
    private final String name;
    
    private SAPIVoice(ISpeechObjectToken token) {
        this.token = token;
        name = token.getAttribute("Name");
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void speak(String text) {
        iSpeechVoice.voice(token);
        iSpeechVoice.speak(text, SpeechVoiceSpeakFlags.SVSFDefault);
        iSpeechVoice.waitUntilDone(-1);
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof SAPIVoice)) { return false; }
        try {
            SAPIVoice that = (SAPIVoice)o;
            return this.name.equals(that.name);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.name.hashCode();
        return hash;
    }
    
    public void cleanUp() {
        iSpeechVoice.dispose();
        
    } 
}
