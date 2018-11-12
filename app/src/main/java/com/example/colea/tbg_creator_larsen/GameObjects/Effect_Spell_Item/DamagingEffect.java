package com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item;

import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DamagingEffect extends Effect{

    public String uniqueUserId = "";
    public String name;
    public String description;
    public int damage = 0;
    public int id;

    public String getUUID()
    {
        return uniqueUserId;
    }

    public String getMainId()
    {
        if(uniqueUserId.isEmpty())
        {
            return ""+id;
        }
        else
        {
            return getUUID();
        }
    }

    public DamagingEffect(String nam, String desc, int dam)
    {
        name = nam;
        description = desc;
        damage = dam;
        id = GameController.getId();
    }
    public DamagingEffect(String nam, String desc, int dam, int i)
    {
        name = nam;
        description = desc;
        damage = dam;
        id = i;
    }

    public void link(GameObjects gameObjects)
    {

    }

    public static DamagingEffect fromJSON(JSONObject nextObject)
    {
        try {
            String name = (String) nextObject.get("name");
            String description = (String) nextObject.get("description");
            int id = nextObject.getInt("id");
            int damage = nextObject.getInt("damage");
            String uuid = "";
            if(nextObject.has("uuid"))
            {
                uuid = nextObject.getString("uuid");
            }
            DamagingEffect damagingEffect = new DamagingEffect(name, description, damage, id);
            damagingEffect.uniqueUserId = uuid;
            return damagingEffect;
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
            stateObject.put("OBJECT TYPE", "DamagingEffect");
            stateObject.put("id", id);
            stateObject.put("name", name);
            stateObject.put("description", description);
            stateObject.put("damage", damage);
            stateObject.put("uuid", uniqueUserId);
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
            p.setHealth(p.getHealth() - damage);
            ef = name + " dealt " + damage + " to " + p.name;
        }
        else
        {
            Enemy enemy = (Enemy) o;
            enemy.health -= damage;
            ef = p.name + " dealt " + damage + " to " + enemy.name + " with " + name;
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
