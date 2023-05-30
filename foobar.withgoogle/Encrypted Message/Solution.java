import java.util.Base64;

public class Solution {
    public static void main(String args[]) {
        String encrypted = "AkgGBBYCCxdBFllVVVYSEwsFRhZVT1ISGg0CAVNWDApSUU9BSQFBRRwKGBQRRkJEFVQfCRoDARJJ RAgRXgYbEgcECg1QXRxIWVFSAA0MW1QPChgUGxVJRAgRXhobHRoCBQFWFlVPUgMUAwwNRkJeT09R UhIPAlcWVU9SFxoOSUQIEV4YHB9URhM=";
        final byte[] ct = encrypted.replaceAll("\\s","").getBytes();
	final byte[] key = "youquand21".getBytes();
		
        byte[] decoded = Base64.getDecoder().decode(ct);
        byte[] pt = new byte[decoded.length];
        for (int i = 0; i < decoded.length; i++) {
            pt[i] = (byte) (key[i % key.length] ^ decoded[i]);
        }
        System.out.println(new String(pt));
    }
}
