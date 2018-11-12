package com.example.colea.tbg_creator_larsen.GameObjects.Player;

import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;

import org.json.JSONException;
import org.json.JSONObject;

public class Equipment extends Item {

    public int id;
    public int value;
    public String name;
    public String description;
    public boolean keyItem;
    public int defence;
    public boolean isEqu = false;
    public String uniqueUserId = "";

    public String getUniqueUserId()
    {
        if(!uniqueUserId.isEmpty()) {
            return uniqueUserId;
        }
        else
        {
            return "" + id;
        }
    }

    public Equipment()
    {

    }

    public Equipment(String nam, String desc, int val, boolean key, int def)
    {
        name = nam;
        description = desc;
        value = val;
        keyItem = key;
        defence = def;
        id = GameController.getId();
    }

    public Equipment(String nam, String desc, int val, boolean key, int def, int i)
    {
        name = nam;
        description = desc;
        value = val;
        keyItem = key;
        defence = def;
        id = i;
    }

    @Override
    public void link(GameObjects gameObjects)
    {
    }

    public static Equipment fromJSON(JSONObject nextObject)
    {
        /*
        stateObject.put("id", id);
            stateObject.put("name", name);
            stateObject.put("descriptions", description);
            stateObject.put("value", value);
            stateObject.put("keyItem", keyItem);
            stateObject.put("defence", defence);
            stateObject.put("OBJECT TYPE", "Weapon");
         */

        try {
            int id = nextObject.getInt("id");
            int value = nextObject.getInt("value");
            boolean keyItem = nextObject.getBoolean("keyItem");
            boolean equipped = false;
            String uuid = "";
            if(nextObject.has("uuid"))
            {
                uuid = nextObject.getString("uuid");
            }
            if(nextObject.has("equipped"))
            {
                equipped = nextObject.getBoolean("equipped");
            }

            String name = (String)nextObject.get("name");
            String description = (String)nextObject.get("descriptions");
            int defence = nextObject.getInt("defence");
            Equipment e = new Equipment(name, description, value, keyItem, defence, id);
            e.isEqu = equipped;
            e.uniqueUserId = uuid;
            return e;
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
            stateObject.put("defence", defence);
            stateObject.put("equipped", isEqu);
            stateObject.put("OBJECT TYPE", "Equipment");
            stateObject.put("uuid", uniqueUserId);
            return stateObject;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }


        return null;

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
            Player.equipArmor(this);
            isEqu = true;
        }
        else
        {
            Player.equipArmor(new Equipment("None", "None", 0, true, 1));
        }
    }

    public void setDefence(int d)
    {
        defence = d;
    }

    public void setIsEqu(boolean x)
    {
        isEqu = x;
    }

    public int getDefence()
    {
        return defence;
    }

    @Override
    public boolean isEquipment() {
        return true;
    }

    @Override
    public boolean isWeapon() {
        return false;
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
