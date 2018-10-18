import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServerPrec {

	public static void main(String[] args) {
		try {
			while(true) {
				ServerSocket ss = new ServerSocket(1994);
				
				Socket sk = ss.accept();
				
				System.out.println("클라이언트 접속:" + sk.getInetAddress());
				
				BufferedReader br = new BufferedReader(new InputStreamReader(sk.getInputStream()));
				
				String msg = br.readLine();
				
				System.out.println("보내준 메시지:" + msg);
				
				PrintWriter pw = new PrintWriter(sk.getOutputStream());
				
				pw.println(msg + "서버에서 문자열을 전송받았습니다.");
				pw.flush();
				
				pw.close();
				br.close();
				sk.close();
				
				
			}
			
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		
	}

}
