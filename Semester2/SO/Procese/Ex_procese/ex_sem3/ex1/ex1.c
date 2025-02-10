#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <wait.h>
#include <string.h>

int main(int argc, char* argv[]){
	char* a[10];
	a[0]="./egale";
	int n=0;
	while(1){
		char* x=(char*)malloc(10*sizeof(char));
		printf("Introduceti un numar(0=stop): ");
		scanf("%s",x);
		if(strcmp(x,"0") == 0){
			free(x);
			break;
		}
		a[++n]=x;
	}
	a[++n]=NULL;
	int pid=fork();
	if(pid == -1){
		perror("fork(): ");
		exit(0);
	}
	if(pid == 0){
		execv(a[0],a);
	}
	int status;
	wait(&status);
	int cod = WEXITSTATUS(status);
	if(cod == 0)
		printf("Toate numerele sunt egale!\n");
	else
		printf("Numerele nu sunt egale!\n");
	for(int i=1;i<n;i++)
		free(a[i]);
	return 0;
}
