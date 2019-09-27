package main;
import java.util.List;
import containersAndBoat.Boat;
import containersAndBoat.Container;
import containersAndBoat.ContainerSet;
import stowage.CreateStowage;
import stowage.TerminalSet;
import stowage.Terminal;

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
		for(Terminal t: TerminalSet.terminals) {
			if(t.id == 0 ) {
				System.out.printf("Hey I am terminal 0, the starting stowage is: %n");
				boat.showStowage();
				cset.allTellPosition();
				continue;
			}		
			t.talk();
			boat.visitTerminal(t, boat);
			cset.allTellPosition();
		}
		cset.reportPerformance();
	}


}
