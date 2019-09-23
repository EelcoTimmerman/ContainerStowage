package containersAndBoat;

public class Container {
		public int id;
		public ContainerType type;
		public int weight;
		public int destination;
		public int xLoc;
		public int yLoc;
		public int zLoc;

		
		public Container(int id) {
			this.id = id;
			xLoc = -1;
			yLoc = -1;
			zLoc = -1;
		}
		public void talk() {
			int rew = id+1;
			System.out.printf("Hey I am container nr" + rew);
		}
		
		public void setType(ContainerType newtype){
			this.type = newtype;
		}
		
		public void setWeight(int newweight) {
			this.weight = newweight;
		}

}