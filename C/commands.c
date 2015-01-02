
#include "stringutil.c"
#include "expression_parser/code/expression_parser.h"



struct st_command{
  int line; 
  int level;
  char **parameters;
  char *function_name;
  struct st_command *childrens;
  int (*func)(struct st_command *c, char **parameters);
} command;


typedef int (*edscript_function)(struct st_command *_c, char **parameters);

int _print(struct st_command *c, char *parameters[]);

struct st_command *commands;
int commands_count = 0;


void append_command(struct st_command *c)
{
    commands_count = commands_count + 1;
    if(commands_count == 0)
    {
        commands = malloc(sizeof(command));
    }
    else
    {
        realloc(commands, sizeof(c) * commands_count);
    }
    commands[commands_count - 1].line         = c->line;
    commands[commands_count - 1].level        = c->level;
    // commands[commands_count - 1].parameters   = c->parameters;
    // commands[commands_count - 1].function_name= c->function_name;
    // commands[commands_count - 1].childrens    = c->childrens;
}

/**
 * Retorna 0 caso tenha conseguido fazer o parse do comando corretamente
 *
 *
 *
 */
int parse_command(char *line, int line_number)
{
    int i = 0;
    int _spaces = 0;
    struct st_command _command;
    _command.level = 0;
    while(line[i] != 0) 
    {

        
        if(line[i] == 32)
        {
            _spaces = _spaces + 1;
        }
        i = i + 1;
    }
    return 0;
}


// ========================== script functions ===============


edscript_function read_file;
edscript_function query;
edscript_function execute;

int _print(struct st_command *c, char *parameters[])
{
    
    return 0;
}

