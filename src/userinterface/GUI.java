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
import java.util.InputMismatchException;

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
import javax.swing.border.Border;

import card.Card;
import card.Face;
import card.Hand;
import logic.Table;
import logic.TableException;
import player.CPU;
import player.CpuException;
import player.Dealer;
import player.Human;
import player.HumanException;
import player.Person;
import player.PersonException;
import player.Player;
import player.PlayerException;

public class GUI extends JFrame 
{
	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;
	public static final int BUTTON_PANEL_HEIGHT = 80;
	private Font buttonFont = new Font("Verdana", Font.BOLD, 15);
	private Dimension buttonSize = new Dimension(130, BUTTON_PANEL_HEIGHT/2);
	private Dimension tinyWindow = new Dimension(600, 150);
	private Dimension reducedWindow = new Dimension(500, 400);
	private PlayerPanel[] playerPanels;
	private DealerPanel dealerPanel;
	private JTextArea gameLog;
	private Table table;
	
	private class CheckOnExit extends WindowAdapter
	{
		@Override
		public void windowClosing(WindowEvent e)
		{
			CloseWindow window = new CloseWindow();
			window.setVisible(true);
		}
	}
	
	private class CloseWindow extends JDialog
	{	
		private class ButtonListener implements ActionListener
		{
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
	
	private class GameSettingsWindow extends JDialog
	{
		private JTextField roundInput;
		private JTextField humanInput;
		private JTextField cpuInput;
		private JLabel roundError;
		private JLabel humanError;
		private JLabel cpuError;
		
		private class SetNameWindow extends JDialog
		{
			private int index;
			private int maxIndex;
			private JLabel error;
			private JTextField input;
			private class ButtonListener implements ActionListener
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					String command = e.getActionCommand();
					if(command.equals("Confirm"))
					{
						try 
						{
							table.setPersonNameAtIndex(index, input.getText());
						} 
						catch (PersonException ex) 
						{
							error.setText(ex.getMessage());
							input.setText("");
							return;
						}
						dispose();
						nextName(index, maxIndex);
					}
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
			
			private void nextName(int index, int maxIndex)
			{
				if(index + 1 < maxIndex)
				{
					SetNameWindow window = new SetNameWindow("Player " + (index + 2) 
							+ " name", index + 1, maxIndex);
					window.setVisible(true);
				}
				else
				{
					mainScreen();
				}
			}
		}
		
		private class ButtonListener implements ActionListener
		{	
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
					//Error casting round input to integer
					catch(NumberFormatException ex)
					{
						roundError.setText("Invalid input, please enter a number");
						resetInputs();
						return;
					} 
					//Error too few rounds
					catch (TableException ex) 
					{
						roundError.setText(ex.getMessage());
						resetInputs();
						return;
					}
					
					try
					{
						int cpuPlayers;
						String hInput = humanInput.getText();
						String cInput = cpuInput.getText();
						try
						{
							if(hInput.equals(""))
							{
								humanPlayers = 0;
							}
							else
							{
								humanPlayers = Integer.parseInt(humanInput.getText());
							}
						}
						//Error casting human input to integer
						catch(NumberFormatException ex)
						{
							humanError.setText("Invalid input, please enter a number");
							cpuError.setText("");
							resetInputs();
							return;
						} 
						
						try
						{
							if(cInput.equals(""))
							{
								cpuPlayers = 0;
							}
							else
							{
								cpuPlayers = Integer.parseInt(cpuInput.getText());
							}
						}
						//Error casting cpu input to integer
						catch(NumberFormatException ex)
						{
							cpuError.setText("Invalid input, please enter a number");
							resetInputs();
							return;
						} 
						
						table.createPlayers(humanPlayers, cpuPlayers);
					}
					//Error too many/few players
					catch (PlayerException ex) 
					{
						humanError.setText(ex.getMessage());
						cpuError.setText(ex.getMessage());
						resetInputs();
						return;
					} 
					//Error too many/few human players
					catch (HumanException ex) 
					{
						humanError.setText(ex.getMessage());
						resetInputs();
						return;
					} 
					//Error too many cpu players
					catch (CpuException ex) 
					{
						cpuError.setText(ex.getMessage());
						resetInputs();
						return;
					}
					dispose();
					
