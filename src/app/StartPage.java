package app;

import credentials.*;

import java.io.*;

import static app.Cmds.*;

public class StartPage {

    private static final UsersManager usersManager = new UsersManager();

    public void start() {
        System.out.println("Type '" + LOG_IN_CMD + "' to log in\n or '"+ SIGN_UP_CMD +"' to sign in accordingly");

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
            }else if(input.equals(SIGN_UP_CMD)){
                signUp();
                break;
            }
        }while (!input.equals(EXIT_CMD));

    }

    public void signUp() {
        System.out.println("SIGN UP");

        String name;
        String passwordTry1;
        String passwordTry2;
        do {
            name = getNewNameInput();
            passwordTry1 = getNewPasswordInput();
            passwordTry2 = getNewPasswordInput();
        }while (!passwordTry1.equals(passwordTry2));
        usersManager.register(name, passwordTry1);  //registration

        System.out.println("YOU'RE JUST REGISTERED");

        logIn();
    }

    public void logIn() {
        System.out.println("LOG IN");

        String name = getNameInput();
        boolean successfulLogIn = checkPasswordInput(name);

        if(successfulLogIn) {
            startApp(name);     //todo: log
        }
    }

    public String getNewNameInput() {
        String name = null;
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
        return name;
    }

    public String getNewPasswordInput() {
        String password = null;
        boolean passwordIsValid = false;
        while (!passwordIsValid){
            System.out.print("Type your password: ");
            //password = String.valueOf(System.console().readPassword());   //todo: uncomment before deploy
            try {
                password = DataHelper.reader.readLine();    //todo: repeat input (+ due to change pw)
            } catch (IOException e) {
                e.printStackTrace();
            }
            passwordIsValid = PasswordValidator.isPasswordValid(password);
            if(!passwordIsValid){
                System.out.print("Password is invalid\n Make sure your new password follows constraints.\n");
            }
        }
        return password;
    }

    private String getNameInput() {
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
        return name;
    }

    private boolean checkPasswordInput(String name) {
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
                    return false;
                }
            }
        }
        return true;
    }

    private void startApp(String userName) {
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
