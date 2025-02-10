#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <wait.h>

int main(int argc, char* argv[]){
	int c1c2[2],c2c1[2];
	int res = pipe(c1c2);
	if(res == -1){
		perror("pipe1(): ");
		exit(0);
	}
	res = pipe(c2c1);
	if(res == -1){
		perror("pipe2(): ");
		exit(0);
	}
	int pid=fork();
	if(pid == -1){
		perror("fork1(): ");
		exit(0);
	}
	if(pid == 0){
	//c1
		close(c1c2[0]);
		close(c2c1[1]);
		srandom(getpid());
		int nr=random()%10+1;
		write(c1c2[1],&nr,sizeof(int));
		printf("[C1]\t Am inceput cu numarul %d\n",nr);
		if(nr != 10){
			while(1){
				read(c2c1[0],&nr,sizeof(int));
				printf("[C1]\t Am citit numarul %d\n",nr);
				if(nr == 10)
					break;
				nr=random()%10+1;
				write(c1c2[1],&nr,sizeof(int));
				printf("[C1]\t Am trimis numarul %d\n",nr);
				if(nr == 10)
					break;
			}
		}
		close(c1c2[1]);
		close(c2c1[0]);
		exit(0);
	}

	pid = fork();
	if(pid == -1){
		perror("fork2(): ");
		exit(0);
	}
	if(pid == 0){
	//c2
		close(c1c2[1]);
		close(c2c1[0]);
		int nr;
		srandom(getpid());
		while(1){
			read(c1c2[0],&nr,sizeof(int));
			printf("[C2]\t Am citit numarul %d\n",nr);
			if(nr == 10)
				break;
			nr=random()%10+1;
			write(c2c1[1],&nr,sizeof(int));
			printf("[C2]\t Am scris numarul %d\n",nr);
			if(nr == 10)
				break;
		}
		close(c1c2[0]);
		close(c2c1[1]);
		exit(0);
	}
	close(c1c2[0]);
	close(c1c2[1]);
	close(c2c1[0]);
	close(c2c1[1]);
	wait(NULL);
	wait(NULL);
	printf("[P]\t Ambii copii si-au terminat executia!\n");
	return 0;
}
