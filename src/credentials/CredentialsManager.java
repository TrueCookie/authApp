package credentials;

import app.DataHelper;

import java.io.*;
import java.util.*;
import java.util.stream.*;

public class CredentialsManager {

    private File file = new File(DataHelper.USERS);
    private Map<String, String> creds = new HashMap<>();

    public CredentialsManager(){
        mapData();
    }

    public void mapData() {
        InputStream inputStream = null;

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
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


    public boolean checkName(String name) {

    }

    public boolean checkPassword(String name, String password) {
    }
}
