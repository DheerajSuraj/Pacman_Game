package io.github.Pacman.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameScreen implements Screen {
    private final PacmanGame game;
    private Texture playerTexture;
    private TextureRegion player;
    private float x, y;
    private boolean facingright = false;
    private boolean facingtop = false;

    public GameScreen(PacmanGame game) {
        this.game = game;
        playerTexture = new Texture("pacman1.png");
        player = new TextureRegion(playerTexture);

        x = Gdx.graphics.getWidth() / 2f - 100;
        y = Gdx.graphics.getHeight() / 2f - 100;
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        handleInput(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(player, x, y, 100, 100);
        game.batch.end();
    }

    private void handleInput(float delta) {
        float moveSpeed = 200 * delta;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            x -= moveSpeed;
            if(!facingright){
                player.flip(true,false);
                facingright  = true;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            x += moveSpeed;
            if(facingright){
                player.flip(true, false);
                facingright = false;
            }
        };
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            y += moveSpeed;
            if(!facingtop){
                player.flip(false,true);
                facingtop = true;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            y -= moveSpeed;
            if(facingtop){
                player.flip(false,true);
                facingtop = false;
            }
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        playerTexture.dispose();
    }
}
