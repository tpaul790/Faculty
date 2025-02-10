#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <wait.h>

int main(int argc,char* argv[]){
	int c2p[2],p2c[2];
	int res = pipe(c2p);
	if(res == -1){
		perror("pipe1(): ");
		exit(0);
	}
	res = pipe(p2c);
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
		int n;
		read(p2c[0],&n,sizeof(int));
		printf("[C]\t Am citit n=%d\n",n);
		float sum=0;
		for(int i=0;i<n;i++){
			int x;
			read(p2c[0],&x,sizeof(int));
			printf("[C]\t Am citit numarul %d\n",x);
			sum+=x;
		}
		float medie=sum/n;
		write(c2p[1],&medie,sizeof(float));
		close(p2c[0]);
		close(c2p[1]);
		exit(0);
	}
	//in parinte
	close(p2c[0]);
	close(c2p[1]);
	int n;
	printf("n= ");
	scanf("%d",&n);
	write(p2c[1],&n,sizeof(int));
	printf("[P]\t Am scris n=%d\n",n);
	for(int i=0;i<n;i++){
		sleep(1);
		int x=random()%10;
		write(p2c[1],&x,sizeof(int));
		printf("[P]\t Am scris numarul %d\n",x);
	}
	wait(NULL);
	float medie;
	read(c2p[0],&medie,sizeof(float));
	printf("[P]\t Media este %f\n",medie);
	close(p2c[1]);
	close(c2p[0]);
	return 0;
}
