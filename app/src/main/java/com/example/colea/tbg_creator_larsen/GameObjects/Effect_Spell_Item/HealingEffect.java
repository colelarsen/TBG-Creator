package com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item;

import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Player;

public class HealingEffect extends Effect{

    String name;
    String description;
    int healing = 0;
    int id;

    public HealingEffect(String nam, String desc, int heal)
    {
        name = nam;
        description = desc;
        healing = heal;
        id = GameController.getId();
    }

    //This object can be Player or Enemy
    public String effect(Object o)
    {
        Player p = Player.getPlayer();
        String ef = "";
        if(o instanceof Player)
        {
            p.setHealth(p.getHealth() + healing);
            ef = name + " healed " + healing + " to " + p.name;
        }
        else
        {
            Enemy enemy = (Enemy) o;
            enemy.health += healing;
            ef = p.name + " healed " + healing + " to " + enemy.name + " with " + name;
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
        return false;
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
