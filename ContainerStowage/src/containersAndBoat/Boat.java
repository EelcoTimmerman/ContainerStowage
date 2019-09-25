package containersAndBoat;
import java.util.List;

import stowage.Terminal;

public class Boat {
//Hey this is a boat
	public int nrOfBays = 5;
	public int nrOfLayers = 2;
	public int nrOfRows = 2;
//TO DO:: we need the specifications of the boat
	public static int weightBoat;
	//create a variable that indicates the stowage, probably a 3D
	public int[][][] stowage = new int[nrOfLayers][nrOfRows][nrOfBays];
	
	public Boat() {
		System.out.print("Initial stowage: \n");
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
	
	public void setLocationEmpty(int layer, int row, int bay) {
		stowage[layer][row][bay] = 0;
	}	
	
	public void visitTerminal(Terminal terminal, Boat boat) {
		for(Container c: ContainerSet.containers) {
			if(c.destination == terminal && c.isOnBarge && c.export) {
				terminal.unloadExport(c);
				c.removeFromBarge(boat);
			}if(c.destination == terminal && c.isOnBarge == false && c.export == false){
				terminal.loadImport(c);
				c.findFeasibleLocation(boat);
			}
		}
		showStowage();
	}
	
}
