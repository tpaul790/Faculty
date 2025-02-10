#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <wait.h>
#include <string.h>

int main(int argc, char* argv[]){
	int fd[2];
	int res = pipe(fd);
	if(res == -1){
		perror("pipe(): ");
		exit(0);
	}

	int pid=fork();
	if(pid == -1){
		perror("fork(): ");
		exit(0);
	}
	if(pid == 0){
	//in copil
		close(fd[1]);
		int n;
		char fisier[21];
		read(fd[0],&n,sizeof(int));
		read(fd[0],fisier,sizeof(fisier));
		printf("[B]\t N: %d\n",n);
		printf("[B]\t Fisier: %s\n",fisier);

		char continut[1001];
		read(fd[0],continut,1);
		char ch = continut[0];
		int l=1;
		while(ch != '\0'){
			read(fd[0],continut+l,1);
			ch=continut[l++];
		}
		printf("%s",continut);
		char* cuv = strtok(continut," \n");
		int cnt=0;
		while(cuv != NULL){
			if(strlen(cuv) == n)
				cnt++;
			cuv=strtok(NULL," \n");
		}

		printf("Cuvinte cu %d litere: %d\n",n,cnt);

		close(fd[0]);
		exit(0);
	}
	//in parinte
	close(fd[0]);
	int n;
	char fisier[21];
	printf("N: ");
	scanf("%d",&n);
	printf("Fisier: ");
	scanf("%s",fisier);
	write(fd[1],&n,sizeof(int));
	write(fd[1],fisier,sizeof(char)*(strlen(fisier)+1));

	char continut[1000];

	FILE* f = fopen(fisier,"r");

	char ch=fgetc(f);
	int i=1;
	while(!feof(f)){
		continut[i]=ch;
		ch=fgetc(f);
		i++;
	}
	continut[i]='\0';

	write(fd[1],continut,strlen(continut)+1);
	write(fd[1],"\0",1);

	fclose(f);
	close(fd[1]);
	wait(0);
	return 0;
}
