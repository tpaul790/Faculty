#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <wait.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

int main(int argc, char* argv[]){
	int fd[2];
	if(pipe(fd) == -1){
		perror("pipe(): ");
		exit(0);
	}
	int pid = fork();
	if(pid == 0){
	//proces B
		close(fd[1]);
		int n;
		char fisier[21];
		read(fd[0],&n,sizeof(int));
		read(fd[0],fisier,21*sizeof(char));

		sleep(2);
		printf("[B]\t N:%d\n",n);
		printf("[B]\t Fisier: %s\n",fisier);

		char ch;
		int i=0;
		int cnt=0;
		sleep(1);
		read(fd[0],&ch,sizeof(char));
		while(ch != '\0'){
			printf("%c",ch);
			read(fd[0],&ch,sizeof(char));
		}

		printf("[B]\t Cuvinte cu %d litere: %d\n",n,cnt);

		close(fd[0]);
		exit(0);
	}
	//proces A
	int n;
	char fisier[21];
	printf("N: ");
	scanf("%d",&n);
	write(fd[1],&n,sizeof(int));

	printf("Fisier: ");
	scanf("%s",fisier);
	write(fd[1],fisier,sizeof(char)*(strlen(fisier)+1));

	if(fork()==0){
		close(fd[0]);
		dup2(fd[1],1);
		char* cmd[]={"cat",fisier,NULL};
		if(0>execvp(cmd[0],cmd)){
			perror("exec(): ");
			exit(0);
		}
		close(fd[1]);
		exit(0);
	}
	close(fd[0]);
	close(fd[1]);
	wait(NULL);
	wait(NULL);
	return 0;
}
