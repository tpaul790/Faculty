#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <wait.h>
#include <string.h>

int main(int argc, char* argv[]){
	char* numere[11];
	int n=-1;
	while(1){
		char* x = (char*) malloc(11*sizeof(char));
		printf("Introduceti un numar (s=stop): ");
		scanf("%s",x);
		if(strcmp(x,"s")==0){
			free(x);
			break;
		}
		numere[++n] = x;
	}
	char* first=numere[0];
	for(int i=1;i<=n;i++){
		char* second=numere[i];
		if(fork()==0){
			execl("./cmmmc","./cmmmc",first,second,"fisier",NULL);
			exit(0);
		}
		wait(NULL);
		FILE* fd=fopen("fisier","r");
		fscanf(fd,"%s",first);
		fclose(fd);
	}
	printf("Cel mai mic multiplu comun al tuturor numerelor este: %s\n",first);
}
