package org.blim.whist.player;

import java.util.List;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.blim.whist.game.Card;
import org.blim.whist.game.Game;
import org.blim.whist.game.Trick;
import org.blim.whist.game.Card.Suit;

import com.google.common.collect.Lists;

@Entity
public class BasicComputerPlayer extends ComputerPlayer {

	public static final String computerType = "Basic";
	
	public BasicComputerPlayer() {
	}

	public BasicComputerPlayer(ComputerPlayer another) {
		super(another);
	}
	
	@Override
	@Transient
	public String getComputerType() {
		return computerType;
	}

	@Override
	public Integer bid(Game game) {
		
		Random rand = new Random();
		Integer bid;
		
		// Are we the last to bid 
		if (game.getCurrentRound().getNumberOfBids() == (game.getPlayers().size() - 1)) {
			// Get random number in the right range
			bid = rand.nextInt(game.getCurrentRound().getNumberOfCards() - 1) + 1;
			
			// how many has been bid so far
			Integer bidCount = 0, playerBid;
			
			for (int i = 0; i < game.getCurrentRound().getBids().size(); i++) {
				playerBid = game.getCurrentRound().getBids().get(i);
				if (playerBid != null) {
					bidCount += playerBid;
				}
			}

			if (bid >= bidCount - game.getCurrentRound().getNumberOfCards()) {
				bid += 1;
			}
		} else {
			bid = rand.nextInt(game.getCurrentRound().getNumberOfCards()) + 1;
		}

		return bid;
	}

	@Override
	public Suit chooseTrumps(Game game) {
		List<Suit> suits = Lists.newArrayList();
		
		suits.add(Suit.CLUBS);
		suits.add(Suit.DIAMONDS);
		suits.add(Suit.HEARTS);
		suits.add(Suit.SPADES);
		suits.add(Suit.NO_TRUMPS);

		Random rand = new Random();
		
		return suits.get(rand.nextInt(5));
	}

	@Override
	public Card playCard(Game game) {
		Trick currTrick = game.getCurrentRound().getTricks().get(game.getCurrentRound().getTricks().size() - 1);
		List<Card> handCards = game.getCurrentRound().getHands().get(game.activePlayer()).getCards();
		
		Random rand = new Random();
		
		if (currTrick.getNumberOfCards() > 0) {
			Suit firstCardSuit = currTrick.getCards().get(currTrick.getFirstPlayer()).getSuit();
			
			List<Card> suitableHandCards = Lists.newArrayList();
			
			for (Card handCard : handCards) {
				if (firstCardSuit == handCard.getSuit()) {
					suitableHandCards.add(handCard);
				}
			}
			
			if (suitableHandCards.size() > 0) {
				Integer randCardIndex = rand.nextInt(suitableHandCards.size()) + 1;
				return suitableHandCards.get(randCardIndex);
			}
		}
		
		Integer randCardIndex = rand.nextInt(game.getCurrentRound().getHands().get(game.activePlayer()).getCards().size()) + 1;
		Card card = game.getCurrentRound().getHands().get(game.activePlayer()).getCards().get(randCardIndex);

		return card;
	}

}
