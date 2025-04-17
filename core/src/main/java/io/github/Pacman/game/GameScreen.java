package io.github.Pacman.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import java.util.*;

public class GameScreen implements Screen {
    private final PacmanGame game;
    private final Texture playerTexture, wallTexture, pointTexture, powerupTexture, ghostTexture, vulnerableGhostTexture;
    private final Sprite pacmanRight, pacmanLeft, pacmanUp, pacmanDown;
    private Sprite currentPacman;
    private final BitmapFont font;
    private final GlyphLayout layout;
    private float x, y;
    private float dx = 0, dy = 0;
    private final List<Rectangle> walls;
    private final List<Pellet> pellets;
    private final List<Ghost> ghosts = new ArrayList<>();
    private int score = 0;
    private boolean gameOver = false;
    private boolean playerCaught = false;
    private boolean isPoweredUp = false;
    private float powerupTimer = 0;

    private final int[][] layoutData = {
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,0,0,0,0,0,0,1,0,0,1,0,0,0,1,0,0,0,0,1},
        {1,0,1,1,1,1,0,1,1,0,1,1,1,0,1,1,1,1,0,1},
        {1,0,1,0,0,1,0,0,1,0,0,0,1,0,0,0,0,1,0,1},
        {1,0,1,0,1,1,1,0,1,1,1,0,1,1,1,1,0,1,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1},
        {1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1},
        {1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,1},
        {1,1,1,1,1,0,1,1,1,1,1,1,0,1,1,1,0,1,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1},
        {1,1,0,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    public GameScreen(PacmanGame game, int levelNumber) {
        this.game = game;
        playerTexture = new Texture("pacman1.png");
        wallTexture = new Texture("wall.png");
        pointTexture = new Texture("point.png");
        powerupTexture = new Texture("powerup.png");
        ghostTexture = new Texture("ghost.png");
        vulnerableGhostTexture = new Texture("vulnerable_ghost.png");

        float[] pos1 = getRandomFreeTilePosition();
        float[] pos2 = getRandomFreeTilePosition();
        ghosts.add(new Ghost(ghostTexture, vulnerableGhostTexture, pos1[0], pos1[1]));
        ghosts.add(new Ghost(ghostTexture, vulnerableGhostTexture, pos2[0], pos2[1]));

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);
        layout = new GlyphLayout();

        pacmanRight = new Sprite(playerTexture);
        pacmanLeft = new Sprite(playerTexture); pacmanLeft.flip(true, false);
        pacmanUp = new Sprite(playerTexture); pacmanUp.setRotation(90);
        pacmanDown = new Sprite(playerTexture); pacmanDown.setRotation(-90);

        for (Sprite s : new Sprite[]{pacmanRight, pacmanLeft, pacmanUp, pacmanDown}) {
            s.setSize(25, 25);
            s.setOriginCenter();
        }

        currentPacman = pacmanRight;
        walls = new ArrayList<>();
        pellets = new ArrayList<>();
        createMaze();
    }

