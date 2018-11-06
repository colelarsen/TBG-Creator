package com.example.colea.tbg_creator_larsen.GameObjects.Conversation;

import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.NPC;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChangeBaseStateConversationTransition extends ConversationTransition{
    private String displayString;
    private ConversationState toTrans;
    private Conditional conditional;
    public int id;
    private ConversationCharacter npc;
    private ConversationState toBaseState;

    public ChangeBaseStateConversationTransition(String displayVal)
    {
        displayString = displayVal;
        id = GameController.getId();
    }

    public int npcId = -1;
    public int toTransId = -1;
    public int toBaseId = -1;
    public int condId = -1;
    public ChangeBaseStateConversationTransition(String displayVal, int i, int npcI, int toTransI, int toBaseI, int condI)
    {
        displayString = displayVal;
        id = i;
        npcId = npcI;
        toTransId = toTransI;
        toBaseId = toBaseI;
        condId = condI;
    }

    public void link(GameObjects gameObjects)
    {
        npc = (NPC) gameObjects.findObjectById(npcId);
        toTrans = (ConversationState) gameObjects.findObjectById(toTransId);
        toBaseState = (ConversationState) gameObjects.findObjectById(toBaseId);
        conditional = (Conditional) gameObjects.findObjectById(condId);
    }

    public static ChangeBaseStateConversationTransition fromJSON(JSONObject nextObject)
    {
        try {
            /*
            stateObject.put("id", id);
            stateObject.put("displayString", displayString);
            stateObject.put("npc", npc.getId());
            if(toTrans != null) {
                stateObject.put("toTrans", toTrans.getId());
            }
            if(conditional != null)
            {
                stateObject.put("conditional", conditional.getId());
            }
            stateObject.put("toBase", toBaseState.getId());
            return stateObject;
            */

            int id = nextObject.getInt("id");
            String disString = nextObject.getString("displayString");
            int npcId = nextObject.getInt("npc");

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
            int baseId = -1;
            if(nextObject.has("toBase"))
            {
                baseId = nextObject.getInt("toBase");
            }
            return new ChangeBaseStateConversationTransition(disString, id, npcId, toTransId, baseId, condId);
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
            stateObject.put("OBJECT TYPE", "ChangeBaseStateConversationTransition");
            stateObject.put("id", id);
            stateObject.put("displayString", displayString);
            stateObject.put("npc", npc.getId());
            if(toTrans != null) {
                stateObject.put("toTrans", toTrans.getId());
            }
            if(conditional != null)
            {
                stateObject.put("conditional", conditional.getId());
            }
            if(toBaseState != null) {
                stateObject.put("toBase", toBaseState.getId());
            }
            return stateObject;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void setConvoCharacter(ConversationCharacter convoCharacter, ConversationState conversationState)
    {
        npc = convoCharacter;
        toBaseState = conversationState;
    }

    public void setState(ConversationState trans)
    {
        toTrans = trans;
    }

    public String getDisplayString()
    {
        return displayString;
    }

    public int getId()
    {
        return id;
    }

    public ConversationState getState()
    {
        npc.setStartState(toBaseState);
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
}
