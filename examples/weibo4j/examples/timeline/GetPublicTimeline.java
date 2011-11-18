package weibo4j.examples.timeline;

import java.util.List;

import weibo4j.Timeline;
import weibo4j.Weibo;
import weibo4j.examples.Log;
import weibo4j.model.Status;
import weibo4j.model.WeiboException;
import weibo4j.util.WeiboConfig;

public class GetPublicTimeline {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String access_token = "bc2a9b7eae843e903f05862136d50102";
		Weibo weibo = new Weibo();
		weibo.setToken(access_token);
		Timeline tm = new Timeline();

		List<Status> status;

		for(int i =0; i < 10000;i++)
		{
			try {
					status = tm.getPublicTimeline();
					/*for(Status s : status){
						Log.logInfo(s.toString());
					}*/
					System.out.println("Number: "+i);
			} catch (WeiboException e) {
				e.printStackTrace();
			}

		}
	}

}
