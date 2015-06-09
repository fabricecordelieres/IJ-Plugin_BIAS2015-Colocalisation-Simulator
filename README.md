# BIAS2015-Colocalisation-Simulator

*BIAS2015 Colocalisation Simulator is an ImageJ plugin designed to generate synthetic images in order to teach and test co-localisation problematics*

![alt tag](https://github.com/fab2506/BIAS2015-Colocalisation-Simulator/blob/master/img/BIAS_Coloc_Simulator.jpg)

#Simulation types:

* *Random:*
  * Randomly place shapes on channel 1 and 2.
* *Pure co-localisation:*
  * Places shapes on channel 1, depending on parameters entered under the channel 1 section. It then copies the channel 1 to generate channel 2.
* *Exclusion:*
  * Not yet implemented...
* *Inclusion:*
  * Uses parameters from channel 1 to generate hollow shapes, hollow part being tailored to accommodate shapes from channel 2.

#General parameters:
* *Image dimensions:*
  * Dimensions of the image to be generated.
* *Chromatic shift:*
  * Shift to be applied to objects on channel 2, expressed in pixels, in X, Y and Z. Negative values allowed.
* *Field non-homogeneity:*
  * Field uneven illumination is generated in the form of a Gaussian beam, centered on the provided XY coordinates.
* *Bleedthrough:*
  * Percentage of signal in channel 1 leaking into channel 2 and vice-versa.	

#Shape types:
* *Parallelepiped:*
  *Parameters: number of elements to draw, width, height and depth of the bounding box.
* *Ellipsoid:*
  * Parameters: number of elements to draw, width, height and depth of the bounding box.	
* *Cylinder:*
  * Parameters: number of elements to draw, width, height and depth of the bounding box.
  * Orientation: the first element is drawn with the circular section in the XY plane, the second in the XZ plane, the third in the YZ plane, the fourth on the XY etc...
* *Cone:*
  * Parameters: number of elements to draw, width, height and depth of the bounding box.
  * Orientation: the first element is drawn with the circular section in the XY plane, the second in the XZ plane, the third in the YZ plane, the fourth on the XY etc...
* *Torus:*
  * Parameters: number of elements to draw, width, height and depth of the bounding box, the largest radius and the smallest radius of the torus.
  * Orientation: the first element is drawn with the circular section in the XY plane, the second in the XZ plane, the third in the YZ plane, the fourth on the XY etc...
* *Helix:*
  * Parameters: number of elements to draw, width, height and depth of the bounding box, the largest radius, the smallest radius and the step of the helix.
  * Orientation: the first element is drawn with the circular section in the XY plane, the second in the XZ plane, the third in the YZ plane, the fourth on the XY etc...
