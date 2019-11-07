package stowage;
import containersAndBoat.ContainerType;
import containersAndBoat.Container;
import containersAndBoat.ContainerSet;
import containersAndBoat.Boat;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class CreateStowage {
//take as input the set of containers that has been created,
	 public static List<Container> fixedContainers;
	 public static Boat boat;
	 
	 public CreateStowage(List<Container> containers, Boat boat) {
		 containers.sort(Comparator.comparing(Container::getWeight));
		 fixedContainers = containers;
		 this.boat = boat;
	 }
	 	 
	 public void createInitialStowage() {
		 for(Container c: fixedContainers) {
			 if(c.export) {
				 c.findFeasibleLocation(boat);
			 }
		 }
	 }
	 
	 public static void  removeExport(Boat boat, Terminal terminal, List<Container> copyContainers) {
		 for(int i = boat.nrOfLayers -1;i>-1;i--) {
			 for(Container c: copyContainers) {
				 if(c.export&& c.destination == terminal && c.isOnBarge && c.isInLayer(i)) {
					 int realID = c.id +1;
					 if(hasContainersAbove(c,boat)) {
						 int realiD = c.id +1;
						 System.out.print("Start shifting containers to reach container "+realiD+ " in position " + c.zLoc + c.yLoc + c.xLoc+"\n");
						 shiftContainersAbove(c, boat, terminal, copyContainers);
					 }
					 System.out.printf("Unloading container "+realID+" from position: " + c.zLoc + c.yLoc + c.xLoc +"\n");
					 c.removeFromBarge(boat);
					 terminal.unloadExport(c);
					 boat.showStowage();
				 }
			 }
		 }
	 }
	 
	 public static boolean hasContainersAbove(Container c, Boat boat) {	 
		 if(c.isOnBarge == true && c.zLoc< boat.nrOfLayers - 1 ) {
			 if(boat.stowage[c.zLoc+1][c.yLoc][c.xLoc] >0) {
				 return true;
			 }else if(c.type == ContainerType.TWENTY&& c.xLoc>0&&boat.stowage[c.zLoc+1][c.yLoc][c.xLoc-1] == 2) {
				 return true;
			 }else return false;
		 }else {
			 return false;
		 }

	 }
	 
	 public static void shiftContainersAbove(Container c, Boat boat, Terminal terminal, List<Container> copyContainers) {
		int firstX = c.xLoc;
		int Y = c.yLoc;
		int layer = c.zLoc;
		for(int i = boat.nrOfLayers -1;i>layer;i--) {
			if(boat.stowage[i][Y][firstX] > 0) {
				//clear container with this coordinate
				for(Container con: copyContainers) {
					if(con.zLoc == i && con.yLoc == Y && con.xLoc == firstX) {
						if(con.export && con.destination == terminal) {
							System.out.print("Error.. an export container is shifted, while it should already have been unloaded.. sukkel");
						}
						int realID = con.id+1;
						System.out.printf("Shifting container "+realID+" from position: " + con.zLoc + con.yLoc + con.xLoc +"\n");
						con.shiftFromBarge(boat);
						terminal.addToShiftAndUnloadedImport(con);
						boat.showStowage();
					}
				}
			}
		}
	 }
	 
	 public static void clearPile(Boat boat, Terminal terminal, int z, int y, int x, List<Container> copyContainers) {
		 for(int i = z + 1;i<boat.nrOfLayers - 1;i++) {
				 if(boat.stowage[i][y][x] == 1) {
					 for(Container c: copyContainers) {
						 if(c.zLoc == i && c.yLoc == y && c.xLoc == x) {
							int realID = c.id+1;
							System.out.printf("Shifting container "+realID+" from position: " + c.zLoc + c.yLoc + c.xLoc +"\n");
							c.removeFromBarge(boat);
							boat.showStowage();
							terminal.addToShiftAndUnloadedImport(c);
						 }					 
					}
				 }
			 

		 }
	 }
	 
	 public static int shiftBack(Boat boat, Terminal terminal, Container c){
		if(c.shifted == false) {
			System.out.printf("Errorrrrr, this is not a shifted container..\n");
		}
		int realID = c.id+1;
		c.shifted =false;
     	if(c.findFeasibleLocation(boat)== false){
			System.out.printf("Errorrrrr, unable to shift back container!\n");
     	}
		System.out.printf("Shifting back container "+realID+" in position: " + c.zLoc + c.yLoc + c.xLoc +"\n");
     	boat.showStowage();
     	return 1;
	 }
	 
	 public static boolean loadImportContainer(Boat boat, Terminal terminal, Container c) {
			if(c.shifted == true) {
				System.out.printf("Errorrrrr, this is a shifted container..\n");
			}
			int realID = c.id+1;
	        System.out.printf("Trying to load container "+realID+"...\n");
	        if(c.findFeasibleLocation(boat)) {
				terminal.loadedImport.add(c);	
				System.out.printf("Loading container "+realID+" in position: " + c.zLoc + c.yLoc + c.xLoc +"\n");
		     	boat.showStowage();
		     	return true;
	        }else {
				System.out.printf("Unable to load back container "+realID+"\n");
		     	boat.showStowage();
	        	return false;
	        }
	 }
	 
	 public static void loadListOfContainers( List<Container> containers) {
		 containers.sort(Comparator.comparing(Container::getOrder));
		 for(Container c: containers) {
			 c.tellPosition();
		 }
		 
		 
	 }
	 
	 public static void loadBoat(Boat boat, Terminal terminal, List<Container> containers) {
		 //first load the shifted containers..		 		 
		 int freeSlots = boat.countFreeSlots();
		 int j = 0;
		 terminal.unloadedImport.sort(Comparator.comparing(Container::getOrder));
		 while(freeSlots > terminal.shiftedContainers.size() && terminal.unloadedImport.size() > 0 && j<20) {
			 for (Iterator<Container> iterator2 = terminal.unloadedImport.iterator(); iterator2.hasNext();) {
				    Container c = iterator2.next();
				        if(c.shifted == true) {
				        	freeSlots -= shiftBack(boat,terminal,c);
					        iterator2.remove();
					     	terminal.shiftedContainers.remove(c);
				        }else {
				        	if(loadImportContainer(boat, terminal,c)) {
						        iterator2.remove();
				        		freeSlots --;
				        	}
				        }	
				        j++;
				}
		 }
		 if(terminal.shiftedContainers.size() != 0) {
			 System.out.printf("No space anymore for import containers, just loading back shifted ones.\n");
			 terminal.shiftedContainers.sort(Comparator.comparing(Container::getOrder));
			 int k=0;
			 while(terminal.shiftedContainers.size() != 0 && k<20) {
				 for (Iterator<Container> iterator = terminal.shiftedContainers.iterator(); iterator.hasNext();) {
					    Container c = iterator.next();
					    shiftBack(boat, terminal, c); //also removes it from 
				         iterator.remove();
				 }
				 k++;
			 }
		 }

	 }

}
