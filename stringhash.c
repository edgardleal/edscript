

#include "stringhash.h"
#include "hashmap.c"


int put(char *bucket[], char *key, char *value)
{
    int hash = create_hash(key);
    bucket[hash] = value;
    return hash;
}

char *stringhash_get(char *bucket[],char *key)
{
    int hash = create_hash(key);
    return bucket[hash];
}
