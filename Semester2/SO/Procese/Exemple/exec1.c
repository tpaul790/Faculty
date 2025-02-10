#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

int main(int argc, char* argv[]){
	//execlp("ls","ls","-l","-t","-r",NULL);
	char* v[]={"ls","-l","-t",NULL};
	execvp("ls",v);
	return 0;
}
