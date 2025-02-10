#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <wait.h>

int main(int argc, char* argv[]){
	int fd[2];
	int a[]={1,2,3,4};
	int res = pipe(fd);
	if(res==-1){
		perror("pipe() error: ");
		exit(EXIT_FAILURE);
	}
	int pid=fork();
	if(pid==-1){
		perror("fork() error: ");
		exit(EXIT_FAILURE);
	}
	if(pid==0){
		close(fd[0]);//inchid capatul de citire
		printf("[IN CHILD]\t Pid: %d,parent pid: %d\n",getpid(),getppid());
		a[0]+=a[1];
		printf("[IN CHILD]\t Suma este: %d\n",a[0]);
		write(fd[1],&a[0],sizeof(int));
		close(fd[1]);//incjid si capatul de scriere dupa ce am scris
		exit(0);
	}
	int status;
	wait(&status);
	close(fd[1]);//inchid capatul de scriere in parinte
	printf("[IN PARENT]\t Pid: %d, child pid: %d\n",getpid(),pid);
	a[2]+=a[3];
	read(fd[0],&a[0],sizeof(int));
	close(fd[0]);
	a[0]+=a[2];
	printf("[IN PARENT]\t Suma este: %d\n",a[0]);
	return 0;
}
