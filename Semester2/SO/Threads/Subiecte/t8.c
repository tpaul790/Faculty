#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <time.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#define SIZE 110000

int vals[SIZE],NRT;
int generate[55];
int frecv[55],sum;
float medie;
pthread_mutex_t mtx;
pthread_barrier_t barr;

void* f(void* a){
	int tid = *(int*)a;
	free(a);
	for(int i=0;i<SIZE;i++)
		if(vals[i] == generate[tid])
			frecv[tid]++;
	pthread_mutex_lock(&mtx);
	sum+=frecv[tid];
	pthread_mutex_unlock(&mtx);

	pthread_barrier_wait(&barr);

	pthread_mutex_lock(&mtx);
	medie = (float)sum/NRT;
	pthread_mutex_unlock(&mtx);
	int min_mai_mare=SIZE+1,max_mai_mic=0;
	for(int i=0;i<NRT;i++){
		if(frecv[i]<=medie && frecv[i]>max_mai_mic)
			max_mai_mic=frecv[i];
		if(frecv[i]>=medie && frecv[i]<min_mai_mare)
			min_mai_mare=frecv[i];
	}
	int apropiat;
	if(abs(medie-min_mai_mare)>abs(medie-max_mai_mic))
		apropiat = max_mai_mic;
	else
		apropiat = min_mai_mare;
	if(frecv[tid] == apropiat)
		printf("[Thread %d]\tNumarul:%d. Frecventa:%d\n",tid,generate[tid],frecv[tid]);
	return NULL;
}

int main(int argc, char* argv[]){
	int fd = open("random-file.bin",O_RDONLY);
	for(int i=0;i<SIZE;i++)
		read(fd,&vals[i],1);
	printf("K: ");
	scanf("%d",&NRT);
	srand(time(NULL));
	for(int i=0;i<NRT;i++){
		generate[i]=rand()%255+1;
	}

	pthread_t t[NRT];
	pthread_mutex_init(&mtx,NULL);
	pthread_barrier_init(&barr,NULL,NRT);
	for(int i=0;i<NRT;i++){
		int* id=(int*)malloc(sizeof(int));
		*id = i;
		pthread_create(&t[i],NULL,f,id);
	}
	for(int i=0;i<NRT;i++)
		pthread_join(t[i],NULL);
	pthread_mutex_destroy(&mtx);
	pthread_barrier_destroy(&barr);
	close(fd);
	printf("Media este: %f\n",medie);
	return 0;
}
