
APP_NAME=edscript
CC=gcc
BIN=./bin/
APP=$(BIN)$(APP_NAME)
OBJ_FILE=$(BIN)

all: build

$(BIN)%.o: %.c
	$(CC) -c -o $@ $

setup:
	[[ -d $(BIN) ]] || mkdir $(BIN)

build: $(BIN)main.obj
	gcc ./bin/main.o -o $(APP)


$(BIN)main.obj: main.c setup
	$(CC) -c main.c -o $@
