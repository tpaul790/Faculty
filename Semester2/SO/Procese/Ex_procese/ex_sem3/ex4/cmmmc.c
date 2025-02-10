#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>

int cmmdc(int a, int b){
	while(b){
		int r=a%b;
		a=b;
		b=r;
	}
	return a;
}

int cmmmc(int a,int b){
	return a/cmmdc(a,b)*b;
}

int main(int argc, char* argv[]){
	FILE* fd = fopen(argv[3],"w");
	int a = atoi(argv[1]);
	int b = atoi(argv[2]);
	int m = cmmmc(a,b);
	fprintf(fd,"%d",m);
	return 0;
}
