package com.example.colea.tbg_creator_larsen.GameObjects.Activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.colea.tbg_creator_larsen.GameObjects.NPC;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Inventory;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Player;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Weapon;
import com.example.colea.tbg_creator_larsen.GameObjects.R;

public class Trading extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trading);



        setUpPlayerInventory(findViewById(R.id.playerTradeInventory));
        setUpVendorInventory(findViewById(R.id.vendorInventory));
    }



    //Sets up the player inventory
    private static View vi;
    public void setUpPlayerInventory(View view)
    {
        vi = view;
        LinearLayout inventoryRows =  view.findViewById(R.id.playerTradeInventory);
        TextView playerText = (TextView)view.getRootView().findViewById(R.id.playerTrade);
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
                b.setText("Sell");
                b.setOnClickListener(this);
                b.setTag("1," + it.getId());
                inventoryColumns.addView(b);
            }
            inventoryRows.addView(inventoryColumns);
        }
    }

    //Set up the NPC inventory
    public static NPC vendor;
    private static View venView;
    public void setUpVendorInventory(View view)
    {
        venView = view;
        LinearLayout inventoryRows =  view.findViewById(R.id.vendorInventory);
        TextView vendorText = (TextView)view.getRootView().findViewById(R.id.vendorSell);
        vendorText.setText(vendor.name + ": " + vendor.inventory.gold);
        inventoryRows.removeAllViews();

        Inventory i = vendor.inventory;
        for (Item it : i.getItems())
        {
            LinearLayout inventoryColumns = new LinearLayout(view.getContext());
            inventoryColumns.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            TextView text = new TextView(view.getContext());
            String itemName = it.getName() + ": " + it.getValue() + " Gold";
            text.setText(itemName);
            text.setKeyListener(null);
            inventoryColumns.addView(text);

            Button b = new Button(view.getContext());
            b.setWidth(b.getWidth() / 2);
            b.setHeight(b.getHeight() / 2);
            b.setText("Buy");
            b.setOnClickListener(this);
            b.setTag("0," + it.getId());
            inventoryColumns.addView(b);
            inventoryRows.addView(inventoryColumns);
        }
    }


    //Handles buying / selling
    @Override
    public void onClick(View v) {
        String[] tags = ((String)v.getTag()).split(",");
        int itemId = Integer.parseInt(tags[1]);


        //Buying From vendor
        if(tags[0].compareTo("0") == 0)
        {
            Item i = vendor.inventory.findItemById(itemId);
            if(Player.getPlayer().inventory.gold >= i.getValue()) {
                Player.getPlayer().inventory.gold -= i.getValue();
                vendor.inventory.gold += i.getValue();
                Player.getPlayer().inventory.add(i);
                vendor.inventory.drop(i.getId());
            }
        }

        //Selling to Vendor
        else
        {
            Item i = Player.getPlayer().inventory.findItemById(itemId);
            if(vendor.inventory.gold >= i.getValue()) {
                vendor.inventory.add(Player.getPlayer().inventory.findItemById(itemId));
                Player.getPlayer().inventory.drop(itemId);
                vendor.inventory.gold -= i.getValue();
                Player.getPlayer().inventory.gold += i.getValue();
            }
        }
        setUpVendorInventory(venView);
        setUpPlayerInventory(vi);
    }
}
