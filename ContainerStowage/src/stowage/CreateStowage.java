package stowage;
import containersAndBoat.ContainerSet;
import containersAndBoat.Container;
import java.util.List;


public class CreateStowage {
//take as input the set of containers that has been created,
	 public List<Container> containers;
	 public CreateStowage(List<Container> container) {
		 this.containers = container;
	 }
	
	
	  public void talk() {
		  for(Container c: containers) { c.talk(); }
		  }
	 
	 
	 public void createInitialStowage() {
		 for(Container c: containers) {
			 
		 }
		 
	 }
//then look for try to put every container in a spot
}
