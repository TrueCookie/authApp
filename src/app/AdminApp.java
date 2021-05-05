package app;

import java.io.*;

public class AdminApp implements App{

    @Override
    public void start() {
        System.out.println("Type '-exit' to exit");
        System.out.println("Type '-h' for help");

        String input = null;
        do{
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                input = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }while (!input.equals("-exit"));
    }
}
