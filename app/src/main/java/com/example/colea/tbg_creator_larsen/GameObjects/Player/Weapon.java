package com.example.colea.tbg_creator_larsen.GameObjects.Player;

import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.Effect;

import org.json.JSONException;
import org.json.JSONObject;

public class Weapon extends Item {
    private int id;
    private int value;
    private String name;
    private String description;
    private boolean keyItem;
    private int attack;
    private boolean isEqu = false;

    @Override
    public void link(GameObjects gameObjects)
    {
    }

    public static Weapon fromJSON(JSONObject nextObject)
    {
        /*
        stateObject.put("id", id);
            stateObject.put("name", name);
            stateObject.put("descriptions", description);
            stateObject.put("value", value);
            stateObject.put("keyItem", keyItem);
            stateObject.put("attack", attack);
            stateObject.put("OBJECT TYPE", "Weapon");
         */

        try {
            int id = nextObject.getInt("id");
            int value = nextObject.getInt("value");
            boolean keyItem = nextObject.getBoolean("keyItem");
            String name = (String)nextObject.get("name");
            String description = (String)nextObject.get("descriptions");
            int attack = nextObject.getInt("attack");
            Weapon w = new Weapon(name, description, value, keyItem, attack, id);
            return w;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject toJSON()
    {
        try {
            /*
            name = nam;
            description = desc;
            value = val;
            keyItem = key;
            attack = attak;
            id = GameController.getId();
             */
            JSONObject stateObject = new JSONObject();

            stateObject.put("id", id);
            stateObject.put("name", name);
            stateObject.put("descriptions", description);
            stateObject.put("value", value);
            stateObject.put("keyItem", keyItem);
            stateObject.put("attack", attack);
            stateObject.put("OBJECT TYPE", "Weapon");
            return stateObject;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }


        return null;

    }

    public Weapon(String nam, String desc, int val, boolean key, int attak)
    {
        name = nam;
        description = desc;
        value = val;
        keyItem = key;
        attack = attak;
        id = GameController.getId();
    }

    public Weapon(String nam, String desc, int val, boolean key, int attak, int i)
    {
        name = nam;
        description = desc;
        value = val;
        keyItem = key;
        attack = attak;
        id = i;
    }

    public void drop()
    {
        if(!keyItem)
        {
            Player.getPlayer().inventory.drop(id);
        }
    }

    public void equip()
    {
        if(!isEqu) {
            Player.equipWeapon(this);
            isEqu = true;
        }
        else
        {
            Player.equipWeapon(new Weapon("None", "None", 0, true, 1));
        }
    }

    public void setIsEqu(boolean x)
    {
        isEqu = x;
    }

    public void setAttack(int a)
    {
        attack = a;
    }

    public int getAttack()
    {
        return attack;
    }

    @Override
    public boolean isEquipment() {
        return false;
    }

    @Override
    public boolean isWeapon() {
        return true;
    }

    @Override
    public boolean isEquipped()
    {
        return isEqu;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public boolean isKeyItem() {
        return keyItem;
    }
}
