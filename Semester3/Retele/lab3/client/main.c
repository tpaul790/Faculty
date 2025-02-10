#include <stdio.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <stdlib.h>
#include <arpa/inet.h>

int main(int count,char* args[]) {
    char* serverAddr = args[1];
    uint16_t firstPort = atoi(args[2]);
    struct sockaddr_in server;
    int c;
    uint16_t rez;
    char sir[1001];

    c = socket(AF_INET,SOCK_DGRAM,0);
    if(c<0){
        printf("Eroare la crearea socketului client\n");
        exit(0);
    }

    memset(&server,0,sizeof(server));
    server.sin_port = htons(firstPort);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr(serverAddr);

    //trimit un caracter serverului pentru ca el sa mi poata trimite portul
    unsigned int l = sizeof(server);
    char ch = 'a';
    sendto(c, &ch, sizeof(ch), 0, (struct sockaddr*) &server, l);

    printf("Introduceti sirul: ");
    fgets(sir,1024,stdin);
    sir[strlen(sir)-1] = 0;

    uint16_t port = 0;
    recvfrom(c, &port, sizeof(port), MSG_WAITALL, (struct sockaddr*) &server, &l);

    port = htons(port);
    printf("Portul este: %hu\n",port);

    //ma leg de portul primit
    server.sin_port = ntohs(port);

    uint16_t n = (uint16_t)strlen(sir);
    n = htons(n);
    sendto(c, &n, sizeof(n), 0, (struct sockaddr*) &server, sizeof(server));
    n = ntohs(n);
    sendto(c, sir, n, 0, (struct sockaddr*) &server, sizeof(server));

    recvfrom(c, &rez, sizeof(rez), MSG_WAITALL, (struct sockaddr*) &server, &l);
    rez = ntohs(rez);

    printf("Numarul de caractere spatiu este: %d\n",rez);
    return 0;
}
