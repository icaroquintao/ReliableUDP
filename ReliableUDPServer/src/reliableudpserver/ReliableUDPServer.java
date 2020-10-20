/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reliableudpserver;

import Packet.Constants;
import Packet.Packet;
import Socket.UDPSocket;
import Support.FileController;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author icaroquintao
 */
public class ReliableUDPServer {

    static boolean userValidated;
    static boolean userFounded;

    private static boolean validateUser() throws InterruptedException {
        Scanner in = new Scanner(System.in);
        UDPSocket socket;
        Packet packet;
        while (true) {
            ArrayList<String> userOptions = new ArrayList<>();
            
            userOptions.add("1 - Para entrar com um usuário.");
            userOptions.add("2 - Para criar um novo usuário.");
            userOptions.add("Sua opção: ");
            socket = new UDPSocket(false, false);
            for (String line : userOptions) {
                packet = new Packet(0, Constants.DATA);
                packet.setBody(line);
                socket.send(packet);
            }
            packet = new Packet(0, Constants.END_ARCHIVE);
            socket.send(packet);
            socket.close();

            socket = new UDPSocket(true, false);
            packet = socket.recieve();
            socket.close();
            int option = Integer.parseInt(packet.getBody());
            while (option == 1) {
                //Cliente conecta com usuario e senha existentes
                socket = new UDPSocket(true, false);
                packet = socket.recieve();
                socket.close();
                String user = packet.getBody();
                socket = new UDPSocket(true, false);
                packet = socket.recieve();
                socket.close();
                String pass = packet.getBody();
                FileController fc = new FileController("Users.txt");
                ArrayList<String> users = fc.read();
                fc = new FileController("Passwords.txt");
                ArrayList<String> passwords = fc.read();
                int i;
                userFounded = false;
                for (i = 0; i < users.size(); i++) {
                    if (users.get(i).equals(user)) {
                        userFounded = true;
                        break;
                    }
                }
                if (userFounded) {

                    if (pass.equals(passwords.get(i))) {
                        socket = new UDPSocket(false, false);
                        packet = new Packet(0, Constants.USER_VALIDATED);
                        socket.send(packet);
                        socket.close();
                        return true;
                    } else {
                        socket = new UDPSocket(false, false);
                        packet = new Packet(0, Constants.END_ARCHIVE);
                        socket.send(packet);
                        socket.close();
                    }
                } else {
                    socket = new UDPSocket(false, false);
                    packet = new Packet(0, Constants.END_ARCHIVE);
                    socket.send(packet);
                    socket.close();
                }
            }
            if (option == 2) {
                //Cliente cria um novo usuario e senha
                socket = new UDPSocket(true, false);
                packet = socket.recieve();
                socket.close();
                String user = packet.getBody();
                
                socket = new UDPSocket(true, false);
                packet = socket.recieve();
                socket.close();
                String pass = packet.getBody();

                FileController fc1 = new FileController("Users.txt");
                ArrayList<String> users = fc1.read();
                FileController fc2 = new FileController("Passwords.txt");
                ArrayList<String> passwords = fc2.read();
                int i;
                userFounded = false;
                if (users.contains(user)) {
                    packet = new Packet(0, Constants.END_ARCHIVE);
                    packet.setBody("Usuário já existente.");
                    socket = new UDPSocket(false, false);
                    socket.send(packet);
                    socket.close();
                } else {
                    packet = new Packet(0, Constants.USER_VALIDATED);
                    packet.setBody("Usuário criado com sucesso.");
                    socket = new UDPSocket(false, false);
                    socket.send(packet);
                    socket.close();
                    users.add(user);
                    passwords.add(pass);
                    fc1.write(users);
                    fc2.write(passwords);
                }

            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Menu menu;
            System.out.println("Servidor rodando...");
            System.out.println("Esperando Cliente");
            userValidated = validateUser();

            if (userValidated) {
                menu = new Menu();
            }

        } catch (Exception e) {
            System.out.println("Error Fatal!");
            exit(0);
        }
    }

}
