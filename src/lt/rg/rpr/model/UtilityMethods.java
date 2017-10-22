package lt.rg.rpr.model;

public class UtilityMethods {
	static public String msToTime(int ms) {
		int ml = ms % 1000;
		int temp = ms / 1000; 
		int sc = temp % 60;
		int m = (temp / 60) % 60;
		int h = temp / 3600;
		
		return h+":"+addNero(m, false)+":"+addNero(sc, false)+":"+ addNero(ml, true);
	}
	
	private static String addNero(int x, boolean isMS) {
		String s;
		if(isMS) {
			if(x < 10) {
				s = "00"+x;
			}else if(x < 100){
				s = "0"+x;
			}else{
				s = ""+x;
			}
		}else {
			if(x < 10) {
				s = "0"+x;
			}else{
				s = ""+x;
			}
		}
		return s;
	}
	
	public static int timeToMS(String time) {
		int n = 0;
		
		String[] ta =  time.split(":",-1);
		
		for (int i = 0; i < ta.length; i++) {
			if(ta[i].equals("")) {
				ta[i] = "0";
			}
		}
		
		n += Integer.parseInt(ta[0]) * 3600000;
		n += Integer.parseInt(ta[1]) * 60000;
		n += Integer.parseInt(ta[2]) * 1000;
		n += Integer.parseInt(ta[3]);
		
		return n;
	}
}
