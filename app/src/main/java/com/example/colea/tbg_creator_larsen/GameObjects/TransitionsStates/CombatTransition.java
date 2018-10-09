package com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates;

import android.os.Handler;

import com.example.colea.tbg_creator_larsen.GameObjects.Activities.Combat;
import com.example.colea.tbg_creator_larsen.GameObjects.Activities.TestActivity;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;
import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.GameController;

public class CombatTransition extends Transition {
    private String displayString;
    private String transitionString;
    private State toTrans;
    private Conditional conditional;
    private Enemy[] enemies;
    public int id;

    public CombatTransition(String displayVal, String transVal, Enemy[] enemis)
    {
        displayString = displayVal;
        transitionString = transVal;
        toTrans = new State("");
        id = GameController.getId();
        enemies = enemis;
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
        Combat.enemies = enemies;
        Handler h=new Handler();
        final TestActivity test = t;
        h.postDelayed(new Runnable(){
            @Override
            public void run(){
                test.showCombat(enemies);
            }
        }, 3000);

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
