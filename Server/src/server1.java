
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class server1 {public static void main(String[] args) {
    Socket socket = null;                //Client와 통신하기 위한 Socket
    ServerSocket server_socket = null;  //서버 생성을 위한 ServerSocket
    BufferedReader serverin = null;
    BufferedReader in = null;            //Client로부터 데이터를 읽어들이기 위한 입력스트림
    PrintWriter out = null;                //Client로 데이터를 내보내기 위한 출력 스트림

    try{ //Serversocket을 포트번호 9000에 할당한다. socket()와 bind()에 해당하는 과정
        server_socket = new ServerSocket(9000); //신기하게도 자동으로 포트번호를 연결시켜준다.
        serverin=new BufferedReader(new InputStreamReader(System.in)); //키보드 입력 받기 위한 입력 스트림

    }catch(IOException e)
    {
        System.out.println("해당 포트가 열려있습니다."); //오류메시지
    }

    System.out.println("서버 생성 성공");

    try {

        socket = server_socket.accept();    //서버 생성 , Client 접속 대기

        System.out.println(socket.toString()); //socket 정보 확인(Socket[addr=/127.0.0.1,port=60984,localport=9000])

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));    //소켓의 입력스트림 생성
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))); //소켓의 출력스트림 생성

        String str = null;
        str = in.readLine();                //Client로부터 데이터를 읽어옴

        System.out.println("준영 : " + str); //출력
        System.out.print(">> ");
        assert serverin != null;
        str = serverin.readLine(); //키보드 입력


        out.print(str); //출력스트림으로 내보내주기
        out.flush();


        socket.close();

    }catch(IOException e){

        System.out.println("Server error! Server error!");
    }
}
}
