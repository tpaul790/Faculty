#include <stdio.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <string.h>
#include <arpa/inet.h>
#include <time.h>
#include <unistd.h>

int main() {
    srand(time(NULL));
    uint16_t port1 = 1235;
    uint16_t port2 = 1235;
    int s,s1,s2;
    struct sockaddr_in server, client1, client2;

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

    //primesc juctorul 1
    char msg;
    recvfrom(s, &msg, 1, MSG_WAITALL, (struct sockaddr*) &client1, &l);
    printf("S-a conectat jucatorul 1!\n");

    //primesc jucatorul 2
    recvfrom(s, &msg, 1, MSG_WAITALL, (struct sockaddr*) &client2, &l);
    printf("S-a conectat jucatorul 2!\n");

    //creez cate un socket pentru fiecare jucator
    s1 = socket(AF_INET, SOCK_DGRAM, 0);
    s2 = socket(AF_INET, SOCK_DGRAM, 0);

    //gasesc 2 porturi valide si conectez s1 si s2 de cate un port
    server.sin_port = htons(port1);
    while(bind(s1, (struct sockaddr*) &server, l) < 0){
        port1++;
        server.sin_port = htons(port1);
    }
    server.sin_port = htons(port2);
    while(bind(s2, (struct sockaddr*) &server, l) < 0){
        port2++;
        server.sin_port = htons(port2);
    }

    //le trimit porturile clientilor ca sa trimita fiecare pe un alt port
    port1 = htons(port1);
    sendto(s, &port1, 2, 0, (struct sockaddr*) &client1, l);
    port2 = htons(port2);
    sendto(s, &port2, 2, 0, (struct sockaddr*) &client2, l);

    uint8_t status = 1;
    char litera = 'a' + (rand()%26);

    //pornesc jocul cu primul jucator, ii trimit status de continuare si o litera random
    sendto(s1, &status, 1, 0, (struct sockaddr*) &client1, l);
    sendto(s1, &litera, 1, 0, (struct sockaddr*) &client1, l);
    printf("Litera este: %c\n", litera);
    uint16_t n;
    char cuvant[101];

    while(1){

        recvfrom(s1, &n, 2, MSG_WAITALL, (struct  sockaddr*) &client1, &l);
        n = ntohs(n);
        recvfrom(s1, cuvant, n, MSG_WAITALL, (struct  sockaddr*) &client1, &l);

        //verific daca cuvantul primit e bun
        if(cuvant[0] != litera){
            //trimit statusuri de exit
            status = 0;
            sendto(s1, &status, 1, 0, (struct sockaddr*) &client1, l);
            sendto(s2, &status, 1, 0, (struct sockaddr*) &client2, l);

            //trimit statusuri castigator/pierzator
            status = 0;
            sendto(s1, &status, 1, 0, (struct sockaddr*) &client1, l);
            status = 1;
            sendto(s2, &status, 1, 0, (struct sockaddr*) &client2, l);
            break;
        }

        //daca cuvantul e bun atunci trimit litera la celalalt si status de continuare
        status = 1;
        sendto(s2, &status, 1, 0, (struct sockaddr*) &client2, l);
        litera = cuvant[n-1];
        sendto(s2, &litera, 1, 0, (struct sockaddr*) &client2, l);

        //primesc lungimea si cuvantul
        recvfrom(s2, &n, 2, MSG_WAITALL, (struct  sockaddr*) &client2, &l);
        n = ntohs(n);
        recvfrom(s2, cuvant, n, MSG_WAITALL, (struct  sockaddr*) &client2, &l);

        //verific daca cuvantul primit e bun
        if(cuvant[0] != litera){
            //trimit statusuri de exit
            status = 0;
            sendto(s1, &status, 1, 0, (struct sockaddr*) &client1, l);
            sendto(s2, &status, 1, 0, (struct sockaddr*) &client2, l);

            //trimit statusuri castigator/pierzator
            status = 1;
            sendto(s1, &status, 1, 0, (struct sockaddr*) &client1, l);
            status = 0;
            sendto(s2, &status, 1, 0, (struct sockaddr*) &client2, l);
            break;
        }

        //daca e bine atunci trimit status de continuare la jucator 1 si litera
        status = 1;
        sendto(s1, &status, 1, 0, (struct sockaddr*) &client1, l);
        litera = cuvant[n-1];
        sendto(s1, &litera, 1, 0, (struct sockaddr*) &client1, l);

    }
    printf("Jocul s-a terminat!\n");
    return 0;
}
