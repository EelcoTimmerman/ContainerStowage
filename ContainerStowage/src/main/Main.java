package main;
import java.util.List;
import containersAndBoat.Boat;
import containersAndBoat.Container;
import containersAndBoat.ContainerSet;
import stowage.CreateStowage;
import stowage.TerminalSet;
import stowage.Terminal;
import stowage.OverstowageCalculator;

public class Main {
//Hey this is my first line, but its a comment so it doesnt do anyhting...
	
	public static void main(String[] args) {
		//this is the loading part
		TerminalSet terminals = new TerminalSet();
		int[][] routes = terminals.decideRoutes();
		Boat boat = new Boat();
		ContainerSet cset = new ContainerSet(terminals);
		cset.createSetOfContainers();
		List <Container> containers = cset.getContainers();
		CreateStowage stowagePlanner = new CreateStowage(containers, boat);
		stowagePlanner.createInitialStowage();
		//This is the moment where a boat, a set of containers, a set of terminals should be copied
		for(int r = 0;r<TerminalSet.nrOfRoutes;r++) {
			OverstowageCalculator Ocalc = new OverstowageCalculator(terminals.terminals, boat, containers, routes, terminals, stowagePlanner);
			Ocalc.reportRoute(r);
		}

	}


}
