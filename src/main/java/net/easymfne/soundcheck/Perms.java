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

import org.bukkit.permissions.Permissible;

/**
 * Utility class for checking user permissions.
 * 
 * @author Eric Hildebrand
 */
public class Perms {
    
    /**
     * @param p
     *            User
     * @return Whether user can use PlayEfx command
     */
    public static boolean canUsePlayEfx(Permissible p) {
        return p.hasPermission("soundcheck.command.playefx");
    }
    
    /**
     * @param p
     *            User
     * @return Whether user can use PlayFx command
     */
    public static boolean canUsePlayFx(Permissible p) {
        return p.hasPermission("soundcheck.command.playfx");
    }
    
    /**
     * @param p
     *            User
     * @return Whether user can use PlaySound command
     */
    public static boolean canUsePlaySound(Permissible p) {
        return p.hasPermission("soundcheck.command.playsound");
    }
    
    /**
     * @param p
     *            User
     * @return Whether user can use Sequence command
     */
    public static boolean canUseSequence(Permissible p) {
        return p.hasPermission("soundcheck.command.sequence");
    }
    
}
