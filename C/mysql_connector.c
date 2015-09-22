#include <mysql.h>
#include <stdio.h>
#include <stdlib.h>
#include "clib/src/fileutils.c"
#include "clib/src/stringutil.c"

#define SHOW_PROCESS_LIST "show processlist"

typedef struct st_database_connection
{
  char *host;
  char *user;
  char *pass;
  char *db;
  MYSQL *conn;
  int
  (*close) (struct st_database_connection *c);
  int isclosed;
} database_connection;

int
_close (struct st_database_connection *c)
{
  if (!c->isclosed)
    {
      mysql_close (c->conn);
    }
  return (0);
}

char *_mysql_last_pass;
char *_mysql_last_user;
char *_mysql_last_host;
char *_mysql_last_db;

void
_read_key_value_text (char * text)
{
  char* value;
  char* key;
  char **_array;
  _array = string_split (text, "=", _array);
  key = _array[0];
  value = _array[1];

  if (strcmp ("host", key) == 0)
    {
      _mysql_last_host = value;
      return;
    }
  else if (strcmp ("db", key) == 0)
    {
      _mysql_last_db = value;
    }
  free (_array);
  free (value);
  free (key);
  return;
}

/**
 * Callbak to parse of connection file 
 *
 */
int
_mysql_connection_file_reader (int line, char *text)
{
  if (line == 1)
    {
      char ** driver_array;
      char ** values_array;

      driver_array = string_split (text, ":", driver_array);
      values_array = string_split (driver_array[1], ";", values_array);

      int _array_length = (int) (sizeof(values_array) / sizeof(char *));
      int _i = 0;
      for (_i = 0; _i < _array_length; _i = _i + 1)
	{
	  _read_key_value_text (values_array[_i]);
	}

      free (driver_array);
      free (values_array);
    }
  else if (line == 2)
    {
      _mysql_last_user = text;
    }
  else if (line == 3)
    {
      _mysql_last_pass = text;
    }
  return (0);
}

int
read_mysql_connection_from_file (char *file_name,
				 struct st_database_connection *c)
{
  read_file_with_callback (file_name, _mysql_connection_file_reader);
  c->user = _mysql_last_user;
  c->pass = _mysql_last_pass;
  c->db = _mysql_last_db;
  c->host = _mysql_last_host;
  return (0);
}

int
open_connection (struct st_database_connection *c)
{
  c->conn = mysql_init (NULL);
  if (!mysql_real_connect (c->conn, c->host, c->user, c->pass, c->db, 0, NULL,
			   0))
    {
      fprintf (stderr, "%s\n", mysql_error (c->conn));
      exit (1);
    }

  c->close = _close;
  return (0);
}

int
connect ()
{
  MYSQL *conn;
  MYSQL_RES *res;
  MYSQL_ROW row;
  char *server = "54.213.167.13";
  char *user = "root";
  char *password = "K1xp#Px1k"; /* set me first */
  char *database = "lojasguaibim";
  printf ("Iniciando a conexao... \n");
  conn = mysql_init (NULL);

  printf ("conectando-se ao banco de dados... \n");
  /* Connect to database */
  if (!mysql_real_connect (conn, server, user, password, database, 0, NULL, 0))
    {
      fprintf (stderr, "%s\n", mysql_error (conn));
      exit (1);
    }
  /* send SQL query */
  if (mysql_query (conn, SHOW_PROCESS_LIST))
    {
      fprintf (stderr, "%s\n", mysql_error (conn));
      exit (1);
    }
  res = mysql_use_result (conn);
  /* output table name */
  printf ("MySQL Tables in mysql database:\n");
  while ((row = mysql_fetch_row (res)) != NULL)
    printf ("%s \n", row[0]);
  /* close connection */
  mysql_free_result (res);
  mysql_close (conn);
  return (0);
}

