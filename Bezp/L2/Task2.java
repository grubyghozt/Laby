import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Scanner;

public class Task2 {

    String suffix;
    private byte[] iv;
    private int missingKeyChars;

    private static int keyLength = 64;

    class bruteForceThread extends Thread {

        private long keyStart;
        private long keyEnd;
        private byte[] cipherText;

        public bruteForceThread(long start, long end, byte[] cipherText) {
            this.keyStart = start;
            this.keyEnd = end;
            this.cipherText = new byte[cipherText.length];
            System.arraycopy(cipherText, 0, this.cipherText, 0, cipherText.length);
        }

        @Override
        public void run() {
            for(long i = keyStart; i <= keyEnd; i++) {
                try {
                    String keyString = longToHexString(i) + suffix;
                    byte[] keyByte = DatatypeConverter.parseHexBinary(keyString);
                    validate(decrypt(cipherText, keyByte), keyString);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    public Task2(String suffix, byte[] iv) {
        this.suffix = suffix;
        this.iv = iv;
        this.missingKeyChars = keyLength - suffix.length();
    }

    public static void main(String[] args) {
        try {

            Scanner read = new Scanner(System.in);
            String key = read.nextLine();
            byte[] iv = DatatypeConverter.parseHexBinary(read.nextLine());
            String cipherText = read.nextLine();
            read.close();
            Task2 controller = new Task2(key, iv);
            controller.prepareThreads(controller.binaryToBytes(cipherText), Integer.parseInt(args[0]));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public byte[] binaryToBytes(String binary) {
        String[] bytesSplit = binary.split(" ");
        byte[] bytes = new byte[bytesSplit.length];
        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(bytesSplit[i], 2);
        }
        return bytes;
    }

    public void prepareThreads(byte[] cipherTextBytes, int numOfThreads) throws Exception {
        long maxKeyValue = computeMaxKeyValue();
        long step = maxKeyValue / numOfThreads;
        for(long i = 0; i <= maxKeyValue; ) {
            long next = i + step;
            if(next > maxKeyValue) {
                next = maxKeyValue;
            }
            bruteForceThread bruteForceThread = new bruteForceThread(i, next, cipherTextBytes);
            bruteForceThread.start();
            i = next + 1;
        }
    }
    public Long computeMaxKeyValue() {
        StringBuilder maxKeyValue = new StringBuilder("");
        for (int i = 0; i < missingKeyChars; i++) {
            maxKeyValue.append("f");
        }
        return Long.parseLong(maxKeyValue.toString(), 16);
    }
    public String longToHexString(Long number) {
        StringBuilder str = new StringBuilder(Long.toHexString(number));
        int len = str.length();
        while(len < missingKeyChars) {
            str.insert(0, 0);
            len++;
        }
        return str.toString();
    }
    public void validate(String str, String key) {
        if(str.matches("\\A[\\p{ASCII}ąężźćś]*\\z")) {
            System.out.println("wiadomosc" + str + " klucz: " + key);
        }
    }
    public String decrypt(byte[] cipherTextBytes, byte[] possibleKey) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(possibleKey, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(cipherTextBytes);
        return new String(decryptedBytes);
    }
}
