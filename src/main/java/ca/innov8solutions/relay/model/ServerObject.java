package ca.innov8solutions.relay.model;

public class ServerObject {

	private String ip;
	private int port;

	private int usedSlots;
	private final int maxSlots = 10;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getUsedSlots() {
		return usedSlots;
	}

	public void setUsedSlots(int usedSlots) {
		this.usedSlots = usedSlots;
	}

	public int getMaxSlots() {
		return maxSlots;
	}
}
