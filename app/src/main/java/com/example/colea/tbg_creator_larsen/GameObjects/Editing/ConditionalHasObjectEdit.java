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
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.ConditionalSwitch;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.HasObject;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.MainAppController;
import com.example.colea.tbg_creator_larsen.GameObjects.Editing.EditMain;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.Effect;
import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Equipment;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Inventory;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Player;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Weapon;
import com.example.colea.tbg_creator_larsen.GameObjects.R;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.CombatTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.ItemTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.NormalTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.State;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.Transition;
import org.w3c.dom.Text;
import java.util.ArrayList;
public class ConditionalHasObjectEdit extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, LifecycleObserver {

    public static HasObject givenConditional = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conditional_has_object_edit);
        if(givenConditional != null)
        {
            EditText uniqueNameEdit = findViewById(R.id.condSwitchUniqueNameEdit);
            TextView nameEdit = findViewById(R.id.objectNameSelect);
            TextView condAndSelect = findViewById(R.id.condSwitchAnd);
            TextView condOrSelect = findViewById(R.id.condSwitchOr);
            CheckBox notEdit = findViewById(R.id.notCheckbox);


            uniqueNameEdit.setText(givenConditional.uniqueUserId);
            nameEdit.setText(givenConditional.object);
            notEdit.setChecked(givenConditional.not);
            if(givenConditional.and != null)
            {
                condAndSelect.setText(givenConditional.and.getUUID() + "@" + givenConditional.and.getId());
            }
            else
            {
                condAndSelect.setText("N/A");
            }

            if(givenConditional.or != null)
            {
                condOrSelect.setText(givenConditional.or.getUUID() + "@" + givenConditional.or.getId());
            }
            else
            {
                condOrSelect.setText("N/A");
            }
            TextView itemTitle = findViewById(R.id.condItemBar);
            itemTitle.setText("Has Object " + givenConditional.id);
        }
    }


    public void onSaveClick(View view)
    {

        EditText uniqueNameEdit = findViewById(R.id.condSwitchUniqueNameEdit);
        TextView nameEdit = findViewById(R.id.objectNameSelect);
        TextView condAndSelect = findViewById(R.id.condSwitchAnd);
        TextView condOrSelect = findViewById(R.id.condSwitchOr);
        CheckBox notEdit = findViewById(R.id.notCheckbox);

        String uniqueName = uniqueNameEdit.getText().toString();
        String name = nameEdit.getText().toString();
        boolean not = notEdit.isChecked();
        String andText = condAndSelect.getText().toString();
        String orText = condOrSelect.getText().toString();

        HasObject conditionalSwitch = givenConditional;
        Conditional and = null;
        Conditional or = null;
        if(andText.compareTo("N/A") != 0)
        {
            int id = Integer.parseInt(andText.split("@")[1]);
            and = (Conditional)EditMain.gameObjects.findObjectById(id);
        }
        if(orText.compareTo("N/A") != 0)
        {
            int id = Integer.parseInt(orText.split("@")[1]);
            or = (Conditional)EditMain.gameObjects.findObjectById(id);
        }
        if(givenConditional == null)
        {
            conditionalSwitch = new HasObject();
        }

        conditionalSwitch.and = and;
        conditionalSwitch.or = or;
        conditionalSwitch.not = not;
        conditionalSwitch.object = name;
        conditionalSwitch.uniqueUserId = uniqueName;
        if(givenConditional == null) {
            conditionalSwitch.id = EditMain.gameObjects.getNewId();
            EditMain.gameObjects.conditionals.add(conditionalSwitch);
        }
        this.onBackPressed();
    }

    TextView lastClicked;

    public void onConditionalClicked(View view)
    {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.spell_popup);
        popup.getMenu().add("N/A");
        for(Conditional conditional : EditMain.gameObjects.conditionals)
        {
            String id = conditional.getUUID() +"@" + conditional.getId();
            popup.getMenu().add(id);
        }
        lastClicked = (TextView)view;
        popup.show();
    }

    public void onItemClicked(View view)
    {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.spell_popup);
        popup.getMenu().add("");
        for(Item item : EditMain.gameObjects.items)
        {
            String id = item.name;
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
        givenConditional = null;
        super.onBackPressed();
    }
}
