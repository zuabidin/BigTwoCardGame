
/**
 * The BigTwoCard class is a subclass of the Card class,
 * and is used to model a card used in a Big Two card game.
 * 
 * @author Zain Ul Abidin
 *
 */
public class BigTwoCard extends Card{

	private static final long serialVersionUID = -713898713776577970L;

	
	/**
	 * A constructor for building a card with the specified suit and rank.
	 * 
	 * @param suit
	 * 			The suit of card from Diamonds, Hearts, Spades and Clubs.
	 * 
	 * @param rank
	 * 			The rank of card from  2, A, K, Q, J, 10, 9, 8, 7, 6, 5, 4, 3. 
	 * 			
	 */
	public BigTwoCard(int suit, int rank){
		
		super(suit, rank); //calling the super class constructor.
		
	}
	

	/**
	 * A method for comparing this card with the specified card for order
	 *  
	 * @param card
	 * 			A random card
	 * 
	 * @return Returns a negative integer, zero, or a positive integer as this card is less than, equal to, or greater than the specified card
	 * 			
	 * 
	 */
	public int compareTo(Card card){
		
	     if ((this.rank==1)&&(card.getRank()!=1)){
	   
	    	 return 1;
	     
	     }else if((this.rank!=1) && (card.getRank()==1)){
	      
	    	 return -1;
	    	 
	     }else if((this.rank!=0) && (card.getRank()==0)){
		      
		    	 return -1;
		    	 
		 }else if((this.rank==0) && (card.getRank()!=0)){
			      
		    	 return 1;
	     
	     } else if (this.rank > card.getRank()) {
	  
	    	 return 1;
	 
	     } else if (this.rank < card.getRank()) {
	  
	    	 return -1;
	  
	     } else if (this.suit > card.getSuit()) {
	   
	    	 return 1;
	  
	     } else if (this.suit < card.getSuit()) {
	   
	    	 return -1;
	  
	     } else {
	   
	    	 return 0;
	  
	     }
	 }
}
