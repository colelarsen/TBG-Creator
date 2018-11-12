package com.example.colea.tbg_creator_larsen.GameObjects.Player;

import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationState;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.DamagingEffect;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.Effect;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.HealingEffect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player implements CombatTracker{
    private static Player p = null;

    private int health;
    public int maxHealth;
    public String name;
    public int defenceScore = 0;
    public int attackScore = 0;
    public Weapon equippedWeapon;
    public Equipment equippedArmor;
    public ArrayList<Effect> spells = new ArrayList<>();
    public Inventory inventory;

    public ArrayList<Integer> itemIDs;
    public ArrayList<Integer> spellIDs;
    public int equipId = -1;
    public int weaponId = -1;
    private Player(int heal, int max, String nam, int defS, int attkS, int equipWeapI, int equipEquipI, ArrayList<Integer> items, ArrayList<Integer> spellIds, Inventory inventor)
    {
        health = heal;
        maxHealth = max;
        defenceScore = defS;
        attackScore = attkS;
        weaponId = equipWeapI;
        equipId = equipEquipI;
        itemIDs = items;
        spellIDs = spellIds;
        inventory = inventor;
        name = nam;
    }

    public void link(GameObjects gameObjects)
    {
        equippedWeapon = (Weapon) gameObjects.findObjectById(weaponId);
        equippedArmor = (Equipment) gameObjects.findObjectById(equipId);
        for(Integer i : spellIDs)
        {
            spells.add((Effect) gameObjects.findObjectById(i.intValue()));
        }
        for(Integer i : itemIDs)
        {
            inventory.add((Item)gameObjects.findObjectById(i.intValue()));
        }
    }

    public static Player fromJSON(JSONObject nextObject)
    {
        try {
            String name = nextObject.getString("name");
            int maxHp = nextObject.getInt("maxHealth");
            int health = nextObject.getInt("health");
            int defScore = nextObject.getInt("defenceScore");
            int attkScore = nextObject.getInt("attackScore");

            int equipWeapon = -1;
            int equipEquip = -1;
            if(nextObject.has("equippedWeapon"))
            {
                equipWeapon = nextObject.getInt("equippedWeapon");
            }
            if(nextObject.has("equippedArmor"))
            {
                equipEquip = nextObject.getInt("equippedArmor");
            }
            int itemCap = nextObject.getInt("itemCap");
            int gold = nextObject.getInt("gold");

            JSONArray spellsJSON = nextObject.getJSONArray("spells");
            ArrayList<Integer> spellIds = new ArrayList<>();
            for(int i = 0; i < spellsJSON.length(); i++)
            {
                spellIds.add(spellsJSON.getInt(i));
            }

            JSONArray itemsJSON = nextObject.getJSONArray("items");
            ArrayList<Integer> itemIds = new ArrayList<>();
            for(int i = 0; i < itemsJSON.length(); i++)
            {
                itemIds.add(itemsJSON.getInt(i));
            }

            Inventory inventory = new Inventory(itemCap);
            inventory.gold = gold;
            return new Player(health, maxHp, name, defScore, attkScore, equipWeapon, equipEquip, itemIds, spellIds, inventory);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject toJSON()
    {
        try {
            JSONObject stateObject = new JSONObject();
            stateObject.put("OBJECT TYPE", "Player");
            stateObject.put("name", name);
            stateObject.put("maxHealth", maxHealth);
            stateObject.put("health", health);
            stateObject.put("defenceScore", defenceScore);
            stateObject.put("attackScore", attackScore);
            if(equippedWeapon != null) {
                stateObject.put("equippedWeapon", equippedWeapon.getId());
            }
            if(equippedArmor != null) {
                stateObject.put("equippedArmor", equippedArmor.getId());
            }

            JSONArray spell = new JSONArray();
            for(Effect i : spells)
            {
                spell.put(i.getId());
            }
            stateObject.put("spells", spell);


            JSONArray items = new JSONArray();
            for(Item i : inventory.getItems())
            {
                items.put(i.getId());
            }
            stateObject.put("items", items);
            stateObject.put("itemCap", inventory.itemCap);
            stateObject.put("gold", inventory.gold);
            return stateObject;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }


        return null;

    }




    public Player(int hp, int maxHP, String n)
    {
        health = hp;
        name = n;
        maxHealth = maxHP;
        inventory = new Inventory(0);
    }

    public static void setPlayer(Player player)
    {
        p = player;
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

    public void equipWeaponPerPlayer(Weapon w)
    {
        if(equippedWeapon != null) {
            equippedWeapon.setIsEqu(false);
            attackScore = attackScore - equippedWeapon.getAttack();
            equippedWeapon = w;
            attackScore = attackScore + equippedWeapon.getAttack();
        }
        else
        {
            equippedWeapon = w;
            attackScore = attackScore + equippedWeapon.getAttack();
        }
    }

    public void equipArmorPerPlayer(Equipment e)
    {
        if(equippedArmor != null) {
            equippedArmor.setIsEqu(false);

            defenceScore = defenceScore - equippedArmor.getDefence();
            equippedArmor = e;
            defenceScore = defenceScore + equippedArmor.getDefence();
        }
        else
        {
            equippedArmor = e;
            defenceScore = defenceScore + equippedArmor.getDefence();
        }
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
        Player.getPlayer().health = Math.max(Math.min(h, Player.getPlayer().maxHealth), 0);
    }

    public static int getHealth()
    {
        return Player.getPlayer().health;
    }

    public static void equipArmor(Equipment e)
    {
        if(Player.getPlayer().equippedArmor != null) {
            Player.getPlayer().equippedArmor.setIsEqu(false);

            Player.setDefence(Player.getPlayer().defenceScore - Player.getPlayer().equippedArmor.getDefence());

            Player.getPlayer().equippedArmor = e;
            Player.setDefence(Player.getPlayer().defenceScore + Player.getPlayer().equippedArmor.getDefence());
        }
        else
        {
            Player.getPlayer().equippedArmor = e;
            Player.setDefence(Player.getPlayer().defenceScore + Player.getPlayer().equippedArmor.getDefence());
        }
    }

    public int attack()
    {
        return Math.max(attackScore, 1);
    }

    public int defence()
    {
        return defenceScore;
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
