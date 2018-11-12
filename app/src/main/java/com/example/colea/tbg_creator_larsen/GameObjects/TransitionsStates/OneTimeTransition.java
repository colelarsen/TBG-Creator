package com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates;

import com.example.colea.tbg_creator_larsen.GameObjects.Activities.TestActivity;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OneTimeTransition extends Transition {
    public String displayString;
    public String transitionString;
    public State toTrans;
    public Conditional conditional;
    public boolean goneYet = false;
    public int id;
    public String uniqueUserId = "";
    public String getUniqueUserId()
    {
        return (uniqueUserId.isEmpty())? ""+id : uniqueUserId;
    }

    public OneTimeTransition(String displayVal, String transVal)
    {
        displayString = displayVal;
        transitionString = transVal;
        id = GameController.getId();
    }

    public int condId = -1;
    public int transId = -1;
    public ArrayList<Integer> chainIds;
    public OneTimeTransition(String displayVal, String transVal, int i, int condI, int transI, boolean gone, ArrayList<Integer> chains)
    {
        displayString = displayVal;
        transitionString = transVal;
        id = i;
        transId = transI;
        condId = condI;
        goneYet = gone;
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

    public static OneTimeTransition fromJSON(JSONObject nextObject)
    {
        /*
        private boolean goneYet;
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

            JSONArray chainIdJSONArray = nextObject.getJSONArray("chainTransitions");
            ArrayList<Integer> chainIds = new ArrayList<>();
            for(int i = 0; i < chainIdJSONArray.length(); i++)
            {
                chainIds.add(chainIdJSONArray.getInt(i));
            }
            String uuid = "";
            if(nextObject.has("uuid"))
            {
                uuid = nextObject.getString("uuid");
            }

            boolean goneYet = nextObject.getBoolean("goneYet");
            /////ALL TRANSITIONS SHOULD HAVE THIS////////////
            OneTimeTransition oneTimeTransition = new OneTimeTransition(displayString, transitionString, id, condId, toTransId, goneYet, chainIds);
            oneTimeTransition.uniqueUserId = uuid;
            return oneTimeTransition;

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
        private boolean goneYet;
         */
        try {
            JSONObject stateObject = new JSONObject();
            stateObject.put("OBJECT TYPE", "OneTimeTransition");
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
            stateObject.put("goneYet", goneYet);
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


    public int getId()
    {
        return id;
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
        goneYet = true;
        return toTrans;
    }

    public void setConditional(Conditional cond)
    {
        conditional = cond;
    }

    public boolean check()
    {
        if(!goneYet) {
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
