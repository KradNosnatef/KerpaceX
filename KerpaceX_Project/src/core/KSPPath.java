package core;
public class KSPPath {
	public static String kspPath;
	public static void setPath(String[] args){
		kspPath="";
		for(int i=0;i<args.length-1;i++)kspPath+=args[i]+" ";
		kspPath+=args[args.length-1]+"\\";
	}
}
