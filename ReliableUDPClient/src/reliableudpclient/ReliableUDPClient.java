/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reliableudpclient;

import Packet.Constants;
import Packet.Packet;
import Socket.UDPSocket;
import Support.Log;
import Support.RecieveFileClient;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.Scanner;

/**
 *
 * @author icaroquintao
 */
public class ReliableUDPClient {

    public static void clear() {
        for (int i = 0; i < 30; i++) {
            System.out.printf("\n");
        }
    }

    private static void validateUser() throws InterruptedException {
        while (true) {
            try {
            new Log();
            } catch (IOException ex) {
                System.out.println("Não foi possível criar arquivo de log.");
            }
            UDPSocket socket = new UDPSocket(true, false);
            Packet pkt;
            pkt = socket.recieve();
            do {
                System.out.println(pkt.getBody());
                Log.d(pkt.getBody());
                pkt = socket.recieve();
            } while (pkt.getFlags()[Constants.END_ARCHIVE] != 1);
            socket.close();

            //Escolhe entre usar um usuario e senha já existentes ou criar um novo
            Scanner in = new Scanner(System.in);
            int option = in.nextInt();
            socket = new UDPSocket(false, false);
            pkt = new Packet(0, Constants.DATA);
            pkt.setBody("" + option);
            System.out.println("Enviando opção escolhida ao servidor.");
            socket.send(pkt);
            socket.close();
            while (option == 1) {
                //Envia usuario e senha para o servidor
                System.out.printf("Digite o nome do usuário: ");
                String user = in.next();
                System.out.print("Digite a senha: ");
                String pass = in.next();
                socket = new UDPSocket(false, false);
                pkt = new Packet(0, Constants.DATA);
                pkt.setBody(user);
                socket.send(pkt);
                pkt.setBody(pass);
                socket.send(pkt);
                socket.close();

                Thread.sleep(50);
                socket = new UDPSocket(true, false);
                pkt = socket.recieve();
                socket.close();

                if (pkt.getFlags()[Constants.USER_VALIDATED] != 1) {
                    clear();
                    System.out.println("Usuário ou senha incorretos");
                    Log.d("Usuário ou senha incorretos");
                    option = 1;
                } else {
                    return;
                }
            }
            while (option == 2) {
                System.out.printf("Digite o nome do novo usuário: ");
                Log.d("Digite o nome do novo usuário: ");
                String user = in.next();
                Log.d(user);
                System.out.print("Digite a senha do novo usuário: ");
                Log.d("Digite a senha do novo usuário: ");
                String pass = in.next();
                Log.d(pass);
                
                socket = new UDPSocket(false, false);
                pkt = new Packet(0, Constants.DATA);
                pkt.setBody(user);
                socket.send(pkt);
                pkt.setBody(pass);
                socket.send(pkt);
                socket.close();
                
                socket = new UDPSocket(true, false);
                pkt = socket.recieve();
                socket.close();
                clear();
                System.out.println(pkt.getBody());
                if(pkt.getFlags()[Constants.END_ARCHIVE]==1)
                {
                    continue;
                } else {
                    option = 0;
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        UDPSocket socket;
        Packet pkt;
        Scanner in = new Scanner(System.in);
        int option;
        validateUser();

        while (true) {
            clear();
            socket = new UDPSocket(true, false);
            pkt = socket.recieve();
            do {
                System.out.println(pkt.getBody());
                pkt = socket.recieve();
            } while (pkt.getFlags()[Constants.END_ARCHIVE] != 1);
            socket.close();
            socket = new UDPSocket(false, false);

            //escolhe uma opção do menu enviado pelo servidor
            in = new Scanner(System.in);
            option = in.nextInt();
            pkt = new Packet(1, Constants.DATA);
            pkt.setBody("" + option);
            socket.send(pkt);
            socket.close();
            if (option == 2) {
                exit(0);
            }
            socket = new UDPSocket(true, false);
            pkt = socket.recieve();
            socket.close();
            if (option == 1) {
                //imprime lista de arquivos disponiveis no arquivo ''Files'' do servidor
                do {
                    System.out.println(pkt.getBody());
                    socket = new UDPSocket(true, false);
                    pkt = socket.recieve();
                    socket.close();
                } while (pkt.getFlags()[Constants.END_ARCHIVE] != 1);
                //escolhe um dos arquivos para baixar
                option = in.nextInt(); 
                Log.d("" + option);
                socket = new UDPSocket(false, false);
                pkt = new Packet(0, Constants.GIVE_ME_ARCHIVE);
                pkt.setBody("" + option);
                socket.send(pkt);
                socket.close();
                clear();
                Thread.sleep(50);
                socket = new UDPSocket(true, false);
                pkt = socket.recieve();
                socket.close();
                System.out.println(pkt.getBody());
                Thread.sleep(50);
                socket = new UDPSocket(true, false);
                pkt = socket.recieve();
                socket.close();
                
                if (pkt.getFlags()[Constants.SEND_ARCHIVE] == 1) {

                    //Pacote com o nome do arquivo
                    socket = new UDPSocket(true, false);
                    pkt = socket.recieve();
                    socket.close();
                    Thread rf = new Thread(new RecieveFileClient(pkt));
                    rf.start();
                    rf.join();
                    clear();
                    System.out.println("Download finalizado.");
                }
            }
        }
    }

}
