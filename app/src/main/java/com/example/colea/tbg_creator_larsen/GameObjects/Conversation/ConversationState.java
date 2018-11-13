package com.example.colea.tbg_creator_larsen.GameObjects.Conversation;

import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.Transition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConversationState {

    public ConversationTransition[] transitions = new ConversationTransition[4];
    public String text;
    public int id;
    public String uniqueUserId = "";

    private int[] transitionIds;

    public ConversationState(String desc) {
        text = desc;
        id = GameController.getId();
    }

    public ConversationState(String desc, int i, int[] transIDs) {
        text = desc;
        id = i;
        transitionIds = transIDs;
    }

    public void link(GameObjects gameObjects)
    {
        for(int i = 0; i < 4; i++)
        {
            if(transitionIds[i] != -1) {
                transitions[i] = (ConversationTransition) gameObjects.findObjectById(transitionIds[i]);
            }
        }
    }

    public static ConversationState fromJSON(JSONObject nextObject)
    {
        try {
            int id = nextObject.getInt("id");
            String text = nextObject.getString("text");

            String uuid = "";
            if(nextObject.has("uuid"))
            {
                uuid = nextObject.getString("uuid");
            }

            int[] transIds = new int[4];
            JSONArray ids = nextObject.getJSONArray("transitions");
            for(int i = 0; i < ids.length(); i++)
            {
                transIds[i] = ids.getInt(i);
            }
            ConversationState conversationState = new ConversationState(text, id, transIds);
            conversationState.uniqueUserId = uuid;
            return conversationState;
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
            stateObject.put("OBJECT TYPE", "ConversationState");
            stateObject.put("id", id);
            stateObject.put("text", text);
            stateObject.put("uuid", uniqueUserId);
            JSONArray ids = new JSONArray();
            for(ConversationTransition t : transitions)
            {
                if(t != null) {
                    ids.put(t.getId());
                }
                else
                {
                    ids.put(-1);
                }
            }
            stateObject.put("transitions", ids);

            return stateObject;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public int getId() {
        return id;
    }

    //Temporary
    public static ConversationState getStartState() {
        ConversationState startState = new ConversationState("Hello");
        NormalConversationTransition option1 = new NormalConversationTransition("Hello There");
        NormalConversationTransition option2 = new NormalConversationTransition("Goodbye");
        NormalConversationTransition option3 = new NormalConversationTransition("Heck Yourself");
        NormalConversationTransition option4 = new NormalConversationTransition("Uhhhh...");

        ConversationState state10 = new ConversationState("Welp, see Ya");
        ConversationState state11 = new ConversationState("Goodbye?");
        ConversationState state12 = new ConversationState("NO HECK YOU");
        ConversationState state13 = new ConversationState("Hmm");

        option1.setState(state10);
        option2.setState(state11);
        option3.setState(state12);
        option4.setState(state13);

        ConversationTransition[] ops = {option1, option2, option3, option4};
        startState.transitions = ops;
        return startState;
    }

    public void setTransitions(ConversationTransition[] transitions) {
        this.transitions = transitions;
    }

    public ConversationTransition[] getTransitions() {
        return transitions;
    }

    public String getText() {
        return text;
    }
}