#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
 
struct data {
  int nr;
  int tries;
};
 
int getNumberFromQueryString() {
  char buffer[2048];
  int id, nr;
  strcpy(buffer, getenv("QUERY_STRING"));
  sscanf(buffer, "id=%d&nr=%d", &id, &nr);
  return nr;  
}
 
int init() { 
  int r, id;
  int code;
  char filename[100];
  struct data d;
  
  srand(getpid());
  r = random() % 100;
 
  do {
    id = random();
    sprintf(filename, "/tmp/%d.txt", id);
    code = creat(filename, O_CREAT | O_EXCL | 0600);
  }
  while (code < 0);
 
  d.nr = r;
  d.tries = 0;
  write(code, &d, sizeof(d));
  close(code);
  
  return id;
}
 
void destroy(int id) {
  char filename[100];
  sprintf(filename, "/tmp/%d.txt", id);
  unlink(filename);
}
 
int getNumberFromFile(int id) {
  char filename[100];
  int fd;
  
  sprintf(filename, "/tmp/%d.txt", id);
  struct data d;
  fd = open(filename, O_RDWR);
  if (fd < 0)
    return -1;
  read(fd, &d, sizeof(d));
  d.tries++;
  lseek(fd, 0, SEEK_SET);
  write(fd, &d, sizeof(d));  
  close(fd);
  return d.nr;
}
 
int getNoOfTries(int id) {
  char filename[100];
  int fd;
  sprintf(filename, "/tmp/%d.txt", id);
  struct data d;
  fd = open(filename, O_RDONLY);
  read(fd, &d, sizeof(d));
  close(fd);
  return d.tries;    
}
 
void printForm(int id) {
  printf("<form action='/cgi-bin/pr8.cgi' method='get'>\n");
  printf("Nr: <input type='text' name='nr'><br>\n");
  printf("<input type='submit' value='Trimite'>\n");
  printf("</form>");
}
 
void setCookie(int id) {
  printf("Set-Cookie: id=%d; path=/; HttpOnly\n", id);
  printf("\n");
}

int getIdFromCookie() {
    char *cookie = getenv("HTTP_COOKIE");
    if (cookie == NULL) {
        return -1;
    }

    char *cookie_copy = strdup(cookie);
    if (!cookie_copy) return -1;

    char *token = strtok(cookie_copy, ";");
    while (token != NULL) {
        while (*token == ' ') token++;

        if (strncmp(token, "id=", 3) == 0) {
            int id;
            if (sscanf(token + 3, "%d", &id) == 1) {
                free(cookie_copy);
                return id;
            }
        }

        token = strtok(NULL, ";");
    }

    free(cookie_copy);
    return -1;
}

int isNewUser() {  
        if (getIdFromCookie() == -1)
                return 1;
        return 0;
}

int main() {
  int id, status;
  if (isNewUser()) {
    id = init();    
    status = 0;
  }
  else {
    int nr, nr2;
    id = getIdFromCookie();
    nr = getNumberFromQueryString();
    nr2 = getNumberFromFile(id);
    if (nr2 == -1)
      status = 1;
    else if (nr == nr2)
      status = 2;
    else if (nr < nr2)
      status = 3;
    else if (nr > nr2)
       status = 4;                
  }
  printf("Content-type: text/html\n\n");
  printf("<html>\n<body>\n");
  
  switch (status) {
    case 0 : printf("Ati inceput un joc nou.<br>\n"); printForm(id); break;
    case 1 : printf("Eroare. Click <a href='/cgi-bin/pr8.cgi'>here</a> for a new game!"); break;
    case 2 : printf("Ati ghicit din %d incercari. Click <a href='/cgi-bin/pr8.cgi'>here</a> for a new game!</body></html>", getNoOfTries(id)); destroy(id); break;
    case 3 : printf("Prea mic!<br>\n"); printForm(id); break;
    case 4 : printf("Prea mare!<br>\n"); printForm(id);
  }
  
  printf("</body>\n</html>");
}
