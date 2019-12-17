package containersAndBoat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import stowage.CreateStowage;
import stowage.Terminal;
import stowage.TerminalSet;

public class Boat {
	public static int nrOfBays = 6;
	public static int nrOfLayers = 3;
	public static int nrOfRows = 3;
	public int leftback = 0;
	public int rightback = 0;
	public int leftfront = 0;
	public int rightfront = 0;
	public int leftweight = 0;
	public int rightweight= 0;
	public int backweight =0;
	public int frontweight =0;
	public int q1 = 220; //barge capacity. about 2/3 of full volume with heavy containers.
	public int q2 = 10; // max left/right weight difference, was 16. 8%
	public int q3 = 3200;  // front/back weight difference, was 32. 16%
	public List<Container> containersOnBoat = new ArrayList<>();

	public static int weightBoat;
	public int[][][] stowage = new int[nrOfLayers][nrOfRows][nrOfBays];
	public int [][][] destStowage = new int [nrOfLayers][nrOfRows][nrOfBays];
	
	public Boat() {
		System.out.print("A boat has been created \n");
	}
	
	public void showStowage() {
		System.out.print("Stowage:\n");  
		for(int i = 0; i<nrOfLayers;i++) {
			  for(int j = 0; j<nrOfRows;j++) {
				  for(int k = 0; k<nrOfBays;k++) {
					  System.out.print(stowage[i][j][k]);
					  if(k == nrOfBays - 1 && j != nrOfRows -1) {
						  System.out.printf("\n");
					  }if(k == nrOfBays - 1 && j == nrOfRows -1) {
						  System.out.printf("\n");
						  System.out.printf("\n");
					  }
				  }
			  }
		  }
		showDestStowage();
		calcWeight();
		//reportWeight();
	}
	
	public void showDestStowage() {
		System.out.print("Destination plan:\n");  
		for(int i = 0; i<nrOfLayers;i++) {
			  for(int j = 0; j<nrOfRows;j++) {
				  for(int k = 0; k<nrOfBays;k++) {
					  System.out.print(destStowage[i][j][k]);
					  if(k == nrOfBays - 1 && j != nrOfRows -1) {
						  System.out.printf("\n");
					  }if(k == nrOfBays - 1 && j == nrOfRows -1) {
						  System.out.printf("\n");
						  System.out.printf("\n");
					  }
				  }
			  }
		  }
	}
	
	public int[][][] getStowage(){
		return stowage;		
	}
		
	public void set20footSpotOccupied(int layer, int row, int bay, int weight, int dest) {
		stowage[layer][row][bay] = weight;
		destStowage[layer][row][bay] = dest;
	}
	
	public void set20LocationEmpty(int layer, int row, int bay) {
		stowage[layer][row][bay] = 0;
		destStowage[layer][row][bay] = 0;
	}	
		
	public int visitTerminal(Terminal terminal, int route, Boat boat, List<Container> containers) {
		int shifts = 0;
		CreateStowage.removeExport(boat, terminal, containers);
		shifts += terminal.shiftedContainers.size();
		CreateStowage.loadBoat(boat, route, terminal);
		return shifts;
	}
	
	public int countFreeSlots() {
		int freeSlots = 0;
		for(int i =0;i<nrOfLayers;i++) {
			for(int j=0;j<nrOfRows;j++) {
				for(int k=0;k<nrOfBays;k++) {
					if(this.stowage[i][j][k] == 0) {
						freeSlots++;
					}
				}
			}
		}
		return freeSlots;
	}
	
	public boolean baysAreEven() {
		if(nrOfBays %2 == 0) {
			return true;
		}else{
			return false;
		}
	}
	
	public boolean rowsAreEven() {
		if(nrOfRows %2 == 0) {
			return true;
		}else{
			return false;
		}
	}
	
	public int calcWeightOfCertainPart(int startrow, int endrow, int startbay, int endbay) {
		int weight =0;
		for(int i =0;i<nrOfLayers;i++) {
			for(int j=startrow;j<endrow;j++) {
				for(int k = startbay;k<endbay;k++) {
					weight += stowage[i][j][k];
				}
			}
		}
		return weight;
	}
	
	public int calcTotalWeight() {
		int weight =0;
		for(int i =0;i<nrOfLayers;i++) {
			for(int j=0;j<nrOfRows;j++) {
				for(int k = 0;k<nrOfBays;k++) {
					weight += stowage[i][j][k];
				}
			}
		}	
		return weight;
	}
	
