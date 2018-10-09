package com.example.colea.tbg_creator_larsen.GameObjects;

import com.example.colea.tbg_creator_larsen.GameObjects.Activities.TestActivity;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.Effect;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.State;

import java.util.ArrayList;

public class GameController {
    public static State currentState = State.getStartState();
    public static ArrayList<State> stateChain = new ArrayList<>();
    public static ArrayList<Effect> effects = new ArrayList<>();

    private static int id = 0;

    public static int getId()
    {
        return id++;
    }

    public static State transStates(TestActivity t, int choice) {
        if (currentState.getTransitions()[choice] != null) {
            State transTo = currentState.getTransitions()[choice].trans(t);
            State past = currentState;
            currentState = transTo;
            GameController.stateChain.add(GameController.currentState);
            return past;
        }
        return null;
    }

    public static Effect getEffectById(int id)
    {
        for(Effect e: effects)
        {
            if(e.getId() == id)
            {
                return e;
            }
        }
        return null;
    }

    public static Effect getEffectByName(String s)
    {
        for(Effect e: effects)
        {
            if(e.getName().compareTo(s) == 0)
            {
                return e;
            }
        }
        return null;
    }
}
