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
		terminals.decideRoutes();
		Boat boat = new Boat();
		ContainerSet cset = new ContainerSet();
		cset.createSetOfContainers();
		List <Container> containers = cset.getContainers();
		CreateStowage stowagePlanner = new CreateStowage(containers, boat);
		stowagePlanner.createInitialStowage();
		//calculate overstowage route 1.
		OverstowageCalculator Ocalc = new OverstowageCalculator(boat, terminals.routes, terminals.terminals);
		Ocalc.reportRoute(0);
		//calculate overstowage in case of route 2.

	}


}
