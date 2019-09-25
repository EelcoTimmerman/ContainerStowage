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

		
		public Container(int id) {
			this.id = id;
			xLoc = -1;
			yLoc = -1;
			zLoc = -1;
			isOnBarge = false;
		}
		public void tellPosition() {
			int rew = id+1;
			System.out.printf("Container nr" + rew +" in position " + zLoc + yLoc + xLoc + "\n");
		}
		
		public void tellDestination() {
			int cId = id+1;
			int tId = this.destination.id;
			if(export == true) {
				System.out.printf("Container nr" + cId +", to be exported to destination:" + tId +"\n");
			}else {
				System.out.printf("Container nr" + cId +", to be imported from destination:" + tId +"\n");
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
		
		public void findFeasibleLocation(Boat boat){ // does not meet all feasibility criteria yet
			outerloop:
			for(int i = 0; i< boat.nrOfLayers;i++) {
				  for(int j = 0; j< boat.nrOfRows;j++) {
					  for(int k = 0; k< boat.nrOfBays;k++) {
						  if( isFeasible(boat,i,j,k) ) {
							  boat.setLocationOccupied(i,j,k);
							  updateLocationOnBarge(i,j,k);
							  break outerloop;
						  }
						  
					  }
				  }  
			}	  
		}
		
		public void updateLocationOnBarge(int i, int j, int k) {
			this.xLoc = k;
			this.yLoc = j;
			this.zLoc = i;
			this.isOnBarge = true;
		}
		
		public void removeFromBarge(Boat barge) {
			if(export == false) {
				System.out.print("This is an import container, it should never be removed");
			}
			barge.setLocationEmpty(this.zLoc, this.yLoc, this.xLoc);
			this.xLoc = -1;
			this.yLoc = -1;
			this.zLoc = -1;
			this.isOnBarge = false;
		}
		
		 public boolean isFeasible(Boat boat, int i, int j, int k) {
			int count = 0;	
			 if(boat.stowage[i][j][k] == 0) {
					count++;
				}
			 if( (i==0) || (i>0 && boat.stowage[i-1][j][k] != 0) ) {
				 count++;
			 }
			 if(count == 2) {
				 return true;
			 }else {
				 return false;
			 }
			}
		


}