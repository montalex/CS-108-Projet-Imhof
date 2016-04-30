/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */

package ch.epfl.imhof.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

/**
 * Représente un model numérique de terrain (MNT) stocker à partir d'un fichier HGT.
 */
public final class HGTDigitalElevationModel implements DigitalElevationModel {
    private final File file;
    private final int latitudeMin;
    private final int latitudeMax;
    private final int longitudeMin;
    private final int longitudeMax;
    private final FileInputStream stream;
    private ShortBuffer buffer;
    
    /**
     * Construit un model numérique de terrain (MNT) à partir d'un fichier HGT.
     * @param f
     *      un File, le fichier.hgt
     * @throws IllegalArgumentException
     *      ssi le nom du fichier ne correspond pas aux exigences de nommage, 
     *      ou si sa taille en octets divisée par deux n'a pas une racine carrée entière.
     * @throws IOException
     *      En cas d'autre erreur d'entrée/sortie, p.ex. si le fichier n'existe pas.
     */
    public HGTDigitalElevationModel(File f) throws IllegalArgumentException, IOException {
        /**
         * Vérifie les exigences de nommage
         */
        if(!(f.getName().charAt(0) == 'N' || f.getName().charAt(0) == 'S')){
            throw new IllegalArgumentException("Nom du fichier érroné");
        } else if(!(Integer.parseInt(f.getName().substring(1, 3)) >= 0 && Integer.parseInt(f.getName().substring(1, 3)) <= 90)){
            throw new IllegalArgumentException("Latitude érronée");
        } else if(!(f.getName().charAt(3) == 'E' || f.getName().charAt(3) == 'W')){
            throw new IllegalArgumentException("Nom du fichier érroné");
        } else if(!(Integer.parseInt(f.getName().substring(4, 7)) >= 0 && Integer.parseInt(f.getName().substring(4, 7)) <= 180)){
            throw new IllegalArgumentException("Longitude érronée");
        } else if(!(f.getName().substring(7, 11).equals(".hgt"))){
            throw new IllegalArgumentException("Format fichier érroné");
        }
        /**
         * Vérifie que la taille du fichier en octets divisée par deux a une racine carrée entière.
         */
        int sqrt = (int)Math.sqrt(f.length() / 2);
        int toLength = (sqrt * sqrt * 2);
        if(!(f.length() == toLength)){
            throw new IllegalArgumentException("Mauvais fichier");
        }
        this.file = f;
        /**
         * Permet de calculer la zone couverte par le MNT.
         */
        this.latitudeMin = Integer.parseInt(file.getName().substring(1, 3));
        int latitudeMax = Integer.parseInt(file.getName().substring(1, 3));
        if(file.getName().charAt(0) == 'S'){
            latitudeMax = -latitudeMax;
        }
        latitudeMax = latitudeMax + 1;
        this.latitudeMax = latitudeMax;
        this.longitudeMin = Integer.parseInt(file.getName().substring(4, 7));
        int longitudeMax = Integer.parseInt(file.getName().substring(4, 7));
        if(file.getName().charAt(3) == 'W'){
            longitudeMax = -longitudeMax;
        }
        longitudeMax = longitudeMax + 1;
        this.longitudeMax = longitudeMax;
        /**
         * lecture du fichier et stockage des informations
         */
        long length = f.length();
        this.stream = new FileInputStream(f);
        try{
           this.buffer = stream.getChannel().map(MapMode.READ_ONLY, 0, length).asShortBuffer();
        } finally {}
    }
    
    @Override
    public Vector3 normalAt(PointGeo p) throws IllegalArgumentException {
        if(Math.toDegrees(p.longitude()) > longitudeMax || Math.toDegrees(p.longitude()) < longitudeMin){
            throw new IllegalArgumentException("Le point donné ne fait pas partie de la zone couverte par le MNT.");
        } else if(Math.toDegrees(p.latitude()) > latitudeMax || Math.toDegrees(p.latitude()) < latitudeMin){
            throw new IllegalArgumentException("Le point donné ne fait pas partie de la zone couverte par le MNT.");
        }
        int xi = (int)Math.floor((p.longitude() - Math.toRadians(longitudeMin)) / ANGULAR_RESOLUTION);
        int yi = (int)Math.floor((p.latitude() - Math.toRadians(latitudeMin)) / ANGULAR_RESOLUTION);
        double xVector = (0.5 * S) * (buffer.get(indexAt(xi, yi)) - buffer.get(indexAt(xi+1, yi)) 
                                        + buffer.get(indexAt(xi, yi+1)) - buffer.get(indexAt(xi+1, yi+1)));
        double yVector = (0.5 * S) * (buffer.get(indexAt(xi, yi)) + buffer.get(indexAt(xi+1, yi)) 
                                        - buffer.get(indexAt(xi, yi+1)) - buffer.get(indexAt(xi+1, yi+1)));
        double zVector = S * S; 
        return new Vector3(xVector, yVector, zVector);
    }
    
    /**
     * retourne l'index du point (x,y) dans le fichier HGT, index utilisable dans le buffer.
     */
    private int indexAt(int x, int y){
        return ((3600 - y) * 3601) + x;
    }
    
    @Override
    public void close() throws Exception {
        stream.close();
        buffer = null;
    }
}
