package com.example.colea.tbg_creator_larsen.GameObjects;

import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationCharacter;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationState;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.Effect;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.CombatTracker;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Equipment;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Weapon;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.State;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.Transition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Enemy implements CombatTracker, ConversationCharacter {
    public int health;
    public int maxHealth;
    public String name;
    public int defenceScore = 0;
    public int attackScore = 0;
    public String description;
    private Weapon equippedWeapon;
    private Equipment equippedArmor;
    public ArrayList<Item> drops;
    public ArrayList<Double> dropChance;
    public boolean canConverse;
    public ConversationState convoStart;
    public ConversationState passState;
    public boolean isPassive = false;
    public int id;


    public static Enemy clone(Enemy e)
    {
        Enemy newEnemy = new Enemy();
        newEnemy.name = e.name;
        newEnemy.health = e.health;
        newEnemy.maxHealth = e.maxHealth;
        newEnemy.defenceScore = e.defenceScore;
        newEnemy.attackScore = e.attackScore;
        newEnemy.description = e.description;
        newEnemy.equippedArmor = e.equippedArmor;
        newEnemy.equippedWeapon = e.equippedWeapon;
        newEnemy.drops = e.drops;
        newEnemy.dropChance = e.dropChance;
        newEnemy.canConverse = e.canConverse;
        newEnemy.convoStart = e.convoStart;
        newEnemy.passState = e.passState;
        newEnemy.isPassive = e.isPassive;
        return newEnemy;
    }

    //public ArrayList<Spell> spells = new ArrayList<Spell>();
    //public ArrayList<Skill> skills = new ArrayList<Skill>();

    public Enemy(int hp, int maxHp, String n, String desc, Weapon weapo, Equipment equip, ArrayList<Item> drop, ArrayList<Double> dropChanc, boolean canCon, ConversationState convo, ConversationState pass)
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
        maxHealth = maxHp;
        id = GameController.getId();
    }

    public Enemy(int hp, int maxHp, String n, String desc, ArrayList<Double> dropChanc, boolean canCon, int i)
    {
        health = hp;
        name = n;
        description = desc;
        dropChance = dropChanc;
        canConverse = canCon;
        maxHealth = maxHp;
        id = i;
    }


    public int weaponId;
    public int equipId;
    public ArrayList<Integer> dropIds;
    public int convoStartId;
    public int passStateId;

    public void link(GameObjects gameObjects)
    {
        convoStart = (ConversationState) gameObjects.findObjectById(convoStartId);
        passState = (ConversationState) gameObjects.findObjectById(passStateId);
        equippedWeapon = (Weapon) gameObjects.findObjectById(weaponId);
        equippedArmor = (Equipment) gameObjects.findObjectById(equipId);
        drops = new ArrayList<>();
        for(int i = 0; i < dropIds.size(); i++)
        {
            Item drop = (Item) gameObjects.findObjectById(dropIds.get(i).intValue());
            drops.add(drop);
        }
    }

    public static Enemy fromJSON(JSONObject nextObject)
    {
        int id = -1;
        try {
            id = nextObject.getInt("id");
            int hp = nextObject.getInt("health");
            int maxHP = nextObject.getInt("maxHealth");
            String name = (String)nextObject.get("name");
            String description = (String)nextObject.get("description");

            int weaponId = -1;
            int equipId = -1;

            if(nextObject.has("equippedWeapon")) {
                weaponId = nextObject.getInt("equippedWeapon");
            }
            if(nextObject.has("equippedArmor")) {
                equipId = nextObject.getInt("equippedArmor");
            }
            int convoStartId = -1;
            if(nextObject.has("convoStart")) {
                convoStartId = nextObject.getInt("convoStart");
            }
            int passStateId = -1;
            if(nextObject.has("passState")) {
                passStateId = nextObject.getInt("passState");
            }
            boolean canConverse = nextObject.getBoolean("canConverse");


            ArrayList<Double> dropChance = new ArrayList<>();
            if(nextObject.has("dropChance")) {
                JSONArray dropChanceArray = nextObject.getJSONArray("dropChance");
                for (int i = 0; i < dropChanceArray.length(); i++) {
                    if (dropChanceArray.get(i) instanceof Double) {
                        Double d = (Double) dropChanceArray.get(i);
                        dropChance.add(d);
                    } else {
                        Double d = new Double(dropChanceArray.getInt(i));
                        dropChance.add(d);
                    }
                }
            }

            //Still needs: drops, weaponId, equipmentId
            Enemy enemy = new Enemy(hp, maxHP, name, description, dropChance, canConverse, id);

            ArrayList<Integer> drops = new ArrayList<>();
            if(nextObject.has("drops")) {
                JSONArray dropArray = nextObject.getJSONArray("drops");
                for (int i = 0; i < dropArray.length(); i++) {
                    Integer integer = (int) dropArray.get(i);
                    drops.add(integer);
                }
            }
            enemy.dropIds = drops;
            enemy.weaponId = weaponId;
            enemy.equipId = equipId;
            enemy.passStateId = passStateId;
            enemy.convoStartId = convoStartId;
            return enemy;
        }
        catch (JSONException e)
        {
            System.out.print("AAAAAAAAAA FAILED AT :" + id);
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject toJSON()
    {
        try {
            JSONObject stateObject = new JSONObject();
            stateObject.put("OBJECT TYPE", "Enemy");
            stateObject.put("id", id);
            stateObject.put("health", health);
            stateObject.put("name", name);
            stateObject.put("description", description);
            if(equippedWeapon != null) {
                stateObject.put("equippedWeapon", equippedWeapon.getId());
            }
            if(equippedArmor != null) {
                stateObject.put("equippedArmor", equippedArmor.getId());
            }
            stateObject.put("canConverse", canConverse);
            if(convoStart != null) {
                stateObject.put("convoStart", convoStart.getId());
            }
            if(passState != null) {
                stateObject.put("passState", passState.getId());
            }
            stateObject.put("maxHealth", maxHealth);

            if(drops != null) {
                JSONArray dropItems = new JSONArray();

                for (Item i : drops) {
                    dropItems.put(i.getId());
                }
                stateObject.put("drops", dropItems);
            }

            if(dropChance != null) {
                JSONArray dropItemsChance = new JSONArray();
                for (Double d : dropChance) {
                    dropItemsChance.put(d);
                }
                stateObject.put("dropChance", dropItemsChance);
            }
            return stateObject;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    //THIS IS A PLACEHOLDER NEVER USE THIS BESIDES FOR COMBAT
    public Enemy()
    {
        id = GameController.getId();
    }

    public int getId()
    {
        return id;
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

    public void setStartState(ConversationState state)
    {
        convoStart = state;
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
        health = maxHealth;
        turnCounter = 0;
        effectEnds = new ArrayList<>();
        effects = new ArrayList<>();
        isPassive = false;
    }

    public void turnOver()
    {
        turnCounter++;
    }

    public boolean shouldSkipTurn()
    {
        checkHealth();
        for(int i = 0; i < effects.size(); i++)
        {
            if(effectEnds.get(i) > turnCounter && effects.get(i).skipsTurn())
            {
                return true;
            }
        }
        return false;
    }

    private void checkHealth()
    {
        health = Math.min(health, maxHealth);
        health = Math.max(health, 0);
    }

    public void turnStarts()
    {
        defending = false;
        checkHealth();
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
        checkHealth();
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
