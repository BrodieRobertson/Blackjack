package userinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import card.Card;
import card.Colour;
import card.Deck;
import card.DeckException;
import card.Face;
import card.Hand;
import card.Suit;
import logic.*;
import player.*;

/**
 * The graphics layer of the game, manages all of the users interactions with 
 * the logic layer.
 * 
 * @author Brodie Robertson
 * @version 1.7.0
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
	 * Height of tiny window button panel.
	 */
	public static final int TINY_BUTTON_PANEL_HEIGHT = 50;
	/**
	 * Vertical gap between buttons on tiny window button panels.
	 */
	public static final int TINY_BUTTON_VGAP = TINY_BUTTON_PANEL_HEIGHT / 4;
	/**
	 * Horizontal gap between buttons on tiny window button panels.
	 */
	public static final int TINY_BUTTON_HGAP = TINY_BUTTON_PANEL_HEIGHT / 4;
	/**
	 * Size of small windows which don't require many elements.
	 */
	private static final Dimension TINY_WINDOW = new Dimension(660, 150);
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
	 * Size of small windows which require more elements than the tiny window.
	 */
	private static final Dimension REDUCED_WINDOW = new Dimension(500, 400);
	/**
	 * Font for windows that only have a label except for the title screen.
	 */
	private static final Font SINGLE_TEXT_FONT = new Font("Calibri", Font.BOLD, 24);
	/**
	 * Font for headings on the main screen.
	 */
	private static final Font MAIN_HEADING_FONT = new Font("Calibri", Font.BOLD, 18);
	/**
	 * Font for general text on the main screen.
	 */
	private static final Font MAIN_TEXT_FONT = new Font("Calibri", Font.PLAIN, 16);
	/**
	 * Font for every button except for the title screen.
	 */
	private static final Font BUTTON_FONT = new Font("Trebuchet MS", 
			Font.BOLD, 13);
	/**
	 * Size of all buttons except for the title screen.
	 */
	private static final Dimension BUTTON_SIZE = new Dimension(115, 30);
	/**
	 * Font for general text.
	 */
	private static final Font TEXT_FONT = new Font("Calibri", Font.BOLD, 14);
	/**
	 * Shorthand of JComponent constant WHEN_IN_FOCUSED_WINDOW
	 */
	public static final int WIFW = JComponent.WHEN_IN_FOCUSED_WINDOW;

	/**
	 * Every KeyStroke that has been reused.
	 */
	private static final KeyStroke ENTER = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), ESC = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
			A = KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), S = KeyStroke.getKeyStroke(KeyEvent.VK_S, 0),
			H = KeyStroke.getKeyStroke(KeyEvent.VK_H, 0), D = KeyStroke.getKeyStroke(KeyEvent.VK_D, 0),
			P = KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), U = KeyStroke.getKeyStroke(KeyEvent.VK_U, 0),
			M = KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), Y = KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0),
			N = KeyStroke.getKeyStroke(KeyEvent.VK_N, 0), G = KeyStroke.getKeyStroke(KeyEvent.VK_G, 0),
			L = KeyStroke.getKeyStroke(KeyEvent.VK_L, 0);
	/**
	 * Every Color that has been reused.
	 */
	public static final Color GREEN = new Color(0, 220, 0), RED = new Color(235, 0, 0), 
			LIGHT_BLUE = new Color(45, 177, 255), LIGHT_GRAY = new Color(128, 133, 137);
	/**
	 * Delay between CPU turns and some game log updates.
	 */
	private int turnDelay = 500;
	/**
	 * Timer to delay CPU turns and some game log updates.
	 */
	private Timer timer;
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
	 * GUI element showing the back of a card and how many cards are left in the deck.
	 */
	private GUICard deck;
	/**
	 * The logic layer for the game.
	 */
	private Table table;
	/**
	 * Formats doubles to to contain 2 digits after the decimal.
	 */
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	/**
	 * @author Brodie Robertson
	 * @version 1.4.2
	 * @since 1.4.2
	 */
	private class JGradientButton extends JButton
	{
	    /**
	     * Constructs a JGradientButton with a string parameter.
	     * 
	     * @param text The text on the JGradientButton.
	     */
	    private JGradientButton(String text)
	    {
	        super(text);
	        setContentAreaFilled(false);
	        setFocusPainted(false);
	        setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
	    }

	    /**
	     * Paints the JGradientButton with a gradient or solid colour depending
	     * on whether it is being pressed.
	     * 
	     * (non-Javadoc)
	     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	     * @since 1.4.2
	     */
	    @Override
	    protected void paintComponent(Graphics g)
	    {
	    	//If the button has been pressed set to a solid colour.
	    	if(getModel().isPressed())
	    	{
	    		Graphics2D g2 = (Graphics2D)g.create();
	    		g2.setPaint(getBackground());
	    		g2.fillRect(0, 0, getWidth(), getHeight());
	    		g2.dispose();
	    		
	    	}
	    	//If the button is only enabled set to a gradient.
	    	else if(getModel().isEnabled())
	    	{
		        Graphics2D g2 = (Graphics2D)g.create();        
		        g2.setPaint(new GradientPaint(new Point(0, 0), getBackground(),
		                new Point(0, getHeight()/3), Color.WHITE));
		        g2.fillRect(0, 0, getWidth(), getHeight()/3);
		        g2.setPaint(new GradientPaint(new Point(0, getHeight()/3), 
		        		Color.WHITE, new Point(0, getHeight()), getBackground()));
		        g2.fillRect(0, getHeight()/3, getWidth(), getHeight());
		        g2.dispose();
	    	}
    		super.paintComponent(g);
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
	 * @version 1.4.3
	 * @since 1.2.0
	 */
	private class CloseWindow extends JDialog
	{	
		/**
		 * Constructs a close window with a default layout.
		 * 
		 * @since 1.2.0
		 */
		public CloseWindow()
		{
			setTitle("Are you sure?");
			setSize(new Dimension(400, TINY_WINDOW.height));
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setModalityType(ModalityType.APPLICATION_MODAL);
			setLayout(new BorderLayout());
			getContentPane().setBackground(Color.LIGHT_GRAY);
			setResizable(false);
			
			JLabel message = new JLabel("Are you sure you want to exit?");
			message.setFont(SINGLE_TEXT_FONT);
			message.setHorizontalAlignment(JLabel.CENTER);
			add(message, BorderLayout.CENTER);
			
			//Ends the game.
			Action confirm = new AbstractAction() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					System.exit(0);	
				}
			};
			
			//Key bindings
			JRootPane rootPane = getRootPane();
			rootPane.getInputMap(WIFW).put(ENTER, "ENTER");
			rootPane.getActionMap().put("ENTER", confirm);
			rootPane.getInputMap(WIFW).put(ESC, "ESC");
			rootPane.getActionMap().put("ESC", new Close());
			
			//Button Panel
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 
					TINY_BUTTON_HGAP, TINY_BUTTON_VGAP));
			buttonPanel.setPreferredSize(new Dimension(getWidth(), TINY_BUTTON_PANEL_HEIGHT));
			buttonPanel.setBackground(Color.DARK_GRAY);
			JGradientButton confirmButton = new JGradientButton("Confirm");
			confirmButton.setFont(BUTTON_FONT);
			confirmButton.setPreferredSize(BUTTON_SIZE);
			confirmButton.setBackground(GREEN);
			confirmButton.addActionListener(confirm);
			confirmButton.setToolTipText("Closes the game");
			buttonPanel.add(confirmButton);
			JGradientButton cancelButton = new JGradientButton("Cancel");
			cancelButton.setFont(BUTTON_FONT);
			cancelButton.setPreferredSize(BUTTON_SIZE);
			cancelButton.setBackground(RED);
			cancelButton.addActionListener(new Close());
			cancelButton.setToolTipText("Continue");
			cancelButton.setToolTipText("Continue playing the game");
			buttonPanel.add(cancelButton);
			add(buttonPanel, BorderLayout.SOUTH);
		}
	}
	
	/**
	 * Closes the current window.
	 * 
	 * @author Brodie Robertson
	 * @version 1.6.0
	 * @since 1.6.0
	 */
	private class Close extends AbstractAction
	{
		/**
		 * Activates when this object receives an action event.
		 * 
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 * @since 1.6.0
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			dispose();	
		}
	}
	
	/**
	 * Opens a CloseWindow for user confirmation before exiting the program.
	 * 
	 * @author Brodie Robertson
	 * @version 1.4.1
	 * @since 1.4.1
	 */
	private class QuitGame extends AbstractAction
	{
		/**
		 * Activates when this object receives an action event.
		 * 
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 * @since 1.4.1
		 */
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			CloseWindow window = new CloseWindow();
			window.setVisible(true);
		}
	}
	
	/**
	 * Opens a StatisicsWindow to show the user various statistics about 
	 * their current game.
	 * 
	 * @author Brodie Robertson
	 * @version 1.4.1
	 * @since 1.4.1
	 */
	private class Statistics extends AbstractAction
	{
		/**
		 * Activates when this object receives an action event.
		 * 
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 * @since 1.4.1
		 */
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			StatisticsWindow window = new StatisticsWindow();
			window.setVisible(true);
		}
	}
	
	/**
	 * Opens a AboutWindow to show the user various statistics about the game's
	 * creation.
	 * 
	 * @author Brodie Robertson
	 * @version 1.4.1
	 * @since 1.4.1
	 */
	private class About extends AbstractAction
	{
		/**
		 * Activates when this object receives an action event.
		 * 
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 * 
		 * @since 1.4.1
		 */
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			AboutWindow window = new AboutWindow();
			window.setVisible(true);
		}	
	}
	
	/**
	 * Opens a GameSpeedWindow to change how fast CPU turns occur.
	 * 
	 * @author Brodie Robertson
	 * @version 1.6.0
	 * @since 1.6.0
	 */
	private class GameSpeed extends AbstractAction
	{
		/**
		 * Activates when this object receives an action event.
		 * 
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 * @since 1.6.0
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			GameSpeedWindow window = new GameSpeedWindow();
			window.setVisible(true);
		}
	}
	
	/**
	 * Dialog window used for setting the games options.
	 * 
	 * @author Brodie Robertson
	 * @version 1.4.3
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
		 * @version 1.4.3
		 * @since 1.2.0
		 */
		private class SetNameWindow extends JDialog
		{	
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
				setSize(TINY_WINDOW);
				setTitle(title);
				setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				setResizable(false);
				setModalityType(ModalityType.APPLICATION_MODAL);
				setLayout(new BorderLayout());
				
				//Request for input
				JPanel inputPanel = new JPanel(new GridLayout(2, 1));
				JPanel messagePanel = new JPanel(new GridLayout(1, 2));
				JLabel command = new JLabel("Enter the name of this player");
				command.setFont(TEXT_FONT);
				command.setToolTipText("Must contain at least 1 character");
				messagePanel.add(command);
				JLabel error = new JLabel("");
				error.setFont(TEXT_FONT);
				error.setHorizontalAlignment(SwingConstants.CENTER);
				error.setForeground(Color.RED);
				messagePanel.add(error);
				inputPanel.add(messagePanel);
				JTextField input = new JTextField(30);
				inputPanel.add(input);
				add(inputPanel, BorderLayout.CENTER);
				
				//Confirms the users name input and validates it.
				Action confirm = new AbstractAction() 
				{
					@Override
					public void actionPerformed(ActionEvent e) 
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
				};
				
				//Clears the users name input.
				Action clear = new AbstractAction() 
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						input.setText("");
						error.setText("");
					}
				};
				
				//Key bindings
				JRootPane rootPane = getRootPane();
				rootPane.getInputMap(WIFW).put(ENTER, "ENTER");
				rootPane.getActionMap().put("ENTER", confirm);
				rootPane.getInputMap(WIFW).put(ESC, "ESC");
				rootPane.getActionMap().put("ESC", clear);
				
				//Button Panel
				JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 
						TINY_BUTTON_HGAP, TINY_BUTTON_VGAP));
				buttonPanel.setPreferredSize(new Dimension(getWidth(), TINY_BUTTON_PANEL_HEIGHT));
				JGradientButton confirmButton = new JGradientButton("Confirm");
				confirmButton.setFont(BUTTON_FONT);
				confirmButton.setBackground(GREEN);
				confirmButton.setPreferredSize(BUTTON_SIZE);
				confirmButton.addActionListener(confirm);
				confirmButton.setToolTipText("Confirms the name input");
				buttonPanel.add(confirmButton);
				JGradientButton clearButton = new JGradientButton("Clear");
				clearButton.setFont(BUTTON_FONT);
				clearButton.setBackground(RED);
				clearButton.setPreferredSize(BUTTON_SIZE);
				clearButton.addActionListener(clear);
				clearButton.setToolTipText("Clears the name input");
				buttonPanel.add(clearButton);
				add(buttonPanel, BorderLayout.SOUTH);
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
		 * Constructs a GameSettingsWindow with a default layout.
		 * 
		 * @since 1.2.0
		 */
		public GameSettingsWindow()
		{
			setTitle("Game Settings");
			setSize(REDUCED_WINDOW);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			setResizable(false);
			setModalityType(ModalityType.APPLICATION_MODAL);
			setLayout(new BorderLayout());
			
			//Confirms the users game setting input.
			Action confirm = new AbstractAction() 
			{	
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					int humanPlayers;
					int cpuPlayers;
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
					try 
					{
						table.createDeck(humanPlayers + cpuPlayers);
					} 
					catch (DeckException ex) 
					{
						ex.printStackTrace();
						System.exit(0);
					}
					
					SetNameWindow window = new SetNameWindow("Player " + 1 
							+ " name", 0, humanPlayers);
					window.setVisible(true);
				}
			};
			
			//Clears the users name input.
			Action clear = new AbstractAction() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					resetInputs();
				}
			};
			
			//Key bindings
			JRootPane rootPane = getRootPane();
			rootPane.getInputMap(WIFW).put(ENTER, "ENTER");
			rootPane.getActionMap().put("ENTER", confirm);
			rootPane.getInputMap(WIFW).put(ESC, "ESC");
			rootPane.getActionMap().put("ESC", clear);
			
			JPanel inputPanel = new JPanel(new GridLayout(6, 1));
			
			//Round input panel
			JPanel roundPanel = new JPanel(new GridLayout(1, 2));
			JLabel roundLabel = new JLabel("Enter the number of rounds");
			roundLabel.setFont(TEXT_FONT);
			roundLabel.setToolTipText("Must be a postive number greater than 0");
			roundPanel.add(roundLabel);
			roundError = new JLabel("");
			roundError.setFont(TEXT_FONT);
			roundError.setForeground(Color.RED);
			roundError.setHorizontalAlignment(SwingConstants.CENTER);
			roundPanel.add(roundError);
			inputPanel.add(roundPanel);
			roundInput = new JTextField(10);
			inputPanel.add(roundInput);
			
			//Human input panel
			JPanel humanPanel = new JPanel(new GridLayout(1, 2));
			JLabel humanLabel = new JLabel("Enter the number of human players");
			humanLabel.setFont(TEXT_FONT);
			humanLabel.setToolTipText("Must be at least 1 and total players less than " 
					+ Table.MAXNUMPLAYERS);
			humanPanel.add(humanLabel);
			humanError = new JLabel("");
			humanError.setFont(TEXT_FONT);
			humanError.setForeground(Color.RED);
			humanError.setHorizontalAlignment(SwingConstants.CENTER);
			humanPanel.add(humanError);
			inputPanel.add(humanPanel);
			humanInput = new JTextField(10);
			inputPanel.add(humanInput);
			
			//CPU input panel
			JPanel cpuPanel = new JPanel(new GridLayout(1, 2));
			JLabel cpuLabel = new JLabel("Enter the number of CPU players");
			cpuLabel.setFont(TEXT_FONT);
			cpuLabel.setToolTipText("Must be at least 0 and total players less than " 
					+ Table.MAXNUMPLAYERS);
			cpuPanel.add(cpuLabel);
			cpuError = new JLabel("");
			cpuError.setFont(TEXT_FONT);
			cpuError.setForeground(Color.RED);
			cpuError.setHorizontalAlignment(SwingConstants.CENTER);
			cpuPanel.add(cpuError);
			inputPanel.add(cpuPanel);
			cpuInput = new JTextField(10);
			inputPanel.add(cpuInput);
			
			//Button Panel
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 
					REDUCED_BUTTON_HGAP, REDUCED_BUTTON_VGAP));
			buttonPanel.setPreferredSize(new Dimension(getWidth(), 
					REDUCED_BUTTON_PANEL_HEIGHT));
			JGradientButton confirmButton = new JGradientButton("Confirm");
			confirmButton.setFont(BUTTON_FONT);
			confirmButton.setPreferredSize(BUTTON_SIZE);
			confirmButton.setBackground(GREEN);
			confirmButton.addActionListener(confirm);
			confirmButton.setToolTipText("Confirms all inputs");
			buttonPanel.add(confirmButton);
			JGradientButton clearButton = new JGradientButton("Clear");
			clearButton.setFont(BUTTON_FONT);
			clearButton.setPreferredSize(BUTTON_SIZE);
			clearButton.setBackground(RED);
			clearButton.addActionListener(clear);
			clearButton.setToolTipText("Clears all inputs");
			buttonPanel.add(clearButton);
			add(inputPanel, BorderLayout.CENTER);
			add(buttonPanel, BorderLayout.SOUTH);
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
	 * Dialog window displaying general statistics about the creation of the
	 * game.
	 * 
	 * @author Brodie Robertson
	 * @version 1.7.0
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
			setSize(new Dimension(325, 200));
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setModalityType(ModalityType.APPLICATION_MODAL);
			setLayout(new BorderLayout());
			
			//Key bindings
			JRootPane rootPane = getRootPane();
			rootPane.getInputMap().put(ESC, "ESC");
			rootPane.getActionMap().put("ESC", new Close());
			
			final Dimension breakSize = new Dimension(REDUCED_WINDOW.width, 
					REDUCED_WINDOW.height / 15);
			final Font headingFont = new Font("Calibri", Font.BOLD, 20);
			JPanel about = new JPanel();
			about.setLayout(new BoxLayout(about, BoxLayout.Y_AXIS));
			
			JLabel title = new JLabel("Blackjack");
			title.setVerticalAlignment(JLabel.TOP);
			title.setAlignmentX(CENTER_ALIGNMENT);
			title.setFont(headingFont);
			about.add(title);
			
			JLabel version = new JLabel("Version: Beta 1.7.0");
			version.setVerticalAlignment(JLabel.TOP);
			version.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			version.setFont(MAIN_HEADING_FONT);
			about.add(version);
			
			JLabel lastUpdated = new JLabel("Last Updated: 8/1/2017");
			lastUpdated.setVerticalAlignment(JLabel.TOP);
			lastUpdated.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			lastUpdated.setFont(MAIN_HEADING_FONT);
			about.add(lastUpdated);
			
			JLabel breakLabel = new JLabel("");
			breakLabel.setMinimumSize(breakSize);
			breakLabel.setMaximumSize(breakSize);
			breakLabel.setPreferredSize(breakSize);
			breakLabel.setVerticalAlignment(JLabel.TOP);
			breakLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			about.add(breakLabel);
			
			JLabel developed = new JLabel("Developed by Brodie Robertson");
			developed.setVerticalAlignment(JLabel.TOP);
			developed.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			developed.setFont(MAIN_HEADING_FONT);
			about.add(developed);
			
			JLabel tested = new JLabel("Tested by Brodie Robertson");
			tested.setVerticalAlignment(JLabel.TOP);
			tested.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			tested.setFont(MAIN_HEADING_FONT);
			about.add(tested);
			
			add(about, BorderLayout.NORTH);
		}
	}
	
	/**
	 * Dialog window displaying the statistics of the current game.
	 * 
	 * @author Brodie Robertson
	 * @version 1.7.0
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
			setSize(REDUCED_WINDOW);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setModalityType(ModalityType.APPLICATION_MODAL);
			setLayout(new BorderLayout());
			
			//Key bindings
			JRootPane rootPane = getRootPane();
			rootPane.getInputMap().put(ESC, "ESC");
			rootPane.getActionMap().put("ESC", new Close());
			final Dimension breakSize = new Dimension(getWidth(), 
					REDUCED_WINDOW.height / 15);
			
			JPanel statistics = new JPanel();
			statistics.setLayout(new BoxLayout(statistics, BoxLayout.Y_AXIS));
				
			JLabel gameStatistics = new JLabel("Overall Game Statistics:");
			gameStatistics.setFont(MAIN_HEADING_FONT);
			statistics.add(gameStatistics);
			
			//Minimum Wager
			JLabel minWager  = new JLabel("Minimum Wager: $" + df.format(Table.MINWAGER));
			minWager.setFont(MAIN_TEXT_FONT);
			statistics.add(minWager);

			//Maximum Wager
			JLabel maxWager = new JLabel("Maximum Wager: $" + df.format(Table.MAXWAGER));
			maxWager.setFont(MAIN_TEXT_FONT);
			statistics.add(maxWager);
			
			//Minimum Players
			JLabel minNumPlayer = new JLabel("Minimum Players: " + Table.MINNUMPLAYERS);
			minNumPlayer.setFont(MAIN_TEXT_FONT);
			statistics.add(minNumPlayer);
			
			//Minimum Players
			JLabel maxNumPlayer = new JLabel("Maximum Players: " + Table.MAXNUMPLAYERS);
			maxNumPlayer.setFont(MAIN_TEXT_FONT);
			statistics.add(maxNumPlayer);
		
			//Number of Players
			JLabel numPlayer = new JLabel("Number of Players: " 
			+ (table.getPlayers().length - 1));
			numPlayer.setFont(MAIN_TEXT_FONT);
			statistics.add(numPlayer);
			
			//Cards in Deck
			JLabel cardsInFullDeck = new JLabel("Cards in Full Deck: " 
					+ (table.getDeck().getNumOfDecks() * Deck.DECKSIZE));
			cardsInFullDeck.setFont(MAIN_TEXT_FONT);
			statistics.add(cardsInFullDeck);
			
			JLabel cardsLeftinDeck = new JLabel("Cards Remaining in Deck: " 
			+ table.getDeck().getCardsRemaining());
			cardsLeftinDeck.setFont(MAIN_TEXT_FONT);
			statistics.add(cardsLeftinDeck);
			
			//Current Round
			JLabel currentRound = new JLabel("Current Round: " + (table.
					getCurrentRound()));
			currentRound.setFont(MAIN_TEXT_FONT);
			statistics.add(currentRound);
			
			Person[] players = table.getPlayers();
			for(int i = 0; i < players.length; i++)
			{
				if(players[i] instanceof Player)
				{
					Player player = (Player)players[i];
					JLabel statBreak = new JLabel("");
					statBreak.setMinimumSize(breakSize);
					statBreak.setMaximumSize(breakSize);
					statBreak.setPreferredSize(breakSize);
					statistics.add(statBreak);
					
					JLabel header = new JLabel("Player " + (i + 1) + " Statistics:");
					header.setFont(MAIN_HEADING_FONT);
					statistics.add(header);
					
					//Name
					JLabel name = new JLabel("Name: " + player.getName());
					name.setFont(MAIN_TEXT_FONT);
					statistics.add(name);
					
					//Bankrupt
					if(player.getBankrupt())
					{
						JLabel bankrupt = new JLabel("Bankrupt");
						bankrupt.setFont(MAIN_TEXT_FONT);
						statistics.add(bankrupt);
					}
						
					//Current Wager
					JLabel currentWager = new JLabel("Current Wager: $" 
							+ df.format(player.getWager()));
					currentWager.setFont(MAIN_TEXT_FONT);
					statistics.add(currentWager);
					
					//Not split
					if(!player.getHand(0).getSplit())
					{
						JLabel handScore = new JLabel("Hand Score: " + 
								player.getHand(0).getHandScore());
						handScore.setFont(MAIN_TEXT_FONT);
						statistics.add(handScore);
						
						JLabel cardsHeld = new JLabel("Cards Held: " + 
								player.getHand(0).getCardsRemaining());
						cardsHeld.setFont(MAIN_TEXT_FONT);
						statistics.add(cardsHeld);
					}
					//Split
					else
					{
						Hand[] hands = player.getHands();
						for(int j = 0; j < hands.length; j++)
						{
							JLabel handScore = new JLabel("Hand " +	(i + 1) 
									+ "Score " + hands[i].getHandScore());
							handScore.setFont(MAIN_TEXT_FONT);
							statistics.add(handScore);
							
							JLabel cardsHeld = new JLabel("Cards Held in Hand " 
									+ (i + 1) + ":");
							cardsHeld.setFont(MAIN_TEXT_FONT);
							statistics.add(cardsHeld);
						}
					}
					
					//Starting Money
					JLabel startingMoney = new JLabel("Starting Money: $" + df.format(Player.STARTING_MONEY));
					startingMoney.setFont(MAIN_TEXT_FONT);
					statistics.add(startingMoney);
					
					//Total Money
					JLabel totalMoney = new JLabel("Total Money: $" 
							+ df.format(player.getTotalMoney()));
					totalMoney.setFont(MAIN_TEXT_FONT);
					statistics.add(totalMoney);
					
					//Total Winnings
					JLabel totalWinnings = new JLabel("Total Winnings: $" 
							+ df.format(player.getTotalWinnings()));
					totalWinnings.setFont(MAIN_TEXT_FONT);
					statistics.add(totalWinnings);
					
					//Total Wagers
					JLabel totalWagers = new JLabel("Total Wagers: $" 
							+ df.format(player.getTotalWager()));
					totalWagers.setFont(MAIN_TEXT_FONT);
					statistics.add(totalWagers);
					
					//Total Insurance
					JLabel totalInsurance = new JLabel("Total Insurance: $" 
							+ df.format(player.getTotalInsurance()));
					totalInsurance.setFont(MAIN_TEXT_FONT);
					statistics.add(totalInsurance);
					
					//Wins
					JLabel win = new JLabel("Wins: " + player.getWin());
					win.setFont(MAIN_TEXT_FONT);
					statistics.add(win);
					
					//Losses
					JLabel loss = new JLabel("Losses: " + player.getLoss());
					loss.setFont(MAIN_TEXT_FONT);
					statistics.add(loss);
					
					//Pushes
					JLabel push = new JLabel("Pushes: " + player.getPush());
					push.setFont(MAIN_TEXT_FONT);
					statistics.add(push);
					
					//Blackjack
					JLabel blackjack = new JLabel("Blackjack: " + player.
							getBlackjack());
					blackjack.setFont(MAIN_TEXT_FONT);
					statistics.add(blackjack);
					
					//Busts
					JLabel bust = new JLabel("Busts: " + player.getBust());
					bust.setFont(MAIN_TEXT_FONT);
					statistics.add(bust);
					
					//Surrenders
					JLabel surrender = new JLabel("Surrenders: " + player.getSurrender());
					surrender.setFont(MAIN_TEXT_FONT);
					statistics.add(surrender);
				}
				else
				{
					Person dealer = players[i];
					JLabel statBreak = new JLabel("");
					statBreak.setMinimumSize(breakSize);
					statBreak.setMaximumSize(breakSize);
					statBreak.setPreferredSize(breakSize);
					statistics.add(statBreak);
					
					JLabel header = new JLabel("Dealer Statistics");
					header.setFont(MAIN_HEADING_FONT);
					statistics.add(header);
					
					//Name
					JLabel name = new JLabel("Name: " + dealer.getName());
					name.setFont(MAIN_TEXT_FONT);
					statistics.add(name);
					
					//Hand Score
					JLabel handScore = new JLabel("Hand Score: " + dealer.
							getHand(0).getHandScore());
					handScore.setFont(MAIN_TEXT_FONT);
					statistics.add(handScore);
					
					//Cards Held
					JLabel cardsHeld = new JLabel("Cards Held: " + dealer.
							getHand(0).getCardsRemaining());
					cardsHeld.setFont(MAIN_TEXT_FONT);
					statistics.add(cardsHeld);
				}
			}

			JScrollPane scrollPane = new JScrollPane(statistics);
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.
					HORIZONTAL_SCROLLBAR_NEVER);
			add(scrollPane);
		}
	}
	
	/**
	 * Dialog window allowing the player to change the speed of the game.
	 * 
	 * @author Brodie Robertson
	 * @version 1.6.0
	 * @since 1.6.0
	 */
	private class GameSpeedWindow extends JDialog
	{
		/**
		 * Modifies the turn delay when the slider is moved.
		 * 
		 * @author Brodie Robertson
		 * @since 1.6.0
		 */
		private class SliderMoved implements ChangeListener
		{
			/**
			 * Activates when this object recieves a ChangeEvent.
			 * 
			 * (non-Javadoc)
			 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
			 * @since 1.6.0
			 */
			@Override
			public void stateChanged(ChangeEvent e) 
			{
				JSlider source = (JSlider)e.getSource();
				if(source.getValueIsAdjusting())
				{
					turnDelay = source.getValue();
				}
			}
		}
		
		/**
		 * Constructs a GameSpeedWindow with a default layout.
		 * 
		 * @since 1.6.0
		 */
		public GameSpeedWindow()
		{
			setTitle("Game Speed");
			setSize(new Dimension(REDUCED_WINDOW.width, 125));
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setModalityType(ModalityType.APPLICATION_MODAL);
			setLayout(new BorderLayout());
			
			//Key bindings
			JRootPane rootPane = getRootPane();
			rootPane.getInputMap().put(ESC, "ESC");
			rootPane.getActionMap().put("ESC", new Close());
			
			JPanel gameSpeed = new JPanel();
			gameSpeed.setLayout(new BoxLayout(gameSpeed, BoxLayout.Y_AXIS));
			JLabel header = new JLabel("Game Speed");
			
			header.setAlignmentX(CENTER_ALIGNMENT);
			header.setFont(MAIN_HEADING_FONT);
			gameSpeed.add(header);
			
			final int maxTurnDelay = 3000;
			final int minTurnDelay = 500;
			
			JSlider gameSpeedSlider = new JSlider(JSlider.HORIZONTAL, minTurnDelay, 
					maxTurnDelay, turnDelay);
			gameSpeedSlider.addChangeListener(new SliderMoved());
			gameSpeedSlider.setSnapToTicks(true);
			gameSpeedSlider.setMajorTickSpacing(500);
			gameSpeedSlider.setMinorTickSpacing(100);
			gameSpeedSlider.setPaintLabels(true);
			gameSpeedSlider.setPaintTicks(true);
			gameSpeedSlider.setFont(MAIN_TEXT_FONT);
			gameSpeed.add(gameSpeedSlider);
			
			add(gameSpeed);
		}
	}
	
	/**
	 * Used to draw a player's hand onto the display.
	 * 
	 * @author Brodie Robertson
	 * @version 1.7.0
	 * @since 1.2.0
	 */
	private class HandPanel extends JPanel
	{
		/**
		 * An array of labels displaying cards.
		 */
		private GUICard[] cards;
		/**
		 * Number of rows in a hand panel.
		 */
		private int row = 2;
		/**
		 * Number of columns in a hand panel.
		 */
		private int col = 4;
		
		/**
		 * Constructs a HandPanel with a default layout, blank upon creation.
		 * 
		 * @param hand The persom's hand.
		 * @since 1.2.0
		 */
		public HandPanel(Hand hand)
		{
			setLayout(new GridLayout(row, col));
			cards = new GUICard[row*col];
			for(int i = 0; i < cards.length; i++)
			{
				GUICard card = new GUICard();
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
			for(int i = 0; i < row*col; i++)
			{
				if(i < hand.getCardsRemaining())
				{
					cards[i].card = hand.getCard(i);	
				}
				else
				{
					cards[i].card = null;
				}
				cards[i].repaint();
			}
		}
	}
	
	/**
	 * Used to draw a card to the screen.
	 * 
	 * @author Brodie Robertson
	 * @version 1.7.0
	 * @since 1.7.0
	 */
	private class GUICard extends JLabel
	{
		/**
		 * Card being drawn.
		 */
		private Card card;
		
		/**
		 * Paints the card to the screen.
	     * 
		 * (non-Javadoc)
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 * @since 1.7.0
		 */
		@Override
		protected void paintComponent(Graphics g) 
		{
			//If the there is a card in this spot draw a card
			if(card != null)
			{
				final int distFromEdge = 5;
				Graphics2D g2 = (Graphics2D)g.create();
	    		g2.setPaint(Color.WHITE);
				g2.drawRoundRect(distFromEdge, distFromEdge, getWidth() - (distFromEdge*2), 
						getHeight() - (distFromEdge*2), 6, 6);
				g2.fillRoundRect(distFromEdge, distFromEdge, getWidth() - (distFromEdge*2), 
						getHeight() - (distFromEdge*2), 6, 6);
				
				//If the card is face up draw front
	    		if(card.getFaceUp())
	    		{
	    			//Draw Ace
	    			if(card.getFace() == Face.ACE)
	    			{
	    				//Draw Diamond
		    			if(card.getSuit() == Suit.DIAMONDS)
		    			{
			    			drawDiamond(g2, (getWidth()/2) - 6, (getHeight()/2) - 6);
		    			}
		    			//Draw Heart
		    			else if(card.getSuit() == Suit.HEARTS)
		    			{
			    			drawHeart(g2, (getWidth()/2) - 6, (getHeight()/2) - 6);
		    			}
		    			//Draw Club
		    			else if(card.getSuit() == Suit.CLUBS)
		    			{
		    				drawClub(g2, (getWidth()/2) - 6, (getHeight()/2) - 6);
		    			}
		    			//Draw Spade
		    			else
		    			{
		    				drawSpade(g2, (getWidth()/2) - 6, (getHeight()/2) - 6);
		    			}
	    			}
	    			//Draw Two
	    			else if(card.getFace() == Face.TWO)
	    			{
	    				//Draw Diamond
		    			if(card.getSuit() == Suit.DIAMONDS)
		    			{
			    			drawDiamond(g2, (getWidth()/2) - 6, (int)(getHeight()*0.8) - 6);
			    			drawDiamond(g2, (getWidth()/2) - 6, (int)(getHeight()*0.2) - 6);
		    			}
		    			//Draw Heart
		    			else if(card.getSuit() == Suit.HEARTS)
		    			{
			    			drawHeart(g2, (getWidth()/2) - 6, (int)(getHeight()*0.8) - 6);
			    			drawHeart(g2, (getWidth()/2) - 6, (int)(getHeight()*0.2) - 6);
		    			}
		    			//Draw Club
		    			else if(card.getSuit() == Suit.CLUBS)
		    			{
		    				drawClub(g2, (getWidth()/2) - 6, (int)(getHeight()*0.8) - 6);
			    			drawClub(g2, (getWidth()/2) - 6, (int)(getHeight()*0.2) - 6);
		    			}
		    			//Draw Spade
		    			else
		    			{
		    				drawSpade(g2, (getWidth()/2) - 6, (int)(getHeight()*0.8) - 6);
			    			drawSpade(g2, (getWidth()/2) - 6, (int)(getHeight()*0.2) - 6);
		    			}
	    			}
	    			//Draw Three
	    			else if(card.getFace() == Face.THREE)
	    			{
	    				//Draw Diamond
		    			if(card.getSuit() == Suit.DIAMONDS)
		    			{
		    				drawDiamond(g2, (getWidth()/2) - 6, (int)(getHeight()*0.8) - 6);
		    				drawDiamond(g2, (getWidth()/2) - 6, (int)(getHeight()*0.5) - 6);
			    			drawDiamond(g2, (getWidth()/2) - 6, (int)(getHeight()*0.2) - 6);
		    			}
		    			//Draw Heart
		    			else if(card.getSuit() == Suit.HEARTS)
		    			{
		    				drawHeart(g2, (getWidth()/2) - 6, (int)(getHeight()*0.8) - 6);
		    				drawHeart(g2, (getWidth()/2) - 6, (int)(getHeight()*0.5) - 6);
			    			drawHeart(g2, (getWidth()/2) - 6, (int)(getHeight()*0.2) - 6);
		    			}
		    			//Draw Club
		    			else if(card.getSuit() == Suit.CLUBS)
		    			{
		    				drawClub(g2, (getWidth()/2) - 6, (int)(getHeight()*0.8) - 6);
		    				drawClub(g2, (getWidth()/2) - 6, (int)(getHeight()*0.5) - 6);
			    			drawClub(g2, (getWidth()/2) - 6, (int)(getHeight()*0.2) - 6);
		    			}
		    			//Draw Spade
		    			else
		    			{
		    				drawSpade(g2, (getWidth()/2) - 6, (int)(getHeight()*0.8) - 6);
		    				drawSpade(g2, (getWidth()/2) - 6, (int)(getHeight()*0.5) - 6);
			    			drawSpade(g2, (getWidth()/2) - 6, (int)(getHeight()*0.2) - 6);
		    			}
	    			}
	    			//Draw Four
	    			else if(card.getFace() == Face.FOUR)
	    			{
	    				//Draw Diamond
		    			if(card.getSuit() == Suit.DIAMONDS)
		    			{
		    				drawDiamond(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
		    			}
		    			//Draw Heart
		    			else if(card.getSuit() == Suit.HEARTS)
		    			{
		    				drawHeart(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
		    			}
		    			//Draw Club
		    			else if(card.getSuit() == Suit.CLUBS)
		    			{
		    				drawClub(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawClub(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawClub(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
		    				drawClub(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
		    			}
		    			//Draw Spade
		    			else
		    			{
		    				drawSpade(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
		    			}
	    			}
	    			//Draw Five
	    			else if(card.getFace() == Face.FIVE)
	    			{
	    				//Draw Diamond
		    			if(card.getSuit() == Suit.DIAMONDS)
		    			{
		    				drawDiamond(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.5) - 6);
		    				
		    			}
		    			//Draw Heart
		    			else if(card.getSuit() == Suit.HEARTS)
		    			{
		    				drawHeart(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.5) - 6);
		    			}
		    			//Draw Club
		    			else if(card.getSuit() == Suit.CLUBS)
		    			{
		    				drawClub(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawClub(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawClub(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
		    				drawClub(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
		    				drawClub(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.5) - 6);
		    			}
		    			//Draw Spade
		    			else
		    			{
		    				drawSpade(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.5) - 6);
		    			}
	    			}
	    			//Draw Six
	    			else if(card.getFace() == Face.SIX)
	    			{
	    				//Draw Diamond
		    			if(card.getSuit() == Suit.DIAMONDS)
		    			{
		    				drawDiamond(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.5) - 6);
			    			drawDiamond(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
			    			drawDiamond(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.5) - 6);
			    			drawDiamond(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
		    			}
		    			//Draw Heart
		    			else if(card.getSuit() == Suit.HEARTS)
		    			{
		    				drawHeart(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.5) - 6);
			    			drawHeart(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
			    			drawHeart(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.5) - 6);
			    			drawHeart(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
		    			}
		    			//Draw Club
		    			else if(card.getSuit() == Suit.CLUBS)
		    			{
		    				drawClub(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawClub(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.5) - 6);
			    			drawClub(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
			    			drawClub(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawClub(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.5) - 6);
			    			drawClub(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
		    			}
		    			//Draw Spade
		    			else
		    			{
		    				drawSpade(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.5) - 6);
			    			drawSpade(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
			    			drawSpade(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.5) - 6);
			    			drawSpade(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
		    			}
	    			}
	    			//Draw Seven
	    			else if(card.getFace() == Face.SEVEN)
	    			{
	    				//Draw Diamond
		    			if(card.getSuit() == Suit.DIAMONDS)
		    			{
		    				drawDiamond(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.5) - 6);
			    			drawDiamond(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
			    			drawDiamond(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.5) - 6);
			    			drawDiamond(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
			    			drawDiamond(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.35) - 6);
			    			
		    			}
		    			//Draw Heart
		    			else if(card.getSuit() == Suit.HEARTS)
		    			{
		    				drawHeart(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.5) - 6);
			    			drawHeart(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
			    			drawHeart(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.5) - 6);
			    			drawHeart(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
			    			drawHeart(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.35) - 6);
		    			}
		    			//Draw Club
		    			else if(card.getSuit() == Suit.CLUBS)
		    			{
		    				drawClub(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawClub(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.5) - 6);
			    			drawClub(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
			    			drawClub(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawClub(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.5) - 6);
			    			drawClub(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
			    			drawClub(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.35) - 6);
		    			}
		    			//Draw Spade
		    			else
		    			{
		    				drawSpade(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.5) - 6);
			    			drawSpade(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
			    			drawSpade(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.5) - 6);
			    			drawSpade(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
			    			drawSpade(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.35) - 6);
		    			}
	    			}
	    			//Draw Eight
	    			else if(card.getFace() == Face.EIGHT)
	    			{
	    				//Draw Diamond
		    			if(card.getSuit() == Suit.DIAMONDS)
		    			{
		    				drawDiamond(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.5) - 6);
			    			drawDiamond(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
			    			drawDiamond(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.5) - 6);
			    			drawDiamond(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
			    			drawDiamond(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.35) - 6);
			    			drawDiamond(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.65) - 6);
		    			}
		    			//Draw Heart
		    			else if(card.getSuit() == Suit.HEARTS)
		    			{
		    				drawHeart(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.5) - 6);
			    			drawHeart(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
			    			drawHeart(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.5) - 6);
			    			drawHeart(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
			    			drawHeart(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.35) - 6);
			    			drawHeart(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.65) - 6);
		    			}
		    			//Draw Club
		    			else if(card.getSuit() == Suit.CLUBS)
		    			{
		    				drawClub(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawClub(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.5) - 6);
			    			drawClub(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
			    			drawClub(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawClub(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.5) - 6);
			    			drawClub(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
			    			drawClub(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.35) - 6);
			    			drawClub(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.65) - 6);
		    			}
		    			//Draw Spade
		    			else
		    			{
		    				drawSpade(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.5) - 6);
			    			drawSpade(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
			    			drawSpade(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.5) - 6);
			    			drawSpade(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
			    			drawSpade(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.35) - 6);
			    			drawSpade(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.65) - 6);
		    			}
	    			}
	    			//Draw Nine
	    			else if(card.getFace() == Face.NINE)
	    			{
	    				//Draw Diamond
		    			if(card.getSuit() == Suit.DIAMONDS)
		    			{
		    				drawDiamond(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.6) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.4) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.6) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.4) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.5) - 5);
		    			}
		    			//Draw Heart
		    			else if(card.getSuit() == Suit.HEARTS)
		    			{
		    				drawHeart(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.6) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.4) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.6) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.4) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.5) - 5);
		    			}
		    			//Draw Club
		    			else if(card.getSuit() == Suit.CLUBS)
		    			{
		    				drawClub(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawClub(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.6) - 6);
		    				drawClub(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.4) - 6);
		    				drawClub(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
		    				drawClub(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawClub(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.6) - 6);
		    				drawClub(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.4) - 6);
		    				drawClub(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
		    				drawClub(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.5) - 5);
		    			}
		    			//Draw Spade
		    			else
		    			{
		    				drawSpade(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.6) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.4) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.6) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.4) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.5) - 5);
		    			}
	    			}
	    			//Draw Ten
	    			else if(card.getFace() == Face.TEN)
	    			{
	    				//Draw Diamond
		    			if(card.getSuit() == Suit.DIAMONDS)
		    			{
		    				drawDiamond(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.6) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.4) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.6) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.4) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
		    				drawDiamond(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.3) - 5);
		    				drawDiamond(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.7) - 5);
		    			}
		    			//Draw Heart
		    			else if(card.getSuit() == Suit.HEARTS)
		    			{
		    				drawHeart(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.6) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.4) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.6) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.4) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
		    				drawHeart(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.3) - 5);
		    				drawHeart(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.7) - 5);
		    			}
		    			//Draw Club
		    			else if(card.getSuit() == Suit.CLUBS)
		    			{
		    				drawClub(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawClub(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.6) - 6);
		    				drawClub(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.4) - 6);
		    				drawClub(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
		    				drawClub(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawClub(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.6) - 6);
		    				drawClub(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.4) - 6);
		    				drawClub(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
		    				drawClub(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.3) - 5);
		    				drawClub(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.7) - 5);
		    			}
		    			//Draw Spade
		    			else
		    			{
		    				drawSpade(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.8) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.6) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.4) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.7) - 6, (int)(getHeight()*0.2) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.8) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.6) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.4) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.3) - 6, (int)(getHeight()*0.2) - 6);
		    				drawSpade(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.3) - 5);
		    				drawSpade(g2, (int)(getWidth()*0.5) - 6, (int)(getHeight()*0.7) - 5);
		    			}
	    			}
	    			//Draw Jack, Queen or King
	    			else if(card.getFace() == Face.JACK || card.getFace() == Face.QUEEN
	    					|| card.getFace() == Face.KING)
	    			{
	    				if(card.getColour() == Colour.RED)
	    				{
	    					g2.setPaint(Color.RED);
	    				}
	    				else
	    				{
	    					g2.setPaint(Color.BLACK);
	    				}
	    				
	    				final Font smallSize = new Font("Bell MT", Font.PLAIN, 72);
	    				final Font largeSize = new Font("Bell MT", Font.PLAIN, 90);
	    				
	    				//Draw Diamond
		    			if(card.getSuit() == Suit.DIAMONDS)
		    			{
			    			drawDiamond(g2, (int)(getWidth()*0.2) - 6, (int)(getHeight()*0.125) - 6);
			    			drawDiamond(g2, (int)(getWidth()*0.8) - 6, (int)(getHeight()*0.875) - 6);
		    			}
		    			//Draw Heart
		    			else if(card.getSuit() == Suit.HEARTS)
		    			{
		    				drawHeart(g2, (int)(getWidth()*0.2) - 6, (int)(getHeight()*0.125) - 6);
			    			drawHeart(g2, (int)(getWidth()*0.8) - 6, (int)(getHeight()*0.875) - 6);
		    			}
		    			//Draw Club
		    			else if(card.getSuit() == Suit.CLUBS)
		    			{
		    				drawClub(g2, (int)(getWidth()*0.2) - 6, (int)(getHeight()*0.125) - 6);
			    			drawClub(g2, (int)(getWidth()*0.8) - 6, (int)(getHeight()*0.875) - 6);
		    			}
		    			//Draw Spade
		    			else
		    			{
		    				drawSpade(g2, (int)(getWidth()*0.2) - 6, (int)(getHeight()*0.125) - 6);
			    			drawSpade(g2, (int)(getWidth()*0.8) - 6, (int)(getHeight()*0.875) - 6);
		    			}
		    			
		    			//Draw Jack
		    			if(card.getFace() == Face.JACK)
		    			{
		    				g2.setFont(largeSize);
		    				g2.drawString("J", 22, 93);
		    			}
		    			//Draw Queen
		    			else if(card.getFace() == Face.QUEEN)
		    			{
		    				g2.setFont(smallSize);
		    				g2.drawString("Q", 12, 78);
		    			}
		    			//Draw King
		    			else
		    			{
		    				g2.setFont(largeSize);
		    				g2.drawString("K", 8, 93);
		    			}
	    			}
	    		}
	    		//If the card is face down draw the back
	    		else
	    		{
	    			int rectX = distFromEdge*2;
	    			int rectY = distFromEdge*2;
	    			int cardWidth = getWidth();
	    			int cardHeight = getHeight();
	    			g2.setPaint(new Color(36, 46, 133));
	    			g2.drawRect(rectX, rectY, getWidth() - (rectX * 2), 
	    					getHeight() - (rectY * 2));
	    			
	    			g2.setPaint(new Color(40, 53, 135));
	    			
	    			//Main vertical line
	    			int[] xPoints = new int[]{(int)(cardWidth*0.55), (int)(cardWidth*0.45), 
	    					(int)(cardWidth*0.45), (int)(cardWidth*0.55)};
	    			int[] yPoints = new int[]{rectY, rectY, cardHeight - rectY, cardHeight - rectY};
	    			g2.drawPolygon(xPoints, yPoints, xPoints.length);
	    			g2.fillPolygon(xPoints, yPoints, xPoints.length);
	    			
	    			//Top vertical line
	    			xPoints = new int[]{(int)(cardWidth*0.7), (int)(cardWidth*0.8), 
	    					(int)(cardWidth*0.8), (int)(cardWidth*0.7)};
	    			yPoints = new int[]{rectY, rectY, (int)(cardHeight*0.535), 
	    					(int)(cardHeight*0.535)};
	    			g2.drawPolygon(xPoints, yPoints, xPoints.length);
	    			g2.fillPolygon(xPoints, yPoints, xPoints.length);
	    			
	    			//Bottom vertical line
	    			xPoints = new int[]{(int)(cardWidth*0.2), (int)(cardWidth*0.3), 
	    					(int)(cardWidth*0.3), (int)(cardWidth*0.2)};
	    			yPoints = new int[]{(int)(cardHeight*0.465), (int)(cardHeight*0.465), 
	    					cardHeight - rectY, cardHeight - rectY};
	    			g2.drawPolygon(xPoints, yPoints, xPoints.length);
	    			g2.fillPolygon(xPoints, yPoints, xPoints.length);
	    			
	    			//Main horizontal line
	    			xPoints = new int[]{rectX, rectX, cardWidth - rectX, cardWidth - rectX};
	    			yPoints = new int[]{(int)(cardHeight*0.535), (int)(cardHeight*0.465), 
	    					(int)(cardHeight*0.465), (int)(cardHeight*0.535)};
	    			g2.drawPolygon(xPoints, yPoints, xPoints.length);
	    			g2.fillPolygon(xPoints, yPoints, xPoints.length);
	    			
	    			//Top horizontal line
	    			xPoints = new int[]{rectX, rectX, (int)(cardWidth*0.55), 
	    					(int)(cardWidth*0.55)};
	    			yPoints = new int[]{(int)(cardHeight*0.23), (int)(cardHeight*0.3), 
	    					(int)(cardHeight*0.3), (int)(cardHeight*0.23)};
	    			g2.drawPolygon(xPoints, yPoints, xPoints.length);
	    			g2.fillPolygon(xPoints, yPoints, xPoints.length);
	    			
	    			//Bottom horizontal line
	    			xPoints = new int[]{(int)(cardWidth*0.55), (int)(cardWidth*0.55), 
	    					cardWidth - rectX, cardWidth - rectX};
	    			yPoints = new int[]{(int)(cardHeight*0.69), (int)(cardHeight*0.76),
	    					(int)(cardHeight*0.76),(int)(cardHeight*0.69)};
	    			g2.drawPolygon(xPoints, yPoints, xPoints.length);
	    			g2.fillPolygon(xPoints, yPoints, xPoints.length);
	    		}
	    		g2.dispose();
			}
			super.paintComponent(g);
		}
		
		/**
		 * Draws a diamond to the screen at a specified location.
		 * 
		 * @param g graphics object to use.
		 * @param x position of the diamond.
		 * @param y position of the diamond.
		 * @since 1.7.0
		 */
		private void drawDiamond(Graphics2D g, int x, int y)
		{
			int[] xPoints = new int[]{6 + x, 12 + x, 6 + x, 0 + x, 6 + x};
			int[] yPoints = new int[]{0 + y, 6 + y, 12 + y, 6 + y, 0 + y};
			
			g.setPaint(Color.RED);
			g.drawPolygon(xPoints, yPoints, xPoints.length);
			g.fillPolygon(xPoints, yPoints, xPoints.length);
		}
		
		/**
		 * Draws a heart to the screen at a specified location.
		 * 
		 * @param g graphics object to use.
		 * @param x position of the heart.
		 * @param y position of the heart.
		 * @since 1.7.0
		 */
		private void drawHeart(Graphics2D g, int x, int y)
		{
			g.setPaint(Color.RED);
			g.drawOval(0 + x, 0 + y, 6, 6);
			g.fillOval(0 + x, 0 + y, 6, 6);
			g.drawOval(6 + x, 0 + y, 6, 6);
			g.fillOval(6 + x, 0 + y, 6, 6);
			
			int[] xPoints = new int[]{0 + x, 12 + x, 6 + x, 0 + x};
			int[] yPoints = new int[]{3 + y, 3 + y, 12 + y, 3 + y};
			g.drawPolygon(xPoints, yPoints, xPoints.length);
			g.fillPolygon(xPoints, yPoints, xPoints.length);
		}
		
		/**
		 * Draws a spade to the screen at a specified location.
		 * 
		 * @param g graphics object to use.
		 * @param x position of the spade.
		 * @param y position of the spade.
		 * @since 1.7.0
		 */
		private void drawSpade(Graphics2D g, int x, int y)
		{
			g.setPaint(Color.BLACK);
			g.drawOval(0 + x, 6 + y, 6, 6);
			g.fillOval(0 + x, 6 + y, 6, 6);
			g.drawOval(6 + x, 6 + y, 6, 6);
			g.fillOval(6 + x, 6 + y, 6, 6);
			
			int[] xPoints = new int[]{0 + x, 6 + x, 12 + x, 0 + x};
			int[] yPoints = new int[]{9 + y, 0 + y, 9 + y, 9 + y};
			g.drawPolygon(xPoints, yPoints, xPoints.length);
			g.fillPolygon(xPoints, yPoints, xPoints.length);
			
			xPoints = new int[]{6 + x, 4 + x, 8 + x, 6 + x};
			yPoints = new int[]{9 + y, 15 + y, 15 + y, 9 + y};
			g.drawPolygon(xPoints, yPoints, xPoints.length);
			g.fillPolygon(xPoints, yPoints, xPoints.length);
		}
		
		/**
		 * Draws a club to the screen at a specified location.
		 * 
		 * @param g graphics object to use.
		 * @param x position of the club.
		 * @param y position of the club.
		 */
		private void drawClub(Graphics2D g, int x, int y)
		{
			g.setPaint(Color.BLACK);
			g.drawOval(3 + x, 0 + y, 6, 6);
			g.fillOval(3 + x, 0 + y, 6, 6);
			g.drawOval(0 + x, 5 + y, 6, 6);
			g.fillOval(0 + x, 5 + y, 6, 6);
			g.drawOval(6 + x, 5 + y, 6, 6);
			g.fillOval(6 + x, 5 + y, 6, 6);
			
			int[] xPoints = new int[]{6 + x, 4 + x, 8 + x, 6 + x};
			int[] yPoints = new int[]{8 + y, 14 + y, 14 + y, 8 + y};
			g.drawPolygon(xPoints, yPoints, xPoints.length);
			g.fillPolygon(xPoints, yPoints, xPoints.length);
		}
	}
	
	/**
	 * Used to draw a players statistics on the screen, including their hand.
	 * 
	 * @author Brodie Robertson
	 * @version 1.7.0
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
		 * Array of panel used to display the players hands.
		 */
		private HandPanel[] hands;
		/**
		 * Panel containing the all of the players hands.
		 */
		private JPanel handList;	
		/**
		 * Whether the second hand has been added to the hands array.
		 */
		private boolean splitHand;
		
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
			nameLabel.setFont(MAIN_HEADING_FONT);
			nameLabel.setHorizontalAlignment(JLabel.CENTER);
			namePanel.add(nameLabel);
			JLabel name = new JLabel(player.getName());
			name.setFont(MAIN_TEXT_FONT);
			name.setHorizontalAlignment(JLabel.CENTER);
			name.setToolTipText("The player's name");
			namePanel.add(name);
			statisticsPanel.add(namePanel);
			
			//Total Money
			JPanel totalMoneyPanel = new JPanel(new GridLayout(2, 1));
			JLabel totalMoneyLabel = new JLabel("Total Money");
			totalMoneyLabel.setFont(MAIN_HEADING_FONT);
			totalMoneyLabel.setHorizontalAlignment(JLabel.CENTER);
			totalMoneyPanel.add(totalMoneyLabel);
			totalMoney = new JLabel("$" + df.format(player.getTotalMoney()));
			totalMoney.setFont(MAIN_TEXT_FONT);
			totalMoney.setHorizontalAlignment(JLabel.CENTER);
			totalMoney.setToolTipText(player.getName() + "'s total money");
			totalMoneyPanel.add(totalMoney);
			statisticsPanel.add(totalMoneyPanel);
			
			//Wager
			JPanel wagerPanel = new JPanel(new GridLayout(2, 1));
			JLabel wagerLabel = new JLabel("Wager");
			wagerLabel.setFont(MAIN_HEADING_FONT);
			wagerLabel.setHorizontalAlignment(JLabel.CENTER);
			wagerPanel.add(wagerLabel);
			wager = new JLabel("$0.00");
			wager.setFont(MAIN_TEXT_FONT);
			wager.setHorizontalAlignment(JLabel.CENTER);
			wager.setToolTipText(player.getName() + "'s wager");
			wagerPanel.add(wager);
			statisticsPanel.add(wagerPanel);
			
			//First Hand Score
			JPanel[] scorePanels = new JPanel[2];
			scoreLabels = new JLabel[2];
			scores = new JLabel[2];
			scorePanels[0] = new JPanel(new GridLayout(2, 1));
			scoreLabels[0] = new JLabel("Hand Score");
			scoreLabels[0].setFont(MAIN_HEADING_FONT);
			scoreLabels[0].setHorizontalAlignment(JLabel.CENTER);
			scorePanels[0].add(scoreLabels[0]);
			scores[0] = new JLabel("" + player.getHand(0).getHandScore());
			scores[0].setFont(MAIN_TEXT_FONT);
			scores[0].setHorizontalAlignment(JLabel.CENTER);
			scores[0].setToolTipText(player.getName() + " hand score");
			scorePanels[0].add(scores[0]);
			statisticsPanel.add(scorePanels[0]);
			
			//Extra
			JPanel extraPanel = new JPanel(new GridLayout(2, 1));
			extraLabel = new JLabel("");
			extraLabel.setFont(MAIN_HEADING_FONT);
			extraLabel.setHorizontalAlignment(JLabel.CENTER);
			extraPanel.add(extraLabel);
			extra = new JLabel("");
			extra.setFont(MAIN_TEXT_FONT);
			extra.setHorizontalAlignment(JLabel.CENTER);
			extraPanel.add(extra);
			statisticsPanel.add(extraPanel);
			
			//Second Hand Score
			scorePanels[1] = new JPanel(new GridLayout(2, 1));
			scoreLabels[1] = new JLabel("");
			scoreLabels[1].setFont(MAIN_HEADING_FONT);
			scoreLabels[1].setHorizontalAlignment(JLabel.CENTER);
			scorePanels[1].add(scoreLabels[1]);
			scores[1] = new JLabel("");
			scores[1].setFont(MAIN_TEXT_FONT);
			scores[1].setHorizontalAlignment(JLabel.CENTER);
			scorePanels[1].add(scores[1]);
			statisticsPanel.add(scorePanels[1]);
			add(statisticsPanel);
			
			//Hand Panel
			JPanel fullHandPanel = new JPanel(new BorderLayout());
			handList = new JPanel();
			handList.setLayout(new GridLayout(0, 1));
			
			hands = new HandPanel[2];
			hands[0] = new HandPanel(player.getHand(0));
			hands[0].setToolTipText("Cards in hand: " + player.getHand(0).getCardsRemaining());
			handList.add(hands[0]);

			JScrollPane handScrollPane = new JScrollPane(handList);
			handScrollPane.setHorizontalScrollBarPolicy(JScrollPane.
					HORIZONTAL_SCROLLBAR_NEVER);
			handScrollPane.setVerticalScrollBarPolicy(JScrollPane.
					VERTICAL_SCROLLBAR_ALWAYS);
			fullHandPanel.add(handScrollPane);
			add(fullHandPanel);
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
			totalMoney.setText("$" + df.format(player.getTotalMoney()));
			wager.setText("$" + df.format(player.getWager()));
			scores[0].setText("" + player.getHand(0).getHandScore());
			
			//Hand not split
			if(player.getHand(0).getSplit() && !splitHand)
			{
				scoreLabels[0].setText("Hand 1 Score");
				scoreLabels[1].setText("Hand 2 Score");
				scores[1].setText("" + player.getHand(1).getHandScore());
				
				hands[1] = new HandPanel(player.getHand(0));
				hands[1].setToolTipText("Cards in hand: " + player.getHand(0).
						getCardsRemaining());
				hands[1].setPreferredSize(new Dimension(0, handList.getHeight()));
				hands[1].setBorder(new MatteBorder(1, 0, 0, 0, Color.DARK_GRAY));
				hands[1].updatePanel(index, 1);
				handList.add(hands[1]);
				splitHand = true;
			}
			//Hand split
			else if(player.getHand(0).getSplit())
			{
				scores[1].setText("" + player.getHand(1).getHandScore());
				hands[1].updatePanel(index, 1);
				hands[1].setToolTipText("Cards in hand: " + player.getHand(0).
						getCardsRemaining());
			}
			
			//Took insurance
			if(player.getTookInsurance())
			{
				extraLabel.setText("Insurance");
				extra.setText("$" + df.format(player.getInsurance()));
				extra.setToolTipText(player.getName() + "'s insurance");
			}
			//Bankrupt
			else if(player.getBankrupt())
			{
				extraLabel.setText("Bankrupt");
				extra.setText("");
				extra.setToolTipText("");
			}
			//No extra property
			else
			{
				extraLabel.setText("");
				extra.setText("");
				extra.setToolTipText("");
			}
			
			hands[0].setToolTipText("Cards in hand: " + player.getHand(0).
					getCardsRemaining());
			hands[0].updatePanel(index, 0);
		}
		
		/**
		 * Resets the panel to a default state.
		 * 
		 * @param index Index of the player
		 */
		public void resetPanel(int index)
		{
			extraLabel.setText("");
			extra.setText("");
			extra.setToolTipText("");
			scores[0].setText("0");
			hands[0].setToolTipText("Cards in hand: " + 0);
			hands[0].updatePanel(index, 0);
			splitHand = false;
			if(hands[1] != null)
			{
				hands[1] = null;
				scoreLabels[1].setText("");
				scores[1].setText("");
				handList.remove(1);
				handList.revalidate();
				handList.repaint();
			}
		}
	}
	
	/**
	 * Used to draw the dealer's statistics onto the display.
	 * 
	 * @author Brodie Robertson
	 * @version 1.4.3
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
			nameLabel.setFont(MAIN_HEADING_FONT);
			nameLabel.setHorizontalAlignment(JLabel.CENTER);
			namePanel.add(nameLabel);
			JLabel name = new JLabel(dealer.getName());
			name.setFont(MAIN_TEXT_FONT);
			name.setHorizontalAlignment(JLabel.CENTER);
			name.setToolTipText("The dealer's name");
			namePanel.add(name);
			statisticsPanel.add(namePanel);
			add(statisticsPanel);
			
			//Hand Score
			JPanel scorePanel = new JPanel(new GridLayout(2, 1));
			JLabel scoreLabel = new JLabel("Hand Score");
			scoreLabel.setFont(MAIN_HEADING_FONT);
			scoreLabel.setHorizontalAlignment(JLabel.CENTER);
			scorePanel.add(scoreLabel);
			score = new JLabel("" + dealer.getHand(0).getHandScore());
			score.setFont(MAIN_TEXT_FONT);
			score.setHorizontalAlignment(JLabel.CENTER);
			score.setToolTipText("The dealer's score");
			scorePanel.add(score);
			statisticsPanel.add(scorePanel);
			statisticsPanel.add(new JLabel(""));
			
			//Hand Panel
			JPanel fullHandPanel = new JPanel(new GridLayout(0, 1));
			hand = new HandPanel(dealer.getHand(0));
			hand.setToolTipText("Cards in hand: " + dealer.getHand(0).getCardsRemaining());
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
			Person dealer = table.getPersonAtIndex(index);
			score.setText("" + dealer.getHand(0).getHandScore());
			hand.setToolTipText("Cards in hand: " + dealer.getHand(0).
					getCardsRemaining());
			hand.updatePanel(index, 0);
		}
	}
	
	/**
	 * Dialog window used for setting a human's wager.
	 * 
	 * @author Brodie Robertson
	 * @version 1.4.3
	 * @since 1.2.0
	 */
	private class WagerWindow extends JDialog
	{				
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
			setSize(TINY_WINDOW);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			setResizable(false);
			setModalityType(ModalityType.MODELESS);
			setLayout(new BorderLayout());
			
			//Request for input.
			JPanel inputPanel = new JPanel(new GridLayout(2, 1));
			JPanel messagePanel = new JPanel(new GridLayout(1, 2));
			JLabel command = new JLabel("Enter " + name + "'s  wager");
			command.setFont(TEXT_FONT);
			command.setToolTipText("Wager must be between $" + Table.MINWAGER 
					+ " and $" + Table.MAXWAGER);
			messagePanel.add(command);
			JLabel error = new JLabel("");
			error.setFont(TEXT_FONT);
			error.setForeground(Color.RED);
			error.setHorizontalAlignment(SwingConstants.CENTER);
			messagePanel.add(error);
			inputPanel.add(messagePanel);
			JTextField input = new JTextField(30);
			inputPanel.add(input);
			add(inputPanel, BorderLayout.CENTER);
			
			//Confirms the users wager input and validates it.
			Action confirm = new AbstractAction() 
			{
				@Override
				public void actionPerformed(ActionEvent arg0) 
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
							getName() + "'s wager is $" + df.format(wager) + "\n");
					playerPanels[index].updatePanel(index);
					dispose();
					nextWager(index);
					return;
				}
			};
			
			//Clears the users wager input.
			Action clear = new AbstractAction() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					input.setText("");
					error.setText("");
				}
			};
			
			//Key bindings
			JRootPane rootPane = getRootPane();
			rootPane.getInputMap(WIFW).put(ENTER, "ENTER");
			rootPane.getActionMap().put("ENTER", confirm);
			rootPane.getInputMap(WIFW).put(ESC, "ESC");
			rootPane.getActionMap().put("ESC", clear);
			
			//Button Panel
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 
					TINY_BUTTON_HGAP, TINY_BUTTON_VGAP));
			buttonPanel.setPreferredSize(new Dimension(getWidth(), 
					TINY_BUTTON_PANEL_HEIGHT));
			JGradientButton confirmButton = new JGradientButton("Confirm");
			confirmButton.setFont(BUTTON_FONT);
			confirmButton.setPreferredSize(BUTTON_SIZE);
			confirmButton.setBackground(GREEN);
			confirmButton.addActionListener(confirm);
			confirmButton.setToolTipText("Confirms the wager input");
			buttonPanel.add(confirmButton);
			JGradientButton clearButton = new JGradientButton("Clear");
			clearButton.setFont(BUTTON_FONT);
			clearButton.setPreferredSize(BUTTON_SIZE);
			clearButton.setBackground(RED);
			clearButton.addActionListener(clear);
			clearButton.setToolTipText("Clears the wager input");
			buttonPanel.add(clearButton);
			add(buttonPanel, BorderLayout.SOUTH);
		}
	}
	
	/**
	 * Dialog window used for setting a human's insurance.
	 * 
	 * @author Brodie Robertson
	 * @version 1.7.0
	 * @since 1.2.0
	 */
	private class InsuranceWindow extends JDialog
	{		
		/**
		 * Constructs a InsuranceWindow with a default layout.
		 * 
		 * @param index The index of the human.
		 * @since 1.2.0
		 */
		public InsuranceWindow(int index)
		{
			Player player = (Player)table.getPersonAtIndex(index);
			String name = player.getName();
			setTitle(name + "'s Insurance");
			setSize(TINY_WINDOW);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			setResizable(false);
			setModalityType(ModalityType.MODELESS);
			setLayout(new BorderLayout());
			
			//Request for input.
			JPanel messagePanel = new JPanel(new GridLayout(1, 2));
			JLabel command = new JLabel("Would you like insurance?");
			command.setFont(TEXT_FONT);
			command.setToolTipText("Total money must be at least as as much as "
					+ "half your wager " + player.getWager() / 2);
			messagePanel.add(command);
			JLabel error = new JLabel("");
			error.setFont(TEXT_FONT);
			error.setForeground(Color.RED);
			error.setHorizontalAlignment(SwingConstants.CENTER);
			messagePanel.add(error);
			add(messagePanel, BorderLayout.CENTER);
			
			JTextField input = new JTextField();
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 
					TINY_BUTTON_HGAP, TINY_BUTTON_VGAP));
			buttonPanel.setPreferredSize(new Dimension(getWidth(), 
					TINY_BUTTON_PANEL_HEIGHT));
			
			//Confims the users insurance input and validates it.
			Action confirm = new AbstractAction() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
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
						error.setText("Invalid input, please enter a valid "
								+ "number");
						return;
					}
					//Invalid insurance.
					catch(PlayerException | TableException ex)
					{
						input.setText("");
						error.setText(ex.getMessage());
						return;
					}
					gameLog.setText(gameLog.getText() + player.getName()
							+ " has $" + df.format(insurance) + " of insurance\n");
					dispose();
					nextInsurance(index);
				}
			};
			
			//Clears the users insurance input.
			Action clear = new AbstractAction() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					error.setText("");
					input.setText("");
				}
			};
			
			//Confirms that the user wishes to take insurance and validates
			//that they can.
			Action yes = new AbstractAction() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					if(player.getTotalMoney() == 0)
					{
						error.setText(player.getName() + " has no money remaining");
						return;
					}
					else
					{	
						//Request for input.
						JPanel inputPanel = new JPanel(new GridLayout(2, 1));
						command.setText("How much would you like?");
						command.setToolTipText("Can't be greater than half the "
								+ "wager: " + player.getWager() / 2);
						error.setText("");
						input.setText("");
						inputPanel.add(messagePanel);
						inputPanel.add(input);
						add(inputPanel, BorderLayout.CENTER);
						
						//Key bindings
						JRootPane rootPane = getRootPane();
						rootPane.getInputMap(WIFW).clear();
						rootPane.getActionMap().clear();
						rootPane.getInputMap(WIFW).put(ENTER, "ENTER");
						rootPane.getActionMap().put("ENTER", confirm);
						rootPane.getInputMap(WIFW).put(ESC, "ESC");
						rootPane.getActionMap().put("ESC", clear);
						
						//Button panel
						buttonPanel.removeAll();
						JGradientButton confirmButton = new JGradientButton("Confirm");
						confirmButton.setFont(BUTTON_FONT);
						confirmButton.setPreferredSize(BUTTON_SIZE);
						confirmButton.setBackground(GREEN);
						confirmButton.addActionListener(confirm);
						confirmButton.setToolTipText("Confirm the insurance input");
						buttonPanel.add(confirmButton);
						JGradientButton clearButton = new JGradientButton("Clear");
						clearButton.setFont(BUTTON_FONT);
						clearButton.setPreferredSize(BUTTON_SIZE);
						clearButton.setBackground(RED);
						clearButton.setToolTipText("Clears the insurance input");
						clearButton.addActionListener(clear);
						buttonPanel.add(clearButton);
						add(buttonPanel, BorderLayout.SOUTH);
						
						buttonPanel.revalidate();
						buttonPanel.repaint();
						gameLog.setText(gameLog.getText() + player.getName() 
						+ " takes insurance\n");
					}
				}
			};
			
			//Confirms that the user does not wish to take insurance.
			Action no = new AbstractAction() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					gameLog.setText(gameLog.getText() + player.getName() 
					+ " doesn't take insurance\n");
					dispose();
					nextInsurance(index);
				}
			};
			
			//Key bindings
			JRootPane rootPane = getRootPane();
			rootPane.getInputMap(WIFW).put(ENTER, "ENTER");
			rootPane.getActionMap().put("ENTER", yes);
			rootPane.getInputMap(WIFW).put(ESC, "ESC");
			rootPane.getActionMap().put("ESC", no);
			rootPane.getInputMap(WIFW).put(Y, "Y");
			rootPane.getActionMap().put("Y", yes);
			rootPane.getInputMap(WIFW).put(N, "N");
			rootPane.getActionMap().put("N", no);
			
			//Button panel
			JGradientButton yesButton = new JGradientButton("Yes");
			yesButton.setFont(BUTTON_FONT);
			yesButton.setPreferredSize(BUTTON_SIZE);
			yesButton.setBackground(GREEN);
			yesButton.addActionListener(yes);
			yesButton.setToolTipText("Confirms that the user wants insurance");
			buttonPanel.add(yesButton);
			JGradientButton noButton = new JGradientButton("No");
			noButton.setFont(BUTTON_FONT);
			noButton.setPreferredSize(BUTTON_SIZE);
			noButton.setBackground(RED);
			noButton.addActionListener(no);
			noButton.setToolTipText("Confirms that the user doesn't wants insurance");
			buttonPanel.add(noButton);
			add(buttonPanel, BorderLayout.SOUTH);
		}
	}
	
	/**
	 * Dialog window used for playing out a human's turn.
	 * 
	 * @author Brodie Robertson
	 * @version 1.7.0
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
		 * The index of the human.
		 */
		private int index;
		
		/**
		 * Ends the Human's turn with this hand and displays the results.
		 * 
		 * @author Brodie Robertson
		 * @version 1.4.2
		 * @since 1.4.1
		 */
		private class SplitStand extends AbstractAction
		{
			private int handIndex;
			
			public SplitStand(int handIndex)
			{
				this.handIndex = handIndex;
			}
			
			/**
			 * Activates when this object receives an action event.
			 * 
			 * (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 * @since 1.4.1
			 */
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Player player = (Player)table.getPersonAtIndex(index);
				gameLog.setText(gameLog.getText() + "Stands hand " 
						+ (handIndex + 1) + " with a score of " + player.
						getHand(handIndex).getHandScore() + "\n");
				nextHand(handIndex);
			}
		}
		
		/**
		 * Deals the human another card to this hand and displays the reslts.
		 * 
		 * @author Brodie Robertson
		 * @version 1.4.3
		 * @since 1.4.1
		 */
		private class SplitHit extends AbstractAction
		{
			private int handIndex;
			
			public SplitHit(int handIndex)
			{
				this.handIndex = handIndex;
			}
			
			/**
			 * Activates when this object receives an action event.
			 * 
			 * (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 * @since 1.4.1
			 */
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Player player = (Player)table.getPersonAtIndex(index);
				error.setText("");
				Card card = table.hit(index, handIndex);
				updateDeck();
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
					//Key bindings
					JRootPane rootPane = getRootPane();
					rootPane.getInputMap(WIFW).remove(D);
					rootPane.getActionMap().remove(D);
					
					//Button Panel
					buttonPanel.removeAll();
					JGradientButton standButton = new JGradientButton("Stand");
					standButton.setFont(BUTTON_FONT);
					standButton.setBackground(LIGHT_BLUE);
					standButton.addActionListener(new SplitStand(handIndex));
					standButton.setToolTipText("Ends the player's turn with this "
							+ "hand");
					buttonPanel.add(standButton);
					JGradientButton hitButton = new JGradientButton("Hit");
					hitButton.setFont(BUTTON_FONT);
					hitButton.setBackground(LIGHT_BLUE);
					hitButton.addActionListener(new SplitHit(handIndex));
					hitButton.setToolTipText("Deals the player another card to "
							+ "this hand");
					buttonPanel.add(hitButton);
					buttonPanel.revalidate();
					buttonPanel.repaint();
				}
				//If the hand's score is equal to Blackjack
				else if(handScore == Table.BLACKJACK)
				{
					gameLog.setText(gameLog.getText() + player.getName() 
						+ " has Blackjack and is forced to stand\n");
					nextPlayer(index);
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
		}
		
		/**
		 * Doubles the Human's wager, deals them a second card and then 
		 * displays the results.
		 * 
		 * @author Brodie Robertson
		 * @version 1.7.0
		 * @since 1.4.1
		 */
		private class SplitDoubleDown extends AbstractAction
		{
			private int handIndex;
			
			public SplitDoubleDown(int handIndex) 
			{
				this.handIndex = handIndex;
			}
			
			/**
			 * Activates when this object receives an action event.
			 * 
			 * (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 * @since 1.4.1
			 */
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Player player = (Player)table.getPersonAtIndex(index);
				error.setText("");
				//If the human can afford to double down.
				if(player.getTotalMoney() >= player.getWager())
				{
					Card card = table.doubleDown(index, handIndex);
					updateDeck();
					player = (Player)table.getPersonAtIndex(index);
					gameLog.setText(gameLog.getText() + player.getName() 
						+ " doubles down hand " + (handIndex + 1) + " and their "
						+ "wager has increased to " + df.format(player.getWager()) + "\n");
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
			setSize(TINY_WINDOW);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			setResizable(false);
			setModalityType(ModalityType.MODELESS);
			setLayout(new BorderLayout());
			this.index = index;
			
			//Request for action
			JPanel messagePanel = new JPanel(new GridLayout(1, 2));
			command = new JLabel("What action would you like to take?");
			command.setFont(TEXT_FONT);
			messagePanel.add(command);
			error = new JLabel("");
			error.setFont(TEXT_FONT);
			error.setForeground(Color.RED);
			error.setHorizontalAlignment(SwingConstants.CENTER);
			messagePanel.add(error);
			add(messagePanel, BorderLayout.CENTER);
			
			//Ends the Human's turn and displays the results.
			Action stand = new AbstractAction() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					Player player = (Player)table.getPersonAtIndex(index);
					gameLog.setText(gameLog.getText() + player.getName() 
					+ " stands with a score of " + player.getHand(0).
					getHandScore() + "\n");
					dispose();
					nextPlayer(index);
				}
			};
			
			//Deals the Human another card and displays the result.
			Action hit = new AbstractAction() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					error.setText("");
					Card card = table.hit(index, 0);
					updateDeck();
					Player player = (Player)table.getPersonAtIndex(index);
					gameLog.setText(gameLog.getText() + player.getName() 
						+ " hits and is dealt a " + card + "\n");
					playerPanels[index].updatePanel(index);
					
					int handScore = player.getHand(0).getHandScore();
					
					//If hand score is less than Blackjack.
					if(handScore < Table.BLACKJACK)
					{
						//Key bindings
						JRootPane rootPane = getRootPane();
						rootPane.getInputMap(WIFW).clear();
						rootPane.getActionMap().clear();
						rootPane.getInputMap(WIFW).put(S, "S");
						rootPane.getActionMap().put("S", stand);
						rootPane.getInputMap(WIFW).put(H, "H");
						rootPane.getActionMap().put("H", this);
						
						//Button panel
						buttonPanel.removeAll();
						JGradientButton standButton = new JGradientButton("Stand");
						standButton.setFont(BUTTON_FONT);
						standButton.setPreferredSize(BUTTON_SIZE);
						standButton.setBackground(LIGHT_BLUE);
						standButton.addActionListener(stand);
						standButton.setToolTipText("Ends the player's turn");
						buttonPanel.add(standButton);
						JGradientButton hitButton = new JGradientButton("Hit");
						hitButton.setFont(BUTTON_FONT);
						hitButton.setPreferredSize(BUTTON_SIZE);
						hitButton.setBackground(LIGHT_BLUE);
						hitButton.addActionListener(this);
						hitButton.setToolTipText("Deals the player another card");
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
			};
			
			//Doubles the Human's wager, deals them another card and displays 
			//the result.
			Action doubleDown = new AbstractAction() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					Player player = (Player)table.getPersonAtIndex(0);
					error.setText("");
					//Doubles the wager and deals a new card if the human can 
					//afford to.
					if(player.getTotalMoney() >= player.getWager())
					{
						Card card = table.doubleDown(index, 0);
						updateDeck();
						player = (Player)table.getPersonAtIndex(index);
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " doubles down and their wager has increased to " 
								+ df.format(player.getWager()) + "\n");
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
			};
			
			//Doubles the Human's wager and splits there hand into 2 distinct 
			//hands.
			Action split = new AbstractAction() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					Player player = (Player)table.getPersonAtIndex(0);
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
							+ df.format(player.getWager()) + "\n");
						playerPanels[index].updatePanel(index);
						
						int handIndex = 0;
						command.setText("What action would you like to take with hand " 
								+ "1?");
						
						//Key bindings
						JRootPane rootPane = getRootPane();
						rootPane.getInputMap(WIFW).clear();
						rootPane.getActionMap().clear();
						rootPane.getInputMap(WIFW).put(S, "S");
						rootPane.getActionMap().put("S", new SplitStand(handIndex));
						rootPane.getInputMap(WIFW).put(H, "H");
						rootPane.getActionMap().put("H", new SplitHit(handIndex));
						rootPane.getInputMap(WIFW).put(D, "D");
						rootPane.getActionMap().put("D", new SplitDoubleDown(handIndex));
						
						//Button panel
						buttonPanel.removeAll();
						JGradientButton standButton = new JGradientButton("Stand");
						standButton.setFont(BUTTON_FONT);
						standButton.setPreferredSize(BUTTON_SIZE);
						standButton.setBackground(LIGHT_BLUE);
						standButton.addActionListener(new SplitStand(handIndex));
						standButton.setToolTipText("Ends the player's turn "
								+ "with this hand");
						buttonPanel.add(standButton);
						JGradientButton hitButton = new JGradientButton("Hit");
						hitButton.setFont(BUTTON_FONT);
						hitButton.setPreferredSize(BUTTON_SIZE);
						hitButton.setBackground(LIGHT_BLUE);
						hitButton.addActionListener(new SplitHit(handIndex));
						hitButton.setToolTipText("Deals the player another card "
								+ "to this hand");
						buttonPanel.add(hitButton);
						JGradientButton doubleDownButton = new JGradientButton("Double Down");
						doubleDownButton.setFont(BUTTON_FONT);
						doubleDownButton.setPreferredSize(BUTTON_SIZE);
						doubleDownButton.setBackground(LIGHT_BLUE);
						doubleDownButton.addActionListener(new SplitDoubleDown(handIndex));
						doubleDownButton.setToolTipText("Doubles the player's "
								+ "wager and deals a new card to this hand");
						
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
			};
			
			//Nullifies the Human's chances of winning the round and returns
			//half of their bet.
			Action surrender = new AbstractAction() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					Player player = (Player)table.getPersonAtIndex(0);
					double returnedAmount = table.surrender(index);
					gameLog.setText(gameLog.getText() + player.getName()
						+ " surrenders and half of their wager is returned\n");
					gameLog.setText(gameLog.getText() + player.getName() 
						+ " regains $" + df.format(returnedAmount) + "\n");
					playerPanels[index].updatePanel(index);
					dispose();
					nextPlayer(index);
				}
			};
			
			//Key bindings
			JRootPane rootPane = getRootPane();
			rootPane.getInputMap(WIFW).put(S, "S");
			rootPane.getActionMap().put("S", stand);
			rootPane.getInputMap(WIFW).put(H, "H");
			rootPane.getActionMap().put("H", hit);
			rootPane.getInputMap(WIFW).put(D, "D");
			rootPane.getActionMap().put("D", doubleDown);
			rootPane.getInputMap(WIFW).put(P, "P");
			rootPane.getActionMap().put("P", split);
			rootPane.getInputMap(WIFW).put(U, "U");
			rootPane.getActionMap().put("U", surrender);
			
			//Button panel
			buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 
					TINY_BUTTON_HGAP, TINY_BUTTON_VGAP));
			buttonPanel.setSize(new Dimension(getWidth(), 
					TINY_BUTTON_PANEL_HEIGHT));
			JGradientButton standButton = new JGradientButton("Stand");
			standButton.setFont(BUTTON_FONT);
			standButton.setPreferredSize(BUTTON_SIZE);
			standButton.setBackground(LIGHT_BLUE);
			standButton.addActionListener(stand);
			standButton.setToolTipText("Ends the player's turn");
			buttonPanel.add(standButton);
			JGradientButton hitButton = new JGradientButton("Hit");
			hitButton.setFont(BUTTON_FONT);
			hitButton.setPreferredSize(BUTTON_SIZE);
			hitButton.setBackground(LIGHT_BLUE);
			hitButton.addActionListener(hit);
			hitButton.setToolTipText("Deals the player another card");
			buttonPanel.add(hitButton);
			JGradientButton doubleDownButton = new JGradientButton("Double Down");
			doubleDownButton.setFont(BUTTON_FONT);
			doubleDownButton.setPreferredSize(BUTTON_SIZE);
			doubleDownButton.setBackground(LIGHT_BLUE);
			doubleDownButton.addActionListener(doubleDown);
			doubleDownButton.setToolTipText("Doubles the player's wager and "
					+ "deals them a second card");
			buttonPanel.add(doubleDownButton);
			JGradientButton splitButton = new JGradientButton("Split");
			splitButton.setFont(BUTTON_FONT);
			splitButton.setPreferredSize(BUTTON_SIZE);
			splitButton.setBackground(LIGHT_BLUE);
			splitButton.addActionListener(split);
			splitButton.setToolTipText("Doubles the player's wager and splits"
					+ " their hand");
			buttonPanel.add(splitButton);
			JGradientButton surrenderButton = new JGradientButton("Surrender");
			surrenderButton.setFont(BUTTON_FONT);
			surrenderButton.setPreferredSize(BUTTON_SIZE);
			surrenderButton.setBackground(LIGHT_BLUE);
			surrenderButton.addActionListener(surrender);
			surrenderButton.setToolTipText("The player loses this round and "
					+ "half their wager is returned");
			buttonPanel.add(surrenderButton);
			add(buttonPanel, BorderLayout.SOUTH);
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
			Hand[] hands = table.getPersonAtIndex(index).getHands();
			//If there is a hand remaining, rebuild the window.
			if(handIndex + 1 < hands.length)
			{
				handIndex++;
				error.setText("");
				command.setText("What action would you like to take with hand " 
						+ (handIndex + 1) + "?");
				
				//Key bindings
				JRootPane rootPane = getRootPane();
				rootPane.getInputMap(WIFW).clear();
				rootPane.getActionMap().clear();
				rootPane.getInputMap(WIFW).put(S, "S");
				rootPane.getActionMap().put("S", new SplitStand(handIndex));
				rootPane.getInputMap(WIFW).put(H, "H");
				rootPane.getActionMap().put("H", new SplitHit(handIndex));
				rootPane.getInputMap(WIFW).put(D, "D");
				rootPane.getActionMap().put("D", new SplitDoubleDown(handIndex));
				
				//Button Panel
				buttonPanel.removeAll();
				JGradientButton standButton = new JGradientButton("Stand");
				standButton.setFont(BUTTON_FONT);
				standButton.setBackground(LIGHT_BLUE);
				standButton.addActionListener(new SplitStand(handIndex));
				standButton.setToolTipText("Ends the player's turn with this "
						+ "hand");
				buttonPanel.add(standButton);
				JGradientButton hitButton = new JGradientButton("Hit");
				hitButton.setFont(BUTTON_FONT);
				hitButton.setBackground(LIGHT_BLUE);
				hitButton.addActionListener(new SplitHit(handIndex));
				hitButton.setToolTipText("Deals the player another card to this "
						+ "hand");
				buttonPanel.add(hitButton);
				JGradientButton doubleDownButton = new JGradientButton("Double Down");
				doubleDownButton.setFont(BUTTON_FONT);
				doubleDownButton.setBackground(LIGHT_BLUE);
				doubleDownButton.addActionListener(new SplitDoubleDown(handIndex));
				doubleDownButton.setToolTipText("Doubles the player's wager and "
						+ "deals them another card");
				buttonPanel.add(doubleDownButton);
				buttonPanel.repaint();
				buttonPanel.revalidate();
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
	 * Dialog window used for resetting the game after all rounds are 
	 * completed.
	 * 
	 * @author Brodie Robertson
	 * @version 1.4.3
	 * @since 1.2.0
	 */
	private class EndGameWindow extends JDialog
	{		
		/**
		 * Constructs an EndGameWindow with a default layout.
		 * 
		 * @since 1.4.0
		 */
		public EndGameWindow()
		{
			setTitle("End Game");
			final Dimension dimension = new Dimension(800, TINY_WINDOW.height);
			setSize(dimension);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			setResizable(false);
			setModalityType(ModalityType.MODELESS);
			setLayout(new BorderLayout());
			
			//Request for input.
			JLabel command = new JLabel("Would you like to play again?");
			command.setFont(SINGLE_TEXT_FONT);
			command.setHorizontalAlignment(JLabel.CENTER);
			add(command, BorderLayout.CENTER);
			
			//Confirms that the user wants to play another set of rounds.
			Action yes = new AbstractAction() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					dispose();
					clearScreen();
					GameSettingsWindow window = new GameSettingsWindow();
					window.setVisible(true);
				}
			};
			
			//Confirms that the user wants to return to the main menu.
			Action titleScreen = new AbstractAction() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					dispose();
					titleScreen();
				}
			};
			
			//Saves the game log to a .txt file, overwriting the previous log.
			Action printGameLog = new AbstractAction()
			{
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					PrintWriter outputStream = null;
					try
					{
						outputStream = new PrintWriter(new FileOutputStream("Game Log.txt"), false);
					}
					catch(FileNotFoundException ex)
					{
						System.out.println("Error opening the file");
						System.exit(0);
					}
					
					String[] log = gameLog.getText().split("\n");
					for(int i = 0; i < log.length; i++)
					{
						outputStream.println(log[i]);
					}
					outputStream.close();
				}
			};
			
			//Key bindings
			JRootPane rootPane = getRootPane();
			rootPane.getInputMap(WIFW).put(ENTER, "ENTER");
			rootPane.getActionMap().put("ENTER", yes);
			rootPane.getInputMap(WIFW).put(Y, "Y");
			rootPane.getActionMap().put("Y", yes);
			rootPane.getInputMap(WIFW).put(ESC, "ESC");
			rootPane.getActionMap().put("ESC", new QuitGame());
			rootPane.getInputMap(WIFW).put(N, "N");
			rootPane.getActionMap().put("N", new QuitGame());
			rootPane.getInputMap(WIFW).put(M, "M");
			rootPane.getActionMap().put("M", titleScreen);
			rootPane.getInputMap(WIFW).put(S, "S");
			rootPane.getActionMap().put("S", new Statistics());
			rootPane.getInputMap(WIFW).put(A, "A");
			rootPane.getActionMap().put("A", new About());
			rootPane.getInputMap(WIFW).put(L, "L");
			rootPane.getActionMap().put("L", printGameLog);
			
			//Button panel
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 
					TINY_BUTTON_HGAP,  TINY_BUTTON_VGAP));
			buttonPanel.setPreferredSize(new Dimension(getWidth(), 
					TINY_BUTTON_PANEL_HEIGHT));
			JGradientButton yesButton = new JGradientButton("Yes");
			yesButton.setFont(BUTTON_FONT);
			yesButton.setPreferredSize(BUTTON_SIZE);
			yesButton.setBackground(GREEN);
			yesButton.addActionListener(yes);
			yesButton.setToolTipText("Restarts the game");
			buttonPanel.add(yesButton);
			JGradientButton noButton = new JGradientButton("No");
			noButton.setFont(BUTTON_FONT);
			noButton.setPreferredSize(BUTTON_SIZE);
			noButton.setBackground(RED);
			noButton.addActionListener(new QuitGame());
			noButton.setToolTipText("Ends the game");
			buttonPanel.add(noButton);
			JGradientButton titleButton = new JGradientButton("Title Screen");
			titleButton.setFont(BUTTON_FONT);
			titleButton.setPreferredSize(BUTTON_SIZE);
			titleButton.setBackground(LIGHT_BLUE);
			titleButton.addActionListener(titleScreen);
			titleButton.setToolTipText("Goes back to the title screen");
			buttonPanel.add(titleButton);
			JGradientButton statisticsButton = new JGradientButton("Statistics");
			statisticsButton.setFont(BUTTON_FONT);
			statisticsButton.setPreferredSize(BUTTON_SIZE);
			statisticsButton.setBackground(LIGHT_BLUE);
			statisticsButton.addActionListener(new Statistics());
			statisticsButton.setToolTipText("Opens the Statistics window");
			buttonPanel.add(statisticsButton);
			JGradientButton aboutButton = new JGradientButton("About");
			aboutButton.setFont(BUTTON_FONT);
			aboutButton.setPreferredSize(BUTTON_SIZE);
			aboutButton.setBackground(LIGHT_BLUE);
			aboutButton.addActionListener(new About());
			aboutButton.setToolTipText("Opens the About window");
			buttonPanel.add(aboutButton);
			JGradientButton saveButton = new JGradientButton("Save Log");
			saveButton.setFont(BUTTON_FONT);
			saveButton.setPreferredSize(BUTTON_SIZE);
			saveButton.setBackground(LIGHT_BLUE);
			saveButton.addActionListener(printGameLog);
			saveButton.setToolTipText("Prints the game log to a txt file");
			buttonPanel.add(saveButton);
			
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
		JRootPane rootPane = getRootPane();
		rootPane.getInputMap(WIFW).put(ESC, "ESCAPE");
		rootPane.getActionMap().put("ESCAPE", new QuitGame());
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
		
		Action startGame = new AbstractAction() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				GameSettingsWindow window = new GameSettingsWindow();
				window.setVisible(true);
			}
		};
		
		JRootPane rootPane = getRootPane();
		rootPane.getInputMap(WIFW).put(ENTER, "ENTER");
		rootPane.getActionMap().put("ENTER", startGame);
		rootPane.getInputMap(WIFW).put(A, "A");
		rootPane.getActionMap().put("A", new About());
		rootPane.getInputMap(WIFW).put(S, "S");
		rootPane.getActionMap().put("S", new Statistics());
		
		final int buttonPanelHeight = 80;
		final int buttonVGap = buttonPanelHeight / 4;
		final int buttonHGap = buttonPanelHeight / 5;
		final Font buttonFont = new Font("Trebuchet MS", Font.BOLD, 18);
		final Dimension buttonSize = new Dimension(130, 
				buttonPanelHeight/2);
		getContentPane().setBackground(new Color(243, 101, 37));
		
		//Title
		JLabel title = new JLabel("Blackjack");
		Font titleFont = new Font("Centaur", Font.BOLD, 350);
		title.setFont(titleFont);
		title.setHorizontalAlignment(JLabel.CENTER);
		add(title);
		
		//Button panel
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 
				buttonHGap, buttonVGap));
		buttonPanel.setBackground(Color.DARK_GRAY);
		buttonPanel.setPreferredSize(new Dimension(WIDTH, buttonPanelHeight));
		JGradientButton playButton = new JGradientButton("Start Game");
		playButton.setBackground(LIGHT_BLUE);
		playButton.addActionListener(startGame);
		playButton.setToolTipText("Starts the game");
		playButton.setPreferredSize(buttonSize);
		playButton.setFont(buttonFont);
		buttonPanel.add(playButton);
		JGradientButton quitButton = new JGradientButton("Quit Game");
		quitButton.setBackground(LIGHT_BLUE);
		quitButton.addActionListener(new QuitGame());
		quitButton.setToolTipText("Ends the game");
		quitButton.setPreferredSize(buttonSize);
		quitButton.setFont(buttonFont);
		buttonPanel.add(quitButton);
		JGradientButton aboutButton = new JGradientButton("About");
		aboutButton.setBackground(LIGHT_BLUE);
		aboutButton.addActionListener(new About());
		aboutButton.setToolTipText("Opens the About window");
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
		getContentPane().setBackground(LIGHT_GRAY);
		setLayout(new BorderLayout());
		
		//Key Bindings
		JRootPane rootPane = getRootPane();
		rootPane.getInputMap(WIFW).remove(ENTER);
		rootPane.getActionMap().remove(ENTER);
		rootPane.getInputMap(WIFW).put(G, "G");
		rootPane.getActionMap().put("G", new GameSpeed());
		
		//Menu Bar
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		menu.setFont(MAIN_HEADING_FONT);
		JMenuItem gameSpeedButton = new JMenuItem("Game Speed");
		gameSpeedButton.setFont(MAIN_TEXT_FONT);
		gameSpeedButton.addActionListener(new GameSpeed());
		gameSpeedButton.setToolTipText("Opens the Game Speed window");
		menu.add(gameSpeedButton);
		JMenuItem aboutButton = new JMenuItem("About");
		aboutButton.setFont(MAIN_TEXT_FONT);
		aboutButton.addActionListener(new About());
		aboutButton.setToolTipText("Opens the About window");
		menu.add(aboutButton);
		JMenuItem statsButton = new JMenuItem("Statistics");
		statsButton.setFont(MAIN_TEXT_FONT);
		statsButton.addActionListener(new Statistics());
		statsButton.setToolTipText("Opens the statistics windows");
		menu.add(statsButton);
		JMenuItem quitButton = new JMenuItem("Quit Game");
		quitButton.setFont(MAIN_TEXT_FONT);
		quitButton.addActionListener(new QuitGame());
		quitButton.setToolTipText("Ends the game");
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
		
		//Deck Panel
		JPanel deckPanel = new JPanel(new GridLayout(3, 3));
		deckPanel.add(new JLabel(""));
		JLabel deckHeader = new JLabel("Deck");
		deckHeader.setHorizontalAlignment(JLabel.CENTER);
		deckHeader.setVerticalAlignment(JLabel.BOTTOM);
		deckHeader.setFont(MAIN_HEADING_FONT);
		deckPanel.add(deckHeader);
		deckPanel.add(new JLabel(""));
		deckPanel.add(new JLabel(""));
		deck = new GUICard();
		deck.card = new Card();
		updateDeck();
		deckPanel.add(deck);
		deckPanel.add(new JLabel(""));
		deckPanel.add(new JLabel(""));
		deckPanel.add(new JLabel(""));
		
		topQuarterPanel.add(deckPanel);
		topQuarterPanel.add(new JLabel(""));
		topCenterPanel.add(topQuarterPanel);
		centerPanel.add(topCenterPanel);
		
		//Log Panel
		JPanel logPanel = new JPanel(new BorderLayout());
		JLabel gameLogHeader = new JLabel("Game Log");
		gameLogHeader.setFont(MAIN_HEADING_FONT);
		logPanel.add(gameLogHeader, BorderLayout.NORTH);
		gameLog = new JTextArea();
		gameLog.setFont(MAIN_TEXT_FONT);
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
		initialWager(0);
	}
	
	/**
	 * Updates the deck tool tip text.
	 * 
	 * @since 1.7.0
	 */
	private void updateDeck()
	{
		deck.setToolTipText("Cards Remaining: " + table.getDeck().getCardsRemaining() 
				+ "/" + (table.getDeck().getNumOfDecks() * Deck.DECKSIZE));
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
	 * @param index The index of the Player.
	 * @since 1.2.0
	 */
	private void initialWager(int index)
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
				ActionListener cpuTurn = new ActionListener() 
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						double wager = table.setCPUWager(index);
						gameLog.setText(gameLog.getText() + player.getName() + 
								"'s wager is $" + df.format(wager) + "\n");
						playerPanels[index].updatePanel(index);
						nextWager(index);	
					}
				};
				timer = new Timer(turnDelay, cpuTurn);
				timer.setRepeats(false);
				timer.start();
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
	 * @param index The index of the Person.
	 * @since 1.2.0
	 */
	private void nextWager(int index)
	{
		//If there are any more players left.
		if(index + 1 < table.getPlayers().length - 1)
		{
			initialWager(index + 1);
		}
		//If not goes to the next stage of the game.
		else
		{
			gameLog.setText(gameLog.getText() + "\nDealing Cards to Players:\n");
			gameLog.setText(gameLog.getText() + "All player's are dealt 2 cards\n");
			initialDeal(0);
		}
	}
	
	/**
	 * Deals 2 cards to a person at a specified index.
	 * 
	 * @param index The index of the Person
	 * @since 1.2.0
	 */
	private void initialDeal(int index)
	{
		table.deal(index);
		updateDeck();
		Person person = table.getPersonAtIndex(index);
		ActionListener deal = new ActionListener() 
		{	
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//If the person is a Player.
				if(person instanceof Player)
				{
					Player player = (Player)person;
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
						
						player = (Player)table.getPersonAtIndex(index);
						//If the player has Blackjack
						if(player.getHand(0).getHandScore() == Table.BLACKJACK)
						{
							gameLog.setText(gameLog.getText() + player.getName() 
								+ " has Blackjack\n");
						}
						playerPanels[index].updatePanel(index);
					}
				}
				//If the person is the Dealer.
				else
				{
					Person dealer = person;
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
					
					dealer = table.getPersonAtIndex(index);
					//If the player has Blackjack
					if(dealer.getHand(0).getHandScore() == Table.BLACKJACK)
					{
						gameLog.setText(gameLog.getText() + dealer.getName() 
						+ " has Blackjack\n");
					}
					dealerPanel.updatePanel(index);
				}
				nextDeal(index);
			}
		};
		
		timer = new Timer(turnDelay, deal);
		timer.setRepeats(false);
		timer.start();
	}
	
	/**
	 * Decks if there are any players who haven't been dealt cards and then
	 * either begins the insurance wager collection or the player turns.
	 * 
	 * @param index Index of the Person.
	 * @since 1.5.0
	 */
	private void nextDeal(int index)
	{
		if(index + 1 < table.getPlayers().length)
		{
			initialDeal(index + 1);
		}
		else
		{
			ActionListener nextStage = new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					Person[] people = table.getPlayers();
					//If the dealer's first card is an Ace,begin insurance round.
					if(people[people.length - 1].getHand(0).getCard(0).
							getFace() == Face.ACE)
					{
						gameLog.setText(gameLog.getText() + "\nInsurance:\n");
						insurance(0);
					}
					//If not begin the first player turn.
					else
					{
						gameLog.setText(gameLog.getText() + "\nPlayer Turns:");
						playerTurn(0);
					}
				}
			};
			
			timer = new Timer(turnDelay, nextStage);
			timer.setRepeats(false);
			timer.start();
		}
	}
	
	/**
	 * Sets the insurance of a player at a specified index.
	 * 
	 * @param index Index of the Player.
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
				ActionListener cpuInsurance = new ActionListener() 
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						double insurance = table.setCPUInsurance(index);
						Player cpu = (Player)table.getPersonAtIndex(index);
						//If the CPU took insurance.
						if(cpu.getTookInsurance())
						{
							playerPanels[index].updatePanel(index);
							gameLog.setText(gameLog.getText() + cpu.getName() + 
									" has $" + df.format(insurance) + " of insurance\n");
						}
						//If the CPU doesn't take insurance.
						else
						{
							gameLog.setText(gameLog.getText() + 
									cpu.getName() + " doesn't take insurance\n");
						}
						
						playerPanels[index].updatePanel(index);
						nextInsurance(index);
					}
				};
				
				timer = new Timer(turnDelay, cpuInsurance);
				timer.setRepeats(false);
				timer.start();
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
						ActionListener bankrupt = new ActionListener() 
						{
							
							@Override
							public void actionPerformed(ActionEvent e) 
							{
								gameLog.setText(gameLog.getText() + player.getName() 
								+ " has gone bankrupt\n");
							}
						};
						
						timer = new Timer(turnDelay, bankrupt);
						timer.setRepeats(false);
						timer.start();
					}
				}
				
				nextRound();
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
			gameLog.setText(gameLog.getText() + "\nInsurance Result:\n");
			gameLog.setText(gameLog.getText() + dealer.getName() + "'s second card is " + 
					dealer.getHand(0).getCard(1) + "\n");
			gameLog.setText(gameLog.getText() + dealer.getName() 
					+ " has Blackjack\n");
			dealerPanel.updatePanel(finalIndex);
			
			for(int i = 0; i < players.length - 1; i++)
			{
				final int j = i;
				ActionListener insuranceResult = new ActionListener() 
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						gameLog.setText(gameLog.getText() + table.insurancePayout(j));
						playerPanels[j].updatePanel(j);
					}
				};
				
				timer = new Timer(turnDelay, insuranceResult);
				timer.setRepeats(false);
				timer.start();
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
			gameLog.setText(gameLog.getText() + "\nIt's now " 
					+ player.getName() + "'s turn");
			//If the Player is a Human open up a new TurnWindow.
			if(player instanceof Human)
			{
				TurnWindow window = new TurnWindow(index);
				window.setVisible(true);
			}
			//If the Player is a CPU the table handles the player's turn.
			else if(player instanceof CPU)
			{	
				timer = new Timer(turnDelay, new CpuTurn(index));
				timer.setRepeats(false);
				timer.start();
			}
			gameLog.setText(gameLog.getText() + "\n");
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
	 * Plays out the turn of a CPU at a specified index.
	 * 
	 * @author Brodie Robertson
	 * @version 1.6.0
	 * @since 1.6.0
	 */
	private class CpuTurn implements ActionListener
	{
		/**
		 * Index of the CPU.
		 */
		private int index;
		
		/**
		 * Constructs a CpuTurn with a specified index.
		 * 
		 * @param index Index of the CPU.
		 * @since 1.6.0
		 */
		public CpuTurn(int index) 
		{
			this.index = index;
		}
		
		/**
		 * Activates when this object receives an action event.
		 * 
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 * @since 1.6.0
		 */
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			Player player = (Player)table.getPersonAtIndex(index);
			
			//If the player can split and has an an ace a 9 or a card with a
			//value of 10.
			if(player.canSplit() && (player.getHand(0).getCard(0).getValue() == 10 
					|| player.getHand(0).getCard(0).getValue() == 9
					|| player.getHand(0).getCard(0).getFace() == Face.ACE))
			{
				table.split(index);
				player = (Player)table.getPersonAtIndex(index);
				gameLog.setText(gameLog.getText() + player.getName() 
					+ " splits their hand and their wager increase to " 
					+ df.format(player.getWager()) + "\n");
				playerPanels[index].updatePanel(index);
				
				int i = 0;
				while(i < player.getHands().length && player.getHand(0).
						getHandScore() < Table.BLACKJACK && player.getHand(1).
						getHandScore() < Table.BLACKJACK)
				{
					while(player.getHand(i).getHandScore() < 17)
					{
						Card card = table.hit(index, i);
						updateDeck();
						player = (Player)table.getPersonAtIndex(index);
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " hits and is dealt a " + card + " to hand " + 
							(i + 1) + "\n");
						playerPanels[index].updatePanel(index);
					}
					
					int handScore = player.getHand(i).getHandScore();
					//If the CPU has Blackjack.
					if(handScore == Table.BLACKJACK)
					{
						gameLog.setText(gameLog.getText() + player.getName()
						+ " has Blackjack and is forced to stand\n");
					}
					//If the CPU has gone bust.
					else if(player.getBusted())
					{
						gameLog.setText(gameLog.getText() + player.getName() 
						+ " busts\n");
					}
					//Else the player stands.
					else
					{
						gameLog.setText(gameLog.getText() + "Stands hand " 
							+ (i + 1) + " with a score of " + handScore + "\n");
					}
					playerPanels[index].updatePanel(index);
					i++;
				}
				
				nextPlayer(index);
			}
			//If the CPU doesn't split.
			else
			{
				//If the CPU's score is less than or equal to 10, hit.
				if(player.getHand(0).getHandScore() <= 10)
				{
					hit();
				}
				//If the the CPU's score is from 11 - 14.
				else if(player.getHand(0).getHandScore() >= 11 && 
						player.getHand(0).getHandScore() <= 14)
				{
					//If the player can afford to double down then do do so.
					if(player.getTotalMoney() >= player.getWager())
					{
						Card card = table.doubleDown(index, 0);
						updateDeck();
						player = (Player)table.getPersonAtIndex(index);
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " doubles down and their wager has increased to " 
							+ df.format(player.getWager()) + "\n");
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " is dealt a " + card + "\n");
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " now has a score of " + player.getHand(0).
							getHandScore() + "\n");
						
						if(player.getBusted())
						{
							gameLog.setText(gameLog.getText() 
									+ player.getName() + " has gone bust\n");
						}
						playerPanels[index].updatePanel(index);
					}
					//Otherwise hit.
					else
					{
						hit();
					}
				}
				//If the CPU's score equals 15 then surrender.
				else if(player.getHand(0).getHandScore() == 15)
				{
					double returnedAmount = table.surrender(index);
					gameLog.setText(gameLog.getText() + player.getName()
						+ " surrenders and half of their wager is returned\n");
					gameLog.setText(gameLog.getText() + player.getName() 
						+ " regains $" + df.format(returnedAmount) + "\n");
					playerPanels[index].updatePanel(index);
				}
				//If the CPU's score is 16 or greater than stand.
				else if(player.getHand(0).getHandScore() >= 16)
				{
					gameLog.setText(gameLog.getText() + player.getName() 
					+ " stands with a score of " + table.getPersonAtIndex
					(index).getHand(0).getHandScore() + "\n");
					playerPanels[index].updatePanel(index);
				}
				playerPanels[index].updatePanel(index);
				nextPlayer(index);
			}
		}
		
		/**
		 * Deals the CPU a new card.
		 * 
		 * @since 1.6.0
		 */
		private void hit()
		{
			Player player = (Player)table.getPersonAtIndex(index);
			
			//While the CPU's score is less than 17 hit.
			while(player.getHand(0).getHandScore() < 17)
			{
				Card card = table.hit(index, 0);
				updateDeck();
				player = (Player)table.getPersonAtIndex(index);
				gameLog.setText(gameLog.getText() + player.getName() 
						+ " hits and is dealt a " + card + "\n");
				playerPanels[index].updatePanel(index);
			}
			
			int handScore = player.getHand(0).getHandScore();
			//If the CPU's score equals Blackjack stand
			if(handScore == Table.BLACKJACK)
			{
				gameLog.setText(gameLog.getText() + player.getName()
				+ " has Blackjack and is forced to stand\n");
			}
			//If the CPU has gone bust.
			else if(player.getBusted())
			{
				gameLog.setText(gameLog.getText() + player.getName() 
				+ " busts\n");
			}
			//Else stand.
			else
			{
				gameLog.setText(gameLog.getText() + player.getName() 
				+ " stands with a score of " + handScore + "\n");
			}
			playerPanels[index].updatePanel(index);
		}
	}
	
	/**
	 * Plays out the dealer's turn.
	 * 
	 * @since 1.2.0
	 */
	private void dealerTurn()
	{
		final int finalIndex = table.getPlayers().length - 1;
		table.flipDealersCard();

		ActionListener dealerStart = new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Person dealer = table.getPersonAtIndex(finalIndex);
				gameLog.setText(gameLog.getText() + "\nIt's now " 
						+ dealer.getName() + "'s turn\n");
				gameLog.setText(gameLog.getText() + dealer.getName() + "'s face "
						+ "down card was " + dealer.getHand(0).getCard(1) + " and"
						+ " they now have a score of " + dealer.getHand(0).
						getHandScore() + "\n");	
				dealerPanel.updatePanel(finalIndex);
				dealerDraw();
			}
		};
		timer = new Timer(turnDelay, dealerStart);
		timer.setRepeats(false);
		timer.start();
	}
	
	/**
	 * Deals the dealer cards until they reach a score of at least 17.
	 * 
	 * @since 1.7.0
	 */
	private void dealerDraw()
	{
		final int finalIndex = table.getPlayers().length - 1;
		Card[] cards = table.addToDealersHand();
		updateDeck();
		
		//If the dealer is dealt any cards, add them to the game log.
		if(cards.length > 0)
		{
			ActionListener hit = new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					Person dealer = table.getPersonAtIndex(finalIndex);
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
					dealerPanel.updatePanel(finalIndex);
					gameLog.setText(gameLog.getText() + "\nRound Results:\n");
					roundResults(0);
				}
			};
			
			timer = new Timer(turnDelay, hit);
			timer.setRepeats(false);
			timer.start();
		}
		else
		{
			gameLog.setText(gameLog.getText() + "\nRound Results:\n");
			roundResults(0);
		}
	}
	
	/**
	 * Determines the results of the round for a Player at a specified index.
	 * 
	 * @param index Index of the Player.
	 * @since 1.3.0
	 */
	private void roundResults(int index)
	{
		ActionListener result = new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				gameLog.setText(gameLog.getText() + table.roundResult(index) + "\n");		
				Player player = (Player)table.getPersonAtIndex(index);
				
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
						gameLog.setText(gameLog.getText() + player.getName() 
							+ " wins $" + df.format(player.getCurrentWin()) + "\n");
					}
					//If the Player has Blackjack.
					else if(player.getHasBlackjack())
					{
						gameLog.setText(gameLog.getText() + player.getName()
							+ " wins $" + df.format(player.getCurrentBlackjack()) + "\n");
					}
				}
				playerPanels[index].updatePanel(index);	
				nextResult(index);
			}
		};
		
		timer = new Timer(turnDelay, result);
		timer.setRepeats(false);
		timer.start();
	}
	
	/**
	 * Checks if there are any players who need to receive their results if
	 * there aren't the game moves to the next round.
	 * 
	 * @param index Index of the Player.
	 * @since 1.5.0
	 */
	private void nextResult(int index)
	{
		if(index + 1 < table.getPlayers().length - 1)
		{
			roundResults(index + 1);
		}
		else
		{
			nextRound();
		}
	}
	
	/**
	 * Checks if there are any rounds remaining.
	 * 
	 * @since 1.3.0
	 */
	private void nextRound()
	{		
		//If there are rounds remaining and any player's who aren't bankrupt.
		if(table.getCurrentRound() + 1 <= table.getTotalRounds() && 
				table.checkIfAnyPlayerNotBankrupt())
		{
			resetTableForNewRound();
			table.setCurrentRound(table.getCurrentRound() + 1);
			gameLog.setText(gameLog.getText() + "\n");
			currentRound();
			initialWager(0);
		}
		//If there are no rounds remaining or every player is bankrupt.
		else
		{
			EndGameWindow window = new EndGameWindow();
			window.setVisible(true);
		}
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
				playerPanels[i].resetPanel(i);
			}
			//If the Person is a Dealer, update their panel.
			else
			{
				dealerPanel.updatePanel(i);
			}
		}
	}
	
	/**
	 * Clears all elements off the screen
	 * 
	 * @since 1.4.3
	 */
	private void clearScreen()
	{
		getContentPane().removeAll();
		setJMenuBar(null);
		revalidate();
		repaint();
	}
}