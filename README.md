# Blackjack Changelog
GUI 1.7.0
<br>Note:
Finally, it's done, it ended up taking far longer than expected but this project is finally over, I've been lost every now and then but I'm so happy that I decided to do this now instead of waiting for the new semester to roll around and then trying to learn it then along side everything else. The next time I take on a project of this scale I'll definetly do more in the way of planning the GUI classes, I couldn't do so this time because I was learning Event-Driven Programming for the first time whilst I was writing this game. I'll take a short break but as for my next project it will be in a web language, it won't be too this scale as I've never touched web programming before so we'll see how that goes.

Change Log:
1. CPU bets and insurance are generated based on how much money the CPU has left.
2. The game log can be optionally printed to a text file at the end of the game.
3. Every card is drawn to the screen and looks as the a physical card would.
4. Face down cards are drawn to the screen showing the back of the card, this includes the deck.
5. Completely rewrote how ace vlaues are swapped so that they swap under the correct conditions.
6. Modified the where new lines occur within the game log to make it easier to read.
7. Changed the font and the size of the logo.
8. Modiged some of the lines that are outputted to the game log.

GUI Beta 1.6.0
<br>Note:
I intend to have this project out of beta by tomorrow whether that means it'll be completed wiill be dependent upon how much I feel like I can deal with looking at this code any longer.

Change Log:
1. CPU's now make different turn actions depending on the cards they hold and the amount of money they have, I'm Blackjack expert so they're probably all bad actions but that's not the point.
2. The game speed can be modified via a slider window.

Future Features:
1. GUI elements for every component of the game, for the cards and deck they will be drawn on screen.
2. Print the log to file when the game is over.
3. Dynamically generate CPU characters initial bets, insurance.

GUI Beta 1.5.0
<br>Note:
I'm finally on the home stretch, the features list is getting shorter and shorter and I'm happy to see it that way, at the rate I'm going I should be done by the middle of this week and then I can start working on a new project. I've left the 2 most annnoying features till the end, it should be obvious but I'm referring to drawing the cards yes I know I could use images but that's no fun and the other is deciding how a CPU player should act.

Change Log:
1. About window finally implemented, shows the version number and the credits the people who worked on the game i.e me unless someone decides to give the game a test.
2. CPU turns slowed down so that the usr can follow what is happening on screen.
3. Some methods that we based around for loops, such as round results have been broken down and use a logic gate to move to the next player to make it easier to slow down with a timer.

Future Features:
1. GUI elements for every component of the game, for the cards and deck they will be drawn on screen.
2. Print the log to file when the game is over.
3. Dynamically generate CPU characters initial bets, insurance, and round descisions.
4. Allow the use to modify the game speed.

GUI Beta 1.4.3
<br>Change Log:
1. Every text element has had it's font changed and error messages are now red.
2. Some windows have been resized to better accommodate the modified text text size.
3. All button panels except for the title screen have been increaed in size.
4. I finally implemented the statistics window, and it shows basically everything that I couldd think of keeping track of.
5. The number of decks change based on how many players are in the game.
6. Smoothed out some bugs that I hadn't previously noticed.

Future Features:
1. GUI elements for every component of the game, for the cards and deck they will be drawn on screen.
2. Complete the about window which will show the game version and people who worked on the game.
3. Print the log to file when the game is over.
4. Slow down the game to allow for the user to understand what is happening without reading the log.
5. Dynamically generate CPU characters initial bets, insurance, and round descisions.

GUI Beta 1.4.2
<br>Change Log:
1. All buttons replaced with custom JButtons with gradients.
2. A few extra hot keys added.
3. Hand panel now actually shows the second hand.
4. Player panel resets after a round where a player split their hand.
5. A few mistakes within the gamelog have been addressed.
6. Almost every element has a tool tip.

