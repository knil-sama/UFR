#include <opencv2/core/core.hpp>
#include <iostream>
#include <stdio.h>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <algorithm>

#ifndef H_RECOGNITION
#def H_RECOGNITION

vector<int> createLUT(img sobelX, img sobelY);

void findMotif(vector<int> LUT, int alpha,int beta,img result);

#endif