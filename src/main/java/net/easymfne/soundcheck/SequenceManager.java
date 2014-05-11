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
package net.easymfne.soundcheck;

import net.easymfne.soundcheck.datatype.Frame;
import net.easymfne.soundcheck.datatype.Sequence;

import org.bukkit.Location;

/**
 * This class is used for scheduling all of a sequence's frames using
 * BukkitScheduler.
 * 
 * @author Eric Hildebrand
 */
public class SequenceManager {
    
    private static Soundcheck plugin;
    
    /**
     * Clear reference to Soundcheck plugin.
     */
    public static void close() {
        plugin = null;
    }
    
    /**
     * Instantiate the reference to the Soundcheck plugin.
     * 
     * @param plugin
     *            Soundcheck plugin
     */
    public static void init(Soundcheck plugin) {
        SequenceManager.plugin = plugin;
    }
    
    /**
     * Iterate through each frame of a sequence and schedule it with its
     * specified delay timing.
     * 
     * @param sequence
     *            Sequence to schedule
     * @param location
     *            Location to play sequence
     */
    public static void queueSequence(Sequence sequence, Location location) {
        for (Frame frame : sequence.getFrameList()) {
            plugin.getServer()
                    .getScheduler()
                    .scheduleSyncDelayedTask(plugin,
                            new FramePlayer(frame, location), frame.getTime());
        }
    }
    
}
