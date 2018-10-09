package com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates;

import com.example.colea.tbg_creator_larsen.GameObjects.Activities.TestActivity;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;

public abstract class Transition {

    public Transition()
    {
    }

    public abstract void setState(State trans);

    public abstract String getDisplayString();

    public abstract String getTransitionString();

    public abstract void setConditional(Conditional cond);

    public abstract State trans(TestActivity t);

    public abstract boolean check();
}
