
/**
 * The BigTwoDeck class is a subclass of the Deck class,
 * and is used to model a deck of cards used in a Big Two card game
 * 
 * @author Zain Ul Abidin
 */
public class BigTwoDeck extends Deck {

	private static final long serialVersionUID = -3886066435694112173L;
	
	//overrides the initialize method
	public void initialize(){
		
		removeAllCards();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				BigTwoCard card = new BigTwoCard(i, j); //forms a BigTwoCard
				addCard(card);
			}
		}
	}
	
}
