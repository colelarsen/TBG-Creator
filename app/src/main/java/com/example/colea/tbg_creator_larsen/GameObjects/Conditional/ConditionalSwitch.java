package com.example.colea.tbg_creator_larsen.GameObjects.Conditional;

import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.DamagingEffect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConditionalSwitch extends Conditional {
    public static ArrayList<String> switches = new ArrayList<>();
    public static ArrayList<Boolean> switchesValue = new ArrayList<>();


    public Conditional or;
    public Conditional and;
    public String singleSwitch;
    public int id;
    public boolean not = false;
    public String uniqueUserId = "";

    public String getUUID()
    {
        return uniqueUserId;
    }

    public String getMainId()
    {
        if(uniqueUserId.isEmpty())
        {
            return ""+id;
        }
        else
        {
            return getUUID();
        }
    }


    public int conditionalOrId = -1;
    public int conditionalAndId = -1;
    public ConditionalSwitch(boolean starting, String obj1, Conditional an, Conditional o)
    {
        singleSwitch = obj1;
        switches.add(singleSwitch);
        switchesValue.add(Boolean.valueOf(starting));
        and = an;
        or = o;
        id = GameController.getId();
    }
    public ConditionalSwitch(String obj1, int andId, int orId, boolean noT, int i)
    {
        singleSwitch = obj1;
        conditionalAndId = andId;
        conditionalOrId = orId;
        not = noT;
        id = i;
    }

    @Override
    public void link(GameObjects gameObjects) {
        or = (Conditional) gameObjects.findObjectById(conditionalOrId);
        and = (Conditional) gameObjects.findObjectById(conditionalAndId);
    }

    public static ConditionalSwitch fromJSON(JSONObject nextObject)
    {
        /*
            private static ArrayList<String> switches = new ArrayList<>();
            private static ArrayList<Boolean> switchesValue = new ArrayList<>();
            private Conditional or;
            private Conditional and;
            private String singleSwitch;
            private int id;
            private boolean not = false;
            */
        try {
            int id = nextObject.getInt("id");
            String singleSwitch = nextObject.getString("singleSwitch");
            int orId = -1;
            int andId = -1;
            String uuid = "";
            if(nextObject.has("uuid"))
            {
                uuid = nextObject.getString("uuid");
            }
            if(nextObject.has("or"))
            {
                orId = nextObject.getInt("or");
            }
            if(nextObject.has("and"))
            {
                andId = nextObject.getInt("and");
            }
            boolean not = nextObject.getBoolean("not");

            if(ConditionalSwitch.switches.isEmpty())
            {
                JSONArray switc = nextObject.getJSONArray("switches");
                switches = new ArrayList<>();
                for (int i = 0; i < switc.length(); i++)
                {
                    switches.add(switc.getString(i));
                }
            }

            if(ConditionalSwitch.switchesValue.isEmpty())
            {
                JSONArray switc = nextObject.getJSONArray("switchesValue");
                switchesValue = new ArrayList<>();
                for (int i = 0; i < switc.length(); i++) {
                    switchesValue.add(switc.getBoolean(i));
                }
            }
            ConditionalSwitch conditionalSwitch = new ConditionalSwitch(singleSwitch, andId, orId, not, id);
            conditionalSwitch.uniqueUserId = uuid;
            return conditionalSwitch;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject toJSON()
    {
        try {
            /*
            private static ArrayList<String> switches = new ArrayList<>();
            private static ArrayList<Boolean> switchesValue = new ArrayList<>();
            private Conditional or;
            private Conditional and;
            private String singleSwitch;
            private int id;
            private boolean not = false;
            */
            JSONObject stateObject = new JSONObject();

            stateObject.put("id", id);
            if(or != null) {
                stateObject.put("or", or.getId());
            }
            if(and != null) {
                stateObject.put("and", and.getId());
            }
            stateObject.put("uuid", uniqueUserId);
            stateObject.put("not", not);
            stateObject.put("singleSwitch", singleSwitch);
            stateObject.put("OBJECT TYPE", "ConditionalSwitch");


            JSONArray switc = new JSONArray();
            for(String s : switches)
            {
                switc.put(s);
            }
            stateObject.put("switches", switc);

            JSONArray switcVal = new JSONArray();
            for(Boolean s : switchesValue)
            {
                switcVal.put(s);
            }
            stateObject.put("switchesValue", switcVal);

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

    public static void switchSwitch(String x)
    {
        for(int i = 0; i < switches.size(); i++)
        {
            String s = switches.get(i);
            if(x.compareTo(s) == 0)
            {
                boolean value = !switchesValue.get(i).booleanValue();
                switchesValue.set(i, Boolean.valueOf(value));
            }
        }
    }

    public static boolean isOn(String x)
    {
        for(int i = 0; i < switches.size(); i++)
        {
            String s = switches.get(i);
            if(x.compareTo(s) == 0)
            {
                return switchesValue.get(i).booleanValue();
            }
        }
        return false;
    }

    public void not()
    {
        not = !not;
    }

    public boolean check() {
        boolean ret = false;
        for(int i = 0; i < switches.size(); i++)
        {
            String s = switches.get(i);
            if(singleSwitch.compareTo(s) == 0)
            {
                boolean value = switchesValue.get(i).booleanValue();
                if(or != null)
                {
                    ret = (value || or.check());
                }
                else if(and != null)
                {
                    ret = (value && and.check());
                }
                else
                {
                    ret = value;
                }

                if(not)
                {
                    return !ret;
                }
                else
                {
                    return ret;
                }
            }
        }

        ret = false;
        if(not)
        {
            return !ret;
        }
        else
        {
            return ret;
        }
    }



    @Override
    public void setConditional(String obj1, Conditional an, Conditional o) {
        singleSwitch = obj1;
        switches.add(singleSwitch);
        switchesValue.add(false);
        and = an;
        or = o;
    }
}
