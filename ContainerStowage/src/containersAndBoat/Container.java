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
		public boolean hasLocation;

		
		public Container(int id) {
			this.id = id;
			xLoc = -1;
			yLoc = -1;
			zLoc = -1;
			hasLocation = false;
		}
		public void talk() {
			int rew = id+1;
			System.out.printf("Hey I am container nr" + rew +" in position " + zLoc + yLoc + xLoc + "\n");
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
			this.hasLocation = true;
		}
		
		public void removeFromBarge() {
			this.xLoc = -1;
			this.yLoc = -1;
			this.zLoc = -1;
			this.hasLocation = false;
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