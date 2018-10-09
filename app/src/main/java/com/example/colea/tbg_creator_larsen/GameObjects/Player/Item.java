package com.example.colea.tbg_creator_larsen.GameObjects.Player;

import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.Effect;
import com.example.colea.tbg_creator_larsen.GameObjects.GameController;

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
