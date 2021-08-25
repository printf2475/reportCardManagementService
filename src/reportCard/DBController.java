package reportCard;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBController {
	public static final int NUMBER = 1, NAME = 2, JAVA=3, ANDROID=4, KOTLIN=5, TOTALSCORE=6, AVG=7, GRADE=8;
	static List<ReportCard> DBconList = new ArrayList<ReportCard>();
	static String sortQuary="";
	
	public static int reportCardInsertTBL(ReportCard reportCard) {
		Connection connection = new DBUtility().getConnection();
		PreparedStatement preparedStatement = null;
		String insertQuery = "call procedure_insertCal_reportCardTBL(?, ?, ?, ?, ?)";
		int count = 0;
		try {
			preparedStatement = connection.prepareStatement(insertQuery);
			preparedStatement.setInt(1, reportCard.getNumber());
			preparedStatement.setString(2, reportCard.getName());
			preparedStatement.setInt(3, reportCard.getJavaScore());
			preparedStatement.setInt(4, reportCard.getAndroidScore());
			preparedStatement.setInt(5, reportCard.getKotlinScore());
			count = preparedStatement.executeUpdate();
			DBconList=getList();			
			return count;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null && !preparedStatement.isClosed())
					preparedStatement.close();
				if (connection != null && !connection.isClosed())
					connection.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return count;
		
	}

	public static List<ReportCard> reportCardSearchTBL(String searchData, int searchNum) {
		final int NUMBER=1, NAME=2, GRADE=3;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String searchQuery;
		List<ReportCard> list = new ArrayList<ReportCard>();

		switch (searchNum) {
		case NUMBER: searchQuery = "select * from reportcardtbl where s_num like ?"; break;
			
		case NAME: searchQuery = "select * from reportcardtbl where s_name like ?"; break;
		
		case GRADE :  searchQuery ="select * from reportcardtbl where s_grade like ?"; break;
		
		default: System.out.println("올바르지않은 입력값입니다. 다시입력하여주세요");  return list;
		}

		con = new DBUtility().getConnection();
		if (con != null) {

			try {
				preparedStatement = con.prepareStatement(searchQuery);
				preparedStatement.setString(1, searchData);
				resultSet = preparedStatement.executeQuery();

				if (!resultSet.isBeforeFirst()) {
					return list;
				}
			

				while (resultSet.next()) {
					int studentNum = resultSet.getInt(NUMBER);
					String studentName = resultSet.getString(NAME);
					int studentJavaScore = resultSet.getInt(JAVA);
					int studentAndroid = resultSet.getInt(ANDROID);
					int studentKotlinScore = resultSet.getInt(KOTLIN);
					int studentTotalScore = resultSet.getInt(TOTALSCORE);
					double avg=resultSet.getDouble(AVG);
					char grade = 0;
					try {
						grade = (char)resultSet.getCharacterStream(DBController.GRADE).read();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ReportCard reportCard = new ReportCard(studentNum, studentName, studentJavaScore, studentAndroid, studentKotlinScore, studentTotalScore, avg, grade);
					list.add(reportCard);
					
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (preparedStatement != null && !preparedStatement.isClosed()) {
						preparedStatement.close();
					}
					if (con != null && !con.isClosed()) {
						con.close();
					}
				} catch (SQLException e) {}
			}
		}
		return list;
	}
	
	public static int reportCardUpdateTBL(int updateNumber, int selectNum ,String updateData) {
		final int NAME=1, JAVA=2, ANDROID=3, KOTLIN=4;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		int count;
		con = new DBUtility().getConnection();
		String updateQuery ;
		String query = "call procedure_updateScoreCal_reportCardTBL(?)";
		
		switch (selectNum) {
		case NAME: updateQuery =  "update reportcardtbl set s_name = ? where s_num = ?"; break;
			
		case JAVA: updateQuery =  "update reportcardtbl set s_javaScore = ? where s_num = ?"; break;
		
		case ANDROID :  updateQuery = "update reportcardtbl set s_androidScore = ? where s_num = ?"; break;
		
		case KOTLIN :  updateQuery = "update reportcardtbl set s_kotlinScore = ? where s_num = ?"; break;

		default: System.out.println("올바르지않은 입력값입니다. 다시입력하여주세요"); return 0;
		}
		
		
		
		if (con != null) {
			try {
				preparedStatement = con.prepareStatement(updateQuery);
				preparedStatement.setString(1, updateData);
				preparedStatement.setInt(2, updateNumber);
				count =  preparedStatement.executeUpdate();
				
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setInt(1, updateNumber);
				count =  preparedStatement.executeUpdate();
				DBconList=getList();
				return  count;
				

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (preparedStatement != null && !preparedStatement.isClosed()) {
						preparedStatement.close();
					}
					if (con != null && !con.isClosed()) {
						con.close();
					}
				} catch (SQLException e) {
				}
			}
		}
		return 0;
	}
	
	public static int reportCardDeleteTBL(int deleteNum) {
		boolean flag = false;
		Connection con = new DBUtility().getConnection();
		ResultSet  resultSet;
		int count = 0;
		boolean studentNum = false;
		PreparedStatement preparedStatement = null;
		String deleteQuery = "select function_reportcardTBL_Delete(?)";
		if (con != null) {
			try {
				preparedStatement = con.prepareStatement(deleteQuery);
				preparedStatement.setInt(1, deleteNum);
				resultSet=preparedStatement.executeQuery();
				DBconList=getList();
				while (resultSet.next()) {
					studentNum = resultSet.getBoolean(1);
				}
				if(studentNum) {
					count=1;
				}else {
					count=0;
				}
				
				return count ;
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (preparedStatement != null && !preparedStatement.isClosed()) {
						preparedStatement.close();
					}
					if (con != null && !con.isClosed()) {
						con.close();
					}

				} catch (SQLException e) {
				}
			}
		}
		return 0;
	}

	public static List<ReportCard> getList() {
		List<ReportCard> list = new ArrayList<ReportCard>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		con = new DBUtility().getConnection();
		if (con != null) {

			ResultSet resultSet = null;

			String selectQuery = "select * from reportcardtbl "+sortQuary;
			try {
				preparedStatement = con.prepareStatement(selectQuery);
				resultSet = preparedStatement.executeQuery();

				if (!resultSet.isBeforeFirst()) {
					System.out.println("찾으시는 데이터가 없습니다.");
					return DBconList;
				}
				DBconList.removeAll(DBconList);
				while (resultSet.next()) {
					int studentNum = resultSet.getInt(NUMBER);
					String studentName = resultSet.getString(NAME);
					int studentJavaScore = resultSet.getInt(JAVA);
					int studentAndroid = resultSet.getInt(ANDROID);
					int studentKotlinScore = resultSet.getInt(KOTLIN);
					int studentTotalScore = resultSet.getInt(TOTALSCORE);
					double avg=resultSet.getDouble(AVG);
					char grade = 0;
					try {
						grade = (char)resultSet.getCharacterStream(GRADE).read();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ReportCard reportCard = new ReportCard(studentNum, studentName, studentJavaScore, studentAndroid, studentKotlinScore, studentTotalScore, avg, grade);
					
					DBconList.add(reportCard);
				}
				return DBconList;
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (preparedStatement != null && !preparedStatement.isClosed()) {
						preparedStatement.close();
					}
					if (con != null && !con.isClosed()) {
						con.close();
					}

				} catch (SQLException e) {
				}
			}
		}
		return null;
	}

	public static void setSortQuery(int setSortField , int setSortOption) {
		final int NUMBER=1, NAME=2, AVG=3, ASC=1, DESC=2;
		switch(setSortField) {
		case NUMBER: sortQuary="order by s_num "; break;
		case NAME: 	sortQuary="order by s_name "; break;
		case AVG: 		sortQuary="order by s_avg "; break;
		default : 			sortQuary+="";
		}
		if(setSortOption==ASC) {
			sortQuary+="asc";
		}
		
		if(setSortOption==DESC) {
			sortQuary+="desc";
		}
		
		
		
	}
}
