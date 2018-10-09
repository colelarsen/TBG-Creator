package com.example.colea.tbg_creator_larsen.GameObjects.Conversation;

import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;
import com.example.colea.tbg_creator_larsen.GameObjects.GameController;

public class NormalConversationTransition extends ConversationTransition{
    private String displayString;
    private ConversationState toTrans;
    private Conditional conditional;
    public int id;

    public NormalConversationTransition(String displayVal)
    {
        displayString = displayVal;
        toTrans = new ConversationState("");
        id = GameController.getId();
    }

    public void setState(ConversationState trans)
    {
        toTrans = trans;
    }

    public String getDisplayString()
    {
        return displayString;
    }


    public ConversationState getState()
    {
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
