#include <stdio.h>
#include <stdlib.h>

#define DEFAULT_BUCKET_SIZE = 10000

inline int create_hash(char *key);
extern int hashint_put(char *key, int value);

static int bucket[100];

void println(char *text)
{
    printf("%s\n", text);
}


long ascii_hash(char *key)
{
    int i = 0;
    long result = 0;
    while(key[i] != 0)
    {
        result = result + ((i + 1) * key[i]);

        i = i + 1;
    }
    return result;
}


long hash(char *str)
{
    unsigned long hash = 5381;
    int c;

    while ((c = *str++))
        hash = ((hash << 5) + hash) + c; /* hash * 33 + c */

    return hash;
}

int create_hash(char *key)
{
    int result = 0;
    int i = 0;
    for(i = 0;key[i] != 0; i = i + 1)
    {
        result = result + key[i];
    }
    return result % 100;
}

int hashint_put(char *key, int value)
{
    int hash = create_hash(key);
    bucket[hash] = value;
    return hash;
}

