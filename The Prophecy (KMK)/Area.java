import java.util.HashMap;
import java.util.ArrayList;

/*
 * A simple description of a area in the simple sample game.
 * 
 * Last updated: 2015-04-21
 * Original author: Dr. Jean Gourd
 * Author: Lucas Hashimoto
 */
public class Area
{
    private String name;
    private String image;
    // exits map directions (e.g., "north") to areas
    private HashMap<String,Area> exits;
    // items map items (e.g., "table") to their description
    private HashMap<String,String> items;
    // grabbables are essentially items that can be picked up and stored as inventory
    private ArrayList<String> grabbables;
    // talkables are items that add an additional text option when you "talk"
    private HashMap<String,String> talkables;
    
    private HashMap<String,String> descriptions;
    
    private ArrayList<String> key_items;

    // constructor
    public Area(String n, String i)
    {
        name = n;
        image = i;
        exits = new HashMap<String,Area>();
        items = new HashMap<String,String>();
        grabbables = new ArrayList<String>();
        talkables = new HashMap<String,String>();
        descriptions = new HashMap<String, String>();
        key_items = new ArrayList<String>();
    }
    
    // getters and setters
    public String getImage()
    {
        return image;
    }
    
    public HashMap<String,Area> getExits()
    {
        return exits;
    }
    
    public void addExit(String e, Area r)
    {
        exits.put(e, r);
    }
    
    public HashMap<String,String> getItems()
    {
        return items;
    }
    
    public void addItem(String i, String d)
    {
        items.put(i, d);
    }
    
    public ArrayList<String> getGrabbables()
    {
        return grabbables;
    }
    
    public void addGrabbable(String g)
    {
        grabbables.add(g);
    }
    
    public void removeGrabbable(String g)
    {
        grabbables.remove(g);
    }
    
    public HashMap<String,String> getTalkables()
    {
        return talkables;
    }
    
    public void addTalkable(String x, String y)
    {
        talkables.put(x, y);
    }
    
    public HashMap<String,String> getDescriptions()
    {
        return descriptions;
    }
    
    public void addDescription(String u, String m)
    {
        descriptions.put(u, m);
    }
    
    public ArrayList<String> getKeyItems()
    {
        return key_items;
    }
    
    public void addKeyItem(String p)
    {
        key_items.add(p);
    }
    
    // what to display when outputting a area
    public String toString()
    {
        String s = "  You are in " + name + ".\n\n";
        
        s += "  You see: ";
        for (String item : items.keySet())
        {
                s += item + " ";
        }
        s += "\n\n";
        
        s += "  Exits: ";
        for (String exit : exits.keySet())
        {
            s += exit + " ";
        }
        s += "\n";
        
        return s;
    }
}