#include <opencv2/core/core.hpp>
#include <iostream>
#include <stdio.h>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <algorithm>

#ifndef H_IMAGE_PROCESSING
#define H_IMAGE_PROCESSING

#define max(a,b) \
   ({ __typeof__ (a) _a = (a); \
       __typeof__ (b) _b = (b); \
     _a > _b ? _a : _b; }) 

#define min(a,b) \
   ({ __typeof__ (a) _a = (a); \
       __typeof__ (b) _b = (b); \
     _a < _b ? _a : _b; }) 


#define RADIUS_MATCH 20
#define DEBUG_DOMINANT_GRADIENT 1
#define DEBUG_GRADIENT_INTEREST_POINT 1

void get_neighborhood(cv::Mat image, cv::Point central_point, int neighborhood[8]);

int max_neighborhood(cv::Mat image, cv::Point central_point);

int min_neighborhood(cv::Mat image, cv::Point central_point);

int sum_difference_pixel(cv::Mat image_A, cv::Point point_A, cv::Mat image_B, cv::Point point_B);

int norme_euclidian(cv::Point p1, cv::Point p2);

cv::Mat erode_clement(cv::Mat);

cv::Mat dilate_clement(cv::Mat image);

cv::Mat pre_processing_image(cv::Mat img);

int showPic(cv::Mat pic);
	
cv::Mat sobelHori(cv::Mat pic);

cv::Mat sobelVerti(cv::Mat pic);

std::vector<int> create_LUT(cv::Mat angle);

cv::Mat recognition_face(cv::Mat pic, cv::Mat face);

#endif
