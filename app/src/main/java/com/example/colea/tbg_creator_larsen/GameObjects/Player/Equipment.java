package com.example.colea.tbg_creator_larsen.GameObjects.Player;

public class Equipment extends Item {
    static int uniqueItemNumCounter = 0;

    private int id;
    private int value;
    private String name;
    private String description;
    private boolean keyItem;
    private int defence;
    private boolean isEqu = false;

    public Equipment(String nam, String desc, int val, boolean key, int def)
    {
        name = nam;
        description = desc;
        value = val;
        keyItem = key;
        defence = def;
        id = Item.uniqueItemNumCounter++;
    }

    public void drop()
    {
        if(!keyItem)
        {
            Inventory.getInventory().drop(id);
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
