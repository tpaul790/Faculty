#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#define NRT 25

int prim(int n){
	if(n<2)
		return 0;
	if(n>2 && n%2==0)
		return 0;
	for(int i=3;i*i<=n;i++)
		if(n%i==0)
			return 0;
	return 1;
}
pthread_mutex_t mtx;
pthread_barrier_t barr;
int N,vals[100001];
int prime[NRT],frecv[NRT];//pe pozitia 0 se afla primul numar prim precum si frecventa sa
			//pe a 2a poz se afla al doilea numar prim si frecventa sa, etc
int sum,medie;

void* f(void* a){
	int tid = *(int*)a;
	free(a);
	int f=0;

	int p=prime[tid];//fiecare are numarul sau
	for(int i=0;i<N;i++)
		if(vals[i] == p)
			f++;
	frecv[tid]=f;
	pthread_mutex_lock(&mtx);
	sum+=f;
	pthread_mutex_unlock(&mtx);

	pthread_barrier_wait(&barr);//toate asteapta la bariera pentru ca am nevoie de toate frecventele
	medie=sum/NRT;
	int max_mai_mic=0,min_mai_mare=N+1;
	for(int i=0;i<NRT;i++){
		if(frecv[i]<=medie && frecv[i]>max_mai_mic)
			max_mai_mic = frecv[i];
		if(frecv[i]>=medie && frecv[i]<min_mai_mare)
			min_mai_mare=frecv[i];
	}
	int apropiat;
	if(abs(medie-max_mai_mic)>abs(medie-min_mai_mare))
		apropiat = min_mai_mare;
	else
		apropiat = max_mai_mic;
	if(frecv[tid] == apropiat)
		printf("[Thread %d]\tNumar:%d, Frecventa: %d\n",tid,prime[tid],frecv[tid]);

	return NULL;
}


int main(int argc,char* argv[]){
	printf("N: ");
	scanf("%d",&N);
	int fd = open("random-file.bin",O_RDONLY);
	for(int i=0;i<N;i++)
		read(fd,&vals[i],1);
	pthread_t t[NRT];//cate un thread pt fiecare numar prim
	int k = -1;
	for(int i=1;i<=100;i++)
		if(prim(i))
			prime[++k]=i;//pun toate numere prime din (0,100]
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
	printf("Media este: %d!\n",medie);
	return 0;
}
