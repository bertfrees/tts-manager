/**
 *  Copyright (c) 2012 Bert Frees <bertfrees@gmail.com>
 * 
 *  This file is part of tts-manager <https://github.com/bertfrees/tts-manager>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.bertfrees.tts.mac;

import org.rococoa.Rococoa;
import org.rococoa.ID;
import org.rococoa.NSObject;
import org.rococoa.NSClass;
import org.rococoa.cocoa.foundation.NSArray;
import org.rococoa.cocoa.foundation.NSURL;
import org.rococoa.cocoa.foundation.NSError;
import org.rococoa.cocoa.foundation.NSDictionary;

/**
 *
 * @author Bert Frees
 */
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
