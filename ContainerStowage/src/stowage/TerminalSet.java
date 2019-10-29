package stowage;
import java.util.ArrayList;
import java.util.List;
import stowage.Terminal;

public class TerminalSet {
	public static int nrOfTerminals = 2;
	public static List<Terminal> terminals = new ArrayList<>();
	
	public TerminalSet() {
		int id = 0;
		for(int i = 0; i<nrOfTerminals; i++) {
			Terminal terminal = new Terminal(id);
			id++;
			terminals.add(terminal);
		}	
	}
}
