#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <wait.h>

int main(int argc, char* argv[]){
	int p_c1[2],c1_c2[2],c2_p[2];
	int res=pipe(p_c1);
	if(res == -1){
		perror("pipe1(): ");
		exit(0);
	}
	res=pipe(c1_c2);
        if(res == -1){
                perror("pipe2(): ");
                exit(0);
        }
	res=pipe(c2_p);
        if(res == -1){
                perror("pipe3(): ");
                exit(0);
        }
	int pid=fork();
	if(pid==-1){
		perror("fork(): ");
		exit(0);
	}
	if(pid == 0){
	//proces B || child 1
		close(p_c1[1]);
		close(c1_c2[0]);
		close(c2_p[0]);
		close(c2_p[1]);
		int n;
		read(p_c1[0],&n,sizeof(int));
		printf("[B]\t Am citit n=%d\n",n);
		write(c1_c2[1],&n,sizeof(int));
		int rdm=random()%2+3;
		for(int i=0;i<n;i++){
			int x;
			read(p_c1[0],&x,sizeof(int));
			x+=rdm;
			write(c1_c2[1],&x,sizeof(int));
			printf("[B]\t Am citit %d si am scris %d\n",x-rdm,x);
		}
		close(p_c1[0]);
		close(c1_c2[1]);
		exit(0);
	}
	pid=fork();
        if(pid==-1){
                perror("fork(): ");
                exit(0);
        }
        if(pid == 0){
        //proces C || child 2
		close(p_c1[0]);
		close(p_c1[1]);
		close(c1_c2[1]);
		close(c2_p[0]);
		int n;
		read(c1_c2[0],&n,sizeof(int));
		printf("[C]\t Am citit n=%d\n",n);
		int sum=0;
		for(int i=0;i<n;i++){
			int x;
			read(c1_c2[0],&x,sizeof(int));
			sum+=x;
			printf("[C]\t Am citit %d\n",x);
		}
		write(c2_p[1],&sum,sizeof(int));
		printf("[C]\t Am scris suma %d\n",sum);
		close(c1_c2[0]);
		close(c2_p[1]);
		exit(0);
        }
	//proces A sau parinte
	close(p_c1[0]);
	close(c1_c2[0]);
	close(c1_c2[1]);
	close(c2_p[1]);
	int n;
	printf("n= ");
	scanf("%d",&n);
	printf("[A]\t Am scris n=%d\n",n);
	write(p_c1[1],&n,sizeof(int));
	for(int i=0;i<n;i++){
		sleep(1);
		printf("numarul %d: ",i);
		int x;
		scanf("%d",&x);
		write(p_c1[1],&x,sizeof(int));
		printf("[A]\t Am trimis %d\n",x);
	}
	int sum;
	read(c2_p[0],&sum,sizeof(int));
	wait(NULL);
	wait(NULL);
	printf("[A]\t Suma este: %d\n",sum);
	close(p_c1[1]);
	close(c2_p[0]);
	return 0;
}











