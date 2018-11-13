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
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.Effect;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Player;
import com.example.colea.tbg_creator_larsen.GameObjects.R;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.State;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.Transition;
import java.util.ArrayList;

public class StateEditing extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, LifecycleObserver {

    public static State givenState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_editing);
        if(givenState != null)
        {
            EditText uniqueNameField = findViewById(R.id.uniqueNameState);
            uniqueNameField.getText().clear();
            uniqueNameField.getText().append(givenState.uniqueUserId);
            EditText displayField = findViewById(R.id.displayStringState);
            displayField.getText().clear();
            displayField.getText().append(givenState.text);
            CheckBox isStartField = findViewById(R.id.isStartState);
            isStartField.setChecked(givenState.isStartState);
            int[] transOptions = {R.id.trans0, R.id.trans1, R.id.trans2, R.id.trans3, R.id.trans4, R.id.trans5, R.id.trans6, R.id.trans7};
            for(int i = 0; i < 8; i++)
            {
                TextView transId = findViewById(transOptions[i]);
                if(givenState.getTransitions()[i] != null)
                {
                    String id = givenState.getTransitions()[i].getUniqueUserId();
                    if(!MainAppController.stringIsInt(id))
                    {
                        id = id + "@" + givenState.getTransitions()[i].getId();
                    }
                    else
                    {
                        id = "@" + id;
                    }
                    transId.setText(id);
                }
            }

            TextView stateTitle = findViewById(R.id.editStateTitle);
            stateTitle.setText("State " + givenState.getId());
        }
    }

    public void onSaveClick(View view)
    {
        EditText uniqueNameField = findViewById(R.id.uniqueNameState);
        EditText displayField = findViewById(R.id.displayStringState);
        CheckBox isStartField = findViewById(R.id.isStartState);

        String uniqueName = uniqueNameField.getText().toString();
        String desc = displayField.getText().toString();
        boolean isStart = isStartField.isChecked();


        State newState;
        if(givenState == null)
        {
            int id = EditMain.gameObjects.getNewId();
            newState = new State(desc, id);
        }
        else
        {
            newState = givenState;
        }


        if(isStart) {
            for (State state : EditMain.gameObjects.states) {
                state.isStartState = false;
            }
        }

        newState.uniqueUserId = uniqueName;
        newState.setText(desc);
        newState.isStartState = isStart;

        int[] transOptions = {R.id.trans0, R.id.trans1, R.id.trans2, R.id.trans3, R.id.trans4, R.id.trans5, R.id.trans6, R.id.trans7};
        for(int i = 0; i < 8; i++)
        {
            TextView transId = findViewById(transOptions[i]);
            if(transId.getText().toString().compareTo("N/A") != 0)
            {
                int transI = Integer.parseInt(transId.getText().toString().split("@")[1]);
                Transition t = (Transition)EditMain.gameObjects.findObjectById(transI);
                newState.getTransitions()[i] = t;
            }
        }
        if(givenState == null) {
            EditMain.gameObjects.states.add(newState);
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
        ArrayList<Transition> gameTrans = EditMain.gameObjects.transitions;
        popup.getMenu().add("N/A");
        for(Transition t : gameTrans)
        {
            String id = t.getUniqueUserId();
            if(!MainAppController.stringIsInt(id))
            {
                id = id + "@" + t.getId();
            }
            else
            {
                id = "@" + id;
            }
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
