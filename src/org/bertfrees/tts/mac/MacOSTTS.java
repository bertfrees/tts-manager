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

import org.bertfrees.tts.TTS;
import org.bertfrees.tts.Voice;

/**
 *
 * @author Bert Frees
 */
public class MacOSTTS extends TTS {

    private final String voiceParam;
    private final MacOSVoice voice;
    
    public MacOSTTS() {
        voiceParam = "";
        voice = MacOSVoice.getDefault();
    }
    
    public MacOSTTS(String voiceParam) {
        this.voiceParam = voiceParam;
        voice = updateVoice(voiceParam);
    }
    
    public String getVoiceParam() {
        return voiceParam;
    }
    
//    public void setVoiceParam(String voiceParam) {
//        if (this.voiceParam.equals(voiceParam)) { return; }
//        this.voiceParam = voiceParam;
//        voice = updateVoice();
//        notifyListeners();
//    }
    
    public static MacOSVoice updateVoice(String voiceParam) {
        MacOSVoice v = MacOSVoice.get(voiceParam);
        if (v != null) {
            return v;
        } else {
            return MacOSVoice.getDefault();
        }
    }
    
    @Override
    public Voice getVoice() {
        return voice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof MacOSTTS)) { return false; }
        try {
            MacOSTTS that = (MacOSTTS)o;
            return this.voiceParam.equals(that.voiceParam);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.voiceParam != null ? this.voiceParam.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "MacOS TTS [@voice='" + voiceParam + "']";
    }
}
