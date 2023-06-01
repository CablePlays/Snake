package me.cable.snake.test;

import me.cable.snake.Direction;
import me.cable.snake.Game;
import me.cable.snake.LoseReason;
import org.jetbrains.annotations.NotNull;

import java.util.Scanner;

public class TestGame extends Game {

    private static final int SIZE_X = 4;
    private static final int SIZE_Y = 4;

    boolean running = true;

    private final int[][] grid = new int[SIZE_Y][SIZE_X]; // 0: none; 1: snake; 2: apple

    public TestGame() {
        super(SIZE_X, SIZE_Y);

        start();
        displayGame();

//        new Thread(() -> {
//            while (running) {
//                try {
//                    Thread.sleep(1500);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//
//                nextFrame();
//
//                if (running) {
//                    displayGame();
//                }
//            }
//        }).start();

        Scanner scanner = new Scanner(System.in);

        while (running) {
            String dir = scanner.next();

            switch (dir) {
                case "w" -> changeDirection(Direction.UP);
                case "s" -> changeDirection(Direction.DOWN);
                case "a" -> changeDirection(Direction.LEFT);
                case "d" -> changeDirection(Direction.RIGHT);
            }

            nextFrame();
            displayGame();
        }
    }

    private void displayGame() {
        System.out.println();

        for (int y = SIZE_Y - 1; y >= 0; y--) {
            StringBuilder row = new StringBuilder();

            for (int x = 0; x < SIZE_X; x++) {
                int n = grid[y][x];

                if (!row.isEmpty()) {
                    row.append(' ');
                }

                row.append(n);
            }

            System.out.println(row);
        }
    }

    @Override
    protected void placeSnake(int x, int y, int index) {
        grid[y][x] = 1;
    }

    @Override
    protected void placeApple(int x, int y) {
        grid[y][x] = 2;
    }

    @Override
    protected void reset(int x, int y) {
        grid[y][x] = 0;
    }

    @Override
    protected void onWin() {
        running = false;
        System.out.println("Won game!");
    }

    @Override
    protected void onLose(@NotNull LoseReason loseReason) {
        running = false;

        switch (loseReason) {
            case SELF_CRASH -> System.out.println("You crashed into yourself!");
            case WALL_CRASH -> System.out.println("You crashed into a wall!");
        }
    }
}
