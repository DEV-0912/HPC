#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>

#define N 1000000  // 1 Million elements

int main() {
    struct timeval tv1, tv2;
    struct timezone tz;
    double elapsed;

    int *arr = (int *)malloc(N * sizeof(int));
    long long sum = 0;

    // Initializing array
    for (int i = 0; i < N; i++) {
        arr[i] = i + 1;
    }

    gettimeofday(&tv1, &tz); // Start time

    // **Spatial Locality: Accessing consecutive elements**
    for (int i = 0; i < N; i++) {
        sum += arr[i];
    }

    gettimeofday(&tv2, &tz); // End time

    elapsed = (double)(tv2.tv_sec - tv1.tv_sec) +
              (double)(tv2.tv_usec - tv1.tv_usec) / 1000000.0;

    printf("Spatial Locality Sum: %lld\n", sum);
    printf("Time Elapsed: %f seconds\n", elapsed);

    free(arr);
    return 0;
}
