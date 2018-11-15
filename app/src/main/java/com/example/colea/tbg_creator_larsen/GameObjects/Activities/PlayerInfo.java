package com.example.colea.tbg_creator_larsen.GameObjects.Activities;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.Effect;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Equipment;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Inventory;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Player;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Weapon;
import com.example.colea.tbg_creator_larsen.GameObjects.R;

import java.util.ArrayList;

public class PlayerInfo extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;


    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }


    //Where all the work happens
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        //Generates fragments
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            switch(getArguments().getInt(ARG_SECTION_NUMBER))
            {
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_player_info, container, false);
                    setUpPlayerInfo(rootView);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_player_inventory, container, false);
                    setUpPlayerInventory(rootView);
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_player_spells, container, false);
                    setUpPlayerSpells(rootView);
                    break;
            }

            return rootView;
        }


        //Sets up player information / stats
        private static View pinfoView;
        public void setUpPlayerInfo(View view)
        {
            pinfoView = view;
            EditText playerInfo = view.findViewById(R.id.playerInfoText);
            Player p = Player.getPlayer();
            Inventory i = p.inventory;
            playerInfo.getText().clear();
            String textToPut = "Name: " + p.name + "\nHealth: " + p.getHealth() +  "\nDefence: " + p.defence() + "\nGold: " + i.gold + "\nAttack: " + p.attack();
            playerInfo.getText().append(textToPut);
            playerInfo.setKeyListener(null);
        }


        //Sets up player inventory with use/equip buttons
        private static View vi;
        public void setUpPlayerInventory(View view)
        {
            vi = view;
            LinearLayout inventoryRows =  view.findViewById(R.id.inventoryRows);
            inventoryRows.removeAllViews();

            Inventory i = Player.getPlayer().inventory;
            boolean alreadyWeapon = false;
            boolean alreadyEquip = false;
            for (Item it : i.getItems())
            {
                LinearLayout inventoryColumns = new LinearLayout(getContext());
                inventoryColumns.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

                if(it instanceof Weapon)
                {
                    it = (Weapon)it;
                }

                if(it.isEquipment())
                {
                    //it = (Equipment) it;
                }
                TextView text = new TextView(getContext());
                String itemName = "     " + it.getName() + "     ";
                text.setText(itemName);
                text.setKeyListener(null);
                inventoryColumns.addView(text);

                if(!it.isKeyItem()) {
                    Button b = new Button(getContext());

                    b.setWidth(b.getWidth() / 2);
                    b.setHeight(b.getHeight() / 2);
                    b.setText("Drop");
                    b.setOnClickListener(this);
                    b.setTag("" + it.getId() + ",Item");
                    inventoryColumns.addView(b);
                }

                if(it.isWeapon() || it.isEquipment())
                {

                    Button b = new Button(getContext());

                    b.setWidth(b.getWidth() / 2);
                    b.setHeight(b.getHeight() / 2);

                    if(it.isWeapon())
                    {
                        if(it.isEquipped() && !alreadyWeapon)
                        {
                            b.setText("Unequip");
                            alreadyWeapon = true;
                        }
                        else
                        {
                            b.setText("Equip");
                        }
                    }
                    else
                    {
                        if(it.isEquipped() && !alreadyEquip)
                        {
                            b.setText("Unequip");
                            alreadyEquip = true;
                        }
                        else
                        {
                            b.setText("Equip");
                        }
                    }
                    b.setOnClickListener(this);
                    b.setTag("" + it.getId() + ",Equip");
                    inventoryColumns.addView(b);
                }

                if(it.isUseable() && !it.combatOnly())
                {
                    Button b = new Button(getContext());
                    b.setWidth(b.getWidth() / 2);
                    b.setHeight(b.getHeight() / 2);
                    b.setText("Use on Self");
                    b.setOnClickListener(this);
                    b.setTag("" + it.getEffect().getId() + ",Effect," + it.getId());
                    inventoryColumns.addView(b);
                }
                inventoryRows.addView(inventoryColumns);
            }
        }


        //Handles button presses
        @Override
        public void onClick(View v) {

            String itemString = (String) v.getTag();
            String[] itemData = itemString.split(",");
            int itemNum = Integer.parseInt(itemData[0]);

            switch (itemData[1])
            {
                case "Item": {
                    Item item = Player.getPlayer().inventory.findItemById(itemNum);
                    if(item instanceof Weapon)
                    {
                        if(((Weapon) item).isEqu)
                        {
                            ((Weapon) item).equip();
                        }
                    }
                    if(item instanceof Equipment)
                    {
                        if(((Equipment) item).isEqu)
                        {
                            ((Equipment) item).equip();
                        }
                    }
                    item.drop();
                    Log.d("Inventory Testing Drop", "Sword");
                    break;
                }


                case "Equip": {
                    Item item = Player.getPlayer().inventory.findItemById(itemNum);
                    if(item instanceof  Weapon) {
                        Weapon ite = (Weapon) item;
                        ite.equip();
                    }
                    else {
                            Equipment ite = (Equipment) item;
                            ite.equip();
                    }
                    break;
                }

                case "Effect": {
                    int id = Integer.parseInt(itemData[0]);
                    Effect ef = GameController.getEffectById(id);
                    if(ef != null)
                    {
                        Player.getPlayer().applyEffect(ef);
                    }


                    if(itemData.length > 2) {
                        itemNum = Integer.parseInt(itemData[2]);
                        Item item = Player.getPlayer().inventory.findItemById(itemNum);
                        item.drop();
                    }
                    break;
                    }
            }

            /*switch(getArguments().getInt(ARG_SECTION_NUMBER))
            {
                case 2: {
                    setUpPlayerInventory(vi);
                    setUpPlayerInfo(pinfoView);
                    break;
                }
            }
            */
            setUpPlayerInventory(vi);
            setUpPlayerInfo(pinfoView);
        }

        //Sets up player spells
        static View spellView;
        public void setUpPlayerSpells(View view)
        {
            spellView = view;

            LinearLayout spellRows =  view.findViewById(R.id.spellRows);
            spellRows.removeAllViews();

            ArrayList<Effect> spells = Player.getPlayer().spells;
            for (Effect effect : spells)
            {
                LinearLayout spellCollumns = new LinearLayout(getContext());
                spellCollumns.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));


                TextView text = new TextView(getContext());
                String itemName = "     " + effect.getName() + "     ";
                text.setText(itemName);
                text.setKeyListener(null);
                spellCollumns.addView(text);

                TextView desc = new TextView(getContext());
                String descS = "     " + effect.getDescription() + "     ";
                desc.setText(descS);
                desc.setKeyListener(null);
                spellCollumns.addView(desc);

                if(!effect.combatOnly())
                {
                    Button b = new Button(getContext());
                    b.setWidth(b.getWidth() / 2);
                    b.setHeight(b.getHeight() / 2);
                    b.setText("Use on Self");
                    b.setOnClickListener(this);
                    b.setTag("" + effect.getId() + ",Effect");
                    spellCollumns.addView(b);
                }

                spellRows.addView(spellCollumns);
            }
        }












    }














    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
