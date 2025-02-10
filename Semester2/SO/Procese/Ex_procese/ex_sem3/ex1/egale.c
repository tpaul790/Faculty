#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>

int main(int argc, char* argv[]){
	int first=atoi(argv[1]);
	for(int i=2;i<argc;i++)
		if(first != atoi(argv[i]))
			exit(1);
	exit(0);
}
