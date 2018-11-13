package com.example.colea.tbg_creator_larsen.GameObjects.Editing;
import android.arch.lifecycle.LifecycleObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.colea.tbg_creator_larsen.GameObjects.Activities.MainActivity;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.MainAppController;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ChangeBaseStateConversationTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationState;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.NormalConversationTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.TradingConversationTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.Editing.EditMain;
import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.NPC;
import com.example.colea.tbg_creator_larsen.GameObjects.R;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.NormalTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.State;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.Transition;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NormalConversationTransitionEditing extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, LifecycleObserver {

    public static NormalConversationTransition givenTransition;
    public static TradingConversationTransition otherGivenTransition;
    public static boolean normal = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_conversation_transition_editing);
        if(givenTransition != null)
        {
            EditText uniqueNameText = findViewById(R.id.uniqueName);
            EditText displayStringText = findViewById(R.id.displayString);
            TextView conditionalEdit = findViewById(R.id.conditionalSelect);
            TextView toStateEdit = findViewById(R.id.stateSelect);
            TextView titleBar = findViewById(R.id.titleBar);

            uniqueNameText.setText(givenTransition.uniqueUserId);
            displayStringText.setText(givenTransition.displayString);
            if(givenTransition.conditional != null)
            {
                conditionalEdit.setText(givenTransition.conditional.getUUID() + "@" + givenTransition.conditional.getId());
            }
            else
            {
                conditionalEdit.setText("N/A");
            }

            if(givenTransition.toTrans != null)
            {
                toStateEdit.setText(givenTransition.toTrans.uniqueUserId + "@" + givenTransition.toTrans.id);
            }
            else
            {
                toStateEdit.setText("N/A");
            }

            titleBar.setText("Normal Convo Transition " + givenTransition.id);
        }
        if(otherGivenTransition != null)
        {
            EditText uniqueNameText = findViewById(R.id.uniqueName);
            EditText displayStringText = findViewById(R.id.displayString);
            TextView conditionalEdit = findViewById(R.id.conditionalSelect);
            TextView toStateEdit = findViewById(R.id.stateSelect);
            TextView titleBar = findViewById(R.id.titleBar);

            uniqueNameText.setText(otherGivenTransition.uniqueUserId);
            displayStringText.setText(otherGivenTransition.displayString);
            if(otherGivenTransition.conditional != null)
            {
                conditionalEdit.setText(otherGivenTransition.conditional.getUUID() + "@" + otherGivenTransition.conditional.getId());
            }
            else
            {
                conditionalEdit.setText("N/A");
            }

            if(otherGivenTransition.toTrans != null)
            {
                toStateEdit.setText(otherGivenTransition.toTrans.uniqueUserId + "@" + otherGivenTransition.toTrans.id);
            }
            else
            {
                toStateEdit.setText("N/A");
            }

            titleBar.setText("Trading Convo Transition " + otherGivenTransition.id);
        }
    }

    public void onSaveClick(View view)
    {

        EditText uniqueNameText = findViewById(R.id.uniqueName);
        EditText displayStringText = findViewById(R.id.displayString);
        TextView condEdit = findViewById(R.id.conditionalSelect);
        TextView stateEdit = findViewById(R.id.stateSelect);
        String disVal = displayStringText.getText().toString();
        String uniqueId = uniqueNameText.getText().toString();
        Conditional conditional = null;
        if(condEdit.getText().toString().compareTo("N/A") != 0) {
            String condString = condEdit.getText().toString();
            int id = Integer.parseInt(condString.split("@")[1]);
            conditional = (Conditional)EditMain.gameObjects.findObjectById(id);
        }
        ConversationState state = null;
        if(stateEdit.getText().toString().compareTo("N/A") != 0) {
            String stateString = stateEdit.getText().toString();
            int id = Integer.parseInt(stateString.split("@")[1]);
            state = (ConversationState) EditMain.gameObjects.findObjectById(id);
        }

        if(normal) {
            NormalConversationTransition x = givenTransition;
            if (x == null) {
                x = new NormalConversationTransition(disVal);
            }
            //Set Everything
            x.uniqueUserId = uniqueId;
            x.displayString = disVal;
            x.toTrans = state;
            x.conditional = conditional;

            if (givenTransition == null) {
                x.id = EditMain.gameObjects.getNewId();
                EditMain.gameObjects.convoTransitions.add(x);
            }
        }
        else
        {
            TradingConversationTransition x = otherGivenTransition;
            if (x == null) {
                x = new TradingConversationTransition(disVal);
            }
            //Set Everything
            x.uniqueUserId = uniqueId;
            x.displayString = disVal;
            x.toTrans = state;
            x.conditional = conditional;

            if (givenTransition == null) {
                x.id = EditMain.gameObjects.getNewId();
                EditMain.gameObjects.convoTransitions.add(x);
            }
        }
        this.onBackPressed();
    }

    TextView lastClicked;
    public void onStateClicked(View view)
    {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.spell_popup);
        ArrayList<ConversationState> gameTrans = EditMain.gameObjects.convoStates;

        popup.getMenu().add("N/A");
        for(ConversationState t : gameTrans)
        {
            String id = "" + t.uniqueUserId + "@" + t.getId();
            popup.getMenu().add(id);
        }
        lastClicked = (TextView)view;
        popup.show();
    }

    public void onConditionalClick(View view)
    {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.spell_popup);
        ArrayList<Conditional> gameTrans = EditMain.gameObjects.conditionals;
        popup.getMenu().add("N/A");
        for(Conditional t : gameTrans)
        {
            String id = t.getUUID()+"@"+t.getId();
            popup.getMenu().add(id);
        }
        lastClicked = (TextView)view;
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        String transId = item.getTitle().toString();
        lastClicked.setText(transId);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        givenTransition = null;
        otherGivenTransition = null;
        normal = false;
        super.onBackPressed();
    }
}
