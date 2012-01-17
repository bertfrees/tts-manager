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

/**
 *
 * @author Bert Frees
 */
public abstract class Voice {
        
    public abstract String getName();
    
    public abstract void speak(String text);
    
    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();
    
    @Override
    public abstract String toString();
}
