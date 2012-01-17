package org.bertfrees.tts;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author Bert Frees
 */
public class TTSManager extends HashMap<Locale,TTS> implements Map<Locale,TTS> {
    
    private List<TTSManagerListener> listeners = new ArrayList<TTSManagerListener>();
    
    public TTSManager() {
        super();
    }
    
    public void addListener(TTSManagerListener listener) {
        listeners.add(listener);
    }
    
    @Override
    public TTS put(Locale locale, TTS tts) {
        TTS prevTTS = null;
        if (containsKey(locale)) {
            prevTTS = remove(locale);
        }
        super.put(locale, tts);
        for (TTSManagerListener listener : listeners) {
            listener.ttsAdded(this, locale);
        }
        return prevTTS;
    }
    
    @Override
    public TTS remove(Object object) {
        Locale locale = (Locale)object;
        TTS tts = super.remove(locale);
        if (tts != null) {
            for (TTSManagerListener listener : listeners) {
                listener.ttsRemoved(this, locale);
            }
        }
        return tts;
    }
}
