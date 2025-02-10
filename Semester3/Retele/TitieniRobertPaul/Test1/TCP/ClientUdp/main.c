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
    uint16_t port = 1551;
    struct sockaddr_in server;

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

    //trimit ceva serverului ca sa ma vada
    char msg = 'a';
    sendto(c, &msg, 1, 0, (struct sockaddr*) &server, l);

    //urmeaza sa mi trimita el ceva, poate un port sau cv
    uint16_t max;
    uint8_t status;
    recvfrom(c, &status, 1, MSG_WAITALL, (struct sockaddr*) &server, &l);
    recvfrom(c, &max, 2, MSG_WAITALL, (struct sockaddr*) &server, &l);
    max = ntohs(max);
    if(status == 0){
        printf("Nu exista niciun maxim pentru ca serverul nu a primit siruri!\n");
        exit(0);
    }
    printf("Maximul este: %hu\n", max);
    return 0;
}
