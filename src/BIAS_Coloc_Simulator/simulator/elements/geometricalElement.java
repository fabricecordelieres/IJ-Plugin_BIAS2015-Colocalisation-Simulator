/**
  * geometricalElement.java v1, 20 mai 2015
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

import ij.ImagePlus;
import ij.gui.NewImage;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Fabrice P Cordelieres
 *
 */
public class geometricalElement {
	/** XYZ centre coordinates (top left upper point) **/
	public point3D centrePoint;
	
	/** Size of the bounding box along the XYZ axis **/
	public point3D boundingBox;
	
	/** Size of the inner bounding box along the XYZ axis for emptying part of the element **/
	public point3D innerBoundingBox;
	
	/** For torus: r **/
	public int rTorus=5;
	
	/** For torus: R **/
	public int RTorus=10;
	
	/** For helix: step **/
	public int step=5;
	
	
	/** List of points constitutive of the element **/
	public ArrayList<point3D> points=new ArrayList<point3D>();
	
	/** Type **/
	public int type=0;
	
	/** Filling type: parallelepiped **/
	static final int PARALLELEPIPED=0;
	
	/** Filling type: ellipsoid **/
	static final int ELLIPSOID=1;
	
	/** Filling type: cylinderXY **/
	static final int CYLINDER_XY=2;
	
	/** Filling type: cylinderXZ **/
	static final int CYLINDER_XZ=3;
	
	/** Filling type: cylinderYZ **/
	static final int CYLINDER_YZ=4;
	
	/** Filling type: coneXY **/
	static final int CONE_XY=5;
	
	/** Filling type: coneXZ **/
	static final int CONE_XZ=6;
	
	/** Filling type: coneYZ **/
	static final int CONE_YZ=7;
	
	/** Filling type: torusXY **/
	static final int TORUS_XY=8;
	
	/** Filling type: torusXZ **/
	static final int TORUS_XZ=9;
	
	/** Filling type: torusYZ **/
	static final int TORUS_YZ=10;
	
	/** Filling type: helixXY **/
	static final int HELIX_XY=11;
	
	/** Filling type: helixXZ **/
	static final int HELIX_XZ=12;
	
	/** Filling type: helixYZ **/
	static final int HELIX_YZ=13;
	
	/**
	 * Creates a new geometrical element based on input parameters
	 * @param centrePoint XYZ coordinate of the top left upper corner
	 * @param boundingBox size of the bounding box along the XYZ axis
	 * @param intensity default intensity
	 */
	public geometricalElement(point3D centrePoint, point3D boundingBox, double intensity){
		type=0;
		this.centrePoint=centrePoint;
		this.boundingBox=boundingBox;
		this.innerBoundingBox=null;
		initiateElement(intensity);
	}
	
	/**
	 * Creates a new geometrical element based on input parameters, default intensity being 0
	 * @param centrePoint XYZ coordinate of the top left upper corner
	 * @param boundingBox size of the bounding box along the XYZ axis
	 */
	public geometricalElement(point3D centrePoint, point3D boundingBox){
		this(centrePoint, boundingBox, 0);
	}
	
	/**
	 * Sets the type
	 * @param type type
	 */
	public void setType(int type) {
		this.type=type;
	}
	
	
	/**
	 * Sets the r torus
	 * @param r r torus
	 */
	public void setrTorus(int r){
		rTorus=r;
		initiateElement(0);
	}
	
	/**
	 * Sets the R torus
	 * @param R R torus
	 */
	public void setRTorus(int R){
		RTorus=R;
		initiateElement(0);
	}
	
	/**
	 * Sets the helix step
	 * @param s step
	 */
	public void setStep(int s){
		step=s;
		initiateElement(0);
	}
	
	/**
	 * Generates the points with the bounding box, attributing the input intensity to all points
	 * @param intensity default intensity
	 */
	public void initiateElement(double intensity){
		points=new ArrayList<point3D>();
		for(int zPos=centrePoint.z-boundingBox.z/2; zPos<centrePoint.z+boundingBox.z/2; zPos++){
			for(int yPos=centrePoint.y-boundingBox.y/2; yPos<centrePoint.y+boundingBox.y/2; yPos++){
				for(int xPos=centrePoint.x-boundingBox.x/2; xPos<centrePoint.x+boundingBox.x/2; xPos++){
					points.add(new point3D(xPos, yPos, zPos, intensity));
				}
			}
		}
	}	

