#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <wait.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

int main(int argc,char** argv){
	if(argc!=2){
		fprintf(stderr,"Usage: %s output_file\n",argv[0]);
		exit(EXIT_FAILURE);
	}
	//deschid fisierul pentru scriere
	int fd=open(argv[1],O_CREAT | O_TRUNC | O_WRONLY, 0644);
	if(fd==-1){
		perror("open() error: ");
		exit(EXIT_FAILURE);
	}
	//fac ca iesirea standard sa fie fisierul sursa
	dup2(fd,1);
	//creez un pipe
	int f[2];
	int res=pipe(f);
	if(res==-1){
		perror("pipe() error: ");
		exit(EXIT_FAILURE);
	}
	//creez un proces copil care va scrie un numar 
	int pid=fork();
	if(pid==-1){
		perror("fork() error: ");
		exit(EXIT_FAILURE);
	}
	if(pid==0){
		close(f[0]);
		int a=10;
		write(f[1],&a,sizeof(int));
		printf("[IN CHILD]\t AM scris numarul %d!\n",a);
		close(f[1]);
		exit(EXIT_SUCCESS);
	}
	close(f[1]);
	int a;
	read(f[0],&a,sizeof(int));
	close(f[0]);
	printf("[IN PARENT]\t Copilul a scris numarul %d\n",a);
	int status;
	wait(&status);
	printf("[IN PARENT]\t Copilul si-a terminat executia cu codul %d\n",status);
	return 0;
}
