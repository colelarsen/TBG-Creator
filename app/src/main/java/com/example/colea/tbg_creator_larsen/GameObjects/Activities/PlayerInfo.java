package com.example.colea.tbg_creator_larsen.GameObjects.Activities;

import android.media.audiofx.DynamicsProcessing;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.colea.tbg_creator_larsen.GameObjects.Player.Equipment;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Inventory;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Player;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Weapon;
import com.example.colea.tbg_creator_larsen.GameObjects.R;

public class PlayerInfo extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
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

    /**
     * A placeholder fragment containing a simple view.
     */
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



        public void setUpPlayerInfo(View view)
        {
            EditText playerInfo = view.findViewById(R.id.playerInfoText);
            Player p = Player.getPlayer();
            Inventory i = Inventory.getInventory();
            playerInfo.getText().clear();
            playerInfo.getText().append("Name: " + p.name + "\nHealth: " + p.health +  "\nDefence: " + p.defenceScore + "\nGold: " + i.gold);
            playerInfo.setKeyListener(null);
        }


        private static View vi;
        public void setUpPlayerInventory(View view)
        {
            vi = view;

            LinearLayout inventoryRows =  view.findViewById(R.id.inventoryRows);
            inventoryRows.removeAllViews();

            Inventory i = Inventory.getInventory();
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
                    if(it.isEquipped())
                    {
                        b.setText("Unequip");
                    }
                    else
                    {
                        b.setText("Equip");
                    }

                    b.setOnClickListener(this);
                    b.setTag("" + it.getId() + ",Equip");
                    inventoryColumns.addView(b);
                }
                inventoryRows.addView(inventoryColumns);
            }
        }


        @Override
        public void onClick(View v) {
            switch(getArguments().getInt(ARG_SECTION_NUMBER))
            {
                case 2: {
                    String itemString = (String) v.getTag();
                    String[] itemData = itemString.split(",");

                    int itemNum = Integer.parseInt(itemData[0]);

                    if(itemData[1].compareTo("Item") == 0)
                    {
                        Item item = Inventory.getInventory().findItemById(itemNum);
                        item.drop();
                        Log.d("Inventory Testing Drop", "Sword");
                    }
                    else if(itemData[1].compareTo("Equip") == 0)
                    {
                        Item item = Inventory.getInventory().findItemById(itemNum);
                        if(item instanceof  Weapon) {
                            Weapon ite = (Weapon) item;
                            ite.equip();
                        }
                        else
                        {
                            Equipment ite = (Equipment) item;
                            ite.equip();
                        }
                        Log.d("Inventory Testing Equip", "Sword");
                    }

                    setUpPlayerInventory(vi);
                    break;
                }
            }
        }


        public void setUpPlayerSpells(View view)
        {

        }












    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
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
