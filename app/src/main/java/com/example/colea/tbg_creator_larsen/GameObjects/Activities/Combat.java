package com.example.colea.tbg_creator_larsen.GameObjects.Activities;

import android.arch.lifecycle.LifecycleObserver;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.Effect;
import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;
import com.example.colea.tbg_creator_larsen.GameObjects.EnemyRunnable;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Equipment;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Item;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Player;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Weapon;
import com.example.colea.tbg_creator_larsen.GameObjects.R;

public class Combat extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, LifecycleObserver {

    public static Enemy[] enemies;
    private boolean combatOngoing = true;
    private boolean playerTurn = true;
    public static int TURN_DURATION = 2000;
    private View currentView;

    //@ToDO
    //Combat player dying is broken

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat);
        currentView = findViewById(R.id.enemyScrollView).getRootView();
        //testCombat();
        startCombat();
    }

    //Set up test combat
    private void testCombat()
    {
        Weapon badWe = new Weapon("None", "None", 0, true, 1);
        Equipment badAr = new Equipment("None", "None", 0, true, 0);
        Enemy rat = new Enemy(10, 10, "Bagel", "Large Rat", badWe, badAr, null, null, false, null, null);
        Enemy rat2 = new Enemy(10, 10, "Rat But Bigger", "Large Rat", badWe, badAr, null, null, false, null, null);

        Enemy[] enemies = new Enemy[2];
        enemies[0] = rat;
        enemies[1] = rat2;
        Combat.enemies = enemies;

        TextView combatInfo = findViewById(R.id.combatInfo);
        combatInfo.setKeyListener(null);
        updateCombatInfo(Player.getPlayer().name + "'s Turn");

        TextView playerInfo = findViewById(R.id.player_Info);
        playerInfo.setKeyListener(null);

        startCombat();
    }

    private void startCombat()
    {
        Player.getPlayer().combatStart();
        for(Enemy en : enemies)
        {
            en.combatStart();
        }
        setUpCombatants(findViewById(R.id.enemyScrollView));
    }

    //Add a row for each enemy and update their health and other info
    private View vi;
    private void setUpCombatants(View view)
    {
        vi = view;
        LinearLayout enemyRows =  view.findViewById(R.id.enemyLayout);
        enemyRows.removeAllViews();

        int position = 0;
        for (Enemy enemy : enemies) {
            LinearLayout enemyColumns = new LinearLayout(view.getContext());
            enemyColumns.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            //Text view info
            TextView text = new TextView(view.getContext());
            String enemyName = enemy.name;

            String enemyLine = String.format("%s: %d", enemyName, enemy.health);

            while(enemyLine.length() < 50)
            {
                enemyLine += " ";
            }


            text.setText(enemyLine);
            text.setKeyListener(null);
            enemyColumns.addView(text);

            //Button view for selecting
            Button b = new Button(view.getContext());
            b.setText("Select");
            b.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        selectEnemy(v);
                    }
                });
            b.setTag(""+position);

            //Add button to enemy row
            enemyColumns.addView(b);

            //Add item to rows
            enemyRows.addView(enemyColumns);
            position++;
        }

        updatePlayerCombat(view);
    }

    //Makes it so the user cannot segue back out of combat
    @Override
    public void onBackPressed() {
        if(combatOngoing) {
            moveTaskToBack(true);
        }
    }




    /*
    ********************************************
    Update UI
    ********************************************
    */

    //Disables player action buttons when it's the enemy turn
    private void disableButtons()
    {
        int[] ids = {R.id.attack, R.id.defend, R.id.spell, R.id.item, R.id.converse, R.id.runAway};
        for(int id : ids)
        {
            Button b = findViewById(id);
            b.setAlpha(.1f);
            b.setClickable(false);
        }
    }

    //Enables the player action buttons
    private void enableButtons()
    {
        int[] ids = {R.id.attack, R.id.defend, R.id.spell, R.id.item, R.id.converse, R.id.runAway};
        for(int id : ids)
        {
            Button b = findViewById(id);
            b.setAlpha(1f);
            b.setClickable(true);
        }
    }

    public void updatePlayerCombat(View view)
    {
        Player player = Player.getPlayer();
        TextView playerInfo = findViewById(R.id.player_Info);
        String playerLine = String.format("%s: HP %d, Atk %d, Def %d", player.name, player.getHealth(), player.attack(), calcDefence(player));
        while(playerLine.length() < 50)
        {
            playerLine += " ";
        }
        playerInfo.setText(playerLine);
    }

    public void updateCombatInfo(String str)
    {
        TextView combatInfo = findViewById(R.id.combatInfo);
        combatInfo.setText(str);
    }





    /*
    ****************************************************
    * Player Options
    ****************************************************
    */

    public void attack(View view)
    {
        if(playerTurn) {
            updateCombatInfo("Select Target");
            playerAction = Player.getPlayer().name + " Attacked ";
        }
    }

    public void defend(View view)
    {
        if(playerTurn) {
            updateCombatInfo(Player.getPlayer().name + " Defended");
            Player.getPlayer().defending = true;
            currentTurnOver(true);
        }
    }

    int whatMenu = -1;
    public void useItem(View view)
    {
        if(playerTurn) {
            PopupMenu popup = new PopupMenu(this, view);
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.spell_popup);
            for(Item i : Player.getPlayer().inventory.getItems())
            {
                if(i.isUseable()) {
                    popup.getMenu().add(i.getName());
                }
            }
            popup.show();
            whatMenu = 0;
        }
    }

    public void useSpell(View view)
    {
        if(playerTurn) {
            updateCombatInfo("Select Spell");

            PopupMenu popup = new PopupMenu(this, view);
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.spell_popup);
            for(Effect spell : Player.getPlayer().spells)
            {
                popup.getMenu().add(spell.getName());
            }
            popup.show();
            whatMenu = 1;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(whatMenu == 0) {
            String itemName = item.getTitle().toString();
            Item i = Player.getPlayer().inventory.findItemByName(itemName);
            Effect e = i.getEffect();
            playerAction = Player.getPlayer().name + " Used Item," + e.getName() + "," + i.getName();
            updateCombatInfo("Select Target");
            whatMenu = -1;
            return true;
        }
        if(whatMenu == 1) {
            String spellName = item.getTitle().toString();
            Effect e = GameController.getEffectByName(spellName);

            playerAction = Player.getPlayer().name + " Used Spell," + e.getName();
            updateCombatInfo("Select Target");
            whatMenu = -1;
            return true;
        }
        return false;
    }

    public void talk(View view)
    {
        if(playerTurn) {
            updateCombatInfo("Select Target");
            playerAction = Player.getPlayer().name + " Talked to";
        }
    }

    public void runAway(View view)
    {
        if(playerTurn) {
            updateCombatInfo(Player.getPlayer().name + " Attempts to Run Away");
            if (Math.random() > 0.5) {
                disableButtons();
                combatOngoing = false;
                Handler h=new Handler();
                h.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        updateCombatInfo(Player.getPlayer().name + " Ran Away");
                    }
                }, 1000);
                endCombat();
            }
            else {
                currentTurnOver(true);
            }
        }
    }

    //Called when the select button on the player is pressed
    public void playerSelected(View view) {
        TextView combatInfo = findViewById(R.id.combatInfo);


        String[] specifics = playerAction.split(",");

        boolean talkingToSelf = (playerAction.compareTo(Player.getPlayer().name + " Talked to") == 0);

        //If it's the player's turn and a target needs to be selected
        if(playerTurn && (combatInfo.getText().toString().compareTo("Select Target") == 0) && !talkingToSelf) {

            if(playerAction.compareTo(Player.getPlayer().name + " Attacked ") == 0)
            {
                int damage = calculateDamage(Player.getPlayer().attack(), Player.getPlayer().defence(), true, null);
                updateCombatInfo(playerAction + Player.getPlayer().name + " for " + damage + " damage");
            }
            else if(specifics[0].compareTo(Player.getPlayer().name + " Used Spell") == 0)
            {
                castEffect(Player.getPlayer(), GameController.getEffectByName(specifics[1]), "");
            }
            else if(specifics[0].compareTo(Player.getPlayer().name + " Used Item") == 0)
            {
                castEffect(Player.getPlayer(), GameController.getEffectByName(specifics[1]), specifics[2]);
            }
            setUpCombatants(vi);

            if(Player.getHealth() > 0)
            {
                currentTurnOver(true);
                playerAction = "";
            }
            else
            {
                combatOngoing = false;
                Handler h=new Handler();
                h.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        updateCombatInfo(Player.getPlayer().name + " Killed Themselves");
                    }
                }, 1000);
                endCombat();
            }
        }
    }

    public void castEffect(Object target, Effect e, String itemName)
    {
        String spellHappened = "";
        if(target instanceof Player) {
            spellHappened = Player.getPlayer().applyEffect(e);
        }
        else
        {
            Enemy enemy = (Enemy)target;
            spellHappened = enemy.applyEffect(e);
        }
        if(itemName.compareTo("") == 0)
        {
            updateCombatInfo(spellHappened);
        }
        else
        {
            String[] splitEffect = spellHappened.split(" ");
            spellHappened = "";
            for(String s : splitEffect)
            {
                if(s.compareTo(e.getName()) == 0)
                {
                    s = itemName;
                }
                spellHappened = spellHappened + s + " ";
            }

            updateCombatInfo(spellHappened);
        }

    }


    //Called when an enemy is selected
    String playerAction = "";
    public void selectEnemy(View view)
    {
        TextView combatInfo = findViewById(R.id.combatInfo);
        String[] specifics = playerAction.split(",");

        Enemy enemy = enemies[Integer.parseInt(view.getTag().toString())];

        //If it's the player's turn and a target needs to be selected
        boolean talking = (playerAction.compareTo(Player.getPlayer().name + " Talked to") == 0);

        if(playerTurn && (combatInfo.getText().toString().compareTo("Select Target") == 0) && !talking)
        {
            if(playerAction.compareTo(Player.getPlayer().name + " Attacked ") == 0)
            {
                int damage = calculateDamage(Player.getPlayer().attack(), enemy.defence(), false, enemy);
                updateCombatInfo(playerAction + enemy.name + " for " + damage + " damage");
                enemy.health = (enemy.health - damage < 0)? 0 : enemy.health - damage;
            }
            else if(specifics[0].compareTo(Player.getPlayer().name + " Used Spell") == 0)
            {
                castEffect(enemy, GameController.getEffectByName(specifics[1]), "");
            }
            else if(specifics[0].compareTo(Player.getPlayer().name + " Used Item") == 0)
            {
                castEffect(enemy, GameController.getEffectByName(specifics[1]), specifics[2]);
            }
            setUpCombatants(vi);
            playerAction = "";
            currentTurnOver(true);
        }
        else
        {
            if(specifics[0].compareTo(Player.getPlayer().name + " Talked to") == 0)
            {
                if(enemy.canConverse)
                {
                    disableButtons();
                    testConvo(enemy);
                }
                else
                {
                    currentTurnOver(true);
                    playerAction = "";
                }
                updateCombatInfo(Player.getPlayer().name + " is trying to talk to " + enemy.name);
                playerTurn = false;
            }
        }
    }

    static boolean firstResume = true;
    @Override
    protected void onResume() {
        super.onResume();
        if(!firstResume && !playerTurn)
        {
            playerAction = "";
            currentTurnOver(true);
        }
        firstResume = false;
    }

    public void testConvo(Enemy en)
    {
        Handler h=new Handler();
        Conversation.currentNPC = en;
        h.postDelayed(new Runnable(){
            @Override
            public void run(){
                startActivity(new Intent(Combat.this, Conversation.class));
            }
        }, 1000);
    }


    /*
    ********************************************************
    MATH
    ********************************************************
    */

    public int calcDefence(Object o)
    {
        if(o instanceof Player)
        {
            Player player = (Player)o;
            int defence = player.defence();
            if(player.isDefending())
            {
                defence = (defence == 0)? 1 : defence*2;
            }
            return defence;
        }
        else if(o instanceof Enemy)
        {
            Enemy en = (Enemy)o;
            int defence = en.defence();
            if(en.isDefending())
            {
                defence = (defence == 0)? 1 : defence*2;
            }
            return defence;
        }
        return 0;
    }

    //Calculates damage and returns
    public int calculateDamage(int attack, int defence, boolean againstPlayer, Enemy e)
    {
        if(againstPlayer)
        {
            Player player = Player.getPlayer();
            defence = calcDefence(player);
            int damage = (attack - defence > 0)? attack - defence: 0;

            player.setHealth(player.getHealth() - damage);
            return damage;
        }
        else
        {
            defence = calcDefence(e);
            return (attack - defence > 0)? attack - defence: 0;
        }
    }






    /*
    ********************************************
    ENEMY METHODS
    ********************************************
    */

    //Makes each enemy have a turn if they are still alive
    public void playEnemies()
    {
        int deadEn = 0;
        for(int i = 0; i < enemies.length; i++)
        {
            if(enemyActive(enemies[i])) {
                enemies[i].turnStarts();
                enemyTurn(enemies[i], (i-deadEn + 1) * TURN_DURATION, lastAliveEnemy(i));
                enemies[i].turnOver();
            }
            else
            {
                deadEn++;
            }
        }
    }

    //Returns if the position passed is the last enemy alive
    public boolean lastAliveEnemy(int pos)
    {
        boolean lastAlive = true;
        if(pos == enemies.length-1)
        {
            return true;
        }

        for(int i = pos+1; i < enemies.length; i++)
        {
            if(enemyActive(enemies[i]))
            {
                lastAlive = false;
            }
        }
        return lastAlive;
    }

    public boolean enemyActive(Enemy enemy)
    {
        return enemy.health > 0 && !enemy.isPassive;
    }

    //Individual enemy turn
    public void enemyTurn(Enemy enemy, int delay, boolean last)
    {
        Handler h=new Handler();

        h.postDelayed(new EnemyRunnable(enemy){
            @Override
            public void run(){

                updateCombatInfo(this.enemy.name + " Attacked for " + calculateDamage(enemy.attack(), -1, true, null) +  " Damage");
                setUpCombatants(vi);
                enemyTurnOver();
            }
        }, delay);

        if(last)
        {
            Handler j=new Handler();
            j.postDelayed(new Runnable(){
                @Override
                public void run(){
                    currentTurnOver(false);
                }
            }, delay + TURN_DURATION);
        }



        //@TODO When you make the enemies able to use spells make sure to pre-pend their name to the returned spell effect
    }

    //Called at the end of every enemies turn
    public void enemyTurnOver()
    {
        combatOver(Player.getPlayer());
        if(!combatOngoing) {
            endCombat();
        }
    }






    /*
    ***************************************************
    General Combat Methods
    ***************************************************
    */

    //Segues back to the main game screen
    public void endCombat()
    {
        Player p = Player.getPlayer();
        p.combatOver();

        if(p.getHealth() > 0) {
            for(Enemy e : enemies)
            {
                EnemyDropScreen.addToDrops(e.drops, e.dropChance);
                e.combatOver();
            }
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Combat.this, EnemyDropScreen.class));
                    finish();
                }
            }, 4000);
        }
        else //Player hecking died
        {

        }
    }

    //Checks to see if combat is over and sets combatOngoing accordingly
    public void combatOver(Player p)
    {
        boolean enemiesHealth = false;
        boolean playerHasHealth = p.getHealth() > 0;
        for(Enemy enemy : enemies)
        {
            if(enemyActive(enemy))
            {
                enemiesHealth = true;
            }
        }
        combatOngoing = (playerHasHealth && enemiesHealth);

        if(combatOngoing == false)
        {
            if(!playerHasHealth) {
                updateCombatInfo("You Died");
            }
            else
            {
                updateCombatInfo("All Enemies Dead or Neutral");
            }
        }
    }

    //Called at the end of the player turn and the end of the last enemy turn
    public void currentTurnOver(boolean enemyTurn)
    {
        playerTurn = !enemyTurn;
        Player player = Player.getPlayer();
        String combatInf = "";
        if(playerTurn)
        {
            player.turnStarts();
            combatInf = player.name + "'s Turn";
            updateCombatInfo(combatInf);
            enableButtons();
        }
        else
        {
            disableButtons();
        }

        updatePlayerCombat(currentView);
        combatOver(Player.getPlayer());
        if(!combatOngoing) {
            endCombat();
        }

        if(enemyTurn)
        {
            Player.getPlayer().turnOver();
            updatePlayerCombat(currentView);
            playEnemies(); //<-- put your code in here.
        }
    }
}
