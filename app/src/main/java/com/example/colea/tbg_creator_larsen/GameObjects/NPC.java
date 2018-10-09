package com.example.colea.tbg_creator_larsen.GameObjects;

import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationCharacter;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationState;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Inventory;

public class NPC implements ConversationCharacter {
    public ConversationState convoStart;
    public boolean canTrade;
    public Inventory inventory;
    public String name;
    public int id;
    public int gold = 0;

    public NPC(String nam, ConversationState con, boolean canTr, Inventory i, int g)
    {
        name = nam;
        convoStart = con;
        canTrade = canTr;
        inventory = i;
        id = GameController.getId();
        gold = g;
    }

    @Override
    public ConversationState getStart() {
        return convoStart;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean passiveState(int i) {
        return false;
    }
}
