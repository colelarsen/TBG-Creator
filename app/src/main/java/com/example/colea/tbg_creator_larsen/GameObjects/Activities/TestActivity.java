package com.example.colea.tbg_creator_larsen.GameObjects.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import com.example.colea.tbg_creator_larsen.GameObjects.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Equipment;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Inventory;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Player;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Weapon;
import com.example.colea.tbg_creator_larsen.GameObjects.R;
import com.example.colea.tbg_creator_larsen.GameObjects.State;

public class TestActivity extends AppCompatActivity {

    public State currentState;

    public String EXTRA_MESSAGE = "wooo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setUpStartState();
        testInventory();
    }

    public void testInventory()
    {
        Inventory x = Inventory.getInventory();
        Log.d("Inventory Testing", x.itemString());


        x.add(new Weapon("Sword", "A sword of great Strength", 20 , false, 10));
        x.add(new Weapon("BIGGER SWORD", "A sword of great Strength", 20 , false, 15));
        x.add(new Equipment("Chainmail", "A sword of great Strength", 20 , false, 10));
        x.add(new Equipment("Plate Armor", "A sword of great Strength", 20 , false, 15));

        Log.d("Inventory Testing Add I", x.itemString());

        x.add(new Item("Key", "A key", 0, true));
        //x.getItems().get(0).drop();
        //Log.d("Inventory Testing drop", x.itemString());

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

    public void showCombat(View view)
    {
        startActivity(new Intent(TestActivity.this, Combat.class));
    }

    public void choiceMade(Button presser, View view)
    {
        int choice = Integer.parseInt(presser.getTag().toString());
        GameController.transStates(choice);
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
        if(GameController.currentState.getTransitions()[choice] != null) {

            if (GameController.currentState.getTransitions()[choice].check()) {
                State goingTo = GameController.transStates(choice);
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
                    mainText.getText().append("\n\n>" + GameController.currentState.getText() + "\n");
                    int i = 0;
                    while (i < GameController.currentState.getTransitions().length && GameController.currentState.getTransitions()[i] != null) {
                        if (GameController.currentState.getTransitions()[i].check()) {
                            mainText.getText().append("\n(" + (i + 1) + ") " + GameController.currentState.getTransitions()[i].getDisplayString());
                        }
                        i++;
                    }
                    ScrollView scroller = findViewById(R.id.scrollView2);
                    scroller.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }
        }
    }
}
