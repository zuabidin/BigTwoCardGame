
/**
 * A subclass of Hand and models a Straight hand.
 * 
 * @author Zain Ul Abidin
 */
public class Straight extends Hand{

	private static final long serialVersionUID = -3711761437629470849L;
	
	/**
	 * The constructor for building a Straight hand with specified player and list of cards.
	 * 
	 * @param player
	 * 			The player specified
	 * 
	 * @param cards
	 * 			The cards held by the player
	 */
	public Straight(CardGamePlayer player, CardList cards){
		
		super(player,cards); //calls the super class constructor
		
	}
	
	/**
	 * A method for retrieving the type of class
	 * 
	 * @return String specifying the type of class
	 */
    public String getType(){
		
		return "Straight";
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
			
				return true;
				
			}

		}
			return false;
	}
	
}