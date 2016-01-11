public class Deck {
	int count; 
	Card[] d; 
	boolean created = false; 
	int cardsUsed=0;
	
	public Deck() {	
		createDeck();
	}
	
	public void createDeck() { 
		int c = 0; 
		d=new Card[52]; 
		for (int i=0; i<4; i++) {
			for (int j=1; j<14; j++) {
				d[c]= new Card(i,j); 
				c++;
			}
		}
		shuffle();
		created=true;
	}

	public Card deal() {
		if (!created) 
			createDeck();
		if (isEmpty()) { 
			 System.out.println("No cards are left in the deck."); 
			 return null;
		}
		cardsUsed++;
		return d[cardsUsed-1]; 
	}

	public boolean isEmpty() {
		if (d.length==cardsUsed) 
			return true;
		
		else 
			return false; 
	}
	
	public void shuffle() {
	    for (int i=0; i<d.length; i++) {
	        int j= i + (int) (Math.random()*(d.length-i));
	        Card t= d[i]; 
	        d[i] = d[j];
	        d[j] = t;
	    }
	}
}
