package log;

import app.*;

import java.io.*;

public class Log {
    public static void log(String level, String info){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(DataHelper.LOGS))){
            bw.write(info + System.getProperty("line.separator"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
