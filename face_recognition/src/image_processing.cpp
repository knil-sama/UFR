#include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
#include <iostream>
#include <stdio.h>
#include <climits>
#include "../header/image_processing.h"

#define max(a,b) \
   ({ __typeof__ (a) _a = (a); \
       __typeof__ (b) _b = (b); \
     _a > _b ? _a : _b; }) 

#define min(a,b) \
   ({ __typeof__ (a) _a = (a); \
       __typeof__ (b) _b = (b); \
     _a < _b ? _a : _b; }) 

using namespace cv;
using namespace std;

/**!
\author Samuel.B
\brief display a image and wait for the user to press a key
\warning throw a exception if image is not loaded 
!**/
int showPic( Mat pic){
	if (pic.empty()) //check whether the image is loaded or not
	{
		throw "Error : Image cannot be loaded !";
	}

	namedWindow("MyWindow", CV_WINDOW_AUTOSIZE); //create a window with the name "MyWindow"
	imshow("MyWindow", pic); //display the image which is stored in the 'img' in the "MyWindow" window

	waitKey(0); //wait infinite time for a keypress
	destroyWindow("MyWindow"); //destroy the window with the name, "MyWindow"

	return 0;
}

/**!
\author Samuel.B Clement.D
\brief apply a sobel masque on the input matrix and fill the intensity gradient for X et Y axis
!**/
void sobel(const Mat pic, vector<vector<int> > &Ix, vector<vector<int> > &Iy){
	int horizontal[] = {-1, 0, 1, -2, 0, 2, -1, 0 , 1};
	int sumSobelHori = 0;
	int vertical[] = {1, 2, 1, 0, 0, 0, -1, -2, -1};
	int sumSobelVerti = 0;
	
	int pixel_tl,pixel_t, pixel_tr, pixel_l,pixel_c,pixel_r,pixel_bl, pixel_b, pixel_br;

	for(int x = 1 ; x < pic.cols-1; x++){
		for(int y = 1;y < pic.rows-1; y++){

			//calcul du gradient Hori
			pixel_tl=pic.at<uchar>(y-1,x-1); 
			pixel_t=pic.at<uchar>(y-1,x); 
			pixel_tr=pic.at<uchar>(y-1,x+1); 
			pixel_l=pic.at<uchar>(y,x-1); 
			pixel_c=pic.at<uchar>(y,x);
			pixel_r=pic.at<uchar>(y,x+1); 
			pixel_bl=pic.at<uchar>(y+1,x-1);
			pixel_b=pic.at<uchar>(y+1,x);	
			pixel_br=pic.at<uchar>(y+1,x+1);


			 sumSobelHori =	pixel_tl * horizontal[0] +
					pixel_t * horizontal[1] +
					pixel_tr * horizontal[2] +
					pixel_l* horizontal[3] +
					pixel_c* horizontal[4] +
					pixel_r * horizontal[5] +
					pixel_bl * horizontal[6] +
					pixel_b * horizontal[7] +
					pixel_br * horizontal[8];
			//sumSobelHori = sumSobelHori / 4 ;
					sumSobelHori = sumSobelHori;
			sumSobelHori = (int)(sumSobelHori);
			Ix[x][y] = sumSobelHori;
			//Calcul du sobel verti
					
			sumSobelVerti = pixel_tl * vertical[0] +
					pixel_t * vertical[1] +
					pixel_tr * vertical[2] +
					pixel_l* vertical[3] +
					pixel_c* vertical[4] +
					pixel_r * vertical[5] +
					pixel_bl * vertical[6] +
					pixel_b * vertical[7] +
					pixel_br * vertical[8];
			
			//sumSobelVerti = sumSobelVerti / 4;
			sumSobelVerti = sumSobelVerti;
			sumSobelVerti = (int)(sumSobelVerti);
			Iy[x][y] = sumSobelVerti;
		}
	}
}

/**!
\author Clement.D
\brief pre processing of a image
	erosion -> dilation -> erosion -> dilation 
!**/
Mat pre_processing_image(Mat img){
	Mat processed_image = erode_clement(img);	
	processed_image = dilate_clement(processed_image);
	processed_image = erode_clement(processed_image);
	processed_image = dilate_clement(processed_image);
	return  processed_image;
}

