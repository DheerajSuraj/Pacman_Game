package io.github.Pacman.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

public class Ghost {
    private final Sprite sprite;
    private final Texture normalTexture;
    private final Texture vulnerableTexture;

    private final float tileSize = 25f;
    private final float speed = 100f;

    private final float startX, startY;

    private boolean isVulnerable = false;

    private Vector2 direction = new Vector2(0, 0);
    private List<Vector2> path = new ArrayList<>();

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
        Vector2 ghostTile = getTile(sprite.getX(), sprite.getY());
        Vector2 pacmanTile = getTile(pacmanX, pacmanY);

        // Only recalculate if path is empty or Pac-Man moved
        if (path.isEmpty() || !path.get(path.size() - 1).epsilonEquals(pacmanTile, 0.1f)) {
            path = findPath(ghostTile, pacmanTile, walls);
        }

        if (!path.isEmpty()) {
            Vector2 targetTile = path.get(0);
            Vector2 targetPos = new Vector2(targetTile).scl(tileSize);
            Vector2 currentPos = new Vector2(sprite.getX(), sprite.getY());

            Vector2 movement = targetPos.cpy().sub(currentPos).nor().scl(speed * delta);

            if (movement.len() >= targetPos.dst(currentPos)) {
                sprite.setPosition(targetPos.x, targetPos.y);
                path.remove(0); // Move to next step
            } else {
                sprite.translate(movement.x, movement.y);
            }
        }
    }

    private Vector2 getTile(float x, float y) {
        return new Vector2((int) (x / tileSize), (int) (y / tileSize));
    }

    private boolean isBlocked(Vector2 tile, List<Rectangle> walls) {
        float x = tile.x * tileSize;
        float y = tile.y * tileSize;
        Rectangle bounds = new Rectangle(x, y, tileSize, tileSize);
        for (Rectangle wall : walls) {
            if (bounds.overlaps(wall)) return true;
        }
        return false;
    }

    private List<Vector2> findPath(Vector2 start, Vector2 goal, List<Rectangle> walls) {
        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingDouble(n -> n.f));
        Map<Vector2, Vector2> cameFrom = new HashMap<>();
        Map<Vector2, Float> gScore = new HashMap<>();

        open.add(new Node(start, 0, heuristic(start, goal)));
        gScore.put(start, 0f);

        while (!open.isEmpty()) {
            Node current = open.poll();
            if (current.pos.epsilonEquals(goal, 0.1f)) {
                return reconstructPath(cameFrom, current.pos);
            }

            for (Vector2 dir : getNeighbors()) {
                Vector2 neighbor = current.pos.cpy().add(dir);
                if (isBlocked(neighbor, walls)) continue;

                float tentativeG = gScore.getOrDefault(current.pos, Float.MAX_VALUE) + 1;
                if (tentativeG < gScore.getOrDefault(neighbor, Float.MAX_VALUE)) {
                    cameFrom.put(neighbor, current.pos);
                    gScore.put(neighbor, tentativeG);
                    float f = tentativeG + heuristic(neighbor, goal);
                    open.add(new Node(neighbor, tentativeG, f));
                }
            }
        }

        return new ArrayList<>(); // No path
    }

    private float heuristic(Vector2 a, Vector2 b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y); // Manhattan distance
    }

    private List<Vector2> reconstructPath(Map<Vector2, Vector2> cameFrom, Vector2 current) {
        List<Vector2> totalPath = new LinkedList<>();
        while (cameFrom.containsKey(current)) {
            totalPath.add(0, current);
            current = cameFrom.get(current);
        }
        return totalPath;
    }

    private List<Vector2> getNeighbors() {
        return Arrays.asList(
            new Vector2(1, 0), new Vector2(-1, 0),
            new Vector2(0, 1), new Vector2(0, -1)
        );
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }

    public void resetPosition() {
        sprite.setPosition(startX, startY);
        path.clear();
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

    private static class Node {
        Vector2 pos;
        float g, f;

        Node(Vector2 pos, float g, float f) {
            this.pos = pos;
            this.g = g;
            this.f = f;
        }
    }
}
