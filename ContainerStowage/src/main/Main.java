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
		TerminalSet terminals = new TerminalSet();
		Boat boat = new Boat();
		ContainerSet cset = new ContainerSet(terminals);
		cset.createSetOfContainers();
		List <Container> containers = cset.getContainers();
		CreateStowage stowagePlanner = new CreateStowage(containers, boat);
		stowagePlanner.createInitialStowage();
		ALNS alns = new ALNS();
		int i = 0;
		int maxIt = 1;
		double bestSolution = 0;
		double solution = 0;
		while(i< maxIt) {
			alns.alterSolution();
			solution = alns.calculateObjective(boat);
			if(solution > bestSolution) {
				bestSolution = solution;
			}
			i++;
		}
	}


}
