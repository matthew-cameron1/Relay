package ca.innov8solutions.relay;

import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Relay extends JavaPlugin {

	private ChannelManager publisher;
	private JedisPool pool;

	private final int maxSlots = 10;

	@Override
	public void onEnable() {
		getServer().getLogger().info("Relay Enabled!");

		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(10000);
		this.pool = new JedisPool(new JedisPoolConfig(), "172.27.51.3"); // Have to use the vm host for some reason, thanks wsl

		this.publisher = new ChannelManager(pool);

		this.publisher.init();

		this.publisher.sendAddServer();
	}

	@Override
	public void onDisable() {
		getServer().getLogger().info("Relay Disabled!");

	}

	public ChannelManager getPublisher() {
		return publisher;
	}

	public int getMaxSlots() {
		return maxSlots;
	}
}
