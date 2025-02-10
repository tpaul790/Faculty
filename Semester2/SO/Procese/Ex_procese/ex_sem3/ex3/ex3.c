#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <wait.h>

int main(int argc, char* argv[]){
	char* numere[12];
	numere[0]="./prod";
	int n=0;
	while(1){
		char* x=(char*)malloc(3*sizeof(char));
		printf("Introduceti un numar('s'=stop): ");
		scanf("%s",x);
		if(strcmp(x,"s")==0){
			free(x);
			break;
		}
		numere[++n]=x;
	}
	numere[++n]=NULL;
	int pid = fork();
	if(pid == -1){
		perror("fork(): ");
		exit(0);
	}
	if(pid == 0){
		execv("./prod",numere);
	}
	int status;
	wait(&status);
	int cod = WEXITSTATUS(status);
	if(cod == 0)
		printf("Produsul numerelor este pozitiv!\n");
	else if(cod == 1)
		printf("Produsul numerelor este negativ!\n");
	else
		printf("Produsul este nul!\n");
	for(int i=1;i<n;i++)
		free(numere[i]);
	return 0;
}
