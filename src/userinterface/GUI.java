package userinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import card.Card;
import card.Face;
import card.Hand;
import logic.Table;
import logic.TableException;
import player.CPU;
import player.CPUException;
import player.Dealer;
import player.Human;
import player.HumanException;
import player.Person;
import player.PersonException;
import player.Player;
import player.PlayerException;

/**
 * The graphics layer of the game, manages all of the users interactions with 
 * the logic layer.
 * 
 * @author Brodie Robertson
 * @version 1.4.0
 * @since 1.2.0
 */
public class GUI extends JFrame 
{
	/**
	 * Width of the main window.
	 */
	public static final int WIDTH = 1920;
	/**
	 * Height of the main window.
	 */
	public static final int HEIGHT = 1080;
	/**
	 * Height of the main window button panel.
	 */
	public static final int TITLE_BUTTON_PANEL_HEIGHT = 80;
	/**
	 * Vertical gap between buttons on the main button panel.
	 */
	public static final int TITLE_BUTTON_VGAP = TITLE_BUTTON_PANEL_HEIGHT / 4;
	/**
	 * Horizontal gap between buttons on the main button panel.
	 */
	public static final int TITLE_BUTTON_HGAP = TITLE_BUTTON_PANEL_HEIGHT / 4;
	/**
	 * Height of tiny window button panel.
	 */
	public static final int TINY_BUTTON_PANEL_HEIGHT = 60;
	/**
	 * Vertical gap between buttons on tiny window button panels.
	 */
	public static final int TINY_BUTTON_VGAP = TINY_BUTTON_PANEL_HEIGHT / 4;
	/**
	 * Horizontal gap between buttons on tiny window button panels.
	 */
	public static final int TINY_BUTTON_HGAP = TINY_BUTTON_PANEL_HEIGHT / 4;
	/**
	 * Height reduced window button panel.
	 */
	public static final int REDUCED_BUTTON_PANEL_HEIGHT = 60;
	/**
	 * Vertical gap between buttons on reduced window button panels.
	 */
	public static final int REDUCED_BUTTON_VGAP = REDUCED_BUTTON_PANEL_HEIGHT / 4;
	/**
	 * Horizontal gap between buttons on reduced window button panels.
	 */
	public static final int REDUCED_BUTTON_HGAP = REDUCED_BUTTON_PANEL_HEIGHT / 4;
	/**
	 * Font used for all buttons in the game.
	 */
	private Font buttonFont = new Font("Verdana", Font.BOLD, 15);
	/**
	 * Size of all the buttons in the game.
	 */
	private Dimension buttonSize = new Dimension(130, TITLE_BUTTON_PANEL_HEIGHT/2);
	/**
	 * Size of small windows which don't require many elements.
	 */
	private Dimension tinyWindow = new Dimension(600, 150);
	/**
	 * Size of small windows which require more elements than the tiny window.
	 */
	private Dimension reducedWindow = new Dimension(500, 400);
	/**
	 * Array of panels containing each player's game relevant statistics.
	 */
	private PlayerPanel[] playerPanels;
	/**
	 * Panel containing the dealer's game relevant statistics.
	 */
	private DealerPanel dealerPanel;
	/**
	 * Text area containing a log of all events that occurred during the game.
	 */
	private JTextArea gameLog;
	/**
	 * The logic layer for the game.
	 */
	private Table table;
	
