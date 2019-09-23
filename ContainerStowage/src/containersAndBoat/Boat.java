package containersAndBoat;

public class Boat {
//Hey this is a boat
	public static int nrOfBays = 4;
	public static int nrOfLayers = 3;
	public static int nrOfRows = 2;
//TO DO:: we need the specifications of the boat
	public static int weightBoat;
	//create a variable that indicates the stowage, probably a 3D
	public int[][][] stowage;
	
	public Boat() {
		stowage[0][0][0] = 0;
		System.out.print(stowage[0][0][0]);
		/*
		 * for(int i = 0; i<nrOfLayers;i++) { for(int j = 0; j<nrOfRows;j++) { for(int k
		 * = 0; k<nrOfBays;k++) { stowage[i][j][k] = 0;
		 * System.out.print(stowage[i][j][k]); } } }
		 */
	}
	
	
}
