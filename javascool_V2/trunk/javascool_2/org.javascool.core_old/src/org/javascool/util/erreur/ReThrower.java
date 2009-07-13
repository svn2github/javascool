package org.javascool.util.erreur;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ReThrower {


	/**
	 * To know the date's format.
	 */
	private static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


	/**
	 * Logs an exception in the console
	 * @param e the exception to log
	 */
	public static synchronized void log(Throwable e,String file,int nbLigne) {

		Translation translation = new Translation();

		printTime();

		//Recupere le nom de la classe de l'exception
		String s = e.getClass().getName();

		//Traduction de l'exception
		String tmp  = translation.getTraduction(s);
		if (tmp != null)
			s = tmp ;

		s = translation.getCommentaire(0)+" "+s ;

		/* si l'on garde �a le message d'erreur n'est pas entier mais si on ne le garde pas l'anglais apparait
		String[] arguments = e.getMessage().split(" ");

		//A ameliorer
		if (arguments.length != 0){
			s += ": '"+arguments[0]+"'\n";
		}*/
		if(e.getMessage()!=null)
			s+=": '"+e.getMessage()+"'\n";
		else s+="\n";


		s+= "\t    "+translation.getCommentaire(1)+" :\n";

		StackTraceElement[] trace = e.getStackTrace();


		//String file = "Perspective.java";

		boolean addMain = false;
		for(int i=0; i<trace.length-1 ;i++){
			StackTraceElement t= trace[i];
			if (file.compareToIgnoreCase(t.getFileName()) == 0){
				int numLigne = t.getLineNumber() - nbLigne;
				if(!addMain){
					String methodName = t.getMethodName();
					s+= "\t    "+"  "+translation.getCommentaire(2)+" : "+methodName+" , "+ 
					translation.getCommentaire(3)+" "+numLigne+""+System.getProperty("line.separator");
					if(methodName.equals("main")){
						addMain = true;
						break;
					}
				}
			}
		}

		final String log = s.toString();
		System.err.println(log);
		System.err.flush();
	}


	//
	// -- PRIVATE METHODS -----------------------------------------------
	//

	/**
	 * Prints the current time in the console.
	 */
	public static  void printTime(){
		System.err.print(dateFormat.format(new java.util.Date())+" => ");
	}

}
