package app;

import credentials.*;

import java.io.*;

public class UserApp implements App {

    @Override
    public void start(UsersManager usersManager) {
        System.out.println("USER MODE ACTIVATED");
        System.out.println("Type '-h' for help");

        String input = null;
        do{
            try {
                input = DataHelper.reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }while (!input.equals("-exit"));
    }

    @Override
    public void help() {
        System.out.println("""
                Commands:
                    HELP: "-h"
                    EXIT: "-exit" """);
    }
}
