package containersAndBoat;
import containersAndBoat.Container;
import stowage.CreateStowage;
import stowage.Terminal;
import stowage.TerminalSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ContainerSet {
	public static int nrOfContainers = 40;
	public double probOfExport = 0.5;
	public static double probOf20foot = 1;
	public TerminalSet tset;
	public static List<Container> containers = new ArrayList<>();
	
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
			int rweight = rand.nextInt(100);
			if(n>probOfExport*100) { //This can be found in fazi's paper
				c.export = true;
				//need to be added to the export list of the terminal
				tset.terminals.get(ind).initExport(c);
			}else {
				c.export = false;
				tset.terminals.get(ind).initImport(c);
			}
			int shh = rand.nextInt(100);
			if(probOf20foot == 1) {
				c.setType(ContainerType.TWENTY);
			}else {
				c.setType(ContainerType.FORTY);
			}
			if(rweight <= 33) {
				c.setWeight(1);
			}else if(rweight <= 67) {
				c.setWeight(2);
			}else {
				c.setWeight(3);
			}
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
	
}
