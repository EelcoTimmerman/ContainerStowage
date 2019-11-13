package containersAndBoat;
import stowage.Terminal;
import stowage.TerminalSet;

public class Container {
		public int id;
		public int displayID;
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
			this.displayID = id+1;
			xLoc = -1;
			yLoc = -1;
			zLoc = -1;
			isOnBarge = false;
			transported = false;
			shifted = false;
		}
		public void tellPosition() {
			int tId = this.destination.id;
			if(this.weight == 1) {
				if(export) {
					System.out.printf("Container nr" + displayID +", lightweight export to destination:" + tId +", in position " + zLoc + yLoc + xLoc + "\n");
				}else {
					System.out.printf("Container nr" + displayID +", lightweight import from destination:" + tId +", in position " + zLoc + yLoc + xLoc + "\n");
				}
			}else if(this.weight == 2){
				if(export) {
					System.out.printf("Container nr" + displayID +", mediumweight export to destination:" + tId +", in position " + zLoc + yLoc + xLoc + "\n");
				}else {
					System.out.printf("Container nr" + displayID +", mediumweight import from destination:" + tId +", in position " + zLoc + yLoc + xLoc + "\n");
				}
			}else {
				if(export) {
					System.out.printf("Container nr" + displayID +", heavyweight export to destination:" + tId +", in position " + zLoc + yLoc + xLoc + "\n");
				}else {
					System.out.printf("Container nr" + displayID +", heavyweight import from destination:" + tId +", in position " + zLoc + yLoc + xLoc + "\n");
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
				System.out.printf("Looking for a slot in block 1 for container"+displayID+"\n");
				if(boat.baysAreEven()) {
					baystart =  Boat.nrOfBays/2;
				}else {
					baystart =  Boat.nrOfRows/2 +1;
				}
			}
			if(block == 2) {
				System.out.printf("Looking for a slot in block 2 for container "+displayID+"\n");
				if(boat.rowsAreEven()) {
					rowstart =  Boat.nrOfRows/2;
				}else {
					rowstart =  Boat.nrOfRows/2 +1;
				}
			}
			if(block == 3) {
				System.out.printf("Looking for a slot in block 3 for container "+displayID+"\n");
				bayend = Boat.nrOfBays/2;
			}
			if(block == 4) {
				System.out.printf("Looking for a slot in block 4 for container "+displayID+"\n");
				rowend = Boat.nrOfRows/2;
			}
			if(block == 5) {
				System.out.printf("Looking for a slot in block 5 for container "+displayID+"\n");
				if(boat.baysAreEven()) {
					baystart =  Boat.nrOfBays/2;
				}else {
					baystart =  Boat.nrOfRows/2 +1;
				}
				rowend = Boat.nrOfRows/2;
			}
			if(block == 6) {
				System.out.printf("Looking for a slot in block 6 for container "+displayID+"\n");
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
				System.out.printf("Looking for a slot in block 7 for container "+displayID+"\n");
				bayend = Boat.nrOfBays/2;
				if(boat.rowsAreEven()) {
					rowstart =  Boat.nrOfRows/2;
				}else{
					rowstart =  Boat.nrOfRows/2 +1;
				}
			}
			if(block == 8) {
				System.out.printf("Looking for a slot in block 8 for container "+displayID+"\n");
				rowend = Boat.nrOfRows/2;
				bayend = Boat.nrOfBays/2;
			}
			if(this.weight == 3) {
				if(this.export){
					if(placeHeavyExport(boat, rowstart, rowend, baystart, bayend) == false) {
						putInAnyPlace(boat, rowstart, rowend, baystart, bayend);
					}
				}else {
					if(placeHeavyImport(boat, rowstart, rowend, baystart, bayend) == false) {
						putInAnyPlace(boat, rowstart, rowend, baystart, bayend);
					}				
				}
			}
			if(this.weight == 2) {
				if(this.export) {
					if(placeMediumExport(boat, rowstart, rowend, baystart, bayend) == false) {
						putInAnyPlace(boat, rowstart, rowend, baystart, bayend);
					}
				}else {
					if(placeMediumImport(boat, rowstart, rowend, baystart, bayend) == false) {
							putInAnyPlace(boat, rowstart, rowend, baystart, bayend);
					}				
				}			
			}
			if(this.weight == 1){
				if(this.export){
					if(placeLightExport(boat, rowstart, rowend, baystart, bayend) == false) {
						putInAnyPlace(boat, rowstart, rowend, baystart, bayend);
					}
				}else {
					if(placeLightImport(boat, rowstart, rowend, baystart, bayend) == false) {
						putInAnyPlace(boat, rowstart, rowend, baystart, bayend);
					}					
				}
			}			
			return true;
		}
		
		public boolean placeHeavyImport(Boat boat, int rowstart, int rowend, int baystart, int bayend) {
			if(putContainerOnSameDest(boat, rowstart, rowend,baystart,bayend)) {
				return true;
			}else if(putItOnTheBottom(boat, rowstart, rowend,baystart,bayend)) {
				return true;
			}
		return false;
		}
		
		public boolean placeHeavyExport(Boat boat, int rowstart, int rowend, int baystart, int bayend) {
				if(putContainerOnSameDest(boat, rowstart, rowend,baystart,bayend)) {
					return true;
				}else if(putItOnTheBottom(boat, rowstart, rowend,baystart,bayend)) {
					return true;
				}			
			return false;
		}
		
		public boolean placeMediumImport(Boat boat, int rowstart, int rowend, int baystart, int bayend) {
			if(putContainerOnSameDest(boat, rowstart, rowend,baystart,bayend)) {
				return true;
			}	
		return false;
		}
		
		public boolean placeMediumExport(Boat boat, int rowstart, int rowend, int baystart, int bayend) {
			if(putContainerOnSameDest(boat, rowstart, rowend,baystart,bayend)) {
				return true;
			}	
		return false;
		}
		
		public boolean placeLightExport(Boat boat, int rowstart, int rowend, int baystart, int bayend) {
			if(putContainerOnSameDest(boat, rowstart, rowend,baystart,bayend)) {
				return true;
			}	
		return false;
		}
		
		public boolean placeLightImport(Boat boat, int rowstart, int rowend, int baystart, int bayend) {
			if(putContainerOnSameDest(boat, rowstart, rowend,baystart,bayend)) {
				return true;
			}	
		return false;
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
		
		public boolean putInAnyPlace(Boat boat, int rowstart, int rowend, int baystart, int bayend) {
			System.out.print("Putting container "+displayID+" in any place.\n");
			for(int i = 0; i< Boat.nrOfLayers;i++){
				  for(int j = rowstart; j< rowend;j++){
					  for(int k = baystart; k< bayend;k++) {
						  if(is20Feasible(boat,i,j,k)) {
							  boat.set20footSpotOccupied(i,j,k, this.weight);
							  updateLocationOnBarge(i,j,k);
							  transported = true;
							  return true;
						  }
					  }
				  }
			}
		return false;
			
		}
		
		public boolean putContainerOnSameDest(Boat boat, int rowstart, int rowend, int baystart, int bayend) {		
			if(this.weight == 1) {
				if( tryOnLightContainer(boat,rowstart, rowend, baystart,bayend) ) {
					System.out.print("Putting container "+displayID+" on top of a light one.\n");
					return true;
				}else if(tryOnMediumContainer(boat,rowstart, rowend, baystart,bayend)){
					System.out.print("Putting container "+displayID+" on top of a medium one.\n");
					return true;
				}else {
					if(tryOnHeavyContainerSameDest(boat,rowstart, rowend, baystart,bayend)) {
						System.out.print("Putting container "+displayID+" on top of a heavy one.\n");
						return true;
					}
				}
			}
			if(this.weight == 2) {
				if(tryOnMediumContainer(boat,rowstart, rowend, baystart,bayend) ) {
					System.out.print("Putting container "+displayID+" on top of a medium one.\n");
					return true;
				}else {
					if(tryOnHeavyContainerSameDest(boat,rowstart, rowend, baystart,bayend)) {
						System.out.print("Putting container "+displayID+" on top of a heavy one.\n");
						return true;
					}
				}
			}
			if(this.weight == 3) {
				if(tryOnHeavyContainerSameDest(boat,rowstart, rowend, baystart,bayend) ) {
					System.out.print("Putting container "+displayID+" on top of a heavy one.\n");
					return true;
				}
			}
			return false;
		}
		
		public boolean tryOnLightContainer(Boat boat, int rowstart, int rowend, int baystart, int bayend) {
			for(int i = 1; i< Boat.nrOfLayers;i++){
				  for(int j = rowstart; j< rowend;j++){
					  for(int k = baystart; k< bayend;k++) {
						  if(is20Feasible(boat,i,j,k) && containerBelowIsLightweight(boat, i,j,k) && containerBelowHasSameDestination(boat,i,j,k)) {
							  boat.set20footSpotOccupied(i,j,k, this.weight);
							  updateLocationOnBarge(i,j,k);
							  transported = true;
							  return true;
						  }
					  }
				  }
			}
		return false;
		}
		
		public boolean putItOnTheBottom(Boat boat, int rowstart, int rowend, int baystart, int bayend) {	
			for(int j = rowstart; j< rowend;j++){
				for(int k = baystart; k< bayend;k++) {
					if(is20Feasible(boat,0,j,k) ) {
						System.out.print("Putting container"+displayID+" on the bottom.\n");
						boat.set20footSpotOccupied(0,j,k, this.weight);
						updateLocationOnBarge(0,j,k);
						transported = true;
						return true;
					}
				}
			}		
			return false;
		}
		
		public boolean tryOnMediumContainer(Boat boat, int rowstart, int rowend, int baystart, int bayend) {
			for(int i = 1; i< Boat.nrOfLayers;i++){
				  for(int j = rowstart; j< rowend;j++){
					  for(int k = baystart; k< bayend;k++) {
						  if(is20Feasible(boat,i,j,k) && containerBelowIsMediumweight(boat,i,j,k) && containerBelowHasSameDestination(boat,i,j,k)) {
							  boat.set20footSpotOccupied(i,j,k, this.weight);
							  updateLocationOnBarge(i,j,k);
							  transported = true;
							  return true;
						  }
					  }
				  }
			}
		return false;
		}
		
		public boolean tryOnHeavyContainerSameDest(Boat boat, int rowstart, int rowend, int baystart, int bayend) {
			for(int i = 1; i< Boat.nrOfLayers;i++){
				  for(int j = rowstart; j< rowend;j++){
					  for(int k = baystart; k< bayend;k++) {
						  if( is20Feasible(boat,i,j,k) && containerBelowHasSameDestination(boat,i,j,k)) {
							  boat.set20footSpotOccupied(i,j,k, this.weight);
							  updateLocationOnBarge(i,j,k);
							  transported = true;
							  return true;
						  }
					  }
				  }
			}
		return false;
		}

		
		public boolean containerBelowIsMediumweight(Boat boat, int i, int j, int k ) {
			if(i>0 && boat.stowage[i-1][j][k] == 2) {
				return true;
			}
			return false;
		}
		
		public boolean containerBelowIsLightweight(Boat boat, int i, int j, int k ) {
			if(i>0 && boat.stowage[i-1][j][k] == 1) {
				return true;
			}
			return false;
		}
		
		
		public boolean containerBelowHasSameDestination(Boat boat, int i, int j, int k ) {
			if(this.export == true) {
				if(i > 0) {
					for(Container c:boat.containersOnBoat) {
						if(c.destination.id == this.destination.id) {
							if(c.xLoc == k && c.yLoc == j && c.zLoc == i-1 && c.export) {
								return true;
							}
						}

					}
				}
			}else {
				if(i > 0) {
					for(Container c:boat.containersOnBoat) {
						if(c.xLoc == k && c.yLoc == j && c.zLoc == i-1 &&c.export == false) {
							return true;
						}
					}
				}
			}
			return false;
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