FLAG_OPENCV=-lopencv_core -lopencv_highgui -lopencv_imgproc
CC=g++
SRC_DIR=./src/
OBJ_DIR=./obj/
EXEC_DIR=./exec/
HEADER_DIR=./header/
OPTIMISATION=-O2

all: sayCheese unit_test

unit_test: image_processing.o unit_test.o
	$(CC) $(OPTIMISATION) $(OBJ_DIR)image_processing.o $(OBJ_DIR)unit_test.o  -o $(EXEC_DIR)unit_test $(FLAG_OPENCV)

sayCheese: image_processing.o sayCheese.o
	$(CC) $(OPTIMISATION) $(OBJ_DIR)sayCheese.o $(OBJ_DIR)image_processing.o -o $(EXEC_DIR)sayCheese $(FLAG_OPENCV)


image_processing.o: $(SRC_DIR)image_processing.cpp
	$(CC) $(OPTIMISATION) -o $(OBJ_DIR)image_processing.o -c $(SRC_DIR)image_processing.cpp

sayCheese.o: $(SRC_DIR)sayCheese.cpp
	$(CC) $(OPTIMISATION) -o $(OBJ_DIR)sayCheese.o -c $(SRC_DIR)sayCheese.cpp


unit_test.o: $(SRC_DIR)unit_test.C
	$(CC) $(OPTIMISATION) -o $(OBJ_DIR)unit_test.o -c $(SRC_DIR)unit_test.C
