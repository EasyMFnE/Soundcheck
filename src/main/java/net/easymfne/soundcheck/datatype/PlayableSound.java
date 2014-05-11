/*
 * This file is part of the Soundcheck plugin by EasyMFnE.
 * 
 * Soundcheck is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or any later version.
 * 
 * Soundcheck is distributed in the hope that it will be useful, but without any
 * warranty; without even the implied warranty of merchantability or fitness for
 * a particular purpose. See the GNU General Public License for details.
 * 
 * You should have received a copy of the GNU General Public License v3 along
 * with Soundcheck. If not, see <http://www.gnu.org/licenses/>.
 */
package net.easymfne.soundcheck.datatype;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.Sound;

/**
 * Class that can detect, parse, and store all components of a Sound in the
 * format "Sound sound,float volume,float pitch".
 * 
 * @author Eric Hildebrand
 */
public class PlayableSound {
    
    /* Regular expressions for matching Strings */
    private static Pattern name = Pattern.compile("[A-Z0-9_]+");
    private static Pattern number = Pattern.compile("[0-9]+\\.?[0-9]*");
    private static Pattern pattern = Pattern.compile("^(" + name.pattern()
            + "):(" + number.pattern() + "):(" + number.pattern() + ")$");
    
    /**
     * @param string
     *            Input string
     * @return Whether string matches the PlayableSound format
     */
    public static boolean matches(String string) {
        return pattern.matcher(string).matches();
    }
    
    /**
     * Read PlayableSound data from a string and return a new PlayableSound
     * using the data. Returns null when the input does not match the
     * appropriate format.
     * 
     * @param string
     *            Input string
     * @return read PlayableSound
     * @throws IllegalArgumentException
     * @throws NumberFormatException
     */
    public static PlayableSound parse(String string)
            throws IllegalArgumentException, NumberFormatException {
        Matcher matcher = pattern.matcher(string);
        if (!matcher.matches()) {
            return null;
        }
        if (matcher.group(3) != null) {
            return new PlayableSound(Sound.valueOf(matcher.group(1)),
                    Float.parseFloat(matcher.group(2)),
                    Float.parseFloat(matcher.group(3)));
        }
        if (matcher.group(2) != null) {
            return new PlayableSound(Sound.valueOf(matcher.group(1)),
                    Float.parseFloat(matcher.group(2)));
        }
        return new PlayableSound(Sound.valueOf(matcher.group(1)));
    }
    
    private Sound sound;
    private float volume, pitch;
    
    /**
     * Construct PlayableSound with volume=1.0 and pitch=1.0.
     * 
     * @param sound
     *            Sound
     */
    public PlayableSound(Sound sound) {
        this.sound = sound;
        volume = 1f;
        pitch = 1f;
    }
    
    /**
     * Construct PlayableSound with pitch=1.0.
     * 
     * @param sound
     *            Sound
     * @param volume
     *            Volume
     */
    public PlayableSound(Sound sound, float volume) {
        this.sound = sound;
        this.volume = volume;
        pitch = 1f;
    }
    
    /**
     * @param sound
     *            Sound
     * @param volume
     *            Volume
     * @param pitch
     *            Pitch
     */
    public PlayableSound(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }
    
    /**
     * Play the sound at a specified location.
     * 
     * @param location
     *            Location
     */
    public void play(Location location) {
        location.getWorld().playSound(location, sound, volume, pitch);
    }
    
}
