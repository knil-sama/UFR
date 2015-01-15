#include <opencv2/opencv.hpp>
#include <iostream>
#include <stdio.h>
#include "../header/image_processing.h"
using namespace cv;
using namespace std;

int null_recognition_test(int treshold_lut, int treshold_face){
	try{
		Mat img = imread("/home/demonchy/Dropbox/Cours_master_2/biometrics/doc/cercle.png", CV_LOAD_IMAGE_UNCHANGED);
		//Mat img = imread("/home/demonchy/Dropbox/Cours_master_2/biometrics/doc/ellipse.png", CV_LOAD_IMAGE_UNCHANGED);
		//Mat yeux = imread("/home/demonchy/Dropbox/Cours_master_2/biometrics/doc/yeux.png", CV_LOAD_IMAGE_UNCHANGED);
		//Mat face  = imread("/home/demonchy/Dropbox/Cours_master_2/biometrics/doc/identify_me/anony.jpg", CV_LOAD_IMAGE_UNCHANGED);
		//Mat face  = imread("/home/demonchy/Dropbox/Cours_master_2/biometrics/doc/identify_me/clementine.jpg", CV_LOAD_IMAGE_UNCHANGED);
		//Mat face  = imread("/home/demonchy/Dropbox/Cours_master_2/biometrics/doc/identify_me/moha.jpg", CV_LOAD_IMAGE_UNCHANGED);
		Mat face  = imread("/home/demonchy/Dropbox/Cours_master_2/biometrics/doc/identify_me/clement_bleu.jpg", CV_LOAD_IMAGE_UNCHANGED);

		// Create a new matrix to hold the gray image
    		Mat gray;
		Mat gray_face;
    		// convert RGB image to gray
		cvtColor(img, gray, CV_BGR2GRAY);
		cvtColor(face, gray_face, CV_BGR2GRAY);
		//Mat clean_image = pre_processing_image(gray);
		Mat recognize = recognition_face(gray,gray_face,treshold_lut, treshold_face);
		showPic(recognize);

	}catch(int param){
		cout << "exception n° " << param << endl;
	}
	return 0;
}

int main(int argc, const char* argv[]){
	int treshold_lut = 100;
	int treshold_face = 150;
/*	if(argc == 2){
		treshold =  argv[1] - '0';
	}*/
	null_recognition_test(treshold_lut, treshold_face);
	cvWaitKey(0); 

}
