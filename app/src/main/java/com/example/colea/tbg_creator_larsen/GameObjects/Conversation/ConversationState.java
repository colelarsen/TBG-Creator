package com.example.colea.tbg_creator_larsen.GameObjects.Conversation;

import com.example.colea.tbg_creator_larsen.GameObjects.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.NormalTransition;

public class ConversationState {

    private ConversationTransition[] transitions = new ConversationTransition[4];
    private String text;
    private int id;

    public ConversationState(String desc) {
        text = desc;
        id = GameController.getId();
    }

    public int getId() {
        return id;
    }

    //Temporary
    public static ConversationState getStartState() {
        ConversationState startState = new ConversationState("Hello");
        NormalConversationTransition option1 = new NormalConversationTransition("Hello There");
        NormalConversationTransition option2 = new NormalConversationTransition("Goodbye");
        NormalConversationTransition option3 = new NormalConversationTransition("Heck Yourself");
        NormalConversationTransition option4 = new NormalConversationTransition("Uhhhh...");

        ConversationState state10 = new ConversationState("Welp, see Ya");
        ConversationState state11 = new ConversationState("Goodbye?");
        ConversationState state12 = new ConversationState("NO HECK YOU");
        ConversationState state13 = new ConversationState("Hmm");

        option1.setState(state10);
        option2.setState(state11);
        option3.setState(state12);
        option4.setState(state13);

        ConversationTransition[] ops = {option1, option2, option3, option4};
        startState.transitions = ops;
        return startState;
    }

    public void setTransitions(ConversationTransition[] transitions) {
        this.transitions = transitions;
    }

    public ConversationTransition[] getTransitions() {
        return transitions;
    }

    public String getText() {
        return text;
    }
}