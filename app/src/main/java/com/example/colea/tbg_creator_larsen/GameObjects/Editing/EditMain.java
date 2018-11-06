package com.example.colea.tbg_creator_larsen.GameObjects.Editing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationState;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.Effect;
import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Inventory;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Player;
import com.example.colea.tbg_creator_larsen.GameObjects.R;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.State;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.Transition;

import java.util.ArrayList;

public class EditMain extends AppCompatActivity implements View.OnClickListener {

    public static GameObjects gameObjects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_main);
    }

    public void onAddClick(View view)
    {
        if(view.getId() == findViewById(R.id.addState).getId())
        {
            startActivity(new Intent(view.getContext(), StateEditing.class));
        }
        else if(view.getId() == findViewById(R.id.addTransition).getId())
        {
            //Add a new state to gameObjects somehow
        }
        else if(view.getId() == findViewById(R.id.addItem).getId())
        {
            //Add a new state to gameObjects somehow
        }
        else if(view.getId() == findViewById(R.id.addEffect).getId())
        {
            //Add a new state to gameObjects somehow
        }
        else if(view.getId() == findViewById(R.id.addConditional).getId())
        {
            //Add a new state to gameObjects somehow
        }
        else if(view.getId() == findViewById(R.id.addEnemy).getId())
        {
            //Add a new state to gameObjects somehow
        }
        else if(view.getId() == findViewById(R.id.addConvoTransition).getId())
        {
            //Add a new state to gameObjects somehow
        }
    }

    public void onClick(View view)
    {
        String tag = view.getTag().toString();
        String[] tags = tag.split("@");

        if(tags[0].compareTo("State") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            StateEditing.givenState = (State)gameObjects.findObjectById(id);
            TextView stateTextView = findViewById(R.id.statesDropDown);
            startActivity(new Intent(view.getContext(), StateEditing.class));
            stateTextView.callOnClick();
        }
    }

    public void saveGame(View view)
    {
        gameObjects.saveGame(view);
    }

    public void onDropDownClick(View view)
    {
        ArrayList<String> strings = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        if(view.getId() == findViewById(R.id.statesDropDown).getId())
        {
            for(State s : gameObjects.states)
            {
                if(s.uniqueUserId.isEmpty()) {
                    strings.add("" + s.getId());
                }
                else
                {
                    strings.add(s.uniqueUserId);
                }
                ids.add("" + s.getId());

            }
            addObjects(0, strings, "State", ids);
            //List all states in gameObjects
        }
        else if(view.getId() == findViewById(R.id.transitionsDropDown).getId())
        {
            for(Transition s : gameObjects.transitions)
            {
                strings.add(""+s.getId());
                ids.add(""+s.getId());
            }
            addObjects(1, strings, "Transition", ids);
        }
        else if(view.getId() == findViewById(R.id.itemsDropDown).getId())
        {
            for(Item s : gameObjects.items)
            {
                strings.add(""+s.getId());
                ids.add(""+s.getId());
            }
            addObjects(2, strings, "Item", ids);
        }
        else if(view.getId() == findViewById(R.id.effectsDropDown).getId())
        {
            for(Effect s : gameObjects.effects)
            {
                strings.add(""+s.getId());
                ids.add(""+s.getId());
            }
            addObjects(3, strings, "Effect", ids);
        }
        else if(view.getId() == findViewById(R.id.conditionalsDropDown).getId())
        {
            for(Conditional s : gameObjects.conditionals)
            {
                strings.add(""+s.getId());
                ids.add(""+s.getId());
            }
            addObjects(4, strings, "Conditional", ids);
        }
        else if(view.getId() == findViewById(R.id.enemiesDropDown).getId())
        {
            for(Enemy s : gameObjects.enemies)
            {
                strings.add(""+s.getId());
                ids.add(""+s.getId());
            }
            addObjects(5, strings, "Enemy", ids);
        }
        else if(view.getId() == findViewById(R.id.conversationStatesDropDown).getId())
        {
            for(ConversationState s : gameObjects.convoStates)
            {
                strings.add(""+s.getId());
                ids.add(""+s.getId());
            }
            addObjects(6, strings, "ConversationState", ids);
        }
        else if(view.getId() == findViewById(R.id.conversationTransitionsDropDown).getId())
        {
            for(ConversationTransition s : gameObjects.convoTransitions)
            {
                strings.add(""+s.getId());
                ids.add(""+s.getId());
            }
            addObjects(7, strings, "ConversationTransition", ids);
        }
    }


    private static int lastChild = -5;
    public void addObjects(int index, ArrayList<String> addThis, String type, ArrayList<String> ids)
    {
        LinearLayout mainLinear = findViewById(R.id.editMainLinear);

        if(lastChild == index + 1)
        {
            mainLinear.removeViewAt(lastChild);
            lastChild = -5;
        }
        else if(lastChild != index + 1)
        {
            if(lastChild != -5)
            {
                mainLinear.removeViewAt(lastChild);
            }
            LinearLayout newLayout = new LinearLayout(getBaseContext());
            newLayout.setOrientation(LinearLayout.VERTICAL);
            newLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            lastChild = index + 1;
            mainLinear.addView(newLayout, lastChild);

            int i = 0;
            for (String s : addThis) {
                LinearLayout objects = new LinearLayout(newLayout.getContext());
                objects.setOrientation(LinearLayout.HORIZONTAL);
                objects.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

                TextView text = new TextView(newLayout.getContext());
                text.setText("                     " + s);
                text.setKeyListener(null);
                objects.addView(text);

                Button b = new Button(objects.getContext());

                b.setWidth(b.getWidth() / 2);
                b.setHeight(b.getHeight() / 2);
                b.setText("Edit");
                b.setOnClickListener(this);
                b.setTag(type + "@" + s + "@" + ids.get(i));
                objects.addView(b);

                newLayout.addView(objects);

                i++;
            }
        }

    }
}
