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


class Gradient_magnitude{
	private:
		Mat gradient_magnitude;
		Point gravity_center;
	public:
		Gradient_magnitude(Mat pic,  vector<vector<int> > Ix,  vector<vector<int> > Iy, int treshold){
			gradient_magnitude = pic.clone();
			Mat angle = pic.clone();
			int x,y;
			int sum_x = 0,sum_y=0, count=0; 
			double val;
			//calculate gradient_magnitude
			for(x = 0; x < pic.cols;x++){
				for(y = 0; y < pic.rows; y++){
					//addition des gradient x et y
					val = sqrt(pow((double)Iy[x][y],2) + pow((double)Ix[x][y],2));
					if(val > treshold){
						sum_x += x;
						sum_y += y;
						count += 1;
						gradient_magnitude.at<uchar>(y,x) = (int)val;
						//	std::cout << val << " " << Iy[x][y] << " " << Ix[x][y]<< std::endl;
					}else{
						gradient_magnitude.at<uchar>(y,x)  = 0;
					}
				}
			}
			//calcul gravity center
			gravity_center = Point((int)(sum_x/count),(int) (sum_y/count));
		};
		Mat get_gradient_magnitude(){
			return gradient_magnitude;
		}
		Point get_gravity_center(){
			return gravity_center;
		}
};
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

Mat circle_eye(Mat pic, Point eye_center){
	
	Mat circled_eye = pic.clone();

	cvtColor(circled_eye, circled_eye, CV_GRAY2BGR);
 circle( circled_eye,
         eye_center,
         20.0,
         Scalar( 0, 0, 255 ),
         1,
         8 );
         
       return circled_eye;
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
	//cvSetZero(voting_mat);
	int val_beta;
	int x,y;
	for(x = 0; x < angle_beta.cols;x++){
		for(y = 0; y < angle_beta.rows; y++){
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
			
			//std::cout<< (int)(val * 57.29) << " " << Iy[x][y] << " " << Ix[x][y]<< std::endl;
			
		}
	}
	std::cout << std::endl;
	return angle;
}

