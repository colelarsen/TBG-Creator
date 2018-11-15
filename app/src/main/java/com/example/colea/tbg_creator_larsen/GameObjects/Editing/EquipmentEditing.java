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
public class EquipmentEditing extends AppCompatActivity implements LifecycleObserver {

    public static Equipment givenWeapon = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_editing);
        if(givenWeapon != null)
        {
            EditText uniqueNameEdit = findViewById(R.id.weaponUniqueName);
            EditText itemNameEdit = findViewById(R.id.weaponNameEdit);
            EditText itemDescEdit = findViewById(R.id.weaponDescriptionEdit);
            EditText itemValueEdit = findViewById(R.id.weaponValueEdit);
            EditText damageValueEdit = findViewById(R.id.weaponDamageEdit);
            CheckBox canBeDropped = findViewById(R.id.canBeDroppedCheckbox);

            uniqueNameEdit.setText(givenWeapon.uniqueUserId);
            itemNameEdit.setText(givenWeapon.name);
            itemDescEdit.setText(givenWeapon.description);
            itemValueEdit.setText("" + givenWeapon.value);
            damageValueEdit.setText("" + givenWeapon.defence);


            //Can be dropped

            canBeDropped.setChecked(!givenWeapon.keyItem);

            TextView itemTitle = findViewById(R.id.weaponTitleBar);
            itemTitle.setText("Equipment " + givenWeapon.id);
        }
    }

    public void onSaveClick(View view)
    {

        EditText uniqueNameEdit = findViewById(R.id.weaponUniqueName);
        EditText itemNameEdit = findViewById(R.id.weaponNameEdit);
        EditText itemDescEdit = findViewById(R.id.weaponDescriptionEdit);
        EditText itemValueEdit = findViewById(R.id.weaponValueEdit);
        EditText damageValueEdit = findViewById(R.id.weaponDamageEdit);
        CheckBox canBeDropped = findViewById(R.id.canBeDroppedCheckbox);


        Equipment newItem = givenWeapon;
        if(givenWeapon == null)
        {
            newItem = new Equipment();
        }
        newItem.uniqueUserId = uniqueNameEdit.getText().toString();
        newItem.name = itemNameEdit.getText().toString();
        newItem.description = itemDescEdit.getText().toString();
        newItem.keyItem = !canBeDropped.isChecked();

        if(!itemValueEdit.getText().toString().isEmpty())
        {
            newItem.value = Integer.parseInt(itemValueEdit.getText().toString());
        }

        if(!damageValueEdit.getText().toString().isEmpty())
        {
            newItem.defence = Integer.parseInt(damageValueEdit.getText().toString());
        }

        if(givenWeapon == null) {
            newItem.id = EditMain.gameObjects.getNewId();
            EditMain.gameObjects.items.add(newItem);

        }
        this.onBackPressed();
    }

    @Override
    public void onBackPressed()
    {
        givenWeapon = null;
        super.onBackPressed();
    }
}
