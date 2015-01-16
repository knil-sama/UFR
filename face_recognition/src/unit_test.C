#include <opencv2/opencv.hpp>
#include <iostream>
#include <stdio.h>
#include "../header/image_processing.h"
using namespace cv;
using namespace std;

int recognition_test(const char* image_shape, const char* image_face,int treshold_lut, int treshold_face){
	try{
		Mat shape_color  = imread(image_shape, CV_LOAD_IMAGE_UNCHANGED);
		Mat face_color  = imread(image_face, CV_LOAD_IMAGE_UNCHANGED);
		// Create a new matrix to hold the gray image
    		Mat shape_gray;
		Mat face_gray;
    		// convert RGB image to gray
		cvtColor(shape_color, shape_gray, CV_BGR2GRAY);
		cvtColor(face_color, face_gray, CV_BGR2GRAY);
		vector<float> histo_vector = recognition_face(shape_gray,face_gray,treshold_lut, treshold_face);
		//showPic(recognize);
		if(histo_vector.size() != 255){
			cout << "{\"reply\" : \"Ko\", \"data\" : \"empty\"}" << endl;
		}else{
			cout << "{\"reply\" : \"Ok\",";
			cout << "\"data\" : [";
			for(int i = 0; i < histo_vector.size(); i++){
				cout << histo_vector[i];
				if(i < histo_vector.size() - 1){
					cout << ",";
				}
			}
			cout << "]}" << endl;
		}
	}catch(int param){
		cout << "exception n° " << param << endl;
	}
	return 0;
}

int main(int argc, const char* argv[]){
	//usage path image template, path image user, treshold
	if(argc != 4){ 
		return -1;
	} 
	const char* path_image_shape = argv[1];
	const char *path_image_picture = argv[2];
	std::string hello(argv[3]); 
	std::stringstream str(hello); 
	int  treshold_face;  
	str >> treshold_face;  
	if (!str) 
	{      
   		return -1;      
   	}
	int treshold_lut = 100;

/*	if(argc == 2){
		treshold =  argv[1] - '0';
	}*/
	recognition_test(path_image_shape, path_image_picture,treshold_lut, treshold_face);
	cvWaitKey(0); 

}
