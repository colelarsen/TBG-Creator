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
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationState;
import com.example.colea.tbg_creator_larsen.GameObjects.Editing.EditMain;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.Effect;
import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.NPC;
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
public class NpcEditing extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, LifecycleObserver, View.OnClickListener {

    public static NPC givenNPC = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_npc_editing);

        if(givenNPC != null)
        {
            EditText playerNameEdit = findViewById(R.id.npcNameEdit);
            EditText playerMoneyEdit = findViewById(R.id.npcMoney);
            EditText playerUName = findViewById(R.id.npcUniqueName);
            TextView convoStateEdit = findViewById(R.id.convoStateSelect);
            CheckBox canTrade = findViewById(R.id.canTradeCheckbox);


            playerUName.setText(givenNPC.uniqueUserId);
            canTrade.setChecked(givenNPC.canTrade);
            if(givenNPC.convoStart != null)
            {
                convoStateEdit.setText("@" + givenNPC.convoStart.getId());
            }

            playerNameEdit.setText(givenNPC.name);
            playerMoneyEdit.setText(""+givenNPC.inventory.gold);


            ArrayList<Item> items = givenNPC.inventory.getItems();
            LinearLayout itemsList = findViewById(R.id.npcItemsLayout);
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

        EditText playerNameEdit = findViewById(R.id.npcNameEdit);
        EditText playerMoneyEdit = findViewById(R.id.npcMoney);
        EditText playerUName = findViewById(R.id.npcUniqueName);
        TextView convoStateEdit = findViewById(R.id.convoStateSelect);
        CheckBox canTradeCheckbox = findViewById(R.id.canTradeCheckbox);


        String name = playerNameEdit.getText().toString();
        String uuid = playerUName.getText().toString();
        boolean canTrade = canTradeCheckbox.isChecked();
        int playerMoney = 0;
        if(!playerMoneyEdit.getText().toString().isEmpty()) {
            playerMoney = Integer.parseInt(playerMoneyEdit.getText().toString());
        }

        String convoStateString = convoStateEdit.getText().toString();
        ConversationState conversationState = null;
        if(convoStateString.compareTo("N/A") != 0)
        {
            int id = Integer.parseInt(convoStateString.split("@")[1]);
            conversationState = (ConversationState)EditMain.gameObjects.findObjectById(id);
        }


        //Save Items
        LinearLayout itemsList = findViewById(R.id.npcItemsLayout);
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

        Inventory inventory = new Inventory(1000);
        inventory.gold = playerMoney;
        for(Item i : items)
        {
            inventory.add(i);
        }


        NPC x = givenNPC;
        if(x == null)
        {
            x = new NPC(name, conversationState, canTrade, inventory, playerMoney);
        }

        //Set Everything
        x.name = name;
        x.uniqueUserId = uuid;
        x.inventory = inventory;
        x.inventory.gold = playerMoney;
        x.convoStart = conversationState;
        x.canTrade = canTrade;

        if(givenNPC == null)
        {
            x.id = EditMain.gameObjects.getNewId();
            EditMain.gameObjects.npcs.add(x);
        }
        this.onBackPressed();
    }

    TextView lastClicked;

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        String transId = item.getTitle().toString();
        lastClicked.setText(transId);
        return true;
    }

    public void addNewItem(View view)
    {
        LinearLayout items = (LinearLayout)findViewById(R.id.npcItemsLayout);
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
            else {
                id = t.uniqueUserId;
            }
            id = id + "@" + t.getId();
            popup.getMenu().add(id);
        }
        lastClicked = (TextView) view;
        popup.show();
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
            String id = "@" + t.getId();
            popup.getMenu().add(id);
        }
        lastClicked = (TextView)view;
        popup.show();
    }

    @Override
    public void onBackPressed()
    {
        givenNPC = null;
        super.onBackPressed();
    }
}
