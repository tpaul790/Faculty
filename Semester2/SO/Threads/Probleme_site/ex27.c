#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>

char sir[129];
int poz;
pthread_cond_t p2c;
pthread_cond_t c2p;
pthread_mutex_t mtx;
pthread_barrier_t barr;
int N,M;

void* produc(void* p){
//	int tid = *(int*)p;
	free(p);
	while(1){
		char x = random()%('z'-'a'+1)+'a';
		pthread_mutex_lock(&mtx);
			while(poz == 128){
				pthread_cond_signal(&p2c);
				pthread_cond_wait(&c2p,&mtx);
			}
			if(M == 0){
				pthread_mutex_unlock(&mtx);
				break;
			}
			sir[poz++]=x;
		pthread_mutex_unlock(&mtx);
	}
	return NULL;
}

void* afisez(void* p){
	//int tid = *(int*)p;
	free(p);
	while(1){
		pthread_mutex_lock(&mtx);
		while(poz != 128){
			pthread_cond_wait(&p2c,&mtx);
		}
		M--;
		printf("%s\n",sir);
		poz=0;
		pthread_cond_broadcast(&c2p);
		if(M == 0){
			pthread_mutex_unlock(&mtx);
			break;
		}
		pthread_mutex_unlock(&mtx);
	}
	return NULL;
}

int main(int argc, char* argv[]){
	if(argc != 3){
		printf("Numar incorect de argumente!\n");
		exit(0);
	}
	N=atoi(argv[1]);
	M=atoi(argv[2]);
	pthread_t t[N+1];
	pthread_cond_init(&p2c,NULL);
	pthread_cond_init(&c2p,NULL);
	pthread_mutex_init(&mtx,NULL);
	pthread_barrier_init(&barr,NULL,N);
	for(int i=0;i<N;i++){
		int* nr = (int*)malloc(sizeof(int));
		*nr = i;
		pthread_create(&t[i],NULL,produc,nr);
	}
	int* nr = (int*)malloc(sizeof(int));
	*nr=N;
	pthread_create(&t[N],NULL,afisez,nr);
	for(int i=0;i<=N;i++)
		pthread_join(t[i],NULL);
	pthread_cond_destroy(&p2c);
	pthread_cond_destroy(&c2p);
	pthread_mutex_destroy(&mtx);
	pthread_barrier_destroy(&barr);
	return 0;
}
