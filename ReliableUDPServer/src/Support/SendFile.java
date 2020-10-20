/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Support;

import Packet.Constants;
import Packet.Packet;
import Socket.UDPSocket;
import java.util.ArrayList;

/**
 *
 * @author icaroquintao
 */
public class SendFile implements Runnable {

    String name;
    UDPSocket sendFile;
    ArrayList<String> file;
    public static int lastAck;
    int packetNumber;

    public SendFile(String name) {
        packetNumber = 0;
        this.name = name;
        

    }



    @Override
    public void run() {
        FileController fc = new FileController(name);
        file = fc.read();
        
        //Pacote Inicial
        sendFile = new UDPSocket(false, false);
        Packet pkt = new Packet(packetNumber, Constants.SEND_ARCHIVE);
        pkt.setBody(name);
        sendFile.send(pkt);
        packetNumber++;

        //Enviando o Arquivo
        while (!file.isEmpty()) {
            pkt = new Packet(packetNumber, Constants.SEND_ARCHIVE);
            pkt.setBody(file.get(0));
            file.remove(0);
            sendFile.send(pkt);
            packetNumber++;
        }

        //Pacote Final
        pkt = new Packet(packetNumber, Constants.END_ARCHIVE);
        sendFile.send(pkt);
        sendFile.close();
        
    }

}
