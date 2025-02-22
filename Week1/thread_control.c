#include <omp.h>
#include <stdio.h>

int main() {
    int before = omp_get_max_threads();
    omp_set_num_threads(8);
    int after = omp_get_max_threads();

    printf("Thread in the shared memory location: %d --> %d \n", before, after);

    #pragma omp parallel
    {
        printf("Welcome to hcpp %d\n", omp_get_thread_num());
    }

    return 0;
}
