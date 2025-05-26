#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int hexatoint(char c) {
    if ((c >= 'a') && (c <= 'f'))
        return c - 'a' + 10;
    if ((c >= 'A') && (c <= 'F'))
        return c - 'A' + 10;
    return c - '0';
}

void decode(char *s) {
    int i = 0, j;
    while (s[i] != 0) {
        if (s[i] == '+')
            s[i] = ' ';
        if (s[i] == '%') {
            char c = 16 * hexatoint(s[i + 1]) + hexatoint(s[i + 2]);
            s[i] = c;
            j = i + 1;
            do {
                s[j] = s[j + 2];
                j++;
            } while (s[j] != 0);
        }
        i++;
    }
}

int main() {
    char *query_string = getenv("QUERY_STRING"); //iau string ul din variabila de mediu
    printf("Content-type: text/html\n\n");       // il scriu in web asa cum l am primit
    if (query_string == NULL || strlen(query_string) == 0) {
        printf("Nu a fost primit niciun șir de decodat.");
    } else {
        char s[1024];
        // presupunem ca parametrul e de forma text=...
        char *p = strstr(query_string, "text=");
        if (p) {
            strncpy(s, p + 5, sizeof(s) - 1); // copiem valoarea după "text="
            s[sizeof(s) - 1] = '\0';          // siguranță de terminator
            printf("Sir codificat: %s", s);
            printf("<br>");
            decode(s);
            printf("Sir decodificat: %s", s);
        } else {
            printf("Parametrul 'text' nu a fost găsit.");
        }
    }
    return 0;
}
