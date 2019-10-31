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
	public TerminalSet tset;
	public List<Terminal> terminalsCopy = new ArrayList<>();
	public List<Container> containersCopy = new ArrayList<>();
	public int[][] routes = new int[TerminalSet.nrOfRoutes][TerminalSet.nrOfTerminals];
	public int overstowage;
	
	public OverstowageCalculator(List<Terminal> terminals, Boat boat, List<Container> containers, int[][] routes, TerminalSet ter, CreateStowage planner){		
		this.tset = ter;
		copyTerminals(terminals);
		copyContainers(containers);		
		this.boatCopy = new Boat(planner);
		for(int z = 0;z<boat.nrOfLayers;z++) {
			for(int y = 0;y< boat.nrOfRows;y++) {
				for(int x=0;x<boat.nrOfBays;x++) {
					boatCopy.stowage[z][y][x] = boat.stowage[z][y][x];
				}
			}
		}
		this.routes = routes;
		this.overstowage = 0;
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
					  }else{ t.unloadedImport.add(cCopy); 
					   		}
				  }		 
			  }
			  cCopy.isOnBarge = c.isOnBarge;
			  cCopy.type = c.type;
			  cCopy.weight = c.weight;
			  cCopy.xLoc = c.xLoc;
			  cCopy.yLoc = c.yLoc;
			  cCopy.zLoc = c.zLoc;			 
			  this.containersCopy.add(cCopy);
		}
	}
	
	public void reportRoute(int r) {	
		int route = r+1;
		System.out.printf("This is the case of route "+route+":\n");
		for(int i =0;i<TerminalSet.nrOfTerminals-1;i++) {		
		int index = routes[r][i];
		for(Terminal t: terminalsCopy) {
			if(t.id == index) {
				if(t.id == 0) {
					System.out.print("Error, should not be allowed to get the dryport..");
				}
				t.talk();
				System.out.print("The stowage when arriving at this terminal is:\n");
				boatCopy.showStowage();
				for(Container c:containersCopy) {
					c.tellPosition();
				}
				int shifts = boatCopy.visitTerminal(t, boatCopy, containersCopy);
				this.overstowage += shifts;
				System.out.printf("The stowage when leaving this terminal is:\n");
				boatCopy.showStowage();
				for(Container c:containersCopy) {
					c.tellPosition();
				}
				System.out.printf("The overstowage for this terminal was "+shifts+"\n");
			}
		}

		}
		System.out.printf("The overstowage for the route "+route+" is "+this.overstowage+"\n");
	}
}
