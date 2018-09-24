
/**
 * A subclass of Hand and models a StraightFlush hand.
 * 
 * @author Zain Ul Abidin
 */
public class StraightFlush extends Hand{

	private static final long serialVersionUID = -3711761437629470849L;
	
	/**
	 * The constructor for building a StraightFlush hand with specified player and list of cards.
	 * 
	 * @param player
	 * 			The player specified
	 * 
	 * @param cards
	 * 			The cards held by the player
	 */
	public StraightFlush(CardGamePlayer player, CardList cards){
		
		super(player,cards); //calls the super class constructor
		
	}
	
	/**
	 * A method for retrieving the type of class
	 * 
	 * @return String specifying the type of class
	 */
    public String getType(){
		
		return "StraightFlush";
	}
	
    /**
   	 * A method for checking if this is a valid hand. 
   	 * 
   	 * @return trueOrFalse
   	 */
    public boolean isValid(){
		
		int count = 0;
		this.sort();
		
		if (this.size() == 5){
			
			for (int i = 1; i < this.size(); i++){
		
				if(this.getCard(i).getRank() - this.getCard(i - 1).getRank() == 1){
				
					count++;
				
				}
				
			}
			
			if(count == this.size() - 1){
				
				count = 0;
				
				for (int i = 1; i < this.size(); i++){
					
					if( this.getCard(0).getSuit() == this.getCard(i).getSuit() ){
					
						count++;
					
					}
				}
			}
			
			if(count == this.size() - 1){
				
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
					
			}else if(hand.getType() == "Straight" || hand.getType() == "Flush" || hand.getType() == "FullHouse" || hand.getType() == "Quad"){
		
				return true;
			
			}
		}
		
		return false;
	}
}
