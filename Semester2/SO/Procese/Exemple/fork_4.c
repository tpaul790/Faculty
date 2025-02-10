//copilul face suma numerelor pare de la 0-9 si o trimite la parinte prin exit
//parintele face suma numerelor impare de la 0-9 si cu suma primita 
//de la copil face suma totala a numerelor de la 0-9
//se foloseste un vector alocat dinamic

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <wait.h>
#define MAX 10
int main(int argc, char** argv){
	int* a=(int*)malloc(10*sizeof(int));
	if(a == NULL){
		perror("malloc error: ");
		exit(EXIT_FAILURE);
	}
	//punem valori in vector
	for(int i=0;i<MAX;i++)
		a[i]=i;
	int pid = fork();
	if(pid == -1){
		perror("fork() error: ");
		exit(EXIT_FAILURE);
	}
	if(pid == 0){
		//in child,facem suma numerelor impare
		printf("[IN CHILD]\t Pid: %d,parent pid: %d\n",getpid(),getppid());
		int sum=0;
		for(int i=0;i<MAX;i++)
			if(a[i]%2==0)
				sum+=a[i];
		printf("[IN CHILD]\t I finised my job, the sum is : %d\n",sum);
		exit(sum);
	}
	//in parite, fac restul sumei
	printf("[IN PARENT]\t Pid: %d, child pid: %d\n",getpid(),pid);
	int status;
	wait(&status);
	int sum=WEXITSTATUS(status);
	printf("[IN PARENT]\t Child finishvaled with exit status: %d and the sum sended is: %d\n",status,sum);
	for(int i=0;i<MAX;i++)
		if(a[i]%2==1)
			sum+=a[i];
	printf("[IN PARENT]\t I finised my job, the sum is: %d\n",sum);
	free(a);
	return 0;
}
