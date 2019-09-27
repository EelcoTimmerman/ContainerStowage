package stowage;
import containersAndBoat.ContainerType;
import containersAndBoat.Container;
import containersAndBoat.Boat;

import java.util.Iterator;
import java.util.List;


public class CreateStowage {
//take as input the set of containers that has been created,
	 public static List<Container> containers;
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
	 
	 public static void  removeExport(Boat boat, Terminal terminal) {
		 for(int i = boat.nrOfLayers -1;i>-1;i--) {
			 for(Container c: containers) {
				 if(c.export&& c.destination == terminal && c.isOnBarge && c.isInLayer(i)) {
					 if(hasContainersAbove(c,boat)) {
						 shiftContainersAbove(c, boat, terminal);
					 }
					 c.removeFromBarge(boat);
					 terminal.unloadExport(c);
					 boat.showStowage();
				 }
			 }
		 }
	 }
	 
	 public static boolean hasContainersAbove(Container c, Boat boat) {	 
		 if(c.zLoc == boat.nrOfLayers -1) {
			 return false;
		 }
		 if(c.zLoc< boat.nrOfLayers - 1 && boat.stowage[c.zLoc+1][c.yLoc][c.xLoc] >0) {
			 return true;
		 }else return false;
	 }
	 
	 public static void shiftContainersAbove(Container c, Boat boat, Terminal terminal) {
		int firstX = c.xLoc;
		int Y = c.yLoc;
		int secondX = c.xLoc+1;
		int layer = c.zLoc;
		for(int i = layer + 1;i<boat.nrOfLayers;i++) {
			if(boat.stowage[i][Y][firstX] > 0) {
				//clear container with this coordinate
				for(Container con: containers) {
					if(con.zLoc == i && con.yLoc == Y && con.xLoc == firstX) {
						if(con.export && con.destination == terminal) {
							System.out.print("Error.. an export container is shifted, while it should already have been unloaded.. sukkel");
						}
						con.removeFromBarge(boat);
						terminal.addToShift(con);
						if(c.type == ContainerType.FORTY) {
							clearPile(boat,terminal,i,Y,secondX);
						}
					}
				}
			}
		}
		
	 }
	 
	 public static void clearPile(Boat boat, Terminal terminal, int z, int y, int x) {
		 for(int i = z + 1;i<boat.nrOfLayers - 1;i++) {
				 if(boat.stowage[i][y][x] == 1) {
					 for(Container c: containers) {
						 if(c.zLoc == i && c.yLoc == y && c.xLoc == x) {
							 c.removeFromBarge(boat);
							 terminal.addToShift(c);
						 }					 
					}
				 }
			 

		 }
	 }
	 
	 public static void loadImport(Boat boat, Terminal terminal) {
		 //first load the shifted containers..		 
		 for (Iterator<Container> iterator = terminal.shiftedContainers.iterator(); iterator.hasNext();) {
			    Container c = iterator.next();
			    if(c.findFeasibleLocation(boat)) {
			        iterator.remove();
					 boat.showStowage();
			    }
			}
		 
		 for (Iterator<Container> iterator2 = terminal.unloadedImport.iterator(); iterator2.hasNext();) {
			    Container c = iterator2.next();
			    if(c.findFeasibleLocation(boat)) {
			        iterator2.remove();
			        terminal.loadedImport.add(c);
					 boat.showStowage();
			    }
			}

	 }
	 
	 


}
