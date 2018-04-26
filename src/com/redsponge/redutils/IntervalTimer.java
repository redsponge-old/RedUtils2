package com.redsponge.redutils;

import com.redsponge.redutils.console.ConsoleMSG;
import com.redsponge.redutils.util.ThreadPool;

public class IntervalTimer implements Runnable {

    private Runnable toRun;
    private int timesPerSecond;
    private boolean running;

    private boolean printFPS;
    private String id;

    public IntervalTimer(Runnable toRun, ThreadPool pool, int timesPerSecond, boolean printFPS, String timerId) {
        this.toRun = toRun;
        this.timesPerSecond = timesPerSecond;
        this.printFPS = printFPS;
        this.id = timerId;
        running = true;
        pool.runTask(this);
    }

    public IntervalTimer(Runnable toRun, ThreadPool pool, int timesPerSecond) {
        this(toRun, pool, timesPerSecond, false, "");
    }

    @Override
    public void run() {
        double timePerTick = 1000000000 / timesPerSecond;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;
        long ticks = 0;

        while(running) {
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            timer += now - lastTime;
            lastTime = now;
            if(delta >= 1) {
                toRun.run();
                delta--;
                ticks++;
                if(delta >= 1) {
                    delta = 0;
                }
            }

            if(timer >= 1000000000){
                if(printFPS) {
                    ConsoleMSG.INFO.info("FPS on task [" + id + "] is:" + ticks + ". [nano: " + timer + "]");
                }
                ticks = 0;
                timer = 0;
            }
        }
    }
}
