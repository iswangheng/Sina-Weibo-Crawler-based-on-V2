package swarm;

public class PublicMethods {
	public boolean setToken(String token)
	{
		boolean isCorrect = false;
		accessToken = token;
		if(accessToken != null)
		{
			isCorrect = true;
		}
		
		return isCorrect;
	}
	public String getToken()
	{
		return accessToken;
	}
	private String accessToken = "";
}
