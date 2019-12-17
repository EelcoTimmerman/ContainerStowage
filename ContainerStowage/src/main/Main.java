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
		long startTime = System.currentTimeMillis();
		TerminalSet terminals = new TerminalSet();
		Boat boat = new Boat();
		ContainerSet cset = new ContainerSet(terminals);
		cset.createSetOfContainers();
		List <Container> containers = cset.getContainers();
		CreateStowage stowagePlanner = new CreateStowage(containers, boat);
		stowagePlanner.createInitialStowage();
		stowagePlanner.calculateObjective(boat);
		long endTime = System.currentTimeMillis();
		long seconds = (endTime - startTime);
		System.out.printf("Runtime: "+ seconds + " milliseconds");
	}


}
