#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <wait.h>
//Scrieți un program C care execută două comenzi Bash conectate prin pipe.
//Exemplu: ls -l | wc -l
int main(int argc,char** argv){
	char* cmd1[]={"cat","text",NULL};
	char* cmd2[]={"wc","-l",NULL};
	int fd[2];
	int res=pipe(fd);
	if(res==-1){
		perror("pipe() error: ");
		exit(EXIT_FAILURE);
	}
	int pid1=fork();
	if(pid1==-1){
		perror("fork() error: ");
		exit(EXIT_FAILURE);
	}
	if(pid1==0){
	//in primul copil scriu in pipe ce returneaza ls -l
		close(fd[0]);//inchid capatul de citire
		dup2(fd[1],1);//fac sa se scrie in pipe ce se afiseaza la iesirea standard
		if(execvp(cmd1[0],cmd1)<0){
			perror("execvp() error: ");
			exit(EXIT_FAILURE);
		}
		exit(EXIT_SUCCESS);//nu e neaparat necesar din cauza codului aruncat de execvp
	}

	int pid2=fork();
	if(pid2==-1){
                perror("fork() error: ");
                exit(EXIT_FAILURE);
        }
        if(pid2==0){
        //in cel de al doilea copil trimit lui wc -l ce este deja scris in pipe
                close(fd[1]);//inchid capatul de scriere
                dup2(fd[0],0);//fac ca intrarea standard sa fie ce se citeste din pipe
                if(execvp(cmd2[0],cmd2)<0){
                        perror("execvp() error: ");
                        exit(EXIT_FAILURE);
                }
                exit(EXIT_SUCCESS);//nu e neaparat necesar din cauza codului aruncat de execvp
        }
	close(fd[0]);
	close(fd[1]);
	int status;
	waitpid(pid1,&status,0);
	printf("Copilul cu pid: %d si-a terminat executia cu codul: %d\n",pid1,status);
	waitpid(pid2,&status,0);
        printf("Copilul cu pid: %d si-a terminat executia cu codul: %d\n",pid2,status);
	return 0;
}