	public void calcWeight(){
		this.leftweight = calcWeightOfCertainPart(0,nrOfRows/2,0,nrOfBays);
		if(rowsAreEven() && nrOfRows > 1) {
			this.rightweight = calcWeightOfCertainPart(nrOfRows/2,nrOfRows,0,nrOfBays);
		}else if(rowsAreEven() == false && nrOfRows > 1) {
			this.rightweight = calcWeightOfCertainPart(nrOfRows/2 +1,nrOfRows,0,nrOfBays);
		}else {
			this.leftweight = 0;
			this.rightweight = 0;
		}
		this.backweight = calcWeightOfCertainPart(0,nrOfRows,0,nrOfBays/2);
		if(baysAreEven()) {
			this.frontweight = calcWeightOfCertainPart(0,nrOfRows,nrOfBays/2,nrOfBays);
		}else{
			this.frontweight = calcWeightOfCertainPart(0,nrOfRows,nrOfBays/2+1,nrOfBays);
		}		
	}
	
	public void reportWeight() {
		System.out.printf("Left Weight: "+this.leftweight+"\n");
		System.out.printf("Right Weight: "+this.rightweight+"\n");
		System.out.printf("Front Weight: "+this.frontweight+"\n");
		System.out.printf("Back Weight: "+this.backweight+"\n");
	}
	
	public boolean swapColumns() {
		reportWeight();
		showStowage();
		int[] coordinates = whereIsColumnSwapNeeded();
		System.out.printf("Heavy column row:"+coordinates[2]+" bay:"+coordinates[3]+"\n");
		System.out.printf("Light column row:"+coordinates[0]+" bay:"+coordinates[1]+"\n");
		int yres = -1;
		int xres = -1;
		for(Container c:this.containersOnBoat) {
			if(c.yLoc == coordinates[2] && c.xLoc == coordinates[3]) {
				c.yLoc = 1000;
				c.xLoc = 1000;
			}

		}
		for(Container c2:this.containersOnBoat) {
			if(c2.yLoc == coordinates[0] && c2.xLoc == coordinates[1]) {
				c2.yLoc = coordinates[2];
				c2.xLoc = coordinates[3];
			}
		}
		for(Container c3:this.containersOnBoat) {
			if(c3.yLoc == 1000 && c3.xLoc == 1000) {
				c3.yLoc = coordinates[0];
				c3.xLoc = coordinates[1];
				System.out.print("My new position: \n");
				c3.tellPosition();
			}

		}
		for(int i=0;i<Boat.nrOfLayers;i++) {
			int res = destStowage[i][coordinates[0]][coordinates[1]];
			destStowage[i][coordinates[0]][coordinates[1]] = destStowage[i][coordinates[2]][coordinates[3]];
			destStowage[i][coordinates[2]][coordinates[3]] = res;
			int resi = stowage[i][coordinates[0]][coordinates[1]];
			stowage[i][coordinates[0]][coordinates[1]] = stowage[i][coordinates[2]][coordinates[3]];
			stowage[i][coordinates[2]][coordinates[3]] = resi;
		}

		showStowage();
		return false;
	}
	
	public boolean crossBalanceSatisfied() {
		if(frontweight - backweight - q3 > 0) {
			return false;
		}else if(backweight - frontweight - q3 > 0) {
			return false;
		}
		if(rightweight - leftweight - q2 > 0) {
			return false;
		}else if(leftweight - rightweight - q2 > 0) {
			return false;
		}
		return true;
	}
	
