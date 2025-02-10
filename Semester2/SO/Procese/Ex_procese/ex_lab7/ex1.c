#include <stdio.h>
#include <stdlib.h>
//Scrieți un program C care execută o comandă Bash și afișează rezultatul execuției pe ecran.
//Exemplu: ls -l
int main(int argc, char** argv){
	system("ls -l");
	return 0;
}
