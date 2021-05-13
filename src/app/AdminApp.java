package app;

import credentials.*;

import java.io.*;

import static app.Cmds.*;

public class AdminApp implements App{

    @Override
    public void start(UsersManager usersManager) {
        System.out.println("ADMIN MODE ACTIVATED");
        System.out.println("Type '-h' for help");

        String input = null;
        do{
            try {
                input = DataHelper.reader.readLine();

                if(input.startsWith(BLOCK_CMD)){
                    usersManager.block(getUserFromCmd(input));
                }else if(input.startsWith(UNBLOCK_CMD)){
                    usersManager.unblock(getUserFromCmd(input));
                }else if(input.startsWith(DELETE_CMD)){
                    usersManager.delete(getUserFromCmd(input));
                }else if(input.startsWith(APPOINT_ADMIN_CMD)){
                    usersManager.appoint(getUserFromCmd(input));
                }else if(input.startsWith(DISMISS_ADMIN_CMD)){
                    usersManager.dismiss(getUserFromCmd(input));
                }else if(input.startsWith(HELP_CMD)){
                    help();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }while (!input.equals(EXIT_CMD));
    }

    @Override
    public void help() {
        System.out.println(
                "Commands:\n" +
                    "BLOCK: -b\n" +
                    "UNBLOCK: -u\n" +
                    "DELETE: -d\n" +
                    "APPOINT_ADMIN: -aa\n" +
                    "DISMISS_ADMIN: -da\n" +
                    "HELP: -h\n" +
                    "EXIT: -exit\n");
    }

    String getUserFromCmd(String input){
        int spaceIndex = input.indexOf(' ');
        return input.substring(spaceIndex, input.length()-1).trim();
    }
}
