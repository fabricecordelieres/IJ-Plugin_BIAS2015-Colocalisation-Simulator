/**
  * elementParameter.java v1, 30 mai 2015
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
package BIAS_Coloc_Simulator.GUI.parameters;

import javax.swing.table.DefaultTableModel;

import BIAS_Coloc_Simulator.simulator.elements.geometricalElement;
import BIAS_Coloc_Simulator.simulator.elements.point3D;

/**
 * @author Fabrice P Cordelieres
 *
 */
public class elementParameters {
	/** Element name **/
	public String name="";
	/** True to draw the element **/
	public boolean draw=false;
	/** Number of element to draw **/
	public int nElements=0;
	/** Element's width **/
	public int width=0;
	/** Element's height **/
	public int height=0;
	/** Element's depth **/
	public int depth=0;
	/** Element's R (torus big radius) **/
	public int R=0;
	/** Element's r (torus small radius) **/
	public int r=0;
	/** Element's step (helix step) **/
	public int step=0;
	
	/**
	 * Read the parameters for a single elements from the table model
	 * @param model the table model to read from
	 * @param row the row to read
	 */
	public elementParameters(DefaultTableModel model, int row){
		name=(String) model.getValueAt(row, 0);
		draw=(Boolean) model.getValueAt(row, 1);
		nElements=draw?(Integer) model.getValueAt(row, 2):0;
		width=(Integer) model.getValueAt(row, 3);
		height=(Integer) model.getValueAt(row, 4);
		depth=(Integer) model.getValueAt(row, 5);
		R=model.getValueAt(row, 6)!=null?(Integer) model.getValueAt(row, 6):-1;
		r=model.getValueAt(row, 7)!=null?(Integer) model.getValueAt(row, 7):-1;
		step=model.getValueAt(row, 8)!=null?(Integer) model.getValueAt(row, 8):-1;
	}
	
	/**
	 * Creates a geometrical element from the elementParameters
	 * @param modulo index, to determine the sub-type/orientation of the geometrical element
	 * @param dimensions image's dimensions, used to randomly pick-up the element's centre
	 * @param intensity target intensity
	 * @return the current element as a geometrical element (filled)
	 */
	public geometricalElement getGeometricalElement(int modulo, point3D dimensions, double intensity){
		int type=0;
		if(name=="Ellipsoid") type=1;
		if(name=="Cylinder") type=modulo%3+2;
		if(name=="Cone") type=modulo%3+5;
		if(name=="Torus") type=modulo%3+8;
		if(name=="Helix") type=modulo%3+11;
		
		point3D centrePoint=new point3D();
		centrePoint.generateRandomCoordinates(dimensions.getX(), dimensions.getY(), dimensions.getZ(), intensity);
		
		point3D boundingBox=new point3D(width, height, depth, intensity);
		
		geometricalElement element=new geometricalElement(centrePoint, boundingBox, intensity);
		element.setType(type);
		if(name=="Torus" || name=="Helix"){
			element.setRTorus(R);
			element.setRTorus(r);
		}
		if(name=="Helix") element.setStep(step);
		
		
		return element;
	}
	
	@Override
	public String toString(){
		return
				name+"-draw="+draw+"\n"+
				name+"-nElements="+nElements+"\n"+
				name+"-width="+width+"\n"+
				name+"-height="+height+"\n"+
				name+"-depth="+depth+"\n"+
				((name.equals("Torus")||name.equals("Helix"))?
						name+"-R="+R+"\n"+
						name+"-r="+r+"\n"
						:"")+
				(name.equals("Helix")?
						name+"-step="+step
						:"");
	}
}
