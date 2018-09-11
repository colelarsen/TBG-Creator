package com.example.colea.tbg_creator_larsen.GameObjects;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

public class TestActivity extends AppCompatActivity {

    public State currentState;

    public String EXTRA_MESSAGE = "wooo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setUpStartState();
    }

    public void setUpStartState()
    {
        GameController.stateChain.add(GameController.currentState);
        EditText mainText = findViewById(R.id.MainText);
        mainText.setKeyListener(null);
        mainText.getText().clear();
        mainText.getText().append("\n>" + GameController.currentState.getText());
        int i = 0;
        while(i < GameController.currentState.getTransitions().length && GameController.currentState.getTransitions()[i] != null)
        {
            if(GameController.currentState.getTransitionOptions()[i] != null) {
                mainText.getText().append("\n>(" + (i+1) + ") " + GameController.currentState.getTransitionOptions()[i]);
            }
            i++;
        }
        ScrollView scroller = findViewById(R.id.scrollView2);
        scroller.fullScroll(ScrollView.FOCUS_DOWN);
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
        State goingFrom = GameController.transStates(choice);
        if(goingFrom != null) {
            EditText mainText = findViewById(R.id.MainText);
            String onTrans = goingFrom.getOnTransition()[choice];
            if(onTrans != null) {
                mainText.getText().append("\n" + onTrans);
            }
            mainText.getText().append("\n>" + GameController.currentState.getText());
            int i = 0;
            while (i < GameController.currentState.getTransitions().length && GameController.currentState.getTransitions()[i] != null) {
                if (GameController.currentState.getTransitionOptions()[i] != null) {
                    String sourceString = "<b>(" + (i+1) + ") </b> ";
                    mainText.getText().append("\n" + Html.fromHtml(sourceString) + GameController.currentState.getTransitionOptions()[i]);
                }
                i++;
            }
            ScrollView scroller = findViewById(R.id.scrollView2);
            scroller.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }
}
