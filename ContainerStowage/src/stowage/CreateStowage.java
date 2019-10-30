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
	 public static int overstowageCount = 0;
	 
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
	 
	 public static void  removeExport(Boat boat, Terminal terminal, List<Container> containers) {
		 for(int i = boat.nrOfLayers -1;i>-1;i--) {
			 for(Container c: containers) {
				 if(c.export&& c.destination == terminal && c.isOnBarge && c.isInLayer(i)) {
					 if(hasContainersAbove(c,boat)) {
						 int realID = c.id +1;
						 System.out.print("Start shifting containers to reach container "+realID+ " in position " + c.zLoc + c.yLoc + c.xLoc+"\n");
						 shiftContainersAbove(c, boat, terminal);
					 }
					 int realID = c.id+1;
					 System.out.printf("Unloading container "+realID+" from position: " + c.zLoc + c.yLoc + c.xLoc +"\n");
					 c.removeFromBarge(boat);
					 terminal.unloadExport(c);
					 //boat.showStowage();
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
		for(int i = boat.nrOfLayers -1;i>layer;i--) {
			if(boat.stowage[i][Y][firstX] > 0) {
				//clear container with this coordinate
				for(Container con: containers) {
					if(con.zLoc == i && con.yLoc == Y && con.xLoc == firstX ||
							con.zLoc == i && con.yLoc == Y && con.xLoc == firstX -1 && con.type == ContainerType.FORTY && firstX%2 == 1 ) {
						if(con.export && con.destination == terminal) {
							System.out.print("Error.. an export container is shifted, while it should already have been unloaded.. sukkel");
						}
						int realID = con.id+1;
						System.out.printf("Shifting container "+realID+" from position: " + con.zLoc + con.yLoc + con.xLoc +"\n");
						con.removeFromBarge(boat);
						boat.showStowage();
						terminal.addToShift(con);
						overstowageCount++;
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
							int realID = c.id+1;
							System.out.printf("Shifting container "+realID+" from position: " + c.zLoc + c.yLoc + c.xLoc +"\n");
							overstowageCount++;
							c.removeFromBarge(boat);
							boat.showStowage();
							terminal.addToShift(c);
						 }					 
					}
				 }
			 

		 }
	 }
	 
	 public static void loadImport(Boat boat, Terminal terminal, List<Container> containers) {
		 //first load the shifted containers..		 
		 for (Iterator<Container> iterator = terminal.shiftedContainers.iterator(); iterator.hasNext();) {
			    Container c = iterator.next();
			    if(c.findFeasibleLocation(boat)) {
			         iterator.remove();
					 int realID = c.id+1;
					 System.out.printf("Loading back shifted container "+realID+" in position: " + c.zLoc + c.yLoc + c.xLoc +"\n");
			         //boat.showStowage();
			    }else {
			    	System.out.print("Ünable to put back shifted container, quite a bit a mistake sucker..");
			    }
			}
		 
		 for (Iterator<Container> iterator2 = terminal.unloadedImport.iterator(); iterator2.hasNext();) {
			    Container c = iterator2.next();
				int realID = c.id+1;
		        System.out.printf("Trying to load container "+realID+"...\n");
			    if(c.findFeasibleLocation(boat)) {
			        iterator2.remove();
			        terminal.loadedImport.add(c);
			        System.out.printf("Loading container "+realID+" in position: " + c.zLoc + c.yLoc + c.xLoc +"\n");
			        //boat.showStowage();
			    }
			}

	 }
	 
	 


}
