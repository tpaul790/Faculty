#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <arpa/inet.h>

int main() {
    int c;
    struct  sockaddr_in server;
    uint32_t l = sizeof(server);

    c = socket(AF_INET, SOCK_STREAM, 0);
    if(c < 0){
        printf("Eroare la crearea socketului client!\n");
        exit(0);
    }

    memset(&server, 0, l);
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr("127.0.0.1");

    if(connect(c, (struct sockaddr*) &server, l) < 0){
        printf("Eroare de conectare la server!\n");
        exit(0);
    }

    uint16_t nr[101],n;
    printf("n= ");
    scanf("%hu", &n);

    printf("Introduceti cele %d numare: ", n);
    for(int i=0;i<n;i++)
        scanf("%hu", &nr[i]);

    //trimit lungimea si sirul la server
    n = htons(n);
    send(c, &n, 2, 0);
    n = ntohs(n);
    for(int i=0;i<n;i++){
        nr[i] = htons(nr[i]);
        send(c, &nr[i], 2, 0);
    }

    return 0;
}
