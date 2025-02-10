#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#define SIZE 3000
int C,N,NRT;
int vals[SIZE];
int poz;
int fd;
pthread_mutex_t mtx;
pthread_cond_t p2c;//aici dorm consumatorii
pthread_cond_t c2p;//aici dorm producatorii
pthread_barrier_t barr;
int flag;
int diferente[30];
int anterior,cnt;

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
	int dif=-1,nr1,nr2;//fiecare thread va calcula pentru intervalul sau diferenta maxima
			//si numerele care o dau
	pthread_mutex_lock(&mtx);
	while(empty()){
		//astept sa se umple bufferul
		pthread_cond_wait(&p2c,&mtx);
	}
	cnt++;//al catelea consumator e
	//bufferul e plin
	flag = 0;//resetez flagul
	for(int i=1;i<SIZE;i++){//calculez diferenta maxima pe intervalul asta
		if(vals[i-1]%10==C && vals[i]%10==C && abs(vals[i]-vals[i-1])>dif){
			dif=abs(vals[i]-vals[i-1]);
			nr1=vals[i-1];
			nr2=vals[i];
		}
	}
	//cazul in care perechea successiva este formata din ultimul element al intervalului trecut
	//si primul element al intervalului curent
	if(cnt>1){//conditia asta face ca primul consumator sa nu intre in if pentru ca el n are anterior
		int first=vals[0];
		if(first%10==C && anterior%10==C && abs(first-anterior)>dif){
                        dif=abs(first-anterior);
                        nr1=anterior;
                        nr2=first;
                }
	}
	pthread_cond_signal(&c2p);
	pthread_mutex_unlock(&mtx);
	diferente[tid]=dif;
	pthread_barrier_wait(&barr);//toti asteapta la bariera pentru a avea toate diferentele calculate
				//pentru a o selecta pe cea maxima
	int dif_max=0;
	for(int i=0;i<NRT;i++)
		if(diferente[i]>dif_max)
			dif_max=diferente[i];
	if(dif_max == diferente[tid])
		printf("[Consumator %d]\tDif=%d,NR1=%d,NR2=%d\n",tid,dif,nr1,nr2);
	return NULL;
}

void* produc(void* p){
        //int tid = *(int*)p;
        free(p);
	pthread_mutex_lock(&mtx);
	while(full()){
		//bufferul e plin
		pthread_cond_wait(&c2p,&mtx);
	}
	//bufferul e gol
	poz=0;
	flag=1;
	for(int i=0;i<SIZE;i++)
		read(fd,&vals[i],1);
	anterior=vals[SIZE-1];
	pthread_cond_signal(&p2c);
	pthread_mutex_unlock(&mtx);
        return NULL;
}


int main(int argc,char* argv[]){
	fd = open("random-file.bin",O_RDONLY);
	printf("C: ");
	scanf("%d",&C);
	printf("N: ");
	scanf("%d",&N);
	NRT=N/SIZE;
	pthread_t tp[NRT];
	pthread_t tc[NRT];
	pthread_mutex_init(&mtx,NULL);
        pthread_cond_init(&p2c,NULL);
        pthread_cond_init(&c2p,NULL);
	pthread_barrier_init(&barr,NULL,NRT);
	for(int i=0;i<NRT;i++){
		int* nr1 = (int*)malloc(sizeof(int));
		int* nr2 = (int*)malloc(sizeof(int));
		*nr1=i;
		*nr2=i;
		pthread_create(&tp[i],NULL,produc,nr1);
		pthread_create(&tc[i],NULL,consum,nr2);
	}
	for(int i=0;i<NRT;i++){
		pthread_join(tp[i],NULL);
		pthread_join(tc[i],NULL);
	}
	pthread_mutex_destroy(&mtx);
	pthread_cond_destroy(&p2c);
	pthread_cond_destroy(&c2p);
	pthread_barrier_destroy(&barr);
	close(fd);
	return 0;
}
