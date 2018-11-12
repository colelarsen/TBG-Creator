package com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates;

import com.example.colea.tbg_creator_larsen.GameObjects.Activities.TestActivity;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemTransition extends Transition {
    public String displayString;
    public String transitionString;
    public State toTrans;
    public Conditional conditional;
    public ArrayList<Item> items;
    public ArrayList<String> itemDescriptions;
    public int id;
    public String uniqueUserId = "";

    public String getUniqueUserId()
    {
        return (uniqueUserId.isEmpty())? ""+id : uniqueUserId;
    }
    public ItemTransition(String displayVal, String transVal, ArrayList<Item> item, ArrayList<String> itemDesc)
    {
        displayString = displayVal;
        transitionString = transVal;
        id = GameController.getId();
        items = item;
        itemDescriptions = itemDesc;
    }

    public int transId = -1;
    public int condId = -1;
    public ArrayList<Integer> itemIds;

    public ArrayList<Integer> chainIds;

    public ItemTransition(String displayVal, String transVal, ArrayList<Integer> itemId, ArrayList<String> itemDesc, int i, int transI, int condI, ArrayList<Integer> chains)
    {
        displayString = displayVal;
        transitionString = transVal;
        id = i;
        itemIds = itemId;
        itemDescriptions = itemDesc;
        transId = transI;
        condId = condI;
        chainIds = chains;
    }

    public void link(GameObjects gameObjects)
    {
        toTrans = (State)gameObjects.findObjectById(transId);
        conditional = (Conditional) gameObjects.findObjectById(condId);
        for(Integer i : chainIds)
        {
            addChain((Transition)gameObjects.findObjectById(i.intValue()));
        }
        ///////////MOST TRANSITIONS HAVE THIS


        items = new ArrayList<>();
        for(Integer i : itemIds)
        {
            items.add((Item)gameObjects.findObjectById(i.intValue()));
        }
    }

    public static ItemTransition fromJSON(JSONObject nextObject)
    {
         /*
        private ArrayList<Item> items;
        private ArrayList<String> itemDescriptions;
         */
        try {
            int id = nextObject.getInt("id");
            String displayString = nextObject.getString("displayString");
            String transitionString = nextObject.getString("transitionString");
            int toTransId = -1;
            if(nextObject.has("toTrans"))
            {
                toTransId = nextObject.getInt("toTrans");
            }
            int condId = -1;
            if(nextObject.has("conditional"))
            {
                condId = nextObject.getInt("conditional");
            }
            String uuid = "";
            if(nextObject.has("uuid"))
            {
                uuid = nextObject.getString("uuid");
            }

            JSONArray chainIdJSONArray = nextObject.getJSONArray("chainTransitions");
            ArrayList<Integer> chainIds = new ArrayList<>();
            for(int i = 0; i < chainIdJSONArray.length(); i++)
            {
                chainIds.add(chainIdJSONArray.getInt(i));
            }
            /////ALL TRANSITIONS SHOULD HAVE THIS////////////


            JSONArray itemsJSON = nextObject.getJSONArray("items");
            ArrayList<Integer> itemIds = new ArrayList<>();
            for(int i = 0; i < itemsJSON.length(); i++)
            {
                itemIds.add(itemsJSON.getInt(i));
            }

            JSONArray itemStringsJSON = nextObject.getJSONArray("itemDesc");
            ArrayList<String>itemStrings = new ArrayList<>();
            for(int i = 0; i < itemStringsJSON.length(); i++)
            {
                itemStrings.add(itemStringsJSON.getString(i));
            }
            ItemTransition itemTransition = new ItemTransition(displayString, transitionString, itemIds, itemStrings, id, toTransId, condId, chainIds);
            itemTransition.uniqueUserId = uuid;
            return itemTransition;

        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONObject toJSON()
    {
        /*
        private String displayString;
        private String transitionString;
        private State toTrans;
        private Conditional conditional;
        private ArrayList<Item> items;
        private ArrayList<String> itemDescriptions;
        public int id;
         */
        try {
            JSONObject stateObject = new JSONObject();
            stateObject.put("OBJECT TYPE", "ItemTransition");
            stateObject.put("displayString", displayString);
            stateObject.put("transitionString", transitionString);
            stateObject.put("uuid", uniqueUserId);
            if(conditional != null) {
                stateObject.put("conditional", conditional.getId());
            }
            stateObject.put("id", id);
            if(toTrans != null) {
                stateObject.put("toTrans", toTrans.getId());
            }

            JSONArray itemsArray = new JSONArray();
            for(Item t : items)
            {
                if(t != null) {
                    itemsArray.put(t.getId());
                }
            }
            stateObject.put("items", itemsArray);

            JSONArray itemDescr = new JSONArray();
            for(String t : itemDescriptions)
            {
                if(t != null) {
                    itemDescr.put(t);
                }
            }
            stateObject.put("itemDesc", itemDescr);

            JSONArray chainIds = new JSONArray();
            for(Transition t : chainTransitions)
            {
                if(t != null) {
                    chainIds.put(t.getId());
                }
            }
            stateObject.put("chainTransitions", chainIds);
            return stateObject;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void setState(State trans)
    {
        toTrans = trans;
    }

    public String getDisplayString()
    {
        return displayString;
    }

    public String getTransitionString()
    {
        String ret = transitionString;
        for(Transition transition : chainTransitions)
        {
            ret += "\n" + transition.getTransitionString();
        }
        return ret;
    }

    public State trans(TestActivity t)
    {
        for(Transition transition : chainTransitions)
        {
            transition.trans(t);
        }
        for(Item i : items)
        {
            Player.getPlayer().inventory.add(i);
        }
        items = null;
        return toTrans;
    }

    public String getItemDescriptions()
    {
        String ret = "";
        for(String s : itemDescriptions)
        {
            ret += "\n" + s;
        }
        return ret;
    }

    public void setConditional(Conditional cond)
    {
        conditional = cond;
    }

    public boolean check()
    {
        if(items != null) {
            if (conditional != null) {
                return conditional.check();
            }
            return true;
        }
        return false;
    }

    public ArrayList<Transition> chainTransitions = new ArrayList<>();
    @Override
    public void addChain(Transition t) {
        if(!t.hasChain()) {
            chainTransitions.add(t);
        }
    }

    @Override
    public boolean hasChain() {
        return !(chainTransitions.size() == 0);
    }

    public int getId()
    {
        return id;
    }

    @Override
    public boolean shouldStopButtons() {
        for(Transition t : chainTransitions)
        {
            if(t instanceof CombatTransition || t instanceof  ConvoTransition)
            {
                return true;
            }
        }
        return false;
    }
}
