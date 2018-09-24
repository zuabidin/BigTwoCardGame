# Big Two Card Game

A functional card game, Big Two, with multiplayer support on a network in JAVA.

# Features
1. Object Oriented Programming approach 
2. Java GUI for the client
3. Handle networking using Multi-threading
4. Handle errors using Exceptions
5. Java Sockets to handle network multiplayer
6. All classes and methods have been properly documented

# How to play
Reference: http://onlyagame.typepad.com/only_a_game/2008/04/big-two-rules.html

The basis of Big Two is a race to get rid of your cards. It supports 2, 3 or 4 players with one deck, and up to 8 players with two decks shuffled together. You will be dealt 13 cards in each game, and you can play these cards in four different ways:

As Singles (just one card)
As Pairs (two cards of matching values)
As Triplets or “Trips” (three cards of matching values)
As Poker Hands (five cards forming a straight, flush, full house, four of a kind or straight flush)
Note: you must play a fifth card with four of a kind to make a legitimate five card poker hand.
The game consists of a number of hands, each consisting of a number of rounds. Each hand begins by the players being dealt 13 cards (deal passes to the left after each hand).

The player who is dealt the 3 of Diamonds starts each hand (see below for how to discover this), and must make a play involving this card to begin the first round. For instance, they could just play the 3 of Diamonds as a single, or if they had a straight involving this 3, they could lead with that.

(If no-one has the 3 of Diamonds, the player with the next lowest card leads with that instead).

Whichever type of play is led, the next player clockwise around the table must play a higher card (or combination of cards) of the same type – for instance, if the player with the 3 of Diamonds plays a pair of 3’s to start the round, the next player must play a pair of a higher value.

Players can choose to pass if they don’t want to play, and must pass if they cannot play. When all other players have passed, the last player to successfully make a play has possession and can begin a new round with whatever play they wish.

Whatever type of play begins a round, all subsequent plays must be of the same type – for instance, if a player begins a round with a straight, the next player must play a higher straight or a better poker hand (e.g. a flush, full house, four of a kind or a straight flush).

The hand ends when someone successfully plays their last card. The winning player scores one point for each card in every other player’s hand, and this score is doubled for each 2 in the final play – for instance, if the player goes out with a pair of 2’s, their score is multiplied by 4 (2x2), and if they go out with four of a kind in 2’s their score is multiplied by 16 (2x2x2x2).

Play to 49 points or whatever score you choose.
