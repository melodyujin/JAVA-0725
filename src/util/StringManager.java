package util;

public class StringManager {
	
	//문자열의 길이가 1일 때, 2자리로 표현하기
	//예) 7인 경우 07 
	public static String getNumString(int n) { //7
		String str=Integer.toString(n); // "7"
		
		if(str.length()<2) { //문자열 길이가 1이라면..
			str="0"+str;
		}
		return str;
	}
}
