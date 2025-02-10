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
    char msg = 0;
    sendto(c, &msg, 1, 0, (struct sockaddr*) &server, l);

    //urmeaza sa mi trimita el ceva, poate un port sau cv
    return 0;
}
