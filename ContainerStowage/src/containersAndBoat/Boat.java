package containersAndBoat;

public class Boat {
//Hey this is a boat
	public int nrOfBays = 4;
	public int nrOfLayers = 3;
	public int nrOfRows = 3;
//TO DO:: we need the specifications of the boat
	public static int weightBoat;
	//create a variable that indicates the stowage, probably a 3D
	public int[][][] stowage = new int[nrOfLayers][nrOfRows][nrOfBays];
	
	public Boat() {
		showStowage();
	}
	
	public void showStowage() {
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
	}
	
	public int[][][] getStowage(){
		return stowage;
		
	}
	
	public void setLocationOccupied(int layer, int row, int bay) {
		stowage[layer][row][bay] = 1;
	}
	
	public void visitTerminal(int t) {
		//it needs to select the containers that are destined to this terminal, and then 
		//unload them, then select the import containers that are needed and load them.
	}
	
}
