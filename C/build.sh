#!/bin/bash

declare targetdir="target"
declare mysql_include="$(mysql_config --include)"
declare mysql_flag="$(mysql_config --cflags)"
declare mysql_libs="$(mysql_config --libs)"

[ ! -d "clib" ] && git submodule add https://github.com/edgardleal/clib.git
[ ! -d "expression_parser" ] && git submodule add https://github.com/edgardleal/expression_parser.git
[ ! -d $targetdir ] && mkdir $targetdir

if [ "$1" == "test" ]; then
  gcc -g -x c -I . tests.c -o ${targetdir}/edscript_tests ${mysql_include} ${mysql_cflags} ${mysql_libs}
  if [ $? == 0 ]; then 
    if [ "$2" == "debug" ]; then
      gdb ./${targetdir}/edscript_tests
    else
      ./${targetdir}/edscript_tests -c
    fi
    exit $!
  else
    echo "Erro ao compilar"
    exit 1
  fi
else
  gcc -x c $mysql_include ${mysql_flags} ${mysql_libs} main.c -o ${targetdir}/edscript
fi