Future Features:
1. GUI elements for every component of the game, for the cards and deck they will be drawn on screen.
2. Updated elements for components that are already displayed.
3. Complete the about window which will show the game version and people who worked on the game.
4. Print the log to file when the game is over.
5. Decide how many decks should be used based on the number of players.
6. Slow down the game to allow for the user to understand what is happening without reading the log.
7. Complete the statistics window which will show tthe currrent money of each player, how much they've wagered and a bunch of other things.
8. Dynamically generate CPU characters initial bets, insurance, and round descisions.

GUI Beta 1.4.1
<br>Note:
I was planning to get more done in this release but I've never done keybindings before so I had to learn how to do that before I was able to do anything and that ended up taking far longer than I expected, I noticed after my commit that I bound enter for yes and esc for no, but I completely forgot about y for yes and n for no, in 1.4.2 I'll be fixing that.

Change Log:
1. Hot keys for every action in the game.
2. ActionListeners refactored to AbstractAction's to allow for keybindings.
3. With the refactoring any action that doesn't get reused has been made into an anonymous class.

Future Features:
1. GUI elements for every component of the game, for the cards and deck they will be drawn on screen.
2. Updated elements for components that are already displayed.
3. Tool tips everywhere that makes sense.
4. Complete the about window which will show the game version and people who worked on the game.
5. Print the log to file when the game is over.
6. Decide how many decks should be used based on the number of players.
7. Slow down the game to allow for the user to understand what is happening without reading the log.
8. Complete the statistics window which will show tthe currrent money of each player, how much they've wagered and a bunch of other things.
9. Dynamically generate CPU characters initial bets, insurance, and round descisions.

GUI Alpha 1.4.0
<br>Note:
I'm really happy to see this project finally leaving it's alpha, for the next update I plan too address features 2, 3, 4, and 5, I may commit an intemediary update but my next major update will be after those features are done, it shouldn't take me too long to do so. On a side note I just learnt that you don't have to use Git as a web based service, I know it seems obvious once you know about it so by the next release I hope to also work out how to use TortoiseGit.

Change Log:
1. Commenting completed for all of the code.
2. The entire GUI has been refactored into an Event-Driven format.
3. The hands of every person are displayed with a basic graphical representation.
4. Every element of a person's panel which can change will be updated throughout the game.
5. Added a window at the end of the game prompting the user for what they want to do next.
6. Moved the player's hand scores labels and scores into arrays.

Future Features:
1. GUI elements for every component of the game, for the cards and deck they will be drawn on screen.
2. Updated elements for components that are already displayed.
3. Tool tips everywhere that makes sense.
4. Allow the user to use Enter and Esc in place of pressing Confirm/Yes Clear/No.
5. Complete the about window which will show the game version and people who worked on the game.
6. Print the log to file when the game is over.
7. Decide how many decks should be used based on the number of players.
8. Slow down the game to allow for the user to understand what is happening without reading the log.
9. Complete the statistics window which will show tthe currrent money of each player, how much they've wagered and a bunch of other things.
10. Dynamically generate CPU characters initial bets, insurance, and round descisions.

GUI Alpha 1.3.0
Change Log:
1. A few windows which had to be modal before have now become modeless.
2. The first round can be played until the end with accurate payouts happening.
3. New messages included in the log up until the end of the round.
4. Modified some of the backend methods return types to make updating the log easier.
5. The blackjack and standard winnings from the current round are now being tracked.

Future Features:
1. GUI elements for every component of the game, for the cards and deck they will be drawn on screen.
2. Updated elements for components that are already displayed.
3. Tool tips everywhere that makes sense.
4. Allow the user to use Enter and Esc in place of pressing Confirm/Yes Clear/No.
5. Complete the about window which will show the game version and people who worked on the game.
6. Print the log to file when the game is over.
7. Decide how many decks should be used based on the number of players.
8. Slow down the game to allow for the user to understand what is happening without reading the log.
9. Complete the statistics window which will show tthe currrent money of each player, how much they've wagered and a bunch of other things.
10. Dynamically generate CPU characters initial bets, insurance, and round descisions.

