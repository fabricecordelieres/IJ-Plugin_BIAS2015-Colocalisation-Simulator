/**
  * utilities.java v1, 31 mai 2015
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
package BIAS_Coloc_Simulator.utilities;

import ij.ImagePlus;
import ij.gui.NewImage;
import ij.process.ImageProcessor;

/**
 * @author Fabrice P Cordelieres
 *
 */
public class utilities {
	
	/**
	 * Simulates corruption of an image by uneven illumination
	 * @param image input ImagePlus
	 * @param illumination illumination as an ImagePlus
	 * @return original image corrupted by uneven illumination, as an ImagePlus
	 */
	public static ImagePlus generateUnevenIllumination(ImagePlus image, ImagePlus illumination){
		ImagePlus out=NewImage.createImage("Unevenly_illuminated", image.getWidth(), image.getHeight(), image.getNSlices(), 32, NewImage.FILL_BLACK);
		ImageProcessor illuminationProcessor=illumination.getProcessor();
		
		for(int i=1; i<=image.getNSlices(); i++){
			image.setSlice(i);
			out.setSlice(i);
			ImageProcessor slice=image.getProcessor();
			ImageProcessor outProcessor=out.getProcessor();
			
			for(int y=0; y<out.getHeight(); y++){
				for(int x=0; x<out.getWidth(); x++){
					outProcessor.putPixelValue(x, y, slice.getPixelValue(x, y)*illuminationProcessor.getPixelValue(x, y));
				}
			}
		}
		
		return out;
	}
	
	/**
	 * Mimics bleedthrough between ImagePlus
	 * @param ip1 the first ImagePlus
	 * @param ip2 the second ImagePlus
	 * @param pct1 bleedthrough from channel1 to channel2
	 * @param pct2 bleedthrough from channel2 to channel1
	 */
	public static void generateBleedThrough(ImagePlus image1, ImagePlus image2, double pct1, double pct2){
		for(int i=1; i<=image1.getNSlices(); i++){
			image1.setSlice(i);
			image2.setSlice(i);
			
			for(int y=0; y<image1.getHeight(); y++){
				for(int x=0; x<image1.getWidth(); x++){
					float val1=image1.getProcessor().getPixelValue(x, y);
					float val2=image2.getProcessor().getPixelValue(x, y);
					image1.getProcessor().putPixelValue(x, y, val1+pct2*val2/100.0);
					image2.getProcessor().putPixelValue(x, y, val2+pct1*val1/100.0);
				}
			}
		}
	}
}
