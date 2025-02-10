#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>

int val = 0;
pthread_mutex_t m;

void* f(void* a){
	int k = *(int*)a;
	free(a);
	sleep(1);
	pthread_mutex_lock(&m);
	val+=1;
	pthread_mutex_unlock(&m);
	printf("Thread %d - val %d\n",k,val);
	return NULL;
}

int main(){
	pthread_t t[100];
	pthread_mutex_init(&m,NULL);
	for(int i=0;i<100;i++){
		int* p = (int*)malloc(sizeof(int));
		*p = i;
		pthread_create(t+i,NULL,f,p);
	}
	for(int i=0;i<100;i++)
		pthread_join(t[i], NULL);
	printf("Valoarea este %d\n",val);
	pthread_mutex_destroy(&m);
	return 0;
}
