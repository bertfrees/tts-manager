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
