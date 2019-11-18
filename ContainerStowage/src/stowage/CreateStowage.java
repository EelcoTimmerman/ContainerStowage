package stowage;
import containersAndBoat.Container;
import containersAndBoat.Boat;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class CreateStowage {
//take as input the set of containers that has been created,
	 public static List<Container> fixedContainers;
	 public Boat initialBoat;
	 
	 public CreateStowage(List<Container> containers, Boat boat) {
		 containers.sort(Comparator.comparing(Container::getOrder));
		 fixedContainers = containers;
		 this.initialBoat = boat;
	 }
	 	 
	 public void createInitialStowage() {
		 for(Container c: fixedContainers) {
			 if(c.export) {
				c.findFeasibleLocation(this.initialBoat,0);
				this.initialBoat.containersOnBoat.add(c);
			 }
		 }
	 }
	 
	 public static void  removeExport(Boat boat, Terminal terminal, List<Container> copyContainers) {
		 for(int i = Boat.nrOfLayers -1;i>-1;i--) {
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
					 boat.containersOnBoat.remove(c);
					 terminal.unloadExport(c);
					 //boat.showStowage();
				 }
			 }
		 }
	 }
	 
	 public static boolean hasContainersAbove(Container c, Boat boat) {	 
		 if(c.isOnBarge == true && c.zLoc< Boat.nrOfLayers - 1 ) {
			 if(boat.stowage[c.zLoc+1][c.yLoc][c.xLoc] >0) {
				 return true;
			 }else {
				 return false;
			 }
		 }else {
			 return false;
		 }

	 }
	 
	 public static void shiftContainersAbove(Container c, Boat boat, Terminal terminal, List<Container> copyContainers) {
		int firstX = c.xLoc;
		int Y = c.yLoc;
		int layer = c.zLoc;
		for(int i = Boat.nrOfLayers -1;i>layer;i--) {
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
						boat.containersOnBoat.remove(con);
						terminal.addToShiftAndUnloadedImport(con);
						//boat.showStowage();
					}
				}
			}
		}
	 }
	 
	 public static int shiftBack(Boat boat, int route, Terminal terminal, Container c){
		if(c.shifted == false) {
			System.out.printf("Errorrrrr, this is not a shifted container..\n");
		}
		int realID = c.id+1;
		c.shifted =false;
     	if(c.findFeasibleLocation(boat, route)== false){
			System.out.printf("Errorrrrr, unable to shift back container!\n");
			return 0;
     	}
		System.out.printf("Shifting back container "+realID+" in position: " + c.zLoc + c.yLoc + c.xLoc +"\n");
     	boat.containersOnBoat.add(c);
		boat.showStowage();
     	return 1;
	 }
	 
	 public static boolean loadImportContainer(Boat boat, int route, Terminal terminal, Container c) {
			if(c.shifted == true) {
				System.out.printf("Errorrrrr, this is a shifted container..\n");
			}
			int realID = c.id+1;
	        System.out.printf("Trying to load container "+realID+"...\n");
	        if(c.findFeasibleLocation(boat,route)) {
				terminal.loadedImport.add(c);	
				boat.containersOnBoat.add(c);
				System.out.printf("Loading container "+realID+" in position: " + c.zLoc + c.yLoc + c.xLoc +"\n");
		     	//boat.showStowage();
				return true;
	        }else {
				System.out.printf("Unable to load back container "+realID+".\n");
		     	//boat.showStowage();
	        	return false;
	        }
	 }
	 
	 public static void loadListOfContainers( List<Container> containers) {
		 containers.sort(Comparator.comparing(Container::getOrder));
		 for(Container c: containers) {
			 c.tellPosition();
		 }
		 
		 
	 }
	 
	 public static void applyMVSD(Boat boat, int route, Terminal terminal) {
		Container c = terminal.unloadedImport.get(0);
		int highestWeight = c.weight;
		boat.showDestStowage();
		System.out.printf("The highest weight in the trying set is "+highestWeight+"\n");
		for(int j=0;j<Boat.nrOfRows;j++) {
			 for(int k=0;k<Boat.nrOfBays;k++) {
				boat.calcPileScoreRightWeightOnTop(route, j, k, highestWeight, terminal.unloadedImport);
			}
		}
		
		//look for places with that weight on top
		//if(found){   say with n  containers of that weight class on top
		//calculate obj = max_m h(m) + b(0) - b(m) - g(m) for 0<= m <= n.
		// then select the pile with the highest obj and take away m* containers there.
		//else{ do the same but then for the pile where you have to take away the least to reach the right weight class
		 System.out.print("Starting the MVSD procedure, ");

	 }
	
	 public static void loadBoat(Boat boat, int route, Terminal terminal) {
			loadOneSequence(boat,route,terminal);
			if(terminal.unloadedImport.size() > 0) {
				applyMVSD(boat,route, terminal);
			}
		// while(terminal.unloadedImport.size() != 0 && boat.countFreeSlots() != 0) {
			//loadOneSequence(boat,route,terminal);
			//applyMVSD(boat,route, terminal);
		 //}
	 }
	 
	 public static void loadOneSequence(Boat boat, int route, Terminal terminal) {
		 int freeSlots = boat.countFreeSlots();
		 terminal.unloadedImport.sort(Comparator.comparing(Container::getOrder));
		 
		 for (Iterator<Container> iterator2 = terminal.unloadedImport.iterator(); iterator2.hasNext();) {
				 if(freeSlots > terminal.shiftedContainers.size()) {
					 Container c = iterator2.next();
				        if(c.shifted == true) {
				        	freeSlots -= shiftBack(boat, route,terminal,c);
					        iterator2.remove();
					     	terminal.shiftedContainers.remove(c);
				        }else {
				        	if(loadImportContainer(boat, route, terminal,c)) {
						        iterator2.remove();
				        		freeSlots --;
				        	}
				        }	
				}else {
					break;
				}
			}
		 terminal.unloadedImport.sort(Comparator.comparing(Container::getOrder));
		 if(terminal.shiftedContainers.size() != 0) {
			 System.out.printf("No space anymore for import containers, just loading back shifted ones.\n");
			 terminal.shiftedContainers.sort(Comparator.comparing(Container::getOrder));
				 for(Iterator<Container> iterator = terminal.shiftedContainers.iterator(); iterator.hasNext();) {
					    Container c = iterator.next();
					    shiftBack(boat,route, terminal, c);
					    terminal.unloadedImport.remove(c);
				         iterator.remove();
				 }

		 }

	 }

}
