# Blackjack Changelog
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
5. Complete the about win which will show the game version and people who worked on the game.
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
5. Complete the about win which will show the game version and people who worked on the game.
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
Known Bugs
1. Ace values do not change to 1 if 11 would put the player over 21.
2. When 1 is fixed aces will be treated as different cards in a split because there values differ.
3. If a player does not have enough money to make a wager, the game will perpetually be locked in initialBet.

Future Features
1. Refactor all code away from console IO to a GUI.
2. Dynamically generate CPU characters initial bets, insurance, and round descisions.
