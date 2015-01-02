
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
// #include "cheat.h"
#include "libtest.h"
#include "commands.c"
#include "hashmap.c"
#include "expression_parser.c"


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

    new_test_case("Commands");
    struct command c;
    c.line = 5;
    append_command(c);
    ok(commands[commands_count - 1].line == 5,"%s", "Verifica a inclusao do comando");


    new_test_case("Math Expressions");
    test_expression("5+2", 7.0);
    test_expression("5*2", 10.0);
    test_expression("5-2", 3.0);
    test_expression("10*5+4", 54.0);
    test_expression("(3+2)*5", 25.0);

    end_test();
    exit(0);
}

