#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <wait.h>

int main(int argc, char* argv[]){
	int p2c[2],c2p[2];
	int res = pipe(p2c);
	if(res == -1){
		perror("pipe1(): ");
		exit(0);
	}
	res = pipe(c2p);
	if(res == -1){
		perror("pipe2(): ");
		exit(0);
	}

	int pid = fork();
	if(pid == -1){
		perror("fork(): ");
		exit(0);
	}
	if(pid == 0){
	//in copil
		close(c2p[0]);
		close(p2c[1]);
		int nr;
		while(1){
			read(p2c[0],&nr,sizeof(int));
			printf("[C]\t Am citit numarul %d\n",nr);
			if(nr<5)
				break;
			nr+=nr%2;
			write(c2p[1],&nr,sizeof(int));
			printf("[C]\t Am scris numarul %d\n",nr);
		}
		close(c2p[1]);
		close(p2c[0]);
		exit(0);
	}
	//in parinte
	close(p2c[0]);
	close(c2p[1]);
	srandom(getpid());
	int nr = random()%151+50;
	write(p2c[1],&nr,sizeof(int));
	printf("[P]\t Am inceput cu numarul %d\n",nr);
	if(nr >= 5){
		while(1){
			read(c2p[0],&nr,sizeof(int));
			printf("[P]\t Am citit numarul %d\n",nr);
			if(nr < 5)
				break;
			nr=nr/2;
			write(p2c[1],&nr,sizeof(int));
			printf("[P]\t Am scris numarul %d\n",nr);
			if(nr < 5)
				break;
		}
	}
	close(p2c[1]);
	close(c2p[0]);
	wait(NULL);
	printf("[P]\t Aman2 ne-am terminat executia!\n");
	return 0;
}
