package swarm;

import java.util.List;

import weibo4j.Timeline;
import weibo4j.examples.Log;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.WeiboException;

public class GetUserStatus {
	public void getUserStatus()
	{
		Timeline tm = new Timeline();
		try {
			//List<Status> status = tm.getUserTimeline(access_token);
			Paging pag = new Paging();
			pag.setPage(1);
			List<Status> status = tm.getUserTimeline("2093492691", "",
					50, pag, 0, 0);
			for(Status s : status){
				Log.logInfo(s.toString());
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
}
