import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.util.Scanner;

public class Task2 {

    public static String suffix;
    public static byte[] iv;
    public static int missingKeyChars;
    public static byte[] cipherText;
    public static int keyLength = 64;

    public static void main(String[] args) {
        try {
            Security.setProperty("crypto.policy", "unlimited");
            Scanner read = new Scanner(System.in);
            suffix = read.nextLine();
            iv = hexToBinary.parseHexBinary(read.nextLine());
            String cipherTextString = read.nextLine();
            read.close();
            missingKeyChars = keyLength - suffix.length();
            cipherText = binaryToBytes(cipherTextString);
            prepareThreads(Integer.parseInt(args[0]));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static byte[] binaryToBytes(String binary) {
        String[] bytesSplit = binary.split(" ");
        byte[] bytes = new byte[bytesSplit.length];
        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(bytesSplit[i], 2);
        }
        return bytes;
    }

    public static void prepareThreads(int numOfThreads) throws Exception {
        long maxKeyValue = computeMaxKeyValue();
        long step = maxKeyValue / numOfThreads;
        for(long i = 0; i <= maxKeyValue; ) {
            long next = i + step;
            if(next > maxKeyValue) {
                next = maxKeyValue;
            }
            bruteForceThread bruteForceThread = new bruteForceThread(i, next);
            bruteForceThread.start();
            i = next + 1;
        }
    }
    public static Long computeMaxKeyValue() {
        StringBuilder maxKeyValue = new StringBuilder("");
        for (int i = 0; i < missingKeyChars; i++) {
            maxKeyValue.append("f");
        }
        return Long.parseLong(maxKeyValue.toString(), 16);
    }
    public static String longToHexString(Long number) {
        StringBuilder str = new StringBuilder(Long.toHexString(number));
        int len = str.length();
        while(len < missingKeyChars) {
            str.insert(0, 0);
            len++;
        }
        return str.toString();
    }
    public static void validate(String str, String key) {
        if(str.matches("\\A[\\p{ASCII}ąężźćś]*\\z")) {
            System.out.println("wiadomosc: " + str + " klucz: " + key);
        }
    }
    public static String decrypt(byte[] cipherTextBytes, byte[] possibleKey) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(possibleKey, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(cipherTextBytes);
        return new String(decryptedBytes);
    }
}
