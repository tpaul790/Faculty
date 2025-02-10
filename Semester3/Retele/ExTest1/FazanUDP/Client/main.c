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
    uint16_t port = 1234;
    struct sockaddr_in server;
    char cuvant[101];

    c = socket(AF_INET,SOCK_DGRAM,0);
    if(c < 0){
        printf("Eroare la crearea socketului client!\n");
        exit(0);
    }

    uint32_t l = sizeof(server);
    memset(&server, 0, l);
    server.sin_port = htons(port);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr("127.0.0.1");

    //trimit ceva serverului ca sa mi trimita un port
    char msg = 0;
    sendto(c, &msg, 1, 0, (struct sockaddr*) &server, l);

    //primesc un port unic
    recvfrom(c, &port, 2, MSG_WAITALL, (struct sockaddr*) &server, &l);
    server.sin_port = port;
    port = ntohs(port);
    printf("Portul pe care m-am conectat este '%hu'\n",port);

    uint8_t status;
    while(1) {
        recvfrom(c, &status, 1, MSG_WAITALL, (struct sockaddr *) &server, &l);
        if(status == 0)
            break;
        char ch;
        recvfrom(c, &ch, 1, MSG_WAITALL, (struct sockaddr *) &server, &l);

        printf("Introdu un cuvant care incepe cu litera '%c': ", ch);
        scanf("%s",cuvant);

        uint16_t n = strlen(cuvant);
        n = htons(n);
        sendto(c, &n, 2, 0, (struct sockaddr*) &server, l);
        n = ntohs(n);
        sendto(c, cuvant, n, 0, (struct sockaddr*) &server, l);
    }

    recvfrom(c, &status, 1, MSG_WAITALL, (struct sockaddr*) &server, &l);
    if(status == 0)
        printf("Am pierdut...!\n");
    else
        printf("Am castigat!!\n");
    return 0;
}