/**!
\author Clement.D
\brief get all the 8 neighborhood of a pixel
if the pixel has no 8 neighbors the other pixels are at 0 values
[tl,t,tr,l,r,bl,b,br] = [x-1,y-1,
!**/
void get_neighborhood(Mat image, Point central_point, int neighborhood[8]){
	//emptcentral_point.y it
	for(int i = 0; i < 8; i++){
		neighborhood[i] = 0;
	}
	//evercentral_point.y corner and border must be handle differentlcentral_point.y
	//left border
	
	if( central_point.x == 0){
		//corner top
		if(central_point.y == 0){
			neighborhood[4] = image.at<uchar>(central_point.y,central_point.x+1);
			neighborhood[6] = image.at<uchar>(central_point.y+1,central_point.x);
			neighborhood[7] = image.at<uchar>(central_point.y+1,central_point.x+1);
		//corner bottom
		}else if(central_point.y == image.rows - 1){
			neighborhood[1] = image.at<uchar>(central_point.y-1,central_point.x);
			neighborhood[2] = image.at<uchar>(central_point.y-1,central_point.x+1);
			neighborhood[4] = image.at<uchar>(central_point.y,central_point.x+1);
		}else{
			neighborhood[1] = image.at<uchar>(central_point.y-1,central_point.x);
			neighborhood[2] = image.at<uchar>(central_point.y-1,central_point.x+1);
			neighborhood[4] = image.at<uchar>(central_point.y,central_point.x+1);
			neighborhood[6] = image.at<uchar>(central_point.y+1,central_point.x);
			neighborhood[7] = image.at<uchar>(central_point.y+1,central_point.x+1);
		}
	//right border
	}else if(central_point.x == image.cols-1){
		//corner top
		if(central_point.y == 0){
			neighborhood[3] = image.at<uchar>(central_point.y,central_point.x-1);
			neighborhood[5] = image.at<uchar>(central_point.y+1,central_point.x-1);
			neighborhood[6] = image.at<uchar>(central_point.y+1,central_point.x);
		//corner bottom
		}else if(central_point.y == image.rows - 1){
			neighborhood[0] = image.at<uchar>(central_point.y-1,central_point.x-1);
			neighborhood[1] = image.at<uchar>(central_point.y-1,central_point.x);
			neighborhood[3] = image.at<uchar>(central_point.y,central_point.x-1);
		}else{
			neighborhood[0] = image.at<uchar>(central_point.y-1,central_point.x-1);
			neighborhood[1] = image.at<uchar>(central_point.y-1,central_point.x);
			neighborhood[3] = image.at<uchar>(central_point.y,central_point.x-1);
			neighborhood[5] = image.at<uchar>(central_point.y+1,central_point.x-1);
			neighborhood[6] = image.at<uchar>(central_point.y+1,central_point.x);
		}
	//top border
	}else if( central_point.y == 0){
		neighborhood[3] = image.at<uchar>(central_point.y,central_point.x-1);
		neighborhood[4] = image.at<uchar>(central_point.y,central_point.x+1);
		neighborhood[5] = image.at<uchar>(central_point.y+1,central_point.x-1);
		neighborhood[6] = image.at<uchar>(central_point.y+1,central_point.x);
		neighborhood[7] = image.at<uchar>(central_point.y+1,central_point.x+1);
	//bottom border
	}else if( central_point.y == 0){
		neighborhood[0] = image.at<uchar>(central_point.y-1,central_point.x-1);
		neighborhood[1] = image.at<uchar>(central_point.y-1,central_point.x);
		neighborhood[2] = image.at<uchar>(central_point.y-1,central_point.x+1);
		neighborhood[3] = image.at<uchar>(central_point.y,central_point.x-1);
		neighborhood[4] = image.at<uchar>(central_point.y,central_point.x+1);
	}else{
		neighborhood[0] = image.at<uchar>(central_point.y-1,central_point.x-1);
		neighborhood[1] = image.at<uchar>(central_point.y-1,central_point.x);
		neighborhood[2] = image.at<uchar>(central_point.y-1,central_point.x+1);
		neighborhood[3] = image.at<uchar>(central_point.y,central_point.x-1);
		neighborhood[4] = image.at<uchar>(central_point.y,central_point.x+1);
		neighborhood[5] = image.at<uchar>(central_point.y+1,central_point.x-1);
		neighborhood[6] = image.at<uchar>(central_point.y+1,central_point.x);
		neighborhood[7] = image.at<uchar>(central_point.y+1,central_point.x+1);
	}
}

/**!
\author Clement.D
\brief max value of neighborhood
!**/
int max_neighborhood(Mat image, Point central_point){
	int indice;
	int max_result = -1;

	int neighborhood[8];
 	get_neighborhood(image, central_point ,neighborhood);
	for(indice=0; indice < 8; indice++){
		max_result = max(max_result, neighborhood[indice]);
	}
	return max_result;
}

/**!
\author Clement.D
\brief min value of neighborhood
!**/
int min_neighborhood(Mat image, Point central_point){
	int indice;
	int min_result = INT_MAX;
	int neighborhood[8];
	get_neighborhood(image,central_point,neighborhood);
	for(indice=0; indice < 8; indice++){
		min_result = min(min_result, neighborhood[indice]);
	}
	return min_result;
}

/**!
\author Clement.D
\brief do the sum of the dirence between the neighborhood of two pixel that are not on the same image
the value is always positive
image must have the same size
!**/
int sum_difference_pixel(Mat image_A, Point point_A, Mat image_B, Point point_B){
	int neighborhood_A[8];
	get_neighborhood(image_A,point_A, neighborhood_A);
	int neighborhood_B[8];
	get_neighborhood(image_B,point_B, neighborhood_B);
	int index;
	int sum_result = 0;
	for(index = 0; index < 8; index++){
		sum_result += neighborhood_A[index] - neighborhood_B[index];
	}
	return abs(sum_result);
}
/**!
\author Clement.D
\brief return the distance between two point
!**/
int norme_euclidian(Point p1, Point p2){
	return sqrt(pow(p1.x - p2.x,2) + pow(p1.y - p2.y,2));
}

