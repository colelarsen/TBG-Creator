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
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.Effect;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.HealingEffect;
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
public class HealingEffectEditing extends AppCompatActivity {

    public static HealingEffect givenEffect = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healing_effect_editing);
        if(givenEffect != null)
        {
            EditText uniqueNameEdit = findViewById(R.id.damagingEffectUniqueName);
            EditText itemNameEdit = findViewById(R.id.damagingEffectName);
            EditText itemDescEdit = findViewById(R.id.damagingEffectDescEdit);
            EditText damageValueEdit = findViewById(R.id.damageEffectDamageEdit);

            uniqueNameEdit.setText(givenEffect.uniqueUserId);
            itemNameEdit.setText(givenEffect.name);
            itemDescEdit.setText(givenEffect.description);
            damageValueEdit.setText("" + givenEffect.healing);

            TextView itemTitle = findViewById(R.id.damagingEffectTitleBar);
            itemTitle.setText("Healing Effect " + givenEffect.id);
        }
    }

    public void onSaveClick(View view)
    {
        EditText uniqueNameEdit = findViewById(R.id.damagingEffectUniqueName);
        EditText itemNameEdit = findViewById(R.id.damagingEffectName);
        EditText itemDescEdit = findViewById(R.id.damagingEffectDescEdit);
        EditText damageValueEdit = findViewById(R.id.damageEffectDamageEdit);

        String uniqueUserId = uniqueNameEdit.getText().toString();;
        String name = itemNameEdit.getText().toString();;
        String description = itemDescEdit.getText().toString();;
        int healing = 0;
        if(!damageValueEdit.getText().toString().isEmpty())
        {
            healing = Integer.parseInt(damageValueEdit.getText().toString());
        }

        HealingEffect newItem = givenEffect;
        if(givenEffect == null)
        {
            newItem = new HealingEffect(name, description, healing);
        }

        newItem.healing = healing;
        newItem.uniqueUserId = uniqueUserId;
        newItem.name = name;
        newItem.description = description;
        if(givenEffect == null) {
            EditMain.gameObjects.effects.add(newItem);
            newItem.id = EditMain.gameObjects.getNewId();
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
