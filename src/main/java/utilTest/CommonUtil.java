package utilTest;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:公共方法
 * @version:v1.0
 * @author:congge
 * @createDate:2014-6-9下午6:40:28
 */
public class CommonUtil {

    public static Currency currency = Currency.getInstance(Locale.getDefault());

    /**
     * 判断变量是否为空
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        if (null == s || "".equals(s) || "".equals(s.trim()) || "null".equalsIgnoreCase(s)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 使用率计算
     *
     * @return
     */
    public static String fromUsage(long free, long total) {
        Double d = new BigDecimal(free * 100 / total).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        return String.valueOf(d);
    }

    /**
     * 返回当前时间 格式:yyyy-MM-dd hh:mm:ss
     *
     * @return String
     */
    public static String fromDateH() {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format1.format(new Date());
    }

    /**
     * 返回当前时间 格式:yyyy-MM-dd
     *
     * @return String
     */
    public static String fromDateY() {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        return format1.format(new Date());
    }

    /**
     * 用来去掉List中空值和相同项的。
     *
     * @param list
     * @return
     */
    public static List<String> removeSameItem(List<String> list) {
        List<String> difList = new ArrayList<String>();
        for (String t : list) {
            if (t != null && !difList.contains(t)) {
                difList.add(t);
            }
        }
        return difList;
    }

    /**
     * 返回用户的IP地址
     *
     * @param request
     * @return
     */

    /**
     * 传入原图名称，，获得一个以时间格式的新名称
     *
     * @param fileName 原图名称
     * @return
     */
    public static String generateFileName(String fileName) {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String formatDate = format.format(new Date());
        int random = new Random().nextInt(10000);
        int position = fileName.lastIndexOf(".");
        String extension = fileName.substring(position);
        return formatDate + random + extension;
    }

    /**
     * 取得html网页内容 UTF8编码
     *
     * @param urlStr 网络地址
     * @return String
     */
    public static String getInputHtmlUTF8(String urlStr) {
        URL url = null;
        try {
            url = new URL(urlStr);
            HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();

            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setConnectTimeout(5 * 1000);
            httpsURLConnection.connect();
            if (httpsURLConnection.getResponseCode() == 200) {
                // 通过输入流获取网络图片
                InputStream inputStream = httpsURLConnection.getInputStream();
                String data = readHtml(inputStream, "UTF-8");
                inputStream.close();
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;

    }

    /**
     * @param inputStream
     * @param uncode      编码 GBK 或 UTF-8
     * @return
     * @throws Exception
     */
    public static String readHtml(InputStream inputStream, String uncode) throws Exception {
        InputStreamReader input = new InputStreamReader(inputStream, uncode);
        BufferedReader bufReader = new BufferedReader(input);
        String line = "";
        StringBuilder contentBuf = new StringBuilder();
        while ((line = bufReader.readLine()) != null) {
            contentBuf.append(line);
        }
        return contentBuf.toString();
    }

    /**
     * @return 返回资源的二进制数据 @
     */
    public static byte[] readInputStream(InputStream inputStream) {

        // 定义一个输出流向内存输出数据
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 定义一个缓冲区
        byte[] buffer = new byte[1024];
        // 读取数据长度
        int len;
        // 当取得完数据后会返回一个-1
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                // 把缓冲区的数据 写到输出流里面
                byteArrayOutputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 得到数据后返回
        return byteArrayOutputStream.toByteArray();

    }

    /**
     * 检查bean中是否包含有效数据
     *
     * @param o
     * @return
     */
    public static boolean isValidBean(Object o) {
        boolean res = false;
        if (null != o) {
            Field[] fields = o.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (hasGetterMethod(o.getClass(), fields[i].getName())) {
                    Object value = getFieldValueByName(o.getClass(), fields[i].getName(), o);
                    if (value != null) {
                        res = true;
                        break;
                    }
                }
            }
        }
        return res;
    }

    /**
     * 检查bean中是否包含有效数据
     *
     * @param o
     * @param ignores 忽视校验的属性组
     * @return
     */
    public static boolean isValidBean(Object o, String[] ignores) {
        boolean res = false;
        if (null != o) {
            Field[] fields = o.getClass().getDeclaredFields();
            for (Field feild : fields) {
                boolean notContain = true;
                for (String str : ignores) {
                    if (str.equals(feild.getName())) {
                        notContain = false;
                        break;
                    }
                }
                if (notContain && hasGetterMethod(o.getClass(), feild.getName())) {
                    Object value = getFieldValueByName(o.getClass(), feild.getName(), o);
                    if (value != null) {
                        res = true;
                        break;
                    }
                }
            }
        }
        return res;
    }

    /**
     * 将bean转换成字符串格式，包括父类属性
     *
     * @param o
     * @return
     */
    public static String getBeanToString(Object o) {
        return o.getClass().getSimpleName() + ":" + CommonUtil.getMapFromBean(o).toString();
    }

    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     */
    public static Map<String, Object> getMapFromBean(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        Map<String, Object> infoMap = new HashMap<String, Object>(16);
        ;
        for (int i = 0; i < fields.length; i++) {
            if (hasGetterMethod(o.getClass(), fields[i].getName())) {
                infoMap.put(fields[i].getName(), getFieldValueByName(o.getClass(), fields[i].getName(), o));
            }
        }
        getMapFromBeanParent(o, o.getClass().getSuperclass(), infoMap);
        return infoMap;
    }

    public static Map<String, String> getStringMapFromBean(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        Map<String, String> infoMap = new HashMap<String, String>(16);
        ;
        for (int i = 0; i < fields.length; i++) {
            if (hasGetterMethod(o.getClass(), fields[i].getName())) {
                Object svalue = getFieldValueByName(o.getClass(), fields[i].getName(), o);
                String filedValue = svalue == null ? "" : String.valueOf(svalue);
                infoMap.put(fields[i].getName(), filedValue);
            }
        }
        getStringMapFromBeanParent(o, o.getClass().getSuperclass(), infoMap);
        return infoMap;
    }

    public static void getMapFromBeanParent(Object o, Class<?> clazz, Map<String, Object> infoMap) {
        if (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (hasGetterMethod(clazz, fields[i].getName())) {
                    infoMap.put(fields[i].getName(), getFieldValueByName(clazz, fields[i].getName(), o));
                }
            }
            getMapFromBeanParent(o, clazz.getSuperclass(), infoMap);
        }
    }

