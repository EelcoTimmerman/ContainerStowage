package stowage;
import java.util.ArrayList;
import java.util.List;

import containersAndBoat.Boat;
import stowage.TerminalSet;
import containersAndBoat.ContainerSet;
import containersAndBoat.Container;

public class OverstowageCalculator {
	public int nrOfTerminals;
	public int route;
	public Boat boatCopy;
	public List<Terminal> terminalsCopy = new ArrayList<>();
	public List<Container> containersCopy = new ArrayList<>();
	public int[][] routes = new int[TerminalSet.nrOfRoutes][TerminalSet.nrOfTerminals];
	
	public OverstowageCalculator(List<Terminal> terminals, Boat boat, List<Container> containers, int[][] routes){		
		copyTerminals(terminals);
		copyContainers(containers);		
		this.boatCopy = new Boat();
		boatCopy.stowage = boat.stowage;//use.clone maybe?
		this.routes = routes;
	}
	
	public void copyTerminals(List<Terminal> terminals) {
		for(Terminal t:terminals) {
			Terminal tCopy = new Terminal(t.id);
			//System.out.printf("Hey I am copy terminal nr "+tCopy.id+" \n");
			terminalsCopy.add(tCopy);
		}
	}
	
	public void copyContainers(List<Container> containers) {
		for(Container c:containers) {
			Container cCopy = new Container(c.id);
			int dest = c.destination.id;
			cCopy.export = c.export;
			for(Terminal t: terminalsCopy) {
				if(t.id == dest) {
					cCopy.destination = t;
					if(cCopy.export) {
						t.unDeliveredExport.add(cCopy);
					}else {
						t.unloadedImport.add(cCopy);
					}
				}
			}
			cCopy.isOnBarge = c.isOnBarge;
			cCopy.type = c.type;
			cCopy.weight = c.weight;
			cCopy.xLoc = c.xLoc;
			cCopy.yLoc = c.yLoc;
			cCopy.zLoc = c.zLoc;
			containersCopy.add(cCopy);
		}
	}
	
	public void reportAllRoutes() {
		for(int i=0;i<TerminalSet.nrOfRoutes;i++) {
			reportRoute(i);
		}
	}
	
	public void reportRoute(int r) {	
		int route = r+1;
		System.out.printf("This is the case of route "+route+", the stowage when arriving at the first terminal is:\n");
		boatCopy.showStowage();
		for(int i =0;i<TerminalSet.nrOfTerminals-1;i++) {		
		int index = routes[r][i];
		for(int s=0;s<3;s++) {
			System.out.printf("First three stops of the route:"+routes[r][s]+"\n");
		}
		for(Terminal t: terminalsCopy) {
			if(t.id == index) {
				if(t.id == 0) {
					System.out.print("Error, should not be allowed to get the dryport..");
				}
				t.talk();
				boatCopy.visitTerminal(t, boatCopy, containersCopy);
			}
		}

		//cset.allTellPosition();
		}
		
	}
}
