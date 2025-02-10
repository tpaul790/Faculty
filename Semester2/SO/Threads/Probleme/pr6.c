#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#define NRT 10
#define SIZE 1000

pthread_cond_t p2c;
pthread_cond_t c2p;
pthread_mutex_t mtx;
int flag;
int sum;
int fd;
int vals[SIZE];

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

void* consum(void* p){
	int tid = *(int*)p;
	free(p);
	pthread_mutex_lock(&mtx);
		while(empty()){
			printf("[Consumator %d]\tBufferul e gol, ceaw!\n",tid);
			pthread_cond_wait(&p2c,&mtx);
		}
		printf("[Consumator %d]\tBufferul e plin, ma bag!\n",tid);
		for(int i=0;i<SIZE;i++)
			sum+=vals[i];
		flag = 0;
		printf("[Consumator %d]\t Am golit bufferul,bafta!\n",tid);
		pthread_cond_signal(&c2p);
	pthread_mutex_unlock(&mtx);
	return NULL;
}

void* produc(void* p){
        int tid = *(int*)p;
        free(p);
	pthread_mutex_lock(&mtx);
		while(full()){
			printf("[Producator %d]\tBufferul e plin, ceaw!\n",tid);
			pthread_cond_wait(&c2p,&mtx);
		}
		printf("[Producator %d]\tBufferul e gol, il rezolv!\n",tid);
		for(int i=0;i<SIZE;i++){
			read(fd,&vals[i],1);
		}
		flag = 1;
		printf("[Producator %d]\tBufferul e plin, bafta!\n",tid);
		pthread_cond_signal(&p2c);
	pthread_mutex_unlock(&mtx);
        return NULL;
}


int main(int argc, char* argv[]){
	pthread_t tc[NRT];
	pthread_t tp[NRT];
	pthread_cond_init(&p2c,NULL);
	pthread_cond_init(&c2p,NULL);
	pthread_mutex_init(&mtx,NULL);
	fd = open("random-file.bin",O_RDONLY);
	if(fd == -1){
		perror("open(): ");
		exit(0);
	}
	for(int i=0;i<NRT;i++){
		int* c = (int*)malloc(sizeof(int));
		int* p = (int*)malloc(sizeof(int));
		*c=i;
		*p=i;
		pthread_create(&tc[i],NULL,consum,c);
		pthread_create(&tp[i],NULL,produc,p);
	}
	for(int i=0;i<NRT;i++){
		pthread_join(tp[i],NULL);
		pthread_join(tc[i],NULL);
	}
	pthread_cond_destroy(&p2c);
	pthread_cond_destroy(&c2p);
	pthread_mutex_destroy(&mtx);
	printf("Suma este %d\n",sum);
	return 0;
}
