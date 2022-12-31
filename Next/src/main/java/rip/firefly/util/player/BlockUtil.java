package rip.firefly.util.player;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.material.Door;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockUtil {

    /**
     * Thanks too KekzCrafter your the best (im too lazy to write all this)
     */
    static String[] HalfBlocksArray = {"pot", "flower", "step", "slab", "snow", "detector", "daylight",
            "comparator", "repeater", "diode", "water", "lava", "ladder", "vine", "carpet", "sign", "pressure", "plate",
            "button", "mushroom", "torch", "frame", "armor", "banner", "lever", "hook", "redstone", "rail", "brewing",
            "rose", "skull", "enchantment", "cake", "bed"};


    private static Set<Byte> blockSolidPassSet;

    static {
        blockSolidPassSet = new HashSet<>();

        blockSolidPassSet.add((byte) 0);
        blockSolidPassSet.add((byte) 6);
        blockSolidPassSet.add((byte) 8);
        blockSolidPassSet.add((byte) 9);
        blockSolidPassSet.add((byte) 10);
        blockSolidPassSet.add((byte) 11);
        blockSolidPassSet.add((byte) 27);
        blockSolidPassSet.add((byte) 28);
        blockSolidPassSet.add((byte) 30);
        blockSolidPassSet.add((byte) 31);
        blockSolidPassSet.add((byte) 32);
        blockSolidPassSet.add((byte) 37);
        blockSolidPassSet.add((byte) 38);
        blockSolidPassSet.add((byte) 39);
        blockSolidPassSet.add((byte) 40);
        blockSolidPassSet.add((byte) 50);
        blockSolidPassSet.add((byte) 51);
        blockSolidPassSet.add((byte) 55);
        blockSolidPassSet.add((byte) 59);
        blockSolidPassSet.add((byte) 63);
        blockSolidPassSet.add((byte) 66);
        blockSolidPassSet.add((byte) 68);
        blockSolidPassSet.add((byte) 69);
        blockSolidPassSet.add((byte) 70);
        blockSolidPassSet.add((byte) 72);
        blockSolidPassSet.add((byte) 75);
        blockSolidPassSet.add((byte) 76);
        blockSolidPassSet.add((byte) 77);
        blockSolidPassSet.add((byte) 78);
        blockSolidPassSet.add((byte) 83);
        blockSolidPassSet.add((byte) 90);
        blockSolidPassSet.add((byte) 104);
        blockSolidPassSet.add((byte) 105);
        blockSolidPassSet.add((byte) 115);
        blockSolidPassSet.add((byte) 119);
        blockSolidPassSet.add((byte) (-124));
        blockSolidPassSet.add((byte) (-113));
        blockSolidPassSet.add((byte) (-81));
    }

    public static boolean isLiquid(Block block) {
        if ((block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER
                || block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA)) {
            return true;
        }
        return false;
    }

    public static boolean isClimbableBlock(Block block) {
        if (block.getType() == Material.LADDER || block.getType() == Material.VINE) {
            return true;
        }
        return false;
    }

    public static boolean isIce(Block block) {
        return block.getType().equals(Material.ICE)
                || block.getType().equals(Material.PACKED_ICE)
                || block.getType().equals(Material.getMaterial("FROSTED_ICE"));
    }

    @SuppressWarnings("deprecation")
    public static boolean isFence(Block block) {
        return block.getType().getId() == 85 || block.getType().getId() == 139 || block.getType().getId() == 113
                || block.getTypeId() == 188 || block.getTypeId() == 189 || block.getTypeId() == 190 || block.getTypeId() == 191
                || block.getTypeId() == 192;
    }

    @SuppressWarnings("deprecation")
    public static boolean isDoor(Block block) {
        if (block.getType().equals(Material.IRON_DOOR) || block.getType().equals(Material.IRON_DOOR_BLOCK)
                || block.getType().equals(Material.WOOD_DOOR) || block.getType().equals(Material.WOODEN_DOOR)
                || block.getTypeId() == 193 || block.getTypeId() == 194
                || block.getTypeId() == 195 || block.getTypeId() == 196
                || block.getTypeId() == 197 || block.getTypeId() == 324
                || block.getTypeId() == 428 || block.getTypeId() == 429
                || block.getTypeId() == 430 || block.getTypeId() == 431) {
            return true;
        }
        return false;
    }

    public static boolean pointTwo(Block block) {
        return block.getType().equals(Material.ENDER_PORTAL_FRAME) || block.getType().equals(Material.CHEST)
                || block.getType().equals(Material.ENDER_CHEST) || block.getType().equals(Material.TRAPPED_CHEST)
                || block.getType().equals(Material.SOUL_SAND);
    }

    @SuppressWarnings("deprecation")
    public static boolean pointThreeSeven(Block block) {
        return block.getType().equals(Material.FLOWER_POT) || block.getType().equals(Material.DAYLIGHT_DETECTOR)
                || block.getTypeId() == 178;
    }

    public static boolean threeQuarters(Block block) {
        return block.getType().equals(Material.SKULL) || block.getType().equals(Material.COCOA) || block.getType().equals(Material.ENCHANTMENT_TABLE);
    }

    public static boolean isBed(Block block) {
        return block.getType().equals(Material.BED_BLOCK) || block.getType().equals(Material.BED);
    }

    @SuppressWarnings("deprecation")
    public static boolean isTrapDoor(Block block) {
        if (block.getType().equals(Material.TRAP_DOOR) || block.getTypeId() == 167) {
            return true;
        }
        return false;
    }

    public static boolean isChest(Block block) {
        return block.getType().equals(Material.TRAPPED_CHEST) || block.getType().equals(Material.CHEST) || block.getType().equals(Material.ENDER_CHEST);
    }

    @SuppressWarnings("deprecation")
    public static boolean isInBlockDimensions(Location to, Location from, Block block) {
        if (isDoor(block)) {
            Door door = (Door) block.getType().getNewData(block.getData());
            if (door.isTopHalf()) {
                return false;
            }

            BlockFace facing = door.getFacing();
            if (door.isOpen()) {
                Block up = block.getRelative(BlockFace.UP);
                boolean hinge;
                if ((up.getType().equals(Material.IRON_DOOR_BLOCK)) || (up.getType().equals(Material.WOODEN_DOOR))) {
                    hinge = (up.getData() & 0x1) == 1;
                } else {
                    return false;
                }
                if (facing == BlockFace.NORTH) {
                    facing = hinge ? BlockFace.WEST : BlockFace.EAST;
                } else if (facing == BlockFace.EAST) {
                    facing = hinge ? BlockFace.NORTH : BlockFace.SOUTH;
                } else if (facing == BlockFace.SOUTH) {
                    facing = hinge ? BlockFace.EAST : BlockFace.WEST;
                } else {
                    facing = hinge ? BlockFace.SOUTH : BlockFace.NORTH;
                }
            }

            if (facing == BlockFace.EAST) {
                if (block.getX() >= 0) {
                    if (from.getX() > to.getX()) {
                        if (to.getX() < (block.getX() + 0.8)) {
                            return true;
                        }
                    } else {
                        if (to.getX() > (block.getX() + 0.8)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public static double getBlockHeight(Block block) {
        if (block.getTypeId() == 44) {
            return 0.5;
        }
        if (block.getTypeId() == 53) {
            return 0.5;
        }
        if (block.getTypeId() == 85) {
            return 0.2;
        }
        if (block.getTypeId() == 54 || block.getTypeId() == 130) {
            return 0.125;
        }
        return 0;
    }

    public static boolean isPiston(Block block) {
        return block.getType().equals(Material.PISTON_MOVING_PIECE) || block.getType().equals(Material.PISTON_EXTENSION)
                || block.getType().equals(Material.PISTON_BASE) || block.getType().equals(Material.PISTON_STICKY_BASE);
    }

    @SuppressWarnings("deprecation")
    public static boolean isFenceGate(Block block) {
        return block.getType().equals(Material.FENCE_GATE)
                || block.getTypeId() == 183 || block.getTypeId() == 184
                || block.getTypeId() == 185 || block.getTypeId() == 186
                || block.getTypeId() == 187;
    }

    public static boolean isAir(Block block) {
        return block.getType().equals(Material.AIR);
    }

    public static boolean isOneTwoFive(Block block) {
        return block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST)
                || block.getType().equals(Material.SOUL_SAND);
    }

    @SuppressWarnings("deprecation")
    public static boolean isStair(Block block) {
        if (block.getType().equals(Material.ACACIA_STAIRS) || block.getType().equals(Material.BIRCH_WOOD_STAIRS)
                || block.getType().equals(Material.BRICK_STAIRS) || block.getType().equals(Material.COBBLESTONE_STAIRS)
                || block.getType().equals(Material.DARK_OAK_STAIRS) || block.getType().equals(Material.NETHER_BRICK_STAIRS)
                || block.getType().equals(Material.JUNGLE_WOOD_STAIRS) || block.getType().equals(Material.QUARTZ_STAIRS)
                || block.getType().equals(Material.SMOOTH_STAIRS) || block.getType().equals(Material.WOOD_STAIRS)
                || block.getType().equals(Material.SANDSTONE_STAIRS) || block.getType().equals(Material.SPRUCE_WOOD_STAIRS)
                || block.getTypeId() == 203 || block.getTypeId() == 180) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public static boolean isSlab(Block block) {
        return block.getTypeId() == 44 || block.getTypeId() == 126 || block.getTypeId() == 205
                || block.getTypeId() == 182;
    }

    public static ArrayList<Block> getSurroundingB(Block block) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        for (double x = -0.3; x <= 0.3; x += 0.3) {
            for (double y = -0.3; y <= 0.3; y += 0.3) {
                for (double z = -0.3; z <= 0.3; z += 0.3) {
                    if ((x != 0) || (y != 0) || (z != 0)) {
                        blocks.add(block.getLocation().add(x, y, z).getBlock());
                    }
                }
            }
        }
        return blocks;
    }

    public static ArrayList<Block> getSurroundingC(Location location) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        for (double x = -0.3; x <= 0.3; x += 0.3) {
            for (double y = -0.3; y <= 0.3; y += 0.3) {
                for (double z = -0.3; z <= 0.3; z += 0.3) {
                    blocks.add(location.add(x, y, z).getBlock());
                }
            }
        }
        return blocks;
    }

    public static ArrayList<Block> getSurroundingXZ(Block block) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        blocks.add(block.getRelative(BlockFace.NORTH));
        blocks.add(block.getRelative(BlockFace.NORTH_EAST));
        blocks.add(block.getRelative(BlockFace.NORTH_WEST));
        blocks.add(block.getRelative(BlockFace.SOUTH));
        blocks.add(block.getRelative(BlockFace.SOUTH_EAST));
        blocks.add(block.getRelative(BlockFace.SOUTH_WEST));
        blocks.add(block.getRelative(BlockFace.EAST));
        blocks.add(block.getRelative(BlockFace.WEST));

        return blocks;
    }

    public static boolean isRegularBlock(Block block) {
        return !isTrapDoor(block) && !isDoor(block) && !isSlab(block)
                && !isStair(block) && !isFence(block) && !isFenceGate(block)
                && !isOneTwoFive(block) && !isPiston(block);
    }

    public static boolean isEdible(Material material) {
        return material.equals(Material.COOKED_BEEF) || material.equals(Material.COOKED_CHICKEN)
                || material.equals(Material.COOKED_FISH) || material.equals(Material.getMaterial("COOKED_MUTTON"))
                || material.equals(Material.getMaterial("COOKED_RABBIT")) || material.equals(Material.ROTTEN_FLESH)
                || material.equals(Material.CARROT_ITEM) || material.equals(Material.CARROT)
                || material.equals(Material.GOLDEN_APPLE) || material.equals(Material.GOLDEN_CARROT)
                || material.equals(Material.GRILLED_PORK) || material.equals(Material.RAW_BEEF)
                || material.equals(Material.RAW_CHICKEN) || material.equals(Material.RAW_FISH)
                || material.equals(Material.SPIDER_EYE) || material.equals(Material.getMaterial("BEETROOT_SOUP"))
                || material.equals(Material.MUSHROOM_SOUP) || material.equals(Material.POTATO)
                || material.equals(Material.POTATO_ITEM) || material.equals(Material.BAKED_POTATO)
                || material.equals(Material.POISONOUS_POTATO) || material.equals(Material.PUMPKIN_PIE)
                || material.equals(Material.APPLE) || material.equals(Material.getMaterial("MUTTON"))
                || material.equals(Material.getMaterial("RABBIT")) || material.equals(Material.MELON)
                || material.equals(Material.getMaterial("CHORUS_FRUIT")) || material.equals(Material.COOKIE)
                || material.equals(Material.POTION);
    }

    public static ArrayList<Block> getSurroundingXZ(Block block, boolean diagonals) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        if (diagonals) {
            blocks.add(block.getRelative(BlockFace.NORTH));
            blocks.add(block.getRelative(BlockFace.NORTH_EAST));
            blocks.add(block.getRelative(BlockFace.NORTH_WEST));
            blocks.add(block.getRelative(BlockFace.SOUTH));
            blocks.add(block.getRelative(BlockFace.SOUTH_EAST));
            blocks.add(block.getRelative(BlockFace.SOUTH_WEST));
            blocks.add(block.getRelative(BlockFace.EAST));
            blocks.add(block.getRelative(BlockFace.WEST));
        } else {
            blocks.add(block.getRelative(BlockFace.NORTH));
            blocks.add(block.getRelative(BlockFace.SOUTH));
            blocks.add(block.getRelative(BlockFace.EAST));
            blocks.add(block.getRelative(BlockFace.WEST));
        }

        return blocks;
    }

    public static boolean isHalfBlock(Block block) {
        Material type = block.getType();
        for (String types : HalfBlocksArray) {
            if (type.toString().toLowerCase().contains(types)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNearHalfBlock(Player p) {
        boolean out = false;
        for (Block b : getNearbyBlocks(p.getLocation(), 1)) {
            if (isHalfBlock(b)) {
                out = true;
            }
        }
        return out;
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static boolean isNearIce(Player p) {
        boolean out = false;
        for (Block b : getNearbyBlocks(p.getLocation(), 1)) {
            if (isIce(b)) {
                out = true;
            }
        }
        return out;
    }

    public static boolean isNearStair(Player p) {
        boolean out = false;
        for (Block b : getNearbyBlocks(p.getLocation(), 2)) {
            if (isStair(b)) {
                out = true;
            }
        }
        return out;
    }

    public static boolean isNearLiquid(Player p) {
        boolean out = false;
        for (Block b : getNearbyBlocks(p.getLocation(), 1)) {
            if (isLiquid(b)) {
                out = true;
            }
        }
        return out;
    }

    public static boolean isNearLadder(Player p) {
        boolean out = false;
        for (Block b : getNearbyBlocks(p.getLocation(), 1)) {
            if (b.getType() == Material.LADDER) {
                out = true;
            }
        }
        return out;
    }

    public static boolean isNearPistion(Player p) {
        boolean out = false;
        for (Block b : getNearbyBlocks(p.getLocation(), 1)) {
            if (b.getType() == Material.PISTON_BASE || b.getType() == Material.PISTON_MOVING_PIECE || b.getType() == Material.PISTON_STICKY_BASE || b.getType() == Material.PISTON_EXTENSION) {
                out = true;
            }
        }
        return out;
    }

    public static boolean isNearSnow(Player p) {
        boolean out = false;
        for (Block b : getNearbyBlocks(p.getLocation(), 1)) {
            if (b.getType() == Material.SNOW || b.getType() == Material.SNOW_BLOCK) {
                out = true;
            }
        }
        return out;
    }

    public static boolean isOnGround(final Location location, final int down) {
        final double posX = location.getX();
        final double posZ = location.getZ();

        final double fracX = (posX % 1.0 > 0.0) ? Math.abs(posX % 1.0) : (1.0 - Math.abs(posX % 1.0));
        final double fracZ = (posZ % 1.0 > 0.0) ? Math.abs(posZ % 1.0) : (1.0 - Math.abs(posZ % 1.0));

        final int blockX = location.getBlockX();
        final int blockY = location.getBlockY() - down;
        final int blockZ = location.getBlockZ();

        final World world = location.getWorld();

        if (!blockSolidPassSet.contains((byte) world.getBlockAt(blockX, blockY, blockZ).getTypeId())) {
            return true;
        }

        if (fracX < 0.3) {
            if (!blockSolidPassSet.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ).getTypeId())) {
                return true;
            }

            if (fracZ < 0.3) {
                if (!blockSolidPassSet.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ - 1)
                        .getTypeId())) {
                    return true;
                }

                if (!blockSolidPassSet.contains((byte) world.getBlockAt(blockX, blockY, blockZ - 1)
                        .getTypeId())) {
                    return true;
                }

                return !blockSolidPassSet.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ - 1).getTypeId());
            } else if (fracZ > 0.7) {
                if (!blockSolidPassSet.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ + 1)
                        .getTypeId())) {
                    return true;
                }

                if (!blockSolidPassSet.contains((byte) world.getBlockAt(blockX, blockY, blockZ + 1)
                        .getTypeId())) {
                    return true;
                }

                return !blockSolidPassSet.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ + 1).getTypeId());
            }
        } else if (fracX > 0.7) {
            if (!blockSolidPassSet.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ).getTypeId()
            )) {
                return true;
            }

            if (fracZ < 0.3) {
                if (!blockSolidPassSet.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ - 1)
                        .getTypeId())) {
                    return true;
                }

                if (!blockSolidPassSet.contains((byte) world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId())) {
                    return true;
                }

                return !blockSolidPassSet.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ - 1).getTypeId());
            } else if (fracZ > 0.7) {
                if (!blockSolidPassSet.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ + 1)
                        .getTypeId())) {
                    return true;
                }

                if (!blockSolidPassSet.contains((byte) world.getBlockAt(blockX, blockY, blockZ + 1)
                        .getTypeId())) {
                    return true;
                }

                return !blockSolidPassSet.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ + 1).getTypeId());
            }
        } else if (fracZ < 0.3) {
            return !blockSolidPassSet.contains((byte) world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId());
        } else return fracZ > 0.7 && !blockSolidPassSet.contains((byte) world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId());

        return false;
    }
}
