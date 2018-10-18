import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

	public static void main(String[] args) {
		try {
			//서버 소켓 생성
			ServerSocket ss = new ServerSocket(1993);
			System.out.println("서버구동중");
			while(true) {
				//클라이언트의 접속대기 - 접속을 하면 클라이언트와 통신을 할 수 있는 
				//소켓이 리턴되고 그 소켓 정보를 socat에 저장
				Socket socket = ss.accept();
				//클라이언트 정보 출력
				System.out.println("클라이언트 접속:" + socket.getInetAddress());
				//클라이언트에게서 전송된 문자열을 줄단위로 읽기 위한 스트림 생성
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				//한줄 읽기
						String msg = br.readLine();
					System.out.println("보내준 메시지:" + msg);
					//문자열을 전송하기 위한 스트림 만들기
					PrintWriter  pw  = new PrintWriter(socket.getOutputStream());
					//문자열 전송
					pw.println( msg + "을 받았습니다.");
					pw.flush();
					
					pw.close();
					br.close();
					socket.close();
					//한번하고 끝난거니까 한번씩 밖에 못한다.
			}
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

}
