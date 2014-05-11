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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Class that can detect, parse, and store location coordinates of the format
 * "double,double,double".
 * 
 * @author Eric Hildebrand
 */
public class Coordinates {
    
    /* Regular expressions for matching Strings */
    private static Pattern number = Pattern.compile("-?[0-9]+\\.?[0-9]*");
    private static Pattern pattern = Pattern.compile("^(" + number.pattern()
            + "),(" + number.pattern() + "),(" + number.pattern() + ")$");
    
    /**
     * @param string
     *            Input string
     * @return Whether string matches the Coordinates format
     */
    public static boolean matches(String string) {
        return pattern.matcher(string).matches();
    }
    
    /**
     * Read Coordinates data from a string and return a new Coordinates using
     * the data. Returns null when the input does not match the appropriate
     * format.
     * 
     * @param string
     *            Input string
     * @return read Coordinates
     * @throws NumberFormatException
     */
    public static Coordinates parse(String string) throws NumberFormatException {
        Matcher matcher = pattern.matcher(string);
        if (!matcher.matches()) {
            return null;
        }
        return new Coordinates(Double.parseDouble(matcher.group(1)),
                Double.parseDouble(matcher.group(2)),
                Double.parseDouble(matcher.group(3)));
    }
    
    private double x, y, z;
    
    public Coordinates(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * @param world
     *            World
     * @return Location of Coordinates in the world
     */
    public Location getLocation(World world) {
        return new Location(world, x, y, z);
    }
    
}
