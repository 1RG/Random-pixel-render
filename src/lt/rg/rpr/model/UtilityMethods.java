package lt.rg.rpr.model;

public class UtilityMethods {
	static public String sToTime(int s) {
		int sc = s % 60;
		int m = (s / 60) % 60;
		int h = s / 3600;
		
		return h+":"+addNero(m)+":"+addNero(sc);
	}
	
	private static String addNero(int x) {
		String s;
		
		if(x < 10) {
			s = "0"+x;
		}else{
			s = ""+x;
		}
		
		return s;
	}
	
	public static int timeToS(String time) {
		int n = 0;
		
		String[] ta =  time.split(":",-1);
		
		for (int i = 0; i < ta.length; i++) {
			if(ta[i].equals("")) {
				ta[i] = "0";
			}
		}
		
		n += Integer.parseInt(ta[0]) * 3600;
		n += Integer.parseInt(ta[1]) * 60;
		n += Integer.parseInt(ta[2]);
		
		return n;
	}
}
