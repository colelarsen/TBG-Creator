package com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates;

import android.os.Handler;

import com.example.colea.tbg_creator_larsen.GameObjects.Activities.Combat;
import com.example.colea.tbg_creator_larsen.GameObjects.Activities.TestActivity;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationState;
import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Equipment;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Weapon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CombatTransition extends Transition {
    private String displayString;
    private String transitionString;
    private State toTrans;
    private Conditional conditional;
    private Enemy[] enemies;
    private ArrayList<Transition> chainTransitions = new ArrayList<>();
    private boolean oneTimeCombat = false;
    private boolean alreadyHappened = false;
    public int id;

    public CombatTransition(String displayVal, String transVal, Enemy[] enemis)
    {
        displayString = displayVal;
        transitionString = transVal;
        id = GameController.getId();
        enemies = enemis;
    }

    public int transId = -1;
    public int condId = -1;
    public int[] enemyIds;
    public ArrayList<Integer> chainIds;
    public CombatTransition(String displayVal, String transVal, int i, int[] enemyId, int transI, int condI, boolean hap, boolean one, ArrayList<Integer> chains)
    {
        displayString = displayVal;
        transitionString = transVal;
        id = i;
        enemyIds = enemyId;
        alreadyHappened = hap;
        oneTimeCombat = one;
        transId = transI;
        condId = condI;
        chainIds = chains;
    }

    public CombatTransition(String displayVal, String transVal, Enemy[] enemis, boolean oneTime)
    {
        displayString = displayVal;
        transitionString = transVal;
        id = GameController.getId();
        enemies = enemis;
        oneTimeCombat = oneTime;
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


        enemies = new Enemy[enemyIds.length];
        for(int i = 0; i < enemyIds.length; i++)
        {
            enemies[i] = (Enemy)gameObjects.findObjectById(enemyIds[i]);
        }
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
        if(!alreadyHappened)
        {
            Combat.enemies = enemies;
            Handler h=new Handler();
            final TestActivity test = t;
            h.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        if(oneTimeCombat)
                        {
                            alreadyHappened = true;
                        }
                        test.showCombat(enemies);
                    }
                }, 3000);
        }

        return toTrans;
    }

    public int getId()
    {
        return id;
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

    @Override
    public void addChain(Transition t) {
        if(!t.hasChain() && !(t instanceof ConvoTransition) && !(t instanceof CombatTransition)) {
            chainTransitions.add(t);
        }
    }

    @Override
    public boolean hasChain() {
        return !(chainTransitions.size() == 0);
    }

    @Override
    public boolean shouldStopButtons() {
        if(oneTimeCombat)
        {
            return !alreadyHappened;
        }
        else
        {
            return true;
        }
    }


    public static CombatTransition fromJSON(JSONObject nextObject)
    {
        /*
            JSONArray enemyIds = new JSONArray();
            for(Enemy t : enemies)
            {
                if(t != null) {
                    enemyIds.put(t.getId());
                }
            }
            stateObject.put("enemies", enemyIds);
            stateObject.put("chainTransitions", chainIds);

            stateObject.put("oneTimeCombat", oneTimeCombat);
            stateObject.put("alreadyHappened", alreadyHappened);
            return stateObject;
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



            JSONArray enemyIdsJSON = nextObject.getJSONArray("enemies");
            int[] enemyIds = new int[enemyIdsJSON.length()];
            for(int i = 0; i < enemyIdsJSON.length(); i++)
            {
                enemyIds[i] = enemyIdsJSON.getInt(i);
            }

            boolean oneTimeCombat = nextObject.getBoolean("oneTimeCombat");
            boolean alreadyHappened = nextObject.getBoolean("alreadyHappened");

            return new CombatTransition(displayString, transitionString, id, enemyIds, toTransId, condId, alreadyHappened, oneTimeCombat, chainIds);
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
        private Enemy[] enemies;
        private ArrayList<Transition> chainTransitions = new ArrayList<>();
        private boolean oneTimeCombat = false;
        private boolean alreadyHappened = false;
        public int id;
         */
        try {
            JSONObject stateObject = new JSONObject();
            stateObject.put("OBJECT TYPE", "CombatTransition");
            stateObject.put("displayString", displayString);
            stateObject.put("transitionString", transitionString);
            if(toTrans != null) {
                stateObject.put("toTrans", toTrans.getId());
            }
            if(conditional != null) {
                stateObject.put("conditional", conditional.getId());
            }
            stateObject.put("id", id);

            JSONArray enemyIds = new JSONArray();
            for(Enemy t : enemies)
            {
                if(t != null) {
                    enemyIds.put(t.getId());
                }
            }
            stateObject.put("enemies", enemyIds);

            JSONArray chainIds = new JSONArray();
            for(Transition t : chainTransitions)
            {
                if(t != null) {
                    chainIds.put(t.getId());
                }
            }
            stateObject.put("chainTransitions", chainIds);

            stateObject.put("oneTimeCombat", oneTimeCombat);
            stateObject.put("alreadyHappened", alreadyHappened);
            return stateObject;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
