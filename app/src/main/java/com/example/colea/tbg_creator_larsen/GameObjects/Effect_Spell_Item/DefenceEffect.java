package com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item;

import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Player;

import org.json.JSONException;
import org.json.JSONObject;

public class DefenceEffect extends Effect{

    String name;
    String description;
    int defence = 0;
    int id;
    int duration;

    public DefenceEffect(String nam, String desc, int def, int dur)
    {
        name = nam;
        description = desc;
        defence = def;
        id = GameController.getId();
        duration = dur;
    }

    public DefenceEffect(String nam, String desc, int def, int dur, int i)
    {
        name = nam;
        description = desc;
        defence = def;
        id = i;
        duration = dur;
    }

    @Override
    public void link(GameObjects gameObjects)
    {
    }

    public static DefenceEffect fromJSON(JSONObject nextObject)
    {
        try {
            String name = (String) nextObject.get("name");
            String description = (String) nextObject.get("description");
            int id = nextObject.getInt("id");
            int defence = nextObject.getInt("defence");
            int duration = nextObject.getInt("duration");
            return new DefenceEffect(name, description, defence, duration, id);
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
            stateObject.put("OBJECT TYPE", "DefenceEffect");
            stateObject.put("id", id);
            stateObject.put("name", name);
            stateObject.put("description", description);
            stateObject.put("defence", defence);
            stateObject.put("duration", duration);
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
            p.setDefence(p.defence() + defence);
            ef = name + " raised " + p.name + " defence";
        }
        else
        {
            Enemy enemy = (Enemy) o;
            enemy.setDefenceScore(enemy.defence() + defence);
            ef = p.name + " raised " + enemy.name + "'s defence with " + name;
        }
        return ef;
    }

    //Will always be Player or Enemy
    public void undo(Object o)
    {
        Player p = Player.getPlayer();
        if(o instanceof Player)
        {
            p.setDefence(p.defence() - defence);
        }
        else
        {
            Enemy enemy = (Enemy) o;
            enemy.setDefenceScore(enemy.defence() - defence);
        }
    }


    public  int getDuration()
    {
        return duration;
    }

    public boolean skipsTurn()
    {
        return false;
    }

    public boolean combatOnly()
    {
        return true;
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
