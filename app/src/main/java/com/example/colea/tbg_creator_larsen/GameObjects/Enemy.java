package com.example.colea.tbg_creator_larsen.GameObjects;

import com.example.colea.tbg_creator_larsen.GameObjects.Player.Equipment;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Weapon;
import java.util.ArrayList;

public class Enemy {
    public int health;
    public String name;
    public int defenceScore = 0;
    public String description;
    private Weapon equippedWeapon = new Weapon("None", "None", 0, true, 1);
    private Equipment equippedArmor = new Equipment("None", "None", 0, true, 0);
    private Item[] drops;
    private double[] dropChance;
    public boolean canConverse;


    //public ArrayList<Spell> spells = new ArrayList<Spell>();
    //public ArrayList<Skill> skills = new ArrayList<Skill>();

    public Enemy(int hp, String n, String desc, Weapon weapo, Equipment equip, Item[] drop, double[] dropChanc, boolean canCon)
    {
        health = hp;
        name = n;
        description = desc;
        equippedWeapon = weapo;
        equippedArmor = equip;
        drops = drop;
        dropChance = dropChanc;
        canConverse = canCon;
    }

    //THIS IS A PLACEHOLDER NEVER USE THIS BESIDES FOR COMBAT
    public Enemy()
    {

    }

    public int attack()
    {
        return equippedWeapon.getAttack();
    }

    public int defence()
    {
        return equippedArmor.getDefence();
    }
}
