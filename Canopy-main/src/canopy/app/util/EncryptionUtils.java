package canopy.app.util;

public class EncryptionUtils {
	
	public static String fromKey(String key) {	
		StringBuilder builder = new StringBuilder();
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		int counter = 0;
		for (char c : key.toCharArray()) {
			int i = characters.indexOf(c);
			int size = characters.length();
			counter +=1;
			int index = i+counter;
			if (index>=size){
				index-=size;
			}			
			builder.append(characters.charAt(index));
		}
		String keytwo = builder.toString();
		return keytwo;		
	}
}
