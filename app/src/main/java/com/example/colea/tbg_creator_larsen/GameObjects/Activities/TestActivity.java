package com.example.colea.tbg_creator_larsen.GameObjects.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.DamagingEffect;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.DefenceEffect;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.HealingEffect;
import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Equipment;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Inventory;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Player;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Weapon;
import com.example.colea.tbg_creator_larsen.GameObjects.R;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.State;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.Transition;

public class TestActivity extends AppCompatActivity {

    public State currentState;

    public String EXTRA_MESSAGE = "wooo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setUpStartState();
        //testInventory();
        //testSpells();
    }


    public void testSpells()
    {
        Player p = Player.getPlayer();
        //Testing spells
        DamagingEffect de = new DamagingEffect("Fireball", "A ball of fire", 5);
        HealingEffect he = new HealingEffect("Healing", "A warm fuzzy feeling", 5);
        DefenceEffect defenceEffect = new DefenceEffect("Mage Armor", "Magic Armor", 4, 5);
        p.spells.add(de);
        p.spells.add(he);
        p.spells.add(defenceEffect);

        GameController.effects.add(defenceEffect);
        GameController.effects.add(de);
        GameController.effects.add(he);
    }

    public void testInventory()
    {
        Inventory x = Player.getPlayer().inventory;
        Log.d("Inventory Testing", x.itemString(x));

        x.add(new Weapon("Sword", "A sword of great Strength", 20 , false, 10));
        x.add(new Weapon("BIGGER SWORD", "A sword of great Strength", 20 , false, 15));
        x.add(new Equipment("Chainmail", "A sword of great Strength", 20 , false, 10));
        x.add(new Equipment("Plate Armor", "A sword of great Strength", 20 , false, 15));

        Log.d("Inventory Testing Add I", x.itemString(x));

        x.add(new Item("Key", "A key", 0, true));
        //x.getItems().get(0).drop();
        //Log.d("Inventory Testing drop", x.itemString());

        HealingEffect e = new HealingEffect("HealingSmall", "A warm fuzzy feeling", 1);
        GameController.effects.add(e);
        x.add(new Item("Healing Potion", "A Red Bottle", 10, false, true, false, e));
        Player.getPlayer().inventory = x;
    }

    public void setUpStartState()
    {
        GameController.stateChain.add(GameController.currentState);
        EditText mainText = findViewById(R.id.MainText);
        EditText pastText = findViewById(R.id.PastText);
        pastText.setKeyListener(null);
        mainText.setKeyListener(null);
        mainText.getText().clear();
        mainText.getText().append("\n>" + GameController.currentState.getText());
        int i = 0;

        //Check if transition is not null
        //If not null then display the display text
        while(i < GameController.currentState.getTransitions().length && GameController.currentState.getTransitions()[i] != null)
        {
            if(GameController.currentState.getTransitions()[i] != null)
            {
                if(GameController.currentState.getTransitions()[i].check())
                {
                    mainText.getText().append("\n>(" + (i + 1) + ") " + GameController.currentState.getTransitions()[i].getDisplayString());
                }
            }
            i++;
        }
        ScrollView scroller = findViewById(R.id.scrollView2);
        scroller.fullScroll(ScrollView.FOCUS_DOWN);
    }

    public void showPlayerInfo(View view)
    {
        startActivity(new Intent(TestActivity.this, PlayerInfo.class));
    }

    public void onQuitClick(View view)
    {
        super.onBackPressed();
    }


    public void showCombat(Enemy[] enemies)
    {
        Combat.enemies = enemies;
        startActivity(new Intent(TestActivity.this, Combat.class));
    }

    public void showConversation()
    {
        startActivity(new Intent(TestActivity.this, Conversation.class));
    }

    public void updateLog(String s)
    {
        EditText mainText = findViewById(R.id.MainText);
        mainText.getText().append("\n" + s + "\n");
        ScrollView scroller = findViewById(R.id.scrollView2);
        scroller.fullScroll(ScrollView.FOCUS_DOWN);
    }

    public void disableButtons()
    {
        int[] ids = {R.id.op0, R.id.op1, R.id.op2, R.id.op3, R.id.op4, R.id.op5, R.id.op6, R.id.op7, R.id.player_info_button};
        for(int id : ids)
        {
            Button b = findViewById(id);
            b.setAlpha(.1f);
            b.setClickable(false);
        }
    }

    public void enableButtons()
    {
        int[] ids = {R.id.op0, R.id.op1, R.id.op2, R.id.op3, R.id.op4, R.id.op5, R.id.op6, R.id.op7, R.id.player_info_button};
        for(int id : ids)
        {
            Button b = findViewById(id);
            b.setAlpha(1f);
            b.setClickable(true);
        }
    }

    //Makes it so the user cannot segue back out of playing without saving / something else
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void choiceMade(View view)
    {
        int choice = 0;
        switch (view.getId()) {
            case R.id.op0:
                choice = 0;
                break;
            case R.id.op1:
                choice = 1;
                break;
            case R.id.op2:
                choice = 2;
                break;
            case R.id.op3:
                choice = 3;
                break;
            case R.id.op4:
                choice = 4;
                break;
            case R.id.op5:
                choice = 5;
                break;
            case R.id.op6:
                choice = 6;
                break;
            case R.id.op7:
                choice = 7;
                break;
        }
        Transition[] transitions = GameController.currentState.getTransitions();
        if(transitions[choice] != null) {
            if (transitions[choice].check()) {
                State goingTo = GameController.transStates(this, choice);

                if (goingTo != null) {
                    EditText mainText = findViewById(R.id.MainText);
                    EditText pastText = findViewById(R.id.PastText);
                    pastText.getText().append(mainText.getText());
                    pastText.getText().append("\n" + (choice+1) + ")\n");
                    mainText.getText().clear();

                    String onTrans = goingTo.getTransitions()[choice].getTransitionString();
                    if (onTrans != null) {
                        mainText.getText().append("\n" + onTrans);
                    }

                    if(transitions[choice].shouldStopButtons())
                    {
                        disableButtons();
                        Handler h=new Handler();
                        h.postDelayed(new Runnable(){
                            @Override
                            public void run(){
                                updateLog("\n\n>" + GameController.currentState.getText() + "\n");
                                int i = 0;
                                while (i < GameController.currentState.getTransitions().length && GameController.currentState.getTransitions()[i] != null) {
                                    if (GameController.currentState.getTransitions()[i].check()) {
                                        updateLog("(" + (i + 1) + ") " + GameController.currentState.getTransitions()[i].getDisplayString());
                                    }
                                    i++;
                                }
                                ScrollView scroller = findViewById(R.id.scrollView2);
                                scroller.fullScroll(ScrollView.FOCUS_DOWN);
                                enableButtons();
                            }
                        }, 4000);
                    }
                    else {
                        mainText.getText().append("\n\n>" + GameController.currentState.getText() + "\n");
                        int i = 0;
                        while (i < GameController.currentState.getTransitions().length && GameController.currentState.getTransitions()[i] != null) {
                            if (GameController.currentState.getTransitions()[i].check()) {
                                mainText.getText().append("\n(" + (i + 1) + ") " + GameController.currentState.getTransitions()[i].getDisplayString());
                            }
                            i++;
                        }
                        ScrollView scroller = findViewById(R.id.scrollView2);
                        mainText.requestFocus();
                    }
                }
            }
        }
    }
}
