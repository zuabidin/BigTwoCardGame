import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;


/**
 * The BigTwoClient class implements the CardGame interface and NetworkGame interface. 
 * It is used to model a Big Two card game that supports 4 players playing over the internet. 
 * 
 * @author Zain Ul Abidin
 *
 */
public class BigTwoClient implements CardGame, NetworkGame {

	private int numOfPlayers;									
	private Deck deck;								// a deck of cards
	private ArrayList<CardGamePlayer> playerList;	// list of players
	private ArrayList<Hand> handsOnTable;			// list of hands on table
	private int playerID;							// an integer specifying the playerID (i.e., index) of the local player. 
	private String playerName;						// a string specifying the name of the local player. 
	private String serverIP;						// a string specifying the IP address of the game server.
	private int serverPort;							// an integer specifying the TCP port of the game server. 
	private Socket sock;							// a socket connection to the game server. 
	private ObjectOutputStream oos;					// an ObjectOutputStream for sending messages to the server. 
	private int currentIdx;							// an integer specifying the index of the current player
	private BigTwoTable table;						// a Big Two table which builds the GUI for the game and handles all user actions.
	private CardList tempList;								// a CardList to hold the cards chosen by player.
	private CardGamePlayer lastPlayer = new CardGamePlayer(); // a Player object to hold reference to the player who played last hand.
	
	
	/**
	 * a boolean variable to check if the game is running or not?
	 */
	public boolean game_run_check;					// a boolean variable to check if the game is running or not?
	
	
	/**
	 * a boolean variable to check if the player quit game while game has not yet started 
	 */
	public boolean quit;									//a boolean variable to check if the player quit game while game has not yet started
	
	/**
	 * A constructor for creating a Big Two client.
	 */
	public BigTwoClient(){
		
		
		//creating four CardGamePlayer
		CardGamePlayer p1 = new CardGamePlayer();
		CardGamePlayer p2 = new CardGamePlayer();
		CardGamePlayer p3 = new CardGamePlayer();
		CardGamePlayer p4 = new CardGamePlayer();
		
		//initializes the CardGamePlayer arraylist
		playerList = new ArrayList<CardGamePlayer>(); 
				
		//adding CardGamePlayer to arraylist
		playerList.add(p1);
		playerList.add(p2);
		playerList.add(p3);
		playerList.add(p4);
		
		//initializes Hand object.
		handsOnTable = new ArrayList<Hand>();
		
		//initializes BigTwoTable.
		table = new BigTwoTable(this);	
		
		
		//making a connection
		this.makeConnection();
				
	}
	
	
	/**
	 * Prints the cards in this list to the table. Equivalent to calling
	 * print(true, false);
	 */
	public void print_cards () {
		print_cards(true, false);
	}
	
	
	/**
	 * Prints the cards in this list to the table.
	 * 
	 * @param printFront
	 *            a boolean value specifying whether to print the face (true) or
	 *            the black (false) of the cards
	 * @param printIndex
	 *            a boolean value specifying whether to print the index in front
	 *            of each card
	 */
	public void print_cards (boolean printFront, boolean printIndex) {
		if (tempList.size() > 0) {
			for (int i = 0; i < tempList.size(); i++) {
				String string = "";
				if (printIndex) {
					string = i + " ";
				}
				if (printFront) {
					string = string + "[" + tempList.getCard(i) + "]";
				} else {
					string = string + "[  ]";
				}
				if (i % 13 != 0) {
					string = " " + string;
				}
				table.printMsg(string);
				if (i % 13 == 12 || i == tempList.size() - 1) {
					table.printMsg("");
					table.printMsg("\n");

				}
			}
		} else {
			table.printMsg("[Empty]\n");
			table.printMsg("\n");
		}
	}
	

	
	/**
	 * Returns the number of players in this card game.
	 * 
	 * @return the number of players in this card game
	 */
	public int getNumOfPlayers(){
		return numOfPlayers;
	}

