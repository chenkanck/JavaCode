package net.kc;

public class Eval {
	//the larger the number of samples, the more accurate the result is.
	private final static int numberOfSamples = 10000; 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double sum=0;
		for (int i =0;i < numberOfSamples ;i++) {
		HandOfCards hand = new HandOfCards();
//		hand.showHand();
//		System.out.println(hand.result());
		sum+=hand.result();
		}
		System.out.println("Result:"+sum/numberOfSamples);
	}
	
	
	public static int maxInt (int... values)
	{
		int max = Integer.MIN_VALUE;
		for (int v :values)
			if (v>max) max = v;
		return max;
	}
}
