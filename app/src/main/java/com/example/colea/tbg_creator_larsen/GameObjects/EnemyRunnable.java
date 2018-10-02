package com.example.colea.tbg_creator_larsen.GameObjects;

import com.example.colea.tbg_creator_larsen.GameObjects.Enemy;

public class EnemyRunnable implements Runnable {

    public Enemy enemy;
    public EnemyRunnable(Enemy enemy) {
        this.enemy = enemy;
    }

    @Override
    public void run() {

    }
}