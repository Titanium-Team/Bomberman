package bomberman.utils;

public class OS {

    /**
     * if OS is windows.
     */
    public static boolean isWindows = System.getProperty("os.name").contains("Windows");

    /**
     * if OS is linux.
     */
    public static boolean isLinux = System.getProperty("os.name").contains("Linux");

    /**
     * if OS is mac.
     */
    public static boolean isMac = System.getProperty("os.name").contains("Mac");

    /**
     * if the system is 64 bit.
     */
    public static boolean is64Bit = System.getProperty("os.arch").equals("amd64") || System.getProperty("os.arch").equals("x86_64");


}
