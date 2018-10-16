package com.jonex.galaxy.agent.login;

/**
 *
 * java -cp GalaxyAgent.jar -javaagent:GalaxyAgent.jar="login" com.jonex.galaxy.agent.login.UserService
 *
 * Created by xubai on 2018/10/15 下午5:33.
 */
public class UserService {

    public void login(){
        try {
            System.out.println("Logining......");
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UserService userService = new UserService();
        userService.login();
    }

}
