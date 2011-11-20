package swarm;
 
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import weibo4j.Friendships;
import weibo4j.Timeline;
import weibo4j.model.Status;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

public class GetUsers implements Runnable{
	 	
	public void getUserFriends(String uid, int depth, Connection conUsers) {
		if(depth == 6)			//根据六度分割理论，6层的递归深度应该够了
			return;
		System.out.println("******* depth: "+ depth +" *******");
		try {
			Thread.sleep(511);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<User> friendsList = null;
		Friendships fs = new Friendships();
		try {
			friendsList = fs.getFriendsByID(uid);
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		if(friendsList != null) {
			for(User friend: friendsList) { 
				if(friend != null) {
					System.out.println(" "+ friend.getName()+" ");
					if( (friend.getbiFollowersCount() > 5) && (friend.getStatusesCount() > 10) ) {
						PublicMethods.insertUserSql(friend, conUsers);
					}
					getUserFriends(friend.getId(), depth+1, conUsers); 
				}
			} 
		}
	} 
	
	/**
	 * @author swarm
	 * Firstly get some status from public timeline using getPublicTimeline()
	 * Secondly select the status whose user has larger friends number
	 * Thirdly choose this user to be the start user;
	 * @return the user id, which is used for the boost of getUser
	 */
	public String getStartUserId() {
		String startUserId = "2407207504";		//2407207504 is the user id of cnjswnagheng66@sina.com
		User startUser = null;
		Timeline timeline = new Timeline();
		List<Status> publicStatus = null;	 
		do {
			try {
				publicStatus = timeline.getPublicTimeline(10,0);
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while(publicStatus == null);		

		//this part is to select the status whose friends count is the largest
		if(publicStatus != null) {
			int maxFriNum = 0;
			int tempFriNum = 0;			
			User user = null;
			for(Status s: publicStatus) {
				if(s != null) {
					user = s.getUser();
					if( user != null) {
						tempFriNum = user.getFriendsCount();
						if(tempFriNum > maxFriNum) {
							maxFriNum = tempFriNum;
							startUser = user;			
						}
					} 
				}
			}
			startUserId = startUser.getId();
			System.out.println("the start user id is: "+startUserId);
		}
		
		return startUserId;
	}

		
	@Override
	public void run() {	 	

		Connection conUser = null;
		Statement stmt;
		ResultSet rset = null;
		int depth = 0; 
		String startUserId = "";
		startUserId = getStartUserId();		
		
		/**
		 * okay ,lets get connection with the database first
		 */
		System.out.println(" Will connect to the database"); 
		try { 
			 conUser = PublicMethods.getConnection(); 
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true){
			getUserFriends(startUserId, depth, conUser);
		}
	}
	
	 
}
