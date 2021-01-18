import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

//out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))); //소켓의 출력스트림 생성

public class Server {
    public static void main(String[] args) {
        Socket socket = null;                //Client와 통신하기 위한 Socket
        ServerSocket server_socket = null;  //서버 생성을 위한 ServerSocket
        HashMap<String, PrintWriter> member = new HashMap<String, PrintWriter>();


        try{ //Serversocket을 포트번호 9000에 할당한다. socket()와 bind()에 해당하는 과정
            server_socket = new ServerSocket(9000); //신기하게도 자동으로 포트번호를 연결시켜준다.
        }catch(IOException e) {System.out.println("해당 포트가 열려있습니다."); }

        while(true){
            try {
                socket = server_socket.accept();    //서버 생성 , Client 접속 대기

                if (socket!=null){
                    MThread mthread = new MThread(socket, member);
                    mthread.start();
                }
            }catch(IOException e){ System.out.println("Server error!"); }
        }

    }
}

class MThread extends Thread{
    Socket socket;
    HashMap<String, PrintWriter> member;
    String id;

    BufferedReader sin=null;
    PrintWriter sout=null;

    public MThread(Socket socket, HashMap<String,PrintWriter> member){
        this.socket=socket;
        this.member=member;
        try{

            System.out.println(socket.toString()); //접속한 Client 정보 출력
            this.sin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.sout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            this.id = sin.readLine();

            sout.println("\n\n[채팅방에 접속했습니다.]\n\n");
            sout.flush();

            System.out.println("Client id : "+id);
            broadcast('['+this.id+"] 님이 접속하셨습니다.");

            synchronized (member){
                member.put(this.id,sout);
                Iterator<String> ir = member.keySet().iterator();
                System.out.println("\n\n====Group member=====");
                while(ir.hasNext()){
                    String key = ir.next();
                    PrintWriter pw = member.get(key);
                    System.out.println(key+" + "+pw.toString());
                }

                System.out.println("======================\n");
            }


            //member에 client의 id와 출력 스트림을 넣어준다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        try{
            String chat = null;
            while((chat = sin.readLine())!=null){
                System.out.println(id+':'+chat);
                if (chat=="/quit"){
                    break;
                }
                broadcast(chat);}

        }catch (IOException e){
            e.printStackTrace();
        }finally{
            synchronized (member){
                member.remove(id);
            }
            broadcast('['+this.id+"] 님이 접속을 종료하셨습니다.");
            try {
                if(socket != null) {
                    socket.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }

        }
    }

    public void broadcast(String chat){
        synchronized (member){
            Iterator<String> ir = member.keySet().iterator();
            while(ir.hasNext()){
                String key = ir.next();
                PrintWriter pw = (PrintWriter) member.get(key);
                if (key!=id){

                    System.out.println(pw.toString()+"에게 broadcast");
                    pw.println("["+id+"] : "+chat);
                    pw.flush();
                }
            }
        }
    }
}