package stowage;
import java.util.Random;


import java.util.ArrayList;
import java.util.List;
import stowage.Terminal;

public class TerminalSet {
	public static int nrOfTerminals = 4; // this includes the dryport
	public static int nrOfRoutes = 3;
	public static int[] firstIndices = new int[nrOfRoutes];
	public static int[][] routes = new int[nrOfRoutes][nrOfTerminals-1];
	public static double[] routeProb= new double [nrOfRoutes];
	public static List<Terminal> terminals = new ArrayList<>();
	public static double[] ranks = new double[nrOfTerminals-1];
	
	public TerminalSet() {
		int id = 0;
		decideRoutes();
		decideRouteProbabilities();
		calcRank();
		for(int i = 0; i<nrOfTerminals; i++) {
			Terminal terminal = new Terminal(id);
			id++;
			terminals.add(terminal);
		}	
	}
	
	public void decideRoutes() {		
		if(nrOfRoutes> nrOfTerminals) {
			System.out.print("Error, creation of this amount of routes not possible, there are not enough terminals..");
		}
		Random rand = new Random(1000);
		for(int i = 0; i<nrOfRoutes;i++) {
			int var = i+1;
			System.out.printf("Route "+var+": \n");
			System.out.print("0 ");
			System.out.print(" - ");
			ArrayList<Integer> termlist = new ArrayList<>();
			for(int j=1;j<nrOfTerminals;j++) {
				int newTerm = j;
				termlist.add(newTerm);
			}		
				for(int k=0; k<nrOfTerminals - 1;k++) {
					if(k == 0) {
						int index = rand.nextInt(termlist.size());
						int ui = 0;
						while(alreadyWasFirstIndex(index) && ui <1000) {
							index = rand.nextInt(termlist.size());
							if(alreadyWasFirstIndex(index) == false) {
								break;
							}
							ui++;
						}
						firstIndices[i] =index;
						int nextTerm = termlist.get(index);
						routes[i][k] = nextTerm;
						termlist.remove(index);
						System.out.print(routes[i][k]);
						System.out.print(" - ");
					}else {
						int index = rand.nextInt(termlist.size());
						int nextTerm = termlist.get(index);
						routes[i][k] = nextTerm;
						termlist.remove(index);
						System.out.print(routes[i][k]);
						System.out.print(" - ");
					}				
				}
				System.out.print(" 0\n");			
		}
	}
	
	
	public boolean alreadyWasFirstIndex(int n) {
		for(int i=0;i<nrOfRoutes;i++) {
			if(n == firstIndices[i]) {
				return true;
			}
		}
		return false;
	}
	
	public static void decideRouteProbabilities() {
		int i =0;
		while(i< nrOfRoutes) {
			double index = (100/nrOfRoutes);
			routeProb[i] = index/100;
			i++;
		}
		for(int j = 0; j< nrOfRoutes;j++) {
			System.out.printf("route"+j+" prob: "+ routeProb[j] +"\n");

		}
	}
	
	public static void calcRank() {
		for(int g=1; g<TerminalSet.nrOfTerminals;g++) {
			double rank = 0;
			for(int h =0;h<TerminalSet.nrOfRoutes;h++) {
				for(int i=0;i<TerminalSet.nrOfTerminals-1;i++)
				if(g == routes[h][i]) {
					rank += i*TerminalSet.routeProb[h];
				}
			}
			ranks[g-1] = rank;
		}
		for(int i=0;i<nrOfTerminals-1;i++) {
			int j = i+1;
			System.out.printf("Rank terminal "+j+" equals: "+ ranks[i] +"\n");
		}
	}
	
}
