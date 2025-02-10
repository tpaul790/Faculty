#include <stdio.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <string.h>

uint16_t cmmdc(uint16_t a, uint16_t b){
    while(b){
        int r = a%b;
        a=b;
        b=r;
    }
    return a;
}

int main() {
    int s;
    struct sockaddr_in server, client;

    s = socket(AF_INET, SOCK_DGRAM, 0);
    if(s < 0){
        printf("Eroare la crearea socketului server!\n");
        exit(0);
    }

    memset(&server, 0, sizeof(server));
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = INADDR_ANY;

    if(bind(s, (struct sockaddr*) &server, sizeof(server))<0){
        printf("Eroare la rezervarea portului!\n");
        exit(0);
    }

    unsigned int l = sizeof(client);
    memset(&client,0,l);

    uint16_t nr1,nr2;
    while(1){
        printf("Waiting for data...\n");
        recvfrom(s, &nr1, 2, MSG_WAITALL, (struct sockaddr*) &client, &l);
        recvfrom(s, &nr2, 2, MSG_WAITALL, (struct sockaddr*) &client, &l);
        nr1 = ntohs(nr1);
        nr2 = ntohs(nr2);

        uint16_t r1 = cmmdc(nr1,nr2);
        uint16_t r2 = (nr1/r1)*nr2;
        r1 = htons(r1);
        r2 = htons(r2);

        sendto(s, &r1, 2, 0, (struct sockaddr*) &client, l);
        sendto(s, &r2, 2, 0, (struct sockaddr*) &client, l);

    }
    return 0;
}
