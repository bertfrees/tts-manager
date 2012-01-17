package org.bertfrees.tts;

/**
 *
 * @author Bert Frees
 */
public class TTSBuilderConfigurationException extends Exception {
    
    public TTSBuilderConfigurationException(String message) {
        super("Corrupt TTSBuilder configuration file: " + message);
    }
}
