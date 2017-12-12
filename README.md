# Blackjack Changelog
Version 1.1.0
Change Log:
1. Aces values are now handled correctly, their values correctly switch when the value would push the player over blackjack and increase when that's not going to happen.
2. When the player chooses to hit they can now see each new card added to their hand.
3. Aces are now handled correctly in splits, their values return to 11 and will correctly split into 2 hands.
4. Now keeps track of whether the player is bankrupt, and displays if they are after in the end game statistics.
5. More fleshed out tracking of statistics and more statistics included in the game statistics, icluding but not limited to total wager, total insurance, and total winnings.
6. No longer stuck in an infinite loop if a player chooses to take insurance when they have no money remaining.
7. No longer locked in initial bet if a player does not have enough money to continue.

Known Bugs
None currently

Future Features
1. Refactor all code away from console IO to a GUI.
2. Dynamically generate CPU characters initial bets, insurance, and round descisions.
3. Prompt the user for the number of human and AI players.
4. Prompt the user for their name.

Version 1.0.0

Known Bugs
1. Ace values do not change to 1 if 11 would put the player over 21.
2. When 1 is fixed aces will be treated as different cards in a split because there values differ.
3. If a player does not have enough money to make a wager, the game will perpetually be locked in initialBet.

Future Features
1. Refactor all code away from console IO to a GUI.
2. Dynamically generate CPU characters initial bets, insurance, and round descisions.
