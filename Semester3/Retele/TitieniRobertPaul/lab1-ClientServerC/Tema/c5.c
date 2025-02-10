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
#include "conect.h"
#include <ctype.h>

//problema 8 de pe site
int number(char* sir){
  if(*sir == '-')
    sir++;
  while(*sir){
    if(!isdigit(*sir))
      return 0;
    sir++;
  }
  return 1;
}

int main(){
	int c;
	struct sockaddr_in server;
	int n,m;
        
        //citire si validare
        char l1[5],l2[5];
	printf("Introduceti lungimea primului sir: ");
	scanf("%s",l1);
	//validare lungime sir1
	if(sscanf(l1,"%d",&n) != 1){
	  printf("Trebuie sa introduceti un numar natural\n");
	  exit(0);
	}
	scanf("%c",l1);
	int sir1[n],k1=0;
	//validare sir1
	printf("Introduceti cele %d numere: ",n);
	char str1[1024];
	fgets(str1,1024,stdin);
	str1[strcspn(str1, "\n")] = 0;
	char* token1 = strtok(str1," ");
	while(token1 != NULL){
	  if(!number(token1)){
	    printf("S-a detectat ceva nenumeric in sir1\n");
	    exit(0);
	  }
	  sir1[k1++] = atoi(token1);
	  token1 = strtok(NULL," ");
	}
	if(n != k1){
	  printf("Numar invalid de numere!\n");
	  exit(0);
	}

        //validare sir2
	printf("Introduceti lungimea celui de-al 2 lea sir: ");
        scanf("%s",l2);
        if(sscanf(l2,"%d",&m) != 1){
          printf("Trebuie sa introduceti un numar natural\n");
          exit(0);
        }
        scanf("%c",l2);//citesc /0 ramas de la scanf ul trecut
        int sir2[m],k2=0;
        printf("Introduceti cele %d numere: ",m);
        char str2[1024];
        fgets(str2,1024,stdin);
        str2[strcspn(str2,"\n")] = 0;
        char* token2 = strtok(str2," ");
        while(token2 != NULL){
          if(!number(token2)){
            printf("S-a detectat ceva nenumeric in sir2\n");
            exit(0);
          }
          sir2[k2++] = atoi(token2);
          token2 = strtok(NULL," ");
        }
        if(m != k2){
	  printf("Numar invalid de numere!\n");
	  exit(0);
	}
        
        conectare(&c,&server);
        //trimit numarul problemei
        int op = 5;
        op = htons(op);
        send(c,&op,sizeof(int),0);

	n = htons(n);
	send(c, &n, sizeof(int),0);
	m = htons(m);
	send(c, &m, sizeof(int),0);
	n = ntohs(n);
	m = ntohs(m);
	for(int i=0;i<n;i++)
		sir1[i] = htons(sir1[i]);
	for(int i=0;i<m;i++)
		sir2[i] = htons(sir2[i]);
	send(c,sir1,sizeof(int)*(n),0);
	send(c,sir2,sizeof(int)*(m),0);

	int cnt;
	recv(c,&cnt,sizeof(int),MSG_WAITALL);
	cnt = ntohs(cnt);
	if(cnt == 0)
		printf("Exista un unic numar comun, acesta este: ");
	else
		printf("Exista %d numere comune, acestea sunt: ",cnt+1);
	int rez[cnt+1];
	recv(c,rez,sizeof(int)*(cnt+1),MSG_WAITALL);
	for(int i=0;i<=cnt;i++){
		rez[i] = ntohs(rez[i]);
		printf("%d ",rez[i]);
	}
	printf("\n");
	close(c);
	return 0;
}





