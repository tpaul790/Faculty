#include <stdio.h>
#include <unistd.h>
#include <pthread.h>
#include <stdlib.h>

pthread_t t[10];

void* afis(void* p){
	int i = *(int*)p;
	printf("Eu sunt thread ul: %d\n",i);
	free(p);
	return NULL;
}


int main(int argc, char* argv[]){
	for(int i=0;i<10;i++){
		int* nr = (int*)malloc(sizeof(int));
		*nr = i;
		pthread_create(t+i,NULL,afis,nr);
	}
	for(int i=0;i<10;i++)
		pthread_join(t[i],NULL);
	return 0;
}
