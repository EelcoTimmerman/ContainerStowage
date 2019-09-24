package stowage;
import java.util.List;
import containersAndBoat.Container;

public class Terminal {
	public int id;
	public List<Container> exportSet;
	public List<Container> importSet;
	
	public Terminal(int id) {
		this.id = id;
	}
	
	public void talk() {
		System.out.printf("Hey I am terminal "+ this.id + "%n");
	}
	
}
