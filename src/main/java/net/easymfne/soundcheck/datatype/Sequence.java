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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.easymfne.soundcheck.SequenceManager;

import org.bukkit.Location;

/**
 * Class that can parse and store all components of a Sequence, given its
 * configuration.
 * 
 * @author Eric Hildebrand
 */
public class Sequence {
    
    /**
     * Parse a sequence and its frames from the values of its configuration
     * node.
     * 
     * @param list
     *            List of ConfigurationSections in Map form
     * @return represented Sequence
     */
    public static Sequence parse(List<Map<?, ?>> list) {
        if (list == null || list.isEmpty()) {
            throw new NullPointerException();
        }
        List<Frame> frameList = new ArrayList<Frame>();
        
        for (Map<?, ?> frame : list) {
            int time = (Integer) frame.get("time");
            PlayableSound sound = (frame.containsKey("sound") ? PlayableSound
                    .parse((String) frame.get("sound")) : null);
            PlayableEffect effect = (frame.containsKey("effect") ? PlayableEffect
                    .parse((String) frame.get("effect")) : null);
            frameList.add(new Frame(time, sound, effect));
        }
        return new Sequence(frameList);
    }
    
    private List<Frame> frameList;
    
    /**
     * Construct with given list of Frames.
     * 
     * @param frameList
     *            Frames
     */
    public Sequence(List<Frame> frameList) {
        this.frameList = frameList;
    }
    
    /**
     * @return List of Frames for the sequence
     */
    public List<Frame> getFrameList() {
        return frameList;
    }
    
    /**
     * Queue the sequence to be played at a specified location.
     * 
     * @param location
     *            Location
     */
    public void play(Location location) {
        SequenceManager.queueSequence(this, location);
    }
    
}
