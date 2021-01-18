import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] arg)
    {
        Socket socket = null;            //Server와 통신하기 위한 Socket
        BufferedReader in = null;        //Server로부터 데이터를 읽어들이기 위한 입력스트림
        BufferedReader keyboardin = null;        //키보드로부터 읽어들이기 위한 입력스트림
        PrintWriter out = null;
        String id;

        try {
            socket = new Socket("127.0.0.1", 9000);

            keyboardin = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));


        }catch(IOException e) {
            System.out.println("소켓 생성 오류");
        }

        try {
            System.out.print("\n\n아이디를 입력해주세요 >>");
            id = keyboardin.readLine();

            assert out != null;
            out.println(id);                        //서버로 id 전송
            out.flush();

            cThread cthread = new cThread(socket, in);
            cthread.start();

            String line=null;
            while ((line = keyboardin.readLine())!=null) {
                out.println(line);
                out.flush();
                if (line.equalsIgnoreCase("/exit")) {
                    break;
                }
            }
        }catch(IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if (in !=null){
                    in.close();
                }
                if (out != null){
                    out.close();
                }
                if(socket!=null){
                    socket.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

class cThread extends Thread{
    private Socket socket = null;
    private BufferedReader in = null;

    public cThread(Socket socket,BufferedReader in) {
        this.socket = socket;
        this.in = in;
    }
    public void run(){
        try{
            String line=null;
            while ((line = in.readLine())!=null){
                System.out.println(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (in !=null){
                    in.close();
                }
                if (socket!=null){
                    socket.close();
                }
            }catch (IOException e){e.printStackTrace();}
        }
    }

}
