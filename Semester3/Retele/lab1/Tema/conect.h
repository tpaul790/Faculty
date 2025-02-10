#ifndef CONECT_H
#define CONECT_H

void conectare(int* c, struct sockaddr_in* server){
  *c = socket(AF_INET,SOCK_STREAM,0);
  if(c<0){
    printf("Eroare la crearea socketului client\n");
    exit(0);
  }
  memset(server, 0, sizeof(*server));
  server->sin_port = htons(1234);
  server->sin_family = AF_INET;
  server->sin_addr.s_addr = inet_addr("192.168.40.230");
  if(connect(*c, (struct sockaddr*) server, sizeof(*server))<0){
    printf("Eroare de conectare la server\n");
    exit(0);
  }
}

#endif
