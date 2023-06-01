package me.cable.snake;

import me.cable.snake.internal.Coords;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class Game {

    private final int sizeX;
    private final int sizeY;

    private final List<Coords> snake = new ArrayList<>();
    private @NotNull Direction snakeDirection;
    private @NotNull Coords apple;

    private boolean initialized;

    public Game(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        // snake
        snake.add(new Coords(0, sizeY - 3));
        snake.add(new Coords(0, sizeY - 2));
        snake.add(new Coords(0, sizeY - 1));
        snakeDirection = Direction.DOWN;

        // apple
        apple = new Coords(sizeX - 1, sizeY - 1);
    }

    public void start() {
        updateSnakeAppearance();
        placeApple(apple.x(), apple.y());
        initialized = true;
    }

    private @Nullable Coords getRandomOpenCell() {
        List<Coords> list = new ArrayList<>();

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                Coords coords = new Coords(x, y);

                if (!snake.contains(coords)) {
                    list.add(coords);
                }
            }
        }

        return (list.isEmpty() ? null : list.get((int) (Math.random() * list.size())));
    }

    private void placeNewApple() {
        Coords coords = getRandomOpenCell();

        if (coords != null) {
            apple = coords;
            placeApple(apple.x(), apple.y());
        }
    }

    public void nextFrame() {
        if (!initialized) {
            throw new IllegalStateException("Game must be initialized by calling start()");
        }

        Coords snakeHead = snake.get(0);
        Coords snakeTail = snake.get(snake.size() - 1);
        Coords nextCell = snakeHead.getRelative(snakeDirection);

        /* Check Out Of Bounds */

        if (!nextCell.isWithin(0, 0, sizeX - 1, sizeY - 1)) {
            onLose(LoseReason.WALL_CRASH);
            return;
        }

        /* Check Is Blocked */

        boolean blocked = false;

        for (Coords coords : snake) {
            if (nextCell.equals(coords)) {
                if (!coords.equals(snakeTail)) {
                    blocked = true;
                }

                break;
            }
        }

        if (blocked) {
            onLose(LoseReason.SELF_CRASH);
            return;
        }

        snake.add(0, nextCell);

        if (nextCell.equals(apple)) {
            placeNewApple();
        } else {
            Coords tail = snake.remove(snake.size() - 1); // remove last
            reset(tail.x(), tail.y());
        }

        updateSnakeAppearance();

        if (getRandomOpenCell() == null) {
            onWin();
        }
    }

    private void updateSnakeAppearance() {
        for (int i = 0; i < snake.size(); i++) {
            Coords coords = snake.get(i);
            placeSnake(coords.x(), coords.y(), i);
        }
    }

    public void changeDirection(@NotNull Direction direction) {
        if (snakeDirection.getOpposite() != direction) {
            snakeDirection = direction;
        }
    }

    protected abstract void placeSnake(int x, int y, int index);

    protected abstract void placeApple(int x, int y);

    protected abstract void reset(int x, int y);

    protected abstract void onWin();

    protected abstract void onLose(@NotNull LoseReason loseReason);
}
