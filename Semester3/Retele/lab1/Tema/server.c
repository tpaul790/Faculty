#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/wait.h>

int s,c;
struct sockaddr_in server, client;



void problema1(){
  int n;
  recv(c, &n, sizeof(int), MSG_WAITALL);
  n = ntohl(n);
  int sir[n];
  recv(c, sir, sizeof(int)*n, MSG_WAITALL);
  int s = 0;
  for(int i=0;i<n;i++){
    sir[i] = ntohl(sir[i]);
    s = s+sir[i];
  }
  s = htonl(s);
  send(c, &s, sizeof(int),0);
}

void problema2(){
  int n;
  recv(c, &n, sizeof(int), MSG_WAITALL);
  n = ntohl(n);
  char sir[n];
  recv(c,sir,n,MSG_WAITALL);
  int cnt = 0;
  for(int i=0;i<n;i++){
    if(sir[i] == ' ')
      cnt++;
  }
  cnt = htonl(cnt);
  send(c, &cnt, sizeof(int),0);
}

void problema3(){
  int n;
  recv(c, &n, sizeof(int),MSG_WAITALL);
  n = ntohl(n);
  char sir[n];
  recv(c, sir, n, MSG_WAITALL);
  for(int i=0;i<n/2;i++){
    char st = sir[i];
    char dr = sir[n-i-1];
    sir[i] = dr;
    sir[n-i-1] = st;
  }
  send(c,sir,n,0);
}

void problema4(){
  int n;
  recv(c,&n,sizeof(int),MSG_WAITALL);
  n=ntohs(n);
  int div[n/2],k=0;
  for(int i=1;i*i<=n;i++){
    if(i*i==n){
      div[k++]=i;
    }
    else if(n%i == 0){
      div[k++] = i;
      div[k++] = n/i;
    }
  }
  k = htons(k);
  send(c,&k,sizeof(int),0);
  k = ntohs(k);
  for(int i=0;i<k;i++)
    div[i] = htons(div[i]);
  send(c,div,sizeof(int)*k,0);
}

void problema5(){
  int n,m;
  recv(c, &n, sizeof(int), MSG_WAITALL);
  recv(c, &m, sizeof(int), MSG_WAITALL);
  n = ntohs(n);
  m = ntohs(m);
  int sir1[n],sir2[m];
  recv(c, sir1, sizeof(int)*(n), MSG_WAITALL);
  recv(c, sir2, sizeof(int)*(m), MSG_WAITALL);
  for(int i=0;i<n;i++)
    sir1[i] = ntohs(sir1[i]);
  for(int i=0;i<m;i++)
    sir2[i] = ntohs(sir2[i]);
  int cnt = -1;
  int rez[n+m];
  for(int i=0;i<n;i++)
    for(int j=0;j<m;j++)
	if(sir1[i] == sir2[j])
	  rez[++cnt] = sir1[i];

  for(int i=0;i<=cnt;i++)
    rez[i] = htons(rez[i]);
  cnt = htons(cnt);
  send(c,&cnt,sizeof(int),0);
  cnt = ntohs(cnt);
  send(c,rez,sizeof(int)*(cnt+1),0);
}

int main(){
	socklen_t l;

	s = socket(AF_INET, SOCK_STREAM, 0);
	if(s<0){
		printf("Eroare la creareea socketului server\n");
		exit(0);
	}

	memset(&server,0,sizeof(server));
	server.sin_port = htons(1234);
	server.sin_family = AF_INET;
	server.sin_addr.s_addr = INADDR_ANY;

	if(bind(s, (struct sockaddr*) &server, sizeof(server))<0){
		printf("Eroare la rezervarea portului\n");
		exit(0);
	}

	listen(s,5);

	l = sizeof(client);
	memset(&client, 0, l);

	while(1){
		int op;
		c = accept(s,(struct sockaddr*) &client, &l);
		recv(c, &op, sizeof(int), MSG_WAITALL);
		op = ntohl(op);
		printf("S-a conectat c%d\n",op);
		if(op == 1){
		  if(fork() == 0){
		    problema1();
		  }
		}else if(op == 2){
		  if(fork() == 0){
		    problema2();
		  }
		}else if(op == 3){
		  if(fork() == 0){
		    problema3();
		  }
		}else if(op == 4){
		  if(fork() == 0){
		    problema4();
		  }
		}else if(op == 5){
		  if(fork() == 0){
		    problema5();
		  }
		}
		close(c);
	}
	return 0;
}





