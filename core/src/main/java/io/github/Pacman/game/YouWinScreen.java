package io.github.Pacman.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.Color;

public class YouWinScreen implements Screen {
    private final PacmanGame game;
    private final BitmapFont font;
    private final GlyphLayout layout;

    public YouWinScreen(PacmanGame game) {
        this.game = game;
        this.font = new BitmapFont();
        this.font.getData().setScale(3);
        this.font.setColor(Color.YELLOW);
        this.layout = new GlyphLayout();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new MainMenu(game)); // Restart game
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        layout.setText(font, "YOU WIN!");
        font.draw(game.batch, layout,
            (Gdx.graphics.getWidth() - layout.width) / 2,
            (Gdx.graphics.getHeight() + layout.height) / 2
        );

        layout.setText(font, "Press SPACE to Play Again");
        font.draw(game.batch, layout,
            (Gdx.graphics.getWidth() - layout.width) / 2,
            (Gdx.graphics.getHeight() - 50)
        );
        game.batch.end();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { font.dispose(); }
}
