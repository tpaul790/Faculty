#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <wait.h>
//Să se scrie un program C care creează un proces copil cu care comunică prin pipe.
// Procesul părinte citeşte de la tastatură un număr natural şi îl trimite prin pipe
// procesului copil, iar procesul copil verifică şi afişează
// dacă acest număr este par sau impar.
int main(int argc, char** argv){
	int fd[2];
	int res=pipe(fd);
	if(res==-1){
		perror("pipe() error: ");
		exit(EXIT_FAILURE);
	}
	int pid=fork();
	if(pid==-1){
		perror("fork() error: ");
		exit(EXIT_FAILURE);
	}
	if(pid==0){
	//in copil citesc din pipe si verific daca nr este par sau impar
		close(fd[1]);//inchid capatul de scriere
		while(1){
			int nr;
			read(fd[0],&nr,sizeof(int));
			if(nr<0)
				break;
			printf("[IN CHILD]\t Am citit din pipe numarul %d\n",nr);
			if(nr%2==0)
				printf("[IN CHILD}\t Numarul %d este par.\n",nr);
			else
				printf("[IN CHILD}\t Numarul %d este impar.\n",nr);
		}
		close(fd[0]);
		exit(EXIT_SUCCESS);
	}
	//in parinte citesc un numar de la tastatura si il scriu in pipe
	close(fd[0]);//inchid capatul pentru cititre
	int nr;
	while(1){
		printf("Introduceti un numar: ");
		scanf("%d",&nr);
		write(fd[1],&nr,sizeof(int));
		printf("[IN PARENT]\t Am scris in pipe numarul %d\n",nr);
		sleep(1);
		if(nr<0)
			break;
	}
	int status;
	wait(&status);
	printf("[IN PARINTE]\t Copilul si-a terminat executia cu codul %d\n",status);
	close(fd[1]);//inchid si capatul de scriere
	return 0;
}
