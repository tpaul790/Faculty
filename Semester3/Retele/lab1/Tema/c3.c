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

int main(){
  int c;
  struct sockaddr_in server;
  
  conectare(&c,&server);
  
  //trimit la server numarul problemei de rezolvat
  int op = 3;
  op = htons(op);
  send(c, &op, sizeof(int),0);
  
  //citesc sirul si il trimit la server
  char buf[1024];
  printf("Introduceti sirul: ");
  fgets(buf,1024,stdin);
  buf[strcspn(buf,"\n")] = 0;
  int n = strlen(buf);
  n = htons(n);
  send(c, &n, sizeof(int),0);
  n = ntohs(n);
  buf[n] = '\0';
  send(c,buf,n,0);
  
  //primesc sirul oglindit din server
  recv(c,buf,n,MSG_WAITALL);
  printf("Sirul oglindit este: %s\n", buf);
  return 0;
}
