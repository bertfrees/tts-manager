package org.bertfrees.tts.mac;

import org.rococoa.Rococoa;
import org.rococoa.ID;
import org.rococoa.NSObject;
import org.rococoa.NSClass;
import org.rococoa.cocoa.foundation.NSArray;
import org.rococoa.cocoa.foundation.NSURL;
import org.rococoa.cocoa.foundation.NSError;
import org.rococoa.cocoa.foundation.NSDictionary;

public interface NSSpeechSynthesizer extends NSObject {

    final _Class CLASS = Rococoa.createClass("NSSpeechSynthesizer", _Class.class);

    interface _Class extends NSClass {

        NSArray availableVoices();

        NSDictionary attributesForVoice(String voiceIdentifier);

        String defaultVoice();

        boolean isAnyApplicationSpeaking();
    }

    NSSpeechSynthesizer initWithVoice(String voice);
    boolean usesFeedbackWindow();
    void setUsesFeedbackWindow(boolean use);
    String voice();
    void setVoice(String voice);
    float rate();
    void setRate(float rate);
    float volume();
    void setVolume(float volume);
    void addSpeechDictionary(NSDictionary dictionary);
    ID objectForProperty_error(String speechProperty, NSError error);
    boolean setObjectForProperty_error(ID object, String speechProperty, NSError error);
    ID delegate();
    void setDelegate(ID delegate);
    boolean isSpeaking();
    boolean startSpeakingString(String text);
    boolean startSpeakingString_toURL(String text, NSURL url);
    void continueSpeaking();
    void stopSpeaking();
    String phonemesFromText(String string);
}
