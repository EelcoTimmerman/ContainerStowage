package main;

import containersAndBoat.Boat;
import containersAndBoat.Container;
import containersAndBoat.ContainerSet;
import containersAndBoat.ContainerType;
import stowage.CreateStowage;
import stowage.OverstowageCalculator;
import stowage.TerminalSet;

public class ALNS {
	
	public ALNS() {
		
	}
	
	public void alterSolution() {
		
	}
	
	
	
	public double calculateObjective(Boat boat) {
		double value =0;	
		 for(Container c:CreateStowage.fixedContainers) {
				if(c.export == true && c.isOnBarge &&c.type == ContainerType.TWENTY) {
					value++;
				}else if(c.export == true && c.isOnBarge &&c.type == ContainerType.FORTY) {
					value += 2;
				}
			}
			for(int r = 0;r<TerminalSet.nrOfRoutes;r++) {
				OverstowageCalculator Ocalc = new OverstowageCalculator(TerminalSet.terminals, boat, ContainerSet.containers, TerminalSet.routes);
				Ocalc.reportRoute(r);
				value += TerminalSet.routeProb[r]* Ocalc.addedValue();
				}
		 System.out.printf("The resulting objective for this initial stowage is: "+value+"\n");
		 return value;
	 }
}
