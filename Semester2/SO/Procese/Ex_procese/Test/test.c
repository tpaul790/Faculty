#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <wait.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

int main(int argc, char* argv[]){
	int fd[2];
	if(pipe(fd) == -1){
		perror("pipe(): ");
		exit(0);
	}
	int pid = fork();
	if(pid == 0){
	//proces B
		close(fd[1]);
		int n;
		char nume_fisier[21];
		read(fd[0],&n,sizeof(int));
		read(fd[0],nume_fisier,sizeof(char));
		int i = 1;
		char ch = nume_fisier[0];
		while(ch != '\0'){
			read(fd[0],nume_fisier+i,sizeof(char));
			ch=nume_fisier[i++];
		}

		printf("[B]\t N: %d\n",n);
		printf("[B]\t Fisier: %s\n",nume_fisier);

		sleep(1);
		char continut[201];
		char cpy[201];
		int l=1;
		read(fd[0],continut,sizeof(char));
		ch=continut[0];
		while(ch != '\0'){
			read(fd[0],continut+l,sizeof(char));
			ch=continut[l];
			l++;
		}
		strcpy(cpy,continut);
		printf("%s",continut);
		int cnt=0;
		char *cuv = strtok(continut," \n");
		while(cuv != NULL){
			if(strlen(cuv) == n){
				cnt++;
			}
			cuv=strtok(NULL," \n");
		}
		continut[strlen(continut)]='\0';
		printf("Cuvinte cu %d litere: %d\n",n,cnt);

		if(cnt % 2 == 0){
			char final[230];
			char cuvant[11];
			printf("Introduceti un cuvant de %d litere:",n);
			scanf("%s",cuvant);
			strcat(final,cuvant);
			strcat(final," ");

			strcat(final,cpy);

			FILE* fis = fopen(nume_fisier,"w");
			fprintf(fis,"%s",final);

			fclose(fis);
		}

		close(fd[0]);
		exit(0);
	}
	//proces A
	close(fd[0]);
	int n;
	char nume_fisier[21];
	printf("N: ");
	scanf("%d",&n);
	printf("Fisier: ");
	scanf("%s",nume_fisier);

	write(fd[1],&n,sizeof(int));
	write(fd[1],nume_fisier,sizeof(char)*(strlen(nume_fisier)+1));

	FILE* fisier = fopen(nume_fisier,"r");
	if(fisier == NULL){
		perror("fopen((): ");
		exit(0);
	}

	char ch;
	while(!feof(fisier)){
		ch=fgetc(fisier);
		if(ch != 255)
			write(fd[1],&ch,sizeof(char));
	}
	write(fd[1],"\0",sizeof(char));

	fclose(fisier);
	close(fd[1]);
	wait(0);
	return 0;
}







