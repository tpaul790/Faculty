#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#define NRT 10
int N,vals[70001];
int sum,medie,frecv[NRT];
pthread_mutex_t mtx;
pthread_barrier_t barr;

void* f(void* a){
	int tid = *(int*)a;
	free(a);
	for(int i=0;i<N;i++)
		if(vals[i]%10 == tid)
			frecv[tid]++;
	pthread_mutex_lock(&mtx);
	sum+=frecv[tid];
	pthread_mutex_unlock(&mtx);

	pthread_barrier_wait(&barr);

	pthread_mutex_lock(&mtx);
	medie=sum/NRT;
	pthread_mutex_unlock(&mtx);

	int max_mai_mic=0,min_mai_mare=N+1;
	for(int i=0;i<NRT;i++){
		if(frecv[i]<=medie && frecv[i]>max_mai_mic)
			max_mai_mic=frecv[i];
		if(frecv[i]>=medie && frecv[i]<min_mai_mare)
			min_mai_mare=frecv[i];
	}
	int apropiat;
	if(abs(medie-max_mai_mic)>abs(medie-min_mai_mare))
		apropiat = min_mai_mare;
	else
		apropiat = max_mai_mic;
	if(frecv[tid] == apropiat)
		printf("[Thread %d]\tCifra:%d ,Frecventa:%d\n",tid,tid,frecv[tid]);
	return NULL;
}

int main(int argc,char* argv[]){
	printf("N: ");
	scanf("%d",&N);
	int fd = open("random-file.bin",O_RDONLY);
	for(int i=0;i<N;i++)
		read(fd,&vals[i],1);
	pthread_t t[NRT];
	pthread_mutex_init(&mtx,NULL);
	pthread_barrier_init(&barr,NULL,NRT);
	for(int i=0;i<NRT;i++){
		int* id = (int*)malloc(sizeof(int));
		*id=i;
		pthread_create(&t[i],NULL,f,id);
	}
	for(int i=0;i<NRT;i++)
		pthread_join(t[i],NULL);
	pthread_mutex_destroy(&mtx);
	pthread_barrier_destroy(&barr);
	printf("Media este: %d!\n",medie);
	return 0;
}
