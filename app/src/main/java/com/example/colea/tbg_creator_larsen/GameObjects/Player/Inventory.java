package com.example.colea.tbg_creator_larsen.GameObjects.Player;

import java.util.ArrayList;

public class Inventory {

    private static Inventory i = new Inventory();

    private ArrayList<Item> items;
    public int itemCap;
    public int gold;

    private Inventory()
    {
        gold = 0;
        itemCap = 0;
        items = new ArrayList<>();
    }

    public Inventory(int itemCa)
    {
        items = new ArrayList<>();
        itemCap = itemCa;
    }

    private void updateItemCap(int iCap)
    {
        itemCap = iCap;
    }


    public void add(Item x)
    {
        if(itemCap == 0 || itemCap > items.size())
        {
            items.add(x);
        }
    }

    public void drop(int itemId)
    {
        Inventory i = new Inventory();
        boolean alreadyDropped = false;
        for(Item item : items)
        {
            if(item.getId() != itemId)
            {
                i.items.add(item);
            }
            else if(!alreadyDropped)
            {
                alreadyDropped = true;
            }
            else if(alreadyDropped)
            {
                i.items.add(item);
            }
        }
        items = i.items;
    }

    public String itemString(Inventory tempInv)
    {
        String temp = "-----------\n";
        for(Item item : tempInv.items)
        {
            temp.concat(item.getName() + ", " + item.getValue() + ": " + item.getDescription());
        }
        temp.concat("-----------\n");
        return temp;
    }

    public Item findItemById(int id)
    {
        for(Item item : items)
        {
            if(item.getId() == id)
            {
                return item;
            }
        }
        return null;
    }

    public Item findItemByName(String s)
    {
        for(Item item : items)
        {
            if(item.getName().compareTo(s) == 0)
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
