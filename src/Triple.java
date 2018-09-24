
/**
 * A subclass of Hand and models a Triple hand.
 * 
 * @author Zain Ul Abidin
 */
public class Triple extends Hand{

	private static final long serialVersionUID = -3711761437629470849L;
	
	/**
	 * The constructor for building a Triple hand with specified player and list of cards.
	 * 
	 * @param player
	 * 			The player specified
	 * 
	 * @param cards
	 * 			The cards held by the player
	 */
	public Triple(CardGamePlayer player, CardList cards){
		
		super(player,cards);//calls the super class constructor
		
	}
	
	/**
	 * A method for retrieving the type of class
	 * 
	 * @return String specifying the type of class
	 */
    public String getType(){
		
		return "Triple";
	}
	
    /**
   	 * A method for checking if this is a valid hand. 
   	 * 
   	 * @return trueOrFalse
   	 */
	public boolean isValid(){
		
		if (size() == 3){
			
			if(getCard(0).getRank() == getCard(1).getRank() && getCard(0).getRank() == getCard(2).getRank()){
				
				return true;
				
			}
			
		}
			return false;
	  	
    }
}