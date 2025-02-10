//Să se scrie un program C care creează un proces copil cu care comunică prin pipe.
// Procesul părinte citeşte de la tastatură un caracter c şi un şir s şi le trimite
// prin pipe procesului copil, iar procesul copil verifică şi afişează
// numărul de apariţii ale caracterului c în şirul s.
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <wait.h>

int main(int argc,char* argv[]){
	int fd[2];
	int res=pipe(fd);
	if(res == -1){
		perror("pipe(): ");
		exit(0);
	}
	int pid=fork();
	if(pid == -1){
		perror("fork(): ");
		exit(0);
	}
	if(pid == 0){
	//in copil citesc caracterul si sirul scrise in pipe
		close(fd[1]);
		while(1){
			char ch;
			read(fd[0],&ch,sizeof(char));
			printf("[IN CHILD]\t Am citit caracterul: %c\n",ch);

			char sir[50];
			read(fd[0],sir,sizeof(sir));
			printf("[IN CHILD]\t Am citit sirul: %s\n",sir);

			if(strcmp(sir,"stop")==0)
				break;

			int cnt=0;
			for(int i=0;i<strlen(sir);i++)
				if(sir[i] == ch)
					cnt++;
			printf("[IN CHILD]\t Numarul de aparitii a lui '%c' in '%s' este: %d\n",ch,sir,cnt);
		}
		close(fd[0]);
		exit(0);
	}
	//in parinte citesc de la tastatura un sir si un caract si le scriu in pipe
	close(fd[0]);
	while(1){
		char ch;
		char sir[50];
		printf("Introduceti un caracter: ");
		scanf("%c",&ch);
		write(fd[1],&ch,sizeof(char));
                printf("[IN PARENT]\t Am scris in pipe carcaterul: %c\n",ch);

		getchar();
		sleep(1);

		printf("Introduceti un sir: ");
		scanf("%s",sir);
		write(fd[1],sir,(strlen(sir)+1)*sizeof(char));
		printf("[IN PARENT]\t Am scris in pipe sirul: %s\n",sir);

		if(strcmp(sir,"stop")==0)
			break;
		getchar();
		sleep(1);
	}
	int status;
	wait(&status);
	printf("[IN PARENT]\t Copilul si-a terminat executia cu codul: %d\n",status);
	close(fd[1]);
	return 0;
}










