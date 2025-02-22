#include <omp.h>
#include <stdio.h>

int main() {
    omp_set_num_threads(3);

    #pragma omp parallel
    {
        printf("Welcome to HPC-FDP \n");
    }

    return 0;
}
