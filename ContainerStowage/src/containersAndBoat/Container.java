package containersAndBoat;
import stowage.Terminal;
import stowage.TerminalSet;

public class Container {
		public int id;
		public ContainerType type;
		public int weight;
		public Terminal destination;
		public int xLoc;
		public int yLoc;
		public int zLoc;
		public boolean isOnBarge;
		public boolean export;
		public boolean transported;
		public boolean shifted;

		
		public Container(int id) {
			this.id = id;
			xLoc = -1;
			yLoc = -1;
			zLoc = -1;
			isOnBarge = false;
			transported = false;
			shifted = false;
		}
		public void tellPosition() {
			int rew = id+1;
			int tId = this.destination.id;
			if(this.weight == 1) {
				if(export) {
					System.out.printf("Container nr" + rew +", lightweight export to destination:" + tId +", in position " + zLoc + yLoc + xLoc + "\n");
				}else {
					System.out.printf("Container nr" + rew +", lightweight import from destination:" + tId +", in position " + zLoc + yLoc + xLoc + "\n");
				}
			}else if(this.weight == 2){
				if(export) {
					System.out.printf("Container nr" + rew +", mediumweight export to destination:" + tId +", in position " + zLoc + yLoc + xLoc + "\n");
				}else {
					System.out.printf("Container nr" + rew +", mediumweight import from destination:" + tId +", in position " + zLoc + yLoc + xLoc + "\n");
				}
			}else {
				if(export) {
					System.out.printf("Container nr" + rew +", heavyweight export to destination:" + tId +", in position " + zLoc + yLoc + xLoc + "\n");
				}else {
					System.out.printf("Container nr" + rew +", heavyweight import from destination:" + tId +", in position " + zLoc + yLoc + xLoc + "\n");
				}
			}
			
		}
		
		public void setType(ContainerType newtype){
			this.type = newtype;
		}
		
		public void setWeight(int newweight) {
			this.weight = newweight;
		}
		
		public void setDestination(Terminal terminal) {
			this.destination = terminal;
		}
		
		public boolean findFeasibleLocation(Boat boat){
			boat.calcWeight();
			if(boat.checkIfStableWithNewContainer(this.weight)) {
				findFeasibleLocationInBlock(boat, 0);
			}else {
				int block =0;
				block = boat.whereIsBalanceWeightNeeded(this.weight);
				findFeasibleLocationInBlock(boat, block);
			}		
			return true;	
		}
		
		public boolean findFeasibleLocationInBlock(Boat boat, int block) {
			int rowstart = 0;
			int rowend = Boat.nrOfRows;
			int baystart = 0;
			int bayend = Boat.nrOfBays;
			if(block == 1) {
				System.out.printf("Looking for a slot in block 1\n");
				if(boat.baysAreEven()) {
					baystart =  Boat.nrOfBays/2;
				}else {
					baystart =  Boat.nrOfRows/2 +1;
				}
			}
			if(block == 2) {
				System.out.printf("Looking for a slot in block 2\n");
				if(boat.rowsAreEven()) {
					rowstart =  Boat.nrOfRows/2;
				}else {
					rowstart =  Boat.nrOfRows/2 +1;
				}
			}
			if(block == 3) {
				System.out.printf("Looking for a slot in block 3\n");
				bayend = Boat.nrOfBays/2;
			}
			if(block == 4) {
				System.out.printf("Looking for a slot in block 4\n");
				rowend = Boat.nrOfRows/2;
			}
			if(block == 5) {
				System.out.printf("Looking for a slot in block 5\n");
				if(boat.baysAreEven()) {
					baystart =  Boat.nrOfBays/2;
				}else {
					baystart =  Boat.nrOfRows/2 +1;
				}
				rowend = Boat.nrOfRows/2;
			}
			if(block == 6) {
				System.out.printf("Looking for a slot in block 6\n");
				if(boat.rowsAreEven()) {
					rowstart =  Boat.nrOfRows/2;
				}else {
					rowstart =  Boat.nrOfRows/2 +1;
				}
				if(boat.baysAreEven()) {
					baystart =  Boat.nrOfBays/2;
				}else {
					baystart =  Boat.nrOfRows/2 +1;
				}
			}
			if(block == 7) {
				System.out.printf("Looking for a slot in block 7\n");
				bayend = Boat.nrOfBays/2;
				if(boat.rowsAreEven()) {
					rowstart =  Boat.nrOfRows/2;
				}else{
					rowstart =  Boat.nrOfRows/2 +1;
				}
			}
			if(block == 8) {
				System.out.printf("Looking for a slot in block 8\n");
				rowend = Boat.nrOfRows/2;
				bayend = Boat.nrOfBays/2;
			}
			
			if(this.weight == 3) {
				if(this.export){
					
				}else {
					
				}
			}else if(this.weight == 2) {
				if(this.export){
					
				}else {
					
				}
			}else {
				if(this.export){
					
				}else {
					
				}
			}			
			return true;
		}
		
		public boolean placeHeavyImport(Boat boat, int rowstart, int rowend, int baystart, int bayend) {
			int nrOfRules = 1;
			outerloop:
			for(int z= 0; z< nrOfRules; z++) {
				for(int i = 0; i< Boat.nrOfLayers;i++){
				  for(int j = rowstart; j< rowend;j++){
					  for(int k = baystart; k< bayend;k++) {
								 if(z == 0) {
							  			if(is20Feasible(boat,i,j,k) && this.destination.id ==1 ) {
											  boat.set20footSpotOccupied(i,j,k, this.weight);
											  updateLocationOnBarge(i,j,k);
											  transported = true;
											  break outerloop;
										 }
								 }else if(z == 1) {
									 
								 }

					  }
				  }  
				}
			}
			return true;
		}
		
		public int getReverseWeight() {
			return -this.weight;
		}
		
		public int getOrder() {
			int weight = -this.weight*100;
			int dest = -1;
			for(int i =0;i<TerminalSet.nrOfTerminals-1;i++) {
				if(this.destination.id == TerminalSet.routes[0][i]) {
					dest = i;
				}
			}
			int res;
			if(this.export == true) {
				res = weight + dest + 50;
			}else {
				res = weight + dest;
			}
			return res;
		}
		
		public void updateLocationOnBarge(int i, int j, int k) {
			this.xLoc = k;
			this.yLoc = j;
			this.zLoc = i;
			this.isOnBarge = true;
		}
		
		public void removeFromBarge(Boat barge) {
				barge.set20LocationEmpty(this.zLoc, this.yLoc, this.xLoc);
				this.xLoc = -1;
				this.yLoc = -1;
				this.zLoc = -1;
				this.isOnBarge = false;
		}
		
		public void shiftFromBarge(Boat barge) {
			barge.set20LocationEmpty(this.zLoc, this.yLoc, this.xLoc);
			this.shifted = true;
			this.xLoc = -1;
			this.yLoc = -1;
			this.zLoc = -1;
			this.isOnBarge = false;
		}
		
		 public boolean is20Feasible(Boat boat, int i, int j, int k) {
			int count = 0;	
					if(boat.stowage[i][j][k] == 0) {
						count++;
					}
					if( (i==0) || (i>0 && boat.stowage[i-1][j][k] >= this.weight) ) {
						count++;
					}
				if(count == 2) {
					return true;
				}else {
					return false;
				}
		 }
		 
		 public boolean isInLayer(int i) {
			 if(this.zLoc == i) {
				 return true;
			 }else {
				 return false;
			 }
		 }
		


}