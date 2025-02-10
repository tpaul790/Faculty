#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>

void* f(void* a){
	printf("Eu sunt thread ul %d\n",*(int*)a);
	free(a);
	return NULL;
}

int main(int argc, char* argv[]){
	pthread_t t[10];
	for(int i=0; i<10;i++){
		int* nr = (int*)malloc(sizeof(int));
		*nr = i;
		pthread_create(&t[i],NULL,f,nr);
	}
	for(int i=0;i<10;i++){
		pthread_join(t[i],NULL);
	}
	return 0;
}
