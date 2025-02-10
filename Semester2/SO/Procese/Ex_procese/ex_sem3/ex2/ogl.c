#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char* argv[]){
	FILE* fd= fopen(argv[1],"a");
	char ogl[11];
	int n=strlen(argv[2]);
	for(int i=0;i<n;i++)
		ogl[n-i-1]=argv[2][i];
	ogl[n]='\0';
	fprintf(fd,"%s",ogl);
	return 0;
}
