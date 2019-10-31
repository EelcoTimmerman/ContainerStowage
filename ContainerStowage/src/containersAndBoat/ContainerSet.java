package containersAndBoat;
import containersAndBoat.Container;
import stowage.CreateStowage;
import stowage.Terminal;
import stowage.TerminalSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ContainerSet {
	public int nrOfContainers = 25;
	public double probOfExport = 0.5;
	public double probOf20foot = 0.5;
	public TerminalSet tset;
	public List<Container> containers = new ArrayList<>();
	
	public ContainerSet (TerminalSet terminal) {
		this.tset = terminal;
	}
	
	public void createSetOfContainers() {
		int id = 0;
		Random rand = new Random();
		

		
		for(int i=0; i < nrOfContainers; i++) {
			Container c = new Container(id);
			id++;
			int ind = rand.nextInt(tset.nrOfTerminals -1) + 1; // cs worden verdeeld over term 1--T
			c.setDestination(tset.terminals.get(ind));
			int n = rand.nextInt(100);
			if(n>probOfExport*100) { //This can be found in fazi's paper
				c.export = true;
				//need to be added to the export list of the terminal
				tset.terminals.get(ind).initExport(c);
			}else {
				c.export = false;
				tset.terminals.get(ind).initImport(c);
			}
			int shh = rand.nextInt(100);
			if(shh>probOfExport*100) {
				c.setType(ContainerType.FORTY);
			}else {
				c.setType(ContainerType.TWENTY);
			}

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
	
	public List<Container> getContainers() {
		return containers;	
	}
	
	public void reportPerformance() {
		//do this only at the end..
			int totalTransported =0;
		for(Container c: containers) {
			if(c.transported == true) {
				totalTransported++;
			}
		}
		System.out.print("RESULT:\n");
		//System.out.printf("Total transported: "+totalTransported+" containers, total shifted: "+CreateStowage.overstowageCount+" containers, Objective: ..\n");
	}
}
