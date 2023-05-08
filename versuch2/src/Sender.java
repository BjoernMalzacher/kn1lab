import java.io.*;
import java.net.*;
import java.time.Duration;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * Die "Klasse" Sender liest einen String von der Konsole und zerlegt ihn in einzelne Worte. Jedes Wort wird in ein
 * einzelnes {@link Packet} verpackt und an das Medium verschickt. Erst nach dem Erhalt eines entsprechenden
 * ACKs wird das nächste {@link Packet} verschickt. Erhält der Sender nach einem Timeout von einer Sekunde kein ACK,
 * überträgt er das {@link Packet} erneut.
 */
public class Sender {
    /**
     * Hauptmethode, erzeugt Instanz des {@link Sender} und führt {@link #send()} aus.
     * @param args Argumente, werden nicht verwendet.
     */
    public static void main(String[] args) {
        Sender sender = new Sender();
        try {
            sender.send();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Erzeugt neuen Socket. Liest Text von Konsole ein und zerlegt diesen. Packt einzelne Worte in {@link Packet}
     * und schickt diese an Medium. Nutzt {@link SocketTimeoutException}, um eine Sekunde auf ACK zu
     * warten und das {@link Packet} ggf. nochmals zu versenden.
     * @throws IOException Wird geworfen falls Sockets nicht erzeugt werden können.
     */
    private void send() throws IOException {

        
       
        //Text einlesen und in Worte zerlegen
        String txt = new Scanner(System.in).nextLine();
      
        // Socket erzeugen auf Port 9998 und Timeout auf eine Sekunde setzen
        DatagramSocket socket = new DatagramSocket(9998);
        socket.setSoTimeout(1000);
        String[] list = txt.split(" ");
        // Iteration über den Konsolentext
        int index = 0;
        Packet packetIn = null;
        int seqnumb = 0;
        while (index<list.length) {
            Packet p = new Packet(list[index].length()*2, seqnumb, false, list[index].getBytes());
            int newseqnumb =seqnumb + (list[index].length()*2);
        	// Paket an Port 9997 senden
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            ObjectOutputStream o = new ObjectOutputStream(b);
            o.writeObject(p);
            byte[] buf = b.toByteArray();
            InetAddress address = InetAddress.getByName("localhost");
            DatagramPacket pack = new DatagramPacket(buf, buf.length,address, 9997);
            System.out.println("old seq:"+ seqnumb);
            socket.send(pack);
            try {
                buf = new byte[256];
                pack = new DatagramPacket(buf, buf.length);
                socket.receive(pack);
                ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(pack.getData()));
                packetIn = (Packet) is.readObject();
                String received = new String(packetIn.getPayload(),0,packetIn.getPayload().length);
                System.out.println("returnd word:"+received);
                index+=1;
                seqnumb = newseqnumb;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
            	System.out.println("Receive timed out, retrying...");

            }
            
        }
        
        // Wenn alle Packete versendet und von der Gegenseite bestätigt sind, Programm beenden
        socket.close();
        
        if(System.getProperty("os.name").equals("Linux")) {
            socket.disconnect();
        }

        System.exit(0);
    }
}
