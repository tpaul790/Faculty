#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#define SIZE 3000
int N,NRT,vals[90001];
int medii[31];
pthread_barrier_t barr;
pthread_mutex_t mtx;
int minima=900001;

void* citeste(void* a){
	int fd = open("random-file.bin",O_RDONLY);
	for(int i=0;i<N;i++)
		read(fd,&vals[i],1);
	close(fd);
	return NULL;
}

void* f(void* a){
	int tid = *(int*)a;
	free(a);
	int M = random()%255+1;
	int medie = 0,sum = 0;//medie pe intervalul thread ului curent [tid*3000,(tid+1)*3000);
	int st=tid*3000;
	int dr=st+3000;
	for(int i=st;i<dr;i++)
		sum+=vals[i];
	medie=sum/3000;
	medii[tid]=medie;

	pthread_barrier_wait(&barr);

	int diferente[31];
	for(int i=0;i<NRT;i++){
		diferente[i]=abs(M-medii[i]);
		pthread_mutex_lock(&mtx);
		if(diferente[i]<minima){
			minima=diferente[i];
		}
		pthread_mutex_unlock(&mtx);
	}
	pthread_mutex_lock(&mtx);
	printf("[Thread %d]\tM=%d. Diferentele sunt: ",tid,M);
	for(int i=0;i<NRT;i++)
		printf("%d ",diferente[i]);
	printf("\n");
	pthread_mutex_unlock(&mtx);
	return NULL;
}


int main(int argc, char* argv[]){
	printf("N: ");
	scanf("%d",&N);
	pthread_t tr;
	pthread_create(&tr,NULL,citeste,NULL);
	pthread_join(tr,NULL);

	NRT=N/SIZE;
	pthread_t t[NRT];
	pthread_barrier_init(&barr,NULL,NRT);
	for(int i=0;i<NRT;i++){
		int* id = (int*)malloc(sizeof(int));
		*id = i;
		pthread_create(&t[i],NULL,f,id);
	}
	for(int i=0;i<NRT;i++)
		pthread_join(t[i],NULL);
	pthread_barrier_destroy(&barr);
	printf("Diferenta minima calculata este: %d!\n",minima);
	printf("Mediile sunt: ");
	for(int i=0;i<NRT;i++)
		printf("%d ",medii[i]);
	printf("\n");
	return 0;
}
