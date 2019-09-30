package containersAndBoat;
import stowage.Terminal;

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

		
		public Container(int id) {
			this.id = id;
			xLoc = -1;
			yLoc = -1;
			zLoc = -1;
			isOnBarge = false;
			transported = false;
		}
		public void tellPosition() {
			int rew = id+1;
			int tId = this.destination.id;
			if(this.type == ContainerType.TWENTY) {
				if(export) {
					System.out.printf("Container nr" + rew +", 20foot export to destination:" + tId +", in position " + zLoc + yLoc + xLoc + "\n");
				}else {
					System.out.printf("Container nr" + rew +", 20foot import from destination:" + tId +", in position " + zLoc + yLoc + xLoc + "\n");
				}
			}else{
				if(export) {
					System.out.printf("Container nr" + rew +", 40foot export to destination:" + tId +", in position " + zLoc + yLoc + xLoc + "\n");
				}else {
					System.out.printf("Container nr" + rew +", 40foot import from destination:" + tId +", in position " + zLoc + yLoc + xLoc + "\n");
				}			}
			
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
			outerloop:
			for(int i = 0; i< boat.nrOfLayers;i++) {
				  for(int j = 0; j< boat.nrOfRows;j++) {
					  for(int k = 0; k< boat.nrOfBays;k++) {
							  if(this.type == ContainerType.TWENTY) {
								 if( is20Feasible(boat,i,j,k) ) {
									  boat.set20footSpotOccupied(i,j,k);
									  updateLocationOnBarge(i,j,k);
									  transported = true;
									  break outerloop;
								 }
							  }else {
								  if(this.type != ContainerType.FORTY) {
									  System.out.print("Error noob, this should be a 40 container.");
								  }
								  if( is40Feasible(boat,i,j,k) ) {
									  boat.set40footSpotOccupied(i,j,k);
									  updateLocationOnBarge(i,j,k);
									  transported = true;
									  break outerloop;

								  }
								  
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
		
		public void updateLocationOnBarge(int i, int j, int k) {
			this.xLoc = k;
			this.yLoc = j;
			this.zLoc = i;
			this.isOnBarge = true;
		}
		
		public void removeFromBarge(Boat barge) {
			//it should remove the container itself and all containers that are on top of it, if one of the containers on top is an 40 foot container
			//it should remove that entire pile too. They then should be added to a kind of list that has to be replaced no matter what, cause
			//otherwise there are containers left at a terminal where they don't belong
			//furthermore, the removing should start from the top.
				if(this.type == ContainerType.TWENTY) {
					barge.set20LocationEmpty(this.zLoc, this.yLoc, this.xLoc);
				}else {
					barge.set40LocationEmpty(this.zLoc, this.yLoc, this.xLoc);
				}
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
					if( (i==0) || (i>0 && boat.stowage[i-1][j][k] == 1) ) {
						count++;
					}

			
				if(count == 2) {
					return true;
				}else {
					return false;
				}
		 }
		 
		 public boolean is40Feasible(Boat boat, int i, int j, int k) {
				int count =0;
			 if(k < boat.nrOfBays -1) {
					if(k==0 || k % 2 == 0) {
						count++;
					}					
					if(boat.stowage[i][j][k] == 0 && boat.stowage[i][j][k+1] == 0) {
						count++;
					}
					if(i == 0 || (i != 0 && boat.stowage[i-1][j][k] != 0 && boat.stowage[i-1][j][k+1] != 0) ) {
							count++;
						}
				}
			 if(count == 3) {
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