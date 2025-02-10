#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

#define NRT 50

pthread_mutex_t mtx;
pthread_barrier_t barr;
int vals[170001];
int sum,medie,frecv[NRT];
int N;

void* f(void* a){
	int tid = *(int*)a;
	free(a);
	int f=0;//frecventa numarului (tid+1)*5;
	for(int i=0;i<N;i++)
		if(vals[i] == (tid+1)*5)
			f++;
	frecv[tid-1]=f;//pun frecventele intr un vector de frecventa
	pthread_mutex_lock(&mtx);
	sum+=f;//fac suma frecventelor
	pthread_mutex_unlock(&mtx);
	//toti asteapta la bariera pentru a putea calcula media
	pthread_barrier_wait(&barr);
	medie=sum/NRT;//toti si au gasit frecventele asa ca pot calcula media
	int max_mai_mic=0,min_mai_mare=255;//cea mai mica frecventa mai mare decat medie si cea
					  // mai mare frecventa mai mica decat media
	for(int i=0;i<NRT;i++){
		if(frecv[i]<=medie && frecv[i]>max_mai_mic)
			max_mai_mic=frecv[i];
		if(frecv[i]>=medie && frecv[i]<min_mai_mare)
			min_mai_mare=frecv[i];
	}
	int apropiat;//frecventa cea mai apropiata este cel mai mai mare element <=medie sau
	//cel mai mic element mai mare decat media in functie de care diferenta dintre el si medie
	//e mai mica
	if(abs(medie-min_mai_mare)>abs(medie-max_mai_mic))
		apropiat = max_mai_mic;
	else
		apropiat = min_mai_mare;
	//fiecare thread are numarul si frecventa sa deci daca frecventa este cea mai
	//apropiata atunci acesta afiseaza numarul
	if(frecv[tid-1]==apropiat)
		printf("%d ",(tid+1)*5);
	return NULL;
}

int main(int argc,char* argv[]){
	int fd = open("random-file.bin",O_RDONLY);
	printf("Introduceti un numar: ");
	scanf("%d",&N);
	for(int i=0;i<N;i++){
		read(fd,&vals[i],1);
	}//citesc cele n numere pe 1 bit
	pthread_t t[NRT];//creez 50 de thread uri, unul pentru fiecare nr (5,255]
	pthread_mutex_init(&mtx,NULL);
	pthread_barrier_init(&barr,NULL,NRT);
	for(int i=0;i<NRT;i++){
		int* nr = (int*)malloc(sizeof(int));
		*nr = i+1;
		pthread_create(&t[i],NULL,f,nr);
	}
	for(int i=0;i<NRT;i++)
		pthread_join(t[i],NULL);
	pthread_mutex_destroy(&mtx);
	pthread_barrier_destroy(&barr);
	printf("\n");
	close(fd);
	return 0;
}
