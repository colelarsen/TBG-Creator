package com.example.colea.tbg_creator_larsen.GameObjects.Conversation;

import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;

import org.json.JSONObject;

public abstract class ConversationTransition {

    public ConversationTransition()
    {
    }

    public abstract void setState(ConversationState trans);

    public abstract String getDisplayString();

    public abstract void link(GameObjects gameObjects);

    public abstract int getId();

    public abstract JSONObject toJSON();

    public abstract ConversationState getState();

    public abstract void setConditional(Conditional cond);

    public abstract boolean check();
}
