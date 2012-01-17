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
