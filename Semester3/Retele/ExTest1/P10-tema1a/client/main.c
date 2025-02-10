
#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <stdlib.h>
#include <arpa/inet.h>

int main() {
    int c;
    struct sockaddr_in server;
    char sir1[101],sir2[101];

    c = socket(AF_INET,SOCK_STREAM,0);
    if(c < 0){
        printf("Eroare la crearea socket ului client\n");
        exit(0);
    }

    memset(&server,0,sizeof(server));
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr("127.0.0.1");

    if(connect(c,(struct sockaddr*) &server, sizeof(server))<0){
        printf("Eroare de conectare la server!\n");
        exit(0);
    }

    printf("Introduceti primul sir: ");
    fgets(sir1,101,stdin);
    uint16_t l1 = strlen(sir1);
    sir1[l1-1] = 0;

    printf("Introduceti cel de-al doilea sir: ");
    fgets(sir2,101,stdin);
    uint16_t l2 = strlen(sir2);
    sir2[l2-1] = 0;

    //trimit lungimile celor doua siruri
    l1 = htons(l1);
    l2 = htons(l2);
    send(c, &l1, 2, 0);
    send(c, &l2, 2, 0);

    //trimit cele 2 siruri
    l1 = ntohs(l1);
    l2 = ntohs(l2);
    send(c, sir1, l1, 0);
    send(c, sir2, l2, 0);

    char rez;
    recv(c, &rez, 1, MSG_WAITALL);

    uint16_t poz;
    recv(c, &poz, 2, MSG_WAITALL);
    poz = ntohs(poz);

    printf("'%c' apare pe %hu pozitii\n",rez,poz);
    return 0;
}

