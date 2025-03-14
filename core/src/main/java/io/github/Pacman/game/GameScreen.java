package io.github.Pacman.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class GameScreen implements Screen {
    private final PacmanGame game;
    private final Texture playerTexture, wallTexture, pointTexture;
    private final Sprite pacmanRight;
    private final Sprite pacmanLeft;
    private final Sprite pacmanUp;
    private final Sprite pacmanDown;
    private Sprite currentPacman;
    private final BitmapFont font;
    private final GlyphLayout layout; // Used for text alignment
    private float x, y;
    private float dx = 0, dy = 0; // Movement direction
    private final List<Rectangle> walls;
    private final List<Rectangle> points;
    private int score = 0;
    private int[][] mazeLayout; // Store original maze layout for pellet respawn

    public GameScreen(PacmanGame game) {
        this.game = game;
        playerTexture = new Texture("pacman1.png");
        wallTexture = new Texture("wall.png");
        pointTexture = new Texture("point.png"); // Ensure this file exists
        font = new BitmapFont(); // Default font
        font.getData().setScale(2); // Make text larger
        layout = new GlyphLayout();

        float pacmanSize = 25;

        pacmanRight = new Sprite(playerTexture);
        pacmanRight.setSize(pacmanSize, pacmanSize);
        pacmanRight.setOriginCenter();

        pacmanLeft = new Sprite(playerTexture);
        pacmanLeft.setSize(pacmanSize, pacmanSize);
        pacmanLeft.setOriginCenter();
        pacmanLeft.flip(true, false);

        pacmanUp = new Sprite(playerTexture);
        pacmanUp.setSize(pacmanSize, pacmanSize);
        pacmanUp.setOriginCenter();
        pacmanUp.setRotation(90);

        pacmanDown = new Sprite(playerTexture);
        pacmanDown.setSize(pacmanSize, pacmanSize);
        pacmanDown.setOriginCenter();
        pacmanDown.setRotation(-90);

        currentPacman = pacmanRight;

        walls = new ArrayList<>();
        points = new ArrayList<>();
        createMaze();
    }

    private void createMaze() {
        int cols = 20;
        int rows = 15;
        int wallSize = Gdx.graphics.getWidth() / cols;

        int[][] mazeLayout = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1},
            {1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        walls.clear();
        points.clear();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int xPos = col * wallSize;
                int yPos = Gdx.graphics.getHeight() - (row + 1) * wallSize;

                if (mazeLayout[row][col] == 1) {
                    walls.add(new Rectangle(xPos, yPos, wallSize, wallSize));
                } else {
                    points.add(new Rectangle(xPos + (float) wallSize / 2, yPos + (float) wallSize / 2, 5, 5));

                    // Set Pac-Man's spawn position at the first open space found
                    if (x == 0 && y == 0) {
                        x = xPos + (float) wallSize / 2 - pacmanRight.getWidth() / 2;
                        y = yPos + (float) wallSize / 2 - pacmanRight.getHeight() / 2;
                    }
                }
            }
        }
    }


    @Override
    public void render(float delta) {
        handleInput();
        float newX = x + dx * delta;
        float newY = y + dy * delta;

        if (!isColliding(newX, newY)) {
            x = newX;
            y = newY;
        }

        checkPointCollision();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        // Draw Walls
        for (Rectangle wall : walls) {
            game.batch.draw(wallTexture, wall.x, wall.y, wall.width, wall.height);
        }

        // Draw Points
        for (Rectangle point : points) {
            game.batch.draw(pointTexture, point.x, point.y, 5, 5);
        }

        // Draw Pacman
        currentPacman.setPosition(x, y);
        currentPacman.draw(game.batch);

        // Display Score
        String scoreText = "Score: " + score;
        layout.setText(font, scoreText);
        font.draw(game.batch, scoreText, 20, Gdx.graphics.getHeight() - 20); // Position: Top-left

        game.batch.end();
    }


    private void handleInput() {
        // Speed in pixels per second
        float moveSpeed = 150;
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            dx = -moveSpeed;
            dy = 0;
            currentPacman = pacmanLeft;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            dx = moveSpeed;
            dy = 0;
            currentPacman = pacmanRight;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            dy = moveSpeed;
            dx = 0;
            currentPacman = pacmanUp;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            dy = -moveSpeed;
            dx = 0;
            currentPacman = pacmanDown;
        }
    }

    private void checkPointCollision() {
        Rectangle pacmanBounds = new Rectangle(x, y, currentPacman.getWidth(), currentPacman.getHeight());

        points.removeIf(point -> {
            if (pacmanBounds.overlaps(point)) {
                score += 10; // Increase score by 10 per pellet
                return true; // Remove point
            }
            return false;
        });
    }

    private boolean isColliding(float newX, float newY) {
        Rectangle pacmanBounds = new Rectangle(newX, newY, currentPacman.getWidth(), currentPacman.getHeight());

        for (Rectangle wall : walls) {
            if (pacmanBounds.overlaps(wall)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void dispose() {
        playerTexture.dispose();
        wallTexture.dispose();
        font.dispose();
        wallTexture.dispose();
    }

    @Override
    public void show() {}
    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}

}
