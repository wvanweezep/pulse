package com.wvanw.pulse.core;

import com.wvanw.pulse.entities.EntityManager;
import com.wvanw.pulse.graphics.RenderManager;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TickManager extends AnimationTimer {

    private static final double TARGET_FPS = 20.0;
    private static final double NANO_FPS = 1_000_000_000.0 / TARGET_FPS;

    private final GraphicsContext gc;

    private long lastUpdate;
    private long lastFpsUpdate;
    private double delta;
    private int frameCount;
    private int currentFps;

    public TickManager(GraphicsContext gc) {
        this.gc = gc;
    }

    @Override
    public void handle(long now) {
        if (lastUpdate == 0) {
            lastUpdate = now;
            return;
        }

        double elapsed = now - lastUpdate;
        delta += elapsed / NANO_FPS;

        while (delta >= 1) {
            // EntityManager.getInstance().fixedUpdate();
            delta--;
        }

        RenderManager.getInstance().queue(gc -> {
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font(16));
            gc.fillText("FPS: " + currentFps, 10, 20);
        });

        // EntityManager.getInstance().update();
        // EntityManager.getInstance().render();
        RenderManager.getInstance().flush(gc);
        lastUpdate = now;

        frameCount++;
        if (now - lastFpsUpdate >= 1_000_000_000L) {
            currentFps = frameCount;
            frameCount = 0;
            lastFpsUpdate = now;
        }
    }
}
