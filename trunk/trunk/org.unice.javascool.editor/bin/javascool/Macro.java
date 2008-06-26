package javascool;
public class Macro {
public static void println(String s){
	System.out.println(s);
}
public static void print(String s){
	System.out.print(s);
}
public static void dort(int time){
	try{Thread.currentThread().sleep(time);}catch(InterruptedException e){e.printStackTrace();}
}
}
