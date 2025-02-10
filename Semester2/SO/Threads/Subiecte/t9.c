#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#define SIZE 3000
int NRT;
int C,N;
int fd;
int flag;
int vals[90001];
pthread_mutex_t mtx;
pthread_cond_t p2c;
pthread_cond_t c2p;
pthread_barrier_t barr;

int empty(){
	if(flag==0)
		return 1;
	return 0;
}

int full(){
	if(flag==1)
		return 1;
	return 0;
}

void* produc(void* a){
	//int tid = *(int*)a;
	free(a);
	pthread_mutex_lock(&mtx);
	while(full()){
	//dorm
		pthread_cond_wait(&c2p,&mtx);
	}
	//bufferul e gol
	for(int i=0;i<SIZE;i++)
		read(fd,&vals[i],1);
	//am umplut bufferul
	flag = 1;
	pthread_cond_signal(&p2c);//trezesc un consumator(sau mai multi)
	pthread_mutex_unlock(&mtx);
	return NULL;
}
int diferente[30];
void* consum(void* a){
	int tid = *(int*)a;
	free(a);
	int dif=0,nr1,nr2;
	pthread_mutex_lock(&mtx);
	while(empty()){
	//dorm
		pthread_cond_wait(&p2c,&mtx);
	}
	//bufferul e plin
	flag = 0;
	for(int i=1;i<SIZE;i++){
		if(vals[i-1]%10==C && vals[i]%10==C && abs(vals[i-1]-vals[i])>dif){
			dif=abs(vals[i-1]-vals[i]);
			nr1 = vals[i-1];
			nr2 = vals[i];
		}
	}
	diferente[tid]=dif;
	pthread_cond_signal(&c2p);
	pthread_mutex_unlock(&mtx);

	pthread_barrier_wait(&barr);
	int maxim=0;
	for(int i=0;i<NRT;i++)
		if(diferente[i]>maxim)
			maxim=diferente[i];
	if(diferente[tid] == maxim)
		printf("Thread %d]\tDif: %d. NR1: %d. NR2: %d\n",tid,diferente[tid],nr1,nr2);
	return NULL;
}

int main(int argc, char* argv[]){
	fd = open("random-file.bin",O_RDONLY);
	printf("C: ");
	scanf("%d",&C);
	printf("N: ");
	scanf("%d",&N);
	NRT = N/SIZE;
	pthread_t tp[NRT];
	pthread_t tc[NRT];
	pthread_mutex_init(&mtx,NULL);
	pthread_barrier_init(&barr,NULL,NRT);
	pthread_cond_init(&p2c,NULL);
	pthread_cond_init(&c2p,NULL);
	for(int i=0;i<NRT;i++){
		int* id1 = (int*)malloc(sizeof(int));
		int* id2 = (int*)malloc(sizeof(int));
		*id1=i;
		*id2=i;
		pthread_create(&tp[i],NULL,produc,id1);
		pthread_create(&tc[i],NULL,consum,id2);
	}
	for(int i=0;i<NRT;i++){
		pthread_join(tp[i],NULL);
		pthread_join(tc[i],NULL);
	}
	pthread_mutex_destroy(&mtx);
        pthread_barrier_destroy(&barr);
        pthread_cond_destroy(&p2c);
        pthread_cond_destroy(&c2p);
	return 0;
}
