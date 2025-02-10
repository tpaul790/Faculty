#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#define NRT 10

int sums[NRT];
int mat[NRT][NRT];


void* f(void* p){
	int tid = *(int*)p;
	free(p);
	int sum=0;
	for(int i=0;i<NRT;i++)
		sum+=mat[tid][i];
	sums[tid]=sum;
	return NULL;
}

int main(int argc, char* argv[]){
	pthread_t t[NRT];
	int fd = open("random-file.bin",O_RDONLY);
	if(fd == -1){
		perror("opent(): ");
		exit(0);
	}
	for(int i=0;i<NRT;i++){
		for(int j=0;j<NRT;j++){
			read(fd,&mat[i][j],1);
			printf("%d ",mat[i][j]);
		}
		printf("\n");
	}
	for(int i=0;i<NRT;i++){
		int* nr=(int*)malloc(sizeof(int));
		*nr=i;
		pthread_create(&t[i],NULL,f,nr);
	}
	for(int i=0;i<NRT;i++)
		pthread_join(t[i],NULL);
	printf("[Principal]\tThreadurile si au terminat treaba, sumele sunt:\n");
	for(int i=0;i<NRT;i++)
		printf("%d ",sums[i]);
	printf("\n");
	return 0;
}
