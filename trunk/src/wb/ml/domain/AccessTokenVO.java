package wb.ml.domain;

public class AccessTokenVO {
	private static AccessTokenVO accessTokenVO=null;
	private Long currentTime;
	private String accessToken;
	private Long expiresIn; 
	private String refreshToken;
	private String scope=null;
	
	private AccessTokenVO(){}
	
	public static AccessTokenVO getInstance() {
		if(accessTokenVO == null){
			accessTokenVO = new AccessTokenVO();
		}
		return accessTokenVO;
	}
	
	public Long getCurrentTime() {
		return currentTime;
	}
	
	public void setCurrentTime(Long currentTime) {
		this.currentTime = currentTime;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Long getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
