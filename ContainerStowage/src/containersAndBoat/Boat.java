package containersAndBoat;
import java.util.List;

import stowage.CreateStowage;
import stowage.Terminal;

public class Boat {
	public int nrOfBays = 4;
	public int nrOfLayers = 4;
	public int nrOfRows = 1;
//TO DO:: we need the specifications of the boat
	public static int weightBoat;
	public int[][][] stowage = new int[nrOfLayers][nrOfRows][nrOfBays];
	
	public Boat() {
		System.out.print("Initial stowage: \n");
		showStowage();
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
	}
	
	public int[][][] getStowage(){
		return stowage;
		
	}
	
	public void set20footSpotOccupied(int layer, int row, int bay) {
		stowage[layer][row][bay] = 1;
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
	
	public void visitTerminal(Terminal terminal, Boat boat) {
		CreateStowage.removeExport(boat, terminal);
		CreateStowage.loadImport(boat, terminal);
	}
	
}
