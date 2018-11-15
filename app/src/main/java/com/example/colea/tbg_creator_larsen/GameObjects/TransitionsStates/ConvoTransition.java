package com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates;

import android.os.Handler;

import com.example.colea.tbg_creator_larsen.GameObjects.Activities.Conversation;
import com.example.colea.tbg_creator_larsen.GameObjects.Activities.TestActivity;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.NPC;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConvoTransition extends Transition {
    public String displayString;
    public String transitionString;
    public State toTrans;
    public Conditional conditional;
    public NPC npc;
    public int id;
    public ArrayList<Transition> chainTransitions = new ArrayList<>();
    public String uniqueUserId = "";

    public String getUniqueUserId()
    {
        return (uniqueUserId.isEmpty())? ""+id : uniqueUserId;
    }
    public int getId()
    {
        return id;
    }

    public ConvoTransition(String displayVal, String transVal, NPC npc1)
    {
        displayString = displayVal;
        transitionString = transVal;
        id = GameController.getId();
        npc = npc1;
    }

    public int transId = -1;
    public int condId = -1;
    public int npcId = -1;
    public ArrayList<Integer> chainIds;

    public ConvoTransition(String displayVal, String transVal, int npcI, int i, int transI, int condI, ArrayList<Integer> chainI)
    {
        displayString = displayVal;
        transitionString = transVal;
        id = i;
        npcId = npcI;
        transId = transI;
        condId = condI;
        chainIds = chainI;
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


        npc = (NPC) gameObjects.findObjectById(npcId);
    }


    public static ConvoTransition fromJSON(JSONObject nextObject)
    {
         /*
        private NPC npc;
         */
        try {
            int id = nextObject.getInt("id");
            String displayString = nextObject.getString("displayString");
            String transitionString = nextObject.getString("transitionString");
            String uuid = "";
            if(nextObject.has("uuid"))
            {
                uuid = nextObject.getString("uuid");
            }

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
            /////ALL TRANSITIONS SHOULD HAVE THIS////////////

            int npcId = -1;
            if(nextObject.has("npc"))
            {
                npcId = nextObject.getInt("npc");
            }
            ConvoTransition conversationTransition = new ConvoTransition(displayString, transitionString, npcId, id, toTransId, condId, chainIds);
            conversationTransition.uniqueUserId = uuid;
            return conversationTransition;
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
        private NPC npc;
        public int id;
         */
        try {
            JSONObject stateObject = new JSONObject();
            stateObject.put("OBJECT TYPE", "ConvoTransition");
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
            stateObject.put("npc", npc.getId());

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
        if(npc.canTrade) {
            Conversation.currentNPC = npc;
            Handler h=new Handler();
            final TestActivity test = t;
            h.postDelayed(new Runnable(){
                @Override
                public void run(){
                    test.showConversation();
                }
            }, 3000);

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
        return true;
    }
}
