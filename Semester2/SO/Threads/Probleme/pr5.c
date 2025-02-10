#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <time.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#define NRT 10
#define MAX 20000

int vals[MAX];
int sum;
int medie;
pthread_mutex_t mtx;
pthread_barrier_t barr;

void* f(void* p){
	int tid = *(int*)p;
	int min = MAX;
	for(int i=tid*2000;i<(tid+1)*2000;i++)
		if(vals[i]<min)
			min=vals[i];
	pthread_mutex_lock(&mtx);
	sum+=min;
	pthread_mutex_unlock(&mtx);

	pthread_barrier_wait(&barr);

	pthread_mutex_lock(&mtx);
	medie = sum/NRT;
	pthread_mutex_unlock(&mtx);

	printf("[Thread %d]\tMinim=%d, Media=%d, Diferenta=%d\n",tid,min,medie,abs(min-medie));
	return NULL;
}

int main(int argc, char* argv[]){
	pthread_t t[NRT];
	int fd = open("random-file.bin",O_RDONLY);
	if(fd == -1){
		perror("open(): ");
		exit(0);
	}
	for(int i=0;i<MAX;i++){
		read(fd,&vals[i],1);
		//printf("%d ",vals[i]);
	}
	printf("\n");
	pthread_mutex_init(&mtx,NULL);
	pthread_barrier_init(&barr,NULL,NRT);
	for(int i=0;i<NRT;i++){
		int* nr = (int*)malloc(sizeof(int));
		*nr = i;
		pthread_create(&t[i],NULL,f,nr);
	}
	for(int i=0;i<NRT;i++)
		pthread_join(t[i],NULL);
	pthread_mutex_destroy(&mtx);
	pthread_barrier_destroy(&barr);
	return 0;
}
