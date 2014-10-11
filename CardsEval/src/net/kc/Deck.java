package net.kc;

import java.util.ArrayList;

public class Deck {
	private ArrayList<Card> deck = new ArrayList<Card>();
	/*
	 * Construct a Deck of 52 cards
	 */
	public Deck()
	{
		String[] suits = Card.validSuits();
		String[] ranks = Card.validRanks();
		for (int i=0; i<suits.length; i++)
			for (int j=0; j<ranks.length ; j++) {
				Card card = new Card(suits[i],ranks[j]);
				deck.add(card);
			}
	}
	/*
	 * draw a random card from deck
	 */
	public Card drawCard ()
	{
		if (!this.isEmpty()) {
			int index = (int)(Math.random()*deck.size()); 
			Card returnCard = deck.get(index);
			deck.remove(index);
			return returnCard;
		}
		else 
			return null;
	}
	
	public boolean isEmpty()
	{
		if (deck.isEmpty())
			return true;
		else 
			return false;
	}
}
