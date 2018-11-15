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

public class PlayerEditing extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, LifecycleObserver, View.OnClickListener {

    public static Player givenPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_editing);
        if(givenPlayer != null)
        {
            EditText playerNameEdit = findViewById(R.id.playerNameEdit);
            EditText playerMaxHealthEdit = findViewById(R.id.playerMaxHealth);
            TextView playerWeaponEdit = findViewById(R.id.playerWeaponSelect);
            TextView playerEquipmentEdit = findViewById(R.id.playerEquipmentSelect);
            EditText playerMoneyEdit = findViewById(R.id.playerMoney);
            EditText playerMaxItemsEdit = findViewById(R.id.playerMaxItems);
            playerNameEdit.setText(givenPlayer.name);
            playerMaxHealthEdit.setText(""+givenPlayer.maxHealth);
            if(givenPlayer.equippedWeapon != null) {
                playerWeaponEdit.setText(givenPlayer.equippedWeapon.uniqueUserId+ "@" + givenPlayer.equippedWeapon.getId());
            }
            else
            {
                playerWeaponEdit.setText("N/A");
            }
            if(givenPlayer.equippedArmor != null) {
                playerEquipmentEdit.setText(givenPlayer.equippedArmor.uniqueUserId+ "@" + givenPlayer.equippedArmor.getId());
            }
            else
            {
                playerEquipmentEdit.setText("N/A");
            }
            playerMoneyEdit.setText(""+givenPlayer.inventory.gold);
            playerMaxItemsEdit.setText(""+givenPlayer.inventory.itemCap);



            ArrayList<Effect> playerSpellList = givenPlayer.spells;
            LinearLayout effects = findViewById(R.id.playerSpellsLayout);
            for(Effect t : playerSpellList)
            {
                LinearLayout row = new LinearLayout(effects.getContext());
                row.setOrientation(LinearLayout.HORIZONTAL);
                TextView text = new TextView(row.getContext());
                text.setText("Spell " + (effects.getChildCount()) + "          ");
                TextView b = new TextView(row.getContext());
                b.setText(t.getUUID() + "@"+t.getId());
                b.setOnClickListener(this);
                b.setTag("effect");
                text.setTextSize(20);
                b.setTextSize(20);
                row.addView(text);
                row.addView(b);
                effects.addView(row);
            }

            ArrayList<Item> items = givenPlayer.inventory.getItems();
            LinearLayout itemsList = findViewById(R.id.playerItemsLayout);
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
                text.setTextSize(20);
                b.setTextSize(20);
                row.addView(text);
                row.addView(b);
                itemsList.addView(row);
            }
        }
    }

    public void onSaveClick(View view)
    {

        EditText playerNameEdit = findViewById(R.id.playerNameEdit);
        EditText playerMaxHealthEdit = findViewById(R.id.playerMaxHealth);
        TextView playerWeaponEdit = findViewById(R.id.playerWeaponSelect);
        TextView playerEquipmentEdit = findViewById(R.id.playerEquipmentSelect);
        EditText playerMoneyEdit = findViewById(R.id.playerMoney);
        EditText playerMaxItemsEdit = findViewById(R.id.playerMaxItems);

        String name = playerNameEdit.getText().toString();
        int maxHealth = Integer.parseInt(playerMaxHealthEdit.getText().toString());

        int playerMoney = 0;
        if(!playerMoneyEdit.getText().toString().isEmpty()) {
            playerMoney = Integer.parseInt(playerMoneyEdit.getText().toString());
        }

        int playerMaxItems = 500;
        if(!playerMaxItemsEdit.getText().toString().isEmpty()) {
            playerMaxItems = Integer.parseInt(playerMaxItemsEdit.getText().toString());
        }



        Weapon weapon = null;
        if(playerWeaponEdit.getText().toString().compareTo("N/A") != 0) {
            String condString = playerWeaponEdit.getText().toString().split("@")[1];
            //int id = Integer.parseInt(condString.split("@")[1]);
            int id = Integer.parseInt(condString);
            weapon = (Weapon) EditMain.gameObjects.findObjectById(id);
        }

        Equipment equipment = null;
        if(playerEquipmentEdit.getText().toString().compareTo("N/A") != 0) {
            String condString = playerEquipmentEdit.getText().toString().split("@")[1];
            //int id = Integer.parseInt(condString.split("@")[1]);
            int id = Integer.parseInt(condString);
            equipment = (Equipment) EditMain.gameObjects.findObjectById(id);
        }

        //Save Transitions
        ArrayList<Effect> effects = new ArrayList<>();
        LinearLayout columns = findViewById(R.id.playerSpellsLayout);
        for(int i = 1; i < columns.getChildCount(); i++)
        {
            LinearLayout row = (LinearLayout)columns.getChildAt(i);
            TextView idText = (TextView)row.getChildAt(1);
            if(idText.getText().toString().compareTo("N/A") != 0) {
                String text = idText.getText().toString();
                int id = Integer.parseInt(text.split("@")[1]);
                Effect effect = (Effect) EditMain.gameObjects.findObjectById(id);
                effects.add(effect);
            }
        }


        //Save Items
        LinearLayout itemsList = findViewById(R.id.playerItemsLayout);
        ArrayList<Item> items = new ArrayList<>();
        for(int i = 1; i < itemsList.getChildCount(); i++)
        {
            LinearLayout row = (LinearLayout)itemsList.getChildAt(i);
            TextView idText = (TextView)row.getChildAt(1);
            if(idText.getText().toString().compareTo("N/A") != 0) {
                String itemId = idText.getText().toString().split("@")[1];
                Item item = (Item) EditMain.gameObjects.findObjectById(Integer.parseInt(itemId));
                items.add(item);
            }
        }


        Player x = givenPlayer;
        if(x == null)
        {
            x = new Player(maxHealth, maxHealth, name);
        }
        //Set Everything
        x.name = name;
        x.maxHealth = maxHealth;
        x.attackScore = 0;
        x.defenceScore = 0;
        x.equippedArmor = null;
        x.equippedWeapon = null;

        if(equipment != null)
        {
            equipment.isEqu = true;
            x.equipArmorPerPlayer(equipment);
        }
        else
        {
            x.equippedArmor = equipment;
        }
        if(weapon != null)
        {
            weapon.isEqu = true;
            x.equipWeaponPerPlayer(weapon);
        }
        else
        {
            x.equippedWeapon = weapon;
        }
        x.inventory = new Inventory(playerMaxItems);
        x.inventory.gold = playerMoney;
        x.spells = effects;
        for(Item item : items)
        {
            x.inventory.add(item);
        }

        if(givenPlayer == null)
        {
            EditMain.gameObjects.player = x;
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

    //AddNewSpell
    public void addNewSpell(View view)
    {
        LinearLayout chains = (LinearLayout)findViewById(R.id.playerSpellsLayout);
        LinearLayout row = new LinearLayout(chains.getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        TextView t = new TextView(row.getContext());
        t.setText("Spell " + (chains.getChildCount()) + "          ");
        TextView b = new TextView(row.getContext());
        b.setText("N/A");
        b.setOnClickListener(this);
        row.addView(t);
        row.addView(b);
        b.setTag("effect");
        t.setTextSize(20);
        b.setTextSize(20);
        chains.addView(row);
    }

    public void addNewItem(View view)
    {
        LinearLayout items = (LinearLayout)findViewById(R.id.playerItemsLayout);
        LinearLayout row = new LinearLayout(items.getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        TextView t = new TextView(row.getContext());
        t.setText("Item " + (items.getChildCount()) + "          ");
        TextView b = new TextView(row.getContext());
        b.setText("N/A");
        b.setOnClickListener(this);
        b.setTag("item");
        row.addView(t);
        row.addView(b);
        t.setTextSize(20);
        b.setTextSize(20);
        items.addView(row);
    }

    public void onClick(View view)
    {
        String tag = (String)view.getTag();

        if(tag.compareTo("effect") == 0) {

            PopupMenu popup = new PopupMenu(this, view);
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.spell_popup);
            ArrayList<Effect> effects = EditMain.gameObjects.effects;
            popup.getMenu().add("N/A");
            for (Effect t : effects) {
                String id = t.getUUID() + "@"+t.getId();
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
                String id = "";
                if(t instanceof Weapon)
                {
                    id = ((Weapon) t).uniqueUserId;
                }
                else if(t instanceof Equipment)
                {
                    id = ((Equipment) t).uniqueUserId;
                }
                else
                {
                    id = t.uniqueUserId;
                }
                id = id + "@" + t.getId();
                popup.getMenu().add(id);
            }
            lastClicked = (TextView) view;
            popup.show();
        }
    }

    @Override
    public void onBackPressed()
    {
        givenPlayer = null;
        super.onBackPressed();
    }
}
