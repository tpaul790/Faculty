#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>

int main(int argc,char* argv[]){
	int p=1;
	for(int i=1;i<argc;i++){
		int x=atoi(argv[i]);
		if(x == 0)
			exit(2);
		p*=x;
	}
	if(p > 0)
		exit(0);
	exit(1);
}
