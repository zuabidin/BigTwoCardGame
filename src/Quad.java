
/**
 * A subclass of Hand and models a Quad hand.
 * 
 * @author Zain Ul Abidin
 */
public class Quad extends Hand{

	private static final long serialVersionUID = -3711761437629470849L;
	
	/**
	 * The constructor for building a Quad hand with specified player and list of cards.
	 * 
	 * @param player
	 * 			The player specified
	 * 
	 * @param cards
	 * 			The cards held by the player
	 */
	public Quad(CardGamePlayer player, CardList cards){
		
		super(player,cards); //calls the super class constructor
		
	}
	
	/**
	 * A method for retrieving the type of class
	 * 
	 * @return String specifying the type of class
	 */
	public String getType(){
		
		return "Quad";
	}
	
    /**
   	 * A method for checking if this is a valid hand. 
   	 * 
   	 * @return trueOrFalse
   	 */
	public boolean isValid(){
		
		this.sort();
		int count = 0;
	
		if ( this.size() == 5){
			
			for ( int i = 1; i < this.size(); i++){
				
				if ( this.getCard(0).getRank() == this.getCard(i).getRank()){
		
					count++;
				
				}
		
			}
			
			if (count == this.size() - 2){
				
				return true;
			
			}
			
		}
		
		return false;
	}
	
	/**
	 * A method for checking if this hand beats a specified hand
	 * 
	 * @param hand
	 * 			The hand to check against
	 * 
	 * @return trueOrfalse
	 * 
	 */
	public boolean beats(Hand hand){
		
		
		if(this.isValid() && hand.isValid()){
		
			if(this.getType() == hand.getType()){
			
				if(this.getTopCard().compareTo(hand.getTopCard()) == 1){
				
					return true;
			
				}else{
				
					return false;
				
				}	
				
			}else if(hand.getType() == "StraightFlush"){
			
				return false;
			
			}else if(hand.getType() == "Straight" || hand.getType() == "Flush" || hand.getType() == "FullHouse"){
		
				return true;
			
			}
		}
		
		return false;
	}
	
	/**
	 * Returns the top card of this hand
	 * 
	 * @return Returns the top card of this hand
	 */
	public Card getTopCard(){
		
		Card topCard = null;
		
		if( this.getCard(0).getRank() != this.getCard(1).getRank() ){
		
			topCard = getCard(1);
			this.sort(); //sorting the list of cards.
			
			for ( int i = 2; i < 5; i++ ){
				
				if( topCard.compareTo(getCard(i)) == -1){
						
						topCard = getCard(i);
						
				}
				
			}
			
			return topCard;
	
		}else if( this.getCard(3).getRank() != this.getCard(4).getRank() ){
			
			topCard = getCard(0);
			this.sort(); //sorting the list of cards.
			
			for ( int i = 1; i < 4; i++ ){
				
				if( topCard.compareTo(getCard(i)) == -1){
						
						topCard = getCard(i);
						
				}
				
			}
			
			return topCard;	
			
		}
		
		return topCard;
	}
	
	
}
