import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * The BigTwoTable class implements the CardGameTable interface. 
 * It is used to build a GUI for the Big Two card game and handle 
 * all user actions. Below is a detailed description for the BigTwoTable class. 
 * 
 * @author Zain Ul Abidin
 */
public class BigTwoTable implements CardGameTable {

	private BigTwoClient game; 		// a client associated with the table.
	private boolean[] selected;		// a boolean array indicating which cards are being selected.
	private int activePlayer;		// an integer specifying the index of the active player.
	private JFrame frame;			// the main window of the application. 
	private JPanel bigTwoPanel;		// a panel for showing the cards of each player and the cards played on the table. 
	private JButton playButton;		// button for the active player to play the selected cards.
	private JButton passButton;		// button for the active player to pass his/her turn to the next player.
	private JTextArea msgArea;		// a text area for showing the current game status as well as end of game messages.
	private JTextArea chatArea;		// a text area for showing the chat message.
	private Image[][] cardImages;	// a 2D array storing the images for the faces of the cards
	private Image cardBackImage;	// an image for the backs of the cards
	private Image[] avatars;		// an array storing the images for the avatars. 
	private JTextField chat; 		//a text field for chat 
	
	/**
	 * a menu item to connect
	 */
	public JMenuItem connect_menu; 	//a menu item to connect	
	
	/**
	 * A flag variable to check the number of passes
	 */
	public int pass_check = 0;				//flag variable for checking number of passes
	
	/**
	 * A flag variable for checking if it is the first turn or not
	 */
	public int line_check = 0;				//flag variable for checking the first turn
	
