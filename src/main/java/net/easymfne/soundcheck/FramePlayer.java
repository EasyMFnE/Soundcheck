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

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Extension of BukkitRunnable used for scheduling a sequence's frames to play.
 * 
 * @author Eric Hildebrand
 */
public class FramePlayer extends BukkitRunnable {
    
    private Frame frame;
    private Location location;
    
    /**
     * @param frame
     *            Frame to play
     * @param location
     *            Where to play frame
     */
    public FramePlayer(Frame frame, Location location) {
        this.frame = frame;
        this.location = location;
    }
    
    @Override
    public void run() {
        frame.play(location);
    }
    
}
