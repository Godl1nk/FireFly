package ac.firefly.util.math;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecMath {

	public static String decryptBase64(String strEncrypted) {
		String strData = "";

		try {
			byte[] decoded = Base64.getDecoder().decode(strEncrypted);
			strData = (new String(decoded, "UTF-8"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strData;
	}
	
	public static String encryptSha256(String strEncrypt) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(strEncrypt.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
	}

	public static String hashMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(input.getBytes());
			StringBuilder hexString = new StringBuilder();

			byte[] byteData = md.digest();

			for (byte aByteData : byteData) {
				String hex = Integer.toHexString(0xff & aByteData);
				if (hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}
	}
}
