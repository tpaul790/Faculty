#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#define SIZE 3000
int N,K,NRT;
int vals[90001];
int dif_max;
pthread_mutex_t mtx;
void* citeste(void* a){
        int fd = open("random-file.bin",O_RDONLY);
        for(int i=0;i<N;i++)
                read(fd,&vals[i],1);
        close(fd);
	return NULL;
}

void* f(void* a){
        int tid =*(int*)a;
        free(a);
        int st = tid*SIZE;
        int dr = st+SIZE;
	int max = 0,sum = 0,cnt = 0;
        for(int i=st;i<dr;i++){
                if(vals[i]>=10 && vals[i]<=99 && vals[i]>max)
                        max=vals[i];
		if(vals[i]>=100 && vals[i]<=255)
			sum+=vals[i],cnt++;
	}
	int medie = sum/cnt;
        pthread_mutex_lock(&mtx);
        printf("[Thread %d]\tIn [%d,%d). Max de 2 cif: %d. Media nr de 3 cif: %d. Dif: %d\n",tid,st,dr,max,medie,abs(medie-max));
        if(abs(medie-max)>dif_max)
		dif_max=abs(medie-max);
        pthread_mutex_unlock(&mtx);
        return NULL;
}

int main(int argc, char* argv[]){
        printf("N: ");
        scanf("%d",&N);
        pthread_t tc;
        pthread_create(&tc,NULL,citeste,NULL);
        pthread_join(tc,NULL);

        NRT=N/SIZE;
        pthread_t t[NRT];
        for(int i=0;i<NRT;i++){
                int* id = (int*)malloc(sizeof(int));
                *id=i;
                pthread_create(&t[i],NULL,f,id);
        }
        for(int i=0;i<NRT;i++)
                pthread_join(t[i],NULL);
        printf("Diferenta maxima este: %d\n",dif_max);
        return 0;
}

