#include <stdio.h>
#include <unistd.h>
#include <pthread.h>
#include <stdlib.h>
#include <time.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#define MAX 1000
#define NRT 10

int vals[MAX];
int minim=10000;
pthread_mutex_t mtx;
pthread_barrier_t barr;
void* f(void* p){
	int tid = *(int*)p;
	free(p);
	int min_loc=10000;
	for(int i=tid*100;i<(tid+1)*100;i++)
		if(vals[i]<min_loc)
			min_loc=vals[i];
	printf("[Thread %d]\tMinimul pe intervalul meu este %d\n",tid,min_loc);
	pthread_barrier_wait(&barr);
	pthread_mutex_lock(&mtx);
		if(min_loc<minim)
			minim=min_loc;
	pthread_mutex_unlock(&mtx);
	return NULL;
}

int main(int argc, char* argv[]){
	pthread_t t[NRT];
	int fd = open("random-file.bin",O_RDONLY);
	if(fd == -1){
		perror("open(): ");
		exit(0);
	}
	for(int i=1;i<MAX;i++){
		read(fd,&vals[i],2);
		printf("%d ",vals[i]);
	}
	printf("\n");

	pthread_mutex_init(&mtx,NULL);
	pthread_barrier_init(&barr,NULL,NRT);
	for(int i=0;i<NRT;i++){
		int* nr = (int*)malloc(sizeof(int));
		*nr = i;
		pthread_create(t+i,NULL,f,nr);
	}
	for(int i=0;i<NRT;i++)
		pthread_join(t[i],NULL);
	pthread_mutex_destroy(&mtx);
	pthread_barrier_destroy(&barr);
	printf("Minimul este %d\n",minim);
	return 0;
}
