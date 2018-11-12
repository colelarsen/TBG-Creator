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

public class ItemEditing extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, LifecycleObserver{

    public static Item givenItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_editing);

        if(givenItem != null)
        {
            EditText uniqueNameEdit = findViewById(R.id.itemUniqueName);
            EditText itemNameEdit = findViewById(R.id.itemNameEdit);
            EditText itemDescEdit = findViewById(R.id.itemDescriptionEdit);
            TextView itemEffectSelect = findViewById(R.id.itemEffectSelect);
            EditText itemValueEdit = findViewById(R.id.itemValueEdit);

            uniqueNameEdit.setText(givenItem.uniqueUserId);
            itemNameEdit.setText(givenItem.name);
            itemDescEdit.setText(givenItem.description);
            itemValueEdit.setText("" + givenItem.value);
            if(givenItem.effect != null) {
                itemEffectSelect.setText(givenItem.effect.getUUID() +"@" + givenItem.effect.getId());
            }
            else
            {
                itemEffectSelect.setText("N/A");
            }
            //Can be dropped, useable, inCombat
            CheckBox canBeDropped = findViewById(R.id.canBeDroppedCheckbox);
            CheckBox useable = findViewById(R.id.useableCheckBox);
            CheckBox inCombat = findViewById(R.id.inCombatCheckBox);

            canBeDropped.setChecked(!givenItem.keyItem);
            useable.setChecked(givenItem.useable);
            inCombat.setChecked(givenItem.onlyInCombat);

            TextView itemTitle = findViewById(R.id.itemTitleBar);
            itemTitle.setText("Item " + givenItem.id);
        }
    }

    public void onSaveClick(View view)
    {

        EditText uniqueNameEdit = findViewById(R.id.itemUniqueName);
        EditText itemNameEdit = findViewById(R.id.itemNameEdit);
        EditText itemDescEdit = findViewById(R.id.itemDescriptionEdit);
        TextView itemEffectSelect = findViewById(R.id.itemEffectSelect);
        CheckBox canBeDropped = findViewById(R.id.canBeDroppedCheckbox);
        CheckBox useable = findViewById(R.id.useableCheckBox);
        CheckBox inCombat = findViewById(R.id.inCombatCheckBox);
        EditText itemValueEdit = findViewById(R.id.itemValueEdit);

        Item newItem = givenItem;
        if(givenItem == null)
        {
            newItem = new Item();
        }
        newItem.uniqueUserId = uniqueNameEdit.getText().toString();
        newItem.name = itemNameEdit.getText().toString();
        newItem.description = itemDescEdit.getText().toString();

        String effectString = itemEffectSelect.getText().toString();
        if(effectString.compareTo("N/A") != 0)
        {
            newItem.effect = (Effect)EditMain.gameObjects.findObjectById(Integer.parseInt(effectString.split("@")[1]));
        }
        newItem.keyItem = !canBeDropped.isChecked();
        newItem.useable = useable.isChecked();
        newItem.onlyInCombat = inCombat.isChecked();

        if(!itemValueEdit.getText().toString().isEmpty())
        {
            newItem.value = Integer.parseInt(itemValueEdit.getText().toString());
        }


        if(givenItem == null) {
            EditMain.gameObjects.items.add(newItem);
            newItem.id = EditMain.gameObjects.getNewId();
        }


        this.onBackPressed();
    }


    TextView lastClicked;

    public void onEffectClicked(View view)
    {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.spell_popup);
        popup.getMenu().add("N/A");
        for(Effect effect : EditMain.gameObjects.effects)
        {
            String id = effect.getUUID() +"@" + effect.getId();
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
        givenItem = null;
        super.onBackPressed();
    }
}