/**!
\author Clement.D
\brief erode_clement an image and return the result image
!**/
Mat erode_clement(Mat image){
	int x;
	int y;
	Mat image_return = image.clone();
	for(x = 0; x < image.cols;x++){
		for(y = 0; y < image.rows; y++){
			image_return.at<uchar>(y,x) =  max_neighborhood(image,Point(x,y));
		}
	}
	return image_return;
}

/**!
\author Clement.D
\brief dilate_clement an image and return the result image
!**/
Mat dilate_clement(Mat image){
	int x;
	int y;
	Mat image_return = image.clone();
	for(x = 0; x < image.cols;x++){
		for(y = 0; y < image.rows; y++){
			image_return.at<uchar>(y,x) =  min_neighborhood(image,Point(x,y));
		}
	}
	return image_return;
}

vector<int> create_LUT(Mat angle_alpha, Mat angle_beta){
	//to be sure 
	vector<int> LUT(256, -1);
	cvSetZero(voting_mat);
	int val_beta = angle_beta.at<uchar>(y,x);
	for(x = 0; x < pic.cols;x++){
		for(y = 0; y < pic.rows; y++){
			val_beta = angle_beta.at<uchar>(y,x);
			if(val_beta > 0){
				LUT[angle_alpha.at<uchar>(y,x)] = val_beta;
			} 
		}
	}
	return LUT;
}

Mat get_angle_alpha(Mat pic, vector<vector<int> > &Ix, vector<vector<int> > &Iy){
	Mat angle = pic.clone();
	int x,y; 
	for(x = 0; x < pic.cols;x++){
		for(y = 0; y < pic.rows; y++){
			double val = atan2((double)Iy[x][y],(double)Ix[x][y]);
			if(val < 0){
				angle.at<uchar>(y,x) = (int)(((val * 57.29)+360)/360*255);
			}else{
				angle.at<uchar>(y,x) = (int)(((val * 57.29)/360)*255);				
			}
			std::cout << (int)(val * 57.29) << " " << Iy[x][y] << " " << Ix[x][y]<< std::endl;
		}
	}
	std::cout << std::endl;
	return angle;
}

Mat get_angle_beta(Mat pic, vector<vector<int> > &Ix, vector<vector<int> > &Iy){
	Mat gradient_magnitude = pic.clone();
	Mat angle = pic.clone();
	int x,y;
	int TRESHOLD = 125;
	int sum_x = 0,sum_y=0, count=0; 
	//calculate gradient_magnitude
	for(x = 0; x < pic.cols;x++){
		for(y = 0; y < pic.rows; y++){
			double val = sqrt(pow((double)Iy[x][y],2) + pow((double)Ix[x][y],2));
			if(val > TRESHOLD){
				sum_x += x;
				sum_y += y;
				count += 1;
			gradient_magnitude.at<uchar>(y,x) = (int)val;
			std::cout << val << " " << Iy[x][y] << " " << Ix[x][y]<< std::endl;
			}else{
				gradient_magnitude.at<uchar>(y,x)  = 0;
			}
		}
	}
	Point gravity_center = Point((int)(sum_x/count),(int) (sum_y/count));
	cout << gravity_center << endl;
	//calculate angle between center of gravity and border on horizontal axis
	for(x = 0; x < pic.cols;x++){
		for(y = 0; y < pic.rows; y++){
			
			if(gradient_magnitude.at<uchar>(y,x) > TRESHOLD){ 
				double val = atan2((double)gravity_center.y - y,(double) gravity_center.x - x);
				// atan2 return resultat in radian so we must multiply by 180/PI
				if(val < 0){
					angle.at<uchar>(y,x) = (int)((((val * 57.29)+360)/360) * 255);
				}else{
					angle.at<uchar>(y,x) = (int)(((val * 57.29)/360)*255);					
				}
				std::cout << (int)(val * 57.29) << "x " << y << "y " << x << std::endl;

			}else{
				angle.at<uchar>(y,x) = 0;
			}
		}
	}
	return angle;
}
Mat get_Mat_from_vector(Mat pic, vector<vector<int> > a){
	Mat result = pic.clone();
	int x;
	int y;
	for(x = 0; x < pic.cols;x++){
		for(y = 0; y < pic.rows; y++){
			result.at<uchar>(y,x) =  a[x][y];
		}
	}
	return result;
}

Mat recognition_face(Mat pic){
	vector<vector<int> > Ix(pic.cols, vector<int>(pic.rows,0));
	vector<vector<int> > Iy(pic.cols, vector<int>(pic.rows,0));	
	sobel(pic, Ix, Iy);
	Mat angle_alpha = get_angle_alpha(pic,Ix,Iy);
	Mat angle_beta = get_angle_beta(pic,Ix,Iy);

	return angle_beta;
}
