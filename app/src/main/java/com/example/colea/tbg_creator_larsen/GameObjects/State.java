package com.example.colea.tbg_creator_larsen.GameObjects;

import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.TestConditional;
import java.util.UUID;

public class State {

    private Transition[] transitions = new Transition[8];
    private String text;
    private UUID id;

    public State(String desc) {
        text = desc;
        id = UUID.randomUUID();
    }

    //Temporary
    public static State getStartState() {
        State startState = new State("Welcome to the Entrance of the Dungeon");
        State temp1 = new State("You are now in the Dungeon");
        State temp2 = new State("You are now in Town");

        temp1.getTransitions()[0] = new Transition("Return to Dungeon Entrance", "You take the stairs upwards");
        temp2.getTransitions()[0] = new Transition("Return to Dungeon Entrance", "You take the stairs down into the Dungeon Entrance");

        startState.transitions[0] = new Transition("Enter the Dungeon", "You take the stairs downwards into the Dungeon");
        startState.transitions[1] = new Transition("Go to Town", "You took the path to the Town");
        startState.transitions[0].setState(temp1);
        startState.transitions[1].setState(temp2);
        temp1.transitions[0].setState(startState);
        temp1.transitions[1] = new Transition("Go all the way to Town", "You walk up the stairs and down the path to town");
        TestConditional test = new TestConditional();
        test.setConditional("true", null, null);
        temp1.transitions[1].setConditional(test);
        temp1.transitions[1].setState(temp2);
        temp2.transitions[0].setState(startState);

        return startState;
    }

    public Transition[] getTransitions() {
        return transitions;
    }

    public String getText() {
        return text;
    }
}