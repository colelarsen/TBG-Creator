package com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item;

import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Player;

import org.json.JSONException;
import org.json.JSONObject;

public class HealingEffect extends Effect{

    String name;
    String description;
    int healing = 0;
    int id;

    public HealingEffect(String nam, String desc, int heal)
    {
        name = nam;
        description = desc;
        healing = heal;
        id = GameController.getId();
    }

    public HealingEffect(String nam, String desc, int heal, int i)
    {
        name = nam;
        description = desc;
        healing = heal;
        id = i;
    }

    @Override
    public void link(GameObjects gameObjects)
    {
    }

    public static HealingEffect fromJSON(JSONObject nextObject)
    {
        try {
            String name = (String) nextObject.get("name");
            String description = (String) nextObject.get("description");
            int id = nextObject.getInt("id");
            int healing = nextObject.getInt("healing");
            return new HealingEffect(name, description, healing, id);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject toJSON()
    {
        try {
            JSONObject stateObject = new JSONObject();
            stateObject.put("OBJECT TYPE", "HealingEffect");
            stateObject.put("id", id);
            stateObject.put("name", name);
            stateObject.put("description", description);
            stateObject.put("healing", healing);
            return stateObject;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    //This object can be Player or Enemy
    public String effect(Object o)
    {
        Player p = Player.getPlayer();
        String ef = "";
        if(o instanceof Player)
        {
            p.setHealth(p.getHealth() + healing);
            ef = name + " healed " + healing + " to " + p.name;
        }
        else
        {
            Enemy enemy = (Enemy) o;
            enemy.health += healing;
            ef = p.name + " healed " + healing + " to " + enemy.name + " with " + name;
        }
        return ef;
    }

    //Will always be Player or Enemy
    public void undo(Object o)
    {

    }


    public  int getDuration()
    {
        return 0;
    }

    public boolean skipsTurn()
    {
        return false;
    }

    public boolean combatOnly()
    {
        return false;
    }

    public String getDescription()
    {
        return description;
    }

    public String getName()
    {
        return name;
    }

    public int getId()
    {
        return id;
    }
}
