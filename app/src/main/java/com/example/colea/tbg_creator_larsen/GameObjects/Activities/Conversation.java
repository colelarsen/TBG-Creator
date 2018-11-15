package com.example.colea.tbg_creator_larsen.GameObjects.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationCharacter;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationState;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.NormalConversationTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.TradingConversationTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.NPC;
import com.example.colea.tbg_creator_larsen.GameObjects.R;

public class Conversation extends AppCompatActivity implements View.OnClickListener {

    private static ConversationState currentState;
    public static ConversationCharacter currentNPC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        EditText mainText = findViewById(R.id.chatLog);
        mainText.setKeyListener(null);

        currentState = currentNPC.getStart();
        updateText(currentNPC.getName() + ": " + currentState.getText());
        updateButtons();
    }

    public void updateText(String s)
    {
        EditText mainText = findViewById(R.id.chatLog);
        mainText.getText().append("\n\n" + s);
    }

    public void updateButtons()
    {
        checkPassive();
        LinearLayout chatOpsLay = findViewById(R.id.chatButtonsLayout);
        chatOpsLay.removeAllViews();

        ConversationTransition[] ops = currentState.getTransitions();
        for(int i = 0; i < 4; i++)
        {
            ConversationTransition option = ops[i];
            if(option != null) {
                if (option.check()) {
                    Button b = new Button(chatOpsLay.getContext());
                    b.setTag(i);
                    b.setText(option.getDisplayString());
                    b.setOnClickListener(this);
                    chatOpsLay.addView(b);
                }
                else
                {
                    Button b = new Button(chatOpsLay.getContext());
                    b.setText("");
                    chatOpsLay.addView(b);
                }
            }
            else
            {
                Button b = new Button(chatOpsLay.getContext());
                b.setText("");
                chatOpsLay.addView(b);
            }
        }
    }

    public void checkPassive()
    {
        if(currentNPC.passiveState(currentState.getId()))
        {
            Enemy en = (Enemy)currentNPC;
            en.isPassive = true;
        }
    }

    @Override
    public void onClick(View v) {
        int choice = (int) v.getTag();
        ConversationTransition[] transitions = currentState.getTransitions();
        ConversationTransition trans = transitions[choice];
        if(trans.check()) {
            if (!(trans instanceof TradingConversationTransition && currentNPC instanceof NPC)) {
                updateText("You: " + trans.getDisplayString());
                currentState = trans.getState();
                if(currentState != null) {
                    updateText(currentNPC.getName() + ": " + currentState.getText());
                    updateButtons();
                }
                else
                {
                    currentState = new ConversationState("");
                    updateText(currentNPC.getName() + ": " + currentState.getText());
                    updateButtons();
                }
            } else if (trans instanceof TradingConversationTransition && currentNPC instanceof NPC) {
                NPC vendor = (NPC) currentNPC;
                if (vendor.canTrade) {
                    Trading.vendor = vendor;
                    startActivity(new Intent(Conversation.this, Trading.class));
                }
            }
        }


    }
}
