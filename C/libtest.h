
/**
 *
 *
 *
 */
#include <string.h>
#include <stdio.h>
#include <stdarg.h>

#define KNRM  "\x1B[0m"
#define KRED  "\x1B[31m"
#define KGRN  "\x1B[32m"
#define KYEL  "\x1B[33m"
#define KBLU  "\x1B[34m"
#define KMAG  "\x1B[35m"
#define KCYN  "\x1B[36m"
#define KWHT  "\x1B[37m"
#define TEST_IDENT "    " 
#define TEST_CASE_IDENT "  " 

// Macros

typedef int boolean;
struct test_case{
    char name[100];
    int fail;
    int pass;
};

struct test_case *tests_cases;
int tests_count = 0;

typedef void (*print_parser)(int level, char *text);

void color_parser(int level, char *text)
{
    if(level == 0)
    {
        printf("%s%s", KGRN, text);
    }
    else if(level == 1)
    {
        printf("%s%s", KRED, text);
    }

    printf("%s", KNRM);
}

print_parser default_parser = *color_parser;

void print_sumary()
{
    int i = tests_count - 1;
    int error = 0;
    error = tests_cases[i].fail > 0;

    printf("+--------+----------------+\n");
    printf("%s", "|");
    printf(TEST_CASE_IDENT);
    default_parser(0, "Pass: ");
    printf("| %.3d            |\n", tests_cases[i].pass);
    printf("+--------+----------------+\n");
    printf("%s", "|");
    printf(TEST_CASE_IDENT);
    default_parser(1, "Fail: ");
    printf("| %.3d            |\n", tests_cases[i].fail);
    printf("+--------+----------------+\n\n\n");
}

/**
 * Check all tests cases, if any have an error, return 1 else return 0.
 * To use in build scripts 
 *
 */
int end_test()
{
    print_sumary();
    int i = 0;
    int pass_all = 1;
    for(i = 0; i < tests_count; i = i + 1)
    {
        pass_all = pass_all && tests_cases[i].fail == 0;
    }

    if(tests_count)
    {
        free(tests_cases);
    }
    tests_count = 0;

    printf("%s", TEST_IDENT);
    if(pass_all)
    {
        default_parser(0, "PASS!!");
    }
    else
    {
        default_parser(1, "FAIL");
    }
    printf("%s", "\n\n");
    return !pass_all;
}

/**
 * Print sumary of current testcase
 *
 */
static void end_tests()
{
    print_sumary();
}

/**
 * Start and append a new test_case with the name passed in argument "name"
 *
 *
 */
void new_test_case(char *name)
{
    if(tests_count > 0)
    {
        print_sumary();
    }
    struct test_case c;
    strcpy(c.name, name);
    c.fail = 0;
    c.pass = 0;
    tests_count = tests_count + 1;
    printf("%.3d - %s\n",tests_count, name);
    tests_cases = malloc(tests_count * sizeof(c));
}

/**
 * Increase the number os passed tests in current test_case
 * 
 *
 */
void increase_pass()
{
    tests_cases[tests_count - 1].pass = tests_cases[tests_count - 1].pass + 1;
}

/**
 * Increase the number of failed tests in current test_case 
 *
 *
 */
void increase_fail()
{
    tests_cases[tests_count - 1].fail = tests_cases[tests_count - 1].fail + 1;
}

// TODO: Finalizar as macros para os metodos de assercoes 
static void test_pass(char *a)
{
    printf("%s", TEST_IDENT);
    default_parser(0, "Ok  ");
    printf(": [ %s ]\n", a);
}

static void _test_pass0(char *text)
{
    printf(TEST_IDENT);
    default_parser(0, "Ok  ");
    printf(": [ %s ]\n", text);
}

static void test_fail(char *text)
{
    printf(TEST_IDENT);
    default_parser(1, "Fail");
    printf(": [ %s ]\n", text);
}

void check_test_is_initiated()
{
    if(tests_count == 0)
    {
        new_test_case("Main Test");
    }
}

void ok(boolean condition, char *pattern, ...)
{
    char buffer[100];
    va_list args;
    va_start( args, pattern);
    vsnprintf(buffer, 100, pattern, args);
    
    check_test_is_initiated();
    if(condition)
    {
        test_pass(buffer);
        increase_pass();
    }
    else
    {
        test_fail(buffer);
        increase_fail();
    }
    va_end( args );
}


void _ok0(boolean condition, char *text)
{
    check_test_is_initiated();

    if(condition)
    {
        test_pass(text);
        increase_pass();
    }
    else
    {
        test_fail(text);
        increase_fail();
    }
}

void assert(boolean condition, char *text)
{
    return ok(condition, "%s", text);
}

void assert_equals(char *a , char *b, char *text)
{
    return ok(strcmp(a, b) == 0, "%s", text);
}
