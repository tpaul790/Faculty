#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <wait.h>

int get_sum(int n){
	int sum=0;
	while(n){
		sum+=n%10;
		n/=10;
	}
	return sum;
}

int get_prod(int n){
	int prod=1;
	while(n){
		prod*=n%10;
		n/=10;
	}
	return prod;
}

int main(int argc,char** argv){
	int fd[2];
	if(pipe(fd) == -1)
		exit(0);
	if(fork() == 0){
		close(fd[1]);
		int nr;
		char ch;
		while(1){
			read(fd[0],&ch,sizeof(char));
			if(ch == 's')
				break;
			read(fd[0],&nr,sizeof(int));
			if(ch == '+')
				printf("%c %d %d\n",ch,nr,get_sum(nr));
			if(ch == '*')
                                printf("%c %d %d\n",ch,nr,get_prod(nr));
		}
		close(fd[0]);
		exit(0);
	}
	close(fd[0]);
	close(fd[0]);
	FILE* f=fopen(argv[1],"r");
	int nr;
	char ch;
	while(fscanf(f," %c %d",&ch,&nr)==2){
		write(fd[1],&ch,sizeof(char));
		write(fd[1],&nr,sizeof(int));
		sleep(1);
	}
	write(fd[1],"s",sizeof(char));
	fclose(f);
	close(fd[1]);
	wait(NULL);
	return 0;
}
