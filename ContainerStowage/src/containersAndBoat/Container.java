package containersAndBoat;

public class Container {
		public int id;
		public ContainerType type;
		public int weight;
		public int destination;
		
		public Container(int id) {
			this.id = id;
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