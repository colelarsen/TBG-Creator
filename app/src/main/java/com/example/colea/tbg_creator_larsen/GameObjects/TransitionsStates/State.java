package com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates;

import com.example.colea.tbg_creator_larsen.GameObjects.Activities.Trading;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.TestConditional;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationState;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.NormalConversationTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.TradingConversationTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.NPC;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Equipment;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Inventory;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Weapon;

public class State {

    private Transition[] transitions = new Transition[8];
    private String text;
    private int id;

    public State(String desc) {
        text = desc;
        id = GameController.getId();
    }

    //Temporary
    public static State getStartState() {
        State startState = new State("Welcome to the Entrance of the Dungeon");
        State dungeon = new State("You are now in the Dungeon");
        State town = new State("You are now in Town");

        dungeon.getTransitions()[0] = new NormalTransition("Return to Dungeon Entrance", "You take the stairs upwards");
        town.getTransitions()[0] = new NormalTransition("Return to Dungeon Entrance", "You take Path back to the Dungeon Entrance");

        startState.transitions[0] = new NormalTransition("Enter the Dungeon", "You take the stairs downwards into the Dungeon");
        startState.transitions[1] = new NormalTransition("Go to Town", "You took the path to the Town");
        startState.transitions[0].setState(dungeon);
        startState.transitions[1].setState(town);


        dungeon.transitions[0].setState(startState);
        dungeon.transitions[1] = new NormalTransition("Go all the way to Town", "You walk up the stairs and down the path to town");
        dungeon.transitions[1].setState(town);
        dungeon.transitions[2] = new CombatTransition("Explore the Dungeon", "You venture onwards", makeEnemiesArray());
        dungeon.transitions[2].setState(new State("You fought off the enemies"));

        NPC mayor = new NPC("Mayor",testTrading(), true, new Inventory(30), 50 );
        ConvoTransition convoTrans = new ConvoTransition("Talk to the Mayor", "You approach the Mayor", mayor);

        town.transitions[0].setState(startState);
        town.transitions[1] = convoTrans;
        town.transitions[1].setState(town);

        return startState;
    }

    public static  Enemy[] makeEnemiesArray()
    {
        Weapon badWe = new Weapon("None", "None", 0, true, 1);
        Equipment badAr = new Equipment("None", "None", 0, true, 0);
        Enemy rat = new Enemy(10, "Bagel", "Large Rat", badWe, badAr, null, null, false, null, null);



        Enemy[] enemies = new Enemy[2];
        enemies[0] = rat;
        enemies[1] = testConvo();
        return enemies;
    }

    public static Enemy testConvo()
    {
        ConversationState startState = new ConversationState("Hello");
        NormalConversationTransition option1 = new NormalConversationTransition("Can you stop attacking me");

        ConversationState state10 = new ConversationState("Sure!");
        option1.setState(state10);

        ConversationTransition[] ops = {option1, null, null, null};
        startState.setTransitions(ops);

        Weapon badWe = new Weapon("None", "None", 0, true, 1);
        Equipment badAr = new Equipment("None", "None", 0, true, 0);
        Enemy Bandit = new Enemy(10, "Bandit", "Man", badWe, badAr, null, null, true, startState, state10);

        return Bandit;
    }

    public static ConversationState testTrading()
    {
        ConversationState test = new ConversationState("Hello");

        NormalConversationTransition option1 = new NormalConversationTransition("Hello There");
        NormalConversationTransition option2 = new NormalConversationTransition("Goodbye");
        TradingConversationTransition option3 = new TradingConversationTransition("Want to buy some stuff?");

        ConversationTransition[] transitions = {option1, option2, option3, null};
        test.setTransitions(transitions);
        return test;
    }

    public Transition[] getTransitions() {
        return transitions;
    }

    public String getText() {
        return text;
    }
}