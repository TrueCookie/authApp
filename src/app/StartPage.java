package app;

import credentials.*;
import user.*;

import java.io.*;

public class StartPage {
    public static void logIn() {
        CredentialsManager credManager = new CredentialsManager();

        System.out.println("LOG IN");

        String name = null;
        boolean nameIsCorrect = false;
        while (!nameIsCorrect){
            System.out.print("name: ");
            name = System.console().readLine();
            nameIsCorrect = credManager.checkName(name);

            if(!nameIsCorrect) System.out.print("Name is incorrect");
        }

        int failCount = 0;
        boolean passwordIsCorrect = false;

        while (!passwordIsCorrect && failCount < 3){
            String password = String.valueOf(System.console().readPassword());
            passwordIsCorrect = credManager.checkPassword(name, password);
            if(!passwordIsCorrect) failCount++;
            //if(failCount < 1) user.block();
        }

        startApp();

    }

    private static void startApp() {
    }
}
