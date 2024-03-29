package me.stephenminer.redvblue.arena;

import me.stephenminer.redvblue.RedBlue;
import me.stephenminer.redvblue.chests.NewLootChest;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArenaBuilder {
    private final String id;
    private final RedBlue plugin;

    private Location loc1;
    private Location loc2;
    private Location lobby;
    private Location blueLoc;
    private Location redLoc;
    private String name;

    public ArenaBuilder(String id, RedBlue plugin){
        this.plugin = plugin;
        this.id = id;
    }


    public void loadData(){
        String base = "arenas." + id;
        loc1 = plugin.fromString(plugin.arenas.getConfig().getString(base + ".loc1"));
        loc2 = plugin.fromString(plugin.arenas.getConfig().getString(base + ".loc2"));
        blueLoc = plugin.fromString(plugin.arenas.getConfig().getString(base + ".blue-spawn"));
        redLoc = plugin.fromString(plugin.arenas.getConfig().getString(base + ".red-spawn"));
        lobby = plugin.fromString(plugin.arenas.getConfig().getString(base + ".lobby"));
        name = plugin.arenas.getConfig().getString( base + ".name");
    }


    public Set<NewLootChest> loadLootChests(){
        String path = "arenas." + id + ".loot-chests";
        Set<NewLootChest> chests = new HashSet<>();
        if (!plugin.arenas.getConfig().contains(path)) return chests;
        List<String> stringChests  = plugin.arenas.getConfig().getStringList(path);
        for (String entry : stringChests){
            NewLootChest lootChest = new NewLootChest(entry);
            chests.add(lootChest);
        }
        return chests;
    }
    public List<Wall> loadWalls(){
        String base = "arenas." + id + ".walls";
        List<String> sWalls = plugin.arenas.getConfig().getStringList(base);
        List<Wall> walls = new ArrayList<>();
        sWalls.forEach(str->walls.add(new Wall(str)));
        return walls;
    }

    public int getFallTime(){
        String path = "settings.wall-time";
        return plugin.settings.getConfig().getInt(path);
    }


    public Arena build(){
        List<Wall> walls = loadWalls();
        Arena arena = new Arena(id, name, loc1, loc2, redLoc, blueLoc, lobby);
        arena.setWalls(walls);
        walls.forEach(Wall::buildWall);
        arena.setFallTime(getFallTime());
        arena.addChests(loadLootChests());
        Arena.arenas.add(arena);
        return arena;
    }


}
