package app;

import credentials.*;

import java.io.*;

import static app.Cmds.LOG_IN_CMD;
import static app.Cmds.SIGN_IN_CMD;

public class StartPage {

    private static final UsersManager usersManager = new UsersManager();

    public static void start() {
        System.out.println("Type '" + LOG_IN_CMD + "' to log in\n or '"+ SIGN_IN_CMD +"' to sign in accordingly");

        String input = "";
        do{
            //input = System.console().readLine();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                input = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(input.equals(LOG_IN_CMD)){
                logIn();
                break;
            }else if(input.equals(SIGN_IN_CMD)){
                signIn();
                break;
            }
        }while (!input.equals("-exit"));

    }

    public static void signIn() {
        String name;
        boolean nameIsCorrect = false;
        while (!nameIsCorrect){
            System.out.print("name: ");
            name = System.console().readLine();
            nameIsCorrect = usersManager.isNameValid(name);

            if(!nameIsCorrect) System.out.print("Name is invalid\n Make sure your new name does not containts spaces or special symbols");
        }
        //pw check
        boolean passwordIsValid = false;

        while (!passwordIsValid){
            String password = String.valueOf(System.console().readPassword());
            passwordIsValid = usersManager.isPasswordValid(password);
        }
        System.out.println("YOU JUST REGISTERED");

        logIn();
    }

    public static void logIn() {
        System.out.println("LOG IN");

        String name = null;
        boolean nameIsCorrect = false;
        while (!nameIsCorrect){
            System.out.print("name: ");
            //name = System.console().readLine();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                name = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            nameIsCorrect = usersManager.isRegistered(name);

            if(!nameIsCorrect) System.out.print("There is no such account");
        }

        int failCount = 0;
        boolean passwordIsCorrect = false;

        while (!passwordIsCorrect && failCount < 3){
            System.out.print("password: ");
            String password = null;    //String password = String.valueOf(System.console().readPassword());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                password = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            passwordIsCorrect = usersManager.isPasswordCorrect(name, password);

            if(!passwordIsCorrect){
                System.out.println("Password is incorrect");
                failCount++;
                System.out.println("Attempts left: " + (3 - failCount));
                if(failCount >= 3){
                    usersManager.block(name);
                    System.out.println("Your account is blocked! Contact your administrator");
                    return;
                }
            }
        }

        startApp(name);
    }

    private static void startApp(String userName) {
        App app;
        if (usersManager.isBlocked(userName)){
            System.out.println("Cannot log in: ACCOUNT IS BLOCKED");
            return;
        }else if(usersManager.isAdmin(userName)){
            app = new AdminApp();
        }else {
            app = new UserApp();
        }

        app.start();

    }
}
