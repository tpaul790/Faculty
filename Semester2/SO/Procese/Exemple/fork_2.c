#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>

int main(int argc, char* argv[]){
	int pid = fork();
	if(pid == -1){
		perror("fork() error: ");
		exit(EXIT_FAILURE);
	}
	if(pid == 0){
	//in child
		for(int i=0;i<3;i++){
			sleep(1);
		printf("[IN CHILD]\t Pid: %d,parent pid: %d\n",getpid(),getppid());
		}
		printf("[IN CHILD]\t I finished my job\n");
	}
	else{
	//in parent
		signal(SIGCHLD,SIG_IGN);
		printf("[IN PARENT]\t Pid: %d,child pid: %d\n",getpid(),pid);
		while(1);
		printf("[IN PARENT]\t I finished my job\n"); 
	}
	return 0;
}