	public int[] whereIsColumnSwapNeeded() {
		int[] heavyArea = {0,Boat.nrOfRows-1,0,Boat.nrOfBays-1};
		int[] lightArea = {0,Boat.nrOfRows-1,0,Boat.nrOfBays-1}; 
		int[] coordinates = {-1,-1,-1,-1};
		calcWeight();
		if(frontweight - backweight - q3 > 0) { //weight moves from front to back
			if(baysAreEven()) {
				heavyArea[2] = Boat.nrOfBays/2;  //begin
				lightArea[3] = Boat.nrOfBays/2 - 1;
			}else {
				heavyArea[2] = Boat.nrOfBays/2 + 1;
				lightArea[3] = Boat.nrOfBays/2 - 1;
			}
		}else if(backweight - frontweight - q3 > 0) {
			if(baysAreEven()) {
				heavyArea[3] = Boat.nrOfBays/2 -1; //end
				lightArea[2] = Boat.nrOfBays/2 ; 
			}else {
				heavyArea[3] = Boat.nrOfBays/2 - 1 ;
				lightArea[2] = Boat.nrOfBays/2 + 1 ;
			}
		}
		if(rightweight - leftweight - q2 > 0) { //weight needs to go to the right
			if(rowsAreEven()) {
				heavyArea[0] = Boat.nrOfRows/2; //begin
				lightArea[1] = Boat.nrOfRows/2 - 1; //begin
			}else {
				heavyArea[0] = Boat.nrOfRows/2 + 1;
				lightArea[1] = Boat.nrOfRows/2 - 1;
			}
		}else if(leftweight - rightweight - q2 > 0){ // weight moves to left
			if(rowsAreEven()) {
				heavyArea[1] = Boat.nrOfRows/2 -1; //end
				lightArea[0] = Boat.nrOfRows/2 ; //begin
			}else {
				heavyArea[1] = Boat.nrOfRows/2 - 1;
				lightArea[0] = Boat.nrOfRows/2 + 1;
			}
		}
		System.out.printf("Heavy area. Rows "+heavyArea[0]+"--"+heavyArea[1]+", Bays "+heavyArea[2]+"--"+heavyArea[3]+"\n");
		System.out.printf("Light area. Rows "+lightArea[0]+"--"+lightArea[1]+", Bays "+lightArea[2]+"--"+lightArea[3]+"\n");
		
		coordinates[2] = returnHeavyColumn(heavyArea[0],heavyArea[1],heavyArea[2],heavyArea[3])[0];
		coordinates[3] = returnHeavyColumn(heavyArea[0],heavyArea[1],heavyArea[2],heavyArea[3])[1];
		coordinates[0] = returnLightColumn(lightArea[0],lightArea[1],lightArea[2],lightArea[3])[0];
		coordinates[1] = returnLightColumn(lightArea[0],lightArea[1],lightArea[2],lightArea[3])[1];
		return coordinates;
	}
	
	public int[] returnHeavyColumn(int startRow, int endRow, int startBay, int endBay) {
		int coordinates[] = {0,0};
		 int maxWeight = 0;
		  for(int j=startRow;j<endRow+1;j++) {
			for(int k =startBay;k<endBay+1;k++) {
				int weight = calcColumnWeight(j,k);
				if(weight>= maxWeight) {
					maxWeight = weight;
					coordinates[0] = j;
					coordinates[1] = k;
				}
			}
		}
		/*Random rand = new Random(1000);
		Random rand2 = new Random(8920);
		coordinates[0] = rand.nextInt((endRow - startRow) + 1) + startRow;
		coordinates[1] = rand2.nextInt((endBay - startBay) + 1) + startBay;*/
		System.out.printf("Weight heaviest column: "+maxWeight+"\n");
		return coordinates; // max row, max bay
	}
	
	public int[] returnLightColumn(int startRow, int endRow, int startBay, int endBay) {
		int coordinates[] = {0,0};
		
		int minWeight = 100;		
		for(int j=startRow;j<endRow+1;j++) {
			for(int k =startBay;k<endBay+1;k++) {
				int weight = calcColumnWeight(j,k);
				if(weight<= minWeight) {
					minWeight = weight;
					coordinates[0] = j;
					coordinates[1] = k;
				}
			}
		}
		System.out.printf("Weight lightest column: "+minWeight+"\n");
		/*Random rand = new Random(1010);
		Random rand2 = new Random(8320);
		coordinates[0] = rand.nextInt((endRow - startRow) + 1) + startRow;
		coordinates[1] = rand2.nextInt((endBay - startBay) + 1) + startBay*/;
		return coordinates; //min row, min bay
	}
	
	public int calcColumnWeight( int j, int k) {
		int weight = 0;
		for(int i=0;i<Boat.nrOfLayers;i++) {
			weight += this.stowage[i][j][k];
		}
		return weight;		
	}
	
