package com.example.colea.tbg_creator_larsen.GameObjects.Activities;

import android.arch.lifecycle.LifecycleObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.colea.tbg_creator_larsen.GameObjects.NPC;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Inventory;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Player;
import com.example.colea.tbg_creator_larsen.GameObjects.R;

import java.util.ArrayList;

public class EnemyDropScreen extends AppCompatActivity implements View.OnClickListener, LifecycleObserver {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enemy_drop_screen);
        setUpPlayerInventory(findViewById(R.id.playerEnemyDropLayout));
        setUpVendorInventory(findViewById(R.id.enemyDropsLayout));
    }

    private static Inventory dropInventory;
    public static void addToDrops(ArrayList<Item> items, ArrayList<Double> dropRate)
    {
        if(dropInventory == null)
        {
            dropInventory = new Inventory(50);
        }

        if(items != null && dropRate != null) {
            for (int i = 0; i < items.size(); i++) {
                double rand = Math.random();
                if(rand <= dropRate.get(i)) {
                    dropInventory.add(items.get(i));
                }
            }
        }

        if(items != null && dropRate == null) {
            for (Item i : items) {
                dropInventory.add(i);
            }
        }
    }

    private static View vi;
    public void setUpPlayerInventory(View view)
    {
        vi = view;
        LinearLayout inventoryRows = view.findViewById(R.id.playerEnemyDropLayout);
        TextView playerText = (TextView)view.getRootView().findViewById(R.id.playerTextEnemyDrop);
        playerText.setText(Player.getPlayer().name + ": " + Player.getPlayer().inventory.gold);


        inventoryRows.removeAllViews();

        Inventory i = Player.getPlayer().inventory;
        for (Item it : i.getItems())
        {
            LinearLayout inventoryColumns = new LinearLayout(view.getContext());
            inventoryColumns.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));


            TextView text = new TextView(view.getContext());
            String itemName = it.getName() + ": " + it.getValue() + " Gold";
            text.setText(itemName);
            text.setKeyListener(null);
            inventoryColumns.addView(text);

            if(!it.isKeyItem() && !it.isEquipped()) {
                Button b = new Button(view.getContext());

                b.setWidth(b.getWidth() / 2);
                b.setHeight(b.getHeight() / 2);
                b.setText("Put");
                b.setOnClickListener(this);
                b.setTag("1," + it.getId());
                inventoryColumns.addView(b);
            }
            inventoryRows.addView(inventoryColumns);
        }
    }

    private static View venView;
    public void setUpVendorInventory(View view)
    {
        venView = view;
        LinearLayout inventoryRows =  view.findViewById(R.id.enemyDropsLayout);
        TextView vendorText = (TextView)view.getRootView().findViewById(R.id.enemyScreen);
        vendorText.setText("Enemy Drops");
        inventoryRows.removeAllViews();

        Inventory i = dropInventory;
        for (Item it : i.getItems())
        {
            LinearLayout inventoryColumns = new LinearLayout(view.getContext());
            inventoryColumns.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            TextView text = new TextView(view.getContext());
            String itemName = it.getName() + ": " + it.getValue() + " Gold";
            text.setText(itemName);
            text.setKeyListener(null);
            inventoryColumns.addView(text);

            if(!it.isKeyItem() && !it.isEquipped()) {
                Button b = new Button(view.getContext());

                b.setWidth(b.getWidth() / 2);
                b.setHeight(b.getHeight() / 2);
                b.setText("Take");
                b.setOnClickListener(this);
                b.setTag("0," + it.getId());
                inventoryColumns.addView(b);
            }
            inventoryRows.addView(inventoryColumns);
        }
    }

    @Override
    public void onClick(View v) {

        String[] tags = ((String)v.getTag()).split(",");

        int itemId = Integer.parseInt(tags[1]);
        Item i = dropInventory.findItemById(itemId);

        //Taking From Drops
        if(tags[0].compareTo("0") == 0)
        {
            Player.getPlayer().inventory.add(i);
            dropInventory.drop(i.getId());
        }
        //Putting in Drops
        else
        {
            dropInventory.add(Player.getPlayer().inventory.findItemById(itemId));
            Player.getPlayer().inventory.drop(itemId);
        }

        setUpVendorInventory(venView);
        setUpPlayerInventory(vi);
    }

    public void okPressed(View v)
    {
        dropInventory = null;
        super.onBackPressed();
    }

    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
