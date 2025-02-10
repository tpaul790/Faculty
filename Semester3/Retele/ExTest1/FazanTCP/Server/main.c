#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>

int main() {
    srand(time(NULL));
    int s,c1,c2;
    struct sockaddr_in server, client1, client2;
    uint32_t l = sizeof(server);

    s = socket(AF_INET, SOCK_STREAM, 0);
    if(s < 0){
        printf("Eroare la crearea socketului server!\n");
        exit(0);
    }

    memset(&server, 0, l);
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = INADDR_ANY;

    if(bind(s, (struct sockaddr*) &server, l)<0){
        printf("Eroare la rezervarea portului!\n");
        exit(0);
    }

    listen(s,5);

    c1 = accept(s, (struct sockaddr*) &client1, &l);
    printf("S-a conectat primul jucator!\n");
    c2 = accept(s, (struct sockaddr*) &client2, &l);
    printf("S-a conectat al doilea jucator!\n");

    uint8_t status = 1;
    send(c1, &status, 1, 0);

    char litera = 'a'+(rand()%26);
    send(c1, &litera, 1, 0);

    char cuvant[101];
    uint16_t n;
    while(1){
        recv(c1, &n, 2, MSG_WAITALL);
        n = ntohs(n);
        recv(c1, cuvant, n, MSG_WAITALL);

        if(cuvant[0] != litera){
            status = 0;
            send(c1, &status, 1, 0);
            send(c2, &status, 1, 0);

            send(c1, &status, 1, 0);
            status = 1;
            send(c2, &status, 1, 0);
            break;
        }

        litera = cuvant[n-1];
        status = 1;
        send(c2, &status, 1, 0);
        send(c2, &litera, 1, 0);

        recv(c2, &n, 2, MSG_WAITALL);
        n = ntohs(n);
        recv(c2, cuvant, n, MSG_WAITALL);

        if(cuvant[0] != litera){
            status = 0;
            send(c1, &status, 1, 0);
            send(c2, &status, 1, 0);

            send(c2, &status, 1, 0);
            status = 1;
            send(c1, &status, 1, 0);
            break;
        }

        litera = cuvant[n-1];
        status = 1;
        send(c1, &status, 1, 0);
        send(c1, &litera, 1, 0);
    }
    printf("Jocul s-a terminat!\n");
    return 0;
}
