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
	int c;
	struct sockaddr_in server;
	int n,m;

	c = socket(AF_INET, SOCK_STREAM,0);
	if(c<0){
		printf("Eroare la crearea socketului client\n");
		exit(0);
	}

	memset(&server, 0, sizeof(server));
	server.sin_port = htons(1234);
	server.sin_family = AF_INET;
	server.sin_addr.s_addr = inet_addr("127.0.0.1");

	if(connect(c, (struct sockaddr*) &server, sizeof(server))<0){
		printf("Eroare de conectarea la server\n");
		exit(0);
	}

	printf("Introduceti lungimea primului sir: ");
	scanf("%d",&n);
	int sir1[n];
	printf("Introduceti cele %d numere: ",n);
	for(int i=0;i<n;i++)
		scanf("%d",&sir1[i]);

	printf("Introduceti lungimea celui de-al 2 lea sir: ");
        scanf("%d",&m);
        int sir2[m];
        printf("Introduceti cele %d numere: ",m);
        for(int i=0;i<m;i++)
                scanf("%d",&sir2[i]);

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






