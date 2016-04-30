/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */

package ch.epfl.imhof.painting;

/**
 * Représente une couleur.
 */
public final class Color {
	private final float red, green, blue;
	
	/**
	 * Construit une Couleur avec les niveaux de couleurs donnés.
	 * @param r
	 * 		un float, la valeur de la composante rouge.
	 * @param g
	 * 		un float, la valeur de la composante verte.
	 * @param b
	 * 		un float, la valeur de la composante bleue.
	 */
	private Color(float r, float g, float b){
		this.red = r;
		this.green = g;
		this.blue = b;
	}
	
	/**
	 * Construit la Couleur grise dont les trois composantes sont égale à la valeur donnée.
	 * @param x
	 * 		un float, la valeur des trois composante de la Couleur.
	 * @return Color
	 * 		la Couleur grise construite.
	 * @throws IllegalArgumentException
	 * 		ssi la composante donnée n'est pas dans l'intervale [0,1].
	 */
	public static Color gray(float x) throws IllegalArgumentException{
		if(x < 0 || x > 1){
			throw new IllegalArgumentException("Valeur donnée hors de l'intervale [0,1]");
		}
		return new Color(x, x, x);
	}
	
	/**
	 * Construit la Couleur avec les composantes données.
	 * @param r
	 * 		un float, la valeur de la composante rouge.
	 * @param g
	 * 		un float, la valeur de la composante verte.
	 * @param b
	 * 		un float, la valeur de la composante bleue.
	 * @return Color
	 * 		la couleur construite avec les valeurs données.
	 * @throws IllegalArgumentException
	 * 		ssi une (ou plusieurs) des valeurs donnée n'est pas dans l'intervale [0,1].
	 */
	public static Color rgb(float r, float g, float b) throws IllegalArgumentException{
		if(r < 0 || r > 1){
			throw new IllegalArgumentException("Valeur donnée pour rouge est hors de l'intervale [0,1]");
		}
		if(g < 0 || g > 1){
			throw new IllegalArgumentException("Valeur donnée pour vert est hors de l'intervale [0,1]");
		}
		if(b < 0 || b > 1){
			throw new IllegalArgumentException("Valeur donnée pour bleu est hors de l'intervale [0,1]");
		}
		return new Color(r, g, b);
	}
	
	/**
	 * Construit la Couleur avec les composantes rouge, vert et bleu empaquetées
	 * dans un entier.
	 * @param rgb
	 *      un int, l'entier des composantes empaquetées.
	 * @return Color
	 *      la couleur construite avec la valeur donnée.
	 * @throws IllegalArgumentException
	 *      ssi la valeur donnée est hors de l'intervale [0, 255].
	 */
	public static Color rgb(int rgb) throws IllegalArgumentException{
	    int r0 = (rgb >> 16) & 0xFF;
	    float r = (float)r0 / 255;
	    if(r < 0 || r > 255){
            throw new IllegalArgumentException("Valeur de la composante rouge est hors de l'intervale [0,255]");
        }
	    int g0 = (rgb >> 8) & 0xFF;
        float g = (float)g0 / 255;
        if(g < 0 || g > 255){
            throw new IllegalArgumentException("Valeur de la composante verte est hors de l'intervale [0,255]");
        }
        int b0 = (rgb >> 0) & 0xFF;
        float b = (float)b0 / 255;
        if(b < 0 || b > 255){
            throw new IllegalArgumentException("Valeur de la composante bleue est hors de l'intervale [0,255]");
        }
		return new Color(r, g, b);
	}
	
	/**
     * La couleur « rouge ».
     */
	public final static Color RED = new Color(1, 0, 0);
	
	/**
     * La couleur « vert ».
     */
	public final static Color GREEN = new Color(0, 1, 0);
	
	/**
     * La couleur « bleu ».
     */
	public final static Color BLUE = new Color(0, 0, 1);
	
	/**
     * La couleur « noir ».
     */
	public final static Color BLACK = new Color(0, 0, 0);
	
	/**
     * La couleur « blanc ».
     */
	public final static Color WHITE = new Color(1, 1, 1);
	
	/**
	 * @return float
	 * 		La composante rouge de la couleur.
	 */
	public float getRed(){
		return red;
	}
	
	/**
	 * @return float
	 * 		La composante verte de la couleur.
	 */
	public float getGreen(){
		return green;
	}
	
	/**
	 * @return float
	 * 		La composante bleu de la couleur.
	 */
	public float getBlue(){
		return blue;
	}
	
	/**
	 * Permet de multiplier deux Couleurs.
	 * @param c
	 * 		un Color, la couleur à multiplier avec celle que l'on souhaite.
	 * @return Color
	 * 		la nouvelle couleur avec les attributs multipliés;
	 */
	public Color multiply(Color c){
		float newRed = this.getRed() * c.getRed();
		float newGreen = this.getGreen() * c.getGreen();
		float newBlue = this.getBlue() * c.getBlue();
		return new Color(newRed, newGreen, newBlue);
	}
	
	/**
	 * Convertie un Color en Couleur de l'API Java.
	 * @return java.awt.Color
	 * 		La couleur API Java.
	 */
	public java.awt.Color toAWTColor() {
		return new java.awt.Color(this.getRed(), this.getGreen(), this.getBlue());
	}
	
}
