#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <time.h>

#define MAX_SIZE 4096
#define NUM_RUNS 5
#define NUM_THREADS 2

typedef struct {
    int start_row, end_row, size;
    double **A, **C;
} ThreadData;

void* matrix_square_thread(void* arg) {
    ThreadData* data = (ThreadData*)arg;
    double **A = data->A, **C = data->C;
    int size = data->size, start_row = data->start_row, end_row = data->end_row;

    for (int i = start_row; i < end_row; i++) {
        for (int j = 0; j < size; j++) {
            C[i][j] = 0.0;
            for (int k = 0; k < size; k++) {
                C[i][j] += A[i][k] * A[k][j];
            }
        }
    }
    return NULL;
}

// Allocate a matrix dynamically
double** allocate_matrix(int size) {
    double** mat = (double**)malloc(size * sizeof(double*));
    for (int i = 0; i < size; i++)
        mat[i] = (double*)malloc(size * sizeof(double));
    return mat;
}

// Fill matrix with random values between 0 and 1
void fill_matrix(double** A, int size) {
    srand(42); // Fixed seed for reproducibility
    for (int i = 0; i < size; i++)
        for (int j = 0; j < size; j++)
            A[i][j] = (double)rand() / RAND_MAX;
}

// Compute the square of a matrix using multiple threads
double** matrix_square(double** A, int size) {
    double** C = allocate_matrix(size);
    pthread_t threads[NUM_THREADS];
    ThreadData thread_data[NUM_THREADS];

    int rows_per_thread = size / NUM_THREADS;
    
    for (int t = 0; t < NUM_THREADS; t++) {
        thread_data[t].start_row = t * rows_per_thread;
        thread_data[t].end_row = (t == NUM_THREADS - 1) ? size : (t + 1) * rows_per_thread;
        thread_data[t].A = A;
        thread_data[t].C = C;
        thread_data[t].size = size;

        pthread_create(&threads[t], NULL, matrix_square_thread, &thread_data[t]);
    }

    // Wait for all threads to finish
    for (int t = 0; t < NUM_THREADS; t++)
        pthread_join(threads[t], NULL);

    return C;
}

// Measure runtime of a function
double measure_runtime(double** A, int size) {
    struct timespec start, end;
    clock_gettime(CLOCK_MONOTONIC, &start);
    
    double** C = matrix_square(A, size);
    
    clock_gettime(CLOCK_MONOTONIC, &end);
    double elapsed = (end.tv_sec - start.tv_sec) + (end.tv_nsec - start.tv_nsec) / 1e9;
    
    for (int i = 0; i < size; i++) free(C[i]);
    free(C);

    return elapsed;
}

int main() {
    int sizes[] = {256, 512, 1024, 2048, 4096};

    printf("Matrix Size, Threads, Average Runtime (s)\n");

    for (int s = 0; s < 5; s++) {
        int size = sizes[s];
        double** A = allocate_matrix(size);
        fill_matrix(A, size);

        double total_time = 0.0;
        for (int run = 0; run < NUM_RUNS; run++) {
            total_time += measure_runtime(A, size);
        }

        double avg_time = total_time / NUM_RUNS;
        printf("%d, %d, %.6f\n", size, NUM_THREADS, avg_time);

        for (int i = 0; i < size; i++) free(A[i]);
        free(A);
    }

    return 0;
}
