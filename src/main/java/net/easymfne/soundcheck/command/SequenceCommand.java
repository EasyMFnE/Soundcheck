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
import net.easymfne.soundcheck.datatype.RelativeCoordinates;
import net.easymfne.soundcheck.datatype.Sequence;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

/**
 * The class that handles the "/sequence" command for the plugin.
 * 
 * Subcommands: help, list
 * 
 * @author Eric Hildebrand
 */
public class SequenceCommand implements CommandExecutor, TabExecutor {
    
    /**
     * Private helper class for catching command syntax errors.
     */
    private class SequenceError extends Exception {
        private static final long serialVersionUID = 5281701902087274671L;
        
        public SequenceError(String message) {
            super(message);
        }
    }
    
    private Soundcheck plugin = null;
    private List<String> subcommands = null;
    
    /**
     * Initialize by getting a reference to the plugin instance and registering
     * this class to handle the '/sequence' command.
     * 
     * @param plugin
     *            Reference to Soundcheck plugin instance
     */
    public SequenceCommand(Soundcheck plugin) {
        this.plugin = plugin;
        subcommands = new ArrayList<String>();
        subcommands.add("help");
        subcommands.add("list");
        plugin.getCommand("sequence").setExecutor(this);
    }
    
    /**
     * Release the '/sequence' command from its ties to this class.
     */
    public void close() {
        subcommands.clear();
        subcommands = null;
        plugin.getCommand("sequence").setExecutor(null);
        plugin = null;
    }
    
    /**
     * This method handles user commands. Usage: "/sequence".
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            showHelp(sender);
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            sender.sendMessage(StringUtils.join(plugin.getConfigHelper()
                    .getSequences().getKeys(false), ",  "));
            return true;
        }
        if (args.length > 0) {
            try {
                parseCommand(sender, args);
            } catch (SequenceError e) {
                sender.sendMessage(ChatColor.RED + e.getMessage());
            }
            return true;
        }
        return false;
    }
    
    /**
     * Suggest tab-completions from lists of subcommands and sequences.
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
            for (String sequence : plugin.getConfigHelper().getSequences()
                    .getKeys(false)) {
                if (StringUtil.startsWithIgnoreCase(sequence, args[0])) {
                    matches.add(sequence);
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
     * @throws SequenceError
     */
    @SuppressWarnings("deprecation")
    private void parseCommand(CommandSender sender, String... args)
            throws SequenceError {
        Location senderLocation = (sender instanceof Player ? ((Player) sender)
                .getLocation()
                : (sender instanceof BlockCommandSender ? ((BlockCommandSender) sender)
                        .getBlock().getLocation() : null));
        Sequence sequence = null;
        try {
            sequence = Sequence.parse(plugin.getConfigHelper().getSequences()
                    .getMapList(args[0]));
        } catch (NullPointerException e) {
            throw new SequenceError("Unknown sequence: " + args[0]);
        } catch (IllegalArgumentException e) {
            throw new SequenceError("Error in sequence: " + e.getMessage());
        }
        
        Player player = null;
        Coordinates coordinates = null;
        RelativeCoordinates relativeCoordinates = null;
        
        /* Iterate through arguments, matching all that we can */
        /* order of operations: player, coords, relativeCoords */
        for (int i = 1; i < args.length; i++) {
            if (plugin.getServer().getPlayerExact(args[i]) != null) {
                player = plugin.getServer().getPlayerExact(args[i]);
            } else if (Coordinates.matches(args[i])) {
                try {
                    coordinates = Coordinates.parse(args[i]);
                } catch (NumberFormatException e) {
                    throw new SequenceError("Failed to parse coordinates: "
                            + args[i]);
                }
            } else if (RelativeCoordinates.matches(args[i])) {
                try {
                    relativeCoordinates = RelativeCoordinates.parse(args[i]);
                } catch (NumberFormatException e) {
                    throw new SequenceError(
                            "Failed to parse relative coordinates: " + args[i]);
                }
            } else {
                throw new SequenceError("Unrecognized player: " + args[i]);
            }
        }
        
        playSequence(sequence, senderLocation, player, coordinates,
                relativeCoordinates);
    }
    
    /**
     * Check the passed objects and play the sequence at the desired location.
     * 
     * @param sequence
     *            Sequence to play
     * @param sender
     *            User sending command
     * @param player
     *            Player specified
     * @param coordinates
     *            Coordinates specified
     * @param relative
     *            Relative coordinates specified
     * @throws SequenceError
     */
    private void playSequence(Sequence sequence, Location sender,
            Player player, Coordinates coordinates, RelativeCoordinates relative)
            throws SequenceError {
        /* @formatter:off */
        /*======================================================================
         * senderLocation, player, coordinates, and relativeCoordinates can each
         * be null or non-null individually. Thus there are 16 possible cases to
         * handle. Separating the relativeCoordinates for interior processing
         * cuts this in half, and we are left with the possibilities:
         * 
         *   1.) sender != null, player != null, coordinates != null
         *        (Play sequence at coordinates in player's world) 
         *   2.) sender != null, player == null, coordinates != null
         *        (Play sequence at coordinates in sender's world) 
         *   3.) sender != null, player != null, coordinates == null
         *        (Play sequence at player's location) 
         *   4.) sender != null, player == null, coordinates == null
         *        (Play sequence at sender's location) 
         *   5.) sender == null, player != null, coordinates != null
         *        (Play sequence at coordinates in player's world) 
         *   6.) sender == null, player == null, coordinates != null
         *        (Do not play sequence) 
         *   7.) sender == null, player != null, coordinates == null
         *        (Play sequence at player's location) 
         *   8.) sender == null, player == null, coordinates == null
         *        (Do not play sequence)
         * 
         * Combining cases that result in the same functionality, we get:
         *   (1 & 5), (2), (3 & 7), (4), (6 & 8) Resulting in 5 code blocks.
         *====================================================================== 
         */ /* @formatter:on */
        
        /* Handles cases 1 & 5: Play sequence at coordinates in player's world */
        if (player != null && coordinates != null) {
            if (relative != null) {
                sequence.play(relative.getLocationRelativeTo(coordinates
                        .getLocation(player.getWorld())));
            } else {
                sequence.play(coordinates.getLocation(player.getWorld()));
            }
        }
        /* Handles case 2: Play sequence at coordinates in sender's world */
        else if (sender != null && player == null && coordinates != null) {
            if (relative != null) {
                sequence.play(relative.getLocationRelativeTo(coordinates
                        .getLocation(sender.getWorld())));
            } else {
                sequence.play(coordinates.getLocation(sender.getWorld()));
            }
        }
        /* Handles cases 3 & 7: Play sequence at player's location */
        else if (player != null && coordinates == null) {
            if (relative != null) {
                sequence.play(relative.getLocationRelativeTo(player
                        .getLocation()));
            } else {
                sequence.play(player.getLocation());
            }
        }
        /* Handles case 4: Play sequence at sender's location */
        else if (sender != null && player == null && coordinates == null) {
            if (relative != null) {
                sequence.play(relative.getLocationRelativeTo(sender));
            } else {
                sequence.play(sender);
            }
        }
        /* Handles cases 6 & 8: Do not play sequence */
        else if (sender == null && player == null) {
            throw new SequenceError("Console users must specify a player.");
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
                "  /sequence list - List available sequences",
                "  /sequence <sequence> [~x,y,z]",
                "    - Play a sequence relative to the user",
                "  /sequence <sequence> <player> [~x,y,z]",
                "    - Play a sequence relative to another player",
                "  /sequence <sequence> <x,y,z>",
                "    - Play a sequence at a specific location" });
    }
    
}
