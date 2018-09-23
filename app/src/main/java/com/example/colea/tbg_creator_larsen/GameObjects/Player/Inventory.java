package com.example.colea.tbg_creator_larsen.GameObjects.Player;

import java.util.ArrayList;

public class Inventory {

    private static Inventory i = new Inventory();

    private ArrayList<Item> items;
    private int itemCap;
    public int gold;

    private Inventory()
    {
        gold = 0;
        itemCap = 0;
        items = new ArrayList<>();
    }

    private Inventory(int itemCa)
    {
        items = new ArrayList<>();
        itemCap = itemCa;
    }

    public static Inventory getInventory()
    {
        return i;
    }

    private void updateItemCap(int iCap)
    {
        Inventory temp = getInventory();
        temp.itemCap = iCap;
        updateInventory(temp);
    }

    private static void updateInventory(Inventory t)
    {
        i = t;
    }

    public void add(Item x)
    {
        Inventory temp = getInventory();
        if(temp.itemCap == 0 || temp.itemCap > temp.items.size())
        {
            temp.items.add(x);
            updateInventory(temp);
        }
    }

    public void drop(int itemNum)
    {
        Inventory temp = getInventory();
        Inventory i = new Inventory();
        for(Item item : temp.items)
        {
            if(item.getId() != itemNum)
            {
                i.items.add(item);
            }
        }
        i.gold = temp.gold;
        i.itemCap = temp.itemCap;
        updateInventory(i);
    }

    public String itemString()
    {
        String temp = "-----------\n";
        Inventory tempInv = getInventory();
        for(Item item : tempInv.items)
        {
            temp.concat(item.getName() + ", " + item.getValue() + ": " + item.getDescription());
        }
        temp.concat("-----------\n");
        return temp;
    }

    public Item findItemById(int id)
    {
        Inventory tempInv = getInventory();
        for(Item item : tempInv.items)
        {
            if(item.getId() == id)
            {
                return item;
            }
        }
        return null;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
