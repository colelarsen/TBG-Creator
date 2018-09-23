package com.example.colea.tbg_creator_larsen.GameObjects.Player;

import java.util.ArrayList;

public class Player {
    private static Player p = new Player(15, "Cole");

    public int health;
    public String name;
    public int defenceScore = 0;
    private Weapon equippedWeapon = new Weapon("None", "None", 0, true, 1);
    private Equipment equippedArmor = new Equipment("None", "None", 0, true, 1);
    //public ArrayList<Spell> spells = new ArrayList<Spell>();
    //public ArrayList<Skill> skills = new ArrayList<Skill>();

    private Player(int hp, String n)
    {
        health = hp;
        name = n;
    }

    public static Player getPlayer()
    {
        return p;
    }

    public static void setDefence(int i)
    {
        Player.getPlayer().defenceScore = i;
    }

    public static void equipWeapon(Weapon w)
    {
        Player.getPlayer().equippedWeapon.setIsEqu(false);
        Player.getPlayer().equippedWeapon = w;
    }

    public static void equipArmor(Equipment e)
    {
        Player.getPlayer().equippedArmor.setIsEqu(false);
        Player.getPlayer().equippedArmor = e;
    }
}
