#include <stdio.h>
#include <unistd.h>
#include <pthread.h>
#include <stdlib.h>
#include <semaphore.h>

int val;
pthread_mutex_t m;
sem_t sem;

void* f(void* p){
	int i = *(int*)p;
	sem_wait(&sem);
	val+=1;
	sem_post(&sem);
	printf("Tread ul:%d\tValoarea: %d\n",i,val);
	free(p);
	return NULL;
}

int main(int argc, char* argv[]){
	if(argc != 2){
		perror("Argumente insuficiente\n");
		exit(0);
	}
	int n = atoi(argv[1]);
	pthread_t t[n];
	pthread_mutex_init(&m,NULL);
	sem_init(&sem,0,1);
	for(int i=0;i<n;i++){
		int* nr = (int*)malloc(sizeof(int));
		*nr = i;
		pthread_create(t+i,NULL,f,nr);
	}
	for(int i=0;i<n;i++)
		pthread_join(t[i],NULL);
	pthread_mutex_destroy(&m);
	sem_destroy(&sem);
	return 0;
}
