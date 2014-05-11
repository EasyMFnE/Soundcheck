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

import org.bukkit.EntityEffect;
import org.bukkit.entity.Entity;

/**
 * Class that can detect, parse, and store all components of an EntityEffect in
 * the format "Effect effect,int data,int radius".
 * 
 * @author Eric Hildebrand
 */
public class PlayableEntityEffect {
    
    /* Regular expressions for matching Strings */
    private static Pattern name = Pattern.compile("[A-Z0-9_]+");
    private static Pattern pattern = Pattern.compile("^(" + name.pattern()
            + ")$");
    
    /**
     * @param string
     *            Input string
     * @return Whether string matches the PlayableEntityEffect format
     */
    public static boolean matches(String string) {
        return pattern.matcher(string).matches();
    }
    
    /**
     * Read PlayableEntityEffect data from a string and return a new
     * PlayableEntityEffect using the data.
     * 
     * @param string
     *            Input string
     * @return read PlayableSound
     * @throws IllegalArgumentException
     */
    public static PlayableEntityEffect parse(String string)
            throws IllegalArgumentException {
        Matcher matcher = pattern.matcher(string);
        return new PlayableEntityEffect(EntityEffect.valueOf(matcher.group(1)));
    }
    
    private EntityEffect entityEffect;
    
    public PlayableEntityEffect(EntityEffect entityEffect) {
        this.entityEffect = entityEffect;
    }
    
    /**
     * Play the entity effect at a specified entity.
     * 
     * @param entity
     *            Entity
     */
    public void play(Entity entity) {
        entity.playEffect(entityEffect);
    }
    
}
