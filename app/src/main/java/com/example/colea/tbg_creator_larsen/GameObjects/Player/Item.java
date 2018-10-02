package com.example.colea.tbg_creator_larsen.GameObjects.Player;

public class Item {
    static int uniqueItemNumCounter = 0;

    private int id;
    private int value;
    private String name;
    private String description;
    private boolean keyItem;

    public Item()
    {

    }


    public Item(String nam, String desc, int val, boolean key)
    {
        name = nam;
        description = desc;
        value = val;
        keyItem = key;
        id = Item.uniqueItemNumCounter++;
    }

    public void drop()
    {
        if(!keyItem)
        {
            Inventory.getInventory().drop(id);
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
}
