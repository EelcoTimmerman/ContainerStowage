package containersAndBoat;
import java.util.ArrayList;
import java.util.List;

import stowage.CreateStowage;
import stowage.Terminal;
import stowage.TerminalSet;

public class Boat {
	public static int nrOfBays = 4;
	public static int nrOfLayers = 3;
	public static int nrOfRows = 1;
	public int leftback = 0;
	public int rightback = 0;
	public int leftfront = 0;
	public int rightfront = 0;
	public int leftweight = 0;
	public int rightweight= 0;
	public int backweight =0;
	public int frontweight =0;
	public int q1 = 500; //barge capacity
	public int q2 = 10; // max left/right weight difference
	public int q3 = 10;  // front/back weight difference
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
		calcWeight();
		//reportWeight();
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
		destStowage[layer][row][bay] = -1;
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
			l = this.rightweight +weight- this.leftweight - q2;
		}else {
			r = this.leftweight +weight - this.rightweight - q2;
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
		if(dest < 50) {
			for(int i =0;i<TerminalSet.nrOfTerminals-1;i++) {
				if(TerminalSet.routes[route][i] == dest) {
					index = i;
				}
			}
		}else {
			index = 50;
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
		for(int i=0;i<nrOfLayers-1;i++) {
			if(stowage[i][j][k] == weight) {
				count++;
			}
		}
		return count;
	}
	
	public int calcPileScoreRightWeightOnTop(int route, int j, int k, int weight, List<Container> containers) {
		int freespots = 0;
		int b0 = 0;
		for(int i=0;i<nrOfLayers;i++) {
			if(stowage[i][j][k] == 0) {
				freespots++;
			}
			if(i>0 && getProximityInRoute(route, destStowage[i-1][j][k]) > getProximityInRoute(route, destStowage[i][j][k]) && stowage[i][j][k] > 0) {
				b0++;
			}
		}
		int h = Math.min(freespots, containers.size());
		int n = amountInWeightClassOnTop(j,k,weight);
		int obj = 0;
		for(int i = 0;i< n;i++) {
			int currentObj = h  ;
			if(currentObj > obj) {
				obj = currentObj;
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
	
}
