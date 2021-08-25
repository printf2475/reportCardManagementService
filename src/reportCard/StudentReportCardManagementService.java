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
		System.out.println("ÇÁ·Î±×·¥Á¾·á");

	}

	private static void printDisplay() {
		System.out.println("----------------------------------");
		System.out.println("1. ÀÔ·Â | 2. Ãâ·Â |3. °Ë»ö | 4. ¼öÁ¤ | 5. Á¤·Ä | 6. »èÁ¦ | 7. Á¾·á ");
		System.out.println("----------------------------------");
		System.out.print("¹øÈ£ ¼±ÅÃ>");
	}

	/**
	 * startNum ºÎÅÍ lastNum±îÁö¸¸ ÀÔ·Â¹Þ°í ¼ýÀÚÀÎÁö Ã¼Å©
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
				System.out.println("Àß¸øµÈÀÔ·ÂÀÔ´Ï´Ù. ´Ù½ÃÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä");
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
				System.out.println("Àß¸øµÈ ¼ýÀÚ¸¦ ÀÔ·ÂÇÏ¿´½À´Ï´Ù. ´Ù½Ã ÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä.");
				return false;
			}
		} else {
			System.out.println("¼ýÀÚ¸¦ ÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä.");
			return false;
		}
	}

	private static boolean isAptselectNum(String selectnum, int startValue, int lastValue) {
		return Integer.parseInt(selectnum) >= startValue && Integer.parseInt(selectnum) <= lastValue;
	}

	private static boolean isNumberCheck(String inputNum) {
		return Pattern.matches("^[0-9]*$", inputNum);// Á¤±ÔÇ¥Çö½Ä
	}

	private static boolean isStringCheck(String inputString) {
		return Pattern.matches("^[°¡-ÆR]*$", inputString);// Á¤±ÔÇ¥Çö½Ä
	}

	private static void reportCardInsert() {
		Connection con = new DBUtility().getConnection();
		int count;
		if (con != null) {

			ReportCard card = enterStudentInfor();

			count = DBController.reportCardInsertTBL(card);

			if (count == 1) {
				System.out.println(card.getName() + "´Ô »ðÀÔ¼º°ø");
			} else {
				System.out.println(card.getName() + "´Ô »ðÀÔ½ÇÆÐ");
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
	 * @param check Å×ÀÌºí¿¡ Á¸ÀçÇÏ´Â °ªÀ» ÀÔ·Â¹Þ¾Æ¾ßÇÒ¶§ true
	 * @param check Å×ÀÌºí¿¡ Á¸ÀçÇÏÁö ¾Ê´Â °ªÀ» ÀÔ·Â¹Þ¾Æ¾ßÇÒ¶§ false
	 * 
	 * @return
	 */
	private static int enterStudentNumber(Boolean check) {
		int studentNo;

		while (true) {
			System.out.println("ÇÐ¹øÀ» ÀÔ·ÂÇÏ¼¼¿ä");

			try {
				studentNo = Integer.parseInt(scan.nextLine());
			} catch (Exception e) {
				System.out.println("Àß¸øµÈÀÔ·ÂÀÔ´Ï´Ù. ´Ù½ÃÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä");
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
				System.out.println("8ÀÚ¸® ¼ýÀÚ¸¦ ÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä.");
			}
		} else {
			System.out.println("¼ýÀÚ¸¦ ÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä");
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
				System.out.println("Á¸ÀçÇÏÁö ¾Ê´Â °ªÀÔ´Ï´Ù. ´Ù½Ã ÀÔ·ÂÇÏ¿© ÁÖ¼¼¿ä");
			}
			return false;
		} else {
			if (list.size() >= 1) {
				System.out.println("Á¸ÀçÇÏ´Â °ªÀÔ´Ï´Ù. ´Ù½Ã ÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä");
				return false;
			} else {
				return true;
			}
		}
	}

	private static String enterStudentName() {
		while (true) {
			System.out.println("ÀÌ¸§À» ÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä");
			String name = scan.nextLine();
			if (isAptNameCheck(name)) {
				if (name.length() <= 7) {
					return name;
				} else {
					System.out.println("ÀÌ¸§ÀÌ ³Ê¹« ±é´Ï´Ù ´Ù½Ã ÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä.");
				}
			}
		}
	}

	private static boolean isAptNameCheck(String name) {
		if (isStringCheck(name)) {
			if (name.length() > 2 || name.length() < 7) {
				return true;
			} else {
				System.out.println("±ÛÀÚ¼ö°¡ ³Ê¹« ¸¹½À´Ï´Ù. ´Ù½Ã ÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä.");
			}
		} else {
			System.out.println("ÇÑ±Û·Î ´Ù½Ã ÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä");
		}
		return false;
	}

	private static int enterAndroidScore() {
		final int STARTVALUE = 0, LASTVALUE = 100;
		int androidScore;
		while (true) {
			System.out.println("ÇÐ»ýÀÇ android Á¡¼ö¸¦ ÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä");
			try {
				androidScore = Integer.parseInt(scan.nextLine());
			} catch (Exception e) {
				System.out.println("Àß¸øµÈÀÔ·ÂÀÔ´Ï´Ù. ´Ù½ÃÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä");
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
			System.out.println("ÇÐ»ýÀÇ ÀÚ¹Ù Á¡¼ö¸¦ ÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä");

			try {
				javaScore = Integer.parseInt(scan.nextLine());
			} catch (Exception e) {
				System.out.println("Àß¸øµÈÀÔ·ÂÀÔ´Ï´Ù. ´Ù½ÃÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä");
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
			System.out.println("ÇÐ»ýÀÇ ÄÚÆ²¸° Á¡¼ö¸¦ ÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä");
			try {
				kotlinScore = Integer.parseInt(scan.nextLine());
			} catch (Exception e) {
				System.out.println("Àß¸øµÈÀÔ·ÂÀÔ´Ï´Ù. ´Ù½ÃÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä");
				continue;
			}

			if (isinputNumCheck(kotlinScore, STARTVALUE, LASTVALUE)) {
				return kotlinScore;
			}
		}
	}

	private static String enterGrade() {
		while (true) {
			System.out.println("µî±ÞÀ» ÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä");
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
			System.out.println("Á¸ÀçÇÏÁö ¾Ê´Â µî±ÞÀÔ´Ï´Ù.");
		} else {
			System.out.println("¿µ¾î¸¦ ÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä.");
		}
		return false;
	}

	private static void reportCardShowDB() {

		List<ReportCard> list = new ArrayList<ReportCard>();
		list = DBController.getList();
		System.out.println("¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à");
		for (ReportCard s : list) {
			System.out.println(s.toString());
		}
		System.out.println("¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à");
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
				System.out.println("¿Ã¹Ù¸¥ °ªÀ» ÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä");
			}
		}

		list = DBController.reportCardSearchTBL(searchData, selectnum);
		if (list.size() <= 0) {
			System.out.println("Ã£À» µ¥ÀÌÅÍ°¡ ¾ø½À´Ï´Ù.");
			return;
		}
		printTBL_List(list);
	}

	private static void printTBL_List(List<ReportCard> list) {
		System.out.println("¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à");
		for (ReportCard s : list) {
			System.out.println(s.toString());
		}
		System.out.println("¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à¡à");
	}

	private static void printSearchDisplay() {

		System.out.println("-------------------");
		System.out.println("1. ÇÐ¹ø | 2. ÀÌ¸§ | 3. µî±Þ | 4. Á¾·á");
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
			System.out.print("¼öÁ¤ÇÒ ");
			updateNumber = enterStudentNumber(true);

			printUpdateDisplay();
			selectNum = enterUpdateNumber();
			updateData = setUpdateData(selectNum);

			count = DBController.reportCardUpdateTBL(updateNumber, selectNum, updateData);
			if (count == 1) {
				System.out.println("¼öÁ¤¼º°ø");

			} else {
				System.out.println("¼öÁ¤½ÇÆÐ");
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
			System.out.print("¼öÁ¤ÇÒ ");
			updateData = enterStudentName();
			return updateData;
		case JAVA:
		case ANDROID:
		case KOTLIN:
			while (!false) {
				System.out.println("Á¡¼ö¸¦ ÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä ");

				try {
					updateData = scan.nextLine();
					intdata = Integer.parseInt(updateData);
				} catch (Exception e) {
					System.out.println("Àß¸øµÈÀÔ·ÂÀÔ´Ï´Ù. ´Ù½ÃÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä");
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
		System.out.println("1. ÀÌ¸§ ¼öÁ¤ | 2. ÀÚ¹Ù Á¡¼ö ¼öÁ¤| 3. ¾Èµå·ÎÀÌµå Á¡¼ö ¼öÁ¤ | 4. ÄÚÆ²¸° Á¡¼ö ¼öÁ¤");
		System.out.println("-----------------------------------------");
	}

	private static int enterUpdateNumber() {
		final int STARTNUM = 0, LASTNUM = 100;
		int selectNum;
		while (!false) {
			try {
				selectNum = Integer.parseInt(scan.nextLine());
			} catch (Exception e) {
				System.out.println("Àß¸øµÈÀÔ·ÂÀÔ´Ï´Ù. ´Ù½ÃÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä");
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
				System.out.println("Àß¸øµÈÀÔ·ÂÀÔ´Ï´Ù. ´Ù½ÃÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä");
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
				System.out.println("Àß¸øµÈÀÔ·ÂÀÔ´Ï´Ù. ´Ù½ÃÀÔ·ÂÇÏ¿©ÁÖ¼¼¿ä");
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
		System.out.println("1. ÇÐ¹ø Á¤·Ä | 2. ÀÌ¸§Á¤·Ä | 3. Æò±ÕÁ¤·Ä ");
		System.out.println("---------------------");
	}

	private static void printSortDisplay() {
		System.out.println("-------------------");
		System.out.println("1. ¿À¸§Â÷¼ø Á¤·Ä | 2. ³»¸²Â÷¼ø Á¤·Ä");
		System.out.println("-------------------");
	}

	private static void reportCardDelete() {
		List<ReportCard> list = new ArrayList<ReportCard>();
		final int NUMBERCHECK = 1;
		int count;
		int deleteNum;
		String strNumber;
	
		System.out.print("»èÁ¦ÇÒ ");
		deleteNum = enterStudentNumber(true);
		strNumber = String.valueOf(deleteNum);
			count = DBController.reportCardDeleteTBL(deleteNum);
			if (count != 0) {
				System.out.println("»èÁ¦¼º°ø");
			} else {
				System.out.println("»èÁ¦½ÇÆÐ");
			}
	}
}
