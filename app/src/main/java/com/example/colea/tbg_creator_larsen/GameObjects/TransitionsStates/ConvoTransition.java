package com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates;

import android.content.Intent;
import android.os.Handler;

import com.example.colea.tbg_creator_larsen.GameObjects.Activities.Combat;
import com.example.colea.tbg_creator_larsen.GameObjects.Activities.Conversation;
import com.example.colea.tbg_creator_larsen.GameObjects.Activities.TestActivity;
import com.example.colea.tbg_creator_larsen.GameObjects.Activities.Trading;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;
import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.NPC;

public class ConvoTransition extends Transition {
    private String displayString;
    private String transitionString;
    private State toTrans;
    private Conditional conditional;
    private NPC npc;
    public int id;

    public ConvoTransition(String displayVal, String transVal, NPC npc1)
    {
        displayString = displayVal;
        transitionString = transVal;
        toTrans = new State("");
        id = GameController.getId();
        npc = npc1;
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
        if(npc.canTrade) {
            Conversation.currentNPC = npc;
            Handler h=new Handler();
            final TestActivity test = t;
            h.postDelayed(new Runnable(){
                @Override
                public void run(){
                    test.showConversation();
                }
            }, 3000);

        }
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
