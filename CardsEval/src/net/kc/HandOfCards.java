package net.kc;

import java.util.ArrayList;
import java.util.Collections;

public class HandOfCards {
	private ArrayList<Card> hand = new ArrayList<Card>();
	private static final int LIMIT = 17;
	/*
	 * deal 17 cards from a new deck
	 */
	public HandOfCards ()
	{
		Deck deck = new Deck();
		Card card = new Card();
		int i=1;
		while (!deck.isEmpty()&& i<=LIMIT) {
			card = deck.drawCard();
			hand.add(card);
			i++;
		}
	}
	
	public void showHand ()
	{
		for (int i=0 ; i<LIMIT;i++)
		{
			System.out.println("#"+i+"	"+hand.get(i).getSuit()+hand.get(i).getRank());
		}
	}
	public int result ()
	{
		ArrayList<Integer> spadeList = new ArrayList<Integer>();
		ArrayList<Integer> diamondList = new ArrayList<Integer>();
		ArrayList<Integer> heartList = new ArrayList<Integer>();
		ArrayList<Integer> clubList = new ArrayList<Integer>();
		Card card = new Card();
		for (int i=0 ; i<LIMIT; i++)
		{
			card = hand.get(i);
			// insert the rank of each card to correspond array
			if (card.getSuit().equals("Spade"))
				spadeList.add(Integer.parseInt(card.getRank()));
			if (card.getSuit().equals("Diamond"))
				diamondList.add(Integer.parseInt(card.getRank()));
			if (card.getSuit().equals("Heart"))
				heartList.add(Integer.parseInt(card.getRank()));
			if (card.getSuit().equals("Club"))
				clubList.add(Integer.parseInt(card.getRank()));	
		}
		// calculate the longest consecutive length of 4 arrays 
		// and return the max of them.
		return Eval.maxInt(longestConsecutiveLength(spadeList),
					longestConsecutiveLength(diamondList),
					longestConsecutiveLength(heartList),
					longestConsecutiveLength(clubList));
	}
	
	/*
	 * get the longest length of consecutive integers in an array 
	 */
	
	public int longestConsecutiveLength (ArrayList<Integer> a)
	{
		Collections.sort(a);   //sort array 
		int max=0;
		int count =1;
		for (int i=1; i<a.size() ;i++)
		{
			if (a.get(i) == a.get(i-1)+1)
				count++;
			else {
				if (count>max)
					max = count;
				count =1;
			}
		}
		
		return max;
	}
	
	
	
}
