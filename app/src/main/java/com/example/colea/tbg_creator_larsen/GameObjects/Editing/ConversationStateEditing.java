package com.example.colea.tbg_creator_larsen.GameObjects.Editing;

import android.arch.lifecycle.LifecycleObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.MainAppController;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationState;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.Effect;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Player;
import com.example.colea.tbg_creator_larsen.GameObjects.R;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.State;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.Transition;
import java.util.ArrayList;

public class ConversationStateEditing extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, LifecycleObserver {

    public static ConversationState givenState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_state_editing);
        if(givenState != null)
        {
            EditText uniqueNameEdit = findViewById(R.id.uniqueNameConvoState);
            EditText displayTextEdit = findViewById(R.id.displayStringConvoState);

            uniqueNameEdit.setText(givenState.uniqueUserId);
            displayTextEdit.setText(givenState.text);

            int[] transArray = {R.id.trans0, R.id.trans1, R.id.trans2, R.id.trans3};
            for(int i = 0; i < 4; i++)
            {
                TextView trans0 = findViewById(transArray[i]);
                if(givenState.transitions[i] != null) {
                    trans0.setText(givenState.transitions[i].getUUID() + "@" + givenState.transitions[i].getId());
                }
                else
                {
                    trans0.setText("N/A");
                }
            }

            TextView titleBar = findViewById(R.id.editConvoTitle);
            titleBar.setText("Conversation State " + givenState.getId());
        }
    }

    public void onSaveClick(View view)
    {
        EditText uniqueNameEdit = findViewById(R.id.uniqueNameConvoState);
        EditText displayTextEdit = findViewById(R.id.displayStringConvoState);


        String uniqueName = uniqueNameEdit.getText().toString();
        String desc = displayTextEdit.getText().toString();


        ConversationState newState;
        if(givenState == null)
        {
            newState = new ConversationState(desc);

        }
        else
        {
            newState = givenState;
        }
        newState.text = desc;
        newState.uniqueUserId = uniqueName;


        int[] transOptions = {R.id.trans0, R.id.trans1, R.id.trans2, R.id.trans3};
        for(int i = 0; i < 4; i++)
        {
            TextView transId = findViewById(transOptions[i]);
            if(transId.getText().toString().compareTo("N/A") != 0)
            {
                int transI = Integer.parseInt(transId.getText().toString().split("@")[1]);
                ConversationTransition t = (ConversationTransition)EditMain.gameObjects.findObjectById(transI);
                newState.transitions[i] = t;
            }
        }


        if(givenState == null) {
            newState.id = EditMain.gameObjects.getNewId();
            EditMain.gameObjects.convoStates.add(newState);
        }
        givenState = null;
        this.onBackPressed();
    }

    TextView transClicked;
    public void onTransitionClick(View view)
    {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.spell_popup);
        ArrayList<ConversationTransition> gameTrans = EditMain.gameObjects.convoTransitions;
        popup.getMenu().add("N/A");
        for(ConversationTransition t : gameTrans)
        {
            String id = t.getUUID() + "@" + t.getId();
            popup.getMenu().add(id);
        }
        transClicked = (TextView)view;
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        String transId = item.getTitle().toString();
        transClicked.setText(transId);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        givenState = null;
        super.onBackPressed();
    }
}
