package com.example.colea.tbg_creator_larsen.GameObjects;

import java.util.ArrayList;

public class GameController {
    public static State currentState = State.getStartState();
    public static ArrayList<State> stateChain = new ArrayList<State>();

    public static State transStates(int choice) {
        if (currentState.getTransitions()[choice] != null) {
            State transTo = currentState.getTransitions()[choice];
            State past = currentState;
            currentState = transTo;
            GameController.stateChain.add(GameController.currentState);
            return past;
        }
        return null;
    }
}
