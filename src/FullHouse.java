
/**
 * A subclass of Hand and models a FullHouse hand.
 * 
 * @author Zain Ul Abidin
 */
public class FullHouse extends Hand{

	private static final long serialVersionUID = -3711761437629470849L;
	
	/**
	 * The constructor for building a FullHouse hand with specified player and list of cards.
	 * 
	 * @param player
	 * 			The player specified
	 * 
	 * @param cards
	 * 			The cards held by the player
	 */
	public FullHouse(CardGamePlayer player, CardList cards){
		
		super(player,cards); //calls the super class constructor
		
	}
	
	/**
	 * A method for retrieving the type of class
	 * 
	 * @return String specifying the type of class
	 */
    public String getType(){
		
		return "FullHouse";
	}
    
    /**
   	 * A method for checking if this is a valid hand. 
   	 * 
   	 * @return trueOrFalse
   	 */
    public boolean isValid(){
    	
    	this.sort();
    	
    	if(this.size() == 5){
    		
			if(this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(0).getRank() == this.getCard(2).getRank() ){
			
				if( this.getCard(4).getRank() == this.getCard(3).getRank()){
					return true;
				}
			
			}else if(this.getCard(0).getRank() == this.getCard(1).getRank()){
			
				if( this.getCard(2).getRank() == this.getCard(3).getRank()  && this.getCard(2).getRank() == this.getCard(4).getRank() ){
					return true;
				}	
			
			}else{
		
				return false;
			
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
				
			}else if(hand.getType() == "Quad" || hand.getType() == "StraightFlush"){
			
				return false;
			
			}else if(hand.getType() == "Straight" || hand.getType() == "Flush"){
		
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
		
		if(this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(0).getRank() == this.getCard(2).getRank() ){
		
			topCard = getCard(0);
			this.sort(); //sorting the list of cards.
			
			for ( int i = 1; i < 3; i++ ){
				
				if( topCard.compareTo(getCard(i)) == -1){
						
						topCard = getCard(i);
						
				}
				
			}
			
			return topCard;
	
		}else if( this.getCard(2).getRank() == this.getCard(3).getRank() && this.getCard(2).getRank() == this.getCard(4).getRank() ){
			
			topCard = getCard(2);
			this.sort(); //sorting the list of cards.
			
			for ( int i = 2; i < 5; i++ ){
				
				if( topCard.compareTo(getCard(i)) == -1){
						
						topCard = getCard(i);
						
				}
				
			}
			
			return topCard;	
			
		}
		
		return topCard;
	}
}
