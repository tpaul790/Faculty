#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <wait.h>
#include <string.h>

int main(int argc, char* argv[]){
	int fd[2];
	pipe(fd);
	if(fork() == 0){
		close(fd[1]);
		char ch,sir[15];
		while(1){
			read(fd[0],&ch,1);
			if(ch == '0')
				break;
			read(fd[0],sir,1);
			char ok = sir[0];
			int n=1;
			while(ok != '\0'){
				read(fd[0],sir+n,1);
				ok = sir[n++];
			}
			int poz=-1;
			for(int i=0;i<n && poz==-1;i++)
				if(sir[i] == ch)
					poz=i;
			printf("%c %s %d\n",ch,sir,poz);
		}
		close(fd[0]);
		exit(0);
	}
	close(fd[0]);
	FILE* f = fopen(argv[1],"r");
	char ch,sir[15];
	while(fscanf(f," %c %s",&ch,sir)==2){
		write(fd[1],&ch,1);
		write(fd[1],sir,strlen(sir)+1);
		sleep(1);
	}

	write(fd[1],"0",1);
	fclose(f);
	close(fd[1]);
	wait(0);
	return 0;
}
