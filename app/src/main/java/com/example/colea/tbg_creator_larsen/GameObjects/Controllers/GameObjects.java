package com.example.colea.tbg_creator_larsen.GameObjects.Controllers;

import android.view.View;

import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.ConditionalSwitch;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.HasObject;
import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.HasSpell;
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
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Player;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Weapon;
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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class GameObjects {
    public ArrayList<State> states = new ArrayList<State>();
    public ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    public ArrayList<Item> items = new ArrayList<Item>();
    public ArrayList<Effect> effects = new ArrayList<Effect>();
    public ArrayList<Transition> transitions = new ArrayList<Transition>();
    public ArrayList<ConversationState> convoStates = new ArrayList<ConversationState>();
    public ArrayList<ConversationTransition> convoTransitions = new ArrayList<ConversationTransition>();
    public ArrayList<NPC> npcs = new ArrayList<NPC>();
    public ArrayList<Conditional> conditionals = new ArrayList<Conditional>();
    public Player player;
    public String gameName;

    public GameObjects()
    {

    }

    public void setPlayer(Player p)
    {
        player = p;
    }

    public String toStringFromJSON() {
        try {
            JSONObject game = new JSONObject();

            for (State s : states) {
                JSONObject state = s.toJSON();
                game.put("" + (int) state.get("id"), state);
            }

            for(Enemy t : enemies)
            {
                JSONObject transition = t.toJSON();
                game.put("" + transition.get("id"), transition);
            }

            for(Item t : items)
            {
                JSONObject item = t.toJSON();
                game.put("" + item.get("id"), item);
            }

            for(Effect t : effects)
            {
                JSONObject effect = t.toJSON();
                game.put("" + effect.get("id"), effect);
            }

            for(Transition t : transitions)
            {
                JSONObject transition = t.toJSON();
                game.put("" + transition.get("id"), transition);
            }

            for(ConversationState t : convoStates)
            {
                JSONObject convoS = t.toJSON();
                game.put("" + convoS.get("id"), convoS);
            }

            for(ConversationTransition t : convoTransitions)
            {
                JSONObject convoS = t.toJSON();
                game.put("" + convoS.get("id"), convoS);
            }

            for(NPC t : npcs)
            {
                JSONObject npcJSON = t.toJSON();
                game.put("" + npcJSON.get("id"), npcJSON);
            }

            for(Conditional t : conditionals)
            {
                JSONObject condJSON = t.toJSON();
                game.put("" + condJSON.get("id"), condJSON);
            }


            JSONObject gameInOrder = new JSONObject();
            for(int i = 0; i < game.length(); i++)
            {
                gameInOrder.put(""+ i, game.get(""+i));
            }
            JSONObject pJSON = player.toJSON();
            gameInOrder.put("player", pJSON);
            //String test = gameInOrder.toString();
            //JSONObject tester = new JSONObject(test);
            return gameInOrder.toString();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public State fromString(String s)
    {
        try {
               JSONObject shouldWork = new JSONObject(s);
               for(int i = 0; i < shouldWork.length()-1; i++)
               {
                   JSONObject nextObject = (JSONObject) shouldWork.get(""+i);
                   String oType = (String)nextObject.get("OBJECT TYPE");
                   if(oType.compareTo("State") == 0)
                   {
                       states.add(State.fromJSON(nextObject));
                   }
                   if(oType.compareTo("Enemy") == 0)
                   {
                       enemies.add(Enemy.fromJSON(nextObject));
                   }
                   if(oType.compareTo("Item") == 0)
                   {
                       items.add(Item.fromJSON(nextObject));
                   }
                   if(oType.compareTo("Weapon") == 0)
                   {
                       items.add(Weapon.fromJSON(nextObject));
                   }
                   if(oType.compareTo("Equipment") == 0)
                   {
                       items.add(Equipment.fromJSON(nextObject));
                   }
                   if(oType.compareTo("NPC") == 0)
                   {
                       npcs.add(NPC.fromJSON(nextObject));
                   }
                   if(oType.compareTo("DamagingEffect") == 0)
                   {
                       effects.add(DamagingEffect.fromJSON(nextObject));
                   }
                   if(oType.compareTo("DefenceEffect") == 0)
                   {
                       effects.add(DefenceEffect.fromJSON(nextObject));
                   }
                   if(oType.compareTo("HealingEffect") == 0)
                   {
                       effects.add(HealingEffect.fromJSON(nextObject));
                   }
                   if(oType.compareTo("ConditionalSwitch") == 0)
                   {
                       conditionals.add(ConditionalSwitch.fromJSON(nextObject));
                   }
                   if(oType.compareTo("HasObject") == 0)
                   {
                       conditionals.add(HasObject.fromJSON(nextObject));
                   }
                   if(oType.compareTo("HasSpell") == 0)
                   {
                       conditionals.add(HasSpell.fromJSON(nextObject));
                   }
                   if(oType.compareTo("ConversationState") == 0)
                   {
                       convoStates.add(ConversationState.fromJSON(nextObject));
                   }
                   if(oType.compareTo("ChangeBaseStateConversationTransition") == 0)
                   {
                       convoTransitions.add(ChangeBaseStateConversationTransition.fromJSON(nextObject));
                   }
                   if(oType.compareTo("NormalConversationTransition") == 0)
                   {
                       convoTransitions.add(NormalConversationTransition.fromJSON(nextObject));
                   }
                   if(oType.compareTo("TradingConversationTransition") == 0)
                   {
                       convoTransitions.add(TradingConversationTransition.fromJSON(nextObject));
                   }
                   if(oType.compareTo("CombatTransition") == 0)
                   {
                       transitions.add(CombatTransition.fromJSON(nextObject));
                   }
                   if(oType.compareTo("ConvoTransition") == 0)
                   {
                       transitions.add(ConvoTransition.fromJSON(nextObject));
                   }
                   if(oType.compareTo("ItemTransition") == 0)
                   {
                       transitions.add(ItemTransition.fromJSON(nextObject));
                   }
                   if(oType.compareTo("NormalTransition") == 0)
                   {
                       transitions.add(NormalTransition.fromJSON(nextObject));
                   }
                   if(oType.compareTo("OneTimeTransition") == 0)
                   {
                       transitions.add(OneTimeTransition.fromJSON(nextObject));
                   }
                   if(oType.compareTo("RandomTransitionType1") == 0)
                   {
                       transitions.add(RandomTransitionType1.fromJSON(nextObject));
                   }
                   if(oType.compareTo("RandomTransitionType2") == 0)
                   {
                       transitions.add(RandomTransitionType2.fromJSON(nextObject));
                   }
                   if(oType.compareTo("RandomTransitionType3") == 0)
                   {
                       transitions.add(RandomTransitionType3.fromJSON(nextObject));
                   }
                   if(oType.compareTo("SwitchLeverTransition") == 0)
                   {
                       transitions.add(SwitchLeverTransition.fromJSON(nextObject));
                   }
               }

               player = Player.fromJSON((JSONObject)shouldWork.get("player"));
               linkObjects();
               for(State state : states)
               {
                   if(state.isStartState)
                   {
                       return state;
                   }
               }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void linkObjects()
    {
        for (State t : states) {
            t.link(this);
        }

        for(Enemy t : enemies)
        {
            t.link(this);
        }
        for(Item t : items)
        {
            t.link(this);
        }
        for(Effect t : effects)
        {
            t.link(this);
        }
        for(Transition t : transitions)
        {
            t.link(this);
        }
        for(ConversationState t : convoStates)
        {
            t.link(this);
        }
        for(ConversationTransition t : convoTransitions)
        {
            t.link(this);
        }
        for(NPC t : npcs)
        {
            t.link(this);
        }

        for(Conditional t : conditionals)
        {
            t.link(this);
        }

        player.link(this);
    }

    public Object findObjectById(int id)
    {
        for (State s : states) {
            if(s.getId() == id)
            {
                return s;
            }
        }
        for(Enemy t : enemies)
        {
            if(t.getId() == id)
            {
                return t;
            }
        }
        for(Item t : items)
        {
            if(t.getId() == id)
            {
                return t;
            }
        }
        for(Effect t : effects)
        {
            if(t.getId() == id)
            {
                return t;
            }
        }
        for(Transition t : transitions)
        {
            if(t.getId() == id)
            {
                return t;
            }
        }
        for(ConversationState t : convoStates)
        {
            if(t.getId() == id)
            {
                return t;
            }
        }
        for(ConversationTransition t : convoTransitions)
        {
            if(t.getId() == id)
            {
                return t;
            }
        }
        for(NPC t : npcs)
        {
            if(t.getId() == id)
            {
                return t;
            }
        }
        for(Conditional t : conditionals)
        {
            if(t.getId() == id)
            {
                return t;
            }
        }
        return null;
    }

    public void setStateTransitions()
    {
        for(State s : states)
        {

        }
    }

    public int getNewId()
    {
        int ret = -1;
        for (State s : states) {
            if(s.getId() > ret)
            {
                ret = s.getId();
            }
        }
        for(Enemy t : enemies)
        {
            if(t.getId() > ret)
            {
                ret = t.getId();
            }
        }
        for(Item t : items)
        {
            if(t.getId() > ret)
            {
                ret = t.getId();
            }
        }
        for(Effect t : effects)
        {
            if(t.getId() > ret)
            {
                ret = t.getId();
            }
        }
        for(Transition t : transitions)
        {
            if(t.getId() > ret)
            {
                ret = t.getId();
            }
        }
        for(ConversationState t : convoStates)
        {
            if(t.getId() > ret)
            {
                ret = t.getId();
            }
        }
        for(ConversationTransition t : convoTransitions)
        {
            if(t.getId() > ret)
            {
                ret = t.getId();
            }
        }
        for(NPC t : npcs)
        {
            if(t.getId() > ret)
            {
                ret = t.getId();
            }
        }
        for(Conditional t : conditionals)
        {
            if(t.getId() > ret)
            {
                ret = t.getId();
            }
        }
        return ret + 1;
    }

    public void saveGame(View view)
    {
        MainAppController.saveFile(view.getContext(), toStringFromJSON(), gameName);
    }

}
