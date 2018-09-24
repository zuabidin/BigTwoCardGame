
/**
 * The Hand class is a subclass of the CardList class,
 * and is used to model a hand of cards. It has a private
 * instance variable for storing the player who plays this
 * hand. It also has methods for getting the player of this
 * hand, checking if it is a valid hand, getting the type of
 * this hand, getting the top card of this hand, and checking 
 * if it beats a specified hand.
 * 
 * @author Zain Ul Abidin
 */
public abstract class Hand extends CardList {

	private static final long serialVersionUID = -3711761437629470849L;
	private CardGamePlayer player;
	
	
	/**
	 * A constructor for building a hand with the specified player 
	 * and list of cards
	 * 
	 * @param player
	 * 			The player specified
	 * 
	 * @param cards
	 * 			The cards held by the player.
	 */
	public Hand(CardGamePlayer player, CardList cards){
	
		this.player = player;
		
		for(int i = 0; i < player.getNumOfCards(); i++){	
			
			Card card = cards.getCard(i);
			this.addCard(card);
	
		}
		
	}
	
	
	/**
	 * Default constructor for Hand class
	 */
	public Hand(){
		
	}
	

	/**
	 * A method for retrieving the player of this hand
	 * 
	 * @return CardGamePlayer 
	 */
	public CardGamePlayer getPlayer(){
		
		return this.player; 
		
	}
	
	/**
	 * A method for retrieving the top card of this hand. 
	 * 
	 * @return Card the top card for this hand.
	 */
	public Card getTopCard(){
		
		Card topCard = getCard(0);
		
		
		for ( int i = 1; i < size(); i++ ){
			
			if( topCard.compareTo(getCard(i)) == -1){
					
					topCard = getCard(i);
					
			}
			
		}
		
		return topCard;
		
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
		
			if(this.getType() == hand.getType()){ //checking if type if equal
			
				if(this.getTopCard().compareTo(hand.getTopCard()) == 1){ //comparing the ranks
				
					return true;
					
				}else{
					
					return false;

				}
			}
		}
		
		return false;
	}
	
	/**
	 * An abstract method for checking if this is a valid hand
	 * 
	 * @return trueOrfalse
	 */
	public abstract boolean isValid();
	
	/**
	 * A method for returning a string specifying the type of this hand
	 * 
	 * @return a string stating the type of hand,
	 */
	public abstract String getType();
		
}
