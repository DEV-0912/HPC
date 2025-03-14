import java.util.Random;
import java.util.stream.IntStream;

public class GameOfLife {
    private static final int ROWS = 10000;
    private static final int COLS = 10000;
    private static final int STEPS = 1000000;

    // Initialize the grid with ≤10% alive cells
    public static int[][] initializeGrid() {
        Random rand = new Random();
        int[][] grid = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                grid[i][j] = (rand.nextInt(10) == 0) ? 1 : 0; // 10% alive cells
            }
        }
        return grid;
    }

    // Count live neighbors
    public static int countLiveNeighbors(int[][] grid, int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue; // Skip the cell itself
                int r = row + i, c = col + j;
                if (r >= 0 && r < ROWS && c >= 0 && c < COLS) {
                    count += grid[r][c];
                }
            }
        }
        return count;
    }

    // Simulate one step of the game
    public static int[][] simulateStep(int[][] current) {
        int[][] next = new int[ROWS][COLS];

        // Use parallel streams to compute the next state
        IntStream.range(0, ROWS).parallel().forEach(row -> {
            for (int col = 0; col < COLS; col++) {
                int liveNeighbors = countLiveNeighbors(current, row, col);
                if (current[row][col] == 1) {
                    next[row][col] = (liveNeighbors == 2 || liveNeighbors == 3) ? 1 : 0;
                } else {
                    next[row][col] = (liveNeighbors == 3) ? 1 : 0;
                }
            }
        });

        return next;
    }

    // Measure runtime
    public static double measureRuntime(Runnable task) {
        long startTime = System.nanoTime();
        task.run();
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1e9; // Convert to seconds
    }

    public static void main(String[] args) {
        int[][] grid = initializeGrid();

        // Use a mutable container to hold the grid reference
        final int[][][] gridContainer = new int[1][][];
        gridContainer[0] = grid;

        // Measure runtime for 1 million steps
        double runtime = measureRuntime(() -> {
            for (int step = 0; step < STEPS; step++) {
                gridContainer[0] = simulateStep(gridContainer[0]);
            }
        });

        grid = gridContainer[0]; // Update the grid after all steps are completed

        System.out.printf("Runtime for 1M steps: %.6f seconds%n", runtime);
    }
}