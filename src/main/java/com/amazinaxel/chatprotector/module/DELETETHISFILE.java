package escaprs.escaprs;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Door;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;

public final class Escaprs extends JavaPlugin implements Listener {


    List<Block> doorList = new ArrayList<>(); // checks for all opened doors
    List<Player> playerList = new ArrayList<>(); // gets all players in the game
    List<Block> replacableBlocks = new ArrayList<>(); //gets all blocks to replace for bookshelf or head
    int roomTotalNum = 0;
    String worldName = "Game"; // world name
    @Override
    public void onEnable() {
        // Runs when the plugin is enabled, registered n' stuff
        Bukkit.getPluginManager().registerEvents(this, this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() { // runs every tick

                for (Player player : playerList) {

                }
            }
        }, 1, 1);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args){

        if (alias.equalsIgnoreCase("escaprs")){

            if (args[0].equalsIgnoreCase("create")) {
                initGame();

            } else if (args[0].equalsIgnoreCase("join")) {
                joinGame(args[1]);

            } else if (args[0].equalsIgnoreCase("remove")) {
                removePlayer(args[1]);

            } else if (args[0].equalsIgnoreCase("end")) {
                endGame();

            }

            return true;

        }
        return false;
    }
    public void initGame() { // command to create spawn
        Location spawnLoc = new Location(Bukkit.getWorld(worldName), 0,70,0);
        roomTotalNum = 0; // sets score var
        doorList.clear();
        replacableBlocks.clear();
        loadSchem(spawnLoc, "spawn", 0);
    }
    public void joinGame(String playerName) { // command to teleport to spawn and add player to playerList
        Location spawnLoc = new Location(Bukkit.getWorld(worldName), 0,70,0);

        try {
            Player player = Bukkit.getServer().getPlayer(playerName);
            playerList.add(player);
            player.teleport(spawnLoc);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public void removePlayer(String playerName) {
        try {
            Player player = Bukkit.getServer().getPlayer(playerName);
            playerList.remove(player);

        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    public void endGame() { // command to end game
        playerList.clear();

    }

    @EventHandler
    public void playerClick(PlayerInteractEvent event) {
        Action action = event.getAction();
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        int bossLevel = 10; // what level boss level starts at

        try {

            if (roomTotalNum < bossLevel - 1) { // check for normal room
                if (block != null && action.equals(Action.RIGHT_CLICK_BLOCK) && block.getType().equals(Material.DARK_OAK_DOOR) && !doorList.contains(block)) { // Main Rooms
                    roomTotalNum += 1; // adds local score var
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "GIAPI updateroom"); // adds static score for Skript's implementation
                    player.sendMessage(String.valueOf(roomTotalNum));
                    playerClickDoor(block, true);

                }

                if (block != null && action.equals(Action.RIGHT_CLICK_BLOCK) && block.getType().equals(Material.SPRUCE_DOOR) && !doorList.contains(block)) { // Side Rooms
                    playerClickDoor(block, false);

                }
            }

            if (roomTotalNum == bossLevel - 1) { // check for boss room
                if (action.equals(Action.RIGHT_CLICK_BLOCK) && block != null && block.getType().equals(Material.DARK_OAK_DOOR) && !doorList.contains(block)) {
                    roomTotalNum += 1; // adds local score var
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "GIAPI updateroom"); // adds static score for Skript's implementation
                    player.sendMessage(String.valueOf(roomTotalNum));
                    spawnBossRoom(block, player);

                }

            }

            if (block != null && (block.getType().name().startsWith("POTTED_") || block.getType() == Material.FLOWER_POT && player.getGameMode().equals(GameMode.ADVENTURE))) { // check for flower plot click
                event.setCancelled(true);

            }

            if (block != null && block.getType().equals(Material.PLAYER_HEAD) && playerList.contains(player)) { // give player loot when clicking a player head chest
                Skull skull = (Skull) block.getState();
                OfflinePlayer skullType = Bukkit.getOfflinePlayer("MHF_Chest");

                if (skull.getOwningPlayer() == skullType) {
                    block.setType(Material.AIR);
                    givePlayerLoot(player);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public void givePlayerLoot(Player player) {

        PlayerInventory playerInv = player.getInventory();
        ItemStack item = new ItemStack(Material.GLOWSTONE_DUST);

        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.YELLOW + "Sparkly dust :)");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "Freezes time in place");
        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        playerInv.addItem(item);
    }
    public void playerClickDoor(Block block, boolean IsMainDoor) {

        Door door = (Door) block.getBlockData();

        if (door.getFacing().equals(BlockFace.SOUTH)) {
            spawnLevel(block, BlockFace.SOUTH, IsMainDoor);

        } else if (door.getFacing().equals(BlockFace.EAST)) {
            spawnLevel(block, BlockFace.EAST, IsMainDoor);

        } else if (door.getFacing().equals(BlockFace.WEST)) {
            spawnLevel(block, BlockFace.WEST, IsMainDoor);

        } else if (door.getFacing().equals(BlockFace.NORTH)) {
            spawnLevel(block, BlockFace.NORTH, IsMainDoor);

        }
    }
    public void spawnLevel(Block block, BlockFace blockFace, boolean IsMainDoor) { // spawns random level

        Door door = (Door)block.getBlockData();
        Location blockLoc = block.getLocation();
        Location otherBlockLoc;

        if (door.getHalf() == Bisected.Half.TOP) {

            otherBlockLoc = block.getLocation().subtract(0,1,0); // gets block below top part of door
            doorList.add(block);
            doorList.add(otherBlockLoc.getBlock());

            blockLoc = block.getLocation().subtract(0,1,0);

        } else if (door.getHalf() == Bisected.Half.BOTTOM) {

            otherBlockLoc = block.getLocation().add(0,1,0); // gets block above bottom part of door
            doorList.add(block);
            doorList.add(otherBlockLoc.getBlock());

            blockLoc = block.getLocation();
        }

        String levelName = randomizeMainRoom(IsMainDoor);
        int rotation = getRotation(blockFace);

        Bukkit.getLogger().info(levelName);
        loadSchem(blockLoc, levelName, rotation);

    }
    public String randomizeMainRoom(boolean IsMainDoor) { // randomize for spawnLevel

        List<String> rooms = new ArrayList<>();

        if (IsMainDoor) { // all main door rooms
            Collections.addAll(rooms, "1HallwaySouth", "1OffHallwaySouth", "1SmallHallwaySouth", "1SmallLibrarySouth", "1TurnHallwaySouth"); // main rooms
        }

        if (!IsMainDoor) { // all side door rooms
            Collections.addAll(rooms, "1BedroomSouth", "1OfficeSouth", "1ShelfSouth"); // dead-end rooms
        }

        Random rand = new Random();
        int randInt = rand.nextInt(rooms.size());

        return rooms.get(randInt);
    }
    public int getRotation(BlockFace blockFace) {
        int rotation = 0;

        if (blockFace.equals(BlockFace.SOUTH)) {
            rotation = 0;

        } else if (blockFace.equals(BlockFace.EAST)) {
            rotation = 270;

        } else if (blockFace.equals(BlockFace.WEST)) {
            rotation = 90;

        } else if (blockFace.equals(BlockFace.NORTH)) {
            rotation = 180;

        }

        return rotation;
    }
    public void spawnBossRoom(Block block, Player player) {
        Door door = (Door)block.getBlockData();
        Location blockLoc = block.getLocation();
        Location otherBlockLoc;

        if (door.getHalf() == Bisected.Half.TOP) {

            otherBlockLoc = block.getLocation().subtract(0,1,0); // gets block below top part of door
            doorList.add(block);
            doorList.add(otherBlockLoc.getBlock());

            blockLoc = block.getLocation().subtract(0,1,0);

        } else if (door.getHalf() == Bisected.Half.BOTTOM) {

            otherBlockLoc = block.getLocation().add(0,1,0); // gets block above bottom part of door
            doorList.add(block);
            doorList.add(otherBlockLoc.getBlock());

            blockLoc = block.getLocation();
        }

        Location spawnLoc = new Location(Bukkit.getWorld(worldName), blockLoc.getX() + 100, blockLoc.getY(), blockLoc.getZ());
        loadSchem(spawnLoc, "1BossFight", 0);

        checkForReplacableBlocks(spawnLoc, player);
        teleportPlayerToBoss(spawnLoc); // teleports player

    }
    public void checkForReplacableBlocks(Location spawnLoc, Player player) {
        checkForBlocks(spawnLoc);
        for (Block block : replacableBlocks) {

            Random rand = new Random();
            int randNum = rand.nextInt();

            player.sendMessage(String.valueOf(randNum));

            if (randNum <= 97){
                block.setType(Material.BOOKSHELF);

            } else if (randNum >= 100) {

                block.setType(Material.PLAYER_HEAD);
                Skull skull = (Skull)block.getState();
                OfflinePlayer skullType = Bukkit.getOfflinePlayer("MHF_Chest");
                skull.setOwningPlayer(skullType);
                skull.update();


            }
        }
    }
    public void checkForBlocks(Location spawnLoc) {
        List<Block> blocksAroundPoint = getNearbyBlocks(spawnLoc, 50);

        for (Block block : blocksAroundPoint) {
            if (block.getType().equals(Material.BLUE_GLAZED_TERRACOTTA)) {
                replacableBlocks.add(block);
            }
        }
    }
    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }
    public void teleportPlayerToBoss(Location spawnLoc) {
        for (Player player : playerList) {
            player.teleport(spawnLoc);
        }
    }
    public void loadSchem(Location location, String filename, int rotation) { // parameters: location to spawn schem, destination of schematic file. Example: /spruceHallway.schem
        File schem = new File(Bukkit.getServer().getPluginManager().getPlugin("Escaprs").getDataFolder().getAbsolutePath() + "/" + filename + ".schem");
        com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(Bukkit.getWorld(worldName));
        ClipboardFormat format = ClipboardFormats.findByFile(schem);


        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        try(ClipboardReader reader = format.getReader(new FileInputStream(schem))) {

            Clipboard clipboard = reader.read();

            try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld,-1)) {

                ClipboardHolder holder = new ClipboardHolder(clipboard);

                AffineTransform transform = new AffineTransform();
                transform = transform.rotateY(rotation);
                transform = transform.rotateX(0);
                transform = transform.rotateZ(0);
                holder.setTransform(holder.getTransform().combine(transform));

                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(x, y, z))
                        .ignoreAirBlocks(false)
                        .copyEntities(true)
                        .build();

                try {

                    Operations.complete(operation);
                    editSession.flushSession();

                } catch (WorldEditException e) {
                    e.printStackTrace();
                }

            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
