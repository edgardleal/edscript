
#include <stdio.h>


int read_file_by_line(char *filename, int (f)(char *line))
{
    FILE * fp;
    char * line = NULL;
    size_t len = 0;
    ssize_t read;

    fp = fopen(filename, "r");
    if (fp == NULL)
    {
        exit(EXIT_FAILURE);
    }

    while ((read = getline(&line, &len, fp)) != -1) {
        if(f(line) != 0)
        {
            break;
        }
    }

    fclose(fp);
    if (line)
    {
        free(line);
    }
    return 0;
}

