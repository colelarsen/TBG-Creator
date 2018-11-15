package com.example.colea.tbg_creator_larsen.GameObjects;

import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationCharacter;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationState;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Equipment;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Inventory;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Weapon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NPC implements ConversationCharacter {
    public ConversationState convoStart;
    public boolean canTrade;
    public Inventory inventory;
    public String name;
    public String uniqueUserId = "";
    public int id;
    public int convoStartId = -1;

    public ArrayList<Integer> itemIds;
    public void link(GameObjects gameObjects)
    {
        convoStart = (ConversationState) gameObjects.findObjectById(convoStartId);
        for(Integer i : itemIds)
        {
            inventory.add((Item)gameObjects.findObjectById(i.intValue()));
        }
    }

    public static NPC fromJSON(JSONObject nextObject)
    {
        /*
        stateObject.put("id", id);
            stateObject.put("canTrade", canTrade);
            stateObject.put("name", name);
            stateObject.put("convoStart", convoStart);

            JSONArray items = new JSONArray();
            for(Item i : inventory.getItems())
            {
                items.put(i.getId());
            }
            stateObject.put("items", items);
            stateObject.put("itemCap", inventory.itemCap);
            stateObject.put("gold", inventory.gold);
         */
        try {
            int id = nextObject.getInt("id");
            boolean canTrade = nextObject.getBoolean("canTrade");
            String name = (String)nextObject.get("name");
            int convoStartId = -1;
            if(nextObject.has("convoStart")) {
                convoStartId = nextObject.getInt("convoStart");
            }
            int itemCap = nextObject.getInt("itemCap");
            int gold = nextObject.getInt("gold");
            String uuid = "";
            if(nextObject.has("uuid"))
            {
                uuid = nextObject.getString("uuid");
            }


            ArrayList<Integer> itemIds = new ArrayList<>();
            if(nextObject.has("items")) {
                JSONArray items = nextObject.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    itemIds.add(items.getInt(i));
                }
            }

            Inventory i = new Inventory(itemCap);
            i.gold = gold;
            NPC npc = new NPC(name, canTrade, i, id);
            npc.convoStartId = convoStartId;
            npc.itemIds = itemIds;
            npc.uniqueUserId = uuid;
            return npc;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject toJSON()
    {

        try {
            JSONObject stateObject = new JSONObject();
            stateObject.put("OBJECT TYPE", "NPC");
            stateObject.put("id", id);
            stateObject.put("canTrade", canTrade);
            stateObject.put("name", name);
            if(convoStart != null) {
                stateObject.put("convoStart", convoStart.getId());
            }
            stateObject.put("uuid", uniqueUserId);

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

    public NPC(String nam, ConversationState con, boolean canTr, Inventory i, int g)
    {
        name = nam;
        convoStart = con;
        canTrade = canTr;
        inventory = i;
        id = GameController.getId();
        inventory.gold = g;
    }

    public NPC(String nam, boolean canTr, Inventory i, int ids)
    {
        name = nam;
        canTrade = canTr;
        inventory = i;
        id = ids;
    }

    public void setStartState(ConversationState state)
    {
        convoStart = state;
    }

    @Override
    public ConversationState getStart() {
        return convoStart;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean passiveState(int i) {
        return false;
    }

    public int getId()
    {
        return id;
    }
}
