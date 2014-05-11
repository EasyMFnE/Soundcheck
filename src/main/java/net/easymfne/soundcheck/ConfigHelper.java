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

import org.bukkit.configuration.ConfigurationSection;

/**
 * Configuration helper class, with methods for accessing the configuration.
 * 
 * @author Eric Hildebrand
 */
public class ConfigHelper {
    
    private Soundcheck plugin = null;
    
    /**
     * Instantiate the class and give it a reference back to the plugin itself.
     * 
     * @param plugin
     *            The Soundcheck plugin
     */
    public ConfigHelper(Soundcheck plugin) {
        this.plugin = plugin;
    }
    
    /**
     * @return ConfigurationSection "sequence" from 'config.yml'.
     */
    public ConfigurationSection getSequences() {
        return plugin.getConfig().getConfigurationSection("sequence");
    }
    
}
