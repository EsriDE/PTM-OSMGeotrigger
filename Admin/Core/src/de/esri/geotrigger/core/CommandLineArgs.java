package de.esri.geotrigger.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Class used to parse the command line arguments.
 */
public class CommandLineArgs {
	public static final String HELP = "help";
	public static final String CREATE_TRIGGER = "createtrigger";
	public static final String CREATE_TRIGGER_FROM_SERVICE = "triggerfromservice";
	public static final String RUN_TRIGGER = "runtrigger";
	public static final String DELETE_TRIGGER = "deletetrigger";
	
	private String command;
	private Map<String, String> params;
	
	public CommandLineArgs(String[] args){
		if (args != null && args.length > 0){
			if (args[0].startsWith("-") || args[0].startsWith("/")){
				String command = args[0].substring(1);
				switch(command){
				case HELP: 
					this.command = HELP;
					break;
				case CREATE_TRIGGER:
					this.command = CREATE_TRIGGER;
					break;
				case CREATE_TRIGGER_FROM_SERVICE:
					this.command = CREATE_TRIGGER_FROM_SERVICE;
					break;					
				case RUN_TRIGGER:
					this.command = RUN_TRIGGER;
					break;
				case DELETE_TRIGGER:
					this.command = DELETE_TRIGGER;
					break;
				}
				params = new HashMap<String, String>();
				for(int i = 1; i < args.length; i++){
					if (args[i].startsWith("-") || args[i].startsWith("/")){
						String param = args[i].substring(1);
						if(args.length > (i + 1)){
							if (!args[i + 1].startsWith("-") && !args[i + 1].startsWith("/")){
								String value = args[i+1];
								params.put(param, value);								
							}
						}
					}
				}
			}			
		}
	}
	
	/**
	 * Get the command.
	 * @return The command.
	 */
	public String getCommand(){
		return command;
	}
	
	/**
	 * Get the command line parameters.
	 * @return The command line parameters.
	 */
	public Map<String, String> getParameters(){
		return params;
	}
}
