package com.example.asm6_v1.data;

import com.example.asm6_v1.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


@Service
public class DatabaseLayer {

    private final List<UserAccount> users;

    private String filename;

    @Autowired
    public DatabaseLayer() throws IOException {

        users = initialize();
        filename = "db/users.txt";
    }

    public DatabaseLayer(String fileName) throws IOException {
        users = initializeWithFile(fileName);
        this.filename = fileName;
    }

    private static List<UserAccount> initialize() throws IOException {
        return initializeWithFile("db/users.txt");
    }
    private static List<UserAccount> initializeWithFile(String filename) throws IOException {


        try (final BufferedReader br = new BufferedReader(
                new InputStreamReader(new ClassPathResource(filename).getInputStream()))) {
            final List<UserAccount> users = new ArrayList<>();
            String line;
            for (line = br.readLine(); line != null; line = br.readLine()) {
                final String[] arrStr = line.split(",");
                final UserAccount user = new UserAccount(arrStr[0],
                        arrStr[1],
                        Integer.parseInt(arrStr[2]),
                        Integer.parseInt(arrStr[3])
                );
                users.add(user);
            }
            return users;
        }
    }

    public boolean update(UserAccount userAccount){

        int foundIndex = -1;
         for(int i = 0; i < users.size() ; i ++){
             if(userAccount.getUid().equals(users.get(i).getUid())){
                 foundIndex = i;
                 break;
             }
         }
         if(foundIndex >= 0) {
             users.set(foundIndex, userAccount);
             try {
                 updateDb();
             } catch (IOException e) {
                 System.out.println("Can't update");
                 return false;
             }
             return true;
         }
         return false;
    }

    public void updateDb() throws IOException {
        try (final PrintWriter pw = new PrintWriter(new ClassPathResource(filename).getFile())) {
            pw.print("");
            for (final UserAccount user : users) {
                pw.append(user.toString());
                pw.append('\n');
            }
        }
    }

    public UserAccount getUserById(String uid) {
        for (UserAccount user : users) {
            if (uid.equalsIgnoreCase(user.getUid())) {
                return user;
            }
        }
        return null;
    }
}
