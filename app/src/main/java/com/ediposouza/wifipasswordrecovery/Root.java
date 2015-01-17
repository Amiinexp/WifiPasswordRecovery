package com.ediposouza.wifipasswordrecovery;

import java.util.ArrayList;

public class Root extends rootBase {

	ArrayList<String> comando;
	
	public Root(){
		comando = new ArrayList<String>();
	}
	
	@Override
	protected ArrayList<String> getCommandsToExecute() {
		return comando;
	}

	public ArrayList<String> exec(String d){		
		comando.clear();
		comando.add(d);
		comando.add("exit\n");
		return executeWithRes();
	}
	
}
