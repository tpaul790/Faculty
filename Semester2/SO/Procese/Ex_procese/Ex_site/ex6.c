//Scrieti un program C care genereaza N numere intregi aleatoate 
//(N dat la linia de comanda). Apoi creeaza un proces fiu si ii trimite numerele prin pipe.
// Procesul fiu calculeaza media numerelor si trimite rezultatul inapoi parintelui.
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <wait.h>

int main(int argc, char** argv){
	if(argc != 2){
		printf("Numar insuficient de argumente!\n");
		printf("Utilizare: %s numar_natural\n",argv[0]);
		exit(0);
	}
	int n=atoi(argv[1]);
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
		float sum=0;
		int n;
		read(p2c[0],&n,sizeof(int));
		for(int i=0;i<n;i++){
			int x;
			read(p2c[0],&x,sizeof(int));
			sum+=x;
		}
		float medie=sum/n;
		printf("[copil]\t Am calculat media %f\n",medie);
		write(c2p[1],&medie,sizeof(float));
		close(p2c[0]);
		close(c2p[1]);
		exit(0);
	}
	//in parinte
	close(p2c[0]);
	close(c2p[1]);
	write(p2c[1],&n,sizeof(int));
	for(int i=0;i<n;i++){
		int x=random()%10;
		write(p2c[1],&x,sizeof(int));
		printf("[parinte]\t %d: Am scris numarul: %d\n",i,x);
	}
	float medie;
	read(c2p[0],&medie,sizeof(float));
        printf("[parinte]\t Am citit media=%f\n",medie);

	int status;
	wait(&status);
	printf("[parinte]\t Copilul si-a terminat executia cu codul %d\n",status);
	close(p2c[1]);
	close(c2p[0]);
	return 0;
}



















