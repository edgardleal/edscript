
#include <string.h>

void
ltrim (char *text)
{
  int __i = 0, __j = 0;
  int __len = strlen (text);
  while (text[__i++] == ' ' && __i < __len)
    ;
  for (__j = 0; __j < __len; __j = __j + 1)
    {
      text[__j] = text[__j + __i - 1];
    }
  text[__j + __i - 1] = 0;
}

void
rtrim (char *text)
{
  int __len = strlen (text);
  int __i = __len;
  while (text[--__i] == ' ' && __i > 0)
    ;
  text[__i + 1] = 0;
}

void
trim (char *text)
{
  rtrim (text);
  ltrim (text);
}
