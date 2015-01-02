
#include "stringutil.c"
#include "expression_parser.h"


typedef int (*edscript_function)(char *parameters[]);

struct command{
  int line; 
  char **parameters;
  char *function_name;
  edscript_function func;
} command;



struct command *commands;
int commands_count = 0;


void append_command(struct command c)
{
    commands_count = commands_count + 1;
    commands = malloc(sizeof(c) * commands_count);
    commands[commands_count - 1] = c;
}

void parse_command(char *line, int line_number)
{
    return;
}


// ========================== script functions ===============


edscript_function print;
edscript_function read_file;
edscript_function query;
edscript_function execute;

int _print(char *parameters[])
{
    
    return 0;
}
