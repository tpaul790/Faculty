//Să se scrie un program C care creează un proces copil cu care comunică prin pipe.
// Procesul părinte citeşte dintr-un fişier de tip text două numere întregi
// pe care le trimite prin pipe procesului copil, iar procesul copil returnează
// prin pipe suma lor.
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <wait.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

int main(int argc, char* argv[]){
	if(argc != 2){
		printf("Utilizare: %s input_file\n",argv[0]);
		exit(0);
	}
	FILE* f=fopen(argv[1],"r");
	if(f == NULL){
		perror("fopen(): ");
		exit(0);
	}

	int p2c[2];
	int c2p[2];
	int res=pipe(p2c);
	if(res == -1){
		perror("pipe1(): ");
		exit(0);
	}
	res=pipe(c2p);
	if(res == -1){
		perror("pipe2(): ");
		exit(0);
	}

	int pid=fork();
	if(pid == -1){
		perror("fork(): ");
		exit(0);
	}

	if(pid == 0){
	//in copil
		close(p2c[1]);
		close(c2p[0]);
		int a,b;
		read(p2c[0],&a,sizeof(int));
		printf("[IN CHILD]\t Am citit a=%d\n",a);

		read(p2c[0],&b,sizeof(int));
                printf("[IN CHILD]\t Am citit b=%d\n",b);

		int sum=a+b;
		write(c2p[1],&sum,sizeof(int));
		printf("[IN CHILD]\t Am scris sum=%d\n",sum);
		close(p2c[0]);
		close(c2p[1]);
		exit(0);
	}
	//in parite
	int a,b;
	fscanf(f,"%d %d",&a,&b);
	printf("[IN PARENT]\t Am citit din fisier a=%d si b=%d\n",a,b);

	close(p2c[0]);
	close(c2p[1]);

	write(p2c[1],&a,sizeof(int));
	printf("[IN PARENT]\t Am scris a=%d\n",a);

	sleep(1);

	write(p2c[1],&b,sizeof(int));
        printf("[IN PARENT]\t Am scris b=%d\n",b);

	sleep(1);

	int sum;
	read(c2p[0],&sum,sizeof(int));
	printf("[IN PARENT]\t Am citit sum=%d\n",sum);

	int status;
	wait(&status);
	printf("[IN PARENT]\t Copilul si-a terminat executia cu codul: %d\n",status);
	close(p2c[1]);
	close(c2p[0]);
	return 0;
}












