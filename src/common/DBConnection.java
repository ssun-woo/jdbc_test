package common;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	// 데이터베이스에 연동만 하는 class
	// DB클라우드에 저장된 주소만 넣어두면 누가 써도 사용 가능
	// static으로 만들어진거기 떄문에 클래스이름.메소드이름으로 불러와서 사용가능

	public static Connection getConnection() throws Exception {

		Class.forName("oracle.jdbc.driver.OracleDriver");
		System.out.println("드라이브 로드 성공");

		String id = "java", pwd = "1234";
		String url = "jdbc:oracle:thin:@localhost:1521/xe";

		Connection con = DriverManager.getConnection(url, id, pwd);
		System.out.println("연결성공");

		return con;
	}
}
