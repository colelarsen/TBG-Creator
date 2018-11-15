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
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.DamagingEffect;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.DefenceEffect;
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
public class DefenceEffectEditing extends AppCompatActivity implements LifecycleObserver {

    static DefenceEffect givenEffect = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defence_effect_editing);
        if(givenEffect != null)
        {
            EditText uniqueNameEdit = findViewById(R.id.damagingEffectUniqueName);
            EditText itemNameEdit = findViewById(R.id.damagingEffectName);
            EditText itemDescEdit = findViewById(R.id.damagingEffectDescEdit);
            EditText defValueEdit = findViewById(R.id.defenceEffectDefEdit);
            EditText durValEdit = findViewById(R.id.deffenceEffectDurationEdit);

            uniqueNameEdit.setText(givenEffect.uniqueUserId);
            itemNameEdit.setText(givenEffect.name);
            itemDescEdit.setText(givenEffect.description);
            defValueEdit.setText("" + givenEffect.defence);
            durValEdit.setText("" + givenEffect.duration);

            TextView itemTitle = findViewById(R.id.damagingEffectTitleBar);
            itemTitle.setText("Defence Effect " + givenEffect.id);
        }
    }

    public void onSaveClick(View view)
    {
        EditText uniqueNameEdit = findViewById(R.id.damagingEffectUniqueName);
        EditText itemNameEdit = findViewById(R.id.damagingEffectName);
        EditText itemDescEdit = findViewById(R.id.damagingEffectDescEdit);
        EditText defValueEdit = findViewById(R.id.defenceEffectDefEdit);
        EditText durValEdit = findViewById(R.id.deffenceEffectDurationEdit);

        String uniqueUserId = uniqueNameEdit.getText().toString();;
        String name = itemNameEdit.getText().toString();;
        String description = itemDescEdit.getText().toString();;
        int defence = 0;
        if(!defValueEdit.getText().toString().isEmpty())
        {
            defence = Integer.parseInt(defValueEdit.getText().toString());
        }
        int duration = 1;
        if(!durValEdit.getText().toString().isEmpty())
        {
            duration = Integer.parseInt(durValEdit.getText().toString());
        }



        DefenceEffect newItem = givenEffect;
        if(givenEffect == null)
        {
            newItem = new DefenceEffect(name, description, defence, duration);
        }

        newItem.defence = defence;
        newItem.duration = duration;
        newItem.uniqueUserId = uniqueUserId;
        newItem.name = name;
        newItem.description = description;

        if(givenEffect == null) {
            newItem.id = EditMain.gameObjects.getNewId();
            EditMain.gameObjects.effects.add(newItem);
        }

        this.onBackPressed();
    }

    @Override
    public void onBackPressed()
    {
        givenEffect = null;
        super.onBackPressed();
    }
}
