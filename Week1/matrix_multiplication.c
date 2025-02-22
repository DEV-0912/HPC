#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>

int main(int argc, char *argv[]) {
    int i, j, k;
    struct timeval tv1, tv2;
    struct timezone tz;
    double elapsed;

    if (argc < 2) {
        printf("Usage: %s <Matrix Size>\n", argv[0]);
        return 1;
    }

    int N = atoi(argv[1]);
    double **A, **B, **C;

    // Allocating memory for matrices
    A = (double **)malloc(N * sizeof(double *));
    B = (double **)malloc(N * sizeof(double *));
    C = (double **)malloc(N * sizeof(double *));
    
    for (i = 0; i < N; i++) {
        A[i] = (double *)malloc(N * sizeof(double));
        B[i] = (double *)malloc(N * sizeof(double));
        C[i] = (double *)malloc(N * sizeof(double));
    }

    // Initializing matrices
    for (i = 0; i < N; i++)
        for (j = 0; j < N; j++) {
            A[i][j] = 2.0;
            B[i][j] = 2.0;
            C[i][j] = 0.0;
        }

    gettimeofday(&tv1, &tz); // Start time

    // Standard matrix multiplication
    for (i = 0; i < N; i++) {
        for (j = 0; j < N; j++) {
            for (k = 0; k < N; k++) {
                C[i][j] += A[i][k] * B[k][j];
            }
        }
    }

    gettimeofday(&tv2, &tz); // End time

    // Calculating elapsed time
    elapsed = (double)(tv2.tv_sec - tv1.tv_sec) +
              (double)(tv2.tv_usec - tv1.tv_usec) / 1000000.0;

    printf("Time Elapsed: %f seconds\n", elapsed);

    // Free memory
    for (i = 0; i < N; i++) {
        free(A[i]);
        free(B[i]);
        free(C[i]);
    }
    free(A);
    free(B);
    free(C);

    return 0;
}
