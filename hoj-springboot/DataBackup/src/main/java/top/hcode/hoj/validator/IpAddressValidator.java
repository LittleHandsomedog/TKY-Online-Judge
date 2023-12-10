package top.hcode.hoj.validator;


import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * IP 地址验证器，用于判断 IP 地址是否有效、是否在指定范围内。
 *
 * <p>该类通过正则表达式和 InetAddress 类来验证和比较 IP 地址。</p>
 *
 * <p>注意：该类是一个 Spring 组件，用于在 Spring 容器中进行管理。</p>
 *
 * @author Handsomedog
 */

@Component
public class IpAddressValidator {

    private static final String IP_ADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    private static final Pattern pattern = Pattern.compile(IP_ADDRESS_PATTERN);

    public boolean isEmpty(String ipAddressArr){
        if (ipAddressArr == null || ipAddressArr.trim().isEmpty() || ipAddressArr.equals(",")) {
            return true;
        }
        String[] arr = ipAddressArr.split(";");
        for (String paris : arr) {
            String[] pari = paris.split(",");
            if(pari.length<=1 || pari[0].isEmpty()){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断给定的 IP 地址字符串是否有效。
     *
     * @param ipAddressArr 包含 IP 地址范围的字符串，多个范围用分号隔开，每个范围用逗号隔开。
     * @return 如果所有范围内的 IP 地址都有效，则返回 true；否则返回 false。
     */
    public boolean isValidIpAddress(String ipAddressArr) {
        String[] arr = ipAddressArr.split(";");
        for (String paris : arr) {
            String[] pari = paris.split(",");
            String startIp = pari[0];
            String endIp = pari[1];
            if (!pattern.matcher(startIp).matches()) {
                return false;
            }
            if (!pattern.matcher(endIp).matches()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 比较给定的 IP 地址范围是否有效。
     *
     * @param ipAddressArr 包含 IP 地址范围的字符串，多个范围用分号隔开，每个范围用逗号隔开。
     * @return 如果所有范围内的起始 IP 地址小于或等于结束 IP 地址，则返回 true；否则返回 false。
     */
    public boolean compareIpAddresses(String ipAddressArr) {
        try {
            String[] arr = ipAddressArr.split(";");
            for (String paris : arr) {
                String[] pari = paris.split(",");
                String startIp = pari[0];
                String endIp = pari[1];
                InetAddress start = InetAddress.getByName(startIp);
                InetAddress end = InetAddress.getByName(endIp);
                long startLong = ipToLong(start);
                long endLong = ipToLong(end);
                if (startLong > endLong) {
                    return false;
                }
            }
            return true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 判断给定的 IP 地址是否在指定的 IP 范围内。
     *
     * @param ipAddress    要判断的 IP 地址。
     * @param ipAddressArr 包含 IP 地址范围的字符串，多个范围用分号隔开，每个范围用逗号隔开。
     * @return 如果 IP 地址在任何范围内，则返回 true；否则返回 false。
     */
    public boolean isIpInRange(String ipAddress, String ipAddressArr) {
        try {
            if (ipAddressArr == null || ipAddressArr.trim().isEmpty()) {
                return false;
            }
            String startIp, endIp;
            String[] arr = ipAddressArr.split(";");
            for (String paris : arr) {
                String[] pari = paris.split(",");
                startIp = pari[0];
                endIp = pari[1];
                InetAddress start = InetAddress.getByName(startIp);
                InetAddress end = InetAddress.getByName(endIp);
                InetAddress target = InetAddress.getByName(ipAddress);
                long startLong = ipToLong(start);
                long endLong = ipToLong(end);
                long targetLong = ipToLong(target);
                if (targetLong >= startLong && targetLong <= endLong) {
                    return true;
                }
            }
            return false;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
    }

    private long ipToLong(InetAddress ipAddress) {
        byte[] octets = ipAddress.getAddress();
        long result = 0;
        for (byte octet : octets) {
            result <<= 8;
            result |= octet & 0xFF;
        }
        return result;
    }
}
