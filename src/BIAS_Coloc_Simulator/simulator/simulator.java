/**
  * simulator.java v1, 31 mai 2015
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
package BIAS_Coloc_Simulator.simulator;

import java.util.ArrayList;

import ij.ImagePlus;
import ij.gui.NewImage;
import ij.gui.OvalRoi;
import ij.io.FileSaver;
import BIAS_Coloc_Simulator.GUI.BIAS_Coloc_Simulator_GUI;
import BIAS_Coloc_Simulator.GUI.parameters.elementParameters;
import BIAS_Coloc_Simulator.GUI.parameters.parameters;
import BIAS_Coloc_Simulator.simulator.elements.geometricalElement;
import BIAS_Coloc_Simulator.simulator.elements.point3D;
import BIAS_Coloc_Simulator.utilities.utilities;


/**
 * @author Fabrice P Cordelieres
 *
 */
public class simulator {
	/** Stores the simulation parameters **/
	parameters parameters=null;
	
	/** Stores channel1 **/
	ImagePlus channel1Image=null;
	
	/** Stores channel2 **/
	ImagePlus channel2Image=null;
	
	/** Stores uneven illumination **/
	ImagePlus illuminationImage=null;
	
	/** Stores the elements for channel1 **/
	ArrayList<geometricalElement> channel1Elements=new ArrayList<geometricalElement>();
	
	/** Stores the elements for channel1 **/
	ArrayList<geometricalElement> channel2Elements=new ArrayList<geometricalElement>();
	
	/**
	 * Generates a simulation based on input parameters
	 * @param parameters parameters to be used for simulation
	 */
	public simulator(parameters parameters){
		this.parameters=parameters;
	}
	
	/**
	 * Generates the simulated data
	 */
	public void generate(){
		double intensity=1.0;
		point3D dimensions=parameters.globalParameters[0].getPoint3D();
		
		for(int i=0; i<parameters.channel1Parameters.length; i++){
			elementParameters parameters1=parameters.channel1Parameters[i];
			elementParameters parameters2=parameters.channel2Parameters[i];
			
			for(int j=0; j<Math.max(parameters1.nElements, parameters2.nElements); j++){
				geometricalElement element1=null;
				geometricalElement element2=null;
				
				if(j<parameters1.nElements) element1=parameters1.getGeometricalElement(j, dimensions, intensity);
				if(j<parameters2.nElements) element2=parameters2.getGeometricalElement(j, dimensions, intensity);
			
				//--------------------------------------------------------------------------------------------
				if(parameters.method==BIAS_Coloc_Simulator_GUI.INCLUSION){
					if(element1!=null && element2!=null){
						
						element2.centrePoint=element1.centrePoint.duplicate();
						
						if(element1.type>=8 && element1.type<=13){
							int shrink=(int) (2.0*(element1.RTorus-element1.rTorus)-1.0);
							element2.type=1;
							
							element2.boundingBox=new point3D(shrink, shrink, shrink);
						}else{
							element1.innerBoundingBox=element2.boundingBox.duplicate();
						}
						
						if(element1.type>=11 && element1.type<=13){
							element2=element1.duplicate();
							element2.angle+=Math.PI/2;
						}
					}
				}
				
				//--------------------------------------------------------------------------------------------
				if(parameters.method==BIAS_Coloc_Simulator_GUI.COLOC){
					if(parameters1.nElements>=parameters2.nElements){
						element2=element1.duplicate();
					}else{
						element1=element2.duplicate();
					}
				}
				
				//--------------------------------------------------------------------------------------------
				//if(parameters.method==BIAS_Coloc_Simulator_GUI.RANDOM){
					//Nothing to do
				//};
				
				//--------------------------------------------------------------------------------------------
				//if(parameters.method==BIAS_Coloc_Simulator_GUI.EXCLUSION){
					//TODO To be implemented
				//};
				
				
				if(element1!=null){
					element1.fill(intensity);
					channel1Elements.add(element1);
				}
				if(element2!=null){
					element2.fill(intensity++);
					channel2Elements.add(element2);
				}
			}
		}
	}
	
	/**
	 * Generate ground truth images
	 */
	public void generateGroundTruth(){
	}
	
