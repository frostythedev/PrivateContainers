package me.frostythedev.privatecontainers.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class LocationUtils {

    /*Useful tools that helped with my solutions*/

    public static String toString(Location location) {
        String world = location.getWorld().getName();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float pitch = location.getPitch();
        float yaw = location.getYaw();

        return world + ";" + x + ";" + y + ";" + z + ";" + pitch + ";" + yaw;
    }

    public static Location fromString(String input) {
        Location l = null;
        String[] parts = input.split(";");
        if (parts.length == 6) {
            l = new Location(Bukkit.getWorld(parts[0]), Double.parseDouble(parts[1])
                    , Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), Float.parseFloat(parts[4]),
                    Float.parseFloat(parts[5]));
        }

        return l;
    }

    public static Location getRelativeOfSameTypeAround(Block block) {
       return getRelativeOfSameType(block, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
    }

    public static Location getRelativeOfSameType(Block block, BlockFace... blockFaces){
        for(BlockFace bf : blockFaces){
            if(block.getRelative(bf) != null && block.getRelative(bf).getType().equals(block.getType())){
                return block.getRelative(bf).getLocation();
            }
        }
        return null;
    }
}
