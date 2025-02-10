#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#define NRT 10
#define MAX 100

int flag;
pthread_cond_t cv;
pthread_mutex_t mtx;
int sum;
int buff[MAX];

int empty(){
	if(flag == 0)
		return 1;
	return 0;
}
int full(){
	if(flag == 1)
		return 1;
	return 0;
}

void* consuma(void* p){
	int i = *(int*)p;
	free(p);
	pthread_mutex_lock(&mtx);
		while(empty()){
			printf("[Consumator %d]\tBufferul e gol...\n",i);
			pthread_cond_wait(&cv,&mtx);
		}
		printf("[Consumator %d]\tBufferul e plin!\n",i);
		for(int i=0;i<MAX;i++)
			sum+=buff[i];
		flag = 0;
		printf("[Consumator %d]\tAm terminat de consumat, bufferul e gol!\n",i);
		pthread_cond_broadcast(&cv);
	pthread_mutex_unlock(&mtx);
	return NULL;
}

void* produce(void* p){
	int i=*(int*)p;
	free(p);
	pthread_mutex_lock(&mtx);
		while(full()){
			printf("[Producator %d]\tBufferul e plin...\n",i);
			pthread_cond_wait(&cv,&mtx);
		}
		printf("[Producator %d]\tBufferul e gol!\n",i);
		for(int i=0;i<MAX;i++)
			buff[i]=1;
		flag=1;
		printf("[Producator %d]\tBufferul e plin,nb...\n",i);
		pthread_cond_broadcast(&cv);
	pthread_mutex_unlock(&mtx);
	return NULL;
}


int main(int argc, char* argv[]){
	pthread_t tp[NRT];
	pthread_t tc[NRT];
	pthread_cond_init(&cv,NULL);
	pthread_mutex_init(&mtx,NULL);
	for(int i=0;i<NRT;i++){
		int* nr1 = (int*)malloc(sizeof(int));
		*nr1 = i;
		int* nr2 = (int*)malloc(sizeof(int));
                *nr2 = i;
		pthread_create(tp+i,NULL,produce,nr1);
		pthread_create(tc+i,NULL,consuma,nr2);
	}
	for(int i=0;i<NRT;i++){
		pthread_join(tp[i],NULL);
		pthread_join(tc[i],NULL);
	}
	pthread_cond_destroy(&cv);
	pthread_mutex_destroy(&mtx);
	printf("Suma este: %d\n",sum);
	return 0;
}
