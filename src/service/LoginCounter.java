package service;

import controller.LoginController;

public class LoginCounter {

    public static int count = 3;

    public static void logCount () {
        count  --;
        if (count  == 0) {
            System.exit(0);
        }
    }


}
