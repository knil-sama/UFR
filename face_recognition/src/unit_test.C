#include <opencv2/opencv.hpp>
#include <iostream>
#include <stdio.h>
#include "../header/image_processing.h"
using namespace cv;
using namespace std;

int null_recognition_test(){
	try{
		Mat img = imread("/home/demonchy/Dropbox/Cours_master_2/biometrics/doc/ellipse.png", CV_LOAD_IMAGE_UNCHANGED);
		//Mat img = imread("/home/demonchy/Dropbox/Cours_master_2/biometrics/doc/cercle.png", CV_LOAD_IMAGE_UNCHANGED);
		Mat yeux = imread("/home/demonchy/Dropbox/Cours_master_2/biometrics/doc/yeux.png", CV_LOAD_IMAGE_UNCHANGED);

		// Create a new matrix to hold the gray image
    		Mat gray;
		Mat gray_yeux;
    		// convert RGB image to gray
		cvtColor(img, gray, CV_BGR2GRAY);
		cvtColor(yeux, gray_yeux, CV_BGR2GRAY);
		//Mat clean_image = pre_processing_image(gray);
		Mat recognize = recognition_face(gray,gray_yeux);
		showPic(recognize);

	}catch(int param){
		cout << "exception n° " << param << endl;
	}
	return 0;
}

int main(){
	null_recognition_test();
	cvWaitKey(0); 

}
