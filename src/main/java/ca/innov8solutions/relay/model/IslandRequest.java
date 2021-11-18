package ca.innov8solutions.relay.model;

public class IslandRequest {
	private String user;
	private ServerObject object;

	public IslandRequest(String user, ServerObject object) {
		this.user = user;
		this.object = object;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public ServerObject getObject() {
		return object;
	}

	public void setObject(ServerObject object) {
		this.object = object;
	}
}
