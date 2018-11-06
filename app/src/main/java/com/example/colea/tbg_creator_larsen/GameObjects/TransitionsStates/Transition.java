package com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates;

import com.example.colea.tbg_creator_larsen.GameObjects.Activities.TestActivity;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Transition {

    public Transition()
    {
    }

    public abstract void setState(State trans);

    public abstract String getDisplayString();

    public abstract String getTransitionString();

    public abstract void setConditional(Conditional cond);

    public abstract void link(GameObjects gameObjects);

    public abstract State trans(TestActivity t);

    public abstract void addChain(Transition t);

    public abstract boolean hasChain();

    public abstract boolean check();

    public abstract boolean shouldStopButtons();

    public abstract int getId();

    public abstract JSONObject toJSON();
}
