package org.unice.javascool.orphy;

public class OrphyProvider {
	
	private static Orphy orphy;
	
	public OrphyProvider(){
		orphy = null;
	}

	public static Orphy getOrphy() {
		return orphy;
	}

	public static void setOrphy(Orphy newOrphy) {
		orphy = newOrphy;
	}
	
	public static void deleteOrphy(){
		orphy = null;
	}

}
