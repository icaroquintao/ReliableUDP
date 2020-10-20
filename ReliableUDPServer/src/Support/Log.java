package Support;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author icaroquintao
 */
public class Log {

    public Log() throws IOException {
        File file = new File("Log.txt");
        if (file.exists()) {
            file.delete();
        }
    }

    public static void d(String s) {
        FileWriter file = null;
        try {
            file = new FileWriter(new File("Log.txt"),true);
             Timestamp dataDeHoje = new Timestamp(System.currentTimeMillis());
            file.write("\n"+ dataDeHoje + " " + s);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                file.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
}
