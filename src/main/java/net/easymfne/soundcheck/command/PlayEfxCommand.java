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

import net.easymfne.soundcheck.Soundcheck;
import net.easymfne.soundcheck.datatype.PlayableEntityEffect;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

/**
 * The class that handles the "/playefx" command for the plugin.
 * 
 * Subcommands: help, list
 * 
 * @author Eric Hildebrand
 */
public class PlayEfxCommand implements CommandExecutor, TabExecutor {
    
    /**
     * Private helper class for catching command syntax errors.
     */
    private class EntityEffectError extends Exception {
        private static final long serialVersionUID = -270732660189059767L;
        
        public EntityEffectError(String message) {
            super(message);
        }
    }
    
    private Soundcheck plugin = null;
    private List<String> subcommands = null;
    
    /**
     * Initialize by getting a reference to the plugin instance and registering
     * this class to handle the '/playefx' command.
     * 
     * @param plugin
     *            Reference to Soundcheck plugin instance
     */
    public PlayEfxCommand(Soundcheck plugin) {
        this.plugin = plugin;
        subcommands = new ArrayList<String>();
        subcommands.add("help");
        subcommands.add("list");
        plugin.getCommand("playefx").setExecutor(this);
    }
    
    /**
     * Release the '/playefx' command from its ties to this class.
     */
    public void close() {
        subcommands.clear();
        subcommands = null;
        plugin.getCommand("playefx").setExecutor(null);
        plugin = null;
    }
    
    /**
     * This method handles user commands. Usage: "/playefx".
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            showHelp(sender);
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            sender.sendMessage(StringUtils.join(Effect.values(), ",  "));
            return true;
        }
        if (args.length > 0) {
            try {
                parseCommand(sender, args);
            } catch (EntityEffectError e) {
                sender.sendMessage(ChatColor.RED + e.getMessage());
            }
            return true;
        }
        return false;
    }
    
    /**
     * Suggest tab-completions from lists of subcommands and effects.
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
            for (EntityEffect effect : EntityEffect.values()) {
                if (StringUtil.startsWithIgnoreCase(effect.name(), args[0])) {
                    matches.add(effect.name());
                }
            }
            if (!matches.isEmpty()) {
                return matches;
            }
        }
        return null;
    }
    
    /**
     * Attempt to parse a user's command arguments.
     * 
     * TODO: Remove deprecation suppression when Bukkit is at Minecraft 1.8.
     * 
     * @param sender
     *            User
     * @param args
     *            Arguments
     * @throws EffectError
     */
    @SuppressWarnings("deprecation")
    private void parseCommand(CommandSender sender, String... args)
            throws EntityEffectError {
        Player senderPlayer = (sender instanceof Player ? ((Player) sender)
                : null);
        Player player = null;
        PlayableEntityEffect effect = null;
        
        if (args.length > 0) {
            try {
                effect = PlayableEntityEffect.parse(args[0]);
            } catch (NullPointerException e) {
                throw new EntityEffectError("You must specify an effect.");
            } catch (IllegalArgumentException e) {
                throw new EntityEffectError("Unknown entity effect: " + args[0]);
            }
        }
        if (args.length > 1) {
            player = plugin.getServer().getPlayerExact(args[1]);
            if (player == null) {
                throw new EntityEffectError("Unknown player: " + args[1]);
            }
        }
        playEffect(effect, senderPlayer, player);
    }
    
    /**
     * Play the EntityEffect at the desired location, pending valid references
     * to the command sender and referenced player.
     * 
     * @param effect
     *            PlayableEntityEffect to play
     * @param sender
     *            Sender's player instance
     * @param player
     *            Referenced player
     * @throws EntityEffectError
     */
    private void playEffect(PlayableEntityEffect effect, Player sender,
            Player player) throws EntityEffectError {
        if (player != null) {
            effect.play(player);
        } else if (sender != null) {
            effect.play(sender);
        } else {
            throw new EntityEffectError("Console users must specify a player.");
        }
    }
    
    /**
     * Show information about the proper usage of this command.
     * 
     * @param sender
     *            User
     */
    private void showHelp(CommandSender sender) {
        sender.sendMessage(new String[] { "Usage:",
                "  /playefx list - List available effects",
                "  /playefx <EFFECT> [player]",
                "    - Play an entity effect at a player" });
    }
    
}
