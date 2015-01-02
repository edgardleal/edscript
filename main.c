

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "file.c"
#include "stringhash.c"
#include "commands.c"


int func(char *line)
{
    printf("Linha: [%s]\n", line);
    return 0;
}

int main(int argc, char *argv[])
{
    if(argc > 1)
    {
        if(strcmp(argv[1], "test") == 0)
        {
            exit(EXIT_SUCCESS);
        }
        else
        {
            read_file_by_line(argv[1], func);   	
        }
    }
    else
    {
        printf("Informe o arquivo com o script\n");
    }

    exit(EXIT_SUCCESS);
}
