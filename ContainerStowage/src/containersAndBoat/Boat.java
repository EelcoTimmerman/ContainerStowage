package containersAndBoat;
import java.util.List;

import stowage.CreateStowage;
import stowage.Terminal;

public class Boat {
	public static int nrOfBays = 6;
	public static int nrOfLayers = 3;
	public static int nrOfRows = 2;
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
	public int q3 = 10;  // front/back weight diff

	public static int weightBoat;
	public int[][][] stowage = new int[nrOfLayers][nrOfRows][nrOfBays];
	
	public Boat() {
		System.out.print("A boat has been created \n");
		checkIfStable();
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
	}
	
	public int[][][] getStowage(){
		return stowage;		
	}
		
	public void set20footSpotOccupied(int layer, int row, int bay, int weight) {
		stowage[layer][row][bay] = weight;
	}
	
	public void set40footSpotOccupied(int layer, int row, int bay) {
		if(bay >= nrOfBays - 1) {
			System.out.print("Trying to put a container overboard nerd..");
		}
		stowage[layer][row][bay] = 2;
		stowage[layer][row][bay + 1] = 2;
	}
	
	public void set20LocationEmpty(int layer, int row, int bay) {
		stowage[layer][row][bay] = 0;
	}	
	
	public void set40LocationEmpty(int layer, int row, int bay) {
		stowage[layer][row][bay] = 0;
		stowage[layer][row][bay + 1] = 0;
	}
	
	public int visitTerminal(Terminal terminal, Boat boat, List<Container> containers) {
		int shifts = 0;
		CreateStowage.removeExport(boat, terminal, containers);
		shifts += terminal.shiftedContainers.size();
		CreateStowage.loadBoat(boat, terminal, containers);
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
	
	public void calcWeight(){
		this.leftweight = 0;
		this.rightweight =0;
		this.backweight = 0;
		this.frontweight =0;
		if(rowsAreEven() && nrOfRows > 1) {
			for(int i =0;i<nrOfLayers;i++) {
				for(int j=0;j<nrOfRows/2;j++) {
					for(int k = 0;k<nrOfBays;k++) {
						this.leftweight += stowage[i][j][k];
					}
				}
			}
			for(int i =0;i<nrOfLayers;i++) {
				for(int j=nrOfRows/2;j<nrOfRows;j++) {
					for(int k = 0;k<nrOfBays;k++) {
						this.rightweight += stowage[i][j][k];
					}
				}
			}
		}else if(rowsAreEven() == false && nrOfRows > 1) {
			for(int i =0;i<nrOfLayers;i++) {
				for(int j=0;j<nrOfRows/2;j++) {
					for(int k = 0;k<nrOfBays;k++) {
						this.leftweight += stowage[i][j][k];
					}
				}
			}
			for(int i =0;i<nrOfLayers;i++) {
				for(int j=nrOfRows/2 +1;j<nrOfRows;j++) {
					for(int k = 0;k<nrOfBays;k++) {
						this.rightweight += stowage[i][j][k];
					}
				}
			}
		}else {
			this.leftweight = 0;
			this.rightweight = 0;
		}
		if(baysAreEven()) {
			for(int i =0;i<nrOfLayers;i++) {
				for(int j=0;j<nrOfRows;j++) {
					for(int k = 0;k<nrOfBays/2;k++) {
						this.backweight += stowage[i][j][k];
					}
				}
			}
			for(int i =0;i<nrOfLayers;i++) {
				for(int j=0;j<nrOfRows;j++) {
					for(int k = nrOfBays /2;k<nrOfBays;k++) {
						this.frontweight += stowage[i][j][k];
					}
				}
			}
		}else{
			for(int i =0;i<nrOfLayers;i++) {
				for(int j=0;j<nrOfRows;j++) {
					for(int k = 0;k<nrOfBays/2;k++) {
						this.backweight += stowage[i][j][k];
					}
				}
			}
			for(int i =0;i<nrOfLayers;i++) {
				for(int j=0;j<nrOfRows;j++) {
					for(int k = nrOfBays/2 +1;k<nrOfBays;k++) {
						this.frontweight += stowage[i][j][k];
					}
				}
			}
		}		
		System.out.printf("Left Weight: "+this.leftweight+"\n");
		System.out.printf("Right Weight: "+this.rightweight+"\n");
		System.out.printf("Front Weight: "+this.frontweight+"\n");
		System.out.printf("Back Weight: "+this.backweight+"\n");
	}

	
	public boolean blockAssignmentIsNecessary() {
	  return false;
	}
	
	public int whereIsBalanceWeightNeeded() {
		//which block needs the weight?
		int f =0;
		int b = 0;
		int r = 0;
		int l = 0;
		int block = 0;	
		if(frontweight>backweight) {
				 b = this.frontweight - this.backweight - q3;
			}else if(this.backweight> this.frontweight) {
				f = this.frontweight - this.backweight - q3;
			}
			if(rightweight> leftweight) {
				l = this.rightweight - this.leftweight - q2;
			}else {
				r = this.leftweight - this.rightweight - q2;
			}
			if(f == 0 && b == 0 && r ==0 && l ==0) {
				block = 0;
			}else if(f > 0 && b == 0 && r ==0 && l ==0) {
				block = 1;
			}else if(f == 0 && b == 0 && r > 0 && l ==0) {
				block = 2;
			}else if(f == 0 && b > 0 && r ==0 && l ==0) {
				block = 3;
			}else if(f == 0 && b == 0 && r ==0 && l > 0) {
				block = 4;
			}else if(f > 0 && b == 0 && r == 0 && l >0) {
				block = 5;
			}else if(f > 0&& b == 0&& r > 0 && l ==0) {
				block = 6;
			}else if(f == 0&& b > 0&& r  > 0 && l ==0) {
				block = 7;
			}else if(f == 0&& b > 0&& r  == 0 && l > 0) {
				block = 8;
			}
			return block;
	}
	
	public boolean checkIfStable() {
		if(this.frontweight - this.backweight<this.q3 && this.backweight - this.frontweight<this.q3
				&& this.leftweight - this.rightweight<this.q2 && this.rightweight - this.leftweight<this.q2) {
			return true;
		}else {
			return false;
		}
		
		
	}
	
}
