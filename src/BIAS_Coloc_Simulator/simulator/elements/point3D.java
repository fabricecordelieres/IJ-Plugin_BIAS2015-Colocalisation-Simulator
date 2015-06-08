/**
  * point3D.java v1, 20 mai 2015
    Fabrice P Cordelieres, fabrice.cordelieres at gmail.com
    
    Copyright (C) 2015 Fabrice P. Cordelieres
  
    License:
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package BIAS_Coloc_Simulator.simulator.elements;

import java.util.Random;

/**
 * @author Fabrice P Cordelieres
 *
 */
public class point3D {
	/** X coordinate **/
	int x=0;
	
	/** Y coordinate **/
	int y=0;
	
	/** Z coordinate **/
	int z=0;
	
	/** Intensity **/
	double intensity=0.0;
	
	/**
	 * Creates a new point3D
	 */
	public point3D(){
	}
	
	/**
	 * Creates a new point3D, based on input information
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 * @param intensity intensity to be associated to the point
	 */
	public point3D(int x, int y, int z, double intensity){
		setCoordinates(x, y, z);
		setIntensity(intensity);
	}
	
	/**
	 * Creates a new point3D, based on input information
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 */
	public point3D(int x, int y, int z){
		this(x, y, z, 0);
	}
	
	
	/**
	 * Creates a new random point3D, based on input information
	 * @param width max x
	 * @param height max y coordinate
	 * @param depth max z coordinate
	 * @param intensity intensity to be associated to the point
	 */
	public void generateRandomCoordinates(int width, int height, int depth, double intensity){
		setCoordinates(((int) (width*(new Random().nextDouble()))), ((int) (height*(new Random().nextDouble()))), ((int) (depth*(new Random().nextDouble()))));
		setIntensity(intensity);
	}
	
	/**
	 * Modifies the coordinates of the current point3D
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 */
	public void setCoordinates(int x, int y, int z){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	/**
	 * Shifts the current point3D by an input vector expressed as another point3D
	 * @param shift shift to be applied
	 */
	public void shift(point3D shift){
		x+=shift.x;
		y+=shift.y;
		z+=shift.z;
	}
	
	/**
	 * Modifies the intensity of the current point3D
	 * @param intensity intensity to be associated to the point
	 */
	public void setIntensity(double intensity){
		this.intensity=intensity;
	}
	
	/**
	 * Returns the x coordinates of the current point3D
	 * @return the x coordinates of the current point3D
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * Returns the y coordinates of the current point3D
	 * @return the y coordinates of the current point3D
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * Returns the z coordinates of the current point3D
	 * @return the z coordinates of the current point3D
	 */
	public int getZ(){
		return z;
	}
	
	/**
	 * Returns the intensity of the current point3D
	 * @return the intensity of the current point3D
	 */
	public double getIntensity(){
		return intensity;
	}
	
	/**
	 * Returns the distance between the current point3D and an additional input point3D
	 * @param point a point3D
	 * @return the distance
	 */
	public double getDistance(point3D point){
		return Math.sqrt((double) ((point.x-x)*(point.x-x)+(point.y-y)*(point.y-y)+(point.z-z)*(point.z-z)));
	}
	
	/**
	 * Checks if the input point3D has the same coordinates as the current one
	 * @param point input point3D
	 * @return a boolean
	 */
	public boolean hasSameCoordinates(point3D point){
		return x==point.x && y==point.y && z==point.z;
	}
	
	/**
	 * Returns the minimum coordinate amongst XYZ
	 * @return the minimum coordinate amongst XYZ
	 */
	public int getMinCoordinate(){
		return Math.min(Math.min(x, y), z);
	}
	
	/**
	 * Returns the maximum coordinate amongst XYZ
	 * @return the maximum coordinate amongst XYZ
	 */
	public int getMaxCoordinate(){
		return Math.max(Math.max(x, y), z);
	}
	
	@Override
	public String toString(){
		return 	"x="+x
				+"\ny="+y
				+"\nz="+z
				+"\nintensity="+intensity;
	}
	
	/**
	 * Duplicates the current point3D
	 * @return a copy of the current point3D
	 */
	public point3D duplicate(){
		return new point3D(x, y, z ,intensity);
	}
}
