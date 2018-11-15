package com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates;

import com.example.colea.tbg_creator_larsen.GameObjects.Activities.TestActivity;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.NormalConversationTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NormalTransition extends Transition {
    public String displayString;
    public String transitionString;
    public State toTrans;
    public Conditional conditional;
    public int id;
    public String uniqueUserId = "";

    public String getUniqueUserId()
    {
        return (uniqueUserId.isEmpty())? ""+id : uniqueUserId;
    }
    public NormalTransition(String displayVal, String transVal)
    {
        displayString = displayVal;
        transitionString = transVal;
        id = GameController.getId();
    }

    public int condId = -1;
    public int transId = -1;

    public ArrayList<Integer> chainIds;

    public NormalTransition(String displayVal, String transVal, int i, int condI, int transI, ArrayList<Integer> chains)
    {
        displayString = displayVal;
        transitionString = transVal;
        id = i;
        condId = condI;
        transId = transI;
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
    }

    public static NormalTransition fromJSON(JSONObject nextObject)
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
            String userId = "";
            if(nextObject.has("uuid"))
            {
                userId = nextObject.getString("uuid");
            }

            JSONArray chainIdJSONArray = nextObject.getJSONArray("chainTransitions");
            ArrayList<Integer> chainIds = new ArrayList<>();
            for(int i = 0; i < chainIdJSONArray.length(); i++)
            {
                chainIds.add(chainIdJSONArray.getInt(i));
            }
            /////ALL TRANSITIONS SHOULD HAVE THIS////////////
            NormalTransition norm = new NormalTransition(displayString, transitionString, id, condId, toTransId, chainIds);
            norm.uniqueUserId = userId;
            return norm;

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
        public int id;
         */
        try {
            JSONObject stateObject = new JSONObject();
            stateObject.put("OBJECT TYPE", "NormalTransition");
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
        return toTrans;
    }

    public void setConditional(Conditional cond)
    {
        conditional = cond;
    }

    public boolean check()
    {
        if(conditional != null) {
            return conditional.check();
        }
            return true;
    }

    public ArrayList<Transition> chainTransitions = new ArrayList<>();
    @Override
    public void addChain(Transition t) {
        if(!t.hasChain()) {
            chainTransitions.add(t);
        }
    }

    public int getId()
    {
        return id;
    }

    @Override
    public boolean hasChain() {
        return !(chainTransitions.size() == 0);
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