GUI Alpha 1.2.1
<br>Note:
In the last release I've discovered a major issue with the Insurance backend logic which causes the program to compeltely lockup, I'm currently working on an update to the issue.

Change Log:
1. Fixed the game breaking bug in the insurance backend.
2. Begun refactoring code into an Even-Driven format which has caused some of the code to be a bit hard to follow.

Future Features:
1. GUI elements for every component of the game, for the cards and deck they will be drawn on screen.
2. Updated elements for components that are already displayed.
3. Tool tips everywhere that makes sense.
4. Allow the user to use Enter and Esc in place of pressing Confirm/Yes Clear/No.
5. Complete the about window which will show the game version and people who worked on the game.
6. Print the log to file when the game is over.
7. Decide how many decks should be used based on the number of players.
8. Slow down the game to allow for the user to understand what is happening without reading the log.
9. Complete the statistics window which will show tthe currrent money of each player, how much they've wagered and a bunch of other things.
10. Dynamically generate CPU characters initial bets, insurance, and round descisions.

GUI Alpha 1.2.0
<br>Note: 
I've come to realise that I've been approaching Event-Driven programming incorrectly, before my next realease I plan to address these issues. I've been doing sequential programming for so long that I've developed some bad habits which has caused some issues with my code, it hasn't created any bugs per se however the solutions I came up with are sub optimal at best. Whilst writing this log I've come up with a few solutions to the problem so I'll get working on those ASAP.

Change Log:
1. Basic GUI elements for most elements in the game.
2. Human player names a choosable when the game is started.
3. Number of human and CPU players choosable when the game is started.
4. On screen log which keeps track of all choices made during the game.
5. Tool tips for some options (most are still missing).
6. Fixed a minor bugs which would only occur under incredibly rare circumstances.
7. Basic title screen with standard buttons and the title.
8. As the code is still being refactored, a round can't currently be completely played.

Future Features
1. GUI elements for every component of the game, for the cards and deck they will be drawn on screen.
2. Updated elements for components that are already displayed.
3. Tool tips everywhere that makes sense.
4. Allow the user to use Enter and Esc in place of pressing Confirm/Yes Clear/No.
5. Complete the about win which will show the game version and people who worked on the game.
6. Print the log to file when the game is over.
7. Decide how many decks should be used based on the number of players.
8. Slow down the game to allow for the user to understand what is happening without reading the log.
9. Complete the statistics window which will show tthe currrent money of each player, how much they've wagered and a bunch of other things.
10. Dynamically generate CPU characters initial bets, insurance, and round descisions.

Console I/O Version 1.1.0
Change Log:
1. Aces values are now handled correctly, their values correctly switch when the value would push the player over blackjack and increase when that's not going to happen.
2. When the player chooses to hit they can now see each new card added to their hand.
3. Aces are now handled correctly in splits, their values return to 11 and will correctly split into 2 hands.
4. Now keeps track of whether the player is bankrupt, and displays if they are after in the end game statistics.
5. More fleshed out tracking of statistics and more statistics included in the game statistics, icluding but not limited to total wager, total insurance, and total winnings.
6. No longer stuck in an infinite loop if a player chooses to take insurance when they have no money remaining.
7. No longer locked in initial bet if a player does not have enough money to continue.

Future Features
1. Refactor all code away from console IO to a GUI.
2. Dynamically generate CPU characters initial bets, insurance, and round descisions.
3. Prompt the user for the number of human and AI players.
4. Prompt the user for their name.

Console Version I/O 1.0.0
<br>Known Bugs
1. Ace values do not change to 1 if 11 would put the player over 21.
2. When 1 is fixed aces will be treated as different cards in a split because there values differ.
3. If a player does not have enough money to make a wager, the game will perpetually be locked in initialBet.

Future Features
1. Refactor all code away from console IO to a GUI.
2. Dynamically generate CPU characters initial bets, insurance, and round descisions.