    private void createMaze() {
        int wallSize = Gdx.graphics.getWidth() / 20;
        Random rand = new Random();
        List<Pellet> pelletCandidates = new ArrayList<>();

        walls.clear();
        pellets.clear();

        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 20; col++) {
                int xPos = col * wallSize;
                int yPos = Gdx.graphics.getHeight() - (row + 1) * wallSize;
                if (layoutData[row][col] == 1) {
                    walls.add(new Rectangle(xPos, yPos, wallSize, wallSize));
                } else {
                    Pellet pellet = new Pellet(xPos + wallSize / 2f, yPos + wallSize / 2f, 5, 5, Pellet.PelletType.NORMAL);
                    if (x == 0 && y == 0) {
                        x = xPos + wallSize / 2f - pacmanRight.getWidth() / 2f;
                        y = yPos + wallSize / 2f - pacmanRight.getHeight() / 2f;
                    }
                    pelletCandidates.add(pellet);
                }
            }
        }

        Collections.shuffle(pelletCandidates);
        for (int i = 0; i < Math.min(4, pelletCandidates.size()); i++) {
            Pellet p = pelletCandidates.get(i);
            pellets.add(new Pellet(p.getBounds().x, p.getBounds().y, 5, 5, Pellet.PelletType.POWERUP));
        }

        for (int i = 4; i < pelletCandidates.size(); i++) {
            Pellet p = pelletCandidates.get(i);
            pellets.add(p);
        }
    }

    private float[] getRandomFreeTilePosition() {
        int wallSize = Gdx.graphics.getWidth() / 20;
        List<float[]> freeTiles = new ArrayList<>();

        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 20; col++) {
                if (layoutData[row][col] == 0) {
                    float x = col * wallSize + wallSize / 2f - 12.5f;
                    float y = Gdx.graphics.getHeight() - (row + 1) * wallSize + wallSize / 2f - 12.5f;
                    freeTiles.add(new float[]{x, y});
                }
            }
        }

        Collections.shuffle(freeTiles);
        return freeTiles.isEmpty() ? new float[]{100, 100} : freeTiles.get(0);
    }

    private void handleInput() {
        float speed = 150;
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            dx = -speed; dy = 0; currentPacman = pacmanLeft;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            dx = speed; dy = 0; currentPacman = pacmanRight;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            dy = speed; dx = 0; currentPacman = pacmanUp;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            dy = -speed; dx = 0; currentPacman = pacmanDown;
        }
    }

    private boolean isColliding(float newX, float newY) {
        Rectangle pacmanBounds = new Rectangle(newX, newY, currentPacman.getWidth(), currentPacman.getHeight());
        for (Rectangle wall : walls)
            if (pacmanBounds.overlaps(wall)) return true;
        return false;
    }

    private void checkPelletCollision() {
        Rectangle pacmanBounds = new Rectangle(x, y, currentPacman.getWidth(), currentPacman.getHeight());

        Iterator<Pellet> iterator = pellets.iterator();
        while (iterator.hasNext()) {
            Pellet pellet = iterator.next();
            if (pacmanBounds.overlaps(pellet.getBounds())) {
                if (pellet.getType() == Pellet.PelletType.NORMAL) {
                    score += 10;
                } else {
                    score += 50;
                    isPoweredUp = true;
                    powerupTimer = 7f;
                }
                iterator.remove();
            }
        }
    }

    @Override
    public void render(float delta) {


        if (playerCaught) {
            game.setScreen(new GameOverScreen(game));
            return;
        }

        handleInput();
        float newX = x + dx * delta;
        float newY = y + dy * delta;
        if (!isColliding(newX, newY)) {
            x = newX;
            y = newY;
        }

        checkPelletCollision();

        if (pellets.isEmpty()) {
            game.setScreen(new YouWinScreen(game));
            return;
        }

        if (isPoweredUp) {
            powerupTimer -= delta;
            if (powerupTimer <= 0) {
                isPoweredUp = false;
            }
        }

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        for (Rectangle wall : walls)
            game.batch.draw(wallTexture, wall.x, wall.y, wall.width, wall.height);

        for (Pellet pellet : pellets)
            pellet.draw(game.batch, pointTexture, powerupTexture);

        currentPacman.setPosition(x, y);
        currentPacman.draw(game.batch);
        font.draw(game.batch, "Score: " + score, 20, Gdx.graphics.getHeight() - 20);

        Rectangle pacmanBounds = new Rectangle(x, y, currentPacman.getWidth(), currentPacman.getHeight());

        for (Ghost ghost : ghosts) {
            ghost.setVulnerable(isPoweredUp);
            ghost.update(delta, x, y, walls);
            ghost.draw(game.batch);

            if (ghost.getBounds().overlaps(pacmanBounds)) {
                if (isPoweredUp) {
                    ghost.resetPosition();
                    score += 200;
                } else {
                    playerCaught = true;
                }
            }
        }

        game.batch.end();
    }

    @Override public void dispose() {
        playerTexture.dispose();
        wallTexture.dispose();
        pointTexture.dispose();
        powerupTexture.dispose();
        font.dispose();
        ghostTexture.dispose();
        vulnerableGhostTexture.dispose();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
