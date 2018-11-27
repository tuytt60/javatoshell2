package tech.fsdn.javatoshell2.utils;

/**
 * @author qi.liu
 * @create 2018-11-27 17:12
 * @desc 描述:
 **/
public class KeyMapUtil {

    public static int converKeyMap(String message) {
        int code = 0;

        switch (message) {
            case "Shift":
                throw new RuntimeException("不需要shift执行code");
            case "Control":
                break;
            case "CapsLock":
                break;
            case "stopCommond":
                code= 3;
                break;
            case "Enter":
                code = 10;
                break;

            case "Backspace":
                code = 8;
                break;

            case "Delete":
                code = 127;
                break;
            case "Tab":
                code = 9;
                break;

            case "Escape":
                code = 27;
                break;
            case "ArrowUp":
                code = 0xE0;
                break;
            case "ArrowDown":
                code = 0xE1;
                break;
            case "ArrowLeft":
                code = 0xE2;
                break;
            case "ArrowRight":
                code = 0xE3;
                break;



            default:
                code = message.toCharArray()[0];
                break;

        }

        return code;
    }

}
