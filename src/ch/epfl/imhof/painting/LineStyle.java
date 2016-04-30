/**
 *  @Author:      Alexis Montavon (245433)
 *  @Author:      Fares Ahmed (246443)
 */

package ch.epfl.imhof.painting;

/**
 * Représente un Style de ligne.
 */
public final class LineStyle {
	private final float width;
	private final Color color;
	private final EndLine endLine;
	private final SegmentStyle segmentStyle;
	private final float[] sections;
	
	/**
	 * Construit un Style de ligne avec tous les arguments donnés.
	 * @param width
	 * 		un float, La largeur de la ligne.
	 * @param c
	 * 		un Color, La couleur de la ligne.
	 * @param end
	 * 		un EndLine, La terminaison de la ligne.
	 * @param seg
	 * 		un SegmentStyle, La jointure des segments.
	 * @param sections
	 * 		un float[], L'alternance des sections opaques et transparentes de la lignes (pour traitillés).
	 * @throws IllegalArgumentException
	 * 		ssi la largeur du trait est négative ou si l'un des éléments de la séquence d'alternance 
	 * 			des segments est négatif ou nul.
	 */
	public LineStyle(float width, Color c, EndLine end, SegmentStyle seg, float[] sections) throws IllegalArgumentException{
		if(width < 0){
			throw new IllegalArgumentException("Largeur du trait négative.");
		}
		for(float f: sections){
			if(f <= 0){
				throw new IllegalArgumentException("Element de la séquance d'altérnance négatif ou nul.");
			}
		}
		this.width = width;
		this.color = c;
		this.endLine = end;
		this.segmentStyle = seg;
		this.sections = sections;
	}
	
	/**
	 * Construit un Style de ligne avec la largeur et couleur donnée, les autres attributs sont initialisé par défaut.
	 * @param width
	 * 		un float, La largeur de la ligne.
	 * @param c
	 * 		un Color, La couleur de la ligne.
	 * @return LineStyle
	 * 		le nouveau Style de ligne.
	 */
	public LineStyle(float width, Color c){
		float[] sections = new float[]{};
		this.width = width;
		this.color = c;
		this.endLine = EndLine.BUTT;
		this.segmentStyle = SegmentStyle.MITER;
		this.sections = sections;
	}
	
	/**
	 * @return float
	 * 		La largeur de la ligne.
	 */
	public float getWidth(){
		return width;
	}
	
	/**
	 * @return Color
	 * 		La couleur de la ligne.
	 */
	public Color getColor(){
		return color;
	}
	
	/**
	 * @return EndLine
	 * 		La terminaison de la ligne.
	 */
	public EndLine getEndLine(){
		return endLine;
	}
	
	/**
	 * @return SegmentStyle
	 * 		La jointure des segments de la ligne.
	 */
	public SegmentStyle getSegmentStyle(){
		return segmentStyle;
	}
	
	/**
	 * @return float[]
	 * 		L'alternance opaque/transparent de la ligne.
	 */
	public float[] getSections(){
		return sections.clone();
	}
	
	/**
	 * Permet d'obtenir un Style identique à celui auquel on l'applique, sauf pour la largeur
	 * @param f
	 * 		un float, La largeur de la ligne.
	 * @return LineStyle
	 * 		Le nouveau Style obtenu.
	 */
	public LineStyle withWidth(float f){
		return new LineStyle(f, this.getColor(), this.getEndLine(), this.getSegmentStyle(), this.getSections());
	}
	
	/**
	 * Permet d'obtenir un Style identique à celui auquel on l'applique, sauf pour la couleur
	 * @param c
	 * 		un Color, La couleur de la ligne.
	 * @return LineStyle
	 * 		Le nouveau Style obtenu.
	 */
	public LineStyle withColor(Color c){
		return new LineStyle(this.getWidth(), c, this.getEndLine(), this.getSegmentStyle(), this.getSections());
	}
	
	/**
	 * Permet d'obtenir un Style identique à celui auquel on l'applique, sauf pour la terminaison
	 * @param end
	 * 		un EndLine, La terminaison de la ligne.
	 * @return LineStyle
	 * 		Le nouveau Style obtenu.
	 */
	public LineStyle withEndLine(EndLine end){
		return new LineStyle(this.getWidth(), this.getColor(), end, this.getSegmentStyle(), this.getSections());
	}
	
	/**
	 * Permet d'obtenir un Style identique à celui auquel on l'applique, sauf pour la jointure
	 * @param seg
	 * 		un SegmentStyle, La jointure de la ligne
	 * @return LineStyle
	 * 		Le nouveau Style obtenu.
	 */
	public LineStyle withSegmentStyle(SegmentStyle seg){
		return new LineStyle(this.getWidth(), this.getColor(), this.getEndLine(), seg, this.getSections());
	}
	
	/**
	 * Permet d'obtenir un Style identique à celui auquel on l'applique, sauf pour L'alternance opaque/transparent
	 * @param sections
	 * 		un float[], L'alternance opaque/transparent de la ligne.
	 * @return LineStyle
	 * 		Le nouveau Style obtenu.
	 */
	public LineStyle withSections(float[] sections){
		return new LineStyle(this.getWidth(), this.getColor(), this.getEndLine(), this.getSegmentStyle(), sections);
	}
	
	/**
	 * Représente la terminaison d'une ligne.
	 */
	public static enum EndLine {
		BUTT, ROUND, SQUARE;
	}
	
	/**
	 * Représente la jointure des segments d'une ligne.
	 */
	public static enum SegmentStyle {
		MITER, ROUND, BEVEL;
	}
}
