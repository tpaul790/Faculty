#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <fcntl.h>

int main() {
    int s1, s2, c;
    struct sockaddr_in server, client ,clientUdp;
    socklen_t l = sizeof(server);

    s1 = socket(AF_INET, SOCK_STREAM, 0);
    s2 = socket(AF_INET, SOCK_STREAM, 0);
    if (s1 < 0) {
        printf("Eroare la crearea socketului server!\n");
        exit(1);
    }

    memset(&server, 0, l);
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = INADDR_ANY;

    if (bind(s1, (struct sockaddr*)&server, l) < 0) {
        printf("Eroare la rezervarea portului1 Tcp!\n");
        exit(1);
    }

    server.sin_port = htons(1235);
    if(bind(s2, (struct sockaddr*) &server, l) < 0){
        printf("Eroare la rezervarea portului2 Tcp!\n");
        exit(1);
    }

    listen(s1, 5);
    listen(s2, 5);

    //creez socketul pe care asculta serverul conexiuni udp
    int sUdp = socket(AF_INET, SOCK_DGRAM, 0);
    server.sin_port = htons(1551);
    if(bind(sUdp, (struct sockaddr*) &server, l) < 0){
        printf("Eroare la crearea socketului pentru udp!\n");
        exit(0);
    }

    // Setez socket-ul UDP în modul neblocant
    int flags = fcntl(sUdp, F_GETFL, 0);
    fcntl(sUdp, F_SETFL, flags | O_NONBLOCK);

    memset(&client, 0, l);

    int mins[101], k = 0; // Array-ul pentru valorile minime primite de la copii
    char ch;

    while (1) {
        if(recvfrom(sUdp, &ch, 1, 0, (struct sockaddr*) &clientUdp, &l) > 0){
            printf("S-a conectat clientul UDP!\n");
            uint8_t status = 0;
            uint16_t max = 0;
            if(k > 0) {
                max = mins[0];
                for(int i=1;i<k;i++){
                    if (mins[i] > max){
                        max = mins[i];
                    }
                }
                status = 1;
            }
            //1 daca am numere si 0 daca nu am primit nimic
            sendto(sUdp, &status, 1, 0, (struct sockaddr*) &clientUdp, l);
            //trimit maximul
            max = htons(max);
            sendto(sUdp, &max, 2, 0, (struct sockaddr*) &clientUdp, l);
        }

        c = accept(s1, (struct sockaddr*)&client, &l);
        printf("S-a conectat un client TCP!\n");
        if (c < 0) {
            perror("Eroare la acceptare!");
            continue;
        }

        int fd[2];
        if (pipe(fd) == -1) {
            perror("Eroare la crearea pipe-ului!");
            close(c);
            continue;
        }

        if (fork() == 0) {
            // Proces copil
            close(fd[0]);  // Închidem capătul de citire al pipe-ului în copil
            close(s1);      // Închidem socket-ul server în copil

            int n, nr[101];
            recv(c, &n, 2, MSG_WAITALL);
            n = ntohs(n);
            uint16_t min;

            for (int i = 0; i < n; i++) {
                recv(c, &nr[i], 2, MSG_WAITALL);
                nr[i] = ntohs(nr[i]);
                if (i == 0) {
                    min = nr[i];
                } else {
                    if (nr[i] < min)
                        min = nr[i];
                }
            }

            // Trimitem valoarea minimă părintelui prin pipe
            min = htons(min);
            write(fd[1], &min, 2);

            close(c);     // Închidem conexiunea cu clientul
            close(fd[1]); // Închidem capătul de scriere al pipe-ului în copil
            exit(0);
        } else {
            // Proces părinte
            close(fd[1]); // Închidem capătul de scriere al pipe-ului în părinte
            close(c);      // Închidem conexiunea cu clientul

            uint16_t min;
            read(fd[0], &min, sizeof(min)); // Citim valoarea minimă trimisă de copil
            mins[k++] = ntohs(min);         // Adăugăm valoarea la șirul mins

            close(fd[0]); // Închidem capătul de citire al pipe-ului în părinte
        }
    }

    return 0;
}