	/**
	 * a constructor for creating a BigTwoTable
	 * 
	 * @param game
	 * 			Card Game
	 */
	public BigTwoTable(BigTwoClient game){
		
		this.game = game;												
		frame = new JFrame();										//creating a JFrame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		bigTwoPanel = new BigTwoPanel();							//creating a BigTwoPanel
			
		avatars = new Image[4];										//initiating an array to hold avatar
		cardImages = new Image [4][13];		//initiating an array to hold card images
		selected = new boolean [13];		//initiating an array to hold card index
		
		
		//Implementing Menu Bar
		JMenuBar menuBar;
		JMenu game_menu;		//game menu
		JMenu messages_menu;	//messages menu
		
		//initializing 
		menuBar	= new JMenuBar();	
		game_menu = new JMenu("Game");	
		messages_menu = new JMenu("Messages");
		
		JMenuItem clearServer = new JMenuItem("Clear server console");
		JMenuItem clearChat = new JMenuItem("Clear chat console");
		connect_menu	= new JMenuItem("Connect");					//a connect menu item
		JMenuItem quit_menu	= new JMenuItem("Quit");					//a quit menu item
		
		//adding action listeners
		clearServer.addActionListener(new clearServerListener());
		clearChat.addActionListener(new clearChatListener());
		connect_menu.addActionListener(new ConnectMenuItemListener());	
		quit_menu.addActionListener(new QuitMenuItemListener());		
		
		//adding the menu items to the menu bar and adding the menu bar to the frame
		game_menu.add(connect_menu);
		game_menu.add(quit_menu);
		messages_menu.add(clearServer);
		messages_menu.add(clearChat);
		menuBar.add(game_menu);
		menuBar.add(messages_menu);
		frame.setJMenuBar(menuBar);
		
		//implementing the buttons and text field
		JPanel button_chat_Panel = new JPanel();
		
		playButton = new JButton("Play");				//creating a play button
		playButton.addActionListener(new PlayButtonListener());		//adding action listener
		
		passButton = new JButton("Pass");				//creating a pass button
		passButton.addActionListener(new PassButtonListener());		//adding action listener
		
		JLabel label = new JLabel("Message:");
		chat = new JTextField(40);
		chat.addActionListener(new ChatListener());	
		
		//adding buttons to button panel and then button panel to the frame.
		button_chat_Panel.add(playButton);
		button_chat_Panel.add(passButton);
		button_chat_Panel.add(label);
		button_chat_Panel.add(chat);
		
		frame.add(button_chat_Panel, BorderLayout.SOUTH);
		
			
		
		//implementing the Text Area
		msgArea = new JTextArea(20,25);
		JScrollPane scroller = new JScrollPane(msgArea);	
		msgArea.setLineWrap(true);	
		JPanel textarea_panel = new JPanel();	
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);	
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		//implementing the Chat Area
		chatArea = new JTextArea(20,25);
		JScrollPane scroller_msg = new JScrollPane(chatArea);	
		chatArea.setLineWrap(true);	
		textarea_panel.add(scroller_msg);		//adding scroller to the text area panel
		scroller_msg.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);	
		scroller_msg.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		
		//adding both the message and chat area to panel
		textarea_panel.setLayout(new BoxLayout(textarea_panel, BoxLayout.Y_AXIS));	
		textarea_panel.add(scroller);
		textarea_panel.add(scroller_msg);

		
		//adding all the panels
		frame.add(bigTwoPanel, BorderLayout.CENTER);
		frame.add(textarea_panel, BorderLayout.EAST);
		
		//setting the frame size and setting it to visible
		frame.setSize(1366, 768);
		frame.setVisible(true);

	}
	
	
	
	/**
	 * Sets the index of the active player (i.e., the current player).
	 * 
	 * @param activePlayer
	 *            an int value representing the index of the active player
	 */
	public void setActivePlayer(int activePlayer){

		if (activePlayer < 0 || activePlayer >= game.getPlayerList().size()) {
			this.activePlayer = -1;
		} else {
			this.activePlayer = activePlayer;
		}
	}

	/**
	 * Returns an array of indices of the cards selected.
	 * 
	 * @return an array of indices of the cards selected
	 */
	public int[] getSelected(){
		
		int[] cardIdx = null;
		int count = 0;
		for (int j = 0; j < selected.length; j++) {
			if (selected[j]) {
				count++;
			}
		}

		if (count != 0) {
			cardIdx = new int[count];
			count = 0;
			for (int j = 0; j < selected.length; j++) {
				if (selected[j]) {
					cardIdx[count] = j;
					count++;
				}
			}
		}
		return cardIdx;	
	}

	/**
	 * Resets the list of selected cards to an empty list.
	 */
	public void resetSelected(){
		
		//setting all selected to false
		for( int i = 0; i < selected.length; i++){
			
			selected[i] = false;
			
		}	
	}
	
	
	
	/**
	 * Repaints the GUI without outputting anything on the table.
	 */
	public void repaint_only(){
		
		frame.repaint();		
	
	}

	/**
	 * Repaints the GUI.
	 */
	public void repaint(){
		
		if(game.getNumOfPlayers() == 4){
		
			if(game.getCurrentIdx() == game.getPlayerID()){
				printMsg("Your turn:\n");	
			}else{
				printMsg("Player " + game.getPlayerList().get(activePlayer).getName() + "'s turn:\n");
			}
		}
		frame.repaint();		
	
	}

	/**
	 * Prints the specified string to the message area of the card game table.
	 * 
	 * @param msg
	 *            the string to be printed to the message area of the card game
	 *            table
	 */
	public void printMsg(String msg){
		
		msgArea.append(msg);			//appending the messages to the text area
		msgArea.setCaretPosition(msgArea.getDocument().getLength());
		
	}
	
	/**
	 * Prints the specified string to the chat area of the card game table.
	 * 
	 * @param msg
	 *            the string to be printed to the chat of the card game
	 *            table
	 */
	public void printChat(String msg){
		
		chatArea.append(msg);			//appending the messages to the chat area
		chatArea.setCaretPosition(chatArea.getDocument().getLength());
		
	}
	
	

	/**
	 * Clears the message area of the card game table.
	 */
	public void clearMsgArea(){
		
		msgArea.setText("");		//clearing the text area
		
	}
	
	/**
	 * Clears the chat area of the card game table.
	 */
	public void clearChatArea(){
		
		chatArea.setText("");		//clearing the chat area
		
	}

	/**
	 * Resets the GUI.
	 */
	public void reset(){
		
		enable();			//setting the buttons to enable
		line_check = 0;		//setting flag variables to zero
		pass_check = 0;		//setting flag variables to zero
		resetSelected();		
		clearMsgArea();
		
		BigTwoDeck deck  = new BigTwoDeck();		//initiating a big two deck
		deck.initialize();		//initializing the deck
		deck.shuffle();
		game.start(deck);		//starting the game
		
		
	}

	/**
	 * Enables user interactions.
	 */
	public void enable(){
		
		//setting the buttons to true
		playButton.setEnabled(true);
		passButton.setEnabled(true);
	
	}

	/**
	 * Disables user interactions.
	 */
	public void disable(){

		//setting the buttons to false
		playButton.setEnabled(false);
		passButton.setEnabled(false);
	
	}
	
	/**
	 * an inner class that extends the JPanel class and 
	 * implements the MouseListener interface. Overrides 
	 * the paintComponent() method inherited from the JPanel 
	 * class to draw the card game table. Implements the 
	 * mouseClicked() method from the MouseListener interface 
	 * to handle mouse click events.
	 * 
	 * @author Zain Ul Abidin
	 */
	class BigTwoPanel extends JPanel implements MouseListener{
		
		/**
		 * a BigTwoPanel constructor.
		 */
		public BigTwoPanel (){
			this.addMouseListener(this);		//adding mouse listener to the panel
		}
		
		/** 
		 * Contains all the information of designing the GUI
		 */
		public void paintComponent(Graphics g){
			
			g.setColor(Color.GREEN);
			g.fillRect(0, 0, (int)this.getSize().getWidth(), (int)this.getSize().getHeight());		//making a background for the game area
			g.setColor(Color.BLACK);
			
			//drawing lines to separate the playing fields 
			g.drawLine(0, (int)this.getSize().getHeight()/5, (int)this.getSize().getWidth(), (int)this.getSize().getHeight()/5);
			g.drawLine(0, 2*(int)this.getSize().getHeight()/5, (int)this.getSize().getWidth(), 2*(int)this.getSize().getHeight()/5);
			g.drawLine(0, 3*(int)this.getSize().getHeight()/5, (int)this.getSize().getWidth(), 3*(int)this.getSize().getHeight()/5);
			g.drawLine(0, 4*(int)this.getSize().getHeight()/5, (int)this.getSize().getWidth(), 4*(int)this.getSize().getHeight()/5);
		
			//loading the images from folder Images/Avatars
			for(int i = 0; i < avatars.length; i++){
				
				avatars[i] = new ImageIcon("Images/Avatars/"+i+".png").getImage();		
			
			}

			
			//displaying the player avatars and writing the names of Players
			if(game.game_run_check == true){
			
				for(int i = 0 ; i < 4; i++){
				
					if(game.getPlayerList().get(i).getName() == ""){
	
						continue;
	
					}else{
						
						if( i == game.getPlayerID()){
							g.setColor(Color.RED);
							g.drawString("You", 10, 15 + i*(int)this.getSize().getHeight()/5);
							g.drawImage(avatars[i], 0, 40 + i * (int)this.getSize().getHeight()/5, this);
						}else{
							g.setColor(Color.BLACK);
							g.drawString(game.getPlayerList().get(i).getName(), 10, 15 + i*(int)this.getSize().getHeight()/5);
							g.drawImage(avatars[i], 0, 40 + i * (int)this.getSize().getHeight()/5, this);
						}
					}
				}
				
			}else{
				
				if(game.quit == true){
		
					for(int i = 0 ; i < 4; i++){
						
						if(game.getPlayerList().get(i).getName() == ""){
		
							continue;
		
						}else{
							
							if( i == game.getPlayerID()){
								g.setColor(Color.RED);
								g.drawString("You", 10, 15 + i*(int)this.getSize().getHeight()/5);
								g.drawImage(avatars[i], 0, 40 + i * (int)this.getSize().getHeight()/5, this);
							}else{
								g.setColor(Color.BLACK);
								g.drawString(game.getPlayerList().get(i).getName(), 10, 15 + i*(int)this.getSize().getHeight()/5);
								g.drawImage(avatars[i], 0, 40 + i * (int)this.getSize().getHeight()/5, this);
							}
						}
					}		
			
				}else{
					
					for(int i = 0 ; i < game.getNumOfPlayers(); i++){
						
						if(game.getPlayerList().get(i).getName() == ""){
		
							continue;
		
						}else{
							
							if( i == game.getPlayerID()){
								g.setColor(Color.RED);
								g.drawString("You", 10, 15 + i*(int)this.getSize().getHeight()/5);
								g.drawImage(avatars[i], 0, 40 + i * (int)this.getSize().getHeight()/5, this);
							}else{
								g.setColor(Color.BLACK);
								g.drawString(game.getPlayerList().get(i).getName(), 10, 15 + i*(int)this.getSize().getHeight()/5);
								g.drawImage(avatars[i], 0, 40 + i * (int)this.getSize().getHeight()/5, this);
							}
						}
					}
				}
			}
			
			
			g.setColor(Color.BLACK);
			
			//Displaying the status of last hands played
			if(game.getHandsOnTable().isEmpty()){
				g.drawString("No Hands Played", 10, 15 + 4*(int)this.getSize().getHeight()/5);
			}else{
				g.drawString("Played by "+ game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getPlayer().getName(), 10, 15 + 4*(int)this.getSize().getHeight()/5);
			}	

			
			//loading the images of all the 52 cards from Images/cards
			for(int i = 0; i < 4; i++){
				
				for (int j = 1; j < 14; j++){
					
					if(i==0){
						
						cardImages[i][j-1] = new ImageIcon ("Images/cards/"+(j)+"d.gif").getImage();
						
					}else if(i==1){
						
						cardImages[i][j-1] = new ImageIcon ("Images/cards/"+(j)+"c.gif").getImage();
						
					}else if(i==2){
						
						cardImages[i][j-1] = new ImageIcon ("Images/cards/"+(j)+"h.gif").getImage();
						
					}else if(i==3){
						
						cardImages[i][j-1] = new ImageIcon ("Images/cards/"+(j)+"s.gif").getImage();
						
					}	
				}
			}
			
			//x and y coordinate for rendering
			int x = 100;					
			int y = 25;						
			
			//variables to hold the rank and suit of cards
			int rank;
			int suit;
			
			
			//loading card back image 
			cardBackImage = new ImageIcon ("Images/cards/b.gif").getImage();
			
			//for loop for displaying back cards of players.
			for(int f = 0; f < 4; f++){
				
				if(f != game.getPlayerID()){
				
					for(int j = 0; j < game.getPlayerList().get(f).getCardsInHand().size(); j++){
					
						g.drawImage(cardBackImage, 100+j*25, y+f*(int) this.getHeight()/5, this);
						
					}
				
				}else{
						
					for(int i = 0; i < game.getPlayerList().get(f).getCardsInHand().size(); i++){
						
						if (selected[i]){
							
							rank = game.getPlayerList().get(f).getCardsInHand().getCard(i).getRank();
							suit = game.getPlayerList().get(f).getCardsInHand().getCard(i).getSuit();
							g.drawImage(cardImages[suit][rank], x+i*25, y - 10 + game.getPlayerID() * (int)this.getHeight()/5, this);
							this.repaint();
							
						}else{
							
							rank = game.getPlayerList().get(f).getCardsInHand().getCard(i).getRank();
							suit = game.getPlayerList().get(f).getCardsInHand().getCard(i).getSuit();
							g.drawImage(cardImages[suit][rank], x+i*25, y + game.getPlayerID() * (int)this.getHeight()/5, this);
							this.repaint();
							
						}	
				}
					
			}
		}
			
			//Displaying the last hand played on the table
			if(!game.getHandsOnTable().isEmpty()){
			
				for(int i = 0; i < game.getHandsOnTable().get(game.getHandsOnTable().size()-1).size(); i++){
			
					rank = game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getCard(i).getRank();
					suit = game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getCard(i).getSuit();	
					g.drawImage(cardImages[suit][rank], 10+i*25, y + 4*(int)this.getSize().getHeight()/5, this);
					
				}
			}
			
		}
		
		/**
		 * Method for handling the mouse pressed event
		 */
		public void mousePressed(MouseEvent e){
			 
		}
		
		//card dimensions 97*73
		/**
		 * Method for handling the mouse clicked event
		 */
		public void mouseClicked(MouseEvent e){
			 
			double mouse_x = e.getX();
			double mouse_y = e.getY();	
			
			for (int i = 0; i < game.getPlayerList().get(game.getPlayerID()).getCardsInHand().size(); ++i){
				
				
				//To handle the last card
				if (i == game.getPlayerList().get(game.getPlayerID()).getCardsInHand().size()-1){
					
					if(mouse_x>(100+i*25) && mouse_x<(125+48+i*25) && mouse_y>(25+game.getPlayerID()*(int) this.getHeight()/5) && mouse_y<(122+game.getPlayerID()*(int) this.getHeight()/5) && selected[i]==false){
						
						selected[i]=true;
						bigTwoPanel.repaint();
					
					}else if (mouse_x>(100+i*25) && mouse_x<(125+48+i*25) && mouse_y>(25-10+game.getPlayerID()*(int) this.getHeight()/5) && mouse_y<(122-10+game.getPlayerID()*(int) this.getHeight()/5) && selected[i]==true){
						
						selected[i]=false;
						bigTwoPanel.repaint();
					
					}else if(mouse_x> (100+i*25) && mouse_x<(173 - 25 + i*25) &&  mouse_y< (122 + game.getPlayerID()*(int) this.getHeight()/5) && mouse_y> (122-10 +game.getPlayerID()*(int) this.getHeight()/5) && selected[i-1]== false &&selected[i]==true){
						
						selected[i-1]=true;
						bigTwoPanel.repaint();
					
					}else if(mouse_x> (100+i*25) && mouse_x<(125+i*25) && mouse_y< (122 + game.getPlayerID()*(int) this.getHeight()/5) && mouse_y> (122-10 +game.getPlayerID()*(int) this.getHeight()/5)&& selected[i-1]== true && selected[i-2]==false &&selected[i]==true ){
					
						selected[i-2]=true;
						bigTwoPanel.repaint();
					
					}else if(mouse_x> (100+i*25) && mouse_x<(125+i*25) && mouse_y< (122 + game.getPlayerID()*(int) this.getHeight()/5) && mouse_y> (122-10 +game.getPlayerID()*(int) this.getHeight()/5) && selected[i-1]== false && selected[i-2]==false && selected[i]==true){
						
						selected[i-1]=true;
						bigTwoPanel.repaint();
				
					}
				
					
				//Handling the cases for cards other than the last card
				}else{
					
					if(mouse_x>(100+i*25) && mouse_x<(125+i*25) && mouse_y>(25+game.getPlayerID()*(int) this.getHeight()/5) && mouse_y<(122+game.getPlayerID()*(int) this.getHeight()/5) && selected[i]==false){
						
						selected[i]=true;
						bigTwoPanel.repaint();
					
					}else if ((mouse_x>(100+i*25) && mouse_x<(125+i*25) && mouse_y>(25-10+game.getPlayerID()*(int) this.getHeight()/5) && mouse_y<(122-10+game.getPlayerID()*(int) this.getHeight()/5) && selected[i]==true && selected[i+1]==true)||(mouse_x>(125+i*25) && mouse_x<(173+i*25) && mouse_y>(25-10+game.getPlayerID()*(int) this.getHeight()/5) && mouse_y<(122-97+game.getPlayerID()*(int) this.getHeight()/5) && selected[i]==true && selected[i+1]==false)){
						
						selected[i]=false;
						bigTwoPanel.repaint();
					
					}else if( mouse_x> (100+i*25) && mouse_x<(125+i*25) && mouse_y>(25-10+game.getPlayerID()*(int) this.getHeight()/5) && mouse_y < (122-10+game.getPlayerID()*(int) this.getHeight()/5) && selected[i]==true){
					
						selected[i]=false;
						bigTwoPanel.repaint();
					
					}else if(mouse_x> (100+i*25) && mouse_x<(125+i*25) &&  mouse_y< (122 + game.getPlayerID()*(int) this.getHeight()/5) && mouse_y> (122-10 +game.getPlayerID()*(int) this.getHeight()/5) && selected[i-1]== false &&selected[i]==true){
						
						selected[i-1]=true;
						bigTwoPanel.repaint();
					
					}else if(mouse_x> (100+i*25) && mouse_x<(125+i*25) && mouse_y< (122 + game.getPlayerID()*(int) this.getHeight()/5) && mouse_y> (122-10 +game.getPlayerID()*(int) this.getHeight()/5)&& selected[i-1]== true && selected[i-2]==false &&selected[i]==true ){
					
						selected[i-2]=true;
						bigTwoPanel.repaint();
					
					}
					
					else if(mouse_x> (100+i*25) && mouse_x<(125+i*25) && mouse_y< (122 + game.getPlayerID()*(int) this.getHeight()/5) && mouse_y> (122-10 +game.getPlayerID()*(int) this.getHeight()/5) && selected[i-1]== false && selected[i-2]==false && selected[i]==true){
					
						selected[i-1]=true;
						bigTwoPanel.repaint();
					
					}
					
				}
					
			}

		}

		/**
		 * Method for handling the mouse exited event
		 */
		public void mouseExited(MouseEvent e){
			 
		}
		
		/**
		 * Method for handling the mouse entered event
		 */
		public void mouseEntered(MouseEvent e){

		}
		
		/**
		 * Method for handling the mouse released event
		 */
		public void mouseReleased(MouseEvent e){

		}

		
	}
	
	
	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener 
	 * interface to handle button-click events for the “Play” button. 
	 * 
	 * @author Zain Ul Abidin
	 */
	class PlayButtonListener implements ActionListener{
		
		/** 
		 * Contains the set of actions to perform when called upon
		 */
		public void actionPerformed(ActionEvent event){

			if(game.getCurrentIdx() == game.getPlayerID()){
				
				int[] cards_selected = getSelected();	//getting the card index selected in an array
				
				//if no card is selected than play wont call check move
				if(cards_selected != null){
					game.makeMove(game.getPlayerID() , cards_selected);
				}
				
				resetSelected();
		
			}
		}
	}
		
	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener 
	 * interface to handle button-click events for the “Pass” button
	 * 
	 * @author Zain Ul Abidin
	 */
	class PassButtonListener implements ActionListener{

		/** 
		 * Contains the set of actions to perform when called upon
		 */
		public void actionPerformed(ActionEvent event){
			
			if(game.getCurrentIdx() == game.getPlayerID()){

				if(pass_check == 2 ){
					passButton.setEnabled(false);
				}
	
				resetSelected();
				
				//if a move is passed then no cards will be selected.
				int[] cards_selected = null;
				game.makeMove(game.getPlayerID(), cards_selected);
				
			}	
		}
	}
	
	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener 
	 * interface to handle menu-item-click events for the “Restart” menu 
	 * item. 
	 * 
	 * @author Zain Ul Abidin
	 */
	class ConnectMenuItemListener implements ActionListener{
		
		/** 
		 * Contains the set of actions to perform when called upon
		 */
		public void actionPerformed(ActionEvent event){
				
			game.makeConnection(); //makes a connection

		}
		
	}

	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener 
	 * interface to handle menu-item-click events for the “Quit” menu item. 
	 * 
	 * @author Zain Ul Abidin
	 */
	class QuitMenuItemListener implements ActionListener{
	
		/** 
		 * Contains the set of actions to perform when called upon
		 */
		public void actionPerformed(ActionEvent event){
				
			System.exit(0);	//Exits the system
			
		}
	}
	
	
	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener 
	 * interface to handle Chat Messages. 
	 * 
	 * @author Zain Ul Abidin
	 */
	class ChatListener implements ActionListener{
		
		/** 
		 * Contains the set of actions to perform when called upon
		 */
		public void actionPerformed(ActionEvent event){
			
			String message = (String) (chat.getText() + "\n");
			game.sendMessage(new CardGameMessage (CardGameMessage.MSG, -1, message));
			chat.setText(null);
			//chat.requestFocus();
			
		}
	}
	

	
	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener 
	 * interface to handle menu-item-click events for the “Clear server console” menu item. 
	 * 
	 * @author Zain Ul Abidin
	 */
	class clearServerListener implements ActionListener{
		
		/** 
		 * Contains the set of actions to perform when called upon
		 */
		public void actionPerformed(ActionEvent event){
			
			clearMsgArea();
			
		}
	}
	
	
	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener 
	 * interface to handle menu-item-click events for the “Clear chat console” menu item. 
	 * 
	 * @author Zain Ul Abidin
	 */
	class clearChatListener implements ActionListener{
		
		/** 
		 * Contains the set of actions to perform when called upon
		 */
		public void actionPerformed(ActionEvent event){
			
			clearChatArea();
			
		}
	} 
	
	
	/**
	 * A method to disable Pass Button
	 */
	public void setPassButton_False(){
		passButton.setEnabled(false);
	}
	
	/**
	 * A method to enable Pass Button
	 */
	public void setPassButton_True(){
		passButton.setEnabled(true);
	}
	

}


