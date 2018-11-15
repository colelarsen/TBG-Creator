package com.example.colea.tbg_creator_larsen.GameObjects.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.MainAppController;
import com.example.colea.tbg_creator_larsen.GameObjects.Editing.EditMain;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.Effect;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Inventory;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Player;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Weapon;
import com.example.colea.tbg_creator_larsen.GameObjects.R;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.State;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.mainTabs);

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
                    rootView = inflater.inflate(R.layout.fragment_play_game, container, false);
                    setUpPlayGame(rootView);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_edit_game, container, false);
                    setUpEditGame(rootView);
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_share_game, container, false);
                    setUpShareGame(rootView);
                    break;
            }

            return rootView;
        }

        private View vi;

        private void playGamePressed(View view, String fileName)
        {
            String matchingGameString = MainAppController.getFileContent(view.getContext(), fileName);
            GameObjects gO = new GameObjects();
            State startState = gO.fromString(matchingGameString);
            GameController.startState = startState;
            GameController.currentState = startState;
            for(Effect e : gO.effects)
            {
                GameController.effects.add(e);
            }
            Player.setPlayer(gO.player);
            Player.setHealth(Integer.MAX_VALUE);
        }

        private void editGamePressed(View view, String fileName)
        {
            String matchingGameString = MainAppController.getFileContent(view.getContext(), fileName);
            GameObjects gO = new GameObjects();
            gO.fromString(matchingGameString);
            EditMain.gameObjects = gO;
            gO.gameName = fileName;
        }

        public void testingSavingGamesAndStuff(View view)
        {
            //MainAppController.deleteAllGames(view.getContext());
            //MainAppController.saveGameName(view.getContext(), "Second Adventure");
            /*   CODE TO REWRITE TEST FILE FIRSTADVENTURE
            GameObjects gameObjects = new GameObjects();
            State.getStartState(gameObjects);
            gameObjects.player = Player.getPlayer();
            String gameString = gameObjects.toStringFromJSON();
            MainAppController.saveFile(view.getContext(), gameString, "First Adventure");
            */

            /*
            State s = new State("THIRD ADVENTURE TEST");
            s.isStartState = true;
            GameObjects gameObjects = new GameObjects();
            gameObjects.player = Player.getPlayer();
            gameObjects.states.add(s);
            String gameString = gameObjects.toStringFromJSON();
            MainAppController.saveFile(view.getContext(), gameString, "Third Adventure");
            MainAppController.saveGameName(view.getContext(), "Third Adventure");
            */
        }


        static View playGame;
        public void setUpPlayGame(View view)
        {
            testingSavingGamesAndStuff(view);
            String[] Names = MainAppController.getGameNames(view.getContext());

            playGame = view;
            vi = view;
            LinearLayout gamesRows =  view.findViewById(R.id.play_games_selector_layout);
            gamesRows.removeAllViews();

            for (String s : Names)
            {
                if(s.charAt(s.length()-1) == '\n') {
                    s = s.substring(0, s.length() - 1);
                }
                LinearLayout gameCollumns = new LinearLayout(view.getContext());
                gameCollumns.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

                TextView text = new TextView(getContext());
                text.setText(s);
                gameCollumns.addView(text);
                Button b = new Button(view.getContext());
                b.setWidth(b.getWidth() / 2);
                b.setHeight(b.getHeight() / 2);
                b.setText("Play");
                b.setOnClickListener(this);
                b.setTag("Play@" + s);
                gameCollumns.addView(b);
                gamesRows.addView(gameCollumns);
            }
        }

        static View editPlace;
        public void setUpEditGame(View view)
        {
            editPlace = view;
            testingSavingGamesAndStuff(view);
            String[] Names = MainAppController.getGameNames(view.getContext());

            vi = view;
            LinearLayout gamesRows =  view.findViewById(R.id.edit_games_selector_layout);
            gamesRows.removeAllViews();

            //ADD NEW GAME
            LinearLayout gameCol = new LinearLayout(view.getContext());
            gameCol.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            EditText gameName = new EditText(view.getContext());
            gameName.getText().append("Game Name");
            gameCol.addView(gameName);
            Button button = new Button(view.getContext());
            button.setText("New Game");
            button.setTag("NewGame@");
            button.setOnClickListener(this);
            gameCol.addView(button);

            gamesRows.addView(gameCol);


            //Existing Games
            for (String s : Names)
            {
                if(s.charAt(s.length()-1) == '\n') {
                    s = s.substring(0, s.length() - 1);
                }
                LinearLayout gameCollumns = new LinearLayout(view.getContext());
                gameCollumns.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

                TextView text = new TextView(getContext());
                text.setText(s);
                gameCollumns.addView(text);


                Button b = new Button(view.getContext());
                b.setWidth(b.getWidth() / 2);
                b.setHeight(b.getHeight() / 2);
                b.setText("Edit");
                b.setOnClickListener(this);
                b.setTag("Edit@" + s);
                gameCollumns.addView(b);


                Button z = new Button(view.getContext());
                z.setWidth(z.getWidth() / 2);
                z.setHeight(z.getHeight() / 2);
                z.setText("Delete");
                z.setOnClickListener(this);
                z.setTag("Delete@" + s);
                gameCollumns.addView(z);

                gamesRows.addView(gameCollumns);
            }
        }

        static View shareGame;
        public void setUpShareGame(View view)
        {
            shareGame = view;
        }

        public void onClick(View view)
        {
            String[] tags = view.getTag().toString().split("@");
            if(tags[0].compareTo("Play") == 0)
            {
                playGamePressed(view, tags[1]);
                startActivity(new Intent(view.getContext(), TestActivity.class));
            }
            else if(tags[0].compareTo("Edit") == 0)
            {
                editGamePressed(view, tags[1]);
                startActivity(new Intent(view.getContext(), EditMain.class));
            }
            else if(tags[0].compareTo("Delete") == 0)
            {
                deleteGame(view, tags[1]);
            }
            else if(tags[0].compareTo("NewGame") == 0)
            {
                LinearLayout top = getView().findViewById(R.id.edit_games_selector_layout);
                LinearLayout row = (LinearLayout) top.getChildAt(0);
                EditText e = (EditText)row.getChildAt(0);
                String name = e.getText().toString();

                if(!name.isEmpty() && name.compareTo("GameNames") != 0 && !MainAppController.nameExists(e.getContext(), name))
                {
                    GameObjects gameObjects = new GameObjects();
                    EditMain.gameObjects = gameObjects;
                    gameObjects.setPlayer(new Player(10, 10, "Player"));
                    MainAppController.saveGameName(view.getContext(), name);
                    MainAppController.saveFile(view.getContext(), gameObjects.toStringFromJSON(), name);
                    setUpEditGame(editPlace);
                    setUpPlayGame(playGame);
                    setUpShareGame(shareGame);
                }
            }
        }


        private static String deleteFileName = "";
        public void deleteGame(View view, String fileName)
        {
            deleteFileName = fileName;
            //THIS CODE IS TAKEN FROM
            // https://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            builder.setTitle("Confirm Deletion of " + fileName);
            builder.setMessage("Are you sure?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    MainAppController.deleteGame(getContext(), deleteFileName);
                    deleteFileName = "";
                    setUpEditGame(editPlace);
                    setUpPlayGame(playGame);
                    setUpShareGame(shareGame);
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
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
