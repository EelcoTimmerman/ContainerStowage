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
		
		public boolean findFeasibleLocation(Boat boat){ // does not meet all feasibility criteria yet
			int cId = this.id +1;
			//findfeasible block
			outerloop:
			for(int i = 0; i< boat.nrOfLayers;i++) {
				  for(int j = 0; j< boat.nrOfRows;j++) {
					  for(int k = 0; k< boat.nrOfBays;k++) {
								 if( is20Feasible(boat,i,j,k) ) {
									  boat.set20footSpotOccupied(i,j,k, this.weight);
									  updateLocationOnBarge(i,j,k);
									  transported = true;
									  break outerloop;
								 }
							  	if(i==boat.nrOfLayers -1 && j == boat.nrOfRows -1 && k == boat.nrOfBays -1) {
							  			System.out.printf("Container "+cId+" can unfortunately not be taken..how sad\n");
							  			return false;
							  	}
					  }
				  }  
			}
			return true;
		}
		
		public void findFeasibleBlock() {
			
		}
		
		public int getWeight() {
			return this.weight;
		}
		
		public int getOrder() {
			int weight = this.weight*10;
			int dest = -1;
			for(int i =0;i<TerminalSet.nrOfTerminals-1;i++) {
				if(this.destination.id == TerminalSet.routes[0][i]) {
					dest = i;
				}
			}
			int res = weight + dest;
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