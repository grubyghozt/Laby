//import javax.xml.bind.DatatypeConverter;

public class bruteForceThread extends Thread {

    private long keyStart;
    private long keyEnd;

    public bruteForceThread(long start, long end) {
        this.keyStart = start;
        this.keyEnd = end;
    }

    @Override
    public void run() {
        for(long i = keyStart; i <= keyEnd; i++) {
            try {
                String keyString = Task2.longToHexString(i) + Task2.suffix;
                byte[] keyByte = hexToBinary.parseHexBinary(keyString);
                Task2.validate(Task2.decrypt(Task2.cipherText, keyByte), keyString);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
