package stowage;
import containersAndBoat.Container;
import containersAndBoat.ContainerSet;
import containersAndBoat.ContainerType;
import containersAndBoat.Boat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class CreateStowage {
//take as input the set of containers that has been created,
	 public static List<Container> fixedContainers;
	 public static List<Container> tryingSet = new ArrayList<>();
	 public Boat initialBoat;
	 
	 public CreateStowage(List<Container> containers, Boat boat) {
		 containers.sort(Comparator.comparing(Container::getInitOrder));
		 fixedContainers = containers;
		 this.initialBoat = boat;
	 }
	 	 
	 public void createInitialStowage() {
		 for(Container c: fixedContainers) {
			 if(c.export) {
				if(c.findFeasibleInitialLocation(this.initialBoat)) {
					this.initialBoat.containersOnBoat.add(c);
				}else {
					tryingSet.add(c);
				}
			 }
		 }
		 reachCapacity(initialBoat, initialBoat.containersOnBoat);
		 reachStability(initialBoat);
	 }
	 
	public double calculateObjective(Boat boat) {
		double value =0;	
		for(Container c:CreateStowage.fixedContainers) {
			if(c.export == true && c.isOnBarge &&c.type == ContainerType.TWENTY) {
				value++;
			}else if(c.export == true && c.isOnBarge &&c.type == ContainerType.FORTY) {
				value += 2;
			}
		}
		for(int r = 0;r<TerminalSet.nrOfRoutes;r++) {
			OverstowageCalculator Ocalc = new OverstowageCalculator(TerminalSet.terminals, boat, ContainerSet.containers, TerminalSet.routes);
			Ocalc.reportRoute(r);
			value += TerminalSet.routeProb[r]* Ocalc.addedValue();
		}
		System.out.printf("The resulting objective for this initial stowage is: "+value+"\n");
		return value;
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
	 
	 public static void reachCapacity(Boat boat, List<Container> cOnBoard) {
		 int weight = boat.calcTotalWeight();
		 System.out.printf("Total weight at the start: "+weight+".\n");
		 boat.showStowage();
		 while(weight > + boat.q1) {
			 int[] h = calcHeaviestWeightFrom(cOnBoard);
			 removeHeavyContainer(boat, h[1]);
			 boat.showStowage();
			 if(tryingSet.isEmpty() == false) {
				 System.out.print("Trying set is not empty.\n");
				 int[] l = calcLightestWeightFrom(tryingSet);
				 if(h[0] > l[0]) {
					 System.out.print("This is lighter than the one removed, so we place it on board.\n");
					 for (Iterator<Container> iterator2 = tryingSet.iterator(); iterator2.hasNext();) {
						 Container c =iterator2.next();
						 if(c.id == l[1] && c.findFeasibleInitialLocation(boat) ) {
							int id = l[1] -1;
							 System.out.print("Placement of container "+id+"successful.\n");
							 boat.containersOnBoat.add(c);
							 iterator2.remove();
						 }
					 }
				}
			 }			 
			 weight = boat.calcTotalWeight();
			 System.out.printf("(Un)loading operation successful, total weight: "+weight+".\n");
		 }
		 System.out.printf("Barge is now within capacity, total weight: "+weight+".\n");
	 }
	 
	 public static void reachStability(Boat boat) {
		 int i = 0;
		 boat.reportWeight();
		 while(boat.crossBalanceSatisfied() == false && i<50) {
			 boat.swapColumns();
			 i++;
		 }
	 }
	 
	 public static void removeHeavyContainer(Boat boat, int outId) {
		 for (Iterator<Container> iterator2 = boat.containersOnBoat.iterator(); iterator2.hasNext();) {
			 Container c = iterator2.next();
			 if(c.id == outId) {
				 if(c.zLoc < Boat.nrOfLayers-1 &&boat.stowage[c.zLoc+1][c.yLoc][c.xLoc] > 0) {
					 for(Container c2:boat.containersOnBoat) {
						 if(c2.xLoc == c.xLoc && c2.yLoc == c.yLoc && c2.zLoc>c.zLoc) {
							 int newheight =c2.zLoc-1;
							 boat.set20LocationEmpty(c2.zLoc, c2.yLoc, c2.xLoc);
							 c2.updateLocationOnBarge(newheight, c2.yLoc, c2.xLoc);
							 boat.set20footSpotOccupied(newheight, c2.yLoc, c2.xLoc, c2.weight, c2.getDest());
						 }
					 }
					 c.xLoc = -1;
					 c.yLoc = -1;
					 c.zLoc = -1;
					 c.isOnBarge = false;
				 }else {
					 c.removeFromBarge(boat);
				 }

				 iterator2.remove();
			 }
		 }

		 
	 }
	 	 
	 public static int[] calcHeaviestWeightFrom(List<Container> containers) {
		 int[] heaviestWeight = {-1,-1};
		 for(Container c:containers) {
			 if(c.weight>heaviestWeight[0]) {
				 heaviestWeight[0]= c.weight;
				 heaviestWeight[1]= c.id;
			 }
		 }		 
		 System.out.printf("The heaviest container on board has ID: "+heaviestWeight[1]+", with weight: "+heaviestWeight[0]+".\n");
		 return heaviestWeight;
	 }
	 
	 public static int[] calcLightestWeightFrom(List<Container> containers) {
		 int[] lightestWeight = {5,-1};
		 for(Container c:containers) {
			 if(c.weight<lightestWeight[0]) {
				 lightestWeight[0]= c.weight;
				 lightestWeight[1]= c.id;

			 }
		 }		 
		 System.out.printf("The lightest container in the trying set has ID: "+lightestWeight[1]+", with weight: "+lightestWeight[0]+".\n");
		 return lightestWeight;
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
	 
	 public static boolean containsWeight(List<Container> containers, int weight) {
		 for(Container c:containers) {
			 if(c.weight == weight) {
				 return true;
			 }
		 }
		 return false;
	 }
	 
	 public static boolean insertInRightWeight(Boat boat, int route, Terminal terminal, int weight) {
		System.out.printf("The trying set contains weight "+weight+", looking for a pile with this on top.\n");
		int highscore = -100;	
		int score =0;
		int m = 0;
		int h = 0;
		int bestRow = 0;
		int bestBay = 0;
		int startZ = 0;
		
		for(int j=0;j<Boat.nrOfRows;j++) {
				for(int k=0;k<Boat.nrOfBays;k++) {
					if(boat.pileIsEligible(j,k,weight)) {
						score = boat.calcPileScoreRightWeightOnTop(route, j, k, weight, terminal.unloadedImport)[0];
						if(score >= highscore) {
							highscore = score;
							m = boat.calcPileScoreRightWeightOnTop(route, j, k, weight, terminal.unloadedImport)[1];
							h = boat.calcPileScoreRightWeightOnTop(route, j, k, weight, terminal.unloadedImport)[2];
							startZ = boat.calcPileScoreRightWeightOnTop(route, j, k, weight, terminal.unloadedImport)[3];
							bestRow = j;
							bestBay = k;
						}
					}
				}
		}
		if(highscore>-100) {
			insertionInPile(boat, terminal, weight,startZ, bestRow,bestBay,m,h);
			return true;
		}else {
			return false;
		}
	 }
	 
	 public static void insertionInPile(Boat boat, Terminal terminal, int weight, int startZ, int j, int k, int m, int h) {
		 List<Container> floatingContainers = new ArrayList<Container>();
		 //remove the m top containers from the barge
		 for(int i=0;i<Boat.nrOfLayers;i++) {		
				 for(int f = startZ;f<startZ+m;f++) {
					 for(Container c:boat.containersOnBoat) {
						 if(c.zLoc == f && c.xLoc == k && c.yLoc == j) {
							 c.removeFromBarge(boat);
							 floatingContainers.add(c);
						 }
					 }
				 }
				 break;
		 }
		 //remove the first h containers with the right weight from the barge
		 for(int g = 0;g<h;g++) {
			 for(Container c:terminal.unloadedImport) {
				 if(c.weight == weight && c.shifted) {
					 terminal.unloadedImport.remove(c);
					 terminal.shiftedContainers.remove(c);
					 floatingContainers.add(c);
					 break;
				 }else if(c.weight == weight && c.shifted == false) {
					 terminal.unloadedImport.remove(c);
					 floatingContainers.add(c);
					 break;					 
				 }
			 }
		 }
		 //sort the group, and make it rain
		 floatingContainers.sort(Comparator.comparing(Container::getOrder));
		 for(Container c:floatingContainers) {
			 int count = 0;
			 boat.set20footSpotOccupied(startZ+count ,j,k, c.weight, c.getDest());
			  c.updateLocationOnBarge(startZ+count,j,k);
			  c.transported = true;
			  count++;
		 }		 
	 }
	 
	 public static void applyMVSD(Boat boat, int route, Terminal terminal) {
		boat.showStowage();
		for(int weight=3;weight>0;weight--) {
			while( containsWeight(terminal.unloadedImport,weight) ) {
				if( insertInRightWeight(boat,route,terminal,weight)) {				
				}else {
					break;
				}
			}

		}
		if(terminal.unloadedImport.size() > 0) {
			for(Container c1: terminal.unloadedImport) {
				c1.putInAnyPlace(boat, 0, Boat.nrOfRows, 0, Boat.nrOfBays);
			}
		}
		//look for places with that weight on top
		//if(found){   say with n  containers of that weight class on top
		//calculate obj = max_m h(m) + b(0) - b(m) - g(m) for 0<= m <= n.
		// then select the pile with the highest obj and take away m* containers there.
		//else{ do the same but then for the pile where you have to take away the least to reach the right weight class
	 }
	
	 public static void loadBoat(Boat boat, int route, Terminal terminal) {
			loadOneSequence(boat,route,terminal);
			if(terminal.unloadedImport.size() > 0 && boat.countFreeSlots() >0) {
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
