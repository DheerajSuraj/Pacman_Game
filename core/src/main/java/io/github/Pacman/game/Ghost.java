package io.github.Pacman.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

public class Ghost {
    private final Sprite sprite;
    private final Texture normalTexture;
    private final Texture vulnerableTexture;

    private final float speed = 100f;
    private final float tileSize = 25;
    private final float startX, startY;

    private int dirX = 1, dirY = 0;
    private boolean isVulnerable = false;

    public Ghost(Texture normalTexture, Texture vulnerableTexture, float x, float y) {
        this.normalTexture = normalTexture;
        this.vulnerableTexture = vulnerableTexture;

        this.sprite = new Sprite(normalTexture);
        this.sprite.setSize(tileSize, tileSize);
        this.sprite.setOriginCenter();
        this.sprite.setPosition(x, y);

        this.startX = x;
        this.startY = y;
    }

    public void update(float delta, float pacmanX, float pacmanY, List<Rectangle> walls) {
        float x = sprite.getX();
        float y = sprite.getY();

        float newX = x + dirX * speed * delta;
        float newY = y + dirY * speed * delta;

        if (!isColliding(newX, newY, walls)) {
            sprite.setPosition(newX, newY);
        } else {
            pickNewDirection(pacmanX, pacmanY, walls);
        }

        if (isAlignedToTile()) {
            pickNewDirection(pacmanX, pacmanY, walls);
        }
    }

    private boolean isAlignedToTile() {
        float x = sprite.getX();
        float y = sprite.getY();
        return ((int) x % tileSize == 0) && ((int) y % tileSize == 0);
    }

    private void pickNewDirection(float pacmanX, float pacmanY, List<Rectangle> walls) {
        int[][] directions = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };

        float x = sprite.getX();
        float y = sprite.getY();

        float minDist = Float.MAX_VALUE;
        int bestDX = 0;
        int bestDY = 0;
        boolean foundAlternative = false;

        for (int[] dir : directions) {
            float testX = x + dir[0] * tileSize;
            float testY = y + dir[1] * tileSize;

            if (!isColliding(testX, testY, walls)) {
                // Skip reversing unless it's the only option
                if (dir[0] == -dirX && dir[1] == -dirY) {
                    continue;
                }

                float dist = (pacmanX - testX) * (pacmanX - testX) + (pacmanY - testY) * (pacmanY - testY);
                if (dist < minDist) {
                    minDist = dist;
                    bestDX = dir[0];
                    bestDY = dir[1];
                    foundAlternative = true;
                }
            }
        }

        // If stuck, allow reversing direction
        if (!foundAlternative) {
            int reverseDX = -dirX;
            int reverseDY = -dirY;
            float reverseX = x + reverseDX * tileSize;
            float reverseY = y + reverseDY * tileSize;

            if (!isColliding(reverseX, reverseY, walls)) {
                bestDX = reverseDX;
                bestDY = reverseDY;
            }
        }

        dirX = bestDX;
        dirY = bestDY;
    }

    private boolean isColliding(float newX, float newY, List<Rectangle> walls) {
        Rectangle ghostBounds = new Rectangle(newX, newY, sprite.getWidth(), sprite.getHeight());
        for (Rectangle wall : walls) {
            if (ghostBounds.overlaps(wall)) {
                return true;
            }
        }
        return false;
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }

    public void resetPosition() {
        sprite.setPosition(startX, startY);
    }

    public void setVulnerable(boolean vulnerable) {
        if (this.isVulnerable != vulnerable) {
            this.isVulnerable = vulnerable;
            sprite.setTexture(vulnerable ? vulnerableTexture : normalTexture);
        }
    }

    public boolean isVulnerable() {
        return isVulnerable;
    }
}