	/**
	 * Returns the deck of cards being used in this card game.
	 * 
	 * @return the deck of cards being used in this card game
	 */
	public Deck getDeck(){
		return deck;
	}

	/**
	 * Returns the list of players in this card game.
	 * 
	 * @return the list of players in this card game
	 */
	public ArrayList<CardGamePlayer> getPlayerList(){
		return playerList;
	}

	/**
	 * Returns the list of hands played on the table.
	 * 
	 * @return the list of hands played on the table
	 */
	public ArrayList<Hand> getHandsOnTable(){
		return handsOnTable;
	}

	/**
	 * Returns the index of the current player.
	 * 
	 * @return the index of the current player
	 */
	public int getCurrentIdx(){
		return currentIdx;
	}

	/**
	 * Starts the card game.
	 * 
	 * @param deck
	 *            the deck of cards to be used in this game
	 */
	public void start(Deck deck){
		
		CardGamePlayer tempPlayer = new CardGamePlayer();
		
		//removing the hands on table
		for (int i = this.getHandsOnTable().size()-1; i >= 0; i--){
		    
			this.getHandsOnTable().remove(i);
		    
		 }
		
		//first move cannot be passed
		table.setPassButton_False();
		
		
		//removing all the cards held by players
		for ( int i = 0; i < playerList.size(); i++){
			   
			tempPlayer = this.playerList.get(i);
			
			if(tempPlayer.getCardsInHand().size() != 0){
				tempPlayer.removeAllCards(); 
			}
			
			//distributing the shuffled cards to player
			for(int j = 0; j < 13; j++ ){
				
				tempPlayer.addCard(deck.getCard(0)); //giving the current player cards
				deck.removeCard(deck.getCard(0)); 	//removing already given cards from deck.	
		   
			}
			
			tempPlayer.getCardsInHand().sort(); //sorting the cards in hand
		  
		}
		
		//choosing player with first turn (i.e. player with first three of diamonds)
		for (int i = 0; i < this.playerList.size() ; i++){ 
						  
			tempPlayer = this.playerList.get(i);
						  
				if (tempPlayer.getCardsInHand().contains(new BigTwoCard(0,2))){
							     
					currentIdx = i;
					table.setActivePlayer(currentIdx);
					    	
				 }
		}	
				
		table.repaint();
	}	
	
	/**
	 * Makes a move by the player.
	 * 
	 * @param playerID
	 *            the playerID of the player who makes the move
	 * @param cardIdx
	 *            the list of the indices of the cards selected by the player
	 */
	public void makeMove(int playerID,	int[] cardIdx){
		
		this.sendMessage(new CardGameMessage(CardGameMessage.MOVE, -1, (Object)cardIdx));
		
	}
	

