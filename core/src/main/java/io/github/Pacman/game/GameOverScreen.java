package io.github.Pacman.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.Preferences;

public class GameOverScreen implements Screen {
    private final PacmanGame game;
    private final BitmapFont titleFont;
    private final BitmapFont infoFont;
    private final GlyphLayout layout = new GlyphLayout();
    private final Preferences prefs;

    private final int score;
    private final int highScore;
    private boolean transitioning = false;
    private float blinkTimer = 0;
    private boolean showBlinkText = true;

    public GameOverScreen(PacmanGame game, int score) {
        this.game = game;
        this.score = score;

        // Load preferences for high score
        prefs = Gdx.app.getPreferences("PacmanPrefs");
        int savedHighScore = prefs.getInteger("highscore", 0);
        if (score > savedHighScore) {
            savedHighScore = score;
            prefs.putInteger("highscore", score);
            prefs.flush();
        }
        this.highScore = savedHighScore;

        // Fonts
        titleFont = new BitmapFont(); // Can be replaced with TTF-generated fonts
        titleFont.setColor(Color.RED);
        titleFont.getData().setScale(5);

        infoFont = new BitmapFont();
        infoFont.setColor(Color.WHITE);
        infoFont.getData().setScale(2);
    }

    @Override
    public void render(float delta) {
        handleInput();

        blinkTimer += delta;
        if (blinkTimer > 0.5f) {
            blinkTimer = 0;
            showBlinkText = !showBlinkText;
        }

        Gdx.gl.glClearColor(0, 0, 0, 1); // Black background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        game.batch.begin();

        // Draw title with shadow
        layout.setText(titleFont, "GAME OVER");
        float titleX = (screenWidth - layout.width) / 2;
        float titleY = screenHeight - 150;

        titleFont.setColor(Color.BLACK);
        titleFont.draw(game.batch, layout, titleX + 4, titleY - 4); // Shadow
        titleFont.setColor(Color.RED);
        titleFont.draw(game.batch, layout, titleX, titleY);

        // Draw Score
        layout.setText(infoFont, "Score: " + score);
        infoFont.draw(game.batch, layout, (screenWidth - layout.width) / 2, titleY - 100);

        // Draw High Score
        layout.setText(infoFont, "High Score: " + highScore);
        infoFont.draw(game.batch, layout, (screenWidth - layout.width) / 2, titleY - 160);

        // Blinking "Press SPACE" prompt
        if (showBlinkText) {
            layout.setText(infoFont, "Press SPACE to return to Main Menu", Color.LIGHT_GRAY, screenWidth, Align.center, false);
            infoFont.draw(game.batch, layout, 0, 100);
        }

        game.batch.end();
    }

    private void handleInput() {
        if (!transitioning && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            transitioning = true;
            game.setScreen(new MainMenu(game));
            dispose();
        }
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        titleFont.dispose();
        infoFont.dispose();
    }
}
