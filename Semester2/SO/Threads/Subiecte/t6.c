#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#define SIZE 2000
int N,K,NRT;
int vals[80001];
int frecv[41];
int total;
pthread_mutex_t mtx;
void* citeste(void* a){
	int fd = open("random-file.bin",O_RDONLY);
	for(int i=0;i<N;i++)
		read(fd,&vals[i],1);
	return NULL;
}

void* f(void* a){
	int tid =*(int*)a;
	free(a);
	int st = tid*2000;
	int dr = st+2000;
	for(int i=st;i<dr;i++)
		if(vals[i]%K==0)
			frecv[tid]++;
	pthread_mutex_lock(&mtx);
	printf("[Thread %d]\tIn [%d,%d) sunt %d numere divizibile cu %d\n",tid,st,dr,frecv[tid],K);
	total+=frecv[tid];
	pthread_mutex_unlock(&mtx);
	return NULL;
}

int main(int argc, char* argv[]){
	printf("K: ");
	scanf("%d",&K);
	printf("N: ");
	scanf("%d",&N);
	pthread_t tc;
	pthread_create(&tc,NULL,citeste,NULL);
	pthread_join(tc,NULL);

	NRT=N/SIZE;
	pthread_t t[NRT];
	for(int i=0;i<NRT;i++){
		int* id = (int*)malloc(sizeof(int));
		*id=i;
		pthread_create(&t[i],NULL,f,id);
	}
	for(int i=0;i<NRT;i++)
		pthread_join(t[i],NULL);
	printf("Numarul total de numere divizibile cu %d este %d!\n",K,total);
	return 0;
}
