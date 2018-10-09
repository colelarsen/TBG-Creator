package com.example.colea.tbg_creator_larsen.GameObjects.Player;

import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.DamagingEffect;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.Effect;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.HealingEffect;

import java.util.ArrayList;

public class Player implements CombatTracker{
    private static Player p = new Player(15, 20, "Cole");

    private int health;
    private int maxHealth;
    public String name;
    private int defenceScore = 0;
    private int attackScore = 1;
    private Weapon equippedWeapon = new Weapon("None", "None", 0, true, 1);
    private Equipment equippedArmor = new Equipment("None", "None", 0, true, 0);
    public ArrayList<Effect> spells = new ArrayList<>();
    public Inventory inventory;
    public int gold = 0;
    //public ArrayList<Skill> skills = new ArrayList<Skill>();

    private Player(int hp, int maxHP, String n)
    {
        health = hp;
        name = n;
        maxHealth = maxHP;
        inventory = new Inventory(0);
    }

    public static Player getPlayer()
    {
        if(p == null) {
            Player.p = new Player(15, 20, "Cole");
        }
        return p;
    }

    public static void setAttack(int i) { Player.getPlayer().attackScore = i; }

    public static void setDefence(int i)
    {
        Player.getPlayer().defenceScore = i;
    }

    public static void equipWeapon(Weapon w)
    {
        Player p = Player.getPlayer();
        if(p.equippedWeapon != null) {
            Player.getPlayer().equippedWeapon.setIsEqu(false);

            Player.setAttack(Player.getPlayer().attackScore - Player.getPlayer().equippedWeapon.getAttack());

            Player.getPlayer().equippedWeapon = w;
            Player.setAttack(Player.getPlayer().attackScore + Player.getPlayer().equippedWeapon.getAttack());
        }
        else
        {
            p.equippedWeapon = w;
            Player.setAttack(Player.getPlayer().attackScore + Player.getPlayer().equippedWeapon.getAttack());
        }
    }

    public static void setHealth(int h)
    {
        Player.getPlayer().health = Math.min(h, Player.getPlayer().maxHealth);
    }

    public static int getHealth()
    {
        return Player.getPlayer().health;
    }

    public static void equipArmor(Equipment e)
    {
        Player.getPlayer().equippedArmor.setIsEqu(false);

        Player.setDefence(Player.getPlayer().defenceScore - Player.getPlayer().equippedArmor.getDefence());

        Player.getPlayer().equippedArmor = e;
        Player.setDefence(Player.getPlayer().defenceScore + Player.getPlayer().equippedArmor.getDefence());
    }

    public int attack()
    {
        return equippedWeapon.getAttack() + attackScore;
    }

    public int defence()
    {
        return equippedArmor.getDefence() + defenceScore;
    }

    public int getArmorDef() {return equippedArmor.getDefence();}


    /*
    *****************************
    * Combat Tracker Stuff
    *****************************
    */
    private int turnCounter;
    private ArrayList<Integer> effectEnds;
    private ArrayList<Effect> effects;

    public void combatStart()
    {
        turnCounter = 0;
        effectEnds = new ArrayList<>();
        effects = new ArrayList<>();
    }



    public void turnOver()
    {
        turnCounter++;
    }

    public boolean shouldSkipTurn()
    {
        for(int i = 0; i < effects.size(); i++)
        {
            if(effectEnds.get(i) > turnCounter && effects.get(i).skipsTurn())
            {
                return true;
            }
        }
        return false;
    }

    public String applyEffect(Effect e)
    {
        String x = e.effect(Player.getPlayer());
        if(e.getDuration() > 0)
        {
            effects.add(e);
            effectEnds.add(turnCounter + e.getDuration());
        }
        return x;
    }

    public void turnStarts()
    {
        defending = false;
        for(int i = 0; i < effects.size(); i++)
        {
            if(effectEnds.get(i) == turnCounter)
            {
                effects.get(i).undo(getPlayer());
            }
        }
    }

    public boolean defending = false;
    public  boolean isDefending()
    {
        return defending;
    }

    public void combatOver()
    {
        for(int i = 0; i < effects.size(); i++)
        {
            effects.get(i).undo(getPlayer());
        }

        effects.clear();
        effectEnds.clear();
    }
}
