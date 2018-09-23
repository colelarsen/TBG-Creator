package com.example.colea.tbg_creator_larsen.GameObjects;

import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;

public class Transition {
    private String displayString;
    private String transitionString;
    private State toTrans;
    private Conditional conditional;

    public Transition(String displayVal, String transVal)
    {
        displayString = displayVal;
        transitionString = transVal;
        toTrans = new State("");
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
        return transitionString;
    }

    public State getState()
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
