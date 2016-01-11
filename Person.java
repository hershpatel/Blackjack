
public class Person {
	private int num; 
	private int score;
	private double bankroll;
	private boolean isWinner;
	private double bet;
	private boolean actuallyHasInsurance;
	private double insuranceBet;
	private boolean ibetornah;
	
	public Person(int num, double bankroll) {
		this.num=num;
		this.bankroll=bankroll;
		score=0;
		isWinner=true;
		bet=0;
		actuallyHasInsurance=false;
		insuranceBet=0;
		ibetornah=false;
	}
	
	public int getScore() {
		return score;
	}
	
	public void addToScore(int a) { 
		score+=a;
	}
	
	public void resetScore() {
		score=0;
	}
	
	public void addToBankroll(double a) {
		bankroll+=a;
	}
	
	public void minusFromBankroll(double a) {
		bankroll-=a;
	}
	
	public double getBankroll() {
		return bankroll;
	}
	
	public boolean isWin() {
		return isWinner;
	}
	
	public void isNotWin() {
		isWinner=false;
	}
	
	public void resetWin() {
		isWinner=true;
	}
	
	public double getBet() {
		return bet;
	}
	
	public void changeBet(double a) {
		bet=a;
	}
	
	public void changeInsurance() {
		actuallyHasInsurance=true;
	}
	
	public boolean getInsurance() {
		return actuallyHasInsurance;
	}
	
	public void changeIBet(double a) {
		insuranceBet=a;
	}
	
	public double getIBet() {
		return insuranceBet;
	}
	
	public void tookIbet() {
		ibetornah=true;
	}
	
	public boolean gettookIbet() {
		return ibetornah;
	}
	public String toString () {
		return "Player "+num+" has ended with $"+bankroll+".";
	}
	
}