Mat get_angle_beta(Mat pic, vector<vector<int> > &Ix, vector<vector<int> > &Iy, int treshold){
	Gradient_magnitude gradient_magnitude_beta(pic, Ix,Iy,treshold);
	Mat gradient_magnitude = gradient_magnitude_beta.get_gradient_magnitude();
	Mat angle = pic.clone();
	int x,y;
	
	Point gravity_center = gradient_magnitude_beta.get_gravity_center();
	
	//calculate angle between center of gravity and border on horizontal axis
	for(x = 0; x < pic.cols;x++){
		for(y = 0; y < pic.rows; y++){
			
			if(gradient_magnitude.at<uchar>(y,x) > treshold){ 
				double val = atan2(y - (double)gravity_center.y,x-(double) gravity_center.x );
				// atan2 return resultat in radian so we must multiply by 180/PI
				if(val < 0){
					angle.at<uchar>(y,x) = (int)((((val * 57.29)+360)/360) * 255);
				}else{
					angle.at<uchar>(y,x) = (int)(((val * 57.29)/360)*255);					
				}
			//	std::cout << (int)(val * 57.29) << "x " << y << "y " << x << std::endl;

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
/*
Mat voting_mat(Mat pic, vector<int> LUT, r){
	
}
*/
Mat get_contours(Mat pic,  vector<vector<int> > Ix,  vector<vector<int> > Iy, int treshold){
	Mat gradient_magnitude = pic.clone();
	Mat angle = pic.clone();
	int x,y;
	int TRESHOLD = 185;
	int sum_x = 0,sum_y=0, count=0; 
	//calculate gradient_magnitude
	for(x = 0; x < pic.cols;x++){
		for(y = 0; y < pic.rows; y++){
			double val = sqrt(pow((double)Iy[x][y],2) + pow((double)Ix[x][y],2));
			if(val > treshold){
				sum_x += x;
				sum_y += y;
				count += 1;
			gradient_magnitude.at<uchar>(y,x) = (int)val;
			//std::cout << val << " " << Iy[x][y] << " " << Ix[x][y]<< std::endl;
			}else{
				gradient_magnitude.at<uchar>(y,x)  = 0;
			}
		}
	}
	return gradient_magnitude;
}

Point detect_circle_center(Mat contours,  vector<vector<int> > Ix,  vector<vector<int> > Iy, vector<int> LUT){
	//showPic(contours);
	Mat alpha_map = contours.clone();
	vector<vector<int> > vote_map(contours.cols, vector<int>(contours.rows,0));
	//Mat vote_map = contours.clone();
	Mat vote_map_visualisation = contours.clone();
	
	//mettre à vide
	int x,y; 
	for(x = 0; x < contours.cols;x++){
		for(y = 0; y < contours.rows; y++){
			vote_map_visualisation.at<uchar>(y,x) = 0;
		}
	}
	
	//Creation de la map d'alpha
	for(x = 0; x < contours.cols;x++){
		for(y = 0; y < contours.rows; y++){
			if(contours.at<uchar>(y,x) > 0){
				double val = atan2((double)Iy[x][y],(double)Ix[x][y]);
				if(val < 0){
				alpha_map.at<uchar>(y,x) = (int)(((val * 57.29)+360)/360*255);
				}else{
					alpha_map.at<uchar>(y,x) = (int)(((val * 57.29)/360)*255);				
				}
			//std::cout << (int)(val * 57.29) << " " << Iy[x][y] << " " << Ix[x][y]<< std::endl;
			}else{
				alpha_map.at<uchar>(y,x) = 0;
			}
		}
	}
	
	//Vote des Betas 
	for(x = 0; x < alpha_map.cols;x++){
		for(y = 0; y < alpha_map.rows; y++){
			int val_alpha = alpha_map.at<uchar>(y,x);
			if(val_alpha != 0){
				int beta = LUT[val_alpha];
				if(beta != -1){
					
					//Les bons points de contours sont selectionnés
					//cout << "point de contours x : " << x << "  y : " << y << "  Beta Angle : " << beta << endl;
					
					int normalized_beta =(int) (((double)beta/255.0)*360.0);
					double radiant_beta = normalized_beta * 3.14 / 180.0;
					int x_parcours;
					int y_parcours;
						for(int i =3;i<30;i++){
							x_parcours =(int)(x + -i*cos(radiant_beta));
							y_parcours = (int)(y + -i*sin (radiant_beta));
							if(x_parcours < vote_map_visualisation.cols && x_parcours > 0 && y_parcours < vote_map_visualisation.rows && y_parcours >0){
								vote_map[x_parcours][y_parcours] += 1;
						}
					}
				}
			}
		}
	}
	
	int max_value= 0;
	int max_value_2 = 0;

	Point point_vote_1;
	Point point_vote_2;
	Point point_vote_temp;
	
	point_vote_2.x = 0;
	point_vote_2.y = 0;
	
	point_vote_temp.x = 0;
	point_vote_temp.y = 0;
	
	point_vote_1.x = 0;
	point_vote_1.y = 0;
	
	//Reccuperation du 1er point les plus importants
	for(x = 0; x < vote_map_visualisation.cols;x++){
		for(y = 0; y < vote_map_visualisation.rows; y++){	
				if( vote_map[x][y] > max_value){
					
					max_value= vote_map[x][y];
					
					
					point_vote_1.x = x;
					point_vote_1.y = y;
						
				
			}
		}
	}
	//Reccuperation du 2eme point

	for(x = 0; x < vote_map_visualisation.cols;x++){
		for(y = 0; y < vote_map_visualisation.rows; y++){	
			if( vote_map[x][y] > max_value_2){
				
						point_vote_temp.x = x;
						point_vote_temp.y = y;
				
				if(sqrt( pow((point_vote_1.x - point_vote_temp.x ),2) + pow((point_vote_1.y - point_vote_temp.y),2)) > 80){
						
						max_value_2 = vote_map[x][y];
						
						point_vote_2.x = x;
						point_vote_2.y = y;
						
				}
			}
		}
	}
	

	
	//Normalisation pour visualisation
	for(x = 0; x < vote_map_visualisation.cols;x++){
		for(y = 0; y < vote_map_visualisation.rows; y++){	
			
			vote_map_visualisation.at<uchar>(y,x) = (int)(((double)vote_map[x][y]/(double)max_value)*255.0);
			
			//cout << (int)vote_map_visualisation.at<uchar>(y,x) << endl;
		}
	}
	
	//cout << "Point max 1  : x =  " << point_vote_1.x << " y = "<< point_vote_1.y << "Point_max_2 : x = "<< point_vote_2.x<< "  y  = "<< point_vote_2.y << endl;			
	
	Mat circle_point = circle_eye(vote_map_visualisation,point_vote_1);
	//showPic(circle_point);
	imwrite( "./demo.jpg", circle_point );
	
	return point_vote_1;
}


 
 vector<float> create_histo(Point eye_center,int radius,Mat analyse){
	 
	std::vector<int> _values;
	std::vector<float> _proba;
	int MAX_GRAY_VALUE = 255;
	
	_values.resize(MAX_GRAY_VALUE);
	//cout << "max gray value : " << image._max_gray_value <<endl;
	for(int col = eye_center.x - radius ; col < eye_center.x + radius; col++){
		for(int row = eye_center.y - radius; row < eye_center.y + radius; row++){
			_values[(int)analyse.at<uchar>(row,col)] = ++_values[(int)analyse.at<uchar>(row,col)];
			//cout << _values[(int)analyse.at<uchar>(row,col)] << endl;
		}
	}
	
	/*
	int count = 0;
	for(int grey = 0; grey < MAX_GRAY_VALUE; grey++){
		cout << _values[grey] << endl;
		count+=_values[grey];
	}
	cout<<count<<endl;
	*/
	
	_proba.resize(MAX_GRAY_VALUE);
	double count = 0.0;
	for(int grey = 0; grey < MAX_GRAY_VALUE; grey++){

				_proba[grey] = ((double)_values[grey]) / ((double)radius*radius*4);
				//cout << "indice : "<< grey<<  "% : "<<_proba[grey] << endl;
				//count+= _proba[grey];
				
		}
	//cout<< count << "  <----- should be 1 " << endl;
	
	return _proba;
 }

vector<float> recognition_face(Mat shape, Mat analyse, int treshold_lut, int treshold_face){
	vector<vector<int> > Ix(shape.cols, vector<int>(shape.rows,0));
	vector<vector<int> > Iy(shape.cols, vector<int>(shape.rows,0));	
	sobel(shape, Ix, Iy);
	Mat angle_alpha = get_angle_alpha(shape,Ix,Iy);
	//showPic(angle_alpha);
	Mat angle_beta = get_angle_beta(shape,Ix,Iy,treshold_lut);
	vector<int> LUT = create_LUT(angle_alpha,angle_beta);
	
	for(int i=0; i < 255; i++){
	//cout << LUT[i] << endl;
	}
	//Mat voting_mat(shape,LUT,20);

	//Calculer Ix Iy de la nouvelle image (Sobel)
	vector<vector<int> > Ix_face(analyse.cols, vector<int>(analyse.rows,0));
	vector<vector<int> > Iy_face(analyse.cols, vector<int>(analyse.rows,0));	
	sobel(analyse, Ix_face, Iy_face);
	Mat contours_photo = get_contours(analyse, Ix_face, Iy_face,treshold_face);
	//showPic(contours_photo);
	
	Point eye_center = detect_circle_center(contours_photo,Ix_face, Iy_face, LUT);
	//showPic(centers);
	
	Mat circle_point = circle_eye(analyse,eye_center);
	
	//Show picture with circle around the eye
	//showPic(circle_point);
	
	int radius = 20;
	vector<float> histo = create_histo(eye_center,radius,analyse);
	//cout<< "histogramme calculé" << endl;
	
	return histo;
}
