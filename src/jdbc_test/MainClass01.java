package jdbc_test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

import common.DBConnection;

class MemberDTO {
	private int age;
	private String name, id;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}

class DB {

	Connection con; // DB에 접속하기 위한 클래스
	PreparedStatement ps; // DB명령어 전송 객체
	ResultSet rs; // DB 명령어 후 결과 얻어오는 객체

	/*
	 * public DB() { try { Class.forName("oracle.jdbc.driver.OracleDriver"); // 외울
	 * 필요는 없음 하지만 오타가 발생하면 안됨 System.out.println("드라이브 로드 성공");
	 * 
	 * String id = "java", pwd = "1234"; String url =
	 * "jdbc:oracle:thin:@localhost:1521/xe"; // oracle에 대한 정보
	 * 
	 * con = DriverManager.getConnection(url, id, pwd); // 이곳에는 오라클 주소와 id, pwd 정보를
	 * 넣어줘야함 System.out.println("연결성공");
	 * 
	 * // 연결성공이라는 메시지가 콘솔에 출력돼야 함
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } }
	 */

	public DB() {
		try {
			con = DBConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// 위의 주석처리된

	public void select() {
		String sql = "select * from newst";
		// 처음부터 이렇게 작성하는게 아닌 쿼리문이 sqldeveloper에서 문제없이 실행되는지 확인 후 java로 가져옴(; 는 뺴고 가져옴)
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery(); // select는 executeQuery를 사용해야함
									// 반환타입인 ResultSet타입의 rs로 그 결과를 받아옴

			// 여기서 rs값은 iterator처럼 bof값을 참조한다

			while (rs.next()) {
//			System.out.println(rs.next());
				System.out.println("--------------");
				System.out.println(rs.getString("id"));
				System.out.println(rs.getString("name"));
				System.out.println(rs.getInt("age"));
				System.out.println("--------------");
			}

//			System.out.println(rs.next());
//			System.out.println(rs.getString("id"));
//			System.out.println(rs.getString("name"));
//			System.out.println(rs.getInt("age"));
//			
//			System.out.println(rs.next());
//			System.out.println(rs.getString("id"));
//			System.out.println(rs.getString("name"));
//			System.out.println(rs.getInt("age"));
//			
//			System.out.println(rs.next());
			// 여기서부터는 false가 나옴(eof값이기 때문에)

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ArrayList<MemberDTO> select_2() {
		String sql = "select * from newst";
		ArrayList<MemberDTO> list = new ArrayList<>();
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				// 이제 여기서 직접 출력하는게 아닌 dto 객체에 넣어서
				// 그 dto를 arraylist에 넣어서 저장
				// 출력은 main에서 함
				MemberDTO dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setAge(rs.getInt("age"));

				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public MemberDTO search(String id) {
		String sql = "select * from newst where id = '" + id + "'";
		MemberDTO dto = null;
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setAge(rs.getInt("age"));
//				System.out.println(rs.getString("id"));
//				System.out.println(rs.getString("name"));
//				System.out.println(rs.getInt("age"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	public int register(MemberDTO dd) {
		// String sql = "insert into newst values('" + dd.getId() + "', '" +
		// dd.getName() + "'," + dd.getAge() + ")";
		// 이렇게하면 쿼리문이 너무 길어짐
		String sql = "insert into newst(id, name, age) values(?,?,?)";
		int result = 0;
		try {
			ps = con.prepareStatement(sql);

			ps.setString(1, dd.getId());
			ps.setString(2, dd.getName());
			ps.setInt(3, dd.getAge());
			// ? 에 대한값을 이렇게 입력 할 수 있음

			// ps.executeQuery();
			// select문은 executeQuery();
			// select를 제외한 모든 값은 executeUpdate 사용

			result = ps.executeUpdate();
			// 결과값을 int 형태로 가져옴
			// executeQuery도 오류는 발생하지 않음

		} catch (Exception e) {
			// e.printStackTrace();
		}
		return result;
	}

	public int delete(String id) {
		String sql = "delete from newst where id = ?";
		int result = 0;
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, id);
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}

public class MainClass01 {
	public static void main(String[] args) {

		DB db = new DB();
		// 바로 실행하면 오류가 발생함

		// ojdbc라는 라이브러리를 추가해줘야함
		// win+e로 c드라이브 이동
		// C:\oraclexe 이 경로로 이동
		// C:\oraclexe\app\oracle\product\11.2.0\server\jdbc\lib 안에 ojdbc6.jar 파일
		// D:\0406 취업반\sqldeveloper\jdbc\lib 이쪽에 8버전이 있는데 이걸 사용해도 상관없음

		// 이후 프로젝트 우클릭 제일아래 properties 들어감
		// java build path 클릭 >> Libraries 탭에 classpath 클릭 후 add external jars 클릭
		// 위의 ojdbc6.jar or ojdbc8.jar 추가해주기

		// 이후 다시 ctrl + F11을 누르면 오류발생 X

		// db.select();
		// rs.next값이 있기 때문에 true 반환
		// 이후에 rs.getString rs.getInt로 내부의 값을 얻어옴

		Scanner scan = new Scanner(System.in);
		int num, age = 0;
		String id = null, name = null;
		while (true) {
			System.out.println("1. 모든 사용자 보기");
			System.out.println("2. 검색");
			System.out.println("3. 회원가입");
			System.out.println("4. 회원삭제");
			num = scan.nextInt();
			switch (num) {
			case 1:
				ArrayList<MemberDTO> list = db.select_2();
				System.out.println("id\tname\tage");
				System.out.println("====================");
				for (MemberDTO m : list) {
					System.out.println(m.getId() + "\t" + m.getName() + "\t" + m.getAge());
					System.out.println("--------------------");
				}
				break;
			case 2:
				System.out.println("검색 아이디 입력 : ");
				id = scan.next();
				MemberDTO d = db.search(id);
				if (d == null) {
					System.out.println("찾는 회원이 없습니다");
				} else {
					System.out.println("id : " + d.getId());
					System.out.println("name : " + d.getName());
					System.out.println("age : " + d.getAge());
				}
				break;

			case 3:
				System.out.println("회원가입");
				System.out.print("아이디 입력 : ");
				id = scan.next();
				System.out.print("이름 입력 : ");
				name = scan.next();
				System.out.print("나이 입력 : ");
				age = scan.nextInt();
				MemberDTO dd = new MemberDTO();
				dd.setId(id);
				dd.setAge(age);
				dd.setName(name);

				int result = db.register(dd);
				if (result == 0) {
					System.out.println("동일한 아이디가 존재합니다");
				} else {
					System.out.println("회원 가입을 축하합니다");
				}
				break;

			case 4:
				System.out.println("회원 삭제");
				System.out.print("삭제할 회원의 아이디를 입력하세요 : ");
				id = scan.next();

				result = db.delete(id);

				if (result == 0) {
					System.out.println("존재하지 않는 회원입니다");
				} else {
					System.out.println("회원이 삭제됐습니다");
				}
				break;
			}
		}

	}
}
