#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

int vals[101];
int frecv[10];
pthread_mutex_t m[10];
void* f(void* p){
	int tid = *(int*)p;
	free(p);
	int nr = vals[tid];
	while(nr){
		int c = nr%10;
		pthread_mutex_lock(&m[c]);
		frecv[c]++;
		pthread_mutex_unlock(&m[c]);
		nr/=10;
	}
	return NULL;
}

int main(int argc, char* argv[]){
	if(argc <= 1){
		printf("Numar insuficient de argumente\n");
		exit(0);
	}
	int nrt = argc-1;
	pthread_t t[nrt];
	for(int i=1;i<argc;i++){
		int nr = atoi(argv[i]);
		vals[i-1]=nr;
	}
	for(int i=0;i<10;i++)
		pthread_mutex_init(&m[i],NULL);
	for(int i=0;i<nrt;i++){
		int* nr=(int*)malloc(sizeof(int));
		*nr=i;
		pthread_create(&t[i],NULL,f,nr);
	}
	for(int i=0;i<nrt;i++)
		pthread_join(t[i],NULL);
	for(int i=0;i<10;i++)
                pthread_mutex_destroy(&m[i]);
	for(int i=0;i<10;i++)
		printf("%d ",frecv[i]);
	printf("\n");
	return 0;

}