	public int whereIsBalanceWeightNeeded(int weight) {
		int f =0;
		int b = 0;
		int r = 0;
		int l = 0;
		int block = 0;	
		if(frontweight>backweight) {
			b = this.frontweight +weight - this.backweight - q3;
		}else if(this.backweight> this.frontweight) {
			f = this.backweight +weight - this.frontweight - q3;
		}
		if(rightweight> leftweight) {
			l = this.rightweight + weight - this.leftweight - q2;
		}else {
			r = this.leftweight + weight - this.rightweight - q2;
		}
			if(f <= 0 && b <= 0 && r <=0 && l <=0) {
				block = 0;
			}else if(f > 0 && b <= 0 && r <= 0 && l <=0) {
				block = 1;
			}else if(f <= 0 && b <= 0 && r > 0 && l <=0) {
				block = 2;
			}else if(f <= 0 && b > 0 && r <=0 && l <=0) {
				block = 3;
			}else if(f <= 0 && b <= 0 && r <=0 && l > 0) {
				block = 4;
			}else if(f > 0 && b <= 0 && r <= 0 && l >0) {
				block = 5;
			}else if(f > 0 && b <= 0&& r > 0 && l <=0) {
				block = 6;
			}else if(f <= 0 && b > 0&& r  > 0 && l <=0) {
				block = 7;
			}else if(f <= 0 && b > 0&& r  <= 0 && l > 0) {
				block = 8;
			}
			if(block> 0) {
				System.out.print("Balance weight is needed, hang on buddy.");
			}
			return block;
	}
	
	public boolean checkIfStableWithNewContainer(int weight) {
		if(this.frontweight + weight - this.backweight<this.q3 && this.backweight+weight - this.frontweight<this.q3 
				&& this.leftweight+weight - this.rightweight<this.q2 && this.rightweight + weight - this.leftweight<this.q2) {
			return true;
		}else {
			return false;
		}
				
	}
	
	public int getProximityInRoute(int route, int dest) {			
		int index = 100;			
		if(dest < 9) {
			for(int i =0;i<TerminalSet.nrOfTerminals-1;i++) {
				if(TerminalSet.routes[route][i] == dest) {
					index = i;
				}
			}
		}else {
			index = 9;
		}
		return index;
	}
	
	public boolean highestContainerHasWeight(int j, int k, int weight) {
		for(int i=0;i<nrOfLayers-1;i++) {
			if(stowage[i+1][j][k] == 0 && stowage[i][j][k] == weight) {
				return true;
			}
		}
		return false;
	}
	
	public int amountInWeightClassOnTop(int j, int k, int weight) {
		int count =0;
		for(int i=nrOfLayers-1;i >= 0;i--) {
			if(stowage[i][j][k] == 0) {
				continue;
			}
			else if(stowage[i][j][k] == weight) {
				count++;
			}else {
				break;
			}
		}
		return count;
	}
	
	public int amountOfWeightClassInTryingSet(List<Container> containers, int weight) {
		int count = 0;
		for(Container c:containers) {
			if(c.weight == weight) {
				count++;
			}
		}
		return count;
	}
	
	public int[] nrOfBlockingsUntill(int route,int Z, int j,int k) {
		int[] b0 = {0,0};
		int lowestIndexBelow = getProximityInRoute(route, this.destStowage[0][j][k]);
		int index;
		if(Z==-1) {
			b0[0] = 0;
			b0[1] = 10;
		}else if(Z == 0) {
			b0[0] = 0;
			b0[1] = lowestIndexBelow;
		}else {
			for(int i=1;i<=Z;i++) {
				int dest = this.destStowage[i][j][k];
				if(dest > 0) {
					index = getProximityInRoute(route, dest);
				}else {
					index = 0;
				}
				if(index > lowestIndexBelow) {
					b0[0]++;
				}else if(index<= lowestIndexBelow && index>0){
					lowestIndexBelow = index;
					b0[1] = lowestIndexBelow;
				}
			}
		}
		return b0;
	}
	
