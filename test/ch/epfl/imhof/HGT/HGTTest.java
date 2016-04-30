/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */

package ch.epfl.imhof.HGT;

import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import java.io.File;
import javax.imageio.ImageIO;
import org.junit.Test;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.painting.Color;


public class HGTTest {

   @Test
    public void testVectorGray() throws Exception { 
        HGTDigitalElevationModel model = new HGTDigitalElevationModel(new File(getClass().getResource("/N46E007.hgt").getFile()));
         BufferedImage image = new BufferedImage(800, 800, TYPE_INT_RGB);
         for(int i = 0; i < 800; i++){
             for(int j = 0; j < 800; j++){
                 PointGeo p1 = new PointGeo(Math.toRadians(7.2 + (j*0.6/799)), Math.toRadians(46.8 - (i* 0.6/799)));
                 float g = (float)((model.normalAt(p1).normalized().y() + 1)/2);
                 image.setRGB(j, i, Color.gray(g).toAWTColor().getRGB());
             }
         }
         model.close();
         ImageIO.write(image, "png", new File("testVector.png"));
    }
}