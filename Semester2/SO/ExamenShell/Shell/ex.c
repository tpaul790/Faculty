#include <stdio.h>
#include <unistd.h>
#include <wait.h>
#include <stdlib.h>
#include <sys/types.h>
       #include <sys/stat.h>
       #include <fcntl.h>

void f(int n){
	if(n>0 || fork()==0){
		f(n-1);
		exit(0);
	}
	wait(0);
}

int main(int argc, char* argv[]){
	int fd = open("b.txt",O_RDONLY | O_CREAT);
	dup2(fd,0);
	execlp("cat","cat",NULL);
	return 0;
}
