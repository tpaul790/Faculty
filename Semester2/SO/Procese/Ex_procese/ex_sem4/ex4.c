#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <wait.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
//Să se scrie un program C care creează un proces copil cu care comunică prin pipe.
// Procesul părinte citeşte de la tastatură două numere întregi pe care le trimite
// prin pipe procesului copil, iar procesul copil returnează prin pipe suma lor.

int main(int argc, char* argv[]){
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
	//citeste din p2c numerele si screi in c2p suma lor
		close(p2c[1]);
		close(c2p[0]);
		while(1){
			int a,b;
			read(p2c[0],&a,sizeof(int));
			printf("[IN CHILD]\t Am citit din pipe a=%d\n",a);

			if(a == 0)
				break;

			read(p2c[0],&b,sizeof(int));
                	printf("[IN CHILD]\t Am citit din pipe b=%d\n",b);

			int sum=a+b;
			write(c2p[1],&sum,sizeof(int));
			printf("[IN CHILD]\t Am scris in pipe sum=%d\n",sum);
		}
		close(p2c[0]);
		close(c2p[1]);
		exit(0);
	}
	//in parinte citesc doua numere de la tastatura si le scriu in pipe
	// pe urma citesc din pipe suma lor si o afisez
	close(p2c[0]);
	close(c2p[1]);

	int a,b;
	while(1){
		printf("a=");
		scanf("%d",&a);
		write(p2c[1],&a,sizeof(int));
		printf("[IN PARENT]\t Am scris in pipe a=%d\n",a);

		if(a == 0)
			break;
		sleep(1);
		printf("b=");
		scanf("%d",&b);
		write(p2c[1],&b,sizeof(int));
		printf("[IN PARENT]\t Am scris in pipe b=%d\n",b);
		sleep(1);

		int sum;
		read(c2p[0],&sum,sizeof(int));
		printf("[IN PARENT]\t Am citit sum=%d\n",sum);
		sleep(1);
	}
	wait(NULL);
	close(p2c[1]);
	close(c2p[0]);
	return 0;
}






