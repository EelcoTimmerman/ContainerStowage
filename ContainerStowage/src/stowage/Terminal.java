package stowage;
import java.util.ArrayList;
import java.util.List;
import containersAndBoat.Container;

public class Terminal {
	public int id;//Dry port has ID 0
	public List<Container> exportSet = new ArrayList<>();
	public List<Container> importSet = new ArrayList<>();
	
	public Terminal(int id) {
		this.id = id;
	}
	
	public void talk() {
		System.out.printf("Hey I am terminal "+ this.id + "%n");
	}
	
	public void unloadExport(Container c) {
		exportSet.add(c);
	}
	
	public void loadImport(Container c) {
		importSet.remove(c);
	}
	
}
