
import java.io.*;
import java.net.Socket;

public class client1 {
    public static void main(String[] arg)
    {
        Socket socket = null;            //Server와 통신하기 위한 Socket
        BufferedReader in = null;        //Server로부터 데이터를 읽어들이기 위한 입력스트림
        BufferedReader in2 = null;        //키보드로부터 읽어들이기 위한 입력스트림
        PrintWriter out = null;
        try {
            socket = new Socket("127.0.0.1", 9000); //서버

            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //소켓 입력 스트림
            in2 = new BufferedReader(new InputStreamReader(System.in)); //키보드 입력 스트림
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

            System.out.println(socket.toString()); //Socket[addr=/127.0.0.1,port=9000,localport=60984]

        }catch(IOException e) {
            System.out.println("소켓 생성 오류"); //서버 먼저 실행해야됨 ㅅㅂ
        }

        try {
            System.out.print(">> ");
            assert in2 != null;
            String data = in2.readLine();            //키보드로부터 입력

            assert out != null;
            out.println(data);                        //서버로 데이터 전송
            out.flush();

            String str2 = in.readLine();            //서버로부터 되돌아오는 데이터 읽어들임
            System.out.print("태영 : " + str2);

            socket.close();

        }catch(IOException e) {
            System.out.println("Client error! Client error!");
        }


    }
}
