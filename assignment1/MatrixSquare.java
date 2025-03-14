import java.util.Random;

public class MatrixSquare {
    private static final int MAX_SIZE = 4096;

    // Fill matrix with random values between 0 and 1
    public static double[][] fillMatrix(int size) {
        Random rand = new Random();
        double[][] matrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rand.nextDouble(); // Random value between 0 and 1
            }
        }
        return matrix;
    }

    // Compute the square of a matrix
    public static double[][] matrixSquare(double[][] A, int size, int numThreads) {
        double[][] C = new double[size][size];

        // Parallelize computation using threads
        Thread[] threads = new Thread[numThreads];
        int rowsPerThread = size / numThreads;

        for (int t = 0; t < numThreads; t++) {
            final int threadId = t;
            threads[t] = new Thread(() -> {
                int startRow = threadId * rowsPerThread;
                int endRow = (threadId == numThreads - 1) ? size : startRow + rowsPerThread;

                for (int i = startRow; i < endRow; i++) {
                    for (int j = 0; j < size; j++) {
                        C[i][j] = 0.0;
                        for (int k = 0; k < size; k++) {
                            C[i][j] += A[i][k] * A[k][j];
                        }
                    }
                }
            });
            threads[t].start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return C;
    }

    // Measure runtime
    public static double measureRuntime(Runnable task) {
        long startTime = System.nanoTime();
        task.run();
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1e9; // Convert to seconds
    }

    public static void main(String[] args) {
        int[] sizes = {256, 512, 1024, 2048, 4096};
        int[] threads = {2, 4, 6, 8, 10, 12, 14, 16};

        System.out.println("Matrix Size, Threads, Average Runtime (s)");
        for (int size : sizes) {
            double[][] A = fillMatrix(size);

            for (int numThreads : threads) {
                double totalTime = 0.0;

                for (int run = 0; run < 5; run++) {
                    totalTime += measureRuntime(() -> matrixSquare(A, size, numThreads));
                }

                double avgTime = totalTime / 5.0;
                System.out.printf("%d, %d, %.6f%n", size, numThreads, avgTime);
            }
        }
    }
}