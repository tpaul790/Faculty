#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <wait.h>

int main(int argc, char* argv[]){
	int c2p[2];
	int p2c[2];
	int res=pipe(c2p);
	if(res==-1){
		perror("pipe(c2p) error: ");
		exit(EXIT_FAILURE);
	}
	res=pipe(p2c);
        if(res==-1){
                perror("pipe(p2c) error: ");
                exit(EXIT_FAILURE);
        }
	int pid=fork();
	if(pid==-1){
		perror("fork() error: ");
		exit(EXIT_FAILURE);
	}
	if(pid==0){
		//inchid capetele pe care nu le folosesc
		close(p2c[1]);//din p2c citesc ce imi scrie parintele
		close(c2p[0]);//in c2p scriu rezultatul
		while(1){
			int a,b;
			read(p2c[0],&a,sizeof(int));
			if(a==0)
				break;
			read(p2c[0],&b,sizeof(int));
			int sum=a+b;
			printf("[IN CHILD]\t a=%d,b=%d,a+b=%d\n",a,b,sum);
			write(c2p[1],&sum,sizeof(int));
		}
		close(p2c[0]);
		close(c2p[1]);
		exit(EXIT_SUCCESS);
	}
	close(p2c[0]);
	close(c2p[1]);
	while(1){
		int a,b;
		printf("[IN PARENT]\t a=");
		scanf("%d",&a);
		write(p2c[1],&a,sizeof(int));
		if(a==0)
			break;
		printf("[IN PARENT]\t b=");
                scanf("%d",&b);
                write(p2c[1],&b,sizeof(int));
		int sum;
		read(c2p[0],&sum,sizeof(int));
		printf("[IN PARENT]\t Suma este: %d\n",sum);
	}
	int status;
	wait(&status);
	printf("[IN PARENT]\t Child finished his job with exit code %d\n",status);
	close(p2c[1]);
	close(c2p[0]);
	return 0;
}


