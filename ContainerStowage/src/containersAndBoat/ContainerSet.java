package containersAndBoat;
import containersAndBoat.Container;
import stowage.Terminal;
import stowage.TerminalSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ContainerSet {
	public static int nrOfContainers = 10;
	public static double probOfExport = 0.5;
	public static List<Container> containers = new ArrayList<>();
	
	public void createSetOfContainers() {
		int id = 0;
		Random rand = new Random();
		
		for(int i=0; i < nrOfContainers; i++) {
			Container c = new Container(id);
			id++;
			int ind = rand.nextInt(TerminalSet.nrOfTerminals -1) + 1;
			c.setDestination(TerminalSet.terminals.get(ind));
			int n = rand.nextInt(100);
			if(n>probOfExport*100) { //This can be found in fazi's paper
				c.export = true;
				//need to be added to the export list of the terminal
				TerminalSet.terminals.get(ind).unloadExport(c);
			}else {
				c.export = false;
				TerminalSet.terminals.get(ind).loadImport(c);
			}
			c.setType(ContainerType.FORTY);

			c.weight = n;//This is not the actual weight yet, still need to look for that.
			//Add this shizzle to the terminals...
			containers.add(c);
			}
	}
	
	public void allTellPosition() {
		for(Container c: containers) {
			c.tellPosition();
		}
	}
	
	public void allTellDestination() {
		for(Container c: containers) {
			c.tellDestination();
		}
	}
	
	public List<Container> getContainers() {
		return containers;
		
	}
}
