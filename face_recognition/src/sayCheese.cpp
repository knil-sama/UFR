#include <iostream>
#include <stdio.h>
#include "../header/image_processing.h"

using namespace std;

int main(int argc, const char* argv[]){
	if(argc != 3){
		return -1;
		cout << "Error" << endl;
	}
	//mock histo
	cout << "{" ;
	for(int count = 0; count <255; count++){
		cout << '"';
		cout << count;
		cout << '"';
		cout << " : ";
		cout <<  count;
		if(count < 254){
			cout << ",";
		}  
	}
	cout << "}" << endl;
	return 0;
}
