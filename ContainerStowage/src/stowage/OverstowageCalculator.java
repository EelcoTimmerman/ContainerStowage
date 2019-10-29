package stowage;
import containersAndBoat.Boat;

public class OverstowageCalculator {
	public int nrOfTerminals;
	public int route;
	public Boat boat;
	public 
	
	public OverstowageCalculator(int n, Boat boat){
		route = n;
		this.boat = boat;
	}
	
	public void reportRoute(int r) {
		
		cset.reportPerformance();
	}
	for(Terminal t: TerminalSet.terminals) {
		if(t.id == 0 ) {
			System.out.printf("Hey I am terminal 0, the starting stowage is: %n");
			boat.showStowage();
			cset.allTellPosition();
			continue;
		}		
		t.talk();
		boat.visitTerminal(t, boat);
		cset.allTellPosition();
	}
}
