#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <wait.h>

int main(int argc, char* argv[]){
	int a[]={1,2,3,4};
	int pid=fork();
	if(pid == -1){
		perror("fork error: ");
		exit(EXIT_FAILURE);
	}
	if(pid == 0){
		a[0]+=a[1];
		printf("[IN CHILD]\t Suma este: %d\n",a[0]);
		exit(a[0]);
	}
	int status;
	a[2]+=a[3];
	printf("[IN PARENT]\t Suma este: %d\n",a[2]);
	wait(&status);
	printf("[IN PARENT]\t child exit status: %d\n",status);
	int sum=WEXITSTATUS(status);
	sum+=a[2];
	printf("Suma: %d\n",sum);
	return 0;
}
