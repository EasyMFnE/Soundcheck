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
import net.easymfne.soundcheck.datatype.Coordinates;
import net.easymfne.soundcheck.datatype.PlayableSound;
import net.easymfne.soundcheck.datatype.RelativeCoordinates;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

/**
 * The class that handles the "/playsound" command for the plugin.
 * 
 * Subcommands: help, list
 * 
 * @author Eric Hildebrand
 */
public class PlaySoundCommand implements CommandExecutor, TabExecutor {
    
    /**
     * Private helper class for catching command syntax errors.
     */
    private class SoundError extends Exception {
        private static final long serialVersionUID = 3114509247909886492L;
        
        public SoundError(String message) {
            super(message);
        }
    }
    
    private Soundcheck plugin = null;
    private List<String> subcommands = null;
    
    /**
     * Initialize by getting a reference to the plugin instance and registering
     * this class to handle the '/playsound' command.
     * 
     * @param plugin
     *            Reference to Soundcheck plugin instance
     */
    public PlaySoundCommand(Soundcheck plugin) {
        this.plugin = plugin;
        subcommands = new ArrayList<String>();
        subcommands.add("help");
        subcommands.add("list");
        plugin.getCommand("playsound").setExecutor(this);
    }
    
    /**
     * Release the '/playsound' command from its ties to this class.
     */
    public void close() {
        subcommands.clear();
        subcommands = null;
        plugin.getCommand("playsound").setExecutor(null);
        plugin = null;
    }
    
    /**
     * This method handles user commands. Usage: "/playsound".
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            showHelp(sender);
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            sender.sendMessage(StringUtils.join(Sound.values(), ",  "));
            return true;
        }
        if (args.length > 0) {
            try {
                parseCommand(sender, args);
            } catch (SoundError e) {
                sender.sendMessage(ChatColor.RED + e.getMessage());
            }
            return true;
        }
        return false;
    }
    
    /**
     * Suggest tab-completions from lists of subcommands and sounds.
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
            for (Sound sound : Sound.values()) {
                if (StringUtil.startsWithIgnoreCase(sound.name(), args[0])) {
                    matches.add(sound.name());
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
     * @throws SoundError
     */
    @SuppressWarnings("deprecation")
    private void parseCommand(CommandSender sender, String... args)
            throws SoundError {
        Location senderLocation = (sender instanceof Player ? ((Player) sender)
                .getLocation()
                : (sender instanceof BlockCommandSender ? ((BlockCommandSender) sender)
                        .getBlock().getLocation() : null));
        Player player = null;
        PlayableSound sound = null;
        Coordinates coordinates = null;
        RelativeCoordinates relativeCoordinates = null;
        
        /* Iterate through arguments, matching all that we can */
        /* order of operations: player, sound, coords, relativeCoords */
        for (String arg : args) {
            if (plugin.getServer().getPlayerExact(arg) != null) {
                player = plugin.getServer().getPlayerExact(arg);
            } else if (PlayableSound.matches(arg)) {
                try {
                    sound = PlayableSound.parse(arg);
                } catch (NumberFormatException e) {
                    throw new SoundError("Failed to parse volume or pitch.");
                } catch (IllegalArgumentException e) {
                    throw new SoundError("Sound does not exist.");
                }
            } else if (Coordinates.matches(arg)) {
                try {
                    coordinates = Coordinates.parse(arg);
                } catch (NumberFormatException e) {
                    throw new SoundError("Failed to parse coordinates: " + arg);
                }
            } else if (RelativeCoordinates.matches(arg)) {
                try {
                    relativeCoordinates = RelativeCoordinates.parse(arg);
                } catch (NumberFormatException e) {
                    throw new SoundError(
                            "Failed to parse relative coordinates: " + arg);
                }
            } else {
                throw new SoundError("Unrecognized player: " + arg);
            }
        }
        
        playSound(sound, senderLocation, player, coordinates,
                relativeCoordinates);
    }
    
    /**
     * Check the passed objects and play the sound at the desired location.
     * 
     * @param sound
     *            Sound to play
     * @param sender
     *            User sending command
     * @param player
     *            Player specified
     * @param coordinates
     *            Coordinates specified
     * @param relative
     *            Relative coordinates specified
     * @throws SoundError
     */
    private void playSound(PlayableSound sound, Location sender, Player player,
            Coordinates coordinates, RelativeCoordinates relative)
            throws SoundError {
        /* @formatter:off */
        /*======================================================================
         * senderLocation, player, coordinates, and relativeCoordinates can each
         * be null or non-null individually. Thus there are 16 possible cases to
         * handle. Separating the relativeCoordinates for interior processing
         * cuts this in half, and we are left with the possibilities:
         * 
         *   1.) sender != null, player != null, coordinates != null
         *        (Play sound at coordinates in player's world) 
         *   2.) sender != null, player == null, coordinates != null
         *        (Play sound at coordinates in sender's world) 
         *   3.) sender != null, player != null, coordinates == null
         *        (Play sound at player's location) 
         *   4.) sender != null, player == null, coordinates == null
         *        (Play sound at sender's location) 
         *   5.) sender == null, player != null, coordinates != null
         *        (Play sound at coordinates in player's world) 
         *   6.) sender == null, player == null, coordinates != null
         *        (Do not play sound) 
         *   7.) sender == null, player != null, coordinates == null
         *        (Play sound at player's location) 
         *   8.) sender == null, player == null, coordinates == null
         *        (Do not play sound)
         * 
         * Combining cases that result in the same functionality, we get:
         *   (1 & 5), (2), (3 & 7), (4), (6 & 8) Resulting in 5 code blocks.
         *====================================================================== 
         */ /* @formatter:on */
        
        /* Handles cases 1 & 5: Play sound at coordinates in player's world */
        if (player != null && coordinates != null) {
            if (relative != null) {
                sound.play(relative.getLocationRelativeTo(coordinates
                        .getLocation(player.getWorld())));
            } else {
                sound.play(coordinates.getLocation(player.getWorld()));
            }
        }
        /* Handles case 2: Play sound at coordinates in sender's world */
        else if (sender != null && player == null && coordinates != null) {
            if (relative != null) {
                sound.play(relative.getLocationRelativeTo(coordinates
                        .getLocation(sender.getWorld())));
            } else {
                sound.play(coordinates.getLocation(sender.getWorld()));
            }
        }
        /* Handles cases 3 & 7: Play sound at player's location */
        else if (player != null && coordinates == null) {
            if (relative != null) {
                sound.play(relative.getLocationRelativeTo(player.getLocation()));
            } else {
                sound.play(player.getLocation());
            }
        }
        /* Handles case 4: Play sound at sender's location */
        else if (sender != null && player == null && coordinates == null) {
            if (relative != null) {
                sound.play(relative.getLocationRelativeTo(sender));
            } else {
                sound.play(sender);
            }
        }
        /* Handles cases 6 & 8: Do not play sound */
        else if (sender == null && player == null) {
            throw new SoundError("Console users must specify a player.");
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
                "  /playsound list - List available sounds",
                "  /playsound <SOUND:volume:pitch]> [~x,y,z]",
                "    - Play a sound relative to the user",
                "  /playsound <SOUND:volume:pitch]> <player> [~x,y,z]",
                "    - Play a sound relative to another player",
                "  /playsound <SOUND:volume:pitch]> <x,y,z>",
                "    - Play a sound at a specific location" });
    }
    
}
