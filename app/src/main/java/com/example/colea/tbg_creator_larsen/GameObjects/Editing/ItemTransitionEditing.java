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
import com.example.colea.tbg_creator_larsen.GameObjects.Editing.EditMain;
import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.R;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.CombatTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.ItemTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.NormalTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.State;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.Transition;

import org.w3c.dom.Text;

import java.util.ArrayList;
public class ItemTransitionEditing extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, LifecycleObserver,  View.OnClickListener {

    public static ItemTransition givenTransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_transition_editing);
        if(givenTransition != null)
        {
            EditText disValEdit = findViewById(R.id.displayStringItemTrans);
            EditText transValEdit = findViewById(R.id.transStringItemTrans);
            EditText uniqueEdit = findViewById(R.id.uniqueNameItemTransition);
            TextView condEdit = findViewById(R.id.conditionalSelectItemTrans);
            TextView stateEdit = findViewById(R.id.stateSelectItemTrans);

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
            LinearLayout chains = findViewById(R.id.ItemTransChainLayout);

            for(Transition t : chainTrans)
            {
                LinearLayout row = new LinearLayout(chains.getContext());
                row.setOrientation(LinearLayout.HORIZONTAL);
                TextView text = new TextView(row.getContext());
                text.setText("Chain " + (chains.getChildCount() - 1) + "          ");
                TextView b = new TextView(row.getContext());
                b.setText(t.getUniqueUserId()+"@"+t.getId());
                b.setOnClickListener(this);
                b.setTag("chain");
                row.addView(text);
                row.addView(b);
                chains.addView(row, chains.getChildCount()-1);
            }

            ArrayList<Item> items = givenTransition.items;
            ArrayList<String> itemDescs = givenTransition.itemDescriptions;

            LinearLayout itemsList = findViewById(R.id.ItemTransItemsLayout);
            for(int i = 0; i < items.size(); i++)
            {
                LinearLayout row = new LinearLayout(itemsList.getContext());
                row.setOrientation(LinearLayout.HORIZONTAL);
                TextView text = new TextView(row.getContext());
                text.setText("Item " + (i + 1) + "          ");
                TextView b = new TextView(row.getContext());
                b.setText(items.get(i).getUniqueUserId() +"@" + items.get(i).getId());
                b.setOnClickListener(this);
                b.setTag("item");
                EditText editText = new EditText(row.getContext());
                editText.setText(itemDescs.get(i));
                text.setTextSize(20);
                b.setTextSize(20);
                editText.setTextSize(20);
                row.addView(text);
                row.addView(b);
                row.addView(editText);
                itemsList.addView(row);
            }

            TextView transTitle = findViewById(R.id.editItemTransitionTitle);
            transTitle.setText("Item Transition " + givenTransition.getId());
        }
    }

    public void onSaveClick(View view)
    {

        //Get all values
        EditText disValEdit = findViewById(R.id.displayStringItemTrans);
        EditText transValEdit = findViewById(R.id.transStringItemTrans);
        EditText uniqueEdit = findViewById(R.id.uniqueNameItemTransition);
        TextView condEdit = findViewById(R.id.conditionalSelectItemTrans);
        TextView stateEdit = findViewById(R.id.stateSelectItemTrans);

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

        //Save Transitions
        ArrayList<Transition> chainTrans = new ArrayList<>();
        LinearLayout columns = findViewById(R.id.ItemTransChainLayout);
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


        //Save Items
        LinearLayout itemsList = findViewById(R.id.ItemTransItemsLayout);
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<String> itemDescs = new ArrayList<>();
        for(int i = 1; i < itemsList.getChildCount(); i++)
        {
            LinearLayout row = (LinearLayout)itemsList.getChildAt(i);
            TextView idText = (TextView)row.getChildAt(1);
            if(idText.getText().toString().compareTo("N/A") != 0) {
                Item item = (Item) EditMain.gameObjects.findObjectById(Integer.parseInt(idText.getText().toString().split("@")[1]));
                items.add(item);
                TextView descText = (TextView)row.getChildAt(2);
                itemDescs.add(descText.getText().toString());
            }
        }

        //CombatTransition(String displayVal, String transVal, Enemy[] enemis, boolean oneTime)
        ItemTransition x = givenTransition;
        if(x == null)
        {
            x = new ItemTransition(disVal, transVal, items, itemDescs);
        }
        //Set Everything
        x.uniqueUserId = uniqueId;
        x.displayString = disVal;
        x.transitionString = transVal;
        x.setConditional(conditional);
        x.setState(state);
        x.itemDescriptions = itemDescs;
        x.items = items;
        for(Transition t : chainTrans)
        {
            x.addChain(t);
        }

        if(givenTransition == null)
        {
            x.id = EditMain.gameObjects.getNewId();
            EditMain.gameObjects.transitions.add(x);

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
        LinearLayout chains = (LinearLayout)findViewById(R.id.CombatTransChainLayout);
        LinearLayout row = new LinearLayout(chains.getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        TextView t = new TextView(row.getContext());
        t.setText("Chain " + (chains.getChildCount() - 1) + "          ");
        TextView b = new TextView(row.getContext());
        b.setText("N/A");
        b.setOnClickListener(this);
        row.addView(t);
        row.addView(b);
        b.setTag("chain");
        t.setTextSize(20);
        b.setTextSize(20);
        chains.addView(row, chains.getChildCount()-1);
    }

    public void addNewItem(View view)
    {
        LinearLayout items = (LinearLayout)findViewById(R.id.ItemTransItemsLayout);
        LinearLayout row = new LinearLayout(items.getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        TextView t = new TextView(row.getContext());
        t.setText("Item " + (items.getChildCount()) + "          ");
        TextView b = new TextView(row.getContext());
        b.setText("N/A");
        b.setOnClickListener(this);
        b.setTag("item");
        EditText editText = new EditText(row.getContext());
        editText.setTextSize(20);
        row.addView(t);
        row.addView(b);
        row.addView(editText);
        t.setTextSize(20);
        b.setTextSize(20);
        items.addView(row);
    }

    public void onClick(View view)
    {
        String tag = (String)view.getTag();

        if(tag.compareTo("chain") == 0) {

            PopupMenu popup = new PopupMenu(this, view);
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.spell_popup);
            ArrayList<Transition> gameTrans = EditMain.gameObjects.transitions;
            popup.getMenu().add("N/A");
            for (Transition t : gameTrans) {
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
            lastClicked = (TextView) view;
            popup.show();
        }
        else
        {
            PopupMenu popup = new PopupMenu(this, view);
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.spell_popup);
            ArrayList<Item> items = EditMain.gameObjects.items;
            popup.getMenu().add("N/A");
            for (Item t : items) {
                String id = t.getUniqueUserId() + "@" + t.getId();
                popup.getMenu().add(id);
            }
            lastClicked = (TextView) view;
            popup.show();
        }
    }

    @Override
    public void onBackPressed()
    {
        givenTransition = null;
        super.onBackPressed();
    }
}