    public static void getStringMapFromBeanParent(Object o, Class<?> clazz, Map<String, String> infoMap) {
        if (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (hasGetterMethod(clazz, fields[i].getName())) {
                    Object svalue = getFieldValueByName(clazz, fields[i].getName(), o);
                    String filedValue = svalue == null ? "" : String.valueOf(svalue);
                    infoMap.put(fields[i].getName(), filedValue);
                }
            }
            getStringMapFromBeanParent(o, clazz.getSuperclass(), infoMap);
        }
    }

    /**
     * 根据属性名获取属性值
     */
    private static Object getFieldValueByName(Class<? extends Object> clazz, String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = clazz.getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean hasGetterMethod(Class<? extends Object> clazz, String fieldName) {
        boolean res = false;
        Method[] ms = clazz.getMethods();
        for (Method m : ms) {
            if (m.getName().equalsIgnoreCase("get" + fieldName)) {
                res = true;
                break;
            }
        }
        return res;
    }

    /**
     * 主要用来判断转为字符串类型后，对应的值是否相同; 注：如果类型不同判断可能有问题，如：9.0和9->true
     *
     * @param oldStr
     * @param newStr
     * @return
     */




    public static boolean isNumber(String number) {
        try {
            Double.parseDouble(number);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isIpv4(String ipAddress) {

        String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();

    }

    public static Double getDoubleWithScale(Double num, int scale) {
        if (num != null) {
            num = new BigDecimal(num).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return num;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>(16);
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public static Long[] salt = new Long[]{
            0x1fc1745e67c4c0aL,
            0x21c0b0bd6b64695L,
            0x9fb111ffea344e6L,
            0xd17cbb53a5041b0L,
            0x14e2c68158c47daL,
            0x4dedcef1f7a4919L,
            0x770ce322f594a86L,
            0x12391ba30a2412cL,
            0xb9b223691d749f6L,
            0x33d732035164f61L,
            0xab07798af9d4722L,
            0x518fc53453f43c5L,
            0xf7d3fec1c0d42a6L,
            0xc812888ca1f4fb9L,
            0x547c6f24bfa408fL,
            0xa7f0e017377441cL
    };

    public static String decode(long result){
         Long mac;
         Long id =  (result & 0xf000000000000000L) >> 60;
        result ^= salt[id.intValue()];
        mac = (result & 0xff0000L) << 16;
        mac += (result & 0xff00L) << 16;
        mac += (result & 0xff00000000L) >> 16;
        mac += (result & 0xff000000L) >> 16;
        mac += (result & 0xf0L) >> 4;
        mac += (result & 0xfL) << 4;
        mac += 0xb00000000000L;
        return join(Long.toHexString(mac));
    }

    public static String decode(String result) {
        BigInteger rs = new BigInteger(result, 16);
        int index = rs.and(BigInteger.valueOf(0xf000000000000000L)).shiftRight(60).intValue();
        rs = rs.xor(BigInteger.valueOf(salt[index]));

        BigInteger mac = rs.and(BigInteger.valueOf(0xff0000L)).shiftLeft(16);
        mac = mac.add(rs.and(BigInteger.valueOf(0xff00L)).shiftLeft(16));
        mac = mac.add(rs.and(BigInteger.valueOf(0xff00000000L)).shiftRight(16));
        mac = mac.add(rs.and(BigInteger.valueOf(0xff000000L)).shiftRight(16));
        mac = mac.add(rs.and(BigInteger.valueOf(0xf0L)).shiftRight(4));
        mac = mac.add(rs.and(BigInteger.valueOf(0xfL)).shiftLeft(4));
        mac = mac.add(BigInteger.valueOf(0xb00000000000L));

        return join(mac.toString(16));
    }

    public static Long encode(Long mac){
         long result = 0;
         int id = new Random().nextInt(16) %0xf;
        result = 0x0abcb00000000000L + (id << 60);
        result += (mac & 0xff0000) << 16;
        result += (mac & 0xff00) << 16;
        result += (mac & 0xff00000000L) >> 16;
        result += (mac & 0xff000000L) >> 16;
        result += (mac & 0xf0L) >> 4;
        result += (mac & 0xfL) << 4;
        result ^= salt[id];
        //cout<<"id = "<<hex<<id<<" mac = "<<hex<<mac<<" result = "<<hex<<result<<endl;
        return result;
    }

    private static String join(String target){
        char[] chars = target.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<chars.length;i++){
            sb.append(chars[i]);
            if((i+1)%2 ==0 && i < chars.length-1){
                sb.append(":");
            }
        }
        return sb.toString().toUpperCase();
    }

    public static void main(String[] args) {
        System.out.println(decode("c5c18f18e8c35aca"));
    }

}
