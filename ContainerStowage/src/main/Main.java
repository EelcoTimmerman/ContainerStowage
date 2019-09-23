package main;
import java.util.List;
import java.util.Random;

import containersAndBoat.Boat;
import containersAndBoat.Container;
import containersAndBoat.ContainerSet;
import containersAndBoat.ContainerType;
import stowage.CreateStowage;

public class Main {
//Hey this is my first line, but its a comment so it doesnt do anyhting...
	
	public static void main(String[] args) {
		Boat boat = new Boat();
		ContainerSet cset = new ContainerSet();
		cset.createSetOfContainers();
		List <Container> containers = cset.getContainers();
		CreateStowage stowagePlanner = new CreateStowage(containers);
		stowagePlanner.talk();
		//To Do;; give all the containers a location and report this.
	}


}
