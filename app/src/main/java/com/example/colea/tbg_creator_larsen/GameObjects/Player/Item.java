package com.example.colea.tbg_creator_larsen.GameObjects.Player;

import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationState;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.Effect;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.Transition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Item {
    private int id;
    private int value;
    private String name;
    private String description;
    private boolean keyItem;
    private boolean useable;
    private boolean inCombat;
    private Effect effect;

    public Item()
    {
    }


    public int effectId = -1;

    public void link(GameObjects gameObjects)
    {
        if(effectId != -1) {
            effect = (Effect)gameObjects.findObjectById(effectId);
        }
    }

    public static Item fromJSON(JSONObject nextObject)
    {
        /*
        stateObject.put("id", id);
        stateObject.put("name", name);
            stateObject.put("descriptions", description);
            stateObject.put("value", value);
            stateObject.put("keyItem", keyItem);
            if(effect != null) {
                stateObject.put("effect", effect.getId());
            }
            stateObject.put("useable", useable);
            stateObject.put("inCombat", inCombat);
            stateObject.put("OBJECT TYPE", "Item");
         */

        try {
            int id = nextObject.getInt("id");
            int value = nextObject.getInt("value");
            boolean keyItem = nextObject.getBoolean("keyItem");
            boolean useable = nextObject.getBoolean("useable");
            boolean inCombat = nextObject.getBoolean("inCombat");
            String name = (String)nextObject.get("name");
            String description = (String)nextObject.get("descriptions");
            int effectId = -1;
            if(nextObject.has("effect"))
            {
                effectId = nextObject.getInt("effect");
            }
            Item i = new Item(name, description, value, keyItem, useable, inCombat, id);
            i.effectId = effectId;
            return i;
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
            id = GameController.getId();
            effect = null;
            useable = false;
            inCombat = false;
             */
            JSONObject stateObject = new JSONObject();

            stateObject.put("id", id);
            stateObject.put("name", name);
            stateObject.put("descriptions", description);
            stateObject.put("value", value);
            stateObject.put("keyItem", keyItem);
            if(effect != null) {
                stateObject.put("effect", effect.getId());
            }
            stateObject.put("useable", useable);
            stateObject.put("inCombat", inCombat);
            stateObject.put("OBJECT TYPE", "Item");
            return stateObject;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }


        return null;

    }


    public Item(String nam, String desc, int val, boolean key)
    {
        name = nam;
        description = desc;
        value = val;
        keyItem = key;
        id = GameController.getId();
        effect = null;
        useable = false;
        inCombat = false;
    }

    public Item(String nam, String desc, int val, boolean key, boolean usea, boolean inComb, Effect e)
    {
        name = nam;
        description = desc;
        value = val;
        keyItem = key;
        id = GameController.getId();
        useable = usea;
        inCombat = inComb;
        effect = e;
    }
    public Item(String nam, String desc, int val, boolean key, boolean usea, boolean inComb, int i)
    {
        name = nam;
        description = desc;
        value = val;
        keyItem = key;
        id = i;
        useable = usea;
        inCombat = inComb;
    }

    public void drop()
    {
        if(!keyItem)
        {
            Player.getPlayer().inventory.drop(id);
        }
    }

    public boolean isWeapon()
    {
        return false;
    }

    public boolean isEquipment()
    {
        return false;
    }

    public boolean isEquipped()
    {
        return false;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public boolean isKeyItem() {
        return keyItem;
    }

    public boolean combatOnly() { return inCombat; }

    public boolean isUseable() { return useable; }

    public Effect getEffect(){ return effect;}
}
