package org.bertfrees.tts;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bert Frees
 */
public abstract class TTS {
    
    private List<TTSListener> listeners = new ArrayList<TTSListener>();
        
    public void addListener(TTSListener listener) {
        listeners.add(listener);
    }
    
    protected void notifyListeners() {
        for (TTSListener listener : listeners) {
            listener.ttsUpdated(this);
        }
    }
    
    public abstract Voice getVoice();
    
    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();
    
    @Override
    public abstract String toString();
    
}
