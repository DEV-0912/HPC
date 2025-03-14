#include<omp.h>
#include<stdio.h>
/*int main()
{
	omp_set_num_threads(4);
	#pragma omp parallel
	{
	printf("Welcome to HPC-FDP \n");
	}
	return 0;
}*/
/*int main(){
		int before=omp_get_max_threads();
		omp_set_num_threads(4);
		int after=omp_get_max_threads();
		printf(" Threads in the shared memory environment: %d --> %d \n ", before, after);
	        #pragma omp parallel
	        {
		printf("Welcome to HPC-FDP, Hosted by ABC %d\n", omp_get_thread_num());
		}
		return 0;
}*/
int main(){
	omp_set_num_threads(4);	
	#pragma omp parallel
	{
			#pragma omp master 
			{
			      	printf("www.openmp.org %d\n", omp_get_thread_num()); 
			}
			printf("Welcome to HPC %d\n", omp_get_thread_num());
			#pragma omp barrier
			printf("Are you able to understand the concepts! %d\n", omp_get_thread_num());
	}
return 0;
}