	/**
	 * Creates a filled volume, defined by the input type, with a single intensity
	 * @param type type of volume to fill
	 * @param intensity intensity to apply to the element
	 */
	public void fill(int type, double intensity){
		points=new ArrayList<point3D>();
		
		this.type=type;
		
		double radX=boundingBox.x/2.0;
		double radY=boundingBox.y/2.0;
		double radZ=boundingBox.z/2.0;
		
		double tanAngleX=Math.min(radY, radZ)/(2.0*radX);
		double tanAngleY=Math.min(radX, radZ)/(2.0*radY);
		double tanAngleZ=Math.min(radX, radY)/(2.0*radZ);
		
		double innerRadX=innerBoundingBox==null?0.0:innerBoundingBox.x/2.0;
		double innerRadY=innerBoundingBox==null?0.0:innerBoundingBox.y/2.0;
		double innerRadZ=innerBoundingBox==null?0.0:innerBoundingBox.z/2.0;
		
		double tanInnerAngleX=innerBoundingBox==null?0.0:Math.min(innerRadY, innerRadZ)/(2.0*innerRadX);
		double tanInnerAngleY=innerBoundingBox==null?0.0:Math.min(innerRadX, innerRadZ)/(2.0*innerRadY);
		double tanInnerAngleZ=innerBoundingBox==null?0.0:Math.min(innerRadX, innerRadY)/(2.0*innerRadZ);
		
		
		Random randomIntensity=new Random();
		
		for(int z=(int) (centrePoint.z-radZ); z<centrePoint.z+radZ; z++){
			for(int y=(int) (centrePoint.y-radY); y<centrePoint.y+radY; y++){
				for(int x=(int) (centrePoint.x-radX); x<centrePoint.x+radX; x++){
					boolean put=false;
					
					int xPos=x-centrePoint.x;
					int yPos=y-centrePoint.y;
					int zPos=z-centrePoint.z;
					
					
					double intensityToPut=randomIntensity.nextGaussian()*Math.sqrt(intensity)+intensity;
					
					switch(type){
						case PARALLELEPIPED:
							put=true;
							
							//Handle hollow elements
							put=innerBoundingBox==null?put:
								!(x>=centrePoint.x-innerRadX && x<centrePoint.x+innerRadX
								&& y>=centrePoint.y-innerRadY && y<centrePoint.y+innerRadY
								&& z>=centrePoint.z-innerRadZ && z<centrePoint.z+innerRadZ);
							break;
						
						case ELLIPSOID:
							put=(((double) xPos*xPos/(radX*radX))+((double) yPos*yPos/(radY*radY))+((double) zPos*zPos/(radZ*radZ))<=1.0);
							
							//Handle hollow elements
							put=innerBoundingBox==null?put:
								put && !(((double) xPos*xPos/(innerRadX*innerRadX))+((double) yPos*yPos/(innerRadY*innerRadY))+((double) zPos*zPos/(innerRadZ*innerRadZ))<=1.0);
							break;
					
						case CYLINDER_XY:
							put=(((double) xPos*xPos/(radX*radX))+((double) yPos*yPos/(radY*radY))<=1.0);
							
							//Handle hollow elements
							put=innerBoundingBox==null?put:
								put && !(((double) xPos*xPos/(innerRadX*innerRadX))+((double) yPos*yPos/(innerRadY*innerRadY))<=1.0);
							break;
							
						case CYLINDER_XZ:
							put=(((double) xPos*xPos/(radX*radX))+((double) zPos*zPos/(radZ*radZ))<=1.0);
							
							//Handle hollow elements
							put=innerBoundingBox==null?put:
								put && !(((double) xPos*xPos/(innerRadX*innerRadX))+((double) zPos*zPos/(innerRadZ*innerRadZ))<=1.0);
							break;
						
						case CYLINDER_YZ:
							put=(((double) yPos*yPos/(radY*radY))+((double) zPos*zPos/(radZ*radZ))<=1.0);
							
							//Handle hollow elements
							put=innerBoundingBox==null?put:
								put && !(((double) yPos*yPos/(innerRadY*innerRadY))+((double) zPos*zPos/(innerRadZ*innerRadZ))<=1.0);
							break;
							
						case CONE_XY:
							put=(((double) xPos*xPos/(radX*radX))+((double) yPos*yPos/(radY*radY))<=((double) (zPos+radZ)*(zPos+radZ)/(4*radZ*radZ)*tanAngleZ*tanAngleZ));
							
							//Handle hollow elements
							put=innerBoundingBox==null?put:
								put && !(((double) xPos*xPos/(innerRadX*innerRadX))+((double) yPos*yPos/(innerRadY*innerRadY))<=((double) (zPos+innerRadZ)*(zPos+innerRadZ)/(4*innerRadZ*innerRadZ)*tanInnerAngleZ*tanInnerAngleZ));
							break;
						
						case CONE_XZ:
							put=(((double) xPos*xPos/(radX*radX))+((double) zPos*zPos/(radZ*radZ))<=((double) (yPos+radY)*(yPos+radY)/(4*radY*radY)*tanAngleY*tanAngleY));
							
							//Handle hollow elements
							put=innerBoundingBox==null?put:
								put && !(((double) xPos*xPos/(innerRadX*innerRadX))+((double) zPos*zPos/(innerRadZ*innerRadZ))<=((double) (yPos+innerRadY)*(yPos+innerRadY)/(4*innerRadY*innerRadY)*tanInnerAngleY*tanInnerAngleY));
							break;
							
						case CONE_YZ:
							put=(((double) yPos*yPos/(radY*radY))+((double) zPos*zPos/(radZ*radZ))<=((double) (xPos+radX)*(xPos+radX)/(4*radX*radX)*tanAngleX*tanAngleX));
							
							//Handle hollow elements
							put=innerBoundingBox==null?put:
								put && !(((double) yPos*yPos/(innerRadY*innerRadY))+((double) zPos*zPos/(innerRadZ*innerRadZ))<=((double) (xPos+innerRadX)*(xPos+innerRadX)/(4*innerRadX*innerRadX)*tanInnerAngleX*tanInnerAngleX));
							break;
							
						case TORUS_XY:
							put=((double) RTorus-Math.sqrt(xPos*xPos+yPos*yPos))*((double) RTorus-Math.sqrt(xPos*xPos+yPos*yPos))+zPos*zPos<=(double) rTorus*rTorus;
							break;
							
						case TORUS_XZ:
							put=((double) RTorus-Math.sqrt(xPos*xPos+zPos*zPos))*((double) RTorus-Math.sqrt(xPos*xPos+zPos*zPos))+yPos*yPos<=(double) rTorus*rTorus;
							break;
							
						case TORUS_YZ:
							put=((double) RTorus-Math.sqrt(yPos*yPos+zPos*zPos))*((double) RTorus-Math.sqrt(yPos*yPos+zPos*zPos))+xPos*xPos<=(double) rTorus*rTorus;
							break;
							
						case HELIX_XY:
							put=(xPos-RTorus*Math.cos(zPos/step))*(xPos-RTorus*Math.cos(zPos/step))
									+(yPos-RTorus*Math.sin(zPos/step))*(yPos-RTorus*Math.sin(zPos/step))
									<=rTorus*rTorus;
							break;
							
						case HELIX_XZ:
							put=(xPos-RTorus*Math.cos(yPos/step))*(xPos-RTorus*Math.cos(yPos/step))
									+(zPos-RTorus*Math.sin(yPos/step))*(zPos-RTorus*Math.sin(yPos/step))
									<=rTorus*rTorus;
							break;
						
						case HELIX_YZ:
							put=(yPos-RTorus*Math.cos(xPos/step))*(yPos-RTorus*Math.cos(xPos/step))
									+(zPos-RTorus*Math.sin(xPos/step))*(zPos-RTorus*Math.sin(xPos/step))
									<=rTorus*rTorus;
							break;
							
					}
					points.add(new point3D(x, y, z, put?(intensityToPut):0));
				}
			}
		}
	}
	