	/**
	 * Button listener for the main window.
	 * 
	 * @author Brodie Robertson
	 * @version 1.2.0
	 * @since 1.2.0
	 */
	private class ButtonListener implements ActionListener
	{
		/**
		 * Activates when a button attached to this object is pressed.
		 * 
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 * @since 1.2.0
		 */
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			String command = e.getActionCommand();
			if(command.equals("Start Game"))
			{
				GameSettingsWindow window = new GameSettingsWindow();
				window.setVisible(true);
			}
			else if(command.equals("Quit Game"))
			{
				CloseWindow window = new CloseWindow();
				window.setVisible(true);
			}
			else if(command.equals("About"))
			{
				AboutWindow window = new AboutWindow();
				window.setVisible(true);
			}
			else if(command.equals("Statistics"))
			{
				StatisticsWindow window = new StatisticsWindow();
				window.setVisible(true);
			}
			else
			{
				System.out.println("Unexpected Error in the Title Screen");
			}
		}
	}
	
	/**
	 * Window adapter for the main window, activates when the window is closed.
	 * 
	 * @author Brodie Robertson
	 * @version 1.2.0
	 * @since 1.2.0
	 */
	private class CheckOnExit extends WindowAdapter
	{
		@Override
		public void windowClosing(WindowEvent e)
		{
			CloseWindow window = new CloseWindow();
			window.setVisible(true);
		}
	}
	
	/**
	 * Dialog window used for confirming the conclusion of the program.
	 * 
	 * @author Brodie Robertson
	 * @version 1.2.0
	 * @since 1.2.0
	 */
	private class CloseWindow extends JDialog
	{	
		/**
		 * Button listener for the CloseWindow.
		 * 
		 * @author Brodie Robertson
		 * @version 1.2.0
		 * @since 1.2.0
		 */
		private class ButtonListener implements ActionListener
		{
			/**
			 * Activates when a button attached to this object is pressed.
			 * 
			 * (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 * @since 1.2.0
			 */
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String command = e.getActionCommand();
				
				if(command.equals("Confirm"))
				{
					System.exit(0);
				}
				else if(command.equals("Cancel"))
				{
					dispose();
				}
				else
				{
					System.out.println("Unexpected Error in the Close Window");
				}
			}
		}
		
		/**
		 * Constructs a close window with a default layout.
		 * 
		 * @since 1.2.0
		 */
		public CloseWindow()
		{
			setTitle("Are you sure?");
			setSize(tinyWindow);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setModalityType(ModalityType.APPLICATION_MODAL);
			setLayout(new BorderLayout());
			getContentPane().setBackground(Color.LIGHT_GRAY);
			setResizable(false);
			
			JLabel message = new JLabel("Are you sure you want to exit?");
			message.setHorizontalAlignment(JLabel.CENTER);
			add(message, BorderLayout.CENTER);
			
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout());
			buttonPanel.setBackground(Color.DARK_GRAY);
			JButton confirmButton = new JButton("Confirm");
			confirmButton.addActionListener(new ButtonListener());
			buttonPanel.add(confirmButton);
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ButtonListener());
			buttonPanel.add(cancelButton);
			add(buttonPanel, BorderLayout.SOUTH);
		}
	}
	
	/**
	 * Dialog window used for setting the games options.
	 * 
	 * @author Brodie Robertson
	 * @version 1.2.0
	 * @since 1.0.0
	 */
	private class GameSettingsWindow extends JDialog
	{
		/**
		 * Text field for the number of rounds.
		 */
		private JTextField roundInput;
		/**
		 * Text field for the number of human players.
		 */
		private JTextField humanInput;
		/**
		 * Text field for the number of CPU players.
		 */
		private JTextField cpuInput;
		/**
		 * Label for displaying a round error.
		 */
		private JLabel roundError;
		/**
		 * Label for displaying an human error.
		 */
		private JLabel humanError;
		/**
		 * Label for displaying a CPU error.
		 */
		private JLabel cpuError;
		
		/**
		 * Dialog window used for setting the name of a human Player.
		 * 
		 * @author Brodie Robertson
		 * @version 1.2.0
		 * @version 1.2.0
		 */
		private class SetNameWindow extends JDialog
		{
			/**
			 * Index of the human.
			 */
			private int index;
			/**
			 * Index of the last human.
			 */
			private int maxIndex;
			/**
			 * Label for displaying a name error.
			 */
			private JLabel error;
			/**
			 * Text field for the name of the human.
			 */
			private JTextField input;
			
			/**
			 * Button listener for the SetNameWindow.
			 * 
			 * @author Brodie Robertson
			 * @version 1.2.0
			 * @since 1.2.0
			 */
			private class ButtonListener implements ActionListener
			{
				/**
				 * Activates when a button attached to this object is pressed.
				 * 
				 * (non-Javadoc)
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					String command = e.getActionCommand();
					if(command.equals("Confirm"))
					{
						//Set the name of the player to the name in the input.
						try 
						{
							table.setPersonNameAtIndex(index, input.getText());
						} 
						//Exception thrown when the name is invalid.
						catch (PersonException ex) 
						{
							error.setText(ex.getMessage());
							input.setText("");
							return;
						}
						dispose();
						nextName(index, maxIndex);
					}
					//Reset the input and error labels.
					else if(command.equals("Clear"))
					{
						input.setText("");
						error.setText("");
					}
					else
					{
						System.err.println("Unexpected Error in Set Name Window");
					}
				}	
			}
			
			/**
			 * Constructs a SetNameWindow with a title, the index of the human
			 * and the index of the final human.
			 * 
			 * @param title The title of the SetNameWindow.
			 * @param index The index of the human.
			 * @param maxIndex The index of the final human.
			 */
			public SetNameWindow(String title, int index, int maxIndex)
			{
				setSize(tinyWindow);
				setTitle(title);
				setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				setResizable(false);
				setModalityType(ModalityType.APPLICATION_MODAL);
				setLayout(new BorderLayout());
				
				JPanel inputPanel = new JPanel(new GridLayout(2, 1));
				JPanel messagePanel = new JPanel(new GridLayout(1, 2));
				JLabel command = new JLabel("Please enter the name of this player");
				command.setToolTipText("Must contain at least 1 character");
				messagePanel.add(command);
				error = new JLabel("");
				messagePanel.add(error);
				inputPanel.add(messagePanel);
				input = new JTextField(30);
				inputPanel.add(input);
				add(inputPanel, BorderLayout.CENTER);
				
				JPanel buttonPanel = new JPanel(new FlowLayout());
				JButton confirmButton = new JButton("Confirm");
				confirmButton.addActionListener(new ButtonListener());
				buttonPanel.add(confirmButton);
				JButton clearButton = new JButton("Clear");
				clearButton.addActionListener(new ButtonListener());
				buttonPanel.add(clearButton);
				add(buttonPanel, BorderLayout.SOUTH);
				this.index = index;
				this.maxIndex = maxIndex;
			}
			
			/**
			 * Checks if there are any more names to set, if there aren't moves
			 * onto the next stage of the game.
			 * 
			 * @param index The index of the human.
			 * @param maxIndex The index of the final human.
			 * @since 1.3.0
			 */
			private void nextName(int index, int maxIndex)
			{
				//If there is another human left.
				if(index + 1 < maxIndex)
				{
					SetNameWindow window = new SetNameWindow("Player " + (index + 2) 
							+ " name", index + 1, maxIndex);
					window.setVisible(true);
				}
				//If not build the main screen.
				else
				{
					mainScreen();
				}
			}
		}
		
		/**
		 * Button listener for the SetNameWindow.
		 * 
		 * @author Brodie Robertson
		 * @version 1.2.0
		 * @since 1.2.0
		 */
		private class ButtonListener implements ActionListener
		{	
			/**
			 * Activates when a button attached to this object is pressed.
			 * 
			 * (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 * @since 1.2.0
			 */
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String command = e.getActionCommand();
				int humanPlayers;
				if(command.equals("Confirm"))
				{
					table = new Table();
					roundError.setText("");
					humanError.setText("");
					cpuError.setText("");
					try
					{
						int rounds;
						String input = roundInput.getText();
						//Blank input is treated as a 0.
						if(input.equals(""))
						{
							rounds = 0;
						}
						else
						{
							rounds = Integer.parseInt(input);
						}
						table.setTotalRounds(rounds);
					}
					//Error casting round input to integer.
					catch(NumberFormatException ex)
					{
						resetInputs();
						roundError.setText("Invalid input, please enter a number");
						return;
					} 
					//Error too few rounds.
					catch (TableException ex) 
					{
						resetInputs();
						roundError.setText(ex.getMessage());
						return;
					}
					
					try
					{
						int cpuPlayers;
						String hInput = humanInput.getText();
						String cInput = cpuInput.getText();
						try
						{
							//Blank input is treated as a 0.
							if(hInput.equals(""))
							{
								humanPlayers = 0;
							}
							else
							{
								humanPlayers = Integer.parseInt(humanInput.getText());
							}
						}
						//Error casting human input to integer.
						catch(NumberFormatException ex)
						{
							resetInputs();
							humanError.setText("Invalid input, please enter a number");
							cpuError.setText("");
							return;
						} 
						
						try
						{
							//Blank input treated as 0.
							if(cInput.equals(""))
							{
								cpuPlayers = 0;
							}
							else
							{
								cpuPlayers = Integer.parseInt(cpuInput.getText());
							}
						}
						//Error casting cpu input to integer.
						catch(NumberFormatException ex)
						{
							resetInputs();
							cpuError.setText("Invalid input, please enter a number");
							return;
						} 
						
						table.createPlayers(humanPlayers, cpuPlayers);
					}
					//Error too many/few players
					catch (PlayerException ex) 
					{
						resetInputs();
						humanError.setText(ex.getMessage());
						cpuError.setText(ex.getMessage());
						return;
					} 
					//Error too many/few human players
					catch (HumanException ex) 
					{
						resetInputs();
						humanError.setText(ex.getMessage());
						return;
					} 
					//Error too many cpu players
					catch (CPUException ex) 
					{
						resetInputs();
						cpuError.setText(ex.getMessage());
						return;
					}
					
					dispose();
					
					SetNameWindow window = new SetNameWindow("Player " + 1 
							+ " name", 0, humanPlayers);
					window.setVisible(true);
				}
				//Resets the input and error messages.
				else if(command.equals("Clear"))
				{
					resetInputs();
				}
				else
				{
					System.out.println("Unexpected Error in Game Settings Window");
				}
			}
			
			/**
			 * Resets the all of the inputs and error messages.
			 * 
			 * @since 1.2.0
			 */
			private void resetInputs()
			{
				roundInput.setText("");
				humanInput.setText("");
				cpuInput.setText("");
				roundError.setText("");
				humanError.setText("");
				cpuError.setText("");
			}
		}
		
		/**
		 * Constructs a GameSettingsWindow with a default layout.
		 * 
		 * @since 1.2.0
		 */
		public GameSettingsWindow()
		{
			setTitle("Game Settings");
			setSize(reducedWindow);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			setResizable(false);
			setModalityType(ModalityType.APPLICATION_MODAL);
			setLayout(new BorderLayout());
			
			JPanel inputPanel = new JPanel(new GridLayout(6, 1));
			
			//Round input panel.
			JPanel roundPanel = new JPanel(new GridLayout(1, 2));
			JLabel roundLabel = new JLabel("Please enter the number of rounds");
			roundLabel.setToolTipText("Must be a postive number greater than 0");
			roundPanel.add(roundLabel);
			roundError = new JLabel("");
			roundPanel.add(roundError);
			inputPanel.add(roundPanel);
			roundInput = new JTextField(10);
			inputPanel.add(roundInput);
			
			//Human input panel.
			JPanel humanPanel = new JPanel(new GridLayout(1, 2));
			JLabel humanLabel = new JLabel("Please enter the number of human players");
			humanLabel.setToolTipText("Must be at least 1 and total players less than " 
					+ Table.MAXNUMPLAYERS);
			humanPanel.add(humanLabel);
			humanError = new JLabel("");
			humanPanel.add(humanError);
			inputPanel.add(humanPanel);
			humanInput = new JTextField(10);
			inputPanel.add(humanInput);
			
			//CPU input panel.
			JPanel cpuPanel = new JPanel(new GridLayout(1, 2));
			JLabel cpuLabel = new JLabel("Please enter the number of CPU players");
			cpuLabel.setToolTipText("Must be at least 0 and total players less than " 
					+ Table.MAXNUMPLAYERS);
			cpuPanel.add(cpuLabel);
			cpuError = new JLabel("");
			cpuPanel.add(cpuError);
			inputPanel.add(cpuPanel);
			cpuInput = new JTextField(10);
			inputPanel.add(cpuInput);
			
			//Button Panel.
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 
					REDUCED_BUTTON_HGAP, REDUCED_BUTTON_VGAP));
			
			JButton confirmButton = new JButton("Confirm");
			confirmButton.addActionListener(new ButtonListener());
			buttonPanel.add(confirmButton);
			JButton clearButton = new JButton("Clear");
			clearButton.addActionListener(new ButtonListener());
			buttonPanel.add(clearButton);
			add(inputPanel, BorderLayout.CENTER);
			add(buttonPanel, BorderLayout.SOUTH);
		}
	}
	
	/**
	 * Dialog window displaying general statistics about the creation of the
	 * game. Not yet implemented.
	 * 
	 * @author Brodie Robertson
	 * @version 1.2.0
	 * @since 1.2.0
	 */
	private class AboutWindow extends JDialog
	{
		/**
		 * Constructs a AboutWindow with a default layout.
		 * 
		 * @since 1.2.0
		 */
		public AboutWindow()
		{
			setTitle("About");
			setSize(reducedWindow);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setModalityType(ModalityType.APPLICATION_MODAL);
			setLayout(new BorderLayout());
			add(new JLabel("AVAIBLE IN FUTURE UPDATE"), BorderLayout.CENTER);
		}
	}
	
	/**
	 * Statistics window displaying the statistics of the current game. Not yet
	 * implemented.
	 * 
	 * @author Brodie Robertson
	 * @version 1.2.0
	 * @since 1.2.0
	 */
	private class StatisticsWindow extends JDialog
	{
		/**
		 * Constructs a StatisticsWindow with a default layout.
		 * 
		 * @since 1.2.0
		 */
		public StatisticsWindow()
		{
			setTitle("Game Statistics");
			setSize(reducedWindow);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setModalityType(ModalityType.APPLICATION_MODAL);
			setLayout(new GridLayout(0, 1));
			add(new JLabel("AVAILABLE IN FUTURE UPDATE"));
		}
	}
	
	/**
	 * Used to draw a player's hand onto the display.
	 * 
	 * @author Brodie Robertson
	 * @version 1.4.0
	 * @since 1.2.0
	 */
	private class HandPanel extends JPanel
	{
		/**
		 * An array of labels displaying cards.
		 */
		private JLabel[] cards;
		
		/**
		 * Constructs a HandPanel with a default layout, blank upon creation.
		 * 
		 * @param hand The persom's hand.
		 * @since 1.2.0
		 */
		public HandPanel(Hand hand)
		{
			int row = 2;
			int col = 4;
			setLayout(new GridLayout(row, col));
			cards = new JLabel[row*col];
			for(int i = 0; i < cards.length; i++)
			{
				JLabel card = new JLabel("");
				card.setHorizontalAlignment(JLabel.CENTER);				
				cards[i] = card;
				add(cards[i]);
			}
		}
		
		/**
		 * Updates the HandPanel with the new values of the person's hand.
		 * 
		 * @param index The index of the Person.
		 * @param handIndex The index of the hand.
		 * @since 1.4.0
		 */
		public void updatePanel(int index, int handIndex)
		{
			Hand hand = table.getPersonAtIndex(index).getHand(handIndex);
			for(int i = 0; i < cards.length; i++)
			{
				if(i < hand.getCardsRemaining())
				{
					cards[i].setText("" + hand.getCard(i).getValue());
				}
			}
		}
	}
	
	/**
	 * Used to draw a players statistics on the screen, including their hand.
	 * 
	 * @author Brodie Robertson
	 * @version 1.4.0
	 * @since 1.2.0
	 */
	private class PlayerPanel extends JPanel
	{
		/**
		 * Label displaying the Player's total money.
		 */
		private JLabel totalMoney;
		/**
		 * Label displaying the Player's wager.
		 */
		private JLabel wager;
		/**
		 * Array labels displaying hand scores.
		 */
		private JLabel[] scores;
		/**
		 * Array of labels displaying hand labels.
		 */
		private JLabel[] scoreLabels;
		/**
		 * Label displaying an extra stat label.
		 */
		private JLabel extraLabel;
		/**
		 * Label displaying an extra stat.
		 */
		private JLabel extra;
		/**
		 * Used to display the player's hand onto the screen.
		 */
		private HandPanel hand;
		
		/**
		 * Constructs a PlayerPanel with a default layout.
		 * 
		 * @param player The player.
		 * @since 1.2.0
		 */
		public PlayerPanel(Player player)
		{
			setLayout(new GridLayout(2, 1));
			
			//Player Statistics
			JPanel statisticsPanel = new JPanel(new GridLayout(3, 2));
			
			//Name
			JPanel namePanel = new JPanel(new GridLayout(2, 1));
			JLabel nameLabel = new JLabel("Name");
			nameLabel.setHorizontalAlignment(JLabel.CENTER);
			namePanel.add(nameLabel);
			JLabel name = new JLabel(player.getName());
			name.setHorizontalAlignment(JLabel.CENTER);
			namePanel.add(name);
			statisticsPanel.add(namePanel);
			
			//Total Money
			JPanel totalMoneyPanel = new JPanel(new GridLayout(2, 1));
			JLabel totalMoneyLabel = new JLabel("Total Money");
			totalMoneyLabel.setHorizontalAlignment(JLabel.CENTER);
			totalMoneyPanel.add(totalMoneyLabel);
			totalMoney = new JLabel("$" + player.getTotalMoney());
			totalMoney.setHorizontalAlignment(JLabel.CENTER);
			totalMoneyPanel.add(totalMoney);
			statisticsPanel.add(totalMoneyPanel);
			
			//Wager
			JPanel wagerPanel = new JPanel(new GridLayout(2, 1));
			JLabel wagerLabel = new JLabel("Wager");
			wagerLabel.setHorizontalAlignment(JLabel.CENTER);
			wagerPanel.add(wagerLabel);
			wager = new JLabel("$" + player.getWager());
			wager.setHorizontalAlignment(JLabel.CENTER);
			wagerPanel.add(wager);
			statisticsPanel.add(wagerPanel);
			
			//First Hand Score
			JPanel[] scorePanels = new JPanel[2];
			scoreLabels = new JLabel[2];
			scores = new JLabel[2];
			scorePanels[0] = new JPanel(new GridLayout(2, 1));
			scoreLabels[0] = new JLabel("Hand Score");
			scoreLabels[0].setHorizontalAlignment(JLabel.CENTER);
			scorePanels[0].add(scoreLabels[0]);
			scores[0] = new JLabel("" + player.getHand(0).getHandScore());
			scores[0].setHorizontalAlignment(JLabel.CENTER);
			scorePanels[0].add(scores[0]);
			statisticsPanel.add(scorePanels[0]);
			
			//Extra
			JPanel extraPanel = new JPanel(new GridLayout(2, 1));
			extraLabel = new JLabel("");
			extraLabel.setHorizontalAlignment(JLabel.CENTER);
			extraPanel.add(extraLabel);
			extra = new JLabel("");
			extra.setHorizontalAlignment(JLabel.CENTER);
			extraPanel.add(extra);
			statisticsPanel.add(extraPanel);
			
			//Second Hand Score
			scorePanels[1] = new JPanel(new GridLayout(2, 1));
			scoreLabels[1] = new JLabel("");
			scoreLabels[1].setHorizontalAlignment(JLabel.CENTER);
			scorePanels[1].add(scoreLabels[1]);
			scores[1] = new JLabel("");
			scores[1].setHorizontalAlignment(JLabel.CENTER);
			scorePanels[1].add(scores[1]);
			statisticsPanel.add(scorePanels[1]);
			add(statisticsPanel);
			
			//Hand Panel
			JPanel fullHandPanel = new JPanel(new GridLayout(0, 1));
			hand = new HandPanel(player.getHand(0));
			fullHandPanel.add(hand);
			JScrollPane handScrollPanel = new JScrollPane(fullHandPanel);
			handScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.
					HORIZONTAL_SCROLLBAR_NEVER);
			handScrollPanel.setVerticalScrollBarPolicy(JScrollPane.
					VERTICAL_SCROLLBAR_AS_NEEDED);
			add(handScrollPanel);
		}
		
		/**
		 * Updates the PlayerPanel with the new values of the Player's hand.
		 * 
		 * @param index The index of the Player.
		 * @since 1.2.0
		 */
		public void updatePanel(int index)
		{
			Player player = (Player)table.getPersonAtIndex(index);
			totalMoney.setText("$" + player.getTotalMoney());
			wager.setText("$" + player.getWager());
			scores[0].setText("" + player.getHand(0).getHandScore());
			if(player.getHand(0).getSplit())
			{
				scoreLabels[0].setText("Hand 1 Score");
				scoreLabels[1].setText("Hand 2 Score");
				scores[1].setText("" + player.getHand(1).getHandScore());
			}
			
			if(player.getTookInsurance())
			{
				extraLabel.setText("Insurance");
				extra.setText("$" + player.getInsurance());
			}
			else
			{
				extraLabel.setText("");
				extra.setText("");
			}
			
			hand.updatePanel(index, 0);
		}
	}
	
	/**
	 * Used to draw the dealer's statistics onto the display.
	 * 
	 * @author Brodie Robertson
	 * @version 1.4.0
	 * @since 1.2.0
	 */
	private class DealerPanel extends JPanel
	{
		/**
		 * Label displaying the score of the dealers hand.
		 */
		private JLabel score;
		/**
		 * Used to display the dealer's hand.
		 */
		private HandPanel hand;
		
		/**
		 * Constructs a dealer panel with a default layout.
		 * 
		 * @param dealer The dealer.
		 * @since 1.2.0
		 */
		public DealerPanel(Dealer dealer)
		{
			setLayout(new GridLayout(2, 1));
			
			//Player Statistics
			JPanel statisticsPanel = new JPanel(new GridLayout(3, 1));
			
			//Name
			JPanel namePanel = new JPanel(new GridLayout(2, 1));
			JLabel nameLabel = new JLabel("Name");
			nameLabel.setHorizontalAlignment(JLabel.CENTER);
			namePanel.add(nameLabel);
			JLabel name = new JLabel(dealer.getName());
			name.setHorizontalAlignment(JLabel.CENTER);
			namePanel.add(name);
			statisticsPanel.add(namePanel);
			add(statisticsPanel);
			
			//Hand Score
			JPanel scorePanel = new JPanel(new GridLayout(2, 1));
			JLabel scoreLabel = new JLabel("Hand Score");
			scoreLabel.setHorizontalAlignment(JLabel.CENTER);
			scorePanel.add(scoreLabel);
			score = new JLabel("" + dealer.getHand(0).getHandScore());
			score.setHorizontalAlignment(JLabel.CENTER);
			scorePanel.add(score);
			statisticsPanel.add(scorePanel);
			statisticsPanel.add(new JLabel(""));
			
			//Hand Panel
			JPanel fullHandPanel = new JPanel(new GridLayout(0, 1));
			hand = new HandPanel(dealer.getHand(0));
			fullHandPanel.add(hand);
			JScrollPane handScrollPanel = new JScrollPane(fullHandPanel);
			handScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.
					HORIZONTAL_SCROLLBAR_NEVER);
			handScrollPanel.setVerticalScrollBarPolicy(JScrollPane.
					VERTICAL_SCROLLBAR_AS_NEEDED);
			add(handScrollPanel);
		}
		
		/**
		 * Updates the DealerPanel with the dealer's new statistics.
		 * 
		 * @param index The index of the dealer.
		 * @since 1.2.0
		 */
		public void updatePanel(int index)
		{
			Person player = table.getPersonAtIndex(index);
			score.setText("" + player.getHand(0).getHandScore());
			hand.updatePanel(index, 0);
		}
	}
	
	/**
	 * Dialog window used for setting a human's wager.
	 * 
	 * @author Brodie Robertson
	 * @version 1.3.0
	 * @since 1.2.0
	 */
	private class WagerWindow extends JDialog
	{
		/**
		 * Label displaying an error with the input.
		 */
		private JLabel error;
		/**
		 * Text field for human's wager.
		 */
		private JTextField input;
		/**
		 * Index of the human
		 */
		private int index;
		
		/**
		 * Button listener for the WagerWindow.
		 * 
		 * @author Brodie Robertson
		 * @version 1.2.0
		 * @since 1.2.0
		 */
		private class ButtonListener implements ActionListener
		{
			/**
			 * Activates when a button attached to this object is pressed.
			 * 
			 * (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 * @since 1.2.0
			 */
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String command = e.getActionCommand();
				
				if(command.equals("Confirm"))
				{
					double wager;
					try
					{
						wager = Integer.parseInt(input.getText());
						table.setHumanWager(index, wager);
					}
					//Error casting input to integer
					catch(NumberFormatException ex)
					{
						input.setText("");
						error.setText("Invalid input, please enter a valid number");
						return;
					}
					//Invalid wager
					catch(TableException | PlayerException ex)
					{
						input.setText("");
						error.setText(ex.getMessage());
						return;
					}
					gameLog.setText(gameLog.getText() + table.getPersonAtIndex(index).
							getName() + "'s wager is $" + wager + "\n");
					playerPanels[index].updatePanel(index);
					dispose();
					nextWager(index);
					return;
				}
				//Resets the input and error message
				else if(command.equals("Clear"))
				{
					input.setText("");
					error.setText("");
				}
				else
				{
					System.out.println("Unexpected Error on Wager Window");
				}
			}
		}
		
		/**
		 * Constructs a WagerWindow with a default layout.
		 * 
		 * @param index The index of a human.
		 * @since 1.2.0
		 */
		public WagerWindow(int index)
		{
			String name = table.getPersonAtIndex(index).getName();
			setTitle(name + "'s Wager");
			setSize(tinyWindow);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			setResizable(false);
			setModalityType(ModalityType.MODELESS);
			setLayout(new BorderLayout());
			
			//Request for input.
			JPanel inputPanel = new JPanel(new GridLayout(2, 1));
			JPanel messagePanel = new JPanel(new GridLayout(1, 2));
			JLabel command = new JLabel("Please enter " + name + "'s  wager");
			command.setToolTipText("Wager must be between" + Table.MINWAGER + " and " + Table.MAXWAGER);
			messagePanel.add(command);
			error = new JLabel("");
			messagePanel.add(error);
			inputPanel.add(messagePanel);
			input = new JTextField(30);
			inputPanel.add(input);
			add(inputPanel, BorderLayout.CENTER);
			
			//Button Panel.
			JPanel buttonPanel = new JPanel(new FlowLayout());
			JButton confirmButton = new JButton("Confirm");
			confirmButton.addActionListener(new ButtonListener());
			buttonPanel.add(confirmButton);
			JButton clearButton = new JButton("Clear");
			clearButton.addActionListener(new ButtonListener());
			buttonPanel.add(clearButton);
			add(buttonPanel, BorderLayout.SOUTH);
			this.index = index;
		}
	}
	
	/**
	 * Dialog window used for setting a human's insurance.
	 * 
	 * @author Brodie Robertson
	 * @version 1.4.0
	 * @since 1.2.0
	 */
	private class InsuranceWindow extends JDialog
	{
		/**
		 * Index of the human.
		 */
		private int index;
		/**
		 * Label displaying an error with the input.
		 */
		private JLabel error;
		
		/**
		 * Button listener for the InsuranceWindow.
		 * 
		 * @author Brodie Robertson
		 * @version 1.2.0
		 * @since 1.2.0
		 */
		private class ButtonListener implements ActionListener
		{
			/**
			 * Text field for the human's insurance.
			 */
			private JTextField input;
			
			/**
			 * Activates when a button attached to this object is pressed.
			 * 
			 * (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 * @since 1.2.0
			 */
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Player player = (Player)table.getPersonAtIndex(index);
				String cmd = e.getActionCommand();
				
				if(cmd.equals("Yes"))
				{
					if(player.getTotalMoney() == 0)
					{
						error.setText(player.getName() + " has no money remaining");
						return;
					}
					else
					{
						getContentPane().removeAll();;
						setLayout(new BorderLayout());
						
						//Request for input.
						JPanel inputPanel = new JPanel(new GridLayout(2, 1));
						JPanel messagePanel = new JPanel(new GridLayout(1, 2));
						JLabel command = new JLabel("How much would you like?");
						messagePanel.add(command);
						error = new JLabel("");
						messagePanel.add(error);
						inputPanel.add(messagePanel);
						input = new JTextField(30);
						inputPanel.add(input);
						add(inputPanel, BorderLayout.CENTER);
						
						//Button panel.
						JPanel buttonPanel = new JPanel(new FlowLayout());
						JButton confirmButton = new JButton("Confirm");
						confirmButton.addActionListener(this);
						buttonPanel.add(confirmButton);
						JButton clearButton = new JButton("Clear");
						clearButton.addActionListener(this);
						buttonPanel.add(clearButton);
						add(buttonPanel, BorderLayout.SOUTH);
						
						getContentPane().revalidate();
						repaint();
						gameLog.setText(gameLog.getText() + player.getName() + " takes insurance\n");
					}
				}
				//Closes the insurance window.
				else if(cmd.equals("No"))
				{
					gameLog.setText(gameLog.getText() + player.getName() + " doesn't take insurance\n");
					dispose();
					nextInsurance(index);
				}
				//Confirm the input.
				else if(cmd.equals("Confirm"))
				{
					double insurance = 0;
					try
					{
						insurance = Integer.parseInt(input.getText());
						table.setHumanInsurance(index, insurance);
					}
					//Error casting input to integer.
					catch(NumberFormatException ex)
					{
						input.setText("");
						error.setText("Invalid input, please enter a valid number");
						return;
					}
					//Invalid insurance.
					catch(PlayerException | TableException ex)
					{
						input.setText("");
						error.setText(ex.getMessage());
						return;
					}
					gameLog.setText(gameLog.getText() + player.getName() + " has $" + 
							insurance + " of insurance\n");
					dispose();
					nextInsurance(index);
				}
				//Resets the input and error message.
				else if(cmd.equals("Clear"))
				{
					error.setText("");
					input.setText("");
				}
				else
				{
					System.out.println("Unexpected Error on Insurance Window");
				}
			}
		}
		
		/**
		 * Constructs a InsuranceWindow with a default layout.
		 * 
		 * @param index The index of the human.
		 * @since 1.2.0
		 */
		public InsuranceWindow(int index)
		{
			String name = table.getPersonAtIndex(index).getName();
			setTitle(name + "'s Insurance");
			setSize(tinyWindow);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			setResizable(false);
			setModalityType(ModalityType.MODELESS);
			setLayout(new BorderLayout());
			
			//Request for input.
			JPanel messagePanel = new JPanel(new GridLayout(1, 2));
			JLabel command = new JLabel("Would you like insurance?");
			messagePanel.add(command);
			error = new JLabel("");
			messagePanel.add(error);
			add(messagePanel, BorderLayout.CENTER);
			
			//Button panel.
			JPanel buttonPanel = new JPanel(new FlowLayout());
			JButton yesButton = new JButton("Yes");
			yesButton.addActionListener(new ButtonListener());
			buttonPanel.add(yesButton);
			JButton noButton = new JButton("No");
			noButton.addActionListener(new ButtonListener());
			buttonPanel.add(noButton);
			add(buttonPanel, BorderLayout.SOUTH);
			this.index = index;
		}
	}
	
	/**
	 * Dialog window used for playing out a human's turn.
	 * 
	 * @author Brodie Robertson
	 * @version 1.4.0
	 * @since 1.2.0
	 */
	private class TurnWindow extends JDialog
	{
		/**
		 * Label requesting for input.
		 */
		private JLabel command;
		/**
		 * Label used for displaying an error message.
		 */
		private JLabel error;
		/**
		 * Panel containing the buttons in the window.
		 */
		private JPanel buttonPanel;
		/**
		 * Index of the human.
		 */
		private int index;
		
		/**
		 * Main button listener for the TurnWindow.
		 * 
		 * @author Brodie Robertson
		 * @version 1.4.0
		 * @since 1.2.0
		 */
		private class ButtonListener implements ActionListener
		{
			/**
			 * Activates when a button attached to this object is pressed.
			 * 
			 * (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 * @since 1.2.0
			 */
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String cmd = e.getActionCommand();
				Player player = (Player)table.getPersonAtIndex(0);
				
				//Ends the player's turn.
				if(cmd.equals("Stand"))
				{
					gameLog.setText(gameLog.getText() + player.getName() 
						+ " stands with a score of " + player.getHand(0).
						getHandScore() + "\n");
					dispose();
					nextPlayer(index);
				}
				//Deals the human a new card and rebuilds the window.
				else if(cmd.equals("Hit"))
				{
					error.setText("");
					Card card = table.hit(index, 0);
					player = (Player)table.getPersonAtIndex(index);
					gameLog.setText(gameLog.getText() + player.getName() 
						+ " hits and is dealt a " + card + "\n");
					playerPanels[index].updatePanel(index);
					
					int handScore = player.getHand(0).getHandScore();
					
					//If hand score is less than Blackjack.
					if(handScore < Table.BLACKJACK)
					{
						buttonPanel.removeAll();
						buttonPanel.setLayout(new FlowLayout());
						JButton standButton = new JButton("Stand");
						standButton.addActionListener(this);
						buttonPanel.add(standButton);
						JButton hitButton = new JButton("Hit");
						hitButton.addActionListener(this);
						buttonPanel.add(hitButton);
						buttonPanel.revalidate();
						buttonPanel.repaint();
					}
					//If hand score is equal to Blackjack.
					else if(handScore == Table.BLACKJACK)
					{
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " has Blackjack and is forced to stand\n");
						dispose();
						nextPlayer(index);
					}
					//If hand score greater than Blackjack.
					else if(player.getBusted())
					{
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " busts\n");
						dispose();
						nextPlayer(index);
					}
				}
				//Doubles the humans wager and deals a new card.
				else if(cmd.equals("Double Down"))
				{
					error.setText("");
					//Doubles the wager and deals a new card if the human can 
					//afford to.
					if(player.getTotalMoney() >= player.getWager())
					{
						Card card = table.doubleDown(index, 0);
						player = (Player)table.getPersonAtIndex(index);
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " doubles down and their wager has increased to " 
								+ player.getWager() + "\n");
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " is dealt a " + card + "\n");
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " now has a score of " + player.getHand(0).getHandScore() + "\n");
						
						//If hand score greater than Blackjack.
						if(player.getBusted())
						{
							gameLog.setText(gameLog.getText() 
									+ player.getName() + " has gone bust");
						}
						playerPanels[index].updatePanel(index);
						dispose();
						nextPlayer(index);
					}
					//Error if human can't afford to double down.
					else
					{
						error.setText("You don't have enough money to double down");
						return;
					}
				}
				//Splits the humans hand and double wager if the 2 cards in the
				//humans hand match and rebuilds the window.
				else if(cmd.equals("Split"))
				{
					error.setText("");
					//If the human meet the requirements for a split.
					if(player.getTotalMoney() >= player.getWager() && (player.
					   getHand(0).getCard(0).getValue() == player.getHand(0).
					   getCard(1).getValue() || (player.getHand(0).getCard(0).
					   getFace() == Face.ACE && player.getHand(0).getCard(1).
					   getFace() == Face.ACE)))
					{
						error.setText("");
						table.split(index);
						player = (Player)table.getPersonAtIndex(index);
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " splits their hand and their wager increase to " 
							+ player.getWager() + "\n");
						playerPanels[index].updatePanel(index);
						
						int handIndex = 0;
						buttonPanel.removeAll();
						command.setText("What action would you like to take with hand " 
								+ (handIndex + 1) + "?");
						JButton standButton = new JButton("Stand");
						standButton.addActionListener(new SplitButtonListener(handIndex));
						buttonPanel.add(standButton);
						JButton hitButton = new JButton("Hit");
						hitButton.addActionListener(new SplitButtonListener(handIndex));
						buttonPanel.add(hitButton);
						JButton doubleDownButton = new JButton("Double Down");
						doubleDownButton.addActionListener(new SplitButtonListener(handIndex));
						buttonPanel.add(doubleDownButton);
						buttonPanel.repaint();
						buttonPanel.revalidate();
					}
					//Error if the human can't afford to split.
					else if(player.getTotalMoney() < player.getWager())
					{
						error.setText("You don't have enough money to split");
						return;
					}
					//Error if the humans card do not match.
					else if(player.getHand(0).getCard(0).getValue() != player.
							getHand(0).getCard(0).getValue() || (player.getHand(0).
							getCard(0).getFace() != Face.ACE && player.getHand(0).
							getCard(1).getFace() != Face.ACE))
					{
						error.setText("The cards in your hand don't match");
						return;
					}
				}
				//The human loses the round and half their wager is returned.
				else if(cmd.equals("Surrender"))
				{
					double returnedAmount = table.surrender(index);
					gameLog.setText(gameLog.getText() + player.getName()
						+ " surrenders and half of their wager is returned\n");
					gameLog.setText(gameLog.getText() + player.getName() 
						+ " regains $" + returnedAmount + "\n");
					playerPanels[index].updatePanel(index);
					dispose();
					nextPlayer(index);
				}
				else
				{
					System.out.println("Unexpected Error on Turn Window");
				}
			}
		}
		
		/**
		 * Button listener for use with the TurnWindow after a hand has been 
		 * split.
		 * 
		 * @author Brodie Robertson
		 * @version 1.4.0
		 * @since 1.2.0
		 */
		private class SplitButtonListener implements ActionListener
		{
			private int handIndex;
			Player player = (Player)table.getPersonAtIndex(index);
			
			/**
			 * Constructs a SplitButtonLister with a hand index.
			 * 
			 * @param handIndex
			 */
			public SplitButtonListener(int handIndex)
			{
				this.handIndex = handIndex;
			}

			/**
			 * Activates when a button attached to this object is pressed.
			 * 
			 * (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 * @since 1.2.0
			 */
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String cmd = e.getActionCommand();
				
				//Ends the humans turn.
				if(cmd.equals("Stand"))
				{
					gameLog.setText(gameLog.getText() + "Stands hand " 
							+ (handIndex + 1) + " with a score of " + player.
							getHand(0).getHandScore() + "\n");
					nextHand(handIndex);
				}
				//Deals the human another card and rebuilds the window.
				else if(cmd.equals("Hit"))
				{
					error.setText("");
					Card card = table.hit(index, handIndex);
					player = (Player)table.getPersonAtIndex(index);
					gameLog.setText(gameLog.getText() + player.getName() 
						+ " hits and is dealt a " + card + " to hand " + 
						(handIndex + 1) + "\n");
					playerPanels[index].updatePanel(index);
					
					int handScore = player.getHand(handIndex).getHandScore();
					//Rebuilds the window if the hand's score is less than
					//Blackjack.
					if(handScore < Table.BLACKJACK)
					{
						buttonPanel.removeAll();
						buttonPanel.setLayout(new FlowLayout());
						JButton standButton = new JButton("Stand");
						standButton.addActionListener(this);
						buttonPanel.add(standButton);
						JButton hitButton = new JButton("Hit");
						hitButton.addActionListener(this);
						buttonPanel.add(hitButton);
						buttonPanel.revalidate();
						buttonPanel.repaint();
					}
					//If the hand's score is equal to Blackjack
					else if(handScore == Table.BLACKJACK)
					{
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " has Blackjack and is forced to stand\n");
						nextHand(handIndex);
					}
					//If the hand's score is greater than Blackjack.
					else if(player.getBusted())
					{
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " busts\n");
						dispose();
						nextPlayer(index);
					}
				}
				//Deals the human another card and doubles the humans wager if 
				//they can afford it.
				else if(cmd.equals("Double Down"))
				{
					error.setText("");
					//If the human can afford to double down.
					if(player.getTotalMoney() >= player.getWager())
					{
						Card card = table.doubleDown(index, handIndex);
						player = (Player)table.getPersonAtIndex(index);
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " doubles down hand " + (handIndex + 1) + " and their "
							+ "wager has increased to " + player.getWager() + "\n");
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " is dealt a " + card + " to hand " + (handIndex + 1) + "\n");
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " now has a score of " + player.getHand(handIndex).
							getHandScore() + "\n");
						
						playerPanels[index].updatePanel(index);
						//If the player's hand score is above Blackjack
						if(player.getBusted())
						{
							gameLog.setText(gameLog.getText() 
									+ player.getName() + " busts\n");
							dispose();
							nextPlayer(handIndex);
						}
						//If not go to the next hand.
						else
						{
							nextHand(handIndex);
						}
					}
					//Error if the human can't afford to double down.
					else
					{
						error.setText("You don't have enough money to double down");
						return;
					}
				}
				else
				{
					System.out.println("Unexpected error in Split Turn Window");
				}
			}
			
			/**
			 * Checks if there are any hands remaining, if not move onto the 
			 * next stage of the game.
			 * 
			 * @param handIndex The index of the hand.
			 * @since 1.3.0
			 */
			private void nextHand(int handIndex)
			{
				Hand[] hands = player.getHands();
				
				//If there is a hand remaining, rebuild the window.
				if(handIndex + 1 < hands.length)
				{
					int newHandIndex = handIndex + 1;
					command.setText("What action would you like to take with hand " 
							+ (newHandIndex + 1) + "?");
					buttonPanel.removeAll();
					JButton standButton = new JButton("Stand");
					standButton.addActionListener(new SplitButtonListener(newHandIndex));
					buttonPanel.add(standButton);
					JButton hitButton = new JButton("Hit");
					hitButton.addActionListener(new SplitButtonListener(newHandIndex));
					buttonPanel.add(hitButton);
					JButton doubleDownButton = new JButton("Double Down");
					doubleDownButton.addActionListener(new SplitButtonListener(newHandIndex));
					buttonPanel.add(doubleDownButton);
				}
				//If not go to the next player.
				else
				{
					dispose();
					nextPlayer(index);
				}
			}
		}
		
		/**
		 * Constructs the TurnWindow with a default layout.
		 * 
		 * @param index The index of the human.
		 * @since 1.2.0
		 */
		public TurnWindow(int index)
		{
			String name = table.getPersonAtIndex(index).getName();
			setTitle(name + "'s Turn");
			setSize(tinyWindow);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			setResizable(false);
			setModalityType(ModalityType.MODELESS);
			setLayout(new BorderLayout());
			
			//Request for action.
			JPanel messagePanel = new JPanel(new GridLayout(1, 2));
			command = new JLabel("What action would you like to take?");
			messagePanel.add(command);
			error = new JLabel("");
			messagePanel.add(error);
			add(messagePanel, BorderLayout.CENTER);
			
			
			//Button panel.
			buttonPanel = new JPanel(new FlowLayout());
			JButton standButton = new JButton("Stand");
			standButton.addActionListener(new ButtonListener());
			buttonPanel.add(standButton);
			JButton hitButton = new JButton("Hit");
			hitButton.addActionListener(new ButtonListener());
			buttonPanel.add(hitButton);
			JButton doubleDownButton = new JButton("Double Down");
			doubleDownButton.addActionListener(new ButtonListener());
			buttonPanel.add(doubleDownButton);
			JButton splitButton = new JButton("Split");
			splitButton.addActionListener(new ButtonListener());
			buttonPanel.add(splitButton);
			JButton surrenderButton = new JButton("Surrender");
			surrenderButton.addActionListener(new ButtonListener());
			buttonPanel.add(surrenderButton);
			add(buttonPanel, BorderLayout.SOUTH);
			
			this.index = index;
		}
	}
	
	/**
	 * Dialog window used for resetting the game after all rounds are 
	 * completed.
	 * 
	 * @author Brodie Robertson
	 * @version 1.4.0
	 * @since 1.2.0
	 */
	private class EndGameWindow extends JDialog
	{
		/**
		 * Button listener for the EndGameWindow.
		 * 
		 * @author Brodie Robertson
		 * @version 1.4.0
		 * @since 1.4.0
		 */
		private class ButtonListener implements ActionListener
		{
			/**
			 * Activates when a button attached to this object is pressed.
			 *  
			 * (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 * @since 1.2.0
			 */
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String cmd = e.getActionCommand();
				
				if(cmd.equals("Yes"))
				{
					dispose();
					GameSettingsWindow window = new GameSettingsWindow();
					window.setVisible(true);
				}
				else if(cmd.equals("No"))
				{
					CloseWindow window = new CloseWindow();
					window.setVisible(true);
				}
				else if(cmd.equals("Menu"))
				{
					dispose();
					titleScreen();
				}
				else if(cmd.equals("Statistics"))
				{
					StatisticsWindow window = new StatisticsWindow();
					window.setVisible(true);
				}
				else if(cmd.equals("About"))
				{
					AboutWindow window = new AboutWindow();
					window.setVisible(true);
				}
				else
				{
					System.out.println("Unexpected error in End Game Window");
				}
			}
		}
		
		/**
		 * Constructs an EndGameWindow with a default layout.
		 * 
		 * @since 1.4.0
		 */
		public EndGameWindow()
		{
			setTitle("End Game");
			setSize(tinyWindow);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			setResizable(false);
			setModalityType(ModalityType.APPLICATION_MODAL);
			setLayout(new BorderLayout());
			
			//Request for input.
			JPanel messagePanel = new JPanel(new FlowLayout());
			JLabel command = new JLabel("Would you like to play again?");
			messagePanel.add(command);
			add(messagePanel, BorderLayout.CENTER);
			
			//Button panel.
			JPanel buttonPanel = new JPanel(new FlowLayout());
			JButton yesButton = new JButton("Yes");
			yesButton.addActionListener(new ButtonListener());
			buttonPanel.add(yesButton);
			JButton noButton = new JButton("No");
			noButton.addActionListener(new ButtonListener());
			buttonPanel.add(noButton);
			JButton menuButton = new JButton("Return to Menu");
			menuButton.setActionCommand("Menu");
			menuButton.addActionListener(new ButtonListener());
			buttonPanel.add(menuButton);
			JButton statisticsButton = new JButton("Statistics");
			statisticsButton.addActionListener(new ButtonListener());
			buttonPanel.add(statisticsButton);
			JButton aboutButton = new JButton("About");
			aboutButton.addActionListener(new ButtonListener());
			buttonPanel.add(aboutButton);
			
			add(buttonPanel, BorderLayout.SOUTH);
		}
	}
	
	/**
	 * Constructs a GUI and builds the title screen.
	 * 
	 * @since 1.2.0
	 */
	public GUI()
	{
		super("Blackjack - By Brodie Robertson");
		titleScreen();
	}
	
	/**
	 * Rebuilds the window into the title screen.
	 * 
	 * @since 1.4.0
	 */
	private void titleScreen()
	{
		getContentPane().removeAll();
		setSize(WIDTH, HEIGHT);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		addWindowListener(new CheckOnExit());
		setJMenuBar(null);
		
		//Background colour
		Color orange = new Color(243, 101, 37);
		getContentPane().setBackground(orange);
		
		//Title
		JLabel title = new JLabel("Blackjack");
		Font titleFont = new Font("Times New Roman", Font.BOLD, 200);
		title.setFont(titleFont);
		title.setHorizontalAlignment(JLabel.CENTER);
		add(title, BorderLayout.CENTER);
		
		//Button panel
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 
				TITLE_BUTTON_HGAP, TITLE_BUTTON_VGAP));
		buttonPanel.setBackground(Color.DARK_GRAY);
		buttonPanel.setPreferredSize(new Dimension(WIDTH, TITLE_BUTTON_PANEL_HEIGHT));
		JButton playButton = new JButton("Start Game");
		playButton.addActionListener(new ButtonListener());
		playButton.setPreferredSize(buttonSize);
		playButton.setFont(buttonFont);
		buttonPanel.add(playButton);
		JButton quitButton = new JButton("Quit Game");
		quitButton.addActionListener(new ButtonListener());
		quitButton.setPreferredSize(buttonSize);
		quitButton.setFont(buttonFont);
		buttonPanel.add(quitButton);
		JButton aboutButton = new JButton("About");
		aboutButton.addActionListener(new ButtonListener());
		aboutButton.setPreferredSize(buttonSize);
		aboutButton.setFont(buttonFont);
		buttonPanel.add(aboutButton);
		
		add(buttonPanel, BorderLayout.SOUTH);
		getContentPane().revalidate();
		repaint();
	}
	
	/**
	 * Builds the main game screen.
	 * 
	 * @since 1.2.0
	 */
	private void mainScreen()
	{
		getContentPane().removeAll();
		setLayout(new BorderLayout());
		Color lightGray = new Color(249, 249, 250);
		getContentPane().setBackground(lightGray);
		
		//Menu Bar
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		JMenuItem aboutButton = new JMenuItem("About");
		aboutButton.addActionListener(new ButtonListener());
		menu.add(aboutButton);
		JMenuItem statsButton = new JMenuItem("Statistics");
		statsButton.addActionListener(new ButtonListener());
		menu.add(statsButton);
		JMenuItem quitButton = new JMenuItem("Quit Game");
		quitButton.addActionListener(new ButtonListener());
		menu.add(quitButton);
		menuBar.add(menu);
		setJMenuBar(menuBar);
		
		//Center Panel
		JPanel centerPanel = new JPanel(new GridLayout(2, 1));
		JPanel topCenterPanel = new JPanel(new GridLayout(1, 2));
		
		//Dealer Panel
		JPanel topQuarterPanel = new JPanel(new GridLayout(1, 3));
		dealerPanel = new DealerPanel((Dealer)table.getPersonAtIndex
				(table.getPlayers().length - 1));
		topQuarterPanel.add(dealerPanel);
		JLabel deck = new JLabel("Deck");
		deck.setHorizontalAlignment(JLabel.CENTER);
		topQuarterPanel.add(deck);
		topQuarterPanel.add(new JLabel(""));
		topCenterPanel.add(topQuarterPanel);
		centerPanel.add(topCenterPanel);
		
		//Log Panel
		JPanel logPanel = new JPanel(new BorderLayout());
		JLabel gameLogHeader = new JLabel("Game Log");
		logPanel.add(gameLogHeader, BorderLayout.NORTH);
		gameLog = new JTextArea();
		gameLog.setLineWrap(true);
		gameLog.setEditable(false);
		JScrollPane scrolledGameLog = new JScrollPane(gameLog);
		scrolledGameLog.setHorizontalScrollBarPolicy(JScrollPane.
				HORIZONTAL_SCROLLBAR_NEVER);
		scrolledGameLog.setVerticalScrollBarPolicy(JScrollPane.
				VERTICAL_SCROLLBAR_AS_NEEDED);
		logPanel.add(scrolledGameLog, BorderLayout.CENTER);
		topCenterPanel.add(logPanel);
		
		//Player panels
		JPanel bottomCenterPanel = new JPanel(new GridLayout(1, Table.MAXNUMPLAYERS));
		playerPanels = new PlayerPanel[table.getPlayers().length - 1];
		for(int i = 0; i < Table.MAXNUMPLAYERS; i++)
		{
			//If there are player's left create a new player panel
			if(i < playerPanels.length)
			{
				Player player = (Player)table.getPersonAtIndex(i);
				playerPanels[i] = new PlayerPanel(player);
				bottomCenterPanel.add(playerPanels[i]);
			}
			//If not fill the space with a blank label.
			else
			{
				bottomCenterPanel.add(new JLabel(""));
			}
		}
		centerPanel.add(bottomCenterPanel);
		add(centerPanel, BorderLayout.CENTER);
		
		getContentPane().revalidate();
		repaint();
		currentRound();
		gameLog.setText(gameLog.getText() + "Wagers:\n");
		intialWager(0);
	}
	
	/**
	 * Adds the current round to the game log.
	 * 
	 * @since 1.3.0
	 */
	private void currentRound()
	{
		gameLog.setText(gameLog.getText() + "Round " + table.getCurrentRound() 
		+ " -------------------------------------------------------------------"
		+ "----------------------------------\n");
	}
	
	/**
	 * Sets the wager of a player at a specified index.
	 * 
	 * @param index The index of the player.
	 * @since 1.2.0
	 */
	private void intialWager(int index)
	{
		Player player = (Player)table.getPersonAtIndex(index);
		//If the player isn't bankrupt.
		if(!player.getBankrupt())
		{
			//If the player is Human open a WagerWindow.
			if(player instanceof Human)
			{
				WagerWindow window = new WagerWindow(index);
				window.setVisible(true);		
			}
			//If the player is a CPU the table handles the wager.
			else if(player instanceof CPU)
			{
				double wager = table.setCPUWager(index);
				gameLog.setText(gameLog.getText() + player.getName() + 
						"'s wager is $" + wager + "\n");
				playerPanels[index].updatePanel(index);
				
				nextWager(index);
			}
		}
		//If the player is go to the next player.
		else
		{
			nextWager(index);
		}
	}
	
	/**
	 * Checks if there are any more wagers to set, if not goes to the next 
	 * stage of the game.
	 * 
	 * @param index The index of the Player.
	 * @since 1.2.0
	 */
	private void nextWager(int index)
	{
		//If there are any more players left.
		if(index + 1 < table.getPlayers().length - 1)
		{
			intialWager(index + 1);
		}
		//If not goes to the next stage of the game.
		else
		{
			gameLog.setText(gameLog.getText() + "\nDealing Cards to Players:\n");
			initialDeal();
		}
	}
	
	/**
	 * Deals 2 cards to each person in the game.
	 * 
	 * @since 1.2.0
	 */
	private void initialDeal()
	{
		table.deal();
		Person[] players = table.getPlayers();
		gameLog.setText(gameLog.getText() + "All player's are dealt 2 cards\n");
		for(int i = 0; i < players.length; i++)
		{
			//If the person is a Player.
			if(players[i] instanceof Player)
			{
				Player player = (Player)players[i];
				//If the Player is not bankrupt.
				if(!player.getBankrupt())
				{
					gameLog.setText(gameLog.getText() + player.getName() 
						+ " was dealt a ");
					Card[] cards = player.getHand(0).getCards();
					//For each card in the hand.
					for(int j = 0; j < cards.length; j++)
					{
						gameLog.setText(gameLog.getText() + cards[j]);
						if(j < cards.length - 1)
						{
							gameLog.setText(gameLog.getText() + " and a ");
						}
					}
					gameLog.setText(gameLog.getText() + "\n");
					
					player = (Player)table.getPersonAtIndex(i);
					//If the player has Blackjack
					if(player.getHand(0).getHandScore() == Table.BLACKJACK)
					{
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " has Blackjack\n");
					}
					playerPanels[i].updatePanel(i);
				}
			}
			//If the person is the Dealer.
			else
			{
				Person dealer = players[i];
				gameLog.setText(gameLog.getText() + dealer.getName() 
					+ " was dealt a ");
				Card[] cards = dealer.getHand(0).getCards();
				//For each card in the hand.
				for(int j = 0; j < cards.length; j++)
				{
					gameLog.setText(gameLog.getText() + cards[j]);
					if(j < cards.length - 1)
					{
						gameLog.setText(gameLog.getText() + " and a ");
					}
				}
				gameLog.setText(gameLog.getText() + "\n");
				
				dealer = table.getPersonAtIndex(i);
				//If the player has Blackjack
				if(dealer.getHand(0).getHandScore() == Table.BLACKJACK)
				{
					gameLog.setText(gameLog.getText() + dealer.getName() 
						+ " has Blackjack\n");
				}
				dealerPanel.updatePanel(i);
			}
		}
		
		//If the dealer's first card is an Ace,begin insurance round.
		if(players[players.length - 1].getHand(0).getHandScore() == 11)
		{
			gameLog.setText(gameLog.getText() + "\nInsurance:\n");
			insurance(0);
		}
		//If not begin the first player turn.
		else
		{
			gameLog.setText(gameLog.getText() + "\nPlayer Turns:\n");
			playerTurn(0);
		}
	}
	
	/**
	 * Sets the insurance of a player at a specified index.
	 * 
	 * @param index Index of the player.
	 * @since 1.2.0
	 */
	private void insurance(int index)
	{
		Player player = (Player)table.getPersonAtIndex(index);
		
		//If the player is not bankrupt and does not have Blackjack.
		if(!player.getBankrupt() && player.getHand(0).getHandScore() < Table.BLACKJACK)
		{
			//If the player is a Human, open an InsuranceWindow.
			if(player instanceof Human)
			{
				InsuranceWindow window = new InsuranceWindow(index);
				window.setVisible(true);
				playerPanels[index].updatePanel(index);
			}
			//If the player is a CPU the table handles the insurance.
			else if(player instanceof CPU)
			{
				double insurance = table.setCPUInsurance(index);
				player = (Player)table.getPersonAtIndex(index);
				//If the CPU took insurance.
				if(player.getTookInsurance())
				{
					playerPanels[index].updatePanel(index);
					gameLog.setText(gameLog.getText() + player.getName() + 
							" has $" + insurance + " of insurance\n");
				}
				//If the CPU doesn't take insurance.
				else
				{
					gameLog.setText(gameLog.getText() + 
							player.getName() + " doesn't take insurance\n");
				}
				
				playerPanels[index].updatePanel(index);
				nextInsurance(index);
			} 
		}
		//If the player is bankrupt, or has a score greater than or equal to 
		//Blackjack go to the next player.
		else
		{
			nextInsurance(index);
		}
	}
	
	/**
	 * Checks if there are any more insurance bets to set.
	 * 
	 * @param index Index of the player.
	 * @since 1.3.0
	 */
	private void nextInsurance(int index)
	{
		//If there is are any more player's left.
		if(index + 1 < table.getPlayers().length - 1)
		{
			insurance(index + 1);
		}
		//If there aren't any player's left.
		else
		{
			//If the insurance bet was successful prepare for the next round.
			if(insuranceResult())
			{
				for(int i = 0; i < table.getPlayers().length - 1; i++)
				{
					Player player = (Player)table.getPersonAtIndex(i);
					if(player.getBankrupt())
					{
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " has gone bankrupt\n");
					}
				}
				
				resetTableForNewRound();
			}
			//If the insurance bet was not successful
			else
			{
				gameLog.setText(gameLog.getText() + "\nPlayer Turns:\n");
				playerTurn(0);
			}
		}
	}
	
	/**
	 * Determines the result of the insurance bet.
	 * 
	 * @since 1.3.0
	 */
	private boolean insuranceResult()
	{
		Person[] players = table.getPlayers();
		int finalIndex = table.getPlayers().length - 1;
		
		//If the dealer's second card has a value of 10.
		if(table.attemptDealerCardFlip())
		{
			Person dealer = table.getPersonAtIndex(finalIndex);
			gameLog.setText(gameLog.getText() + dealer.getName() + "'s second card is " + 
					dealer.getHand(0).getCard(1) + "\n");
			gameLog.setText(gameLog.getText() + dealer.getName() 
					+ " has Blackjack\n");
			dealerPanel.updatePanel(finalIndex);
				
			for(int i = 0; i < players.length - 1; i++)
			{
				gameLog.setText(gameLog.getText() + table.insurancePayout(i));
				playerPanels[i].updatePanel(i);
			}
			return true;
		}
		//If the dealer's second card does not.
		else
		{
			gameLog.setText(gameLog.getText() + table.getPersonAtIndex(finalIndex).
					getName() + " does not have Blackjack, all insurace bets lost\n");
			
			//Reset the insurance of each player that took insurance.
			for(int i = 0; i < table.getPlayers().length - 1; i++)
			{
				Player player = (Player)table.getPersonAtIndex(i);
				if(player.getTookInsurance())
				{
					table.resetInsurance(i);
				}
				playerPanels[i].updatePanel(i);
				
			}
			return false;
		}
	}
	
	/**
	 * Plays out the turn of a player at a specific index.
	 * 
	 * @param index The index of a Player
	 * @since 1.2.0
	 */
	private void playerTurn(int index)
	{
		Player player = (Player)table.getPersonAtIndex(index);
		//If the Player is not bankrupt and has a score of less than Blackjack.
		if(!player.getBankrupt() && player.getHand(0).getHandScore() < Table.BLACKJACK)			
		{
			gameLog.setText(gameLog.getText() + "It's now " 
					+ player.getName() + "'s turn\n");
			//If the Player is a Human open up a new TurnWindow.
			if(player instanceof Human)
			{
				TurnWindow window = new TurnWindow(index);
				window.setVisible(true);
			}
			//If the Player is a CPU the table handles the player's turn.
			else if(player instanceof CPU)
			{
				gameLog.setText(gameLog.getText() + player.getName() + " "
						+ "stands with a score of " + table.getPersonAtIndex
						(index).getHand(0).getHandScore() + "\n");
				playerPanels[index].updatePanel(index);
				nextPlayer(index);
			}
		}
		//If the Player is bankrupt or the Player's score is greater than or
		//equal to Blackjack.
		else
		{
			nextPlayer(index);
		}
	}
	
	/**
	 * Checks if there are any player's left to have a turn.
	 * 
	 * @param index Index of the player.
	 * @since 1.3.0
	 */
	private void nextPlayer(int index)
	{
		//If there are any more Player's left.
		if(index + 1 < table.getPlayers().length - 1)
		{
			playerTurn(index + 1);
		}
		//If not run the Dealer's turn.
		else
		{
			dealerTurn();
		}
	}
	
	/**
	 * Plays out the dealer's turn.
	 * 
	 * @since 1.2.0
	 */
	private void dealerTurn()
	{
		int finalIndex = table.getPlayers().length - 1;
		table.flipDealersCard();
		Person dealer = table.getPersonAtIndex(finalIndex);
		gameLog.setText(gameLog.getText() + "It's now " 
				+ dealer.getName() + "'s turn\n");
		gameLog.setText(gameLog.getText() + dealer.getName() + "'s face "
				+ "down card was " + dealer.getHand(0).getCard(1) + " and"
				+ " they now have a score of " + dealer.getHand(0).
				getHandScore() + "\n");
		
		Card[] cards = table.addToDealersHand();
		
		//If the dealer is dealt any cards, add them to the game log.
		if(cards.length > 0)
		{
			gameLog.setText(gameLog.getText() + dealer.getName() 
				+ " adds a ");
			for(int i = 0; i < cards.length; i++)
			{
				gameLog.setText(gameLog.getText() + cards[i]);
				if(i < cards.length - 1)
				{
					gameLog.setText(gameLog.getText() + ", ");
				}
			}
			
			dealer = table.getPersonAtIndex(finalIndex);
			gameLog.setText(gameLog.getText() + " and now has a score of " 
					+ dealer.getHand(0).getHandScore() + "\n");
		}
		dealerPanel.updatePanel(finalIndex);
		gameLog.setText(gameLog.getText() + "\nRound Results:\n");
		roundResults();
	}
	
	/**
	 * Determines the results of the round for each Player.
	 * 
	 * @since 1.3.0
	 */
	private void roundResults()
	{
		for(int i = 0; i < table.getPlayers().length - 1; i++)
		{
			gameLog.setText(gameLog.getText() + table.roundResult(i) + "\n");
			Player player = (Player)table.getPersonAtIndex(i);
			
			//If the player has gone bankrupt.
			if(player.getBankrupt())
			{
				gameLog.setText(gameLog.getText() + player.getName() 
					+ " has gone bankrupt\n");
			}
			//If the player has not gone bankrupt.
			else
			{
				//If the Player has a standard win.
				if(player.getHasWin())
				{
					gameLog.setText(gameLog.getText() + player.getName() + " wins $" 
							+ player.getCurrentWin() + "\n");
				}
				//If the Player has Blackjack.
				else if(player.getHasBlackjack())
				{
					gameLog.setText(gameLog.getText() + player.getName() + " wins $"
							+ player.getCurrentBlackjack() + "\n");
				}
			}
			playerPanels[i].updatePanel(i);
		}
		
		resetTableForNewRound();
	}
	
	/**
	 * Resets the table and every person for the new round.
	 * 
	 * @since 1.3.0
	 */
	private void resetTableForNewRound()
	{
		for(int i = 0; i < table.getPlayers().length; i++)
		{
			table.prepareForNewRound(i);
			//If the Person is a Player, update their panel.
			if(table.getPersonAtIndex(i) instanceof Player)
			{
				playerPanels[i].updatePanel(i);
			}
			//If the Person is a Dealer, update their panel.
			else
			{
				dealerPanel.updatePanel(i);
			}
		}
		
		nextRound();
	}
	
	/**
	 * Checks if there are any rounds remaining.
	 * 
	 * @since 1.3.0
	 */
	private void nextRound()
	{
		table.setCompletedRounds(table.getCompletedRounds() + 1);
		
		//If there are rounds remaining and any player's who aren't bankrupt.
		if(table.getCurrentRound() + 1 <= table.getTotalRounds() && 
				table.checkIfAnyPlayerNotBankrupt())
		{
			table.setCurrentRound(table.getCurrentRound() + 1);
			gameLog.setText(gameLog.getText() + "\n");
			currentRound();
			intialWager(0);
		}
		//If there are no rounds remaining or every player is bankrupt.
		else
		{
			EndGameWindow window = new EndGameWindow();
			window.setVisible(true);
		}
	}
}