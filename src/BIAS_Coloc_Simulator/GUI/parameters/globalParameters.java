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

import BIAS_Coloc_Simulator.simulator.elements.point3D;

/**
 * @author Fabrice P Cordelieres
 *
 */
public class globalParameters {
	/** Element name **/
	public String name="";
	/** True to use the element **/
	public boolean use=false;
	/** Element's x value**/
	public int x=0;
	/** Element's y value **/
	public int y=0;
	/** Element's z value **/
	public int z=0;
	
	/**
	 * Read the parameters for a single elements from the table model
	 * @param model the table model to read from
	 * @param row the row to read
	 */
	public globalParameters(DefaultTableModel model, int row){
		name=(String) model.getValueAt(row, 0);
		use=model.getValueAt(row, 1)!=null?(Boolean) model.getValueAt(row, 1):false;
		x=(Integer) model.getValueAt(row, 2);
		y=(Integer) model.getValueAt(row, 3);
		z=model.getValueAt(row, 4)!=null?(Integer) model.getValueAt(row, 4):-1;
	}
	
	/**
	 * Converts the current globalParameters as a point3D
	 * @return a point3D
	 */
	public point3D getPoint3D(){
		return new point3D(x, y, z);
	}
	
	@Override
	public String toString(){
		return
				name+"-draw="+use+"\n"+
				name+"-x="+x+"\n"+
				name+"-y="+y+"\n"+
				name+"-z="+z+"\n";
	}
}
