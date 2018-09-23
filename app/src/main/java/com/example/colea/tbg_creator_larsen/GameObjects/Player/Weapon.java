package com.example.colea.tbg_creator_larsen.GameObjects.Player;

public class Weapon extends Item {
    static int uniqueItemNumCounter = 0;

    private int id;
    private int value;
    private String name;
    private String description;
    private boolean keyItem;
    private int attack;
    private boolean isEqu = false;

    public Weapon(String nam, String desc, int val, boolean key, int attak)
    {
        name = nam;
        description = desc;
        value = val;
        keyItem = key;
        attack = attak;
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
