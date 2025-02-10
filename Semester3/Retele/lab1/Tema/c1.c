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
#include <ctype.h>
#include <string.h>
#include "conect.h"

int number(char* str){
  if(*str == '-')
    str++;
  while(*str){
    if(!isdigit(*str))
      return 0;
    str++;
  }
  return 1;
}

int main(){
	int c;
	struct sockaddr_in server;
	int op = 1,n;
	
	
	printf("n = ");
	char ch[5];
	scanf("%s",ch);
	//validare lungime sir
	int rez = sscanf(ch,"%d",&n);
	if(rez != 1){
	  printf("Trebuie sa introduceti un numar natural!\n");
          exit(0);	
	}
	//validare sir
	char buf[1024];
	printf("Introduceti cele %d numere: ",n);
	scanf("%c", &ch[0]); //citesc \0 care a ramas de la primul scanf
	fgets(buf,sizeof(buf),stdin);
	buf[strcspn(buf, "\n")] = 0; // sterg new line daca exista
        char* token = strtok(buf," ");
        int sir[n];
        int k = -1;
        while(token != NULL){
          if(!number(token)){
            printf("S-a detectat ceva nenumeric in sir!\n");
            exit(0);
          }
          sir[++k] = atoi(token);
          token = strtok(NULL," ");
        }
        
        if(n!=k+1){
          printf("Numar invalid de numere!\n");
          exit(0);
        }
        
        //conectare la server
        conectare(&c,&server);

	//trimit optiunea
	op = htonl(op);
	send(c, &op, sizeof(int),0);
        
        //trimit datele valide la server
	for(int i=0;i<n;i++){
	  sir[i] = htonl(sir[i]);
	}
        
	n = htonl(n);
	send(c, &n, sizeof(int),0);
	n = ntohl(n);
	send(c, sir, sizeof(int)*n,0);
	int s;
	recv(c, &s, sizeof(int),MSG_WAITALL);
	s = ntohl(s);
	printf("Suma numerelor este: %d\n",s);
	close(c);
	return 0;
}





















