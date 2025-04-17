package io.github.Pacman.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class GameOverScreen implements Screen {
    private final PacmanGame game;
    private final BitmapFont font;
    private final GlyphLayout layout;
    private final GlyphLayout subText;

    public GameOverScreen(PacmanGame game) {
        this.game = game;
        font = new BitmapFont();
        font.setColor(Color.RED);
        font.getData().setScale(3);
        layout = new GlyphLayout(font, "GAME OVER");

        subText = new GlyphLayout();
        subText.setText(font, "Press SPACE to restart");
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new MainMenu(game)); // Restart game
            dispose();
            return;
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float centerX = (640 - layout.width) / 2;
        float centerY = (480 + layout.height) / 2;

        float subX = (640 - subText.width) / 2;
        float subY = centerY - layout.height - 30;

        game.batch.begin();
        font.draw(game.batch, layout, centerX, centerY);
        font.draw(game.batch, subText, subX, subY);
        game.batch.end();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { font.dispose(); }
}
