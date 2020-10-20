/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Support;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 *
 * @author icaroquintao
 */
public class FileController {

    String fileName;
    BufferedReader buffRead;
    BufferedWriter buffWrite;

    public FileController(String name) {
            this.fileName = name;
    }

    //Função que lê o arquivo e retorna um ArrayList de Strings onde cada 
    //String corresponde a uma linha do arquivo
    public ArrayList<String> read() {
        ArrayList<String> retorno = new ArrayList<>();
        if (exists()) {
            try {
                buffRead = new BufferedReader(new FileReader(fileName));

                String line = buffRead.readLine();

                while (line != null) {
                    retorno.add(line);
                    line = buffRead.readLine();
                }

                buffRead.close();
            } catch (Exception e) {
            }
        }

        return retorno;
    }

    //Escreve um ArrayList de strings no arquivo, 
    //sendo que cada String vira uma nova linha no arquivo
    public void write(ArrayList<String> texto) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            buffWrite = new BufferedWriter(new FileWriter(fileName));
            if (texto == null) {
                return;
            }
            for (String s : texto) {
                buffWrite.append(s);
                buffWrite.newLine();
            }
            buffWrite.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Verifica se o arquivo existe
    public boolean exists() {
        File arq = new File(this.fileName);
        return arq.exists();
    }
}
