#include <stdio.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <string.h>
#include <arpa/inet.h>
#include <time.h>
#include <unistd.h>

int main() {
    int s;
    struct sockaddr_in server, client;

    //pe socketul asta primesc mesajele de conectare
    s = socket(AF_INET, SOCK_DGRAM, 0);
    if(s < 0){
        printf("Eroare la crearea socketului server!\n");
        exit(0);
    }

    uint32_t l = sizeof(server);
    memset(&server, 0, l);
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = INADDR_ANY;

    //leg socketul s de portul 1234
    if(bind(s, (struct sockaddr*) &server, l) < 0){
        printf("Eroare la bind!\n");
        exit(0);
    }

    //dau un recvfrom ca sa se conecteze un client, in while daca vin mai multi sau aici daca e un numar fix
    char msg;
    recvfrom(s, &msg, 1, MSG_WAITALL, (struct sockaddr*) &client, &l); // s-a completat structura clientului si cu ea fac send uri
    return 0;
}
