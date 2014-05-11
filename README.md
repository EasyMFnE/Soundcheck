<center>![Soundcheck](http://www.easymfne.net/images/soundcheck.png)</center>

<center>[Source](https://github.com/EasyMFnE/Soundcheck) |
[Change Log](https://github.com/EasyMFnE/Soundcheck/blob/master/CHANGES.log) |
[Feature Request](https://github.com/EasyMFnE/Soundcheck/issues) |
[Bug Report](https://github.com/EasyMFnE/Soundcheck/issues) |
[Donate](https://www.paypal.com/cgi-bin/webscr?hosted_button_id=457RX2KYUDY5G&item_name=Soundcheck&cmd=_s-xclick)</center>

<center>**Latest Release:** v1.0 for Bukkit 1.7+</center>

## About ##

Soundcheck is designed to allow players, console users, and command blocks to trigger sounds and effects in-game using commands.  These can be played at player locations or specified coordinates, and can be offset in any direction.  The plugin also allows administrators to configure 'sequences' of sounds and effects.

## Features ##

Users of the Soundcheck plugin can:

* Play sounds at their own location or another specified location
* Play effects at their own location or another specified location
* Play entity effects (e.g. player Hurt effect) for themselves or a specified player
* Play predefined sequences of sounds and effects at their own location or another specified location.

## Installation ##

1. Download Souncheck jar file.
2. Move/copy to your server's `plugins` folder.
3. Restart your server.
4. [**Optional**] Grant specific user permissions (see below).

## Permissions ##

Soundcheck has six permission nodes:

* `soundcheck.all` - Grants all command permission nodes. (Default: `op`)
* `soundcheck.command.soundcheck` - Allows access to `/soundcheck` command. (Default: `false`)
* `soundcheck.command.playsound` - Allows access to `/playsound` command. (Default: `false`)
* `soundcheck.command.playfx` - Allows access to `/playfx` command. (Default: `false`)
* `soundcheck.command.playefx` - Allows access to `/playefx` command. (Default: `false`)
* `soundcheck.command.sequence` - Allows access to `/sequence` command. (Default: `false`)


## Commands ##

DeadHorses has five commands, `/deadhorses` (Alias `/dh`)

* `/soundcheck`
    * `/soundcheck help` - Show available commands.
    * `/soundcheck reload` - Reload configuration from disk.
* `/playsound <sound> [location]...` - Play a specific sound.
* `/playfx <effect> [location]...` - Play a specific effect.
* `/playefx <entity_effect> [player]` - Play a specific entity effect.
* `/sequence <sequence> [location]...` - Play a defined sequence.

The above commands, excluding `/soundcheck` each also accept the subcommands:

* `/<command> help` - Show command usage information.
* `/<command> list` - Show available sounds/effects/sequences.

### Sounds ###

Sounds are in the format "`SOUND_NAME:volume:pitch`".

* Sound names are available using the list subcommand or tab-completion.
* Volume is a non-negative number. `0.0` is silent, `1.0` is normal volume.
* Pitch is a number (`0.5-2.0`). `1.0` is normal, `0.5` is very low, `2.0` is very high.

### Effects ###

Effects are in the format "`EFFECT_NAME:data:radius`".

* Effect names are available using the list subcommand or tab-completion.
* Data is an integer (default: 1).  Used for effects that require extra data.
     * (example: STEP_SOUND:**35**:100 plays the effect using Wool (block #35).
* Radius is an integer used for determining how far away players can see the effect.

### Locations ###

If no location is specified, it will assume the user's location. Locations can be defined using any combination of the following parameters:

* `<player_name>` - Player: Will get the player's location
* `<x,y,z>` - Coordinates: Will get the location of the coordinates (e.g. '`0,64.5,0`')
* `~<x,y,z>` - Offset: Will offset the location by the coordinates (e.g. '`~0,-4,0`')

**Notes:**

* If both Player and Coordinates are specified, the location will be at the coordinates in the player's world.
* Coordinates and offsets can be used together ('`0,64,0 ~0,10,0`' will result in '`0,74,0`').
* Console users must specify a player to define a world (since they are not present in any world).

## Configuration ##

At startup, the plugin will create a default configuration file if none exists.  This file is saved as `config.yml` and is located in `plugins/Soundcheck`.  The configuration is used solely for defining `sequences`.  The default configuration file provides detailed information regarding how to create new sequences.

## Bugs/Requests ##

This plugin is continually tested to ensure that it is performing correctly, but sometimes bugs can sneak in.  If you have found a bug with the plugin, or if you have a feature request, please [create an issue on Github](https://github.com/EasyMFnE/Soundcheck/issues).

## Donations ##

Donating is a great way to thank the developer if you find the plugin useful for your server, and encourages work on more 100% free and open-source plugins.  If you would like to donate (any amount), there is an easily accessible link in the top right corner of this page.  Thank you!

## Privacy ##

This plugin utilizes Hidendra's **Plugin-Metrics** system.  You may opt out of this service by editing your configuration located in `plugins/Plugin Metrics`.  The following anonymous data is collected and sent to [mcstats.org](http://mcstats.org):

* A unique identifier
* The server's version of Java
* Whether the server is in online or offline mode
* The plugin's version
* The server's version
* The OS version, name, and architecture
* The number of CPU cores
* The number of online players
* The Metrics version

## License ##

This plugin is released as a free and open-source project under the [GNU General Public License version 3 (GPLv3)](http://www.gnu.org/copyleft/gpl.html).  To learn more about what this means, click that link or [read about it on Wikipedia](http://en.wikipedia.org/wiki/GNU_General_Public_License).
