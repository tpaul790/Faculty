#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <wait.h>
// Să se scrie un program C care creează un proces copil cu care comunică prin pipe.
// Procesul părinte citeşte de la tastatură un număr natural şi îl trimite prin pipe
// procesului copil, iar procesul copil verifică şi afişează dacă acest număr este prim;

int prim(int n){
	if(n<2)
		return 0;
	if(n!=2 && n%2==0)
		return 0;
	for(int i=3;i*i<=n;i++)
		if(n%i==0)
			return 0;
	return 1;
}

int main(int argc,char** argv){
	int fd[2];
	int res=pipe(fd);
	if(res==-1){
		perror("pipe()");
		exit(0);
	}
	int pid=fork();
	if(pid==-1){
		perror("fork()");
		exit(0);
	}
	if(pid==0){
	//in copil citesc un numar din pipe si afisez daca acesta este sau nu prim
		close(fd[1]);
		int nr;
		while(1){
			read(fd[0],&nr,sizeof(int));
			if(nr==0)
				break;
			printf("[IN CHILD]\t Am citit din pipe numarul %d\n",nr);
			if(prim(nr))
				printf("[IN CHILD]\t Numarul %d este prim!\n",nr);
			else
				printf("[IN CHILD]\t Numarul %d nu este prim!\n",nr);
		}
		close(fd[0]);
		exit(0);
	}
	//in parinte citesc numere de la tastatura pana se introduce 0 si le scriu in pipe
	close(fd[0]);
	int nr;
	while(1){
		printf("Introduceti un numar: ");
		scanf("%d",&nr);
		write(fd[1],&nr,sizeof(int));
		if(nr==0)
			break;
		printf("[IN PARENT]\t Am scris in pipe numarul %d\n",nr);
		sleep(1);
	}
	int status;
	wait(&status);
	printf("[IN PARENT]\t Copilul si-a terminat executia cu codul %d\n",status);
	return 0;
}

