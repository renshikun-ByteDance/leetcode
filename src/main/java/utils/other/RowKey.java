package utils.other;

import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.util.Arrays;

public class RowKey {


    public static void main(String[] args) throws InterruptedException {
        System.out.println(DigestUtils.md5Hex("12022-06-16 12:26:56"));
        System.out.println(DigestUtils.md5Hex("22022-06-16 12:26:56"));
        System.out.println(DigestUtils.md5Hex("32022-06-16 12:26:56"));
        System.out.println(DigestUtils.md5Hex("42022-06-16 12:26:56"));
        System.out.println(DigestUtils.md5Hex("52022-06-16 12:26:56"));
        System.out.println(DigestUtils.md5Hex("62022-06-16 12:26:56"));
        System.out.println(DigestUtils.md5Hex("72022-06-16 12:26:56"));
        System.out.println(DigestUtils.md5Hex("82022-06-16 12:26:56"));
        System.out.println(DigestUtils.md5Hex("92022-06-16 12:26:56"));
        System.out.println(DigestUtils.md5Hex("02022-06-16 12:26:56"));
        System.out.println("参考");
        System.out.println(DigestUtils.md5Hex("2"));
        System.out.println(DigestUtils.md5Hex("3"));


        System.out.println(DigestUtils.md2Hex("1"));
        System.out.println(DigestUtils.md2Hex("2"));
        System.out.println(DigestUtils.md2Hex("3"));

        System.out.println(DigestUtils.sha256Hex("1"));
        System.out.println(DigestUtils.sha256Hex("2"));
        System.out.println(DigestUtils.sha256Hex("3"));

        System.out.println(DigestUtils.sha1Hex("1"));
        System.out.println(DigestUtils.sha1Hex("2"));
        System.out.println(DigestUtils.sha1Hex("3"));

        System.out.println(DigestUtils.md2Hex("2"));


        System.out.println(Arrays.toString(DigestUtils.md5("1")));
//        System.out.println(Arrays.toString(DigestUtils.md5("2")));
//        System.out.println(Arrays.toString(DigestUtils.md5("3")));

        System.out.println(String.format("%0" + 16 + "d", Math.abs("1".hashCode())));
        System.out.println(String.format("%0" + 16 + "d", Math.abs("2".hashCode())));
        System.out.println(String.format("%0" + 16 + "d", Math.abs("3".hashCode())));

        System.out.println(Arrays.toString(DigestUtils.md2("1")));

        System.out.println(Arrays.deepToString(getHexSplits("0000", "ffff", 20)));

        String aaaa = "123456789";
        System.out.println(aaaa.getClass());

    }


    public static byte[][] getHexSplits(String startKey, String endKey, int numRegions) {
        byte[][] splits = new byte[numRegions - 1][];
        BigInteger lowestKey = new BigInteger(startKey, 16);
        BigInteger highestKey = new BigInteger(endKey, 16);
        BigInteger range = highestKey.subtract(lowestKey);
        BigInteger regionIncrement = range.divide(BigInteger.valueOf(numRegions));
        lowestKey = lowestKey.add(regionIncrement);
        for (int i = 0; i < numRegions - 1; i++) {
            BigInteger key = lowestKey.add(regionIncrement.multiply(BigInteger.valueOf(i)));
            byte[] b = String.format("%016x", key).getBytes();
            splits[i] = b;
        }
        return splits;
    }

}
