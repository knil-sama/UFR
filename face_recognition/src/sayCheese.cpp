#include <iostream>
#include <stdio.h>
#include "../header/image_processing.h"

using namespace std;

int main(int argc, const char* argv[]){
	if(argc != 3){
		return -1;
		cout << "{\"status\" : \"Error wrong parameter number\", \"data\" : \"empty\"}" << endl;
	}
	//mock histo
	cout << "{\"status\" : \"Ok\", data : [";
	for(int color = 0; color < 3; color++){
		cout << "{";
		for(int count = 0; count <255; count++){
			cout <<  count;
			if(count < 254){
				cout << ",";
			}  
		}
		cout << "}";
		if(color < 2){
			cout << ",";
		}
	}
	//end data
	cout << "]}" << endl;
	return 0;
}
