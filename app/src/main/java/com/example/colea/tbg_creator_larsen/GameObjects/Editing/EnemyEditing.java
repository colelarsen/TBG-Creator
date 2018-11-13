package com.example.colea.tbg_creator_larsen.GameObjects.Editing;
import android.arch.lifecycle.LifecycleObserver;
import android.content.ClipData;
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
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationState;
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
public class EnemyEditing extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, LifecycleObserver, View.OnClickListener {

    public static Enemy givenEnemy = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enemy_editing);
        
        if(givenEnemy != null)
        {
            EditText enemyUniqueEdit = findViewById(R.id.enemyUniqueNameEdit);
            EditText enemyNameEdit = findViewById(R.id.enemyNameEdit);
            EditText enemyDescEdit = findViewById(R.id.enemyDescriptionEdit);
            EditText enemyMaxHealthEdit = findViewById(R.id.enemyMaxHealth);
            TextView enemyWeaponEdit = findViewById(R.id.enemyWeaponSelect);
            TextView enemyEquipmentEdit = findViewById(R.id.enemyEquipmentSelect);
            TextView convoStateSelect = findViewById(R.id.convoStateSelect);
            TextView passConvoStateSelect = findViewById(R.id.passiveConvoStateSelect);

            enemyUniqueEdit.setText(givenEnemy.uniqueUserId);
            enemyNameEdit.setText(givenEnemy.name);
            enemyDescEdit.setText(givenEnemy.description);
            enemyMaxHealthEdit.setText(""+givenEnemy.maxHealth);

            if(givenEnemy.equippedWeapon != null)
            {
                enemyWeaponEdit.setText(givenEnemy.equippedWeapon.uniqueUserId + "@" + givenEnemy.equippedWeapon.id);
            }
            if(givenEnemy.equippedArmor != null)
            {
                enemyEquipmentEdit.setText(givenEnemy.equippedArmor.uniqueUserId + "@" + givenEnemy.equippedArmor.id);
            }

            if(givenEnemy.convoStart != null)
            {
                convoStateSelect.setText(givenEnemy.convoStart.uniqueUserId + "@" + givenEnemy.convoStart.getId());
            }
            if(givenEnemy.passState != null)
            {
                passConvoStateSelect.setText(givenEnemy.passState.uniqueUserId +"@" + givenEnemy.passState.getId());
            }



            ArrayList<Item> itemDrops = givenEnemy.drops;
            ArrayList<Double> itemDropChance = givenEnemy.dropChance;
            if(itemDropChance.size() != itemDrops.size())
            {
                itemDrops = new ArrayList<>();
                itemDropChance = new ArrayList<>();
            }
            LinearLayout randomChances = findViewById(R.id.enemyItemsLayout);

            for(int i = 0; i < itemDrops.size(); i++)
            {
                Item t = itemDrops.get(i);
                Double itemChance = itemDropChance.get(i);


                LinearLayout row = new LinearLayout(randomChances.getContext());
                row.setOrientation(LinearLayout.HORIZONTAL);
                TextView text = new TextView(row.getContext());
                text.setText("Item " + (randomChances.getChildCount()) + "          ");
                TextView b = new TextView(row.getContext());

                String transId = t.uniqueUserId + "@" + t.getId();
                b.setText(transId);

                b.setOnClickListener(this);
                EditText chance = new EditText(row.getContext());
                chance.setInputType(InputType.TYPE_CLASS_NUMBER);

                Double putThisInt = itemChance * 100;
                chance.setText("" + putThisInt.intValue());

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
                randomChances.addView(row);
            }
            TextView eneyTitle = findViewById(R.id.enemyTitleBar);
            eneyTitle.setText("Enemy " + givenEnemy.id);
        }
    }

    public void onSaveClick(View view)
    {

        EditText enemyUniqueEdit = findViewById(R.id.enemyUniqueNameEdit);
        EditText enemyNameEdit = findViewById(R.id.enemyNameEdit);
        EditText enemyDescEdit = findViewById(R.id.enemyDescriptionEdit);
        EditText enemyMaxHealthEdit = findViewById(R.id.enemyMaxHealth);
        TextView enemyWeaponEdit = findViewById(R.id.enemyWeaponSelect);
        TextView enemyEquipmentEdit = findViewById(R.id.enemyEquipmentSelect);
        TextView convoStateSelect = findViewById(R.id.convoStateSelect);
        TextView passConvoStateSelect = findViewById(R.id.passiveConvoStateSelect);

        String desc = enemyDescEdit.getText().toString();
        String uuid = enemyUniqueEdit.getText().toString();
        String name = enemyNameEdit.getText().toString();
        int maxHealth = Integer.parseInt(enemyMaxHealthEdit.getText().toString());

        String convoStateString = convoStateSelect.getText().toString();
        ConversationState conversationState = null;
        if(convoStateString.compareTo("N/A") != 0)
        {
            int id = Integer.parseInt(convoStateString.split("@")[1]);
            conversationState = (ConversationState)EditMain.gameObjects.findObjectById(id);
        }

        String passiveConvoStateString = passConvoStateSelect.getText().toString();
        ConversationState passiveConvoState = null;
        if(passiveConvoStateString.compareTo("N/A") != 0)
        {
            int id = Integer.parseInt(convoStateString.split("@")[1]);
            passiveConvoState = (ConversationState)EditMain.gameObjects.findObjectById(id);
        }



        Weapon weapon = null;
        if(enemyWeaponEdit.getText().toString().compareTo("N/A") != 0) {
            String condString = enemyWeaponEdit.getText().toString().split("@")[1];
            //int id = Integer.parseInt(condString.split("@")[1]);
            int id = Integer.parseInt(condString);
            weapon = (Weapon) EditMain.gameObjects.findObjectById(id);
        }

        Equipment equipment = null;
        if(enemyEquipmentEdit.getText().toString().compareTo("N/A") != 0) {
            String condString = enemyEquipmentEdit.getText().toString().split("@")[1];
            //int id = Integer.parseInt(condString.split("@")[1]);
            int id = Integer.parseInt(condString);
            equipment = (Equipment) EditMain.gameObjects.findObjectById(id);
        }



        //Save Items
        LinearLayout itemsList = findViewById(R.id.enemyItemsLayout);
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<Double> itemDropChances = new ArrayList<>();

        for(int i = 1; i < itemsList.getChildCount(); i++)
        {
            LinearLayout row = (LinearLayout)itemsList.getChildAt(i);
            TextView idText = (TextView)row.getChildAt(1);
            String THRYTHIS = idText.getText().toString();
            if(idText.getText().toString().compareTo("N/A") != 0) {
                String itemId = idText.getText().toString().split("@")[1];
                Item item = (Item) EditMain.gameObjects.findObjectById(Integer.parseInt(itemId));
                items.add(item);
            }

            TextView chanceText = (TextView)row.getChildAt(2);
            if(chanceText.getText().toString().compareTo("") != 0) {
                itemDropChances.add((Double.parseDouble(chanceText.getText().toString()))/100);
            }
            else
            {
                itemDropChances.add(0.0);
            }
        }



        Enemy x = givenEnemy;
        if(x == null)
        {
            boolean canConvo = conversationState != null;
            x = new Enemy(maxHealth, maxHealth, name, desc, weapon, equipment, items, itemDropChances, canConvo, conversationState, passiveConvoState);
        }

        //Set Everything
        x.name = name;
        x.maxHealth = maxHealth;
        x.attackScore = 0;
        x.defenceScore = 0;
        x.equippedArmor = equipment;
        x.equippedWeapon = weapon;
        x.passState = passiveConvoState;
        x.canConverse = conversationState != null;
        x.convoStart = conversationState;
        x.description = desc;
        x.uniqueUserId = uuid;
        x.health = maxHealth;
        x.drops = items;
        x.dropChance = itemDropChances;


        if(givenEnemy == null)
        {
            x.id = EditMain.gameObjects.getNewId();
            EditMain.gameObjects.enemies.add(x);
        }
        this.onBackPressed();
    }


    TextView lastClicked;

    //onWeaponClicked
    public void onWeaponClicked(View view)
    {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.spell_popup);
        popup.getMenu().add("N/A");
        for(Item item : EditMain.gameObjects.items)
        {
            if(item instanceof Weapon)
            {
                String id = ((Weapon) item).uniqueUserId + "@" + item.getId();
                popup.getMenu().add(id);
            }
        }
        lastClicked = (TextView)view;
        popup.show();
    }

    //onEquipmentClicked
    public void onEquipmentClicked(View view)
    {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.spell_popup);
        popup.getMenu().add("N/A");
        for(Item item : EditMain.gameObjects.items)
        {
            if(item instanceof Equipment)
            {
                String id = ((Equipment) item).uniqueUserId + "@" + item.getId();
                popup.getMenu().add(id);
            }
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

    public void addNewItem(View view)
    {
        LinearLayout randomChances = (LinearLayout)findViewById(R.id.enemyItemsLayout);

        LinearLayout row = new LinearLayout(randomChances.getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        TextView text = new TextView(row.getContext());
        text.setText("Item " + (randomChances.getChildCount() - 1) + "          ");
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
        randomChances.addView(row);
    }

    public void ConversationStateClicked(View view)
    {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.spell_popup);
        ArrayList<ConversationState> convoStates = EditMain.gameObjects.convoStates;
        popup.getMenu().add("N/A");
        for(ConversationState t : convoStates)
        {
            String id = t.uniqueUserId + "@" + t.getId();
            popup.getMenu().add(id);
        }
        lastClicked = (TextView)view;
        popup.show();
    }

    public void onClick(View view)
    {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.spell_popup);
        ArrayList<Item> gameItems = EditMain.gameObjects.items;
        popup.getMenu().add("N/A");
        for(Item t : gameItems)
        {
            String id = t.getUniqueUserId() + "@" + t.getId();
            popup.getMenu().add(id);
        }
        lastClicked = (TextView)view;
        popup.show();
    }

    @Override
    public void onBackPressed()
    {
        givenEnemy = null;
        super.onBackPressed();
    }
}
