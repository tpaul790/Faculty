#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>

int main() {
    int s;
    struct sockaddr_in server, client;
    uint16_t n,rez=0;
    char sir[1024];

    s = socket(AF_INET,SOCK_DGRAM,0);
    if(s<0){
        printf("Eroare la crearea socketului server\n");
        exit(0);
    }

    memset(&server,0,sizeof(server));
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = INADDR_ANY;

    if(bind(s,(struct sockaddr*) &server, sizeof(server))<0){
        printf("Eroare la bind\n");
        exit(0);
    }

    unsigned int l = sizeof(client);
    memset(&client,0,l);

    while(1) {
        char msg;
        recvfrom(s, &msg, 1, 0, (struct sockaddr*) &client, &l);
        printf("S-a conectat un client!\n");

        uint16_t port = 1235;
        server.sin_port = htons(port);
        if(fork() == 0){
            int s1 = socket(AF_INET, SOCK_DGRAM, 0);
            while(bind(s1, (struct sockaddr*) &server, l)<0){
                port ++;
                server.sin_port = htons(port);
            }
            //trimit portul
            port = htons(port);
            sendto(s1, &port, 2, 0, (struct sockaddr*) &client, l);

            //primesc lungimea sirului si sirul propriu zis de lungime n
            recvfrom(s1, &n, 2, MSG_WAITALL, (struct sockaddr*) &client, &l);
            n = ntohs(n);
            recvfrom(s1, sir, n, MSG_WAITALL, (struct sockaddr*) &client, &l);

            //calculez numarul de caractere spatiu
            for(int i=0; i<n; i++)
                if(sir[i] == ' ')
                    rez++;

            rez = htons(rez);
            sendto(s1, &rez, 2, 0, (struct sockaddr*) &client, l);
        }
    }
    return 0;
}