	public int nrOfBlockingsAfterMovement(int route, int m, int h, int j, int k, List<Container> containers) {
		//calc of the blockings in the pile thats left
		int highestContainer = 0;
		for(int y =0;y<nrOfLayers;y++) {
			if(stowage[y][j][k] == 0) {
				highestContainer = y - m -1;
				break;
			}
		}
		if(highestContainer < -1) {
			System.out.print("Trying to unload more containers than available in pile..");
		}
		int lowestIndexLeft = nrOfBlockingsUntill(route, highestContainer,j,k)[1];
		int blockingLeft = nrOfBlockingsUntill(route, highestContainer,j,k)[0];
		//calc of the new amount of blockings
		List<Integer> destinations = new ArrayList<Integer>();
		
		int blockings = 0;
		if(m>0) {
			for(int g = 0;g<m;g++) {
				int removedDest = getProximityInRoute(route,destStowage[highestContainer + g+1][j][k]);						
				destinations.add(removedDest);
			}
		}
		if(h>0) {
			for(int i = 0; i<h;i++) {
				int dest = containers.get(i).getDest();
				int index = getProximityInRoute(route, dest);
				destinations.add(index);
			}
		}
		Collections.sort(destinations,Collections.reverseOrder());
		for(int dest:destinations) {
			if(dest > lowestIndexLeft) {
				blockings++;
			}else {
				lowestIndexLeft = dest;
			}
		}
		
		return blockings + blockingLeft;
	}
	
	public int[] calcPileScoreRightWeightOnTop(int route, int j, int k, int weight, List<Container> containers) {
		System.out.printf("We now calculate the score of pile: "+j+", "+k+", which has the right weight on top, weight: "+weight+"\n");
		System.out.printf("These are the containers to be loaded:\n");
		for(Container c:containers) {
			c.tellPosition();
		}
		int freespots = 0;
		int b0 = nrOfBlockingsUntill(route, nrOfLayers-1, j,k)[0];
		for(int i=0;i<nrOfLayers;i++) {
			if(stowage[i][j][k] == 0) {
				freespots++;
			}
		}
		int maxToBeLoaded = amountOfWeightClassInTryingSet(containers, weight);		
		int h = Math.min(freespots, maxToBeLoaded);
		int n = amountInWeightClassOnTop(j,k,weight);
		int obj[] = {0,0,0,0};
		obj[2] = h;
		if(n > 0) {
			for(int m = 0; m<= n;m++) {
				int bm = nrOfBlockingsAfterMovement(route, m, h, j, k, containers);
				System.out.printf("If we take off: "+m+" containers, we end up with this score:\n");
				System.out.printf("h is: "+h+", the min of freespots "+freespots+" and nr of c. in weightclass: "+maxToBeLoaded+"\n");
				System.out.printf("The initial blocking number: "+b0+"\n");
				System.out.printf("Blocking number after movements are complete: "+bm+"\n");
				System.out.printf("Voluntary shifts needed: "+m+"\n");
				int currentObj = h + b0 - nrOfBlockingsAfterMovement(route, m,h, j,k, containers) ;
				if(currentObj >= obj[0]) {
					obj[0] = currentObj;
					obj[1] = m;
					obj[3] = nrOfLayers-freespots-m;
				}
			}
		}

		return obj;
	}
	
	public boolean pileBelowIsInOrder(Boat boat, int route, int zlocation, int j, int k, int dest) {	
		int destPrev = 100;
		int count = 0;
		for(int i = 0; i < zlocation ;i++) {					
				if(getProximityInRoute(route, this.destStowage[i][j][k]) <= destPrev)  {
					destPrev = getProximityInRoute(route, this.destStowage[i][j][k]);
					count++;
				}		
		}
		int ownIndex = getProximityInRoute(route, dest);
		if(count == zlocation && ownIndex <= destPrev) {
			return true;
		}
		return false;
	}
	
	public boolean pileIsEligible(int j, int k, int weight) {
		if(this.stowage[0][j][k] > 0 && this.stowage[nrOfLayers -1][j][k] == 0 && highestContainerHasWeight(j,k,weight)) {
			return true;
		}else {
			return false;
		}
	}
	
	public int[] giveHighestRank(int weight){
		int[] tierRowBay = {-1,-1,-1};
		double highscore =-1;
		for(int i =0;i<Boat.nrOfLayers-1;i++) {
			for(int j =0;j<Boat.nrOfRows;j++) {
				for(int k =0;k<Boat.nrOfBays;k++) {
					if(destStowage[i][j][k]>0 && destStowage[i+1][j][k]==0) {
						int place = destStowage[i][j][k]-1;
						System.out.printf("Place in the rank list trying to obtain: "+place+"\n");
						double score = TerminalSet.ranks[place];
						if(score >= highscore) {
							highscore = score;
							tierRowBay[0] = i+1;
							tierRowBay[1] = j;
							tierRowBay[2] = k;
						}
					}
				}
			}
		}
		return tierRowBay;
	}
	
	

	

	
}
