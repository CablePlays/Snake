package me.cable.snake;

import org.jetbrains.annotations.NotNull;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public @NotNull Direction getOpposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
}
