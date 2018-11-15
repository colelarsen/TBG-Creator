package com.example.colea.tbg_creator_larsen.GameObjects.Conversation;

import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationState;

public interface ConversationCharacter {

    ConversationState getStart();
    String getName();
    boolean passiveState(int i);
    void setStartState(ConversationState state);
    int getId();
}
