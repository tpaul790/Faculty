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

//rezolvarea problemei 5 de pe site
int main(){
  int c;
  struct sockaddr_in server;
  
  //citesc si validez datele
  char ch[10];
  int n;
  printf("Introduceti un numar: ");
  scanf("%s", ch);
  if(sscanf(ch,"%d",&n) != 1){
    printf("Trebuie sa introduceti un numar natural\n");
    exit(0);
  }
  
  //conectare la server
  conectare(&c,&server);
  
  //trimit numarul problemei pe care sa o rezolve
  int op = 4;
  op = htons(op);
  send(c,&op,sizeof(int),0);
  
  //trimit numarul serverului
  n=htons(n);
  send(c, &n, sizeof(int), 0);
  n=ntohs(n);
  
  //preiau numarul de divizori si sirul divizorilor lui n
  int nrdiv;
  recv(c,&nrdiv,sizeof(int),MSG_WAITALL);
  nrdiv = ntohs(nrdiv);
  int div[nrdiv];
  recv(c,div,sizeof(int)*nrdiv,MSG_WAITALL);
  printf("%d are %d divizori, acestia sunt: ",n,nrdiv);
  for(int i=0;i<nrdiv;i++){
    div[i] = ntohs(div[i]);
    printf("%d ",div[i]);
  }
  printf("\n");
  return 0;
}
