/**
 *	@Author:      Alexis Montavon (245433)
 *  @Author:	  Fares Ahmed (246443)
 */
package ch.epfl.imhof.osm;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ch.epfl.imhof.Attributes;

/**
 * Représente une relation OSM.
 */
public final class OSMRelation extends OSMEntity {
	private final List<Member> members;
	
	/**
	 * Construit une relation OSM avec l'identifiant, la liste de membres et les attributs donnés.
	 * @param id
	 * 		Un long, l'identifiant unique de la relation.
	 * @param members
	 * 		Un Membre, membre de la relation.
	 * @param attributes
	 * 		Un Attributes, liste des attributs de la relation.
	 */
	public OSMRelation(long id, List<Member> members, Attributes attributes){
		super(id, attributes);
		this.members = Collections.unmodifiableList(new ArrayList<Member>(members));
	}
	
	/**
	 * @return List<Member>
	 * 		la liste des membres de la relation.
	 */
	public List<Member> members(){
		return members;
	}
	
	public static final class Member{
		private final Type type;
		private final String role;
		private final OSMEntity member;
		
		/**
		 * Construit un Membre avec le type, le role et la valeur donnés.
		 * @param type
		 * 		Un Type, le type de membre.
		 * @param role
		 * 		Une String, le rôle du membre dans la relation.
		 * @param member
		 * 		Une OSMEntity, la valeur de l'entité qu'est le membre.
		 */
		public Member(Type type, String role, OSMEntity member){
			this.type = type;
			this.role = role;
			this.member = member;
		}
		
		/**
		 * @return Type
		 * 		le type du membre.
		 */
		public Type type(){
			return type;
		}
		
		/**
		 * @return String
		 * 		le rôle du membre.
		 */
		public String role(){
			return role;
		}
		
		/**
		 * @return OSMEntity
		 * 		le membre lui-même.
		 */
		public OSMEntity member(){
			return member;
		}
		
		/**
		 * Enumère les trois types de membres qu'une relation peut comporter.
		 */
		public enum Type{NODE, WAY, RELATION};
	}
	
	public static final class Builder extends OSMEntity.Builder{
		private List<Member> tempMembers = new ArrayList<Member>();
		/**
		 * Permet de construire une relation en plusieurs étapes.
		 * @param id
		 * 		Un long, l'identifiant unique de la relation.
		 */
		public Builder(long id){
			super(id);
		}
		
		/**
		 * Permet d'ajouter un nouveau membre à la liste de membres.
		 * @param type
		 * 		Un Type, le type du nouveau membre.
		 * @param role
		 * 		Une String, le rôle du nouveau membre.
		 * @param newMember
		 * 		Uns OSMEntity, la valeur du nouveau membre.
		 */
		public void addMember(Member.Type type, String role, OSMEntity newMember){
			tempMembers.add(new Member(type, role, newMember));
		}
		
		/**
		 * Construit une nouvelle relation OSM avec l'identifiant passé au constructeur et les éventuels membres
		 * et attributs ajoutés.
		 * @return OSMRelation
		 * 		une nouvelle relation OSM.
		 * @throws IllegalStateException
		 * 		si la relation est incomplète.
		 */
		public OSMRelation build() throws IllegalStateException{
			if(this.isIncomplete()){
				throw new IllegalStateException("Relation incomplète");
			}
			return new OSMRelation(id, tempMembers, attributes.build());
		}
	}
}
