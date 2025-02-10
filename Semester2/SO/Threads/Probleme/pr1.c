#include <stdio.h>
#include <unistd.h>
#include <pthread.h>
#include <stdlib.h>
#include <time.h>
int sum;
pthread_mutex_t mtx;
pthread_barrier_t barr;

void* f(void* p){
	int* nrs = (int*)p;
	int s=0;
	for(int i=0;i<100;i++)
		s+=nrs[i];
	pthread_mutex_lock(&mtx);
	sum+=s;
	pthread_mutex_unlock(&mtx);
	free(p);
	pthread_barrier_wait(&barr);
	//printf("Toate am terminat\n");
	return NULL;
}

int main(int argc, char** argv){
	pthread_t t[100];
	pthread_mutex_init(&mtx,NULL);
	pthread_barrier_init(&barr,NULL,100);
	srand(time(NULL));
	for(int i=0;i<100;i++){
		int* nrs=(int*)malloc(100*sizeof(int));
		for(int i=0;i<100;i++)
			nrs[i]=rand()%9+1;
		pthread_create(t+i,NULL,f,nrs);
	}
	for(int i=0;i<100;i++){
		pthread_join(t[i],NULL);
	}
	pthread_mutex_destroy(&mtx);
	pthread_barrier_destroy(&barr);
	printf("Suma este %d\n",sum);
	return 0;
}
