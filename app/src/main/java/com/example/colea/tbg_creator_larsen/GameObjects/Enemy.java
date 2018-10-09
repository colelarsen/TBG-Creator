package com.example.colea.tbg_creator_larsen.GameObjects;

import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationCharacter;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationState;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.Effect;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.CombatTracker;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Equipment;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Weapon;
import java.util.ArrayList;

public class Enemy implements CombatTracker, ConversationCharacter {
    public int health;
    public String name;
    public int defenceScore = 0;
    public int attackScore = 0;
    public String description;
    private Weapon equippedWeapon = new Weapon("None", "None", 0, true, 1);
    private Equipment equippedArmor = new Equipment("None", "None", 0, true, 0);
    private Item[] drops;
    private double[] dropChance;
    public boolean canConverse;
    public ConversationState convoStart;
    public ConversationState passState;
    public boolean isPassive = false;


    //public ArrayList<Spell> spells = new ArrayList<Spell>();
    //public ArrayList<Skill> skills = new ArrayList<Skill>();

    public Enemy(int hp, String n, String desc, Weapon weapo, Equipment equip, Item[] drop, double[] dropChanc, boolean canCon, ConversationState convo, ConversationState pass)
    {
        health = hp;
        name = n;
        description = desc;
        equippedWeapon = weapo;
        equippedArmor = equip;
        drops = drop;
        dropChance = dropChanc;
        canConverse = canCon;
        convoStart = convo;
        passState = pass;
    }

    //THIS IS A PLACEHOLDER NEVER USE THIS BESIDES FOR COMBAT
    public Enemy()
    {

    }

    public void setDefenceScore(int i)
    {
        defenceScore = i;
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

    //Conversation stuff


    @Override
    public String getName() {
        return name;
    }

    public ConversationState getStart() {
        return convoStart;
    }

    @Override
    public boolean passiveState(int id) {
        return passState.getId() == id;
    }

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

    public void turnStarts()
    {
        defending = false;
        for(int i = 0; i < effects.size(); i++)
        {
            if(effectEnds.get(i) == turnCounter)
            {
                effects.get(i).undo(this);
            }
        }
    }

    public boolean defending = false;
    public  boolean isDefending()
    {
        return defending;
    }

    public String applyEffect(Effect e)
    {
        String x = e.effect(this);
        if(e.getDuration() > 0)
        {
            effects.add(e);
            effectEnds.add(turnCounter + e.getDuration());
        }
        return x;
    }

    public void combatOver()
    {
        for(int i = 0; i < effects.size(); i++)
        {
            effects.get(i).undo(this);
        }

        effects.clear();
        effectEnds.clear();
    }
}
