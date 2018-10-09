package com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item;

import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Player;

public class DefenceEffect extends Effect{

    String name;
    String description;
    int defence = 0;
    int id;
    int duration;

    public DefenceEffect(String nam, String desc, int def, int dur)
    {
        name = nam;
        description = desc;
        defence = def;
        id = GameController.getId();
        duration = dur;
    }

    //This object can be Player or Enemy
    public String effect(Object o)
    {
        Player p = Player.getPlayer();
        String ef = "";
        if(o instanceof Player)
        {
            p.setDefence(p.defence() + defence);
            ef = name + " raised " + p.name + " defence";
        }
        else
        {
            Enemy enemy = (Enemy) o;
            enemy.setDefenceScore(enemy.defence() + defence);
            ef = p.name + " raised " + enemy.name + "'s defence with " + name;
        }
        return ef;
    }

    //Will always be Player or Enemy
    public void undo(Object o)
    {
        Player p = Player.getPlayer();
        if(o instanceof Player)
        {
            p.setDefence(p.defence() - defence);
        }
        else
        {
            Enemy enemy = (Enemy) o;
            enemy.setDefenceScore(enemy.defence() - defence);
        }
    }


    public  int getDuration()
    {
        return duration;
    }

    public boolean skipsTurn()
    {
        return false;
    }

    public boolean combatOnly()
    {
        return true;
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
