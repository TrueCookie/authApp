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
            try{
                input = DataHelper.reader.readLine();
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
        String name = null;                    //username
        boolean nameIsValid = false;
        while (!nameIsValid){
            System.out.print("Type your name: ");
            //name = System.console().readLine();
            try {
                name = DataHelper.reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            nameIsValid = usersManager.isNameValid(name);

            if(!nameIsValid) System.out.print("Name is invalid\n Make sure your new name does not contains spaces or special symbols");
        }

        String password = null;
        boolean passwordIsValid = false;        //password
        while (!passwordIsValid){
            System.out.print("Type your password: ");
            //password = String.valueOf(System.console().readPassword());
            //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                password = DataHelper.reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            passwordIsValid = PasswordValidator.isPasswordValid(password);
            if(!passwordIsValid){
                System.out.print("Password is invalid\n Make sure your new name does not contains spaces or special symbols");
            }
        }

        usersManager.register(name, password);  //registration

        System.out.println("YOU JUST REGISTERED");

        logIn();
    }

    public static void logIn() {
        System.out.println("LOG IN");

        String name = null;                     //name
        boolean nameIsCorrect = false;
        while (!nameIsCorrect){
            System.out.print("name: ");
            //name = System.console().readLine();
            try{
                name = DataHelper.reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            nameIsCorrect = usersManager.isRegistered(name);

            if(!nameIsCorrect) System.out.print("There is no such account\n");
        }

        int failCount = 0;                      //password
        boolean passwordIsCorrect = false;

        while (!passwordIsCorrect){
            System.out.print("password: ");

            //String password = String.valueOf(System.console().readPassword());    //todo: uncomment before deploy
            String password = null;
            try {
                password = DataHelper.reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            passwordIsCorrect = usersManager.isPasswordCorrect(name, password);

            if(!passwordIsCorrect){     //todo: save fail attempt to log
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

        startApp(name);     //todo: save to log
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

        app.start(usersManager);
        try {
            DataHelper.reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
