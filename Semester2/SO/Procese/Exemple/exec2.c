#define _GNU_SOURCE			// to remove compiler warning

#include <stdio.h>
#include <unistd.h>

int main()
{
	char *envp[] = {"USER=gigi", "HOME=/home/gigi", NULL};
/*
	printf("[In main()] Calling execle() ...\n");
	execle("/usr/bin/env", "/usr/bin/env", NULL, envp);
*/

	char *args[] = {"env", NULL};

	printf("[In main()] Calling execvpe() ...\n");
	execvpe(args[0], args, envp);


	printf("[In main()] I finished.\n");
	return 0;
}
