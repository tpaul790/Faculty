#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdbool.h>
pthread_cond_t cv;
pthread_mutex_t mtx;
int poz, n;
int flag;
void* par(void *arg){
	int* nums = (int*)arg;
	while(true){
		pthread_mutex_lock(&mtx);
		if(flag == 1)
			pthread_cond_wait(&cv,&mtx);
		flag = 1;
		if(poz == n){
			pthread_mutex_unlock(&mtx);
                        pthread_cond_signal(&cv);
                        break;
                }
		int nr = rand()%100;
		if(nr%2==1)
			nr++;
		nums[poz++]=nr;
		pthread_cond_signal(&cv);
		pthread_mutex_unlock(&mtx);
	}
	return NULL;
}

void* impar(void *arg){
        int* nums = (int*)arg;
        while(true){
                pthread_mutex_lock(&mtx);
		if(flag == 0)
                        pthread_cond_wait(&cv,&mtx);
                flag = 0;
		if(poz == n){
			pthread_cond_signal(&cv);
                        pthread_mutex_unlock(&mtx);
			break;
		}
                int nr = rand()%100;
                if(nr%2==0)
                        nr++;
                nums[poz++]=nr;
                pthread_cond_signal(&cv);
                pthread_mutex_unlock(&mtx);
        }
	return NULL;
}


int main(int argc, char* argv[]){
	pthread_t t[2];
	printf("Introduceti un numar: ");
	scanf("%d",&n);
	int numere[n];
	pthread_mutex_init(&mtx,NULL);
	pthread_cond_init(&cv,NULL);
	pthread_create(&t[0],NULL,par,numere);
	pthread_create(&t[1],NULL,impar,numere);

	pthread_join(t[0],NULL);
	pthread_join(t[1],NULL);
	pthread_mutex_destroy(&mtx);
	pthread_cond_destroy(&cv);
	for(int i=0;i<n;i++)
		printf("%d ",numere[i]);
	printf("\n");
	return 0;
}
