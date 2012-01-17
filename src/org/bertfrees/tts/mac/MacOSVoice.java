package org.bertfrees.tts.mac;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

import org.rococoa.Rococoa;
import org.rococoa.cocoa.foundation.NSArray;
import org.rococoa.cocoa.foundation.NSAutoreleasePool;

import org.bertfrees.tts.Voice;

public class MacOSVoice extends Voice {
    
    private final static boolean IS_MAC = System.getProperty("os.name").toLowerCase().contains("mac os");
    static { if (!IS_MAC) { throw new RuntimeException("Only available on Mac"); }}
    
    private static final MacOSVoice defaultVoice;
    private static final Map<String,MacOSVoice> availableVoices;
    
    static {
        NSAutoreleasePool pool = NSAutoreleasePool.new_();
        NSSpeechSynthesizer synthesizer = Rococoa.create("NSSpeechSynthesizer", NSSpeechSynthesizer.class);
        defaultVoice = new MacOSVoice(synthesizer.voice());
        availableVoices = new HashMap<String,MacOSVoice>();
        NSArray allVoices = NSSpeechSynthesizer.CLASS.availableVoices();
        for (int i=0; i<allVoices.count(); i++) {
            MacOSVoice v = new MacOSVoice(allVoices.objectAtIndex(i).toString());
            availableVoices.put(v.getName(), v);
        }
        pool.release();
    }
    
    public static Collection<MacOSVoice> list() {
        return availableVoices.values();
    }
    
    public static MacOSVoice get(String name) {
        return availableVoices.get(name);
    }
    
    public static MacOSVoice getDefault() {
        return defaultVoice;
    }

    private final String identifier;
    private final String name;
    
    private MacOSVoice(String identifier) {
        this.identifier = identifier;
        name = identifier.substring(identifier.lastIndexOf(".")+1);
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void speak(String text) {
        try {
            Runtime.getRuntime().exec(new String[] {"/usr/bin/say", "-v", name, text}).waitFor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof MacOSVoice)) { return false; }
        try {
            MacOSVoice that = (MacOSVoice)o;
            return this.identifier.equals(that.identifier);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.identifier != null ? this.identifier.hashCode() : 0);
        return hash;
    }
}
