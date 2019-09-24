package containersAndBoat;
import containersAndBoat.Container;
import stowage.Terminal;
import stowage.TerminalSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ContainerSet {
	public static int nrOfContainers = 5;
	public static double probOfExport = 0.5;
	public List<Container> containers = new ArrayList<>();
	
	public void createSetOfContainers() {
		int id = 0;
		Random rand = new Random();
		
		for(int i=0; i < nrOfContainers; i++) {
			Container cont = new Container(id);
			id++;
			int n = rand.nextInt(100);
			if(n>probOfExport*100) { //This can be found in fazi's paper
				cont.setType(ContainerType.TWENTY);
			}else {
				cont.setType(ContainerType.TWENTY);
			}
			cont.weight = n;//This is not the actual weight yet, still need to look for that.
			cont.setDestination(TerminalSet.terminals.get(1));
			containers.add(cont);
			}
	}
	
	public void allTalk() {
		for(Container c: containers) {
			c.talk();
		}
	}
	
	public List<Container> getContainers() {
		return containers;
		
	}
}
