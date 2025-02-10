#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>

int main(){
	int s,c;
	struct sockaddr_in server, client;
	socklen_t l;

	s = socket(AF_INET,SOCK_STREAM,0);
	if(s<0){
		printf("Eroare la crearea socketului server\n");
		exit(0);
	}
	memset(&server,0,sizeof(server));
	server.sin_port = htons(1234);
	server.sin_family = AF_INET;
	server.sin_addr.s_addr = INADDR_ANY;

	if(bind(s, (struct sockaddr *) &server, sizeof(server))<0){
		printf("Eroare la rezervarea portului\n");
		exit(0);
	}

	listen(s,1);

	l = sizeof(client);
	memset(&client, 0, l);

	while(1){
		int n,m;
		c = accept(s, (struct sockaddr*) &client, &l);
		printf("S-a conectat un client.\n");

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
		close(c);
	}
	return 0;
}






