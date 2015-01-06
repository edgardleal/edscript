
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
// #include "cheat.h"
#include "libtest.h"
#include "commands.c"
#include "hashmap.c"
#include "expression_parser/code/expression_parser.c"
#include "mysql_connector.c"
#include "clib/src/stringutil.c"

void test_expression(char *expression, double expected)
{
    ok(parse_expression(expression) == expected, "Expression: %s, expected: %f", expression, expected);
}

int main(int argc, char *argv[])
{

    new_test_case("Teste de hash");
    ok(ascii_hash("teste")       < 8500,"%s", "Teste com a palavra teste");
    ok(ascii_hash("print")       < 8500,"%s", "Teste com a palavra print");
    ok(ascii_hash("nome_tabela") < 8500,"%s", "Teste com a palavra nome_tabela");
    ok(ascii_hash("nova_palavra")< 8500,"%s", "Teste com a palavra nova_palavra");

    
    new_test_case("StringUtils\n");
    char **_list;
    string_split("mysql:host=teste;db=banco\0\0", ':', &_list);
    ok(strcmp("mysql", _list[0]) == 0, "%s", "Conteudo a primeira parte");

    new_test_case("Mysql");
    if(!file_exists("connection.test"))
    {
        append_to_file("connection.test","mysql:host=localhost;db=test\nroot\nroot");
    }

    struct st_database_connection _c;
    read_mysql_connection_from_file("connection.test", &_c);

    int ret = strncmp("localhost", _c.host, 9);
    ok(ret == 0, "%s", "Host lido do arquivo com sucesso");
    ok(!connect(), "%s", "Conexao com o mysql efetuada com sucesso");


    new_test_case("Commands");
    struct st_command c;
    c.line = 5;
    printf("adicionando o comando");
    append_command(&c);
    printf("command adicionado");
    ok(commands[commands_count - 1].line == 5,"%s", "Verifica a inclusao do comando");
    printf("teste executado");


    new_test_case("Math Expressions");
    test_expression("5+2", 7.0);
    test_expression("5*2", 10.0);
    test_expression("5-2", 3.0);
    test_expression("10*5+4", 54.0);
    test_expression("(3+2)*5", 25.0);

    end_test();
    exit(0);
}

