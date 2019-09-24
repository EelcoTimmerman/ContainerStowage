package main;
import java.util.List;
import java.util.Random;

import containersAndBoat.Boat;
import containersAndBoat.Container;
import containersAndBoat.ContainerSet;
import containersAndBoat.ContainerType;
import stowage.CreateStowage;
import stowage.TerminalSet;

public class Main {
//Hey this is my first line, but its a comment so it doesnt do anyhting...
	
	public static void main(String[] args) {
		//this is the loading part
		TerminalSet terminals = new TerminalSet();
		Boat boat = new Boat();
		ContainerSet cset = new ContainerSet();
		cset.createSetOfContainers();
		List <Container> containers = cset.getContainers();
		CreateStowage stowagePlanner = new CreateStowage(containers, boat);
		stowagePlanner.createInitialStowage();
		stowagePlanner.talk();
		boat.showStowage();
		//Here will be the sailing part, and will the costs for every possible route be calculated, times its probability
		
	}


}
