package net.kc;

public class Card {
	private String rank;
	private String suit;
	public Card ()
	{
		
	}
	public Card (String st,String rk )
	{
		this.suit = st;
		this.rank= rk;
	}
	public static String[] validRanks ()
	{
		return new String[] {"2","3","4","5","6","7","8","9","10","11","12","13","14"};
	}
	
	public static String[] validSuits ()
	{
		return new String[] {"Spade","Diamond","Heart","Club"};
	}
	
	public String getRank ()
	{
		return rank;
	}
	public String getSuit ()
	{
		return suit;
	}
}
