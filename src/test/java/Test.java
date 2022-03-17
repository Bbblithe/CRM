import com.blithe.crm.utils.MD5Util;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/17 19:10
 * Description:
 */


public class Test {

    public static void main(String[] args) {
        // String expireTime = "2021-10-10 10:10:10";
        // // 当前系统时间
        // String currentTime = DateTimeUtil.getSysTime();
        // int count = expireTime.compareTo(currentTime);
        // System.out.println(count);

        // String lockState = "0";
        // if("0".equals(lockState)){
        //
        // }

        String ip = "192.168.1.1";
        if(ip.contains("192")){
            System.out.println("地址允许");
        }

        String pwd = MD5Util.getMD5("bjpowernode");
        System.out.println(pwd);
        if("202CB962AC59075B964B07152D234B70".equals(pwd.toUpperCase())){
            System.out.println("样样");
        }
    }
}
