package com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates;

import com.example.colea.tbg_creator_larsen.GameObjects.Activities.TestActivity;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.ConditionalSwitch;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SwitchLeverTransition extends Transition {
    private String displayString;
    private String transitionString;
    private State toTrans;
    private Conditional conditional;
    private String switc;
    public int id;

    public SwitchLeverTransition(String displayVal, String transVal, String swit)
    {
        displayString = displayVal;
        transitionString = transVal;
        id = GameController.getId();
        switc = swit;
    }

    public int condId = -1;
    public int transId = -1;
    public ArrayList<Integer> chainIds;
    public SwitchLeverTransition(String displayVal, String transVal, String swit, int condI, int transI, int i, ArrayList<Integer> chains)
    {
        displayString = displayVal;
        transitionString = transVal;
        id = i;
        switc = swit;
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

    public static SwitchLeverTransition fromJSON(JSONObject nextObject)
    {
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

            String switc = nextObject.getString("switc");
            /////ALL TRANSITIONS SHOULD HAVE THIS////////////
            return new SwitchLeverTransition(displayString, transitionString, switc, condId, toTransId, id, chainIds);

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
        try {
            JSONObject stateObject = new JSONObject();
            stateObject.put("OBJECT TYPE", "SwitchLeverTransition");
            stateObject.put("displayString", displayString);
            stateObject.put("transitionString", transitionString);
            if(conditional != null) {
                stateObject.put("conditional", conditional.getId());
            }
            stateObject.put("id", id);
            if(toTrans != null) {
                stateObject.put("toTrans", toTrans.getId());
            }

            stateObject.put("switc", switc);
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
        ConditionalSwitch.switchSwitch(switc);
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

    private ArrayList<Transition> chainTransitions = new ArrayList<>();
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
