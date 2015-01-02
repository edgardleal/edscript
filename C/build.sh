#!/bin/bash

declare targetdir="target"
declare mysql_include="$(mysql_config --include)"

[ ! -d "clib" ] && git submodule add https://github.com/edgardleal/clib.git
[ ! -d "expression_parser" ] && git submodule add https://github.com/edgardleal/expression_parser.git
[ ! -d $targetdir ] && mkdir $targetdir

if [ "$1" == "test" ]; then
  gcc -x c -I . tests.c -o ${targetdir}/edscript_tests
  if [ $? == 0 ]; then 
    ./${targetdir}/edscript_tests -c
    exit $!
  else
    echo "Erro ao compilar"
    exit 1
  fi
else
  gcc -x c $mysql_include  main.c -o ${targetdir}/edscript
fi

