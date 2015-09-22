
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

build: $(BIN)main.o
	$(CC) $(BIN)main.o -o $(APP)


$(BIN)main.o: main.c setup
	$(CC) -c main.c -o $@

clean:
	rm -f $(BIN)*.o $(APP)

test.o: setup 
	$(CC) -c test.c -o $(BIN)$@ 

compile_test: test.o
	$(CC) test.c $(BIN)test

test: build compile_test
	$(BIN)test
