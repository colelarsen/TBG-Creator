package com.example.colea.tbg_creator_larsen.GameObjects;

import java.util.ArrayList;

public class GameController {
    public static State currentState = State.getStartState();
    public static ArrayList<State> stateChain = new ArrayList<>();

    public static State transStates(int choice) {
        if (currentState.getTransitions()[choice] != null) {
            State transTo = currentState.getTransitions()[choice].getState();
            State past = currentState;
            currentState = transTo;
            GameController.stateChain.add(GameController.currentState);
            return past;
        }
        return null;
    }
}