	/**
	 * Creates a filled volume, defined by the object's type, with a single intensity
	 * @param intensity intensity to apply to the element
	 */
	public void fill(double intensity){
		fill(type, intensity);
	}
	
	/**
	 * Creates a parallelepiped rectangle with a single intensity
	 * @param intensity intensity to apply to the element
	 */
	public void fillParallelepiped(int intensity){
		fill(PARALLELEPIPED, intensity);
	}
	
	/**
	 * Creates a ellipsoid with a single intensity
	 * @param intensity intensity to apply to the element
	 */
	public void fillEllipsoid(int intensity){
		fill(ELLIPSOID, intensity);
	}
	
	/**
	 * Creates a cylinder, the circle being in the XY plane, with a single intensity
	 * @param intensity intensity to apply to the element
	 */
	public void fillCylinderXY(int intensity){
		fill(CYLINDER_XY, intensity);
	}
	
	/**
	 * Creates a cylinder, the circle being in the XZ plane, with a single intensity
	 * @param intensity intensity to apply to the element
	 */
	public void fillCylinderXZ(int intensity){
		fill(CYLINDER_XZ, intensity);
	}
	
	/**
	 * Creates a cylinder, the circle being in the YZ plane, with a single intensity
	 * @param intensity intensity to apply to the element
	 */
	public void fillCylinderYZ(int intensity){
		fill(CYLINDER_YZ, intensity);
	}
	
