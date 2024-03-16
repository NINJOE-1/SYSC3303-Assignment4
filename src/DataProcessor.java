import java.nio.charset.StandardCharsets;

/**
 * The type Data processor.
 */
public class DataProcessor {
    /**
     * Process request string.
     *
     * @param data the data
     * @return the string
     */
    public static String processRequest(byte[] data) {
        String strData = "";
        for (int i = 0; i < data.length; i++) {
            if ((i >= 3 && i < 11) || (i >= 12))
                strData += new String(new byte[]{data[i]}, StandardCharsets.UTF_8);
            else {
                if (data[i] == 0)
                    strData += " 0 ";
                else
                    strData += Byte.toString(data[i]);
            }
        }
        return strData;
    }

    /**
     * Process response string.
     *
     * @param data the data
     * @return the string
     */
    public static String processResponse(byte[] data) {
        String resData = "";
        for (int i = 0; i < data.length; i++) {
            resData += Byte.toString(data[i]);
            resData += " ";
        }
        return resData;
    }

    /**
     * Inter response string.
     *
     * @param data the data
     * @return the string
     */
    public static String interResponse(byte[] data) {
        return new String(data, StandardCharsets.UTF_8);
    }
}
