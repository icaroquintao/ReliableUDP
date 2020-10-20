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
import Support.SendFile;
import static java.lang.System.exit;
import java.util.ArrayList;

/**
 *
 * @author icaroquintao
 */
public final class Menu {

    public Menu() {
        this.show();
    }

    public int printMenu() {
        System.out.println("Enviando Menu pro cliente");
        int option = 0;
        int number = 0;
        ArrayList<String> menuOptions = new ArrayList<>();
        UDPSocket socket;
        do {
            try {
                menuOptions.add("Meu Menu");
                menuOptions.add("1 - Mostrar lista de arquivos");
                menuOptions.add("2 - Fechar conexão");
                menuOptions.add("Sua opção:  ");
                Packet pkt;
                socket = new UDPSocket(false, false);
                for (int i = 0; i < menuOptions.size(); i++) {
                    pkt = new Packet(i, Constants.DATA);
                    pkt.setBody(menuOptions.get(i));
                    socket.send(pkt);
                }
                pkt = new Packet(0, Constants.END_ARCHIVE);
                socket.send(pkt);
                socket.close();

                socket = new UDPSocket(true, false);
                pkt = socket.recieve();
                option = Integer.parseInt(pkt.getBody());
                socket.close();
            } catch (Exception e) {
                System.out.println("Tentativa inválida, tente novamente!");
            }
        } while (option < 1 || option > 2);

        return option;
    }

    public void sendListOfFiles() {
        FileController fc = new FileController("Files");
        ArrayList<String> file = fc.read();
        int number = 0;
        UDPSocket socket = new UDPSocket(false, false);
        Packet pkt = new Packet(number, Constants.SEND_ARCHIVE);
        pkt.setBody("Escolha um arquivo para download: ");
        socket.send(pkt);
        number++;
        for (String line : file) {
            pkt = new Packet(number, Constants.SEND_ARCHIVE);
            pkt.setBody("" + number + " - " + line);
            socket.send(pkt);
            number++;
        }
        pkt = new Packet(number, Constants.SEND_ARCHIVE);
        pkt.setBody("" + number + " - Voltar para o menu principal.");
        socket.send(pkt);

        pkt = new Packet(number, Constants.SEND_ARCHIVE);
        pkt.setBody("Sua opção: ");
        socket.send(pkt);

        pkt = new Packet(number, Constants.END_ARCHIVE);
        socket.send(pkt);
        socket.close();
        socket = new UDPSocket(true, false);
        pkt = socket.recieve();
        int option = Integer.parseInt(pkt.getBody());
        socket.close();

        if (option != file.size() + 1) {
            socket = new UDPSocket(false, false);
            pkt = new Packet(number, Constants.SEND_ARCHIVE);
            pkt.setBody("Iniciando Download do arquivo. ");
            socket.send(pkt);
            sendFile(file.get(option - 1));
            socket.close();
        } else {
            socket = new UDPSocket(false, false);
            pkt = new Packet(number, Constants.SEND_ARCHIVE);
            pkt.setBody("Retornando ao menu principal.");
            socket.send(pkt);
            socket.close();
        }
    }

    private void sendFile(String name) {
        try {
            System.out.println("Enviando " + name + " ao cliente");
            UDPSocket socket = new UDPSocket(false, false);
            Packet pkt = new Packet(0, Constants.SEND_ARCHIVE);
            socket.send(pkt);
            socket.close();
            Thread sf = new Thread(new SendFile(name));
            sf.start();
            sf.join();
            System.out.println("Envio completo.");
        } catch (InterruptedException ex) {
            System.out.println("Erro ao enviar arquivo.");
        }
    }

    public void show() {
        while (true) {

            int option = printMenu();
            System.out.println("Opção escolhida: " + option);
            switch (option) {
                case 1:
                    sendListOfFiles();
                    break;
                
                default:
                    System.out.println("Fechando servidor.");
                    exit(0);
            }
        }
    }

}
