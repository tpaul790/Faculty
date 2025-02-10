#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <wait.h>

int main(int argc, char* argv[]){
	char* a[11];//aici pun sirurile pe care le citesc
	int n=-1;
	while(1){
		char* x=(char*)malloc(10*sizeof(char));
		printf("Introduceti un sir: ");
		scanf("%s",x);
		if(strcmp(x,"stop") == 0){
			free(x);
			break;
		}
		a[++n]=x;
	}
	for(int i=0;i<=n;i++){
		if(fork()==0){
			execl("./ogl","./ogl","text",a[i],NULL);
		}
	}//pentru fiecare sir citit, creez un copil care sa screi oglinditul in fisier
	int status;
	for(int i=0;i<=n;i++)
		wait(&status);//astept toti copilasii

	char sir[101];
	FILE* fd=fopen("text","r");
	fscanf(fd,"%s",sir);
	printf("Sirul obtinut prin concatenare este: %s\n",sir);

	for(int i=0;i<=n;i++)
		free(a[i]);//eliberez pointerii alocati dinamic
	return 0;
}
