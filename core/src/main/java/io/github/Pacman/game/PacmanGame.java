package io.github.Pacman.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PacmanGame extends com.badlogic.gdx.Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new MainMenu(this)); // Start with Main Menu
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
