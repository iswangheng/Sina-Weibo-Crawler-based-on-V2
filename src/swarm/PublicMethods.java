package swarm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import weibo4j.model.Comment;
import weibo4j.model.User;
import weibo4j.model.Status;

public class PublicMethods {
	
	public static Connection getConnection() throws SQLException,java.lang.ClassNotFoundException 
	{
		Class.forName("com.mysql.jdbc.Driver");

		String url = "jdbc:mysql://localhost:3306/weibo"; 
		String username = "root";
		String password = "root";

		Connection con = DriverManager.getConnection(url, username, password);
		return con;
	}
	
	public static boolean updateUserStatus(Connection conUsers,Long userId)
	{
		try 
		{ 
			String insql = "update users set isStatusDone = "+true+" where id = "+userId;
			PreparedStatement ps = conUsers.prepareStatement(insql);    
			int result = ps.executeUpdate(); 
			if (result > 0)
				return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	 	
		return false;
	}
	
	public static boolean insertUserSql(User user,Connection conUsers) {
		try 
		{ 
			String insql = "insert ignore into users(id,screenName,province,city" +
					",location,description,url,profileImageUrl" +
					",userDomain,gender,followersCount,friendsCount" +
					",statusesCount,createdAt,verified,isRelationshipDone,isStatusDone) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement ps = conUsers.prepareStatement(insql);   
			ps.setLong(1, Long.parseLong(user.getId()));
			ps.setString(2, user.getScreenName());
			ps.setInt(3, user.getProvince());
			ps.setInt(4,  user.getCity());
			ps.setString(5, user.getLocation());
			ps.setString(6,  user.getDescription());
			String urlString = "";
			if(user.getURL() != null)
			{
				urlString = user.getURL().toString();
			}
			ps.setString(7,  urlString);
			String profileImageURL = "";
			if(user.getProfileImageURL() != null)
			{
				profileImageURL = user.getProfileImageURL().toString();
			}
			ps.setString(8,  profileImageURL);
			ps.setString(9,  user.getUserDomain());
			ps.setString(10,  user.getGender());
			ps.setInt(11,  user.getFollowersCount());
			ps.setInt(12,  user.getFriendsCount());
			ps.setInt(13,  user.getStatusesCount());
			ps.setString(14,  PublicMethods.dateToMySQLDateTimeString(user.getCreatedAt()));   
			ps.setBoolean(15,  user.isVerified());
			ps.setBoolean(16,false);
			ps.setBoolean(17,false);
	
			int result = ps.executeUpdate();
			if (result > 0)
				return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	 	
		return false;
	}
	
	public static boolean insertRelationshipSql(Connection conRelationship,Long friendId, Long userId)
	{
		try 
		{ 
			String insql = "insert into relationship(userId,followerId) values(?,?)";
			PreparedStatement ps = conRelationship.prepareStatement(insql);   
			ps.setLong(1, friendId);
			ps.setLong(2, userId); 
			int result = ps.executeUpdate(); 
			if (result > 0)
				return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	 	
		return false;
	}
	
	public static boolean updateUsersRelationship(Connection conUsers,Long userId)
	{
		try 
		{ 
			String insql = "update users set isRelationshipDone = "+true+" where id = "+userId;
			PreparedStatement ps = conUsers.prepareStatement(insql);    
			int result = ps.executeUpdate(); 
			if (result > 0)
				return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	 	
		return false;
	}
	
	public static boolean insertStatusSql(Connection conStatus,Status status) 
	{
		try 
		{
			String insql = "insert ignore into status(id,userScreenName,userId,createdAt,text,source,latitude,longitude,original_pic,retweeted_statusId,rtCounts,commentCounts,mid) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement ps = conStatus.prepareStatement(insql);
			ps.setLong(1, Long.parseLong(status.getId()));
			String userName = "";
			Long userId = (long)0;
			if(status.getUser() != null)
			{
				userName = status.getUser().getName();
				userId = Long.parseLong(status.getUser().getId());
			}
			ps.setString(2, userName);
			ps.setLong(3, userId);
			Date date = null;//new Date();
			date = status.getCreatedAt();
			String dateStr = "";
			if(date != null)
			{
				dateStr = PublicMethods.dateToMySQLDateTimeString(date);
			}
			else
			{
				date = new Date();
				dateStr = date.toString();
			}
			ps.setString(4, dateStr);
			ps.setString(5, status.getText()); 
			ps.setString(6, status.getSource().toString()); 
			ps.setDouble(7, status.getLatitude());
			ps.setDouble(8, status.getLongitude());
			ps.setString(9, status.getOriginalPic());
			long retweetStatusId = (long)0;
			Status retweetStatus = null;
			retweetStatus = status.getRetweetedStatus();
			if(retweetStatus != null)
			{
				String rtId = null;
				rtId = retweetStatus.getId();
				if(rtId != null)
				{
					retweetStatusId = Long.parseLong(rtId);
				} 
			}
			ps.setLong(10, retweetStatusId);
			ps.setInt(11, status.getRepostsCount());
			ps.setInt(12, status.getCommentsCount());
			ps.setString(13, status.getMid());

			int result = ps.executeUpdate(); 
			if (result > 0)
				return true;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
	public static boolean deleteUser(Connection conUsers,Long userId)
	{
		try 
		{ 
			String desql = "delete from users where id = "+userId;
			PreparedStatement ps = conUsers.prepareStatement(desql);    
			int result = ps.executeUpdate(); 
			if (result > 0)
				return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	 	
		return false;
	}
	
	public static boolean updateStatusComments(Connection conStatus,Long statusId)
	{
		try 
		{ 
			String insql = "update status set isDone = "+true+" where id = "+statusId;
			PreparedStatement ps = conStatus.prepareStatement(insql);    
			int result = ps.executeUpdate(); 
			if (result > 0)
				return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	 	
		return false;
	}
	
	public static boolean insertCommentsSql(Connection conComments,Comment comment, long statusId) 
	{
		try 
		{
			String insql = "insert ignore into comments(id,userScreenName,userId,createdAt,statusId,text,source) values(?,?,?,?,?,?,?)";
			PreparedStatement ps = conComments.prepareStatement(insql);
			ps.setLong(1, comment.getId());
			User user =null;
			long userId = (long)0;
			String userScreenName = "";
			user = comment.getUser();
			if(user != null)
			{
				userId = Long.parseLong(user.getId());
				userScreenName = user.getScreenName();
			}
			ps.setString(2, userScreenName);
			ps.setLong(3, userId);
			ps.setString(4, PublicMethods.dateToMySQLDateTimeString(comment.getCreatedAt()));
			ps.setLong(5, statusId);
			ps.setString(6, comment.getText());
			ps.setString(7, comment.getSource()); 
			int result = ps.executeUpdate(); 
			if (result > 0)
				return true;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public static String dateToMySQLDateTimeString(Date date)
	{
		final String[] MONTH = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
				"Jul", "Aug", "Sep", "Oct", "Nov", "Dec", };

		StringBuffer ret = new StringBuffer();
		String dateToString = date.toString(); // like
												// "Sat Dec 17 15:55:16 CST 2005"
		ret.append(dateToString.substring(24, 24 + 4));// append yyyy
		String sMonth = dateToString.substring(4, 4 + 3);
		for (int i = 0; i < 12; i++) 
		{ // append mm
			if (sMonth.equalsIgnoreCase(MONTH[i])) 
			{
				if ((i + 1) < 10)
					ret.append("-0");
				else
					ret.append("-");
				ret.append((i + 1));
				break;
			}
		}

		ret.append("-");
		ret.append(dateToString.substring(8, 8 + 2));
		ret.append(" ");
		ret.append(dateToString.substring(11, 11 + 8));

		return ret.toString();
	}
	
	public static String accessToken = "";
}
