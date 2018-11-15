package com.example.colea.tbg_creator_larsen.GameObjects.Conversation;

import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;

import org.json.JSONException;
import org.json.JSONObject;

public class TradingConversationTransition extends ConversationTransition {
    public String displayString;
    public ConversationState toTrans;
    public Conditional conditional;
    public int id;

    public String uniqueUserId = "";

    @Override
    public String getUUID() {
        return uniqueUserId;
    }

    @Override
    public String getEditMainId() {
        if(uniqueUserId.isEmpty())
        {
            return ""+id;
        }
        return uniqueUserId;
    }

    public TradingConversationTransition(String displayVal)
    {
        displayString = displayVal;
        id = GameController.getId();
    }

    public int transId = -1;
    public int condId = -1;
    public TradingConversationTransition(String displayVal, int i, int transI, int condI)
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

    public static TradingConversationTransition fromJSON(JSONObject nextObject)
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

            String uuid = "";
            if(nextObject.has("uuid"))
            {
                uuid = nextObject.getString("uuid");
            }
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
            TradingConversationTransition tradingConversationTransition = new TradingConversationTransition(disString, id, toTransId, condId);
            tradingConversationTransition.uniqueUserId = uuid;
            return tradingConversationTransition;
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
            stateObject.put("OBJECT TYPE", "TradingConversationTransition");
            stateObject.put("id", id);
            stateObject.put("displayString", displayString);
            stateObject.put("uuid", uniqueUserId);
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
