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
package net.easymfne.soundcheck.command;

import java.util.ArrayList;
import java.util.List;

import net.easymfne.soundcheck.Perms;
import net.easymfne.soundcheck.Soundcheck;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;

/**
 * The class that handles the "/soundcheck" command for the plugin.
 * 
 * Subcommands: help, reload
 * 
 * @author Eric Hildebrand
 */
public class SoundcheckCommand implements CommandExecutor, TabExecutor {
    
    private Soundcheck plugin = null;
    private List<String> subcommands = null;
    
    /**
     * Instantiate by getting a reference to the plugin instance and registering
     * this class to handle the '/soundcheck' command.
     * 
     * @param plugin
     *            Reference to Soundcheck plugin instance
     */
    public SoundcheckCommand(Soundcheck plugin) {
        this.plugin = plugin;
        subcommands = new ArrayList<String>();
        subcommands.add("help");
        subcommands.add("reload");
        plugin.getCommand("soundcheck").setExecutor(this);
    }
    
    /**
     * Release the '/soundcheck' command from its ties to this class.
     */
    public void close() {
        subcommands.clear();
        subcommands = null;
        plugin.getCommand("soundcheck").setExecutor(null);
        plugin = null;
    }
    
    /**
     * This method handles user commands. Usage: "/soundcheck".
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            showHelp(sender);
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reload();
            sender.sendMessage("Configuration reloaded from disk.");
            return true;
        }
        return false;
    }
    
    /**
     * Suggest tab-completions from list of subcommands.
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command,
            String alias, String[] args) {
        if (args.length == 1) {
            List<String> matches = new ArrayList<String>();
            for (String subcommand : subcommands) {
                if (StringUtil.startsWithIgnoreCase(subcommand, args[0])) {
                    matches.add(subcommand);
                }
            }
            if (!matches.isEmpty()) {
                return matches;
            }
        }
        return null;
    }
    
    /**
     * Show information about the proper usage of this command.
     * 
     * @param sender
     *            User
     */
    private void showHelp(CommandSender sender) {
        sender.sendMessage("Commands:");
        sender.sendMessage("  /soundcheck - Plugin administration command");
        if (Perms.canUsePlaySound(sender)) {
            sender.sendMessage("  /playsound - Play an individual sounds");
        }
        if (Perms.canUsePlayFx(sender)) {
            sender.sendMessage("  /playfx - Play an individual effects");
        }
        if (Perms.canUsePlayEfx(sender)) {
            sender.sendMessage("  /playefx - Play an individual entity-effects");
        }
        if (Perms.canUseSequence(sender)) {
            sender.sendMessage("  /sequence - Play configured sound/effect sequences");
        }
    }
    
}
