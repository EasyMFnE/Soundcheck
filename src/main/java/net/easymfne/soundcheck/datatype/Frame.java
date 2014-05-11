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

import org.bukkit.Location;

/**
 * Representation of a single moment in a sequence, and the PlayableSound and/or
 * PlayableEffect it should play.
 * 
 * @author Eric Hildebrand *
 */
public class Frame {
    
    private int time;
    private PlayableSound sound;
    private PlayableEffect effect;
    
    public Frame(int time, PlayableSound sound, PlayableEffect effect) {
        this.time = time;
        this.sound = sound;
        this.effect = effect;
    }
    
    /**
     * @return Time offset from start of Sequence (in ticks)
     */
    public int getTime() {
        return time;
    }
    
    /**
     * Play defined PlayableSound and/or PlayableEffect at the specified
     * location.
     * 
     * @param location
     *            Location
     */
    public void play(Location location) {
        if (sound != null) {
            sound.play(location);
        }
        if (effect != null) {
            effect.play(location);
        }
    }
    
}
