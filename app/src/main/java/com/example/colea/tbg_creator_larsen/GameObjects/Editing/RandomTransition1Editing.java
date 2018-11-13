package com.example.colea.tbg_creator_larsen.GameObjects.Editing;

import android.arch.lifecycle.LifecycleObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
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
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.RandomTransitionType1;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.State;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.Transition;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RandomTransition1Editing extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, LifecycleObserver, View.OnClickListener {

    public static RandomTransitionType1 givenTransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_transition1_editing);

        if(givenTransition != null)
        {
            EditText disValEdit = findViewById(R.id.displayStringNormTrans);
            EditText uniqueEdit = findViewById(R.id.uniqueNameNormTransition);
            TextView condEdit = findViewById(R.id.conditionalSelectNormTrans);

            disValEdit.setText(givenTransition.displayString);
            uniqueEdit.setText(givenTransition.uniqueUserId);
            if(givenTransition.conditional != null) {
                condEdit.setText(givenTransition.conditional.getUUID() + "@"+givenTransition.conditional.getId());
            }

            ArrayList<Transition> chainTrans = givenTransition.transitions;
            LinearLayout randomChances = findViewById(R.id.RandomTrabs1TransLayout);

            for(Transition t : chainTrans)
            {
                LinearLayout row = new LinearLayout(randomChances.getContext());
                row.setOrientation(LinearLayout.HORIZONTAL);
                TextView text = new TextView(row.getContext());
                text.setText("Transition " + (randomChances.getChildCount() - 1) + "          ");
                TextView b = new TextView(row.getContext());

                b.setText(t.getUniqueUserId()+"@"+t.getId());

                b.setOnClickListener(this);
                EditText chance = new EditText(row.getContext());
                chance.setInputType(InputType.TYPE_CLASS_NUMBER);
                b.setTextSize(20);
                text.setTextSize(20);
                chance.setTextSize(20);
                TextView percent = new TextView(row.getContext());
                percent.setTextSize(20);
                percent.setText("%");
                row.addView(text);
                row.addView(b);
                row.addView(chance);
                row.addView(percent);
                randomChances.addView(row, randomChances.getChildCount()-1);
            }

            TextView transTitle = findViewById(R.id.editNormalTransitionTitle);
            transTitle.setText("Random Transition Type 1 " + givenTransition.getId());
        }
    }

    public void onSaveClick(View view)
    {

        //Get all values
        EditText disValEdit = findViewById(R.id.displayStringNormTrans);
        EditText uniqueEdit = findViewById(R.id.uniqueNameNormTransition);
        TextView condEdit = findViewById(R.id.conditionalSelectNormTrans);

        String disVal = disValEdit.getText().toString();
        String uniqueId = uniqueEdit.getText().toString();

        Conditional conditional = null;
        if(condEdit.getText().toString().compareTo("N/A") != 0) {
            String condString = condEdit.getText().toString();
            int id = Integer.parseInt(condString.split("@")[1]);
            conditional = (Conditional)EditMain.gameObjects.findObjectById(id);
        }


        ArrayList<Transition> randomTrans = new ArrayList<>();
        ArrayList<Double> transChans = new ArrayList<>();
        LinearLayout columns = findViewById(R.id.RandomTrabs1TransLayout);
        for(int i = 1; i < columns.getChildCount()-1; i++)
        {
            LinearLayout row = (LinearLayout)columns.getChildAt(i);
            TextView idText = (TextView)row.getChildAt(1);
            if(idText.getText().toString().compareTo("N/A") != 0) {
                String text = idText.getText().toString();
                int id = Integer.parseInt(text.split("@")[1]);
                Transition transition = (Transition) EditMain.gameObjects.findObjectById(id);

                EditText chanceText = (EditText)row.getChildAt(2);
                if(!chanceText.getText().toString().isEmpty()) {
                    double chance = Double.parseDouble(chanceText.getText().toString()) / 100;
                    transChans.add(chance);

                }
                randomTrans.add(transition);
            }
        }


        RandomTransitionType1 x = givenTransition;
        if(x == null)
        {
            if(transChans.size() == randomTrans.size() && transChans.size() > 0) {
                x = new RandomTransitionType1(disVal, randomTrans, transChans);
            }
            else
            {
                x = new RandomTransitionType1(disVal, randomTrans);
            }
        }
        else
        {
            int id = givenTransition.id;
            if(transChans.size() == randomTrans.size() && transChans.size() > 0) {
                x = new RandomTransitionType1(disVal, randomTrans, transChans);
            }
            else
            {
                x = new RandomTransitionType1(disVal, randomTrans);
            }
            x.id = id;
        }
        //Set Everything
        x.uniqueUserId = uniqueId;
        x.displayString = disVal;
        x.setConditional(conditional);


        if(givenTransition == null)
        {
            x.id = EditMain.gameObjects.getNewId();
            EditMain.gameObjects.transitions.add(x);

        }
        else
        {
            for(Transition t : EditMain.gameObjects.transitions)
            {
                if(t.getId() == x.id)
                {
                    EditMain.gameObjects.transitions.set(EditMain.gameObjects.transitions.indexOf(t), x);
                }
            }
        }
        this.onBackPressed();
    }


    TextView lastClicked;
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

    public void addNewTransition(View view)
    {
        LinearLayout randomChances = (LinearLayout)findViewById(R.id.RandomTrabs1TransLayout);
        LinearLayout row = new LinearLayout(randomChances.getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        TextView text = new TextView(row.getContext());
        text.setText("Transition " + (randomChances.getChildCount() - 1) + "          ");
        TextView b = new TextView(row.getContext());
        b.setText("N/A");
        b.setOnClickListener(this);
        EditText chance = new EditText(row.getContext());
        chance.setInputType(InputType.TYPE_CLASS_NUMBER);
        b.setTextSize(20);
        text.setTextSize(20);
        chance.setTextSize(20);
        TextView percent = new TextView(row.getContext());
        percent.setTextSize(20);
        percent.setText("%");
        row.addView(text);
        row.addView(b);
        row.addView(chance);
        row.addView(percent);
        randomChances.addView(row, randomChances.getChildCount()-1);
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
