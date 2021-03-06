package reportCard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class StudentReportCardManagementService {
	public static final int INSERT = 1, SHOWDB = 2, SEARCH = 3, UPDATE = 4, SORT = 5, DELETE = 6, FINISH = 7;
	public static final Scanner scan = new Scanner(System.in);

	public static void main(String[] args) {
		final int STARTNUM = 1, LASTNUM = 7;
		boolean flag = false;
		int selectNum;

		while (!flag) {

			printDisplay();
			selectNum = enterSelectNum(STARTNUM, LASTNUM);
			switch (selectNum) {
			case INSERT:
				reportCardInsert();
				break;
			case SHOWDB:
				reportCardShowDB();
				break;
			case SEARCH:
				reportCardSearch();
				break;
			case UPDATE:
				reportCardUpdate();
				break;
			case SORT:
				reportCardSort();
				break;
			case DELETE:
				reportCardDelete();
				break;
			case FINISH:
				flag = true;
				break;

			}
		}
		System.out.println("프로그램종료");

	}

	private static void printDisplay() {
		System.out.println("----------------------------------");
		System.out.println("1. 입력 | 2. 출력 |3. 검색 | 4. 수정 | 5. 정렬 | 6. 삭제 | 7. 종료 ");
		System.out.println("----------------------------------");
		System.out.print("번호 선택>");
	}

	/**
	 * startNum 부터 lastNum까지만 입력받고 숫자인지 체크
	 * 
	 * @return
	 */
	private static int enterSelectNum(int startNum, int lastNum) {

		int selectCheckedNum;
		boolean flag = false;
		int selectNum;

		while (!flag) {
			try {
				selectNum = Integer.parseInt(scan.nextLine());
			} catch (Exception e) {
				System.out.println("잘못된입력입니다. 다시입력하여주세요");
				continue;
			}

			if (isinputNumCheck(selectNum, startNum, lastNum)) {
				return selectNum;
			}
		}
		return 0;
	}

	private static boolean isinputNumCheck(int inputNum, int startValue, int lastValue) {
		String strInputNum = String.valueOf(inputNum);
		if (isNumberCheck(strInputNum)) {
			if (isAptselectNum(strInputNum, startValue, lastValue)) {
				return true;
			} else {
				System.out.println("잘못된 숫자를 입력하였습니다. 다시 입력하여주세요.");
				return false;
			}
		} else {
			System.out.println("숫자를 입력하여주세요.");
			return false;
		}
	}

	private static boolean isAptselectNum(String selectnum, int startValue, int lastValue) {
		return Integer.parseInt(selectnum) >= startValue && Integer.parseInt(selectnum) <= lastValue;
	}

	private static boolean isNumberCheck(String inputNum) {
		return Pattern.matches("^[0-9]*$", inputNum);// 정규표현식
	}

	private static boolean isStringCheck(String inputString) {
		return Pattern.matches("^[가-힣]*$", inputString);// 정규표현식
	}

	private static void reportCardInsert() {
		Connection con = new DBUtility().getConnection();
		int count;
		if (con != null) {

			ReportCard card = enterStudentInfor();

			count = DBController.reportCardInsertTBL(card);

			if (count == 1) {
				System.out.println(card.getName() + "님 삽입성공");
			} else {
				System.out.println(card.getName() + "님 삽입실패");
			}

		}
	}

	private static ReportCard enterStudentInfor() {
		int studentNum = enterStudentNumber(false);
		String studentName = enterStudentName();
		int studentJavaScore = enterJavaScore();
		int studentKotlinScore = enterKotlinScore();
		int studentAndroid = enterAndroidScore();

		return new ReportCard(studentNum, studentName, studentJavaScore, studentKotlinScore, studentAndroid);
	}

	/**
	 * 
	 * @param check 테이블에 존재하는 값을 입력받아야할때 true
	 * @param check 테이블에 존재하지 않는 값을 입력받아야할때 false
	 * 
	 * @return
	 */
	private static int enterStudentNumber(Boolean check) {
		int studentNo;

		while (true) {
			System.out.println("학번을 입력하세요");

			try {
				studentNo = Integer.parseInt(scan.nextLine());
			} catch (Exception e) {
				System.out.println("잘못된입력입니다. 다시입력하여주세요");
				continue;
			}
			if (isAptStudentNumberCheck(studentNo, check)) {
				return studentNo;
			}
		}
	}

	private static boolean isAptStudentNumberCheck(int studentNo, boolean checkFlag) {
		String strStudentNo = String.valueOf(studentNo);

		if (isNumberCheck(strStudentNo)) {
			if (strStudentNo.length() == 8) {
				return isStudentNumOverlapCheck(studentNo, checkFlag);

			} else {
				System.out.println("8자리 숫자를 입력하여주세요.");
			}
		} else {
			System.out.println("숫자를 입력하여주세요");
		}
		return false;
	}

	private static boolean isStudentNumOverlapCheck(int number, boolean onFlag) {
		List<ReportCard> list = new ArrayList<ReportCard>();
		final int NUMBERCHECK = 1;
		String strNumber = String.valueOf(number);
		list = DBController.reportCardSearchTBL(strNumber, NUMBERCHECK);
		if (onFlag == true) {
			if (list.size() >= 1) {
				return true;
			} else {
				System.out.println("존재하지 않는 값입니다. 다시 입력하여 주세요");
			}
			return false;
		} else {
			if (list.size() >= 1) {
				System.out.println("존재하는 값입니다. 다시 입력하여주세요");
				return false;
			} else {
				return true;
			}
		}
	}

	private static String enterStudentName() {
		while (true) {
			System.out.println("이름을 입력하여주세요");
			String name = scan.nextLine();
			if (isAptNameCheck(name)) {
				if (name.length() <= 7) {
					return name;
				} else {
					System.out.println("이름이 너무 깁니다 다시 입력하여주세요.");
				}
			}
		}
	}

	private static boolean isAptNameCheck(String name) {
		if (isStringCheck(name)) {
			if (name.length() > 2 || name.length() < 7) {
				return true;
			} else {
				System.out.println("글자수가 너무 많습니다. 다시 입력하여주세요.");
			}
		} else {
			System.out.println("한글로 다시 입력하여주세요");
		}
		return false;
	}

	private static int enterAndroidScore() {
		final int STARTVALUE = 0, LASTVALUE = 100;
		int androidScore;
		while (true) {
			System.out.println("학생의 android 점수를 입력하여주세요");
			try {
				androidScore = Integer.parseInt(scan.nextLine());
			} catch (Exception e) {
				System.out.println("잘못된입력입니다. 다시입력하여주세요");
				continue;
			}
			if (isinputNumCheck(androidScore, STARTVALUE, LASTVALUE)) {
				return androidScore;
			}
		}
	}

	private static int enterJavaScore() {
		final int STARTVALUE = 0, LASTVALUE = 100;
		int javaScore;
		while (true) {
			System.out.println("학생의 자바 점수를 입력하여주세요");

			try {
				javaScore = Integer.parseInt(scan.nextLine());
			} catch (Exception e) {
				System.out.println("잘못된입력입니다. 다시입력하여주세요");
				continue;
			}
			if (isinputNumCheck(javaScore, STARTVALUE, LASTVALUE)) {
				return javaScore;
			}

		}
	}

	private static int enterKotlinScore() {
		final int STARTVALUE = 0, LASTVALUE = 100;
		int kotlinScore;
		while (true) {
			System.out.println("학생의 코틀린 점수를 입력하여주세요");
			try {
				kotlinScore = Integer.parseInt(scan.nextLine());
			} catch (Exception e) {
				System.out.println("잘못된입력입니다. 다시입력하여주세요");
				continue;
			}

			if (isinputNumCheck(kotlinScore, STARTVALUE, LASTVALUE)) {
				return kotlinScore;
			}
		}
	}

	private static String enterGrade() {
		while (true) {
			System.out.println("등급을 입력하여주세요");
			String grade = scan.nextLine();
			if (isAptGradeCheck(grade)) {
				return grade.toUpperCase();
			}
		}
	}

	private static boolean isAptGradeCheck(String grade) {
		String upperGrade = grade.toUpperCase();
		if (isStringCheck(upperGrade)) {
			if (upperGrade.equals("A") || upperGrade.equals("B") || upperGrade.equals("C") || upperGrade.equals("D")
					|| upperGrade.equals("F")) {
				return true;
			}
			System.out.println("존재하지 않는 등급입니다.");
		} else {
			System.out.println("영어를 입력하여주세요.");
		}
		return false;
	}

	private static void reportCardShowDB() {

		List<ReportCard> list = new ArrayList<ReportCard>();
		list = DBController.getList();
		System.out.println("□□□□□□□□□□□□□□□□□□□□□□□□□□□□□");
		for (ReportCard s : list) {
			System.out.println(s.toString());
		}
		System.out.println("□□□□□□□□□□□□□□□□□□□□□□□□□□□□□");
	}

	private static void reportCardSearch() {
		final int NUMBER = 1, NAME = 2, GRADE = 3, EXIT = 4;
		final int STARTNUM = 1, LASTNUM = 4;
		List<ReportCard> list = new ArrayList<ReportCard>();

		String searchData = null;
		int selectnum = 0;
		boolean flag = false;
		while (!flag) {
			printSearchDisplay();
			selectnum = enterSelectNum(STARTNUM, LASTNUM);
			switch (selectnum) {
			case NUMBER:
				searchData = String.valueOf(enterStudentNumber(true));
				flag = true;
				break;
			case NAME:
				searchData = enterStudentName();
				flag = true;
				break;
			case GRADE:
				searchData = enterGrade();
				flag = true;
				break;
			case EXIT:
				return;

			default:
				System.out.println("올바른 값을 입력하여주세요");
			}
		}

		list = DBController.reportCardSearchTBL(searchData, selectnum);
		if (list.size() <= 0) {
			System.out.println("찾을 데이터가 없습니다.");
			return;
		}
		printTBL_List(list);
	}

	private static void printTBL_List(List<ReportCard> list) {
		System.out.println("□□□□□□□□□□□□□□□□□□□□□□□□□□□□□");
		for (ReportCard s : list) {
			System.out.println(s.toString());
		}
		System.out.println("□□□□□□□□□□□□□□□□□□□□□□□□□□□□□");
	}

	private static void printSearchDisplay() {

		System.out.println("-------------------");
		System.out.println("1. 학번 | 2. 이름 | 3. 등급 | 4. 종료");
		System.out.println("-------------------");

	}

	private static void reportCardUpdate() {
		List<ReportCard> list = new ArrayList<ReportCard>();
		final int NUMBERCHECK = 1;
		int updateNumber;
		int selectNum;
		String updateData;
		int count;
		while (!false) {
			System.out.print("수정할 ");
			updateNumber = enterStudentNumber(true);

			printUpdateDisplay();
			selectNum = enterUpdateNumber();
			updateData = setUpdateData(selectNum);

			count = DBController.reportCardUpdateTBL(updateNumber, selectNum, updateData);
			if (count == 1) {
				System.out.println("수정성공");

			} else {
				System.out.println("수정실패");
			}

			break;
		}
	}

	private static String setUpdateData(int selectNum) {
		final int NAME = 1, JAVA = 2, ANDROID = 3, KOTLIN = 4;
		final int STARTNUM = 0, LASTNUM = 100;
		String updateData = null;
		int intdata;

		switch (selectNum) {
		case NAME:
			System.out.print("수정할 ");
			updateData = enterStudentName();
			return updateData;
		case JAVA:
		case ANDROID:
		case KOTLIN:
			while (!false) {
				System.out.println("점수를 입력하여주세요 ");

				try {
					updateData = scan.nextLine();
					intdata = Integer.parseInt(updateData);
				} catch (Exception e) {
					System.out.println("잘못된입력입니다. 다시입력하여주세요");
					continue;
				}

				if (isinputNumCheck(intdata, STARTNUM, LASTNUM)) {
					return updateData;
				}
			}
		}
		return null;

	}

	private static void printUpdateDisplay() {
		System.out.println("-----------------------------------------");
		System.out.println("1. 이름 수정 | 2. 자바 점수 수정| 3. 안드로이드 점수 수정 | 4. 코틀린 점수 수정");
		System.out.println("-----------------------------------------");
	}

	private static int enterUpdateNumber() {
		final int STARTNUM = 0, LASTNUM = 100;
		int selectNum;
		while (!false) {
			try {
				selectNum = Integer.parseInt(scan.nextLine());
			} catch (Exception e) {
				System.out.println("잘못된입력입니다. 다시입력하여주세요");
				continue;
			}

			if (isinputNumCheck(selectNum, STARTNUM, LASTNUM)) {
				return selectNum;
			}
		}

	}

	private static void reportCardSort() {
		int sortField = 0;
		int sortOption = 0;
		while (!false) {
			final int STARTNUM = 1, LASTNUM = 3;
			printSortSelectDisplay();

			try {
				sortField = Integer.parseInt(scan.nextLine());
			} catch (Exception e) {
				System.out.println("잘못된입력입니다. 다시입력하여주세요");
				continue;
			}

			if (isinputNumCheck(sortField, STARTNUM, LASTNUM)) {
				break;
			}
		}

		while (!false) {
			final int STARTNUM = 1, LASTNUM = 2;
			printSortDisplay();
			try {
				sortOption = Integer.parseInt(scan.nextLine());
			} catch (Exception e) {
				System.out.println("잘못된입력입니다. 다시입력하여주세요");
				continue;
			}

			if (isinputNumCheck(sortOption, STARTNUM, LASTNUM)) {
				break;
			}
		}
		DBController.setSortQuery(sortField, sortOption);
	}

	private static void printSortSelectDisplay() {
		System.out.println("---------------------");
		System.out.println("1. 학번 정렬 | 2. 이름정렬 | 3. 평균정렬 ");
		System.out.println("---------------------");
	}

	private static void printSortDisplay() {
		System.out.println("-------------------");
		System.out.println("1. 오름차순 정렬 | 2. 내림차순 정렬");
		System.out.println("-------------------");
	}

	private static void reportCardDelete() {
		List<ReportCard> list = new ArrayList<ReportCard>();
		final int NUMBERCHECK = 1;
		int count;
		int deleteNum;
		String strNumber;
	
		System.out.print("삭제할 ");
		deleteNum = enterStudentNumber(true);
		strNumber = String.valueOf(deleteNum);
			count = DBController.reportCardDeleteTBL(deleteNum);
			if (count != 0) {
				System.out.println("삭제성공");
			} else {
				System.out.println("삭제실패");
			}
	}
}
