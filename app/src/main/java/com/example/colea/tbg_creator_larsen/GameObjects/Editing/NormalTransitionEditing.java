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
import com.example.colea.tbg_creator_larsen.GameObjects.R;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.NormalTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.State;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.Transition;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NormalTransitionEditing extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, LifecycleObserver, View.OnClickListener {

    public static NormalTransition givenTransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_transition_editing);
        if(givenTransition != null)
        {
            EditText disValEdit = findViewById(R.id.displayStringNormTrans);
            EditText transValEdit = findViewById(R.id.transStringNormTrans);
            EditText uniqueEdit = findViewById(R.id.uniqueNameNormTransition);
            TextView condEdit = findViewById(R.id.conditionalSelectNormTrans);
            TextView stateEdit = findViewById(R.id.stateSelectNormTrans);

            disValEdit.setText(givenTransition.displayString);
            transValEdit.setText(givenTransition.transitionString);
            uniqueEdit.setText(givenTransition.uniqueUserId);
            if(givenTransition.conditional != null) {
                condEdit.setText(givenTransition.conditional.getUUID() + "@"+givenTransition.conditional.getId());
            }
            if(givenTransition.toTrans != null) {
                stateEdit.setText(givenTransition.toTrans.uniqueUserId + "@" + givenTransition.toTrans.getId());
            }

            ArrayList<Transition> chainTrans = givenTransition.chainTransitions;
            LinearLayout chains = findViewById(R.id.NormTransChainLayout);

            for(Transition t : chainTrans)
            {
                LinearLayout row = new LinearLayout(chains.getContext());
                row.setOrientation(LinearLayout.HORIZONTAL);
                TextView text = new TextView(row.getContext());
                text.setText("Chain " + (chains.getChildCount() - 1) + "          ");
                TextView b = new TextView(row.getContext());
                b.setText(""+t.getId());
                b.setOnClickListener(this);
                row.addView(text);
                row.addView(b);
                chains.addView(row, chains.getChildCount()-1);
            }

            TextView transTitle = findViewById(R.id.editNormalTransitionTitle);
            transTitle.setText("Normal Transition " + givenTransition.getId());
        }
    }

    public void onSaveClick(View view)
    {

        //Get all values
        EditText disValEdit = findViewById(R.id.displayStringNormTrans);
        EditText transValEdit = findViewById(R.id.transStringNormTrans);
        EditText uniqueEdit = findViewById(R.id.uniqueNameNormTransition);
        TextView condEdit = findViewById(R.id.conditionalSelectNormTrans);
        TextView stateEdit = findViewById(R.id.stateSelectNormTrans);

        String disVal = disValEdit.getText().toString();
        String transVal = transValEdit.getText().toString();
        String uniqueId = uniqueEdit.getText().toString();

        Conditional conditional = null;
        if(condEdit.getText().toString().compareTo("N/A") != 0) {
            String condString = condEdit.getText().toString();
            int id = Integer.parseInt(condString.split("@")[1]);
            conditional = (Conditional)EditMain.gameObjects.findObjectById(id);
        }
        State state = null;
        if(stateEdit.getText().toString().compareTo("N/A") != 0) {
            String stateString = stateEdit.getText().toString();
            int id = Integer.parseInt(stateString.split("@")[1]);
            state = (State) EditMain.gameObjects.findObjectById(id);
        }

        ArrayList<Transition> chainTrans = new ArrayList<>();
        LinearLayout columns = findViewById(R.id.NormTransChainLayout);
        for(int i = 1; i < columns.getChildCount()-1; i++)
        {
            LinearLayout row = (LinearLayout)columns.getChildAt(i);
            TextView idText = (TextView)row.getChildAt(1);
            if(idText.getText().toString().compareTo("N/A") != 0) {
                String text = idText.getText().toString();
                int id = Integer.parseInt(text.split("@")[1]);
                Transition transition = (Transition) EditMain.gameObjects.findObjectById(id);
                chainTrans.add(transition);
            }
        }


        NormalTransition x = givenTransition;
        if(x == null)
        {
            x = new NormalTransition(disVal, transVal);
        }
        //Set Everything
        x.uniqueUserId = uniqueId;
        x.displayString = disVal;
        x.transitionString = transVal;
        x.setConditional(conditional);
        x.setState(state);
        for(Transition t : chainTrans)
        {
            x.addChain(t);
        }

        if(givenTransition == null)
        {
            EditMain.gameObjects.transitions.add(x);
            x.id = EditMain.gameObjects.getNewId();
        }
        this.onBackPressed();
    }


    TextView lastClicked;
    public void onStateClicked(View view)
    {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.spell_popup);
        ArrayList<State> gameTrans = EditMain.gameObjects.states;

        popup.getMenu().add("N/A");
        for(State t : gameTrans)
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

    public void addNewChain(View view)
    {
        LinearLayout chains = (LinearLayout)findViewById(R.id.NormTransChainLayout);
        LinearLayout row = new LinearLayout(chains.getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        TextView t = new TextView(row.getContext());
        t.setText("Chain " + (chains.getChildCount() - 1) + "          ");
        TextView b = new TextView(row.getContext());
        b.setText("N/A");
        b.setOnClickListener(this);
        row.addView(t);
        row.addView(b);

        t.setTextSize(20);
        b.setTextSize(20);
        chains.addView(row, chains.getChildCount()-1);
    }

    public void onClick(View view)
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
                id += "@" + t.getId();
            }
            else
            {
                id = "@"+id;
            }
            popup.getMenu().add(id);
        }
        lastClicked = (TextView)view;
        popup.show();
    }

    @Override
    public void onBackPressed()
    {
        givenTransition = null;
        super.onBackPressed();
    }
}