	/**
	 * Checks the move made by the current player.
	 * 
	 * @param cardIdx
	 *            the list of the indices of the cards selected by the current
	 *            player
	 */
	public void checkMove(int playerID, int[] cardIdx){
	
		CardGamePlayer tempPlayer = new CardGamePlayer(); 

		//a CardList to hold the selected cards
		tempList = new CardList();
		tempList = null;
	
			
			tempPlayer = this.getPlayerList().get(this.getCurrentIdx());
			
		
			while(true){
			
				table.setPassButton_True();
	
				int[] indices = cardIdx; //getting the input in an array
				tempList = this.getPlayerList().get(this.getCurrentIdx()).play(indices); //the TempList holds the cards played by the player
			
				
				//if Player passes his turn
				if(tempList == null){ 
					
						//if there are 3 passes and the active player is the one who played the last hand
						if(this.getHandsOnTable().get(this.getHandsOnTable().size()-1).getPlayer() == lastPlayer && table.pass_check == 2){	
							
							table.setPassButton_False();
							
							//Displaying the last hand played on the table
							if(!getHandsOnTable().isEmpty()){
							
								for (int i = this.getHandsOnTable().size()-1; i >= 0; i--){
							    
									this.getHandsOnTable().remove(i);
							    
								}
		
								table.line_check++;
								table.pass_check= 0;
							}
						}
												
						table.printMsg("{Pass}\n");
							
						if(this.getCurrentIdx() != 3){
							this.currentIdx++;
						}else{
							this.currentIdx = 0;
						}
							
						table.setActivePlayer(this.getCurrentIdx()); //setting the active player to next
						table.repaint();
						table.line_check++;
						//controlling the value of the flag vaiable pass check
						if (table.pass_check == 2){
							//table.passButton.setEnabled(false);
							table.pass_check = 0;
						}else{
							table.pass_check++;
						}
						break;
					
					
					
					
				//checking if there is no hand on table
				}else if(this.getHandsOnTable().isEmpty()){ 
	
					//If the hand cannot be composed
					if(composeHand( tempPlayer, tempList ) == null ){

						this.print_cards();
						table.printMsg("^^^^Not a legal move!!!\n");
						break;
							
					}else if(!composeHand(playerList.get(this.getCurrentIdx()), tempList).contains(new BigTwoCard (0,2)) && table.line_check == 0   ){

						this.print_cards();
						table.printMsg("^^^^Not a legal move!!!\n");
						table.setPassButton_False();
						break;
							
					//Else composing hand and checking its validity
					}else if (composeHand( tempPlayer, tempList).isValid() ){
							
						table.pass_check = 0;
						this.getHandsOnTable().add(composeHand( tempPlayer, tempList ));
	
						lastPlayer = this.getPlayerList().get(this.getCurrentIdx());
		
						String type = composeHand( tempPlayer, tempList).getType();
						table.printMsg("{" + type + "} ");
						tempPlayer.removeCards(tempList); //removing the cards played from the cards held by player

						this.print_cards();					
								
	
						if(tempPlayer.getNumOfCards() == 0){ //checking for the win condition
									
							if(endOfGame()){
										
								//outputting the final results on a dialog
								
								table.printMsg("\n");
								table.printMsg("Game ends\n");
									
								String [] result = new String[4];
								
								for ( int i = 0; i < this.getPlayerList().size(); i++){
											
									tempPlayer = this.getPlayerList().get(i);
											
									if(this.getPlayerList().indexOf(tempPlayer) == this.getCurrentIdx()){
												
										table.printMsg(tempPlayer.getName() + " wins the game.\n");
										result[i] = tempPlayer.getName() + " wins the game.\n";		
									
									}else{
												
										table.printMsg(tempPlayer.getName() + " has " + tempPlayer.getNumOfCards() + " cards in hand.\n");
										result[i] = tempPlayer.getName() + " has " + tempPlayer.getNumOfCards() + " cards in hand.\n";
										
									}		
								}
								
								
								int result_input = JOptionPane.showOptionDialog(null, result[0]  + 
																		"\n" + result[1] + "\n" + result[2] + "\n" + 
																		result[3] + "\n" , "Game Ended!", 
																		JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

								if(result_input == JOptionPane.OK_OPTION){
								
									CardGameMessage end= new CardGameMessage(CardGameMessage.READY, -1, null);
									sendMessage(end);
								
								}
								
								if (result_input == JOptionPane.CANCEL_OPTION){
									System.exit(0);
								}
										
								table.disable();
								break;
							}
						}			
						
										
						if(currentIdx != 3){
							this.currentIdx++;
						}else{
							this.currentIdx = 0;
						}
									
						table.setActivePlayer(currentIdx); //setting the next active player
						table.repaint();
						table.line_check++;
						break;
					}
	
				//If the hands on table is not empty 	
				}else if (!this.getHandsOnTable().isEmpty()){
	
						
						//If the hand cannot be composed
						if(composeHand( tempPlayer, tempList ) == null){

							this.print_cards();
							table.printMsg("^^^^Not a legal move!!!\n");
							table.line_check++;
							break;
							
						//If the hand can be composed and it does not beat the previous hand on table
						}else if (composeHand( tempPlayer, tempList ) != null && !composeHand(tempPlayer, tempList).beats( this.getHandsOnTable().get(handsOnTable.size() - 1) ) ){

							this.print_cards();
							table.printMsg("^^^^Not a legal move!!!\n");
							table.line_check++;
							break;
						
						//If the hand is composed and it beats the previous hand on table	
						} else if (composeHand( tempPlayer, tempList).isValid() ){
	
							table.pass_check = 0;
							this.getHandsOnTable().add(composeHand( tempPlayer, tempList ));
	
							lastPlayer = this.getPlayerList().get(this.getCurrentIdx());
	
							String type = composeHand( tempPlayer, tempList).getType();
							table.printMsg("{" + type + "} ");

						    this.print_cards();	
							tempPlayer.removeCards(tempList); //removing the cards played from the cards held by player
	
							if(tempPlayer.getNumOfCards() == 0){ //checking for the win condition
								
								if(endOfGame()){
									
									//outputting the final results on a dialog
									
									table.printMsg("\n");
									table.printMsg("Game ends\n");
										
									String [] result = new String[4];
									
									for ( int i = 0; i < this.getPlayerList().size(); i++){
												
										tempPlayer = this.getPlayerList().get(i);
												
										if(this.getPlayerList().indexOf(tempPlayer) == this.getCurrentIdx()){
													
											table.printMsg(tempPlayer.getName() + " wins the game.\n");
											result[i] = tempPlayer.getName() + " wins the game.\n";		
										
										}else{
													
											table.printMsg(tempPlayer.getName() + " has " + tempPlayer.getNumOfCards() + " cards in hand.\n");
											result[i] = tempPlayer.getName() + " has " + tempPlayer.getNumOfCards() + " cards in hand.\n";
											
										}		
									}
									
									
									int result_input = JOptionPane.showOptionDialog(null, result[0]  + 
																			"\n" + result[1] + "\n" + result[2] + "\n" + 
																			result[3] + "\n" , "Game Ended!", 
																			JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

									if(result_input == JOptionPane.OK_OPTION){
									
										CardGameMessage end= new CardGameMessage(CardGameMessage.READY, -1, null);
										sendMessage(end);
									
									}
									
									if (result_input == JOptionPane.CANCEL_OPTION){
										System.exit(0);
									}
									table.disable();
									break;
								}
								
							}
									
							if(currentIdx != 3){
								this.currentIdx++;
							}else{
								this.currentIdx = 0;
							}
								
							table.setActivePlayer(currentIdx); //setting the next active player
							table.repaint();
							table.line_check++;
							
							break;
									
						}
					}	
				}
			}
	
		


	/**
	 *  a method for checking if the game ends. 
	 *  
	 *  @return trueOrfalse
	 */
	public boolean endOfGame(){
	
		CardGamePlayer tempPlayer = new CardGamePlayer(); 
		tempPlayer = this.getPlayerList().get(this.getCurrentIdx());
		
		if(tempPlayer.getNumOfCards() == 0){ //checking for the win condition
			
			return true;
	
		}
		
		return false;
	}

	/**
	 * Returns the playerID (index) of the local player.
	 * 
	 * @return the playerID (index) of the local player
	 */
	public int getPlayerID(){
		return playerID;
	}

	/**
	 * Sets the playerID (index) of the local player.
	 * 
	 * @param playerID
	 *            the playerID (index) of the local player.
	 */
	public void setPlayerID(int playerID){
		this.playerID = playerID;
	}

	/**
	 * Returns the name of the local player.
	 * 
	 * @return the name of the local player
	 */
	public String getPlayerName(){
		return playerName;
	}

	/**
	 * Sets the name of the local player.
	 * 
	 * @param playerName
	 *            the name of the local player
	 */
	public void setPlayerName(String playerName){
		this.playerName = playerName;
	}

	/**
	 * Returns the IP address of the server.
	 * 
	 * @return the IP address of the server
	 */
	public String getServerIP(){
		return serverIP;
	}

	/**
	 * Sets the IP address of the server.
	 * 
	 * @param serverIP
	 *            the IP address of the server
	 */
	public void setServerIP(String serverIP){
		this.serverIP = serverIP;
	}

	/**
	 * Returns the TCP port of the server.
	 * 
	 * @return the TCP port of the server
	 */
	public int getServerPort(){
		return serverPort;
	}

	/**
	 * Sets the TCP port of the server
	 * 
	 * @param serverPort
	 *            the TCP port of the server
	 */
	public void setServerPort(int serverPort){
		this.serverPort = serverPort;
	}

	/**
	 * Makes a network connection to the server.
	 */
	public void makeConnection(){
		
		try{
			
		    
			//taking the name input
			this.setPlayerName(JOptionPane.showInputDialog("Name"));
			
			if (this.getPlayerName() == null){
				System.exit(0);
			}
			
			//setting the server ip and port
			setServerIP("127.0.0.1");
			setServerPort(2396);
		//	JOptionPane.showInputDialog("Server IP:Port", "127.0.0.1:2396" );
		
			sock = new Socket(getServerIP(), getServerPort());

			oos = new ObjectOutputStream (sock.getOutputStream());
			
			Thread receiveMsg = new Thread (new ServerHandler(sock));
			receiveMsg.start();
			
			this.sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, (Object)this.getPlayerName()));
			this.sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
			
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		table.connect_menu.setEnabled(false);
		
		
	}

	/**
	 * Parses the specified message received from the server.
	 * 
	 * @param message
	 *            the specified message received from the server
	 */
	public void parseMessage(GameMessage message){
		
		if(message.getType() == CardGameMessage.PLAYER_LIST){
			
			this.setPlayerID(message.getPlayerID());
			numOfPlayers = playerID;
	
			
			String[] names = (String[]) message.getData();
			
			for (int i = 0; i < names.length; i++){
			
				if(names[i] == null){
					playerList.get(i).setName("");
				}else{
					playerList.get(i).setName(names[i]);
				}
				
			}
			table.repaint_only();
			
		} else if(message.getType() == CardGameMessage.JOIN){
			
			if(game_run_check == true){
				
				if(playerList.get(3).getName() != ""){
					numOfPlayers = 3;
				}
				
			}else{
				
				quit = true;
			
			}

			playerList.get(message.getPlayerID()).setName((String)message.getData());
			

			if(numOfPlayers < 4){
				numOfPlayers++;
			}
		

			if(game_run_check == true){
				
				for(int i = 0; i < 4; i++){
					
					if(playerList.get(i).getName() != ""){
					
						if(numOfPlayers < 4){
							numOfPlayers++;
						}
					}
				}
			}

			table.printMsg((String)message.getData() + " has joined the game!\n");
			table.repaint_only();
			
		
		} else if(message.getType() == CardGameMessage.FULL){
		
			table.printMsg("The server is full. You cannot join the game\n");
			table.connect_menu.setEnabled(true);
			
		} else if(message.getType() == CardGameMessage.QUIT){
			
			table.printMsg(this.getPlayerList().get(message.getPlayerID()).getName() + " has left the game! \n");
			playerList.get(message.getPlayerID()).setName("");
	
			if(game_run_check == true){				//if game is running and a player quits
				CardGameMessage temp = new CardGameMessage(CardGameMessage.READY,-1,null);
				sendMessage(temp);
				numOfPlayers--;
			}else{									////if game is not running and a player quits
				numOfPlayers--;
				quit = true;
			}
			
			
			table.repaint_only();
			
		} else if(message.getType() == CardGameMessage.READY){
		
			table.printMsg(playerList.get(message.getPlayerID()).getName() + " is ready\n");
			table.repaint_only();
			
		} else if(message.getType() == CardGameMessage.START){

			table.clearMsgArea();
			table.clearChatArea();
			table.printMsg("All players are ready. Game is Starting\n");
			table.printMsg("\n");
			table.enable();
			this.start((BigTwoDeck) message.getData());
			game_run_check = true;
				
		} else if(message.getType() == CardGameMessage.MOVE){
		
			this.checkMove(message.getPlayerID(), (int[]) message.getData());
			
		} else if(message.getType() == CardGameMessage.MSG){
		
			String temp = (String) message.getData();
			table.printChat(temp);
			
		}
		
	}

	/**
	 * Sends the specified message to the server.
	 * 
	 * @param message
	 *            the specified message to be sent the server
	 */
	public void sendMessage(GameMessage message){
		
		try{
		
			oos.writeObject(message);
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * an inner class that implements the Runnable interface. 
	 * 
	 * @author Zain Ul Abidin
	 */
	public class ServerHandler implements Runnable{
		
		private ObjectInputStream inputStream;
		
		/**
		 * Constructor for serverhandler
		 * 
		 * @param clientSocket
		 * 			the socket of client
		 */
		public ServerHandler(Socket clientSocket){
		
			try{
			
				inputStream = new ObjectInputStream(clientSocket.getInputStream());
				
			}catch (Exception ex){
				ex.printStackTrace();
			}
			
		}
		
		
		/** 
		 * Runs the thread
		 * 
		 */
		public synchronized void run(){

			CardGameMessage messages = new CardGameMessage(CardGameMessage.MSG, -1, null);
			
			try{
			
				while (true){
					messages = (CardGameMessage)inputStream.readObject();
					parseMessage(messages);
			
				}
		
			} catch (Exception ex){
				table.connect_menu.setEnabled(true);
				ex.printStackTrace();
			}
		}
	} 
	
	
	
	
	/**
	 * A method for starting a Big Two card game
	 * 
	 * @param args
	 * 			not being used in this application
	 * 
	 */
	public static void main(String[] args){
		
		BigTwoClient client = new BigTwoClient(); 
		
	}
	
	/**
	 * A method for returning a valid hand from the 
	 * specified list of cards of the player. Returns
	 * null is no valid hand can be composed from the 
	 * specified list of cards. 
	 * 
	 * @param player
	 * 				The player 
	 * @param cards
	 * 				List of cards
	 * 
	 * @return returns a valid hand from the specified list of 
	 * 		   cards of the player. Returns null is no valid hand 
	 * 		   can be composed from the specified list of cards.
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards){
		
		//Checking for single hand.
		if(cards.size() == 1){
			
			Single single = new Single(player, cards); //forms Single hand
			
			if (single.isValid()){
				return single;
			}
			
		//Checking for Pair hand.	
		}else if (cards.size() == 2){
			
			Pair pair = new Pair(player, cards); //forms Pair hand
			
			if (pair.isValid()){
				return pair;
			}
		
		//Checking for Triple hand.	
		}else if (cards.size() == 3){
			
			Triple triple = new Triple(player, cards); //forms Triple hand
			
			if (triple.isValid()){
				return triple;
			}
			
		//Checking for hands with 5 cards.			
		}else if (cards.size() == 5){
			
			Straight straight = new Straight(player, cards); //forms Straight hand

			if (straight.isValid()){
				return straight;
			}
			
			Flush flush = new Flush(player, cards); //forms Flush hand

			if (flush.isValid()){
				return flush;
			}			
		
			FullHouse fullhouse = new FullHouse(player, cards); //forms FullHouse hand

			if (fullhouse.isValid()){
				return fullhouse;
			}
		
		
			Quad quad= new Quad(player, cards); //forms Quad hand

			if (quad.isValid()){
				return quad;
			}

			StraightFlush straightflush = new StraightFlush(player, cards); //forms StraightFlush hand

			if (straightflush.isValid()){
				return straightflush;
			}
		}
		
		return null;
	}

}


