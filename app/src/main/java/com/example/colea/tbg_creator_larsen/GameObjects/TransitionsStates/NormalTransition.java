package com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates;

import com.example.colea.tbg_creator_larsen.GameObjects.Activities.TestActivity;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;
import com.example.colea.tbg_creator_larsen.GameObjects.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.State;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.Transition;

public class NormalTransition extends Transition {
    private String displayString;
    private String transitionString;
    private State toTrans;
    private Conditional conditional;
    public int id;

    public NormalTransition(String displayVal, String transVal)
    {
        displayString = displayVal;
        transitionString = transVal;
        toTrans = new State("");
        id = GameController.getId();
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

    public State trans(TestActivity t)
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
