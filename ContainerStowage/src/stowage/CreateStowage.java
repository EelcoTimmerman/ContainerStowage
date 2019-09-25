package stowage;
import containersAndBoat.ContainerSet;
import containersAndBoat.Container;
import containersAndBoat.Boat;
import java.util.List;


public class CreateStowage {
//take as input the set of containers that has been created,
	 public List<Container> containers;
	 public Boat boat;
	 
	 public CreateStowage(List<Container> container, Boat boat) {
		 this.containers = container;
		 this.boat = boat;
	 }
	 	 
	 public void createInitialStowage() {
		 for(Container c: containers) {
			 if(c.export) {
				 c.findFeasibleLocation(boat);
			 }
		 }

	 }
	 
	 
	 


}
