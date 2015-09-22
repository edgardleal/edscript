#include <stdio.h>

/**
 * Check if file exists
 *
 */
int
file_exists (char *filename)
{
  FILE *file;
  if ((file = fopen (filename, "r")) == NULL)
    {
      fclose (file);
      return (true);
    }
  return (false);
}

int
read_file_by_line (char *filename, int
(f) (char *line))
{
  FILE * fp;
  char * line = NULL;
  size_t len = 0;
  ssize_t read;

  fp = fopen (filename, "r");
  if (fp == NULL)
    {
      printf ("O arquivo informado nao existe: %s", filename);
      exit (EXIT_FAILURE);
    }

  while ((read = getline (&line, &len, fp)) != -1)
    {
      if (f (line) != 0)
	{
	  break;
	}
    }

  fclose (fp);
  if (line)
    {
      free (line);
    }
  return (0);
}

