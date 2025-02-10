#include <stdio.h>
#include <pthread.h>

void* f(void* a){
	printf("Eu sunt thread ul %d\n",*(int*)a);
	return NULL;
}

int main(){
	pthread_t t[10];
	int p[10];
	for(int i=0;i<10;i++){
		p[i]=i;
		pthread_create(t+i,NULL,f,p+i);
	}
	for(int i=0;i<10;i++)
		pthread_join(t[i],NULL);
	return 0;
}
