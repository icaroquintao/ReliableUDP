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
public class RecieveFileClient implements Runnable {

    ArrayList<String> nameOfFiles;
    ArrayList<String> archive;
    FileController fc;
    int numberOfCounting;
    int tamArchive;
    UDPSocket recieveFile;

    public RecieveFileClient(Packet initialPacket) {
        numberOfCounting = 0;
        //Cria o arquivo no cliente
        fc = new FileController(initialPacket.getBody());
        archive = new ArrayList<>();

    }

    @Override
    public void run() {
        
        recieveFile = new UDPSocket(true, false);
        Packet pkt = recieveFile.recieve();
        while (pkt.getFlags()[Constants.SEND_ARCHIVE] == 1) {
            archive.add(pkt.getBody());
            pkt = recieveFile.recieve();
        }
        recieveFile.close();
        fc.write(archive);
        
    }

}
