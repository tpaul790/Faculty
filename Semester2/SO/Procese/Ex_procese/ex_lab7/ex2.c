#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
//Scrieți un program C care execută o comandă Bash și scrie rezultatul executiei in fisier.
//Exemplu: ls -l > text
int main(int argc, char** argv){
        if(argc!=2){
		fprintf(stderr,"Utilizare: %s output_file\n",argv[0]);
		exit(EXIT_FAILURE);
	}
	int fd=open(argv[1], O_CREAT | O_TRUNC | O_WRONLY, 0644);
	if(fd==-1){
		perror("open() error: ");
		exit(EXIT_FAILURE);
	}
	dup2(fd,1);
	char* v[]={"ls","-l",NULL};
	execvp(v[0],v);
        return 0;
}
