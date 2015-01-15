package fr.areaX.smartcard;

public class SmartCardHelper {
	
	public static byte getRandomByte(){
		int i = (int) (Math.random()*1000) % 50;
		byte b = (byte)i;
		return b;
	}
	
	public static byte[] getNRandomBytes(int n){
		byte[] b = new byte[n];
		for (int i=0; i<n; i++){
			b[i] = getRandomByte();
		}
		return b;
	}
	
	public static byte[] fromIntToByte(int integer){
		String s = String.valueOf(integer);
		byte[] b = new byte[s.length()];
		for (int i=0; i<s.length(); i++	){
			b[i] = (byte) s.charAt(i);
		}
		return b;
	}
	
	public static int fromByteToInt(byte[] b){
		String str = "";
		for(byte by: b){
			str += String.valueOf((char)by);
		}
		return Integer.valueOf(str);
	}
	
 }
