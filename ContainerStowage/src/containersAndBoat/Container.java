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
		
		public int getDest() {
			if(this.export) {
				return this.destination.id;
			}else {
				return 50;
			}
		}
		
		public boolean findFeasibleLocation(Boat boat, int route){
			boat.calcWeight();
			if(boat.checkIfStableWithNewContainer(this.weight)) {
				return findFeasibleLocationInBlock(boat,route, 0);
			}else {
				int block =0;
				block = boat.whereIsBalanceWeightNeeded(this.weight);
				return findFeasibleLocationInBlock(boat, route, block);
			}		
		}
		
		public boolean findFeasibleLocationInBlock(Boat boat, int route, int block) {
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
					if(placeHeavyExport(boat,route, rowstart, rowend, baystart, bayend) == false) {
						System.out.print("Container "+this.displayID+" is shifted left for the MVSD.");
					}
				}else {
					if(placeHeavyImport(boat,route, rowstart, rowend, baystart, bayend) == false) {
						System.out.print("Container "+this.displayID+" is shifted left for the MVSD.");
					}				
				}
			}
			if(this.weight == 2) {
				if(this.export) {
					if(placeMediumExport(boat,route, rowstart, rowend, baystart, bayend) == false) {
						System.out.print("Container "+this.displayID+" is shifted left for the MVSD.");
					}
				}else {
					if(placeMediumImport(boat,route, rowstart, rowend, baystart, bayend) == false) {
						System.out.print("Container "+this.displayID+" is shifted left for the MVSD.");
					}				
				}			
			}
			if(this.weight == 1){
				if(this.export){
					if(placeLightExport(boat,route, rowstart, rowend, baystart, bayend) == false) {
						System.out.print("Container "+this.displayID+" is shifted left for the MVSD.");
					}
				}else {
					if(placeLightImport(boat,route, rowstart, rowend, baystart, bayend) == false) {
						System.out.print("Container "+this.displayID+" is shifted left for the MVSD.");
					}					
				}
			}			
			return true;
		}
		
		public boolean placeHeavyImport(Boat boat, int route, int rowstart, int rowend, int baystart, int bayend) {
			if(putContainerOnSameDest(boat, route,rowstart, rowend,baystart,bayend)) {
				return true;
			}else if(putItOnTheBottom(boat, rowstart, rowend,baystart,bayend)) {
				return true;
			}else if(putContainerJustNonBlocking(boat, route, rowstart, rowend,baystart,bayend)) {
				return true;
			}
		return false;
		}
		
		public boolean placeHeavyExport(Boat boat,int route, int rowstart, int rowend, int baystart, int bayend) {
				if(putContainerOnSameDest(boat,route, rowstart, rowend,baystart,bayend)) {
					return true;
				}else if(putItOnTheBottom(boat, rowstart, rowend,baystart,bayend)) {
					return true;
				}else if(putContainerOnPileInOrder(boat, route, rowstart, rowend,baystart,bayend)) {
					return true;
				}else if(putContainerJustNonBlocking(boat, route, rowstart, rowend,baystart,bayend)) {
					return true;
				}			
			return false;
		}
		
		public boolean placeMediumImport(Boat boat, int route,int rowstart, int rowend, int baystart, int bayend) {
			if(putContainerOnSameDest(boat,route, rowstart, rowend,baystart,bayend)) {
				return true;
			}else if(putContainerJustNonBlocking(boat, route, rowstart, rowend,baystart,bayend)) {
				return true;
			}	
		return false;
		}
		
		public boolean placeMediumExport(Boat boat,int route, int rowstart, int rowend, int baystart, int bayend) {
			if(putContainerOnSameDest(boat, route, rowstart, rowend,baystart,bayend)) {
				return true;
			}else if(putContainerOnPileInOrder(boat, route, rowstart, rowend,baystart,bayend)) {
				return true;
			}else if(putContainerJustNonBlocking(boat, route, rowstart, rowend,baystart,bayend)) {
				return true;
			}	
		return false;
		}
		
		public boolean placeLightExport(Boat boat,int route, int rowstart, int rowend, int baystart, int bayend) {
			if(putContainerOnSameDest(boat,route, rowstart, rowend,baystart,bayend)) {
				return true;
			}else if(putContainerOnPileInOrder(boat, route, rowstart, rowend,baystart,bayend)) {
				return true;
			}else if(putContainerJustNonBlocking(boat, route, rowstart, rowend,baystart,bayend)) {
				return true;
			}
		return false;
		}
		
		public boolean placeLightImport(Boat boat,int route, int rowstart, int rowend, int baystart, int bayend) {
			if(putContainerOnSameDest(boat,route, rowstart, rowend,baystart,bayend)) {
				return true;
			}else if(putContainerJustNonBlocking(boat, route, rowstart, rowend,baystart,bayend)) {
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
			int check =0;
			for(int i = 0; i< Boat.nrOfLayers;i++){
				  for(int j = rowstart; j< rowend;j++){
					  for(int k = baystart; k< bayend;k++) {
						  if(is20Feasible(boat,i,j,k)) {
							  System.out.print("Putting container "+displayID+" in any place.\n");
							  boat.set20footSpotOccupied(i,j,k, this.weight, getDest());
							  updateLocationOnBarge(i,j,k);
							  transported = true;
							  check =1;
							  return true;
						  }
						  if(i == Boat.nrOfLayers -1 && j == rowend -1 && k == bayend -1 && check  ==0) {
							  System.out.print("Unable to load container "+displayID+" in any place.\n");
						  }

					  }
				  }
			}
		return false;
			
		}
		
		public boolean putContainerOnPileInOrder(Boat boat, int route, int rowstart, int rowend, int baystart, int bayend) {
			if(this.weight == 1) {
				if( tryOnLightContainerInOrder(boat, route,rowstart, rowend, baystart,bayend) ) {
					System.out.print("Putting the light container "+displayID+" on top of a light one, the pile is in order.\n");
					return true;
				}else if(tryOnMediumContainerInOrder(boat, route, rowstart, rowend, baystart,bayend)){
					System.out.print("Putting the light container "+displayID+" on top of a medium one, the pile is in order.\n");
					return true;
				}else {
					if(tryOnHeavyContainerInOrder(boat,route,rowstart, rowend, baystart,bayend)) {
						System.out.print("Putting the light container "+displayID+" on top of a heavy one, the pile is in order..\n");
						return true;
					}
				}
			}
			if(this.weight == 2) {
				if(tryOnMediumContainerInOrder(boat,route,rowstart, rowend, baystart,bayend) ) {
					System.out.print("Putting the medium container "+displayID+" on top of a medium one, the pile is in order..\n");
					return true;
				}else {
					if(tryOnHeavyContainerInOrder(boat,route,rowstart, rowend, baystart,bayend)) {
						System.out.print("Putting the medium container "+displayID+" on top of a heavy one, the pile is in order..\n");
						return true;
					}
				}
			}
			if(this.weight == 3) {
				if(tryOnHeavyContainerInOrder(boat,route,rowstart, rowend, baystart,bayend) ) {
					System.out.print("Putting the heavy container "+displayID+" on top of a heavy one, the pile is in order..\n");
					return true;
				}
			}
			return false;
		}
		
		public boolean putContainerJustNonBlocking(Boat boat, int route, int rowstart, int rowend, int baystart, int bayend) {
			if(this.weight == 1) {
				if( tryOnLightContainerNonBlocking(boat,route, rowstart, rowend, baystart,bayend) ) {
					System.out.print("Putting container "+displayID+" on top of a light one, without blocking anything.\n");
					return true;
				}else if(tryOnMediumContainerNonBlocking(boat,route,rowstart, rowend, baystart,bayend)){
					System.out.print("Putting container "+displayID+" on top of a medium one, without blocking anything.\n");
					return true;
				}else {
					if(tryOnHeavyContainerNonBlocking(boat,route,rowstart, rowend, baystart,bayend)) {
						System.out.print("Putting container "+displayID+" on top of a heavy one, without blocking anything.\n");
						return true;
					}
				}
			}
			if(this.weight == 2) {
				if(tryOnMediumContainerNonBlocking(boat,route,rowstart, rowend, baystart,bayend) ) {
					System.out.print("Putting container "+displayID+" on top of a medium one, without blocking anything.\n");
					return true;
				}else {
					if(tryOnHeavyContainerNonBlocking(boat,route,rowstart, rowend, baystart,bayend)) {
						System.out.print("Putting container "+displayID+" on top of a heavy one, without blocking anything.\n");
						return true;
					}
				}
			}
			if(this.weight == 3) {
				if(tryOnHeavyContainerNonBlocking(boat,route,rowstart, rowend, baystart,bayend) ) {
					System.out.print("Putting container "+displayID+" on top of a heavy one, without blocking anything.\n");
					return true;
				}
			}
			return false;
		}
		
		public boolean putContainerOnSameDest(Boat boat, int route, int rowstart, int rowend, int baystart, int bayend) {		
			if(this.weight == 1) {
				if( tryOnLightContainerSameDest(boat,route,rowstart, rowend, baystart,bayend) ) {
					System.out.print("Putting container "+displayID+" on top of a light one, same destination.\n");
					return true;
				}else if(tryOnMediumContainerSameDest(boat,route, rowstart, rowend, baystart,bayend)){
					System.out.print("Putting container "+displayID+" on top of a medium one, same destination.\n");
					return true;
				}else {
					if(tryOnHeavyContainerSameDest(boat,route, rowstart, rowend, baystart,bayend)) {
						System.out.print("Putting container "+displayID+" on top of a heavy one, same destination.\n");
						return true;
					}
				}
			}
			if(this.weight == 2) {
				if(tryOnMediumContainerSameDest(boat,route, rowstart, rowend, baystart,bayend) ) {
					System.out.print("Putting container "+displayID+" on top of a medium one, same destination.\n");
					return true;
				}else {
					if(tryOnHeavyContainerSameDest(boat,route, rowstart, rowend, baystart,bayend)) {
						System.out.print("Putting container "+displayID+" on top of a heavy one, same destination.\n");
						return true;
					}
				}
			}
			if(this.weight == 3) {
				if(tryOnHeavyContainerSameDest(boat,route,rowstart, rowend, baystart,bayend) ) {
					System.out.print("Putting container "+displayID+" on top of a heavy one, same destination.\n");
					return true;
				}
			}
			return false;
		}
		

		
		public boolean tryOnLightContainerInOrder(Boat boat,int route, int rowstart, int rowend, int baystart, int bayend) {
			for(int i = 1; i< Boat.nrOfLayers;i++){
				  for(int j = rowstart; j< rowend;j++){
					  for(int k = baystart; k< bayend;k++) {
						  if(is20Feasible(boat,i,j,k) && containerBelowIsLightweight(boat, i,j,k) && boat.pileBelowIsInOrder(boat,route,i,j,k,getDest())) {
							  boat.set20footSpotOccupied(i,j,k, this.weight, getDest());
							  updateLocationOnBarge(i,j,k);
							  transported = true;
							  return true;
						  }
					  }
				  }
			}
		return false;
		}
		
		public boolean tryOnMediumContainerInOrder(Boat boat,int route, int rowstart, int rowend, int baystart, int bayend) {
			for(int i = 1; i< Boat.nrOfLayers;i++){
				  for(int j = rowstart; j< rowend;j++){
					  for(int k = baystart; k< bayend;k++) {
						  if(is20Feasible(boat,i,j,k) && containerBelowIsMediumweight(boat, i,j,k) && boat.pileBelowIsInOrder(boat, route,i,j,k,getDest())) {
							  boat.set20footSpotOccupied(i,j,k, this.weight, getDest());
							  updateLocationOnBarge(i,j,k);
							  transported = true;
							  return true;
						  }
					  }
				  }
			}
		return false;
		}
		
		public boolean tryOnHeavyContainerInOrder(Boat boat,int route, int rowstart, int rowend, int baystart, int bayend) {
			for(int i = 1; i< Boat.nrOfLayers;i++){
				  for(int j = rowstart; j< rowend;j++){
					  for(int k = baystart; k< bayend;k++) {
						  if(is20Feasible(boat,i,j,k) && boat.pileBelowIsInOrder(boat, route,i,j,k,getDest())) {
							  boat.set20footSpotOccupied(i,j,k, this.weight, getDest());
							  updateLocationOnBarge(i,j,k);
							  transported = true;
							  return true;
						  }
					  }
				  }
			}
		return false;
		}
		
		public boolean containerDoesNotBlock(Boat boat, int route, int i, int j, int k, int dest) {
			if(i>0) {
				int destBelow = boat.destStowage[i-1][j][k];
				int indexContainerBelow = boat.getProximityInRoute(route, destBelow);
				int ownIndex = boat.getProximityInRoute(route, dest);
				if(ownIndex <= indexContainerBelow) {
					return true;
				}
			}
			return false;
					
		}
		
		public boolean tryOnLightContainerNonBlocking(Boat boat,int route, int rowstart, int rowend, int baystart, int bayend) {
			for(int i = 1; i< Boat.nrOfLayers;i++){
				  for(int j = rowstart; j< rowend;j++){
					  for(int k = baystart; k< bayend;k++) {
						  if(is20Feasible(boat,i,j,k) && containerBelowIsLightweight(boat, i,j,k) && containerDoesNotBlock(boat,route,i,j,k,getDest())) {
							  boat.set20footSpotOccupied(i,j,k, this.weight, getDest());
							  updateLocationOnBarge(i,j,k);
							  transported = true;
							  return true;
						  }
					  }
				  }
			}
		return false;
		}
		
		public boolean tryOnMediumContainerNonBlocking(Boat boat,int route, int rowstart, int rowend, int baystart, int bayend) {
			for(int i = 1; i< Boat.nrOfLayers;i++){
				  for(int j = rowstart; j< rowend;j++){
					  for(int k = baystart; k< bayend;k++) {
						  if(is20Feasible(boat,i,j,k) && containerBelowIsMediumweight(boat, i,j,k) && containerDoesNotBlock(boat, route,i,j,k,getDest())) {
							  boat.set20footSpotOccupied(i,j,k, this.weight, getDest());
							  updateLocationOnBarge(i,j,k);
							  transported = true;
							  return true;
						  }
					  }
				  }
			}
		return false;
		}
		
		public boolean tryOnHeavyContainerNonBlocking(Boat boat,int route, int rowstart, int rowend, int baystart, int bayend) {
			for(int i = 1; i< Boat.nrOfLayers;i++){
				  for(int j = rowstart; j< rowend;j++){
					  for(int k = baystart; k< bayend;k++) {
						  if(is20Feasible(boat,i,j,k) && containerDoesNotBlock(boat, route,i,j,k,getDest())) {
							  boat.set20footSpotOccupied(i,j,k, this.weight, getDest());
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
						  boat.set20footSpotOccupied(0,j,k, this.weight, getDest());
						updateLocationOnBarge(0,j,k);
						transported = true;
						return true;
					}
				}
			}		
			return false;
		}
		
		public boolean tryOnLightContainerSameDest(Boat boat, int route, int rowstart, int rowend, int baystart, int bayend) {
			for(int i = 1; i< Boat.nrOfLayers;i++){
				  for(int j = rowstart; j< rowend;j++){
					  for(int k = baystart; k< bayend;k++) {
						  if(is20Feasible(boat,i,j,k) && containerBelowIsLightweight(boat, i,j,k) &&
								  containerBelowHasSameDestination(boat,i,j,k)&& boat.pileBelowIsInOrder(boat, route,i,j,k,getDest())) {
							  boat.set20footSpotOccupied(i,j,k, this.weight, getDest());
							  updateLocationOnBarge(i,j,k);
							  transported = true;
							  return true;
						  }
					  }
				  }
			}
		return false;
		}
		
		public boolean tryOnMediumContainerSameDest(Boat boat, int route, int rowstart, int rowend, int baystart, int bayend) {
			for(int i = 1; i< Boat.nrOfLayers;i++){
				  for(int j = rowstart; j< rowend;j++){
					  for(int k = baystart; k< bayend;k++) {
						  if(is20Feasible(boat,i,j,k) && containerBelowIsMediumweight(boat,i,j,k) &&
							  containerBelowHasSameDestination(boat,i,j,k) && boat.pileBelowIsInOrder(boat, route,i,j,k,getDest())) {
							  boat.set20footSpotOccupied(i,j,k, this.weight, getDest());
							  updateLocationOnBarge(i,j,k);
							  transported = true;
							  return true;
						  }
					  }
				  }
			}
		return false;
		}
		
		public boolean tryOnHeavyContainerSameDest(Boat boat, int route, int rowstart, int rowend, int baystart, int bayend) {
			for(int i = 1; i< Boat.nrOfLayers;i++){
				  for(int j = rowstart; j< rowend;j++){
					  for(int k = baystart; k< bayend;k++) {
						  if( is20Feasible(boat,i,j,k) && containerBelowHasSameDestination(boat,i,j,k)&& boat.pileBelowIsInOrder(boat, route,i,j,k,getDest())) {
							  boat.set20footSpotOccupied(i,j,k, this.weight, getDest());
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
			if(i>0) {
				int dest = getDest();
				int destBelow = boat.destStowage[i-1][j][k];
				if(dest == destBelow) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
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