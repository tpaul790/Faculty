#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/types.h>

int main(int argc,char** argv){
	int pid=fork();
	if(pid==-1){
		perror("fork() error: ");
		exit(EXIT_FAILURE);
	}
	//in child
	if(pid==0){
		printf("IN CHILD pid: %d,parent pid: %d\n",getpid(),getppid());
		exit(0);
	}
	//in parent
	int status;
        wait(&status); //astept sa termine compilul pentru a nu l lasa orfan
	printf("IN PARENT pid: %d,child pid: %d\n",getpid(),pid);
	return 0;
}
