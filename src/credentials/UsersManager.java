package credentials;

import app.DataHelper;

import java.io.*;
import java.util.*;

public class UsersManager {

    private final Map<String, String> creds = new HashMap<>();

    public UsersManager(){
        mapData();
    }

    public void mapData() {
        try(BufferedReader br = new BufferedReader(new FileReader(DataHelper.CREDS))) {
            for (Object cell : br.lines().toArray())
            {
                String line = cell.toString();
                int space = line.indexOf(' ');

                String name = line.substring(0, space);
                String password = line.substring(space+1);

                creds.put(name, password);
            }
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
        try(BufferedReader br = new BufferedReader(new FileReader(DataHelper.BLOCKED))){
            return br.lines().anyMatch(line -> line.equals(name));
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

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tmpFile));

        String currentLine;
        while ((currentLine = reader.readLine()) != null){
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(line)) continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        reader.close();
        writer.close();
        boolean successful = tmpFile.renameTo(inputFile);
    }

    public boolean isPasswordValid(String password) {
        return true;//todo: check password
    }

    public boolean isNameValid(String name) {
        return true;
    }
}
