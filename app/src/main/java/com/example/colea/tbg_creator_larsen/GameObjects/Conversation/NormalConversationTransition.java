package com.example.colea.tbg_creator_larsen.GameObjects.Conversation;

import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.NPC;

import org.json.JSONException;
import org.json.JSONObject;

public class NormalConversationTransition extends ConversationTransition{
    private String displayString;
    private ConversationState toTrans;
    private Conditional conditional;
    public int id;

    public NormalConversationTransition(String displayVal)
    {
        displayString = displayVal;
        id = GameController.getId();
    }

    public int transId = -1;
    public int condId = -1;
    public NormalConversationTransition(String displayVal, int i, int transI, int condI)
    {
        displayString = displayVal;
        id = i;
        transId = transI;
        condId = condI;
    }

    public void link(GameObjects gameObjects)
    {
        toTrans = (ConversationState) gameObjects.findObjectById(transId);
        conditional = (Conditional) gameObjects.findObjectById(condId);
    }

    public static NormalConversationTransition fromJSON(JSONObject nextObject)
    {
        try {
            /*
            stateObject.put("id", id);
            stateObject.put("displayString", displayString);
            if(toTrans != null) {
                stateObject.put("toTrans", toTrans.getId());
            }
            if(conditional != null)
            {
                stateObject.put("conditional", conditional.getId());
            }
            */

            int id = nextObject.getInt("id");
            String disString = nextObject.getString("displayString");

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
            return new NormalConversationTransition(disString, id, toTransId, condId);
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
            stateObject.put("OBJECT TYPE", "NormalConversationTransition");
            stateObject.put("id", id);
            stateObject.put("displayString", displayString);
            if(toTrans != null) {
                stateObject.put("toTrans", toTrans.getId());
            }
            if(conditional != null)
            {
                stateObject.put("conditional", conditional.getId());
            }
            return stateObject;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return null;
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
