/**
  * test_ge.java v1, 20 mai 2015
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


import java.util.ArrayList;

import BIAS_Coloc_Simulator.simulator.elements.geometricalElement;
import BIAS_Coloc_Simulator.simulator.elements.point3D;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.io.FileSaver;

/**
 * @author Fabrice P Cordelieres
 *
 */
public class test_geometricalElements {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ImagePlus ip=NewImage.createImage("test", 512, 512, 256, 16, NewImage.FILL_BLACK);
		
		
		ArrayList<geometricalElement> ge=new ArrayList<geometricalElement>();
		
		for(int i=0; i<11; i++){
			point3D centrePoint=new point3D((int) (Math.random()*ip.getWidth()),
											(int) (Math.random()*ip.getHeight()),
											(int) (Math.random()*ip.getNSlices()));
			
			//System.out.println("--------------"+i+"--------------");
			//System.out.println("Centre:\n"+centrePoint.toString());
			
			point3D boundingBox=new point3D(64, 64,64);
			
			/*point3D boundingBox=new point3D((int) (Math.random()*ip.getWidth()/2.0),
											(int) (Math.random()*ip.getHeight()/2.0),
											(int) (Math.random()*ip.getNSlices()/2.0));*/
			//System.out.println("BB:\n"+boundingBox.toString());
			
			ge.add(new geometricalElement(centrePoint, boundingBox));
			
			point3D innerBoundingBox=new point3D(32, 32, 32);
			ge.get(i).innerBoundingBox=innerBoundingBox;
			
			if(i%11>7 && i%11<11){
				int min=boundingBox.getMinCoordinate();
				int max=boundingBox.getMaxCoordinate();
				ge.get(i).setrTorus((int) (Math.random()*min/5.0));
				ge.get(i).setRTorus((int) (Math.random()*max/2.0));
				ge.get(i).innerBoundingBox=null;
			}
			
			
			ge.get(i).fill(i%11, (i+1)*10);
			//ge.get(i).fill(i%3+8, (i+1)*10);
			ge.get(i).drawOnImage(ip);
			
			//ge.get(i).getImage().show();
		}
		
		FileSaver fs=new FileSaver(ip);
		
		fs.saveAsZip("/Users/fab/Desktop/test.zip");
		System.out.println("Done");
	}

}