					SetNameWindow window = new SetNameWindow("Player " + (0 + 1) 
							+ " name", 0, humanPlayers);
					window.setVisible(true);
				}
				else if(command.equals("Cancel"))
				{
					dispose();
				}
				else
				{
					System.out.println("Unexpected Error in Game Settings Window");
				}
			}
			
			private void resetInputs()
			{
				roundInput.setText("");
				humanInput.setText("");
				cpuInput.setText("");
			}
		}
		
		public GameSettingsWindow()
		{
			setTitle("Game Settings");
			setSize(reducedWindow);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setResizable(false);
			setModalityType(ModalityType.APPLICATION_MODAL);
			setLayout(new BorderLayout());
			
			JPanel inputPanel = new JPanel(new GridLayout(6, 1));
			JPanel roundPanel = new JPanel(new GridLayout(1, 2));
			JLabel roundLabel = new JLabel("Please enter the number of rounds");
			roundLabel.setToolTipText("Must be a postive number greater than 0");
			roundPanel.add(roundLabel);
			roundError = new JLabel("");
			roundPanel.add(roundError);
			inputPanel.add(roundPanel);
			roundInput = new JTextField(10);
			inputPanel.add(roundInput);
			
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
			
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 
					20, BUTTON_PANEL_HEIGHT/4));
			JButton confirmButton = new JButton("Confirm");
			confirmButton.addActionListener(new ButtonListener());
			buttonPanel.add(confirmButton);
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ButtonListener());
			buttonPanel.add(cancelButton);
			add(inputPanel, BorderLayout.CENTER);
			add(buttonPanel, BorderLayout.SOUTH);
		}
	}
	
	private class AboutWindow extends JDialog
	{
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
	
	private class StatisticsWindow extends JDialog
	{
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
	
	private class MenuButtonListener implements ActionListener
	{
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
	
	private class HandPanel extends JPanel
	{
		private JLabel[] guiHand;
		public HandPanel(Hand[] hand)
		{
			int row = 2;
			int col = 4;
			setLayout(new GridLayout(row, col));
			guiHand = new JLabel[row*col];
			for(int i = 0; i < row*col; i++)
			{
				JLabel card = new JLabel("C");
				card.setHorizontalAlignment(JLabel.CENTER);
				guiHand[i] = card;
				add(guiHand[i]);
			}
		}
	}
	
	private class PlayerPanel extends JPanel
	{
		private JLabel totalMoney;
		private JLabel wager;
		private JLabel scoreLabel1;
		private JLabel score1;
		private JLabel scoreLabel2;
		private JLabel score2;
		private JLabel extraLabel;
		private JLabel extra;
		private HandPanel hand;
		
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
			JPanel scorePanel1 = new JPanel(new GridLayout(2, 1));
			scoreLabel1 = new JLabel("Hand Score");
			scoreLabel1.setHorizontalAlignment(JLabel.CENTER);
			scorePanel1.add(scoreLabel1);
			score1 = new JLabel("" + player.getHand(0).getHandScore());
			score1.setHorizontalAlignment(JLabel.CENTER);
			scorePanel1.add(score1);
			statisticsPanel.add(scorePanel1);
			
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
			JPanel scorePanel2 = new JPanel(new GridLayout(2, 1));
			scoreLabel2 = new JLabel("");
			scoreLabel2.setHorizontalAlignment(JLabel.CENTER);
			scorePanel2.add(scoreLabel2);
			score2 = new JLabel("");
			score2.setHorizontalAlignment(JLabel.CENTER);
			scorePanel2.add(score2);
			statisticsPanel.add(scorePanel2);
			add(statisticsPanel);
			
			//Hand Panel
			JPanel fullHandPanel = new JPanel(new GridLayout(0, 1));
			hand = new HandPanel(player.getHands());
			fullHandPanel.add(hand);
			JScrollPane handScrollPanel = new JScrollPane(fullHandPanel);
			handScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.
					HORIZONTAL_SCROLLBAR_NEVER);
			handScrollPanel.setVerticalScrollBarPolicy(JScrollPane.
					VERTICAL_SCROLLBAR_AS_NEEDED);
			add(handScrollPanel);
		}
		
		public void updatePanel(int index)
		{
			Player player = (Player)table.getPersonAtIndex(index);
			totalMoney.setText("$" + player.getTotalMoney());
			wager.setText("$" + player.getWager());
			score1.setText("" + player.getHand(0).getHandScore());
			if(player.getHand(0).getSplit())
			{
				if(!scoreLabel2.isVisible())
				{
					score2.setVisible(true);
				}
				
				scoreLabel1.setText("Hand 1 Score");
				scoreLabel2.setText("Hand 2 Score");
				score2.setText("" + player.getHand(1).getHandScore());
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
		}
	}
	
	private class DealerPanel extends JPanel
	{
		private JLabel score;
		private HandPanel hand;
		
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
			hand = new HandPanel(dealer.getHands());
			fullHandPanel.add(hand);
			JScrollPane handScrollPanel = new JScrollPane(fullHandPanel);
			handScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.
					HORIZONTAL_SCROLLBAR_NEVER);
			handScrollPanel.setVerticalScrollBarPolicy(JScrollPane.
					VERTICAL_SCROLLBAR_AS_NEEDED);
			add(handScrollPanel);
		}
		
		public void updatePanel(int index)
		{
			Person player = table.getPersonAtIndex(index);
			score.setText("" + player.getHand(0).getHandScore());
		}
	}
	
	private class WagerWindow extends JDialog
	{
		private JLabel error;
		private JTextField input;
		private int index;
		
		private class ButtonListener implements ActionListener
		{
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
					nextBet(index);
					dispose();
					return;
				}
				else if(command.equals("Clear"))
				{
					input.setText("");
				}
				else
				{
					System.out.println("Unexpected Error on Wager Window");
				}
			}
		}
		
		public WagerWindow(int index)
		{
			String name = table.getPersonAtIndex(index).getName();
			setTitle(name + "'s Wager");
			setSize(tinyWindow);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			setResizable(false);
			setModalityType(ModalityType.MODELESS);
			setLayout(new BorderLayout());
			
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
	
	private class InsuranceWindow extends JDialog
	{
		private int index;
		private JLabel error;
		private class ButtonListener implements ActionListener
		{
			private JTextField input;
			
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
				else if(cmd.equals("No"))
				{
					gameLog.setText(gameLog.getText() + player.getName() + " doesn't take insurance\n");
					dispose();
					nextInsurance(index);
				}
				else if(cmd.equals("Confirm"))
				{
					double insurance = 0;
					try
					{
						insurance = Integer.parseInt(input.getText());
						table.setHumanInsurance(index, insurance);
					}
					catch(NumberFormatException ex)
					{
						input.setText("");
						error.setText("Invalid input, please enter a valid number");
						return;
					}
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
		
		public InsuranceWindow(int index)
		{
			String name = table.getPersonAtIndex(index).getName();
			setTitle(name + "'s Insurance");
			setSize(tinyWindow);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			setResizable(false);
			setModalityType(ModalityType.MODELESS);
			setLayout(new BorderLayout());
			
			JPanel messagePanel = new JPanel(new GridLayout(1, 2));
			JLabel command = new JLabel("Would you like insurance?");
			messagePanel.add(command);
			error = new JLabel("");
			messagePanel.add(error);
			add(messagePanel, BorderLayout.CENTER);
			
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
	
	private class TurnWindow extends JDialog
	{
		private JLabel command;
		private JLabel error;
		private JPanel buttonPanel;
		private int index;
		
		private class ButtonListener implements ActionListener
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String cmd = e.getActionCommand();
				Player player = (Player)table.getPersonAtIndex(0);
				
				if(cmd.equals("Stand"))
				{
					gameLog.setText(gameLog.getText() + player.getName() 
						+ " stands with a score of " + player.getHand(0).
						getHandScore() + "\n");
					dispose();
					nextPlayer(index);
				}
				else if(cmd.equals("Hit"))
				{
					error.setText("");
					Card card = table.hit(index, 0);
					player = (Player)table.getPersonAtIndex(index);
					gameLog.setText(gameLog.getText() + player.getName() 
						+ " hits and is dealt a " + card + "\n");
					playerPanels[index].updatePanel(index);
					
					int handScore = player.getHand(0).getHandScore();
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
					else if(handScore == Table.BLACKJACK)
					{
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " has Blackjack and is forced to stand\n");
						dispose();
						nextPlayer(index);
					}
					else
					{
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " busts\n");
						dispose();
						nextPlayer(index);
					}
				}
				else if(cmd.equals("Double Down"))
				{
					error.setText("");
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
						
						if(player.getBusted())
						{
							gameLog.setText(gameLog.getText() 
									+ player.getName() + " has gone bust");
						}
						playerPanels[index].updatePanel(index);
						dispose();
						nextPlayer(index);
					}
					else
					{
						error.setText("You don't have enough money to double down");
						return;
					}
				}
				else if(cmd.equals("Split"))
				{
					error.setText("");
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
					else if(player.getTotalMoney() < player.getWager())
					{
						error.setText("You don't have enough money to split");
						return;
					}
					else if(player.getHand(0).getCard(0).getValue() != player.
							getHand(0).getCard(0).getValue() || (player.getHand(0).
							getCard(0).getFace() != Face.ACE && player.getHand(0).
							getCard(1).getFace() != Face.ACE))
					{
						error.setText("The cards in your hand don't match");
						return;
					}
				}
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
		
		private class SplitButtonListener implements ActionListener
		{
			private int handIndex;
			Player player = (Player)table.getPersonAtIndex(index);
			public SplitButtonListener(int handIndex)
			{
				this.handIndex = handIndex;
			}

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String cmd = e.getActionCommand();
				boolean nextHand = false;
				
				if(cmd.equals("Stand"))
				{
					gameLog.setText(gameLog.getText() + "Stands hand " 
							+ (handIndex + 1) + " with a score of " + player.
							getHand(0).getHandScore() + "\n");
					nextHand = true;
				}
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
					else if(handScore == Table.BLACKJACK)
					{
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " has Blackjack and is forced to stand\n");
						nextHand = true;
					}
					else
					{
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " busts\n");
						dispose();
						nextPlayer(index);
					}
					
				}
				else if(cmd.equals("Double Down"))
				{
					error.setText("");
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
						if(player.getBusted())
						{
							gameLog.setText(gameLog.getText() 
									+ player.getName() + " busts\n");
							dispose();
							nextPlayer(handIndex);
						}
						else
						{
							nextHand = true;
						}
					}
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
				
				if(nextHand)
				{
					Hand[] hands = player.getHands();
					
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
					else
					{
						dispose();
						nextPlayer(index);
					}
				}
			}
		}
		
		public TurnWindow(int index)
		{
			String name = table.getPersonAtIndex(index).getName();
			setTitle(name + "'s Turn");
			setSize(tinyWindow);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			setResizable(false);
			setModalityType(ModalityType.APPLICATION_MODAL);
			setLayout(new BorderLayout());
			
			JPanel messagePanel = new JPanel(new GridLayout(1, 2));
			command = new JLabel("What action would you like to take?");
			messagePanel.add(command);
			error = new JLabel("");
			messagePanel.add(error);
			add(messagePanel, BorderLayout.CENTER);
			
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
	
	public GUI()
	{
		super("Blackjack - By Brodie Robertson");
		setSize(WIDTH, HEIGHT);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		addWindowListener(new CheckOnExit());
		Color orange = new Color(243, 101, 37);
		getContentPane().setBackground(orange);
		
		JLabel title = new JLabel("Blackjack");
		Font titleFont = new Font("Times New Roman", Font.BOLD, 200);
		title.setFont(titleFont);
		title.setHorizontalAlignment(JLabel.CENTER);
		add(title, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 
				BUTTON_PANEL_HEIGHT/4));
		buttonPanel.setBackground(Color.DARK_GRAY);
		buttonPanel.setPreferredSize(new Dimension(WIDTH, BUTTON_PANEL_HEIGHT));
		JButton playButton = new JButton("Start Game");
		playButton.addActionListener(new MenuButtonListener());
		playButton.setPreferredSize(buttonSize);
		playButton.setFont(buttonFont);
		buttonPanel.add(playButton);
		JButton quitButton = new JButton("Quit Game");
		quitButton.addActionListener(new MenuButtonListener());
		quitButton.setPreferredSize(buttonSize);
		quitButton.setFont(buttonFont);
		buttonPanel.add(quitButton);
		JButton aboutButton = new JButton("About");
		aboutButton.addActionListener(new MenuButtonListener());
		aboutButton.setPreferredSize(buttonSize);
		aboutButton.setFont(buttonFont);
		buttonPanel.add(aboutButton);
		
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
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
		aboutButton.addActionListener(new MenuButtonListener());
		menu.add(aboutButton);
		JMenuItem statsButton = new JMenuItem("Statistics");
		statsButton.addActionListener(new MenuButtonListener());
		menu.add(statsButton);
		JMenuItem quitButton = new JMenuItem("Quit Game");
		quitButton.addActionListener(new MenuButtonListener());
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
		
		JPanel bottomCenterPanel = new JPanel(new GridLayout(1, Table.MAXNUMPLAYERS));
		playerPanels = new PlayerPanel[table.getPlayers().length - 1];
		for(int i = 0; i < Table.MAXNUMPLAYERS; i++)
		{
			if(i < playerPanels.length)
			{
				Player player = (Player)table.getPersonAtIndex(i);
				playerPanels[i] = new PlayerPanel(player);
				bottomCenterPanel.add(playerPanels[i]);
			}
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
		initialBet(0);
	}
	
	/**
	 * 
	 */
	private void currentRound()
	{
		gameLog.setText(gameLog.getText() + "Round " + table.getCurrentRound() + "\n");
	}
	
	/**
	 * @param index
	 */
	private void initialBet(int index)
	{
		Player player = (Player)table.getPersonAtIndex(index);
		if(!player.getBankrupt())
		{
			if(player instanceof Human)
			{
				WagerWindow window = new WagerWindow(index);
				window.setVisible(true);		
				playerPanels[index].updatePanel(index);
			}
			else if(player instanceof CPU)
			{
				double wager = table.setCpuWager(index);
				gameLog.setText(gameLog.getText() + player.getName() + 
						"'s wager is $" + wager + "\n");
				playerPanels[index].updatePanel(index);
				
				nextBet(index);
			}
		}
		else
		{
			nextBet(index);
		}
	}
	
	/**
	 * @param index
	 */
	private void nextBet(int index)
	{
		if(index + 1 < table.getPlayers().length - 1)
		{
			initialBet(index + 1);
		}
		else
		{
			initialDeal();
		}
	}
	
	/**
	 * 
	 */
	private void initialDeal()
	{
		table.deal();
		Person[] players = table.getPlayers();
		gameLog.setText(gameLog.getText() + "All player's are dealt 2 cards\n");
		for(int i = 0; i < players.length; i++)
		{
			if(players[i] instanceof Player)
			{
				Player player = (Player)players[i];
				if(!player.getBankrupt())
				{
					gameLog.setText(gameLog.getText() + player.getName() 
						+ " was dealt a ");
					Card[] cards = player.getHand(0).getCards();
					for(int j = 0; j < cards.length; j++)
					{
						gameLog.setText(gameLog.getText() + cards[j]);
						if(j < cards.length - 1)
						{
							gameLog.setText(gameLog.getText() + " and a ");
						}
					}
					gameLog.setText(gameLog.getText() + "\n");
					
					//If the player has Blackjack
					player = (Player)table.getPersonAtIndex(i);
					if(player.getHand(0).getHandScore() == Table.BLACKJACK)
					{
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " has Blackjack\n");
					}
					playerPanels[i].updatePanel(i);
				}
			}
			else
			{
				Person dealer = players[i];

				gameLog.setText(gameLog.getText() + dealer.getName() 
					+ " was dealt a ");
				Card[] cards = dealer.getHand(0).getCards();
				for(int j = 0; j < cards.length; j++)
				{
					gameLog.setText(gameLog.getText() + cards[j]);
					if(j < cards.length - 1)
					{
						gameLog.setText(gameLog.getText() + " and a ");
					}
				}
				gameLog.setText(gameLog.getText() + "\n");
				
				//If the player has Blackjack
				dealer = table.getPersonAtIndex(i);
				if(dealer.getHand(0).getHandScore() == Table.BLACKJACK)
				{
					gameLog.setText(gameLog.getText() + dealer.getName() 
						+ " has Blackjack\n");
				}
				dealerPanel.updatePanel(i);
			}
		}
		
		if(players[players.length - 1].getHand(0).getHandScore() == 11)
		{
			insurance(0);
		}
		else
		{
			playerTurn(0);
		}
	}
	
	/**
	 * @param index
	 */
	private void insurance(int index)
	{
		Player player = (Player)table.getPersonAtIndex(index);
		if(!player.getBankrupt() && player.getHand(0).getHandScore() < Table.BLACKJACK)
		{
			if(player instanceof Human)
			{
				InsuranceWindow window = new InsuranceWindow(index);
				window.setVisible(true);
				playerPanels[index].updatePanel(index);
			}
			
			else if(player instanceof CPU)
			{
				double insurance = table.setCpuInsurance(index);
				player = (Player)table.getPersonAtIndex(index);
				if(player.getTookInsurance())
				{
					playerPanels[index].updatePanel(index);
					gameLog.setText(gameLog.getText() + player.getName() + 
							" has $" + insurance + " of insurance\n");
				}
				else
				{
					gameLog.setText(gameLog.getText() + 
							player.getName() + " doesn't take insurance\n");
				}
				
				playerPanels[index].updatePanel(index);
				nextInsurance(index);
			} 
		}
		else
		{
			nextInsurance(index);
		}
	}
	
	/**
	 * @param index
	 */
	private void nextInsurance(int index)
	{
		if(index + 1 < table.getPlayers().length - 1)
		{
			insurance(index + 1);
		}
		else
		{
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
			else
			{
				playerTurn(0);
			}
		}
	}
	
	/**
	 * 
	 */
	private boolean insuranceResult()
	{
		Person[] players = table.getPlayers();
		int finalIndex = table.getPlayers().length - 1;
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
		else
		{
			gameLog.setText(gameLog.getText() + table.getPersonAtIndex(finalIndex).
					getName() + " does not have Blackjack, all insurace bets lost\n");
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
	 * @param index
	 */
	private void playerTurn(int index)
	{
		Person[] players = table.getPlayers();
		Player player = (Player)players[index];
		if(!player.getBankrupt() && player.getHand(0).getHandScore() < Table.BLACKJACK)			
		{
			gameLog.setText(gameLog.getText() + "It's now " 
					+ player.getName() + "'s turn\n");
			if(player instanceof Human)
			{
				TurnWindow window = new TurnWindow(index);
				window.setVisible(true);
			}
			else if(player instanceof CPU)
			{
				gameLog.setText(gameLog.getText() + player.getName() + " stands\n");
				playerPanels[index].updatePanel(index);
				nextPlayer(index);
			}
		}
		else
		{
			nextPlayer(index);
		}
	}
	
	/**
	 * @param index
	 */
	private void nextPlayer(int index)
	{
		if(index + 1 < table.getPlayers().length - 1)
		{
			playerTurn(index + 1);
		}
		else
		{
			dealerTurn();
		}
	}
	
	/**
	 * 
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
			gameLog.setText(gameLog.getText() + "\n");
		}
		
		dealerPanel.updatePanel(finalIndex);
		roundResults();
	}
	
	/**
	 * 
	 */
	private void roundResults()
	{
		for(int i = 0; i < table.getPlayers().length - 1; i++)
		{
			gameLog.setText(gameLog.getText() + table.roundResult(i) + "\n");
			Player player = (Player)table.getPersonAtIndex(i);
			if(player.getBankrupt())
			{
				gameLog.setText(gameLog.getText() + player.getName() 
					+ " has gone bankrupt\n");
			}
			else
			{
				if(player.getHasWin())
				{
					gameLog.setText(gameLog.getText() + player.getName() + " wins $" 
							+ player.getCurrentWin() + "\n");
				}
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
	 * 
	 */
	private void resetTableForNewRound()
	{
		for(int i = 0; i < table.getPlayers().length; i++)
		{
			table.prepareForNewRound(i);
			if(table.getPersonAtIndex(i) instanceof Player)
			{
				playerPanels[i].updatePanel(i);
			}
			else
			{
				dealerPanel.updatePanel(i);
			}
		}
		
		nextRound();
	}
	
	private void nextRound()
	{
		//Rounds Remaining
		if(table.getCurrentRound() + 1 <= table.getTotalRounds() && 
				table.checkIfAnyPlayerNotBankrupt())
		{
			gameLog.setText(gameLog.getText() + "MORE ROUNDS REMAINING COMING SOON");
		}
		//No Rounds Remaining
		else
		{
			gameLog.setText(gameLog.getText() + "NO ROUNDS REMAINING COMING SOON");
		}
	}
}
