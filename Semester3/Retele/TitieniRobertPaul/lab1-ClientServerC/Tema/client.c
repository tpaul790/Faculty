#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/ip.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <stdlib.h>

int main(){
	printf("Introduce numarul problemei: ");
	char c[10];
	scanf("%s",c);
	//validare
	int nrPr;
	int rez  = sscanf(c,"%d",&nrPr);
	if(rez == 0){
	  printf("Format invalid, trebuie sa introduci un numar de la 1 la 5!\n");
	  exit(0);
	}
	
	//executarea clientului cerut
	if(nrPr == 1){
	  if(system("./c1")<0)
	    printf("Eroare la rezolvarea problemei %d\n",nrPr);
	}else if(nrPr == 2){
	  if(system("./c2")<0)
	    printf("Eroare la rezolvarea problemei %d\n",nrPr);
	}else if(nrPr == 3){
	  if(system("./c3")<0)
	    printf("Eroare la rezolvarea problemei %d\n",nrPr);
	}else if(nrPr == 4){
	  if(system("./c4")<0)
	    printf("Eroare la rezolvarea problemei %d\n",nrPr);
	}else if(nrPr == 5){
	  if(system("./c5")<0)
	    printf("Eroare la rezolvarea problemei %d\n",nrPr);
	}else{
	    printf("Nu exista aceasta problema!\n");
	}
	return 0;
}
