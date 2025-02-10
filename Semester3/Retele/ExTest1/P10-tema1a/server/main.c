#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>

char sir1[101],sir2[101];
uint16_t l1,l2;
int s,c;

void deservireClient() {
    //primesc lungimile celor 2 siruri
    recv(c, &l1, 2, MSG_WAITALL);
    l1 = ntohs(l1);
    recv(c, &l2, 2, MSG_WAITALL);
    l2 = ntohs(l2);

    //primesc cele 2 siruri
    recv(c, sir1, l1, MSG_WAITALL);
    recv(c, sir2, l2, MSG_WAITALL);

    uint16_t n,nrMax;
    char rez = '-';
    if(l1 < l2)
        n = l1;
    else
        n = l2;
    for(int i=0; i<n; i++){
        char ch = sir1[i];
        uint16_t cnt = 0;
        for(int j=0; j<n; j++)
            if(sir1[j] == ch && sir2[j] == ch)
                cnt++;
        if(cnt > nrMax){
            nrMax = cnt;
            rez = ch;
        }
    }
//anamariaa
//palmariaa
    send(c, &rez, 1, 0);
    nrMax = htons(nrMax);
    send(c, &nrMax, 2, 0);

}

int main() {
    struct sockaddr_in server, client;

    s = socket(AF_INET,SOCK_STREAM,0);
    if(s < 0){
        printf("Eroare la crearea socketului server\n");
        exit(0);
    }

    memset(&server,0,sizeof(server));
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = INADDR_ANY;

    if(bind(s, (struct sockaddr*) &server, sizeof(server))<0){
        printf("Eroare la rezervarea portului!\n");
        exit(0);
    }
    listen(s,5);

    unsigned int l = sizeof(client);
    memset(&client,0,l);
    while(1){
        c = accept(s, (struct sockaddr*) &client, &l);
        printf("S-a conectat un client\n");
        deservireClient();
    }
    return 0;
}

