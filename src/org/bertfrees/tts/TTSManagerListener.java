package org.bertfrees.tts;

import java.util.Locale;

/**
 *
 * @author Bert Frees
 */
public interface TTSManagerListener {
    
    public void ttsAdded(TTSManager manager, Locale locale);
    
    public void ttsRemoved(TTSManager manager, Locale locale);
    
}
