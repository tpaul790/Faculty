#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>

int vals[5];
pthread_mutex_t mtx;
pthread_barrier_t barr;

void afis(){
	pthread_mutex_lock(&mtx);
	for(int i=0;i<5;i++)
		printf("%d ",vals[i]);
	printf("\n");
	pthread_mutex_unlock(&mtx);
}

void* do_work(void* p){
	int i = *(int*)p;
	free(p);
	int n;
	pthread_mutex_lock(&mtx);
	printf("Thread %d: ",i);
	scanf("%d",&n);
	vals[i]=n;
	pthread_mutex_unlock(&mtx);
	pthread_barrier_wait(&barr);
	afis();
	return NULL;
}

int main(int argc, char* argv[]){
	if(argc!=2){
		perror("Numar incorect de argumente!\n");
		exit(0);
	}
	int n = 5;
	pthread_mutex_init(&mtx,NULL);
	pthread_barrier_init(&barr,NULL,5);
	pthread_t tid[n];
	for(int i=0;i<n;i++){
		int* nr = (int*)malloc(sizeof(int));
		*nr = i;
		pthread_create(tid+i,NULL,do_work,nr);
	}
	for(int i=0;i<n;i++)
		pthread_join(tid[i],NULL);
	pthread_mutex_destroy(&mtx);
	pthread_barrier_destroy(&barr);
	return 0;
}
