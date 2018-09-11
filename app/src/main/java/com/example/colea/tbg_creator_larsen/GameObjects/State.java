package com.example.colea.tbg_creator_larsen.GameObjects;

public class State {

    private String[] transitionOptions = new String[8];
    private String[] onTransition = new String[8];
    private State[] transitions = new State[8];
    private String text = "";

    public State()
    {
    }

    public static State getStartState()
    {
        State startState = new State();
        startState.text = "Welcome Entrance to of Dungeon";

        State temp1 = new State();
        State temp2 = new State();

        startState.transitionOptions[0] = "Go to the Dungeon";
        startState.onTransition[0] = "You walk down the Stairs";
        startState.transitionOptions[1] = "Go to Town";
        startState.onTransition[1] = "You walk back to Town";

        temp1.text = "Entered the Dungeon";
        temp1.getTransitions()[0] = startState;
        temp1.getTransitionOptions()[0] = "Return to Dungeon Entrance";
        temp1.onTransition[0] = "You walk back up the stairs";
        temp2.text = "Went to Town";
        temp2.getTransitions()[0] = startState;
        temp2.getTransitionOptions()[0] = "Return to Dungeon Entrance";
        temp2.onTransition[0] = "You walk back to the Dungeon Entrance";
        startState.transitions[0] = temp1;
        startState.transitions[1] = temp2;
        return startState;
    }

    public State[] getTransitions()
    {
        return transitions;
    }

    public String getText()
    {
        return text;
    }

    public String[] getTransitionOptions()
    {
        return transitionOptions;
    }

    public String[] getOnTransition()
    {
        return  onTransition;
    }
}
