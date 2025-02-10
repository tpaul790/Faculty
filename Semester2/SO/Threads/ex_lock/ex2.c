#include <stdio.h>
#include <pthread.h>
/*
Problema rezolvata cu mutex,rwl sau cu sem binar(echivalent cu mutex)
*/
int seats = 100;
float price  = 75.0f;
pthread_mutex_t m;
pthread_rwlock_t rwl;
sem_t sem;
void* check(void* a){
	int k = (int)a;
//	pthread_mutex_lock(&m);
//	pthread_rwlock_rdlock(&rwl);
	sem_wait(&sem);
	printf("Reader %d: Locuri %d - Pret %f\n",k,seats,price*(2-seats/100.0f));
	sem_post(&sem);
//	pthread_rwlock_unlock(&rwl);
//	pthread_mutex_unlock(&m);
	return NULL;
}

void* buy(void* a){
	int k = (int)a;
	//pthread_mutex_lock(&m);
//	pthread_rwlock_wrlock(&rwl);
	sem_wait(&sem);
	printf("Writer %d: Locuri %d - Pret %f\n",k,seats,price*(2-seats/100.0f));
	seats--;
	sem_post(&sem);
//	pthread_rwlock_unlock(&rwl);
	//pthread_mutex_unlock(&m);
	return NULL;
}

int main(){
	pthread_t r[1000];
	pthread_t w[100];
	pthread_mutex_init(&m,NULL);
	pthread_rwlock_init(&rwl,NULL);
	sem_init(&sem,0,1);
	for(int i=0;i<1000;i++)
		pthread_create(r+i,NULL,check,(void*)i);
	for(int i=0;i<100;i++)
		pthread_create(w+i,NULL,buy,(void*)i);
	for(int i=0;i<1000;i++)
		pthread_join(r[i],NULL);
	for(int i=0;i<100;i++)
		pthread_join(w[i],NULL);

	sem_destroy(&sem);
	pthread_rwlock_destroy(&rwl);
	pthread_mutex_destroy(&m);
	return 0;
}
