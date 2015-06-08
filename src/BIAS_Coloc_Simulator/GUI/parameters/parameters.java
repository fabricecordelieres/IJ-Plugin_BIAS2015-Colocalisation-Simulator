/**
  * parameters.java v1, 30 mai 2015
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

import javax.swing.SpinnerListModel;
import javax.swing.table.DefaultTableModel;

/**
 * @author Fabrice P Cordelieres
 *
 */
public class parameters {
	/** Colocalisation method **/
	public String method="";
	
	/** Global parameters **/
	public globalParameters[] globalParameters=new globalParameters[4];
	
	/**Channel1 parameters **/
	public elementParameters[] channel1Parameters=new elementParameters[5];
	
	/**Channel2 parameters **/
	public elementParameters[] channel2Parameters=new elementParameters[5];
	
	/**
	 * Reads the parameters from the GUI
	 * @param method model from the spinner
	 * @param globalParameters model from global parameters table
	 * @param channel1 model from channel1 table
	 * @param channel2 model from channel2 table
	 */
	public parameters(SpinnerListModel method, DefaultTableModel globalParameters, DefaultTableModel channel1, DefaultTableModel channel2){
		//Reads the method to be used for simulation
		this.method=(String) method.getValue();
		
		//Reads the global parameters
		for(int i=0; i<4; i++) this.globalParameters[i]=new globalParameters(globalParameters, i);
		
		//Reads parameters for channel 1
		for(int i=0; i<5; i++) this.channel1Parameters[i]=new elementParameters(channel1, i);
		
		//Reads parameters for channel 2
		for(int i=0; i<5; i++) this.channel2Parameters[i]=new elementParameters(channel2, i);
	}

	@Override
	public String toString(){
		String out=method;
		for(int i=0; i<4; i++) out+="\n"+globalParameters[i];
		for(int i=0; i<5; i++) out+="\n"+channel1Parameters[i];
		for(int i=0; i<5; i++) out+="\n"+channel2Parameters[i];
		return out;
	}
}
