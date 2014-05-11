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

import org.bukkit.Effect;
import org.bukkit.Location;

/**
 * Class that can detect, parse, and store all components of an Effect in the
 * format "Effect effect,int data,int radius".
 * 
 * @author Eric Hildebrand
 */
public class PlayableEffect {
    
    private Effect effect;
    private int data, radius;
    
    /* Regular expressions for matching Strings */
    private static Pattern name = Pattern.compile("[A-Z0-9_]+");
    private static Pattern number = Pattern.compile("[0-9]+");
    private static Pattern pattern = Pattern.compile("^(" + name.pattern()
            + ")(?::(" + number.pattern() + ")(?::(" + number.pattern()
            + ")))$");
    
    /**
     * @param string
     *            Input string
     * @return Whether string matches the PlayableEffect format
     */
    public static boolean matches(String string) {
        return pattern.matcher(string).matches();
    }
    
    /**
     * Read PlayableEffect data from a string and return a new PlayableEffect
     * using the data. Returns null when the input does not match the
     * appropriate format.
     * 
     * @param string
     *            Input string
     * @return read PlayableEffect
     * @throws IllegalArgumentException
     * @throws NumberFormatException
     */
    public static PlayableEffect parse(String string)
            throws IllegalArgumentException, NumberFormatException {
        Matcher matcher = pattern.matcher(string);
        if (!matcher.matches()) {
            return null;
        }
        if (matcher.group(3) != null) {
            return new PlayableEffect(Effect.valueOf(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3)));
        }
        if (matcher.group(2) != null) {
            return new PlayableEffect(Effect.valueOf(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)));
        }
        return new PlayableEffect(Effect.valueOf(matcher.group(1)));
    }
    
    /**
     * Construct PlayableEffect with data=1 and radius=-1.
     * 
     * @param effect
     *            Effect
     */
    public PlayableEffect(Effect effect) {
        this.effect = effect;
        data = 1;
        radius = -1;
    }
    
    /**
     * Construct PlayableEffect with radius=-1.
     * 
     * @param effect
     *            Effect
     * @param data
     *            Effect's data byte
     */
    public PlayableEffect(Effect effect, int data) {
        this.effect = effect;
        this.data = data;
        radius = -1;
    }
    
    /**
     * @param effect
     *            Effect
     * @param data
     *            Effect's data byte
     * @param radius
     *            Effect's radius
     */
    public PlayableEffect(Effect effect, int data, int radius) {
        this.effect = effect;
        this.data = data;
        this.radius = radius;
    }
    
    /**
     * Play the effect at a specified location.
     * 
     * @param location
     *            Location
     */
    public void play(Location location) {
        if (radius < 0) {
            location.getWorld().playEffect(location, effect, data);
        } else {
            location.getWorld().playEffect(location, effect, data, radius);
        }
    }
    
}
