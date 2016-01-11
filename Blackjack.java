public class Blackjack {
	public static void main (String[] args) {
		System.out.println("Welcome to Blackjack!");
		
		//determines whether the group of players want to keep playing
		String keepPlaying = "yes";
		
		//asks for the number of players playing the game
		System.out.println();
		System.out.println("How many players?"); 
		int c = IO.readInt();
		
		//array of people playing the game
		Person[] p = new Person[c+1];
		
		//the person at index 0 is the dealer 
		p[0]= new Person(0,0);
		
		//initializes all of the players with unique ID #s and amount of money in their bank
		System.out.println();
		for (int i=1; i<p.length; i++) {
			System.out.println("How much money does Player "+i+" have in the bank?");
			double a = IO.readDouble();
			while (a<0) {
				IO.reportBadInput();
				System.out.println("How much money does Player "+i+" have in the bank?");
				a = IO.readDouble();
			}
			p[i] = new Person(i,a);
		}
		
		do { 
			//resets the score & isWinner variables every round for each player
			for (int i=0; i<p.length; i++) {
				p[i].resetScore();
				p[i].resetWin();
			}

			//Deck is created & shuffled
			Deck d = new Deck();
			
			//small message before beginning the game
			System.out.println();
			System.out.println("We will now begin the game!");
			System.out.println();
			
			//asks how much each player wants to wager
			for (int i=1; i<p.length; i++) {
				if (p[i].getBankroll()==0) 
					System.out.println("Player "+i+" has $0 in bank and cannot wager.");
				else {
					System.out.println("How much does Player "+i+" want to wager? ($"+p[i].getBankroll()+" in bank)");
					double b =IO.readInt();
					while (b<0 || b>p[i].getBankroll()) {
						IO.reportBadInput();
						System.out.println("How much does Player "+i+" want to wager?");
						b = IO.readDouble();
					}
					p[i].changeBet(b);
				}
			}
			
			//dealer deals two cards to each player & to him/herself and adds on to the total score for each player 
			Card[] c1 = new Card[p.length];
			Card[] c2 = new Card[p.length];
			for (int i=0; i<p.length; i++) {
				c1[i]=d.deal();
				c2[i]=d.deal();
				if (c1[i].getValue()==1 && (21-p[i].getScore()>=11)) 
					p[i].addToScore(10+c1[i].getValue());
				else 
					p[i].addToScore(c1[i].getValue());
				if (c2[i].getValue()==1 && (21-p[i].getScore()>=11)) 
					p[i].addToScore(10+c2[i].getValue());
				else 
					p[i].addToScore(c2[i].getValue());
				if (p[i].getScore()>21)
					p[i].isNotWin();
			}
			System.out.println("The Dealer just dealt 2 cards to himself and all of the players.");
			System.out.println("Here are all of the cards dealt to the players:");
			
			//prints out all of the cards the dealer dealt to the players
			System.out.println("---------------------------------");
			for (int i=1; i<p.length; i++) {
				System.out.println("Player "+i+", Card 1: "+c1[i]);
				System.out.println("Player "+i+", Card 2: "+c2[i]);
				System.out.println("Player "+i+" Score: "+p[i].getScore());
				System.out.println("---------------------------------");
			}
			
			//prints out the faced up card that the dealer dealt to himself
			System.out.println();
			System.out.println("The dealer dealt himself two cards: one faced down & the other faced up"); 
			System.out.println("Dealer's faced up card: "+c1[0]);
			System.out.println();
			
			//insurance code
			if (c1[0].getFace()==1) {
				for (int i=1; i<p.length; i++) {
					System.out.println("Player "+i+", the dealer has an Ace, would you like to take insurance? (y or n)");
					String j = IO.readString();
					if (j.equalsIgnoreCase("y")) {
						p[i].tookIbet();
						System.out.println("Player "+i+", what is yours insurance bet amount? (max insurance bet is $"+(.5*p[i].getBet())+")");
						double ibet = IO.readDouble();
						while (ibet<0 || ibet>(.5*p[i].getBet())) {
							IO.reportBadInput();
							ibet = IO.readDouble();
						}
						p[i].changeIBet(ibet);
						if (p[0].getScore()==21) {
							p[i].changeInsurance();
							p[i].addToBankroll(2*ibet);
						}
						else 
							p[i].minusFromBankroll(ibet);
					}
				}
			}
		
			//hit, stand, and double down code for each of the players
			System.out.println();
			for (int i=1; i<p.length; i++) {
				if (!p[i].isWin()) 
					System.out.println("Player "+i+" has busted with a score of "+p[i].getScore()+"!");
				else {
					System.out.println("Player "+i+", do you want to hit or stand or double down with your "+p[i].getScore()+" points? (h or s or dd)");
					
					System.out.println("If you'd like a hint, press h.");
					String a=IO.readString();
					//if the cards are the same value, it prints hints targeted towards splits
					if (a.equalsIgnoreCase("h") && c1[i].getValue()==c2[i].getValue()) {
						getHintSplits(c1[0],c1[i],c2[i]);
						System.out.println("Player "+i+", do you want to hit or stand or double down with your "+p[i].getScore()+" points? (h or s or dd)");
						a=IO.readString();
					}
					//if one of the cards dealt is an Ace, it prints hints targeted towards an Ace card
					else if (a.equalsIgnoreCase("h") && (c1[i].getValue()==1 || c2[i].getValue()==1)) {
						getHintsAces(c1[0],c1[i],c2[i]);
						System.out.println("Player "+i+", do you want to hit or stand or double down with your "+p[i].getScore()+" points? (h or s or dd)");
						a=IO.readString();
					}
					//if the cards don't have an Ace or are able to split, it uses value of hand to calculate a hint
					else if (a.equalsIgnoreCase("h")) {
						getHintsNorm(c1[0],p[i].getScore());
						System.out.println("Player "+i+", do you want to hit or stand or double down with your "+p[i].getScore()+" points? (h or s or dd)");
						a=IO.readString();
					}
					while (a.equalsIgnoreCase("h") && p[i].isWin()) {
						Card b=d.deal();
						if (b.getValue()==1 && (21-p[i].getScore()>=11)) 
							p[i].addToScore(10+b.getValue());
						else 
							p[i].addToScore(b.getValue());
						System.out.println("Player "+i+" you've been dealt "+b+" with a total a score of "+p[i].getScore()+".");
						if (p[i].getScore()>21){
							System.out.println("I'm sorry Player "+i+" but you busted with a score of "+p[i].getScore()+"!");
							p[i].isNotWin();
						}
						else {
							System.out.println("Player "+i+", do you want to hit or stand or double down? (h or s or dd)");
							a=IO.readString();
						}
					}
					if (a.equalsIgnoreCase("s")) {
						System.out.println("Player "+i+" has chosen to stand.");
					}
					else if (a.equalsIgnoreCase("dd")) {
						System.out.println("How much would you like to double down? (max double down bet is $"+p[i].getBet()+")");
						double newBet=IO.readDouble();
						while (newBet<0 || newBet>p[i].getBet()) {
							IO.reportBadInput();
							newBet=IO.readDouble();
						}
						double originalBet = p[i].getBet();
						p[i].changeBet(newBet+originalBet);
						Card b=d.deal();
						if (b.getValue()==1 && (21-p[i].getScore()>=11)) 
							p[i].addToScore(10+b.getValue());
						else 
							p[i].addToScore(b.getValue());
						System.out.println("Player "+i+" you've been dealt "+b+" with a total a score of "+p[i].getScore()+".");
						if (p[i].getScore()>21){
							System.out.println("I'm sorry Player "+i+" but you busted with a score of "+p[i].getScore()+"!");
							p[i].isNotWin();
						}
					}
				}
			}
			
			//the dealers turn to play
			System.out.println();
			System.out.println("Now the dealer will play:");
			System.out.println("Dealer's faced down card: "+c2[0]);
			while(p[0].getScore()<=17) {
				Card b=d.deal();
				if (b.getValue()==1 && (21-p[0].getScore()>=11)) 
					p[0].addToScore(10+b.getValue());
				else 
					p[0].addToScore(b.getValue());
			}
			System.out.println("Dealer's total: "+p[0].getScore());
			
			//shows which player(s)/dealer win/lose/push the round
			System.out.println();
			if (p[0].getScore()>21) {
				System.out.println("The Dealer Busts!");
				for (int i=1; i<p.length; i++) {
					if (p[i].isWin()) 	{
						p[i].addToBankroll(1.5*p[i].getBet());
						System.out.println("Player "+i+" wins & gets $"+(1.5*p[i].getBet())+"!");
					}
					else {
						p[i].minusFromBankroll(p[i].getBet());
						System.out.println("Player "+i+" loses & loses $"+p[i].getBet()+"!");
					}
				}
			}
			else {
				for (int j=1; j<p.length; j++) {
					if (p[j].getScore()==p[0].getScore() && p[j].isWin())
						System.out.println("Player "+j+" pushes & neither wins nor loses any money!");
					else if (p[j].getScore()>p[0].getScore() && p[j].isWin()) {
						p[j].addToBankroll(1.5*p[j].getBet());
						System.out.println("Player "+j+" wins & gets $"+(1.5*p[j].getBet())+"!");
					}
					else if (p[j].getScore()<p[0].getScore() && p[j].isWin()) {
						p[j].minusFromBankroll(p[j].getBet());
						System.out.println("Player "+j+" loses & loses $"+p[j].getBet()+"!");
					}
					else {
						p[j].minusFromBankroll(p[j].getBet());
						System.out.println("Player "+j+" loses & loses $"+p[j].getBet()+"!");
					}
				}
			}
			
			//prints out insurance bet winnings
			System.out.println();
			for (int i=0; i<p.length; i++) {
				if (p[i].getInsurance())
					System.out.println("Also, Player "+i+", you win $"+2*p[i].getIBet()+" from betting insurance!");
				else if (p[i].gettookIbet() && !p[i].getInsurance())
					System.out.println("Also, sorry Player "+i+", you lost $"+p[i].getIBet()+" due to the insurance bet.");
			}
					
			//asks the players if they would like to play again
			System.out.println();
			System.out.println("Do you, as a group of players, want to play again? (y or n)");
			keepPlaying=IO.readString();
		} while (keepPlaying.equalsIgnoreCase("y"));
		
		//prints out how much the players end with
		System.out.println();
		for (int i=1; i<p.length; i++) 
			System.out.println(p[i]);
		System.out.println();
		System.out.println("Thank You For Playing Blackjack!");
	}
	
	//method that prints out hints depending on the player's hand & dealers faced up card
	//link to decisions table used: http://www.blackjackclassroom.com/basic-strategy-charts/
	public static void getHintSplits (Card d, Card p1, Card p2) {
			if (p1.getValue()==1 || p1.getValue()==8) 
				System.out.println("(HINT: split hand)");
			else if (p1.getValue()==10) 
				System.out.println("(HINT: stand");
			else if (p1.getValue()==9 && d.getValue()==7) 
				System.out.println("(HINT: stand)");
			else if (p1.getValue()==9 && d.getValue()==10) 
				System.out.println("(HINT: stand)");
			else if (p1.getValue()==9 && d.getValue()==1) 
				System.out.println("(HINT: stand)");
			else if (p1.getValue()==9) 
				System.out.println("(HINT: stand)");
			else if (p1.getValue()==7 && (d.getValue()==8 || d.getValue()==9 || d.getValue()==1)) 
				System.out.println("(HINT: hit)");
			else if (p1.getValue()==7 && d.getValue()==10) 
				System.out.println("(HINT: stand)");
			else if (p1.getValue()==7) 
				System.out.println("(HINT: split hand)");
			else if (p1.getValue()==6 && (d.getValue()==1 || d.getValue()==7 || d.getValue()==8 || d.getValue()==9 || d.getValue()==10)) 
				System.out.println("(HINT: hit)");
			else if (p1.getValue()==6) 
				System.out.println("(HINT: split)");
			else if (p1.getValue()==5 && (d.getValue()==10 || d.getValue()==1)) 
				System.out.println("(HINT: hit)"); 
			else if (p1.getValue()==5)
				System.out.println("(HINT: double down)");
			else if (p1.getValue()==4 && (d.getValue()==5 || d.getValue()==6)) 
				System.out.println("(HINT: double down)");
			else if (p1.getValue()==4) 
				System.out.println("(HINT: double down)");
			else if (p1.getValue()==3 && (d.getValue()==4 || d.getValue()==5 || d.getValue()==6 || d.getValue()==7))
				System.out.println("(HINT: split)");
			else if (p1.getValue()==3) 
				System.out.println("(HINT: hit)");
			else if (p1.getValue()==2 && (d.getValue()==3 || d.getValue()==4 || d.getValue()==5 || d.getValue()==6 || d.getValue()==7))
				System.out.println("(HINT: split)");
			else if (p1.getValue()==2) 
				System.out.println("(HINT: hit)");
	}
	
	public static void getHintsAces (Card d, Card c1, Card c2) {
		if (c2.getValue()==1) {
			Card temp = c1;
			c1=c2;
			c2=temp;
		}
		if ((c2.getValue()==2 || c2.getValue()==3 || c2.getValue()==4 || c2.getValue()==5) && (d.getValue()==4 || d.getValue()==5 || d.getValue()==6)) 
			System.out.println("(HINT: double down)");
		else if (c2.getValue()==2 || c2.getValue()==3 || c2.getValue()==4 || c2.getValue()==5)
			System.out.println("(HINT: hit)");
		else if (c2.getValue()==6 && (d.getValue()==2 || d.getValue()==3 || d.getValue()==4 || d.getValue()==5 || d.getValue()==6))
			System.out.println("(HINT: double down)");
		else if (c2.getValue()==6) 
			System.out.println("(HINT: hit)");
		else if (c2.getValue()==7 && (d.getValue()==3 || d.getValue()==4 || d.getValue()==5 || d.getValue()==6 ))
			System.out.println("(HINT: double down)");
		else if (c2.getValue()==7 && (d.getValue()==9 || d.getValue()==10 ))
			System.out.println("(HINT: hit)");
		else if (c2.getValue()==7) 
			System.out.println("(HINT: stand)");
		else if (c2.getValue()==8 && d.getValue()==6)
			System.out.println("(HINT: double down)");
		else if (c2.getValue()==8)
			System.out.println("(HINT: stand)");
		else if (c2.getValue()>=10) 
			System.out.println("(HINT: stand)");

	}
	
	public static void getHintsNorm (Card d, int a) {
		if (a<=7 && a>=5) 
			System.out.println("(HINT: hit)");
		else if (a==8 && (d.getValue()==5  || d.getValue()==6))
			System.out.println("(HINT: double down)");
		else if (a==9 && (d.getValue()==2 || d.getValue()==3 || d.getValue()==4 || d.getValue()==5 || d.getValue()==6))
			System.out.println("(HINT: double down)");
		else if (a==9) 
			System.out.println("(HINT: hit)");
		else if (a==10 && (d.getValue()==10 || d.getValue()==1))
			System.out.println("(HINT: hit)");
		else if (a==10) 
			System.out.println("(HINT: double down)");
		else if (a==11)
			System.out.println("(HINT: double down)");
		else if (a==12 && (d.getValue()==4 || d.getValue()==5 || d.getValue()==6))
			System.out.println("(HINT: stand)"); 
		else if (a==12) 
			System.out.println("(HINT: hit)");
		else if ((a==13 || a==14 || a==15 || a==16) && (d.getValue()==2 || d.getValue()==3 || d.getValue()==4 || d.getValue()==5 || d.getValue()==6))
			System.out.println("(HINT: stand)");
		else if (a==13 || a==14 || a==15 || a==16)
			System.out.println("(HINT: hit)");
		else if (a>=17) 
			System.out.println("(HINT: stand)");
		}
		
}
