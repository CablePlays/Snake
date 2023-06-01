package me.cable.snake.internal;

import me.cable.snake.Direction;
import org.jetbrains.annotations.NotNull;

public record Coords(int x, int y) {

    public boolean isWithin(int minX, int minY, int maxX, int maxY) {
        return (x >= minX && x <= maxX && y >= minY && y <= maxY);
    }

    public @NotNull Coords add(int x, int y) {
        return new Coords(this.x + x, this.y + y);
    }

    public @NotNull Coords getRelative(@NotNull Direction direction) {
        return switch (direction) {
            case UP -> add(0, 1);
            case DOWN -> add(0, -1);
            case LEFT -> add(-1, 0);
            case RIGHT -> add(1, 0);
        };
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Coords coords && coords.x() == x() && coords.y() == y());
    }
}
