package com.example.colea.tbg_creator_larsen.GameObjects.Editing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.ConditionalSwitch;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.HasObject;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.HasSpell;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ChangeBaseStateConversationTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationState;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.ConversationTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.NormalConversationTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.Conversation.TradingConversationTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.DamagingEffect;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.DefenceEffect;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.Effect;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.HealingEffect;
import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.NPC;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Equipment;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Weapon;
import com.example.colea.tbg_creator_larsen.GameObjects.R;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.CombatTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.ConvoTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.ItemTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.NormalTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.OneTimeTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.RandomTransitionType1;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.RandomTransitionType2;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.RandomTransitionType3;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.State;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.SwitchLeverTransition;
import com.example.colea.tbg_creator_larsen.GameObjects.TransitionsStates.Transition;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EditMain extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    //@TODO FLAG INVALID OBJECTS AND LET THEM KNOW IF ANY INVALIDS ON SAVING

    public static GameObjects gameObjects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_main);
    }



    //Handles when a '+' button is clicked. And opens the corresponding Edit activity
    //If there are multiple object types it opens a drop down menu
    public void onAddClick(View view)
    {
        if(view.getId() == findViewById(R.id.addState).getId())
        {
            startActivity(new Intent(view.getContext(), StateEditing.class));
        }
        else if(view.getId() == findViewById(R.id.addTransition).getId())
        {
            PopupMenu popup = new PopupMenu(this, view);
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.spell_popup);
            String[] transitionTypes = {"Normal Transition", "Combat Transition", "Conversation Transition", "PickUpItem Transition", "OneTime Transition", "Random1 Transition", "Random2 Transition", "Random3 Transition", "Switch Lever Transition"};
            for (String s : transitionTypes) {
                popup.getMenu().add(s);
            }
            popup.show();
        }
        else if(view.getId() == findViewById(R.id.addItem).getId())
        {
            PopupMenu popup = new PopupMenu(this, view);
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.spell_popup);
            String[] transitionTypes = {"Item", "Weapon", "Equipment/Armor"};
            for (String s : transitionTypes) {
                popup.getMenu().add(s);
            }
            popup.show();
        }
        else if(view.getId() == findViewById(R.id.addEffect).getId())
        {
            PopupMenu popup = new PopupMenu(this, view);
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.spell_popup);
            String[] transitionTypes = {"Damaging Effect", "Defence Effect", "Healing Effect"};
            for (String s : transitionTypes) {
                popup.getMenu().add(s);
            }
            popup.show();
        }
        else if(view.getId() == findViewById(R.id.addConditional).getId())
        {
            PopupMenu popup = new PopupMenu(this, view);
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.spell_popup);
            String[] transitionTypes = {"Conditional Switch", "Has Object", "Has Spell"};
            for (String s : transitionTypes) {
                popup.getMenu().add(s);
            }
            popup.show();
        }
        else if(view.getId() == findViewById(R.id.addEnemy).getId())
        {
            startActivity(new Intent(view.getContext(), EnemyEditing.class));
        }
        else if(view.getId() == findViewById(R.id.addNPC).getId())
        {
            startActivity(new Intent(view.getContext(), NpcEditing.class));
        }
        else if(view.getId() == findViewById(R.id.addConvoState).getId())
        {
            startActivity(new Intent(view.getContext(), ConversationStateEditing.class));
        }
        else if(view.getId() == findViewById(R.id.addConvoTransition).getId())
        {
            PopupMenu popup = new PopupMenu(this, view);
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.spell_popup);
            String[] transitionTypes = {"Change State Convo Transition", "Normal Convo Transition", "Trading Convo Transition"};
            for (String s : transitionTypes) {
                popup.getMenu().add(s);
            }
            popup.show();
        }
    }

    //Opens the player edit activity
    public void onPlayerEdit(View view)
    {
        PlayerEditing.givenPlayer = gameObjects.player;
        startActivity(new Intent(view.getContext(), PlayerEditing.class));
    }

    //Handles when an 'Edit' button is clicked. It uses tags to pass the Edit Activity the corresponding object
    //And opens that activity
    public void onClick(View view)
    {
        String tag = view.getTag().toString();
        String[] tags = tag.split("@");

        if(tags[0].compareTo("State") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            StateEditing.givenState = (State)gameObjects.findObjectById(id);
            TextView stateTextView = findViewById(R.id.statesDropDown);
            startActivity(new Intent(view.getContext(), StateEditing.class));
            stateTextView.callOnClick();
        }
        else if(tags[0].compareTo("NormalTransition") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            NormalTransitionEditing.givenTransition = (NormalTransition)gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.transitionsDropDown);
            startActivity(new Intent(view.getContext(), NormalTransitionEditing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("CombatTransition") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            CombatTransitionEditing.givenTransition = (CombatTransition) gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.transitionsDropDown);
            startActivity(new Intent(view.getContext(), CombatTransitionEditing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("ItemTransition") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            ItemTransitionEditing.givenTransition = (ItemTransition) gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.transitionsDropDown);
            startActivity(new Intent(view.getContext(), ItemTransitionEditing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("ConvoTransition") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            ConvoTransitionEditing.givenTransition = (ConvoTransition) gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.transitionsDropDown);
            startActivity(new Intent(view.getContext(), ConvoTransitionEditing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("OneTimeTransition") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            OneTimeTransitionEditing.givenTransition = (OneTimeTransition) gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.transitionsDropDown);
            startActivity(new Intent(view.getContext(), OneTimeTransitionEditing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("RandomTransitionType1") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            RandomTransition1Editing.givenTransition = (RandomTransitionType1) gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.transitionsDropDown);
            startActivity(new Intent(view.getContext(), RandomTransition1Editing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("RandomTransitionType2") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            RandomTransition2Editing.givenTransition = (RandomTransitionType2) gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.transitionsDropDown);
            startActivity(new Intent(view.getContext(), RandomTransition2Editing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("RandomTransitionType3") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            RandomTransition3Editing.givenTransition = (RandomTransitionType3) gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.transitionsDropDown);
            startActivity(new Intent(view.getContext(), RandomTransition3Editing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("SwitchLeverTransition") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            SwitchLeverTransitionEditing.givenTransition = (SwitchLeverTransition) gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.transitionsDropDown);
            startActivity(new Intent(view.getContext(), SwitchLeverTransitionEditing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("Item") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            ItemEditing.givenItem = (Item) gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.itemsDropDown);
            startActivity(new Intent(view.getContext(), ItemEditing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("Weapon") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            WeaponEditing.givenWeapon = (Weapon) gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.itemsDropDown);
            startActivity(new Intent(view.getContext(), WeaponEditing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("Equipment") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            EquipmentEditing.givenWeapon = (Equipment) gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.itemsDropDown);
            startActivity(new Intent(view.getContext(), EquipmentEditing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("DamagingEffect") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            DamagingEffectEditing.givenEffect = (DamagingEffect)gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.effectsDropDown);
            startActivity(new Intent(view.getContext(), DamagingEffectEditing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("DefenceEffect") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            DefenceEffectEditing.givenEffect = (DefenceEffect)gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.effectsDropDown);
            startActivity(new Intent(view.getContext(), DefenceEffectEditing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("HealingEffect") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            HealingEffectEditing.givenEffect = (HealingEffect) gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.effectsDropDown);
            startActivity(new Intent(view.getContext(), HealingEffectEditing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("ConditionalSwitch") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            ConditionalSwitchEditing.givenConditional = (ConditionalSwitch) gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.conditionalsDropDown);
            startActivity(new Intent(view.getContext(), ConditionalSwitchEditing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("HasObject") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            ConditionalHasObjectEdit.givenConditional = (HasObject) gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.conditionalsDropDown);
            startActivity(new Intent(view.getContext(), ConditionalHasObjectEdit.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("HasSpell") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            ConditionalHasSpellEditing.givenConditional = (HasSpell) gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.conditionalsDropDown);
            startActivity(new Intent(view.getContext(), ConditionalHasSpellEditing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("Enemy") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            EnemyEditing.givenEnemy = (Enemy) gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.enemiesDropDown);
            startActivity(new Intent(view.getContext(), EnemyEditing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("NPC") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            NpcEditing.givenNPC = (NPC) gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.NPCDropDown);
            startActivity(new Intent(view.getContext(), NpcEditing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("ConversationState") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            ConversationStateEditing.givenState = (ConversationState) gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.conversationStatesDropDown);
            startActivity(new Intent(view.getContext(), ConversationStateEditing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("ChangeBaseStateConversationTransition") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            ChangeBaseConvoStateTransitionEditing.givenTransition = (ChangeBaseStateConversationTransition) gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.conversationTransitionsDropDown);
            startActivity(new Intent(view.getContext(), ChangeBaseConvoStateTransitionEditing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("NormalConversationTransition") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            NormalConversationTransitionEditing.normal = true;
            NormalConversationTransitionEditing.givenTransition = (NormalConversationTransition) gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.conversationTransitionsDropDown);
            startActivity(new Intent(view.getContext(), NormalConversationTransitionEditing.class));
            transTextView.callOnClick();
        }
        else if(tags[0].compareTo("TradingConversationTransition") == 0)
        {
            int id = Integer.parseInt(tags[2]);
            NormalConversationTransitionEditing.normal = false;
            NormalConversationTransitionEditing.otherGivenTransition = (TradingConversationTransition) gameObjects.findObjectById(id);
            TextView transTextView = findViewById(R.id.conversationTransitionsDropDown);
            startActivity(new Intent(view.getContext(), NormalConversationTransitionEditing.class));
            transTextView.callOnClick();
        }
    }

    //Dropdown menu that opens the corresponding Edit acitvity
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        String type = item.getTitle().toString();
        if(type.compareTo("Normal Transition") == 0)
        {
            startActivity(new Intent(this, NormalTransitionEditing.class));
        }
        else if(type.compareTo("Combat Transition") == 0)
        {
            startActivity(new Intent(this, CombatTransitionEditing.class));
        }
        else if(type.compareTo("PickUpItem Transition") == 0)
        {
            startActivity(new Intent(this, ItemTransitionEditing.class));
        }
        else if(type.compareTo("Conversation Transition") == 0)
        {
            startActivity(new Intent(this, ConvoTransitionEditing.class));
        }
        else if(type.compareTo("OneTime Transition") == 0)
        {
            startActivity(new Intent(this, OneTimeTransitionEditing.class));
        }
        else if(type.compareTo("Random1 Transition") == 0)
        {
            startActivity(new Intent(this, RandomTransition1Editing.class));
        }
        else if(type.compareTo("Random2 Transition") == 0)
        {
            startActivity(new Intent(this, RandomTransition2Editing.class));
        }
        else if(type.compareTo("Random3 Transition") == 0)
        {
            startActivity(new Intent(this, RandomTransition3Editing.class));
        }
        else if(type.compareTo("Switch Lever Transition") == 0)
        {
            startActivity(new Intent(this, SwitchLeverTransitionEditing.class));
        }
        else if(type.compareTo("Item") == 0)
        {
            startActivity(new Intent(this, ItemEditing.class));
        }
        else if(type.compareTo("Weapon") == 0)
        {
            startActivity(new Intent(this, WeaponEditing.class));
        }
        else if(type.compareTo("Equipment/Armor") == 0)
        {
            startActivity(new Intent(this, EquipmentEditing.class));
        }
        else if(type.compareTo("Damaging Effect") == 0)
        {
            startActivity(new Intent(this, DamagingEffectEditing.class));
        }
        else if(type.compareTo("Defence Effect") == 0)
        {
            startActivity(new Intent(this, DefenceEffectEditing.class));
        }
        else if(type.compareTo("Healing Effect") == 0)
        {
            startActivity(new Intent(this, HealingEffectEditing.class));
        }
        else if(type.compareTo("Conditional Switch") == 0)
        {
            startActivity(new Intent(this, ConditionalSwitchEditing.class));
        }
        else if(type.compareTo("Has Object") == 0)
        {
            startActivity(new Intent(this, ConditionalHasObjectEdit.class));
        }
        else if(type.compareTo("Has Spell") == 0)
        {
            startActivity(new Intent(this, ConditionalHasSpellEditing.class));
        }
        else if(type.compareTo("Change State Convo Transition") == 0)
        {
            startActivity(new Intent(this, ChangeBaseConvoStateTransitionEditing.class));
        }
        else if(type.compareTo("Normal Convo Transition") == 0)
        {
            NormalConversationTransitionEditing.normal = true;
            startActivity(new Intent(this, NormalConversationTransitionEditing.class));
        }
        else if(type.compareTo("Trading Convo Transition") == 0)
        {
            NormalConversationTransitionEditing.normal = false;
            startActivity(new Intent(this, NormalConversationTransitionEditing.class));
        }
        return true;
    }

    //Handles the main save button
    public void saveGame(View view)
    {
        gameObjects.saveGame(view);
    }



    //Handles when a user clicks on one of the drop down objects
    //It uses the addObjects function to add all of the GameObjects under the dropdown
    public void onDropDownClick(View view)
    {
        ArrayList<String> strings = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        if(view.getId() == findViewById(R.id.statesDropDown).getId())
        {
            for(State s : gameObjects.states)
            {
                if(s.uniqueUserId.isEmpty()) {
                    strings.add("" + s.getId());
                }
                else
                {
                    strings.add(s.uniqueUserId);
                }
                ids.add("" + s.getId());
                types.add("State");

            }
            addObjects(1, strings, types, ids);
            //List all states in gameObjects
        }
        else if(view.getId() == findViewById(R.id.transitionsDropDown).getId())
        {
            for(Transition s : gameObjects.transitions)
            {
                if(s instanceof  NormalTransition)
                {
                    types.add("NormalTransition");
                }
                else if(s instanceof CombatTransition)
                {
                    types.add("CombatTransition");
                }
                else if(s instanceof ItemTransition)
                {
                    types.add("ItemTransition");
                }
                else if(s instanceof ConvoTransition)
                {
                    types.add("ConvoTransition");
                }
                else if(s instanceof OneTimeTransition)
                {
                    types.add("OneTimeTransition");
                }
                else if(s instanceof RandomTransitionType1)
                {
                    types.add("RandomTransitionType1");
                }
                else if(s instanceof RandomTransitionType2)
                {
                    types.add("RandomTransitionType2");
                }
                else if(s instanceof RandomTransitionType3)
                {
                    types.add("RandomTransitionType3");
                }
                else if(s instanceof SwitchLeverTransition)
                {
                    types.add("SwitchLeverTransition");
                }
                else {
                    types.add("Transition");
                }
                strings.add(s.getUniqueUserId());
                ids.add(""+s.getId());
            }
            addObjects(2, strings, types, ids);
        }
        else if(view.getId() == findViewById(R.id.itemsDropDown).getId())
        {
            for(Item s : gameObjects.items)
            {
                if(s instanceof Weapon)
                {
                    types.add("Weapon");
                    strings.add(s.getUniqueUserId());
                }
                else if(s instanceof Equipment)
                {
                    types.add("Equipment");
                    strings.add(s.getUniqueUserId());
                }
                else if(s instanceof  Item)
                {
                    types.add("Item");
                    strings.add(s.getUniqueUserId());
                }


                ids.add(""+s.getId());
            }
            addObjects(3, strings, types, ids);
        }
        else if(view.getId() == findViewById(R.id.effectsDropDown).getId())
        {
            for(Effect s : gameObjects.effects)
            {
                if(s instanceof DamagingEffect)
                {
                    types.add("DamagingEffect");
                }
                else if(s instanceof DefenceEffect)
                {
                    types.add("DefenceEffect");
                }
                else if(s instanceof HealingEffect)
                {
                    types.add("HealingEffect");
                }
                strings.add(s.getMainId());
                ids.add(""+s.getId());

            }

            addObjects(4, strings, types, ids);
        }
        else if(view.getId() == findViewById(R.id.conditionalsDropDown).getId())
        {
            for(Conditional s : gameObjects.conditionals)
            {
                if(s instanceof ConditionalSwitch)
                {
                    types.add("ConditionalSwitch");
                }
                else if(s instanceof HasObject)
                {
                    types.add("HasObject");
                }
                else if(s instanceof HasSpell)
                {
                    types.add("HasSpell");
                }
                strings.add(s.getMainId());
                ids.add(""+s.getId());
            }

            addObjects(5, strings, types, ids);
        }
        else if(view.getId() == findViewById(R.id.enemiesDropDown).getId())
        {
            for(Enemy s : gameObjects.enemies)
            {
                if(s.uniqueUserId.isEmpty())
                {
                    strings.add(""+s.getId());
                }
                else
                {
                    strings.add(s.uniqueUserId);
                }
                ids.add(""+s.getId());
                types.add("Enemy");
            }

            addObjects(6, strings, types, ids);
        }
        else if(view.getId() == findViewById(R.id.NPCDropDown).getId())
        {
            for(NPC s : gameObjects.npcs)
            {
                if(s.uniqueUserId.isEmpty())
                {
                    strings.add(""+s.getId());
                }
                else
                {
                    strings.add(s.uniqueUserId);
                }
                ids.add(""+s.getId());
                types.add("NPC");
            }
            addObjects(7, strings, types, ids);
        }
        else if(view.getId() == findViewById(R.id.conversationStatesDropDown).getId())
        {
            for(ConversationState s : gameObjects.convoStates)
            {
                if(s.uniqueUserId.isEmpty())
                {
                    strings.add(""+s.getId());
                }
                else
                {
                    strings.add(s.uniqueUserId);
                }
                ids.add(""+s.getId());
                types.add("ConversationState");
            }

            addObjects(8, strings, types, ids);
        }
        else if(view.getId() == findViewById(R.id.conversationTransitionsDropDown).getId())
        {
            for(ConversationTransition s : gameObjects.convoTransitions)
            {
                if(s instanceof ChangeBaseStateConversationTransition)
                {
                    types.add("ChangeBaseStateConversationTransition");
                }
                if(s instanceof NormalConversationTransition)
                {
                    types.add("NormalConversationTransition");
                }
                if(s instanceof TradingConversationTransition)
                {
                    types.add("TradingConversationTransition");
                }
                strings.add(s.getEditMainId());
                ids.add(""+s.getId());

            }

            addObjects(9, strings, types, ids);
        }

    }

    //Index is where to start adding objects
    //AddThis is the displayed strings being passed
    //Types is the type info of the objects
    //Ids is the ids of the given objects
    //Close the dropDown if it was the lastDropped
    private static int lastDropped = -1;
    public void addObjects(int index, ArrayList<String> addThis, ArrayList<String> types, ArrayList<String> ids) {

        //What not to delete
        int[] dontDelete = {R.id.stateDrop, R.id.transDrop, R.id.itemsDrop, R.id.effectsDrop, R.id.condsDrop, R.id.enemiesDrop, R.id.NPCDrop, R.id.convoDrop, R.id.convoTransDrop, R.id.SaveEditGameButton};

       //Remove all views inbetween the dontDelete objects
        LinearLayout mainLinear = findViewById(R.id.editMainLinear);
        for (int i = 0; i < dontDelete.length - 1; i++) {
            int id1 = dontDelete[i];
            int id2 = dontDelete[i + 1];
            int index1 = mainLinear.indexOfChild(findViewById(id1)) + 1;
            int index2 = mainLinear.indexOfChild(findViewById(id2)) - 1;
            if (index1 < index2) {
                mainLinear.removeViews(index1, index2);
            } else if (index1 == index2) {
                mainLinear.removeViewAt(index1);
            }
        }

        //If the dropDown pressed is not open, open it and add all the objects
        if (lastDropped != index) {
            lastDropped = index;
            LinearLayout newLayout = new LinearLayout(getBaseContext());
            newLayout.setOrientation(LinearLayout.VERTICAL);
            newLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            mainLinear.addView(newLayout, index + 1);

            int i = 0;
            for (String s : addThis) {
                LinearLayout objects = new LinearLayout(newLayout.getContext());
                objects.setOrientation(LinearLayout.HORIZONTAL);
                objects.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

                TextView text = new TextView(newLayout.getContext());
                text.setText("                     " + s);
                text.setKeyListener(null);
                objects.addView(text);

                Button b = new Button(objects.getContext());

                b.setWidth(b.getWidth() / 2);
                b.setHeight(b.getHeight() / 2);
                b.setText("Edit");
                b.setOnClickListener(this);
                b.setTag(types.get(i) + "@" + s + "@" + ids.get(i));
                objects.addView(b);
                newLayout.addView(objects);
                i++;
            }
        }
        //If the dropped down pressed is open, then delete all the views and "close" it
        else
        {
            lastDropped = -1;
        }
    }
}
