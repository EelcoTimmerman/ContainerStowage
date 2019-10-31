package stowage;
import java.util.ArrayList;
import java.util.List;

import containersAndBoat.Container;

public class Terminal {
	public int id;//Dry port has ID 0
	public List<Container> deliveredExport = new ArrayList<>();
	public List<Container> unDeliveredExport = new ArrayList<>();
	public List<Container> loadedImport = new ArrayList<>();	
	public List<Container> unloadedImport = new ArrayList<>();
	public List<Container> shiftedContainers = new ArrayList<>();//This list should always be empty after sailing away from the terminal
	
	public Terminal(int id) {
		this.id = id;
	}
	
	public void report() {
		
	}
	
	public void talk() {
		System.out.printf("The boat is currently at terminal "+ this.id + "%n");
	}
	
	public void unloadExport(Container c) {
		deliveredExport.add(c);
		unDeliveredExport.remove(c);
	}	
	
	public void loadImport(Container c) {
		loadedImport.add(c);
		unloadedImport.remove(c);
	}
	
	public void initExport(Container c) {
		unDeliveredExport.add(c);
	}
	
	public void initImport(Container c) {
		unloadedImport.add(c);
	}
	
	public void addToShift(Container c) {
		shiftedContainers.add(c);
	}
	
	public void removeFromShift(Container c) {
		shiftedContainers.remove(c);
	}
	
}
