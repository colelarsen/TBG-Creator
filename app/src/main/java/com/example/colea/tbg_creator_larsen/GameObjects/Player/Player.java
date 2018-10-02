package com.example.colea.tbg_creator_larsen.GameObjects.Player;

public class Player {
    private static Player p = new Player(15, "Cole");

    private int health;
    public String name;
    private int defenceScore = 0;
    private int attackScore = 1;
    private Weapon equippedWeapon = new Weapon("None", "None", 0, true, 1);
    private Equipment equippedArmor = new Equipment("None", "None", 0, true, 0);
    //public ArrayList<Spell> spells = new ArrayList<Spell>();
    //public ArrayList<Skill> skills = new ArrayList<Skill>();

    private Player(int hp, String n)
    {
        health = hp;
        name = n;
    }

    public static Player getPlayer()
    {
        if(p == null) {
            Player.p = new Player(15, "Cole");
        }
        return p;
    }

    public static void setAttack(int i) { Player.getPlayer().attackScore = i; }

    public static void setDefence(int i)
    {
        Player.getPlayer().defenceScore = i;
    }

    public static void equipWeapon(Weapon w)
    {
        Player p = Player.getPlayer();
        if(p.equippedWeapon != null) {
            Player.getPlayer().equippedWeapon.setIsEqu(false);

            Player.setAttack(Player.getPlayer().attackScore - Player.getPlayer().equippedWeapon.getAttack());

            Player.getPlayer().equippedWeapon = w;
            Player.setAttack(Player.getPlayer().attackScore + Player.getPlayer().equippedWeapon.getAttack());
        }
        else
        {
            p.equippedWeapon = w;
            Player.setAttack(Player.getPlayer().attackScore + Player.getPlayer().equippedWeapon.getAttack());
        }
    }

    public static void setHealth(int h)
    {
        Player.getPlayer().health = h;
    }

    public static int getHealth()
    {
        return Player.getPlayer().health;
    }

    public static void equipArmor(Equipment e)
    {
        Player.getPlayer().equippedArmor.setIsEqu(false);

        Player.setDefence(Player.getPlayer().defenceScore - Player.getPlayer().equippedArmor.getDefence());

        Player.getPlayer().equippedArmor = e;
        Player.setDefence(Player.getPlayer().defenceScore + Player.getPlayer().equippedArmor.getDefence());
    }

    public int attack()
    {
        return equippedWeapon.getAttack();
    }

    public int defence()
    {
        return equippedArmor.getDefence();
    }
}
