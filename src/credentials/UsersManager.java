package credentials;

import app.DataHelper;

import java.io.*;
import java.util.*;

public class UsersManager {

    private static Map<String, String> creds;
    static {
        creds = new HashMap<>();
        mapData();
    }

    public static void mapData() {
        try(BufferedReader br = new BufferedReader(new FileReader(DataHelper.CREDS))) {
            for (Object cell : br.lines().toArray())
            {
                String line = cell.toString();
                int space = line.indexOf(' ');

                String name = line.substring(0, space);
                String password = String.valueOf(System.console().readPassword());
                //String encodedPW = line.substring(space+1);
                //byte[] decodedPW = Base64.getDecoder().decode(encodedPW);
                //String password = Arrays.toString(decodedPW);

                creds.put(name, password);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(String name, String password){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(DataHelper.CREDS))){
            //byte[] encodedPW = Base64.getEncoder().encode(password.getBytes());
            bw.write(name + ' ' + password + System.getProperty("line.separator"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRegistered(String name) {
        return creds.containsKey(name);
    }

    public boolean isBlocked(String name) {
        try(BufferedReader br = new BufferedReader(new FileReader(DataHelper.BLOCKED))){
            return br.lines().anyMatch(line -> line.equals(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean isAdmin(String name) {
        try(BufferedReader br = new BufferedReader(new FileReader(DataHelper.ADMINS))){
            return br.lines().anyMatch(line -> line.trim().equals(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean isPasswordCorrect(String name, String password) {
        return creds.get(name).equals(password);
    }

    public void block(String name){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(DataHelper.BLOCKED))){
            bw.write(name + System.getProperty("line.separator"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unblock(String name){
        try {
            removeLine(DataHelper.BLOCKED, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeLine(String path, String line) throws IOException {
        File inputFile = new File(path);    //path = DataHelper.BLOCKED
        File tmpFile = new File("tmp.txt");

        BufferedReader fileReader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(tmpFile));

        String currentLine;
        while ((currentLine = fileReader.readLine()) != null){
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(line)) continue;
            fileWriter.write(currentLine + System.getProperty("line.separator"));
        }
        fileReader.close();
        fileWriter.close();
        boolean successful = tmpFile.renameTo(inputFile);
    }

    public boolean isNameValid(String name) {
        return true;
    }

    public void delete(String name) {
        try(BufferedReader br = new BufferedReader(new FileReader(DataHelper.CREDS))){
            String credsLine = br.lines().filter(line -> line.startsWith(name)).findFirst().orElse(null);
            removeLine(DataHelper.CREDS, credsLine);
            dismiss(name);
            unblock(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appoint(String name) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(DataHelper.ADMINS))){
            writer.write(name + System.getProperty("line.separator"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dismiss(String name) {
        try {
            removeLine(DataHelper.ADMINS, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
