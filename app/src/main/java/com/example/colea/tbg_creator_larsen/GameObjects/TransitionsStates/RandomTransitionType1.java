package com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates;

import com.example.colea.tbg_creator_larsen.GameObjects.Activities.TestActivity;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RandomTransitionType1 extends Transition {

    //TYPE 1 OF RANDOM

    private String displayString;
    private Conditional conditional;
    private ArrayList<Transition> transitions;
    private ArrayList<Double> lowChance = new ArrayList<>();
    private ArrayList<Double> highChance = new ArrayList<>();
    public int id;


    public RandomTransitionType1(String displayVal, ArrayList<Transition> transitio)
    {
        displayString = displayVal;
        id = GameController.getId();
        transitions = transitio;
        for(int i = 0; i < transitio.size(); i++)
        {
            if(i == transitio.size() - 1)
            {
                highChance.add(1.0);
                lowChance.add((1.0/transitio.size()) * (i));
            }
            else
            {
                highChance.add((1.0/transitio.size()) * (i+1));
                lowChance.add((1.0/transitio.size()) * (i));
            }
        }
    }

    public RandomTransitionType1(String displayVal, ArrayList<Transition> transitio, ArrayList<Double> chanc)
    {
        displayString = displayVal;
        id = GameController.getId();
        transitions = transitio;
        for(int i = 0; i < chanc.size(); i++)
        {
            if(i == 0)
            {
                lowChance.add(0.0);
                highChance.add(chanc.get(i));
            }
            else if(i == chanc.size()-1)
            {
                lowChance.add(1-chanc.get(chanc.size()-1));
                highChance.add(1.0);
            }
            else {
                highChance.add(highChance.get(i-1) + chanc.get(i));
                lowChance.add(highChance.get(i-1));
            }
        }
    }

    public ArrayList<Integer> transitionIDs;
    public int condId = -1;
    public RandomTransitionType1(String displayVal, ArrayList<Integer> transitio, ArrayList<Double> lowCha, ArrayList<Double> highCha, int condI, int i)
    {
        displayString = displayVal;
        id = i;
        transitionIDs = transitio;
        lowChance = lowCha;
        highChance = highCha;
        condId = condI;

    }

    public void link(GameObjects gameObjects)
    {
        conditional = (Conditional) gameObjects.findObjectById(condId);
        ///////////MOST TRANSITIONS HAVE THIS
        transitions = new ArrayList<>();
        for(Integer i : transitionIDs)
        {
            transitions.add((Transition)gameObjects.findObjectById(i.intValue()));
        }
    }


    public static RandomTransitionType1 fromJSON(JSONObject nextObject)
    {
        /*
        private boolean goneYet;
         */
        try {
            int id = nextObject.getInt("id");
            String displayString = nextObject.getString("displayString");
            int condId = -1;
            if(nextObject.has("conditional"))
            {
                condId = nextObject.getInt("conditional");
            }

            JSONArray transIdsJSON = nextObject.getJSONArray("transitions");
            ArrayList<Integer> transIds = new ArrayList<>();
            for(int i = 0; i < transIdsJSON.length(); i++)
            {
                transIds.add(transIdsJSON.getInt(i));
            }

            JSONArray lowChanceJSON = nextObject.getJSONArray("lowChance");
            ArrayList<Double> lowChance = new ArrayList<>();
            for(int i = 0; i < lowChanceJSON.length(); i++)
            {
                lowChance.add(lowChanceJSON.getDouble(i));
            }

            JSONArray highChanceJSON = nextObject.getJSONArray("highChance");
            ArrayList<Double> highChance = new ArrayList<>();
            for(int i = 0; i < highChanceJSON.length(); i++)
            {
                highChance.add(highChanceJSON.getDouble(i));
            }
            return new RandomTransitionType1(displayString, transIds, lowChance, highChance, condId, id);

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
            stateObject.put("OBJECT TYPE", "RandomTransitionType1");
            stateObject.put("displayString", displayString);
            if(conditional != null) {
                stateObject.put("conditional", conditional.getId());
            }
            stateObject.put("id", id);
            JSONArray lowArray = new JSONArray();
            for(Double t : lowChance)
            {
                if(t != null) {
                    lowArray.put(t);
                }
            }
            stateObject.put("lowChance", lowArray);

            JSONArray highArray = new JSONArray();
            for(Double t : highChance)
            {
                if(t != null) {
                    highArray.put(t);
                }
            }
            stateObject.put("highChance", highArray);

            JSONArray transIds = new JSONArray();
            for(Transition t : transitions)
            {
                if(t != null) {
                    transIds.put(t.getId());
                }
            }
            stateObject.put("transitions", transIds);

            JSONArray chainIds = new JSONArray();
            for(Transition t : chainTransitions)
            {
                if(t != null) {
                    chainIds.put(t.getId());
                }
            }
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
    }

    public String getDisplayString()
    {
        return displayString;
    }

    private int choice = -1;
    public void determineChoice()
    {
        if(choice == -1)
        {
            double chance = Math.random();
            for(int i = 0; i < lowChance.size(); i++)
            {
                if(chance >= lowChance.get(i) && chance <= highChance.get(i))
                {
                    choice = i;
                }
            }
        }
    }

    public String getTransitionString()
    {
        determineChoice();
        String ret = transitions.get(choice).getTransitionString();
        for(Transition transition : chainTransitions)
        {
            ret += "\n" + transition.getTransitionString();
        }
        return ret;
    }

    public State trans(TestActivity t)
    {
        determineChoice();
        for(Transition transition : chainTransitions)
        {
            transition.trans(t);
        }
        State state = transitions.get(choice).trans(t);
        return state;
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

    public int getId()
    {
        return id;
    }

    private ArrayList<Transition> chainTransitions = new ArrayList<>();
    @Override
    public void addChain(Transition t) {
    }

    @Override
    public boolean hasChain() {
        return false;
    }

    @Override
    public boolean shouldStopButtons() {

        if(transitions.get(choice) instanceof CombatTransition || transitions.get(choice) instanceof ConvoTransition)
        {
            choice = -1;
            return true;
        }
        choice = -1;
        return false;
    }
}
