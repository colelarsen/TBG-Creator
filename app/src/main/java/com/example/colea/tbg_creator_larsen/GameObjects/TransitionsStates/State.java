package com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates;

import com.example.colea.tbg_creator_larsen.GameObjects.Activities.Conversation;
import com.example.colea.tbg_creator_larsen.GameObjects.Activities.Trading;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.ConditionalSwitch;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.HasObject;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ChangeBaseStateConversationTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationState;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.NormalConversationTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.NPC;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Equipment;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Inventory;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Weapon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class State {

    private Transition[] transitions = new Transition[8];
    private ArrayList<Integer> transitionIds = new ArrayList<>();
    public String text;
    public int id;
    public boolean isStartState = false;
    public String uniqueUserId = "";

    public State(String desc) {
        text = desc;
        id = GameController.getId();
    }
    public void setText(String t)
    {
        text = t;
    }

    public void addTransitionId(int i)
    {
        transitionIds.add(i);
    }

    public State(String desc, int i) {
        text = desc;
        id = i;
    }

    //Temporary
    public static State getStartState(GameObjects gameObjects) {
        State startState = new State("Welcome to the Entrance of the Dungeon");
        startState.isStartState = true;
        State dungeon = makeDungeon(startState, gameObjects);
        State town = makeTown(startState, gameObjects);
        dungeon.transitions[1].setState(town);
        startState.transitions[0] = new NormalTransition("Enter the Dungeon", "You take the stairs downwards into the Dungeon");
        startState.transitions[1] = new NormalTransition("Go to Town", "You took the path to the Town");
        gameObjects.transitions.add(startState.transitions[0]);
        gameObjects.transitions.add(startState.transitions[1]);
        startState.transitions[0].setState(dungeon);
        startState.transitions[1].setState(town);
        gameObjects.states.add(startState);
        gameObjects.states.add(town);
        gameObjects.states.add(dungeon);

        return startState;
    }

    public static State getStartState() {
        State startState = new State("Welcome to the Entrance of the Dungeon");
        State dungeon = makeDungeon(startState);
        State town = makeTown(startState);
        dungeon.transitions[1].setState(town);
        startState.transitions[0] = new NormalTransition("Enter the Dungeon", "You take the stairs downwards into the Dungeon");
        startState.transitions[1] = new NormalTransition("Go to Town", "You took the path to the Town");
        startState.transitions[0].setState(dungeon);
        startState.transitions[1].setState(town);
        return startState;
    }



    public static State makeTown(State startState)
    {
        State town = new State("You are now in Town");
        town.getTransitions()[0] = new NormalTransition("Return to Dungeon Entrance", "You take Path back to the Dungeon Entrance");
        ConvoTransition convoTrans = new ConvoTransition("Talk to the Mayor", "You approach the Mayor", testTrading());

        town.transitions[0].setState(startState);
        town.transitions[1] = convoTrans;
        town.transitions[1].setState(town);
        return town;
    }

    public static State makeTown(State startState, GameObjects gameObjects)
    {
        State town = new State("You are now in Town");
        town.getTransitions()[0] = new NormalTransition("Return to Dungeon Entrance", "You take Path back to the Dungeon Entrance");
        ConvoTransition convoTrans = new ConvoTransition("Talk to the Mayor", "You approach the Mayor", testTrading(gameObjects));
        town.transitions[0].setState(startState);
        town.transitions[1] = convoTrans;
        town.transitions[1].setState(town);
        gameObjects.transitions.add(town.transitions[0]);
        gameObjects.transitions.add(town.transitions[1]);
        return town;
    }

    public static State makeDungeon(State startState)
    {
        State dungeon = new State("You are now in the Dungeon");
        dungeon.getTransitions()[0] = new NormalTransition("Return to Dungeon Entrance", "You take the stairs upwards");
        dungeon.transitions[0].setState(startState);
        dungeon.transitions[1] = new NormalTransition("Go all the way to Town", "You walk up the stairs and down the path to town");
        dungeon.transitions[2] = new CombatTransition("Explore the Dungeon", "You're attacked by a bandit and his rat!", makeEnemiesArray(), true);
        ArrayList<Item> itemForDungeon = new ArrayList<>();
        ArrayList<String> itemStrings = new ArrayList<>();
        itemStrings.add("A sword lies on the ground");


        itemForDungeon.add(new Weapon("Sword", "A sword of great Strength", 20 , false, 10));
        dungeon.transitions[3] = new ItemTransition("Pick up the Sword", "You pick up the Sword", itemForDungeon, itemStrings);
        dungeon.transitions[3].setState(dungeon);
        State dungeonRoom1 = new State("You stand in a dark room with a gate and a red lever on the wall");
        dungeon.transitions[2].setState(dungeonRoom1);
        ConditionalSwitch lever = new ConditionalSwitch(false, "RedLever", null, null);
        dungeonRoom1.transitions[0] = new NormalTransition("Go through the open gate", "The pathway leads around in circles for a while until it ends at a staircase leading downwards.");
        dungeonRoom1.transitions[0].setConditional(lever);
        dungeonRoom1.transitions[1] = new SwitchLeverTransition("Pull the lever", "You pulled the lever", "RedLever");
        dungeonRoom1.transitions[1].setState(dungeonRoom1);
        dungeonRoom1.transitions[0].setState(makeFloor2(dungeonRoom1));
        dungeonRoom1.transitions[2] = new NormalTransition("Go back to the base of the stairs", "You head back without incident");
        dungeonRoom1.transitions[2].setState(dungeon);
        return dungeon;
    }

    public static State makeDungeon(State startState, GameObjects gameObjects)
    {
        State dungeon = new State("You are now in the Dungeon");
        dungeon.getTransitions()[0] = new NormalTransition("Return to Dungeon Entrance", "You take the stairs upwards");
        dungeon.transitions[0].setState(startState);
        dungeon.transitions[1] = new NormalTransition("Go all the way to Town", "You walk up the stairs and down the path to town");
        dungeon.transitions[2] = new CombatTransition("Explore the Dungeon", "You're attacked by a bandit and his rat!", makeEnemiesArray(gameObjects), true);
        gameObjects.transitions.add(dungeon.transitions[0]);
        gameObjects.transitions.add(dungeon.transitions[1]);
        gameObjects.transitions.add(dungeon.transitions[2]);
        ArrayList<Item> itemForDungeon = new ArrayList<>();
        ArrayList<String> itemStrings = new ArrayList<>();
        itemStrings.add("A sword lies on the ground");

        Item sword = new Weapon("Sword", "A sword of great Strength", 20 , false, 10);
        gameObjects.items.add(sword);
        itemForDungeon.add(sword);
        dungeon.transitions[3] = new ItemTransition("Pick up the Sword", "You pick up the Sword", itemForDungeon, itemStrings);
        dungeon.transitions[3].setState(dungeon);
        gameObjects.transitions.add(dungeon.transitions[3]);
        State dungeonRoom1 = new State("You stand in a dark room with a gate and a red lever on the wall");
        gameObjects.states.add(dungeonRoom1);
        dungeon.transitions[2].setState(dungeonRoom1);
        ConditionalSwitch lever = new ConditionalSwitch(false, "RedLever", null, null);
        gameObjects.conditionals.add(lever);
        dungeonRoom1.transitions[0] = new NormalTransition("Go through the open gate", "The pathway leads around in circles for a while until it ends at a staircase leading downwards.");
        dungeonRoom1.transitions[0].setConditional(lever);
        dungeonRoom1.transitions[1] = new SwitchLeverTransition("Pull the lever", "You pulled the lever", "RedLever");
        dungeonRoom1.transitions[1].setState(dungeonRoom1);
        dungeonRoom1.transitions[0].setState(makeFloor2(dungeonRoom1, gameObjects));
        dungeonRoom1.transitions[2] = new NormalTransition("Go back to the base of the stairs", "You head back without incident");
        dungeonRoom1.transitions[2].setState(dungeon);

        gameObjects.transitions.add(dungeonRoom1.transitions[0]);
        gameObjects.transitions.add(dungeonRoom1.transitions[1]);
        gameObjects.transitions.add(dungeonRoom1.transitions[2]);
        return dungeon;
    }

    public static State makeFloor2(State goBack)
    {
        State dungeonFloor2 = new State("You arrive at the bottom of the stairs on floor two. A door is at the back of the room.");

        NormalTransition random1 = new NormalTransition("", "You open the door, nothing is on the other side");
        CombatTransition random2 = new CombatTransition("Explore the Dungeon", "You open the door, enemies await inside!", makeOtherEnemies());
        ArrayList<Transition> randoms = new ArrayList<>();
        randoms.add(random1);
        randoms.add(random2);
        RandomTransitionType3 floor2Door1 = new RandomTransitionType3("Open the door", randoms);
        State dungeonFloor2Room1 = new State("A creepy lair of a dragon, gold litters the floor.");
        floor2Door1.setState(dungeonFloor2Room1);

        dungeonFloor2.transitions[0] = floor2Door1;
        dungeonFloor2.transitions[1] = new NormalTransition("Go back up the stairs", "You head up the stairs");
        dungeonFloor2.transitions[1].setState(goBack);

        dungeonFloor2Room1.transitions[0] = new NormalTransition("Go back up the stairs", "You head up the stairs");
        dungeonFloor2Room1.transitions[0].setState(dungeonFloor2);




        return dungeonFloor2;
    }

    public static State makeFloor2(State goBack, GameObjects gameObjects)
    {
        State dungeonFloor2 = new State("You arrive at the bottom of the stairs on floor two. A door is at the back of the room.");
        gameObjects.states.add(dungeonFloor2);
        NormalTransition random1 = new NormalTransition("", "You open the door, nothing is on the other side");
        CombatTransition random2 = new CombatTransition("Explore the Dungeon", "You open the door, enemies await inside!", makeOtherEnemies(gameObjects));
        gameObjects.transitions.add(random1);
        gameObjects.transitions.add(random2);
        ArrayList<Transition> randoms = new ArrayList<>();
        randoms.add(random1);
        randoms.add(random2);
        RandomTransitionType3 floor2Door1 = new RandomTransitionType3("Open the door", randoms);
        gameObjects.transitions.add(floor2Door1);
        State dungeonFloor2Room1 = new State("A creepy lair of a dragon, gold litters the floor.");
        gameObjects.states.add(dungeonFloor2Room1);
        floor2Door1.setState(dungeonFloor2Room1);

        dungeonFloor2.transitions[0] = floor2Door1;
        dungeonFloor2.transitions[1] = new NormalTransition("Go back up the stairs", "You head up the stairs");
        dungeonFloor2.transitions[1].setState(goBack);
        gameObjects.transitions.add(dungeonFloor2.transitions[1]);

        dungeonFloor2Room1.transitions[0] = new NormalTransition("Go back up the stairs", "You head up the stairs");
        dungeonFloor2Room1.transitions[0].setState(dungeonFloor2);

        gameObjects.transitions.add(dungeonFloor2Room1.transitions[0]);




        return dungeonFloor2;
    }

    public static  Enemy[] makeEnemiesArray()
    {
        Weapon badWe = new Weapon("None", "None", 0, true, 1);
        Equipment badAr = new Equipment("None", "None", 0, true, 0);

        Inventory i = new Inventory(20);
        i.add(new Item("Rat Fur", "Greasy and Gross", 2, false));
        i.add(new Item("Rat Fur", "Greasy and Gross", 2, false));
        i.add(new Item("Rat Fur", "Greasy and Gross", 2, false));

        ArrayList<Double> dropRates = new ArrayList<>();
        dropRates.add(0.5);
        dropRates.add(0.5);
        dropRates.add(0.5);

        Enemy rat = new Enemy(10, 10, "Rat", "Large Rat", badWe, badAr, i.getItems(), dropRates, false, null, null);

        Enemy[] enemies = new Enemy[2];
        enemies[0] = rat;
        enemies[1] = testConvo();
        return enemies;
    }

    public static  Enemy[] makeEnemiesArray(GameObjects gameObjects)
    {
        Weapon badWe = new Weapon("None", "None", 0, true, 1);
        Equipment badAr = new Equipment("None", "None", 0, true, 0);

        gameObjects.items.add(badAr);
        gameObjects.items.add(badWe);

        Inventory i = new Inventory(20);
        Item ratFur = new Item("Rat Fur", "Greasy and Gross", 2, false);
        i.add(ratFur);
        i.add(ratFur);
        i.add(ratFur);
        gameObjects.items.add(ratFur);

        ArrayList<Double> dropRates = new ArrayList<>();
        dropRates.add(0.5);
        dropRates.add(0.5);
        dropRates.add(0.5);

        Enemy rat = new Enemy(10, 10, "Rat", "Large Rat", badWe, badAr, i.getItems(), dropRates, false, null, null);
        gameObjects.enemies.add(rat);
        Enemy[] enemies = new Enemy[2];
        enemies[0] = rat;
        enemies[1] = testConvo(gameObjects);
        return enemies;
    }

    public static Enemy[] makeOtherEnemies()
    {
        Weapon badWe = new Weapon("None", "None", 0, true, 5);
        Equipment badAr = new Equipment("None", "None", 0, true, 2);

        Inventory i = new Inventory(20);
        i.add(new Item("Dragon Claw", "Shiny!", 200, false));

        ArrayList<Double> dropRates = new ArrayList<>();
        dropRates.add(1.0);

        Enemy Dragon = new Enemy(20, 20, "Dragon", "Fearsome Dragon", badWe, badAr, i.getItems(), dropRates, false, null, null);

        Enemy[] enemies = new Enemy[1];
        enemies[0] = Dragon;
        return enemies;
    }

    public static Enemy[] makeOtherEnemies(GameObjects gameObjects)
    {
        Weapon badWe = new Weapon("None", "None", 0, true, 5);
        Equipment badAr = new Equipment("None", "None", 0, true, 2);

        gameObjects.items.add(badAr);
        gameObjects.items.add(badWe);

        Inventory i = new Inventory(20);
        Item dragonClaw = new Item("Dragon Claw", "Shiny!", 200, false);
        i.add(dragonClaw);
        gameObjects.items.add(dragonClaw);

        ArrayList<Double> dropRates = new ArrayList<>();
        dropRates.add(1.0);

        Enemy Dragon = new Enemy(20, 20, "Dragon", "Fearsome Dragon", badWe, badAr, i.getItems(), dropRates, false, null, null);
        gameObjects.enemies.add(Dragon);
        Enemy[] enemies = new Enemy[1];
        enemies[0] = Dragon;
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
        Enemy Bandit = new Enemy(10,10, "Bandit", "Man", badWe, badAr, null, null, true, startState, state10);

        return Bandit;
    }

    public static Enemy testConvo(GameObjects gameObjects)
    {
        ConversationState BLANK = new ConversationState("");
        gameObjects.convoStates.add(BLANK);

        ConversationState startState = new ConversationState("Hello");
        gameObjects.convoStates.add(startState);
        NormalConversationTransition option1 = new NormalConversationTransition("Can you stop attacking me");
        option1.setState(BLANK);
        gameObjects.convoTransitions.add(option1);
        ConversationState state10 = new ConversationState("Sure!");
        gameObjects.convoStates.add(state10);
        option1.setState(state10);

        ConversationTransition[] ops = {option1, null, null, null};
        startState.setTransitions(ops);

        Weapon badWe = new Weapon("None", "None", 0, true, 1);
        Equipment badAr = new Equipment("None", "None", 0, true, 0);
        gameObjects.items.add(badAr);
        gameObjects.items.add(badWe);
        Enemy Bandit = new Enemy(10,10, "Bandit", "Man", badWe, badAr, null, null, true, startState, state10);
        gameObjects.enemies.add(Bandit);
        return Bandit;
    }

    public static NPC testTrading()
    {
        NPC mayor = new NPC("Mayor", null, true, new Inventory(30), 50 );


        ConversationState test = new ConversationState("Hello, can you clear the dungeon and kill the dragon?");
        ChangeBaseStateConversationTransition option1 = new ChangeBaseStateConversationTransition("Sure!");
        HasObject c2 = new HasObject();
        c2.setConditional("Dragon Claw", null, null);
        c2.not();
        ConversationState thanks = new ConversationState("Thanks!");
        option1.setConditional(c2);
        option1.setState(thanks);



        ConversationState acceptedQuest = new ConversationState("Did you have that Dragon Claw?");
        NormalConversationTransition option2 = new NormalConversationTransition("Here's the dragon claw!");
        HasObject c = new HasObject();
        c.setConditional("Dragon Claw", null, null);
        option2.setConditional(c);
        ConversationState EndQuest = new ConversationState("Thanks, you win!");
        option2.setState(EndQuest);


        ConversationTransition[] transitions = {option1, option2, null, null};
        test.setTransitions(transitions);

        NormalConversationTransition option3 = new NormalConversationTransition("I don't have it yet.");
        HasObject c3 = new HasObject();
        c3.setConditional("Dragon Claw", null, null);
        c3.not();
        option3.setConditional(c3);
        ConversationTransition[] ops = {option2, option3, null, null};
        acceptedQuest.setTransitions(ops);
        ConversationState BLANK = new ConversationState("");
        option3.setState(BLANK);

        option1.setConvoCharacter(mayor, acceptedQuest);

        mayor.setStartState(test);
        return mayor;
    }

    public static NPC testTrading(GameObjects gameObjects)
    {
        NPC mayor = new NPC("Mayor", null, true, new Inventory(30), 50 );
        gameObjects.npcs.add(mayor);
        ConversationState test = new ConversationState("Hello, can you clear the dungeon and kill the dragon?");
        gameObjects.convoStates.add(test);
        ChangeBaseStateConversationTransition option1 = new ChangeBaseStateConversationTransition("Sure!");
        gameObjects.convoTransitions.add(option1);
        HasObject c2 = new HasObject();
        gameObjects.conditionals.add(c2);
        c2.setConditional("Dragon Claw", null, null);
        c2.not();
        ConversationState thanks = new ConversationState("Thanks!");
        gameObjects.convoStates.add(thanks);
        option1.setConditional(c2);
        option1.setState(thanks);



        ConversationState acceptedQuest = new ConversationState("Did you have that Dragon Claw?");
        gameObjects.convoStates.add(acceptedQuest);
        NormalConversationTransition option2 = new NormalConversationTransition("Here's the dragon claw!");
        gameObjects.convoTransitions.add(option2);
        HasObject c = new HasObject();
        c.setConditional("Dragon Claw", null, null);
        gameObjects.conditionals.add(c);
        option2.setConditional(c);
        ConversationState EndQuest = new ConversationState("Thanks, you win!");
        gameObjects.convoStates.add(EndQuest);
        option2.setState(EndQuest);


        ConversationTransition[] transitions = {option1, option2, null, null};
        test.setTransitions(transitions);

        NormalConversationTransition option3 = new NormalConversationTransition("I don't have it yet.");
        gameObjects.convoTransitions.add(option3);
        HasObject c3 = new HasObject();
        gameObjects.conditionals.add(c3);
        c3.setConditional("Dragon Claw", null, null);
        c3.not();
        option3.setConditional(c3);
        ConversationTransition[] ops = {option2, option3, null, null};
        acceptedQuest.setTransitions(ops);

        option1.setConvoCharacter(mayor, acceptedQuest);

        mayor.setStartState(test);
        return mayor;
    }

    public Transition[] getTransitions() {
        return transitions;
    }

    public String getText() {
        return text + checkIfItems();
    }

    private String checkIfItems()
    {
        String ret = "";
        for(Transition trans : transitions)
        {
            if(trans instanceof ItemTransition && trans.check())
            {
                ItemTransition itemTrans = (ItemTransition) trans;
                ret += itemTrans.getItemDescriptions();
            }
        }
        return ret;
    }

    public static State fromJSON(JSONObject nextObject)
    {
        try {
            String desc = (String) nextObject.get("text");
            int id = nextObject.getInt("id");
            State state = new State(desc, id);
            JSONArray trans = nextObject.getJSONArray("transitions");
            for(int i = 0; i < trans.length(); i++)
            {
                int tranId = (int)trans.get(i);
                state.addTransitionId(tranId);
            }
            state.isStartState = nextObject.getBoolean("startState");

            if(nextObject.has("uuid")) {
                state.uniqueUserId = nextObject.getString("uuid");
            }
            return state;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void link(GameObjects gameObjects)
    {
        for(int i = 0; i < transitionIds.size(); i++)
        {
            transitions[i] = (Transition) gameObjects.findObjectById(transitionIds.get(i).intValue());
        }
    }

    public JSONObject toJSON()
    {
        try {
            JSONObject stateObject = new JSONObject();
            stateObject.put("id", id);
            stateObject.put("text", text);
            stateObject.put("OBJECT TYPE", "State");
            stateObject.put("startState", isStartState);
            stateObject.put("uuid", uniqueUserId);
            JSONArray ids = new JSONArray();
            for(Transition t : transitions)
            {
                if(t != null) {
                    ids.put(t.getId());
                }
            }
            stateObject.put("transitions", ids);
            return stateObject;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }


        return null;

    }

    public int getId()
    {
        return id;
    }

    public State fromString()
    {
        //State state = new State();
        return null;
    }
}