	/**
	 * Creates a cone, the circle being in the XY plane, with a single intensity
	 * @param intensity intensity to apply to the element
	 */
	public void fillConeXY(int intensity){
		fill(CONE_XY, intensity);
	}
	
	/**
	 * Creates a cone, the circle being in the XZ plane, with a single intensity
	 * @param intensity intensity to apply to the element
	 */
	public void fillConeXZ(int intensity){
		fill(CONE_XZ, intensity);
	}
	
	/**
	 * Creates a cone, the circle being in the YZ plane, with a single intensity
	 * @param intensity intensity to apply to the element
	 */
	public void fillConeYZ(int intensity){
		fill(CONE_YZ, intensity);
	}
	
	/**
	 * Creates a torus, the axis being orthogonal to the XY plane, with a single intensity
	 * @param intensity intensity to apply to the element
	 */
	public void fillTorusXY(int intensity){
		fill(TORUS_XY, intensity);
	}
	
	/**
	 * Creates a torus, the axis being orthogonal to the XZ plane, with a single intensity
	 * @param intensity intensity to apply to the element
	 */
	public void fillTorusXZ(int intensity){
		fill(TORUS_XZ, intensity);
	}
	
	/**
	 * Creates a torus, the axis being orthogonal to the YZ plane, with a single intensity
	 * @param intensity intensity to apply to the element
	 */
	public void fillTorusYZ(int intensity){
		fill(TORUS_YZ, intensity);
	}
	
	/**
	 * Shift the current element by the input vector, given as a point3D
	 * @param shift the shift to apply, as a point3D
	 */
	public void shift(point3D shift){
		centrePoint.shift(shift);
		
		for(int i=0; i<points.size(); i++) points.get(i).shift(shift);
	}
	
	/**
	 * Checks if the input point3D is included in the current element's bounding box
	 * @param point the input point3D
	 * @return a boolean
	 */
	public boolean contains(point3D point){
		return point.x>=centrePoint.x-boundingBox.x/2 && point.x<centrePoint.x+boundingBox.x/2
				&& point.y>=centrePoint.y-boundingBox.y/2 && point.y<centrePoint.y+boundingBox.y/2
				&& point.z>=centrePoint.z-boundingBox.z/2 && point.z<centrePoint.z+boundingBox.z/2;
	}

	/**
	 * Returns a representation of the current geometrical element as an ImagePlus
	 * @return a representation of the current geometrical element as an ImagePlus
	 */
	public ImagePlus getImage(){
		ImagePlus out=NewImage.createImage("Element", centrePoint.x+boundingBox.x/2, centrePoint.y+boundingBox.y/2, centrePoint.z+boundingBox.z/2, 32, NewImage.FILL_BLACK);
		
		for(int i=0; i<points.size(); i++){
			point3D currentPoint=points.get(i);
			out.setSlice(currentPoint.getZ()+1);
			out.getProcessor().putPixelValue(currentPoint.getX(), currentPoint.getY(), currentPoint.getIntensity());
		}
		
		return out;
		
	}
	
	/**
	 * Draw the current geometrical element on the input image after checking boundaries conditions
	 * @param in the Imageplus to write on
	 */
	public void drawOnImage(ImagePlus in){
		for(int i=0; i<points.size(); i++){
			point3D currentPoint=points.get(i);
			if(currentPoint.getZ()>=1 && currentPoint.getZ()<=in.getNSlices()
				&& currentPoint.getX()>=0 && currentPoint.getX()<in.getWidth()
				&& currentPoint.getY()>=0 && currentPoint.getY()<in.getHeight()){
				in.setSlice(currentPoint.getZ()+1);
				float currInt=in.getProcessor().getPixelValue(currentPoint.getX(), currentPoint.getY());
				in.getProcessor().putPixelValue(currentPoint.getX(), currentPoint.getY(), currInt+currentPoint.getIntensity());
			}
		}
	}
	
	/**
	 * Returns a copy of the current element
	 * @return a copy of the current element
	 */
	public geometricalElement duplicate(){
		geometricalElement out=new geometricalElement(centrePoint, boundingBox);
		out.innerBoundingBox=innerBoundingBox;
		out.rTorus=rTorus;
		out.RTorus=RTorus;
		out.step=step;
		
		for(int i=0; i<points.size(); i++){
			out.points.add(points.get(i).duplicate());
		}
		
		out.type=type;
		
		return out;
	}
}
