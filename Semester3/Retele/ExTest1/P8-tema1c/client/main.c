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
    uint16_t nr1,nr2;

    c = socket(AF_INET,SOCK_DGRAM,0);
    if(c < 0){
        printf("Eroare la crearea socketului client!\n");
        exit(0);
    }

    memset(&server, 0, sizeof(server));
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr("127.0.0.1");

    printf("Introduceti primul numar: ");
    char sir1[5];
    scanf("%s",sir1);

    if(sscanf(sir1,"%hu",&nr1) != 1){
        printf("Trebuie sa introduci un numar!\n");
        exit(0);
    }

    printf("Introduceti al doilea numar: ");
    char sir2[5];
    scanf("%s",sir2);
    if(sscanf(sir2,"%hu",&nr2) != 1){
        printf("Trebuie sa introduci un numar!\n");
        exit(0);
    }

    nr1 = htons(nr1);
    nr2 = htons(nr2);
    sendto(c, &nr1, 2, 0, (struct sockaddr*) &server, sizeof(server));
    sendto(c, &nr2, 2, 0, (struct sockaddr*) &server, sizeof(server));

    uint16_t r1,r2;
    uint32_t l = sizeof(server);
    recvfrom(c, &r1, 2, MSG_WAITALL, (struct sockaddr*) &server, &l);
    recvfrom(c, &r2, 2, MSG_WAITALL, (struct sockaddr*) &server, &l);
    r1 = ntohs(r1);
    r2 = ntohs(r2);
    nr1 = ntohs(nr1);
    nr2 = ntohs(nr2);

    printf("cmmdc(%hu,%hu) = %hu iar cmmmc(%hu,%hu) = %hu\n",nr1,nr2,r1,nr1,nr2,r2);
    return 0;
}
