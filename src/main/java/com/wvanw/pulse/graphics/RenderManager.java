package com.wvanw.pulse.graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class RenderManager {

    @FunctionalInterface
    public interface RenderCommand {
        void render(GraphicsContext gc);
    }

    private final static RenderManager INSTANCE = new RenderManager();
    private final List<RenderCommand> queue = new ArrayList<>();

    private RenderManager() {}

    public static RenderManager getInstance() {
        return INSTANCE;
    }

    public void queue(RenderCommand cmd) {
        queue.add(cmd);
    }

    public void flush(GraphicsContext gc) {
        gc.setFill(Color.rgb(24, 24, 24));
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        for (RenderCommand cmd : queue)
            cmd.render(gc);
        queue.clear();
    }
}
