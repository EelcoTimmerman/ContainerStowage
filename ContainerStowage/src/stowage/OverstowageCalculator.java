package stowage;
import java.util.ArrayList;
import java.util.List;

import containersAndBoat.Boat;
import stowage.TerminalSet;

public class OverstowageCalculator {
	public int nrOfTerminals;
	public int route;
	public Boat boat;
	public List<Terminal> terminals = new ArrayList<>();
	public int[][] routes = new int[TerminalSet.nrOfRoutes][TerminalSet.nrOfTerminals];
	
	public OverstowageCalculator(Boat boat, int[][] route, List<Terminal> terminals){
		this.boat = boat;
		routes = route;
		this.terminals = terminals;
	}
	
	public void reportAllRoutes() {
		for(int i=0;i<TerminalSet.nrOfRoutes;i++) {
			reportRoute(i);
		}
	}
	
	public void reportRoute(int r) {	
		System.out.print("This is the case of route 1, the stowage when arriving at the first terminal is:\n");
		boat.showStowage();
		for(int i =0;i<TerminalSet.nrOfTerminals-1;i++) {		
		int index = routes[r][i];
		Terminal t = TerminalSet.getTerminal(index);
		if(t.id == 0) {
			System.out.print("Error, should ot be allowed to get the dryport..");
		}
		t.talk();
		boat.visitTerminal(t, boat);
		//cset.allTellPosition();
		}
		
	}
}
