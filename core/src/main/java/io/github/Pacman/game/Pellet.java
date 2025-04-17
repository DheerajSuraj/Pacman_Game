package io.github.Pacman.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Pellet {
    public enum PelletType { NORMAL, POWERUP }

    private Rectangle bounds;
    private PelletType type;

    public Pellet(float x, float y, float width, float height, PelletType type) {
        this.bounds = new Rectangle(x, y, width, height);
        this.type = type;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public PelletType getType() {
        return type;
    }

    public void draw(SpriteBatch batch, Texture normalTexture, Texture powerupTexture) {
        if (type == PelletType.NORMAL) {
            batch.draw(normalTexture, bounds.x, bounds.y, bounds.width, bounds.height);
        } else {
            batch.draw(powerupTexture, bounds.x - 3, bounds.y - 3, bounds.width + 7, bounds.height + 7);
        }
    }
}
