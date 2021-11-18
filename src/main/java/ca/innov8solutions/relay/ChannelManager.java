package ca.innov8solutions.relay;

import ca.innov8solutions.relay.model.IslandRequest;
import ca.innov8solutions.relay.model.ServerObject;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class ChannelManager {

	private JedisPool pool;
	private final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).setPrettyPrinting().create();

	private int localIslandsLoaded = 0;

	public ChannelManager(JedisPool pool) {
		this.pool = pool;
	}

	public void init() {
		new Thread(() -> {
			pool.getResource().subscribe(new JedisPubSub() {
				@Override
				public void onMessage(String channel, String message) {

					/*
						Few notes:
						 - On prod it would check IP not port, and we can include the container name rather than comparing
						 IPS.

						 - Possibly make some sub channels for this
					 */

					if (channel.startsWith("sb-relay")) {
						String subChannel = channel.split(":")[1];

						if (subChannel.equalsIgnoreCase("loadIsland")) {
							System.out.println("Receiving load island request!");

							IslandRequest request = gson.fromJson(message, IslandRequest.class);

							if (request.getObject().getPort() != Bukkit.getServer().getPort()) {
								System.out.println("Request not meant for this server, ignoring");
								return;
							}

							if (localIslandsLoaded == 10) {
								System.out.println("Island load cannot be processed as it is full!");
								return;
							}

							System.out.println("Processing island load for: " + request.getUser());
							localIslandsLoaded++;

							ServerObject object = new ServerObject();
							object.setUsedSlots(localIslandsLoaded);
							object.setIp(Bukkit.getIp());
							object.setPort(Bukkit.getPort());
							sendSlotInfo(object);

							System.out.println("Sent server slot update!");
						}
					}
				}
			}, "sb-relay:loadIsland", "sb-relay:removeIsland", "sb-relay:shutdown");
		}, "jedis").start();
	}

	public void sendAddServer() {
		Jedis j = pool.getResource();

		ServerObject object = new ServerObject();
		object.setIp(Bukkit.getServer().getIp());
		object.setPort(Bukkit.getServer().getPort());

		j.publish("sb-relay:addServer", gson.toJson(object));
	}

	public void sendSlotInfo(ServerObject object) {
		Jedis j = pool.getResource();
		j.publish("sb-relay:slotUpdate", gson.toJson(object));
	}
}