	/**
	 * Show simulated images
	 */
	public void showImages(){
		String properties="";
		String title1="Channel1_Ground-Truth";
		String title2="Channel2_Ground-Truth";
		
		//Show ground truth
		channel1Image=NewImage.createImage(title1, parameters.globalParameters[0].x, parameters.globalParameters[0].y, parameters.globalParameters[0].z, 32, NewImage.FILL_BLACK);
		channel2Image=NewImage.createImage(title2, parameters.globalParameters[0].x, parameters.globalParameters[0].y, parameters.globalParameters[0].z, 32, NewImage.FILL_BLACK);
		
		for(int i=0; i<channel1Elements.size(); i++) channel1Elements.get(i).drawOnImage(channel1Image);
		for(int i=0; i<channel2Elements.size(); i++) channel2Elements.get(i).drawOnImage(channel2Image);
		
		channel1Image.resetDisplayRange();
		channel1Image.show();
		
		channel2Image.resetDisplayRange();
		channel2Image.show();
		
		//Perform chromatic shift
		if(parameters.globalParameters[1].use){
			point3D shift=parameters.globalParameters[1].getPoint3D();
			for(int i=0; i<channel2Elements.size(); i++) channel2Elements.get(i).shift(shift);
			title1+="_Shifted";
			title2+="_Shifted";
			channel2Image=NewImage.createImage(title2, parameters.globalParameters[0].x, parameters.globalParameters[0].y, parameters.globalParameters[0].z, 32, NewImage.FILL_BLACK);
			for(int i=0; i<channel2Elements.size(); i++) channel2Elements.get(i).drawOnImage(channel2Image);
			
			properties+="Shift_X="+shift.getX()+"\nShift_Y="+shift.getY()+"\nShift_Z="+shift.getZ();
			channel2Image.setProperty("Info", properties);
			channel2Image.resetDisplayRange();
			channel2Image.show();
		}
		
		//Adds uneven illumination
		if(parameters.globalParameters[2].use){
			properties+=(properties.equals("")?"":"\n")+"Uneven_Illumination_X="+parameters.globalParameters[2].x+"\n"
					+"Uneven_Illumination_Y="+parameters.globalParameters[2].y+"\n"
					+"Uneven_Illumination_Z="+parameters.globalParameters[2].z;
			
			title1+="_Uneven-Illumination";
			title2+="_Uneven-Illumination";
			
			//Generate uneven illumination
			illuminationImage=NewImage.createImage("Illumination", parameters.globalParameters[0].x, parameters.globalParameters[0].y, 1, 32, NewImage.FILL_BLACK);
			
			int roiWidth=illuminationImage.getWidth()/10;
			int roiHeight=illuminationImage.getHeight()/10;
			double maxDimension=Math.max(illuminationImage.getWidth(), illuminationImage.getHeight());
			
			illuminationImage.getProcessor().setColor(maxDimension);
			illuminationImage.getProcessor().fill(new OvalRoi(parameters.globalParameters[2].x-roiWidth/2, parameters.globalParameters[2].y-roiHeight/2, roiWidth, roiHeight));
		
			illuminationImage.getProcessor().blurGaussian(maxDimension/2);
			illuminationImage.setProperty("Info", properties);
			
			illuminationImage.resetDisplayRange();
			illuminationImage.show();
			
			//Apply uneven illumination
			channel1Image=utilities.generateUnevenIllumination(channel1Image, illuminationImage);
			channel1Image.setProperty("Info", properties);
			channel1Image.setTitle(title1);
			channel1Image.resetDisplayRange();
			
			channel2Image=utilities.generateUnevenIllumination(channel2Image, illuminationImage);
			channel2Image.setProperty("Info", properties);
			channel2Image.setTitle(title2);
			channel2Image.resetDisplayRange();
			
			channel1Image.show();
			channel2Image.show();
		}
		
		//Adds bleedthrough
		if(parameters.globalParameters[3].use){
			properties+=(properties.equals("")?"":"\n")+"Bleed_Through_ch1>ch2="+parameters.globalParameters[3].x+"\nBleed_Through_ch2>ch1="+parameters.globalParameters[3].y;
			
			title1+="_BleedThrough";
			title2+="_BleedThrough";
			
			//Required not to get images overwritten
			channel1Image=channel1Image.duplicate();
			channel2Image=channel2Image.duplicate();
			
			utilities.generateBleedThrough(channel1Image, channel2Image, parameters.globalParameters[3].x, parameters.globalParameters[3].y);
			
			channel1Image.setTitle(title1);
			channel1Image.setProperty("Info", properties);
			channel1Image.resetDisplayRange();
			
			channel2Image.setTitle(title2);
			channel2Image.setProperty("Info", properties);
			channel2Image.resetDisplayRange();
			
			channel1Image.show();
			channel2Image.show();
		}
		
	}
	
	void saveFiles(){
		FileSaver fs=null;
		
		if(channel1Image!=null){
			fs=new FileSaver(channel1Image);
			fs.saveAsZip("/Users/fab/Desktop/"+channel1Image.getTitle()+".zip");
		}
		
		if(channel2Image!=null){
			fs=new FileSaver(channel2Image);
			fs.saveAsZip("/Users/fab/Desktop/"+channel2Image.getTitle()+".zip");
		}
		
		if(illuminationImage!=null){
			fs=new FileSaver(illuminationImage);
			fs.saveAsZip("/Users/fab/Desktop/"+illuminationImage.getTitle()+".zip");
		}
	}
}
