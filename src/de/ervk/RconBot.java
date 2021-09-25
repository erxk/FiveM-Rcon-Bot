package de.ervk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import javax.security.auth.login.LoginException;

import de.ervk.listener.CommandListener;
import de.ervk.listener.ReactionListener;
import de.ervk.manage.LiteSQL;
import de.ervk.manage.SQLManager;
import de.ervk.rcon.Rcon;
import de.ervk.rcon.exceptions.AuthenticationException;
import de.ervk.utils.Config;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class RconBot {
public static RconBot INSTANCE;
	
	public ShardManager shardMan;
	private CommandManager cmdMan;
	private Thread loop;
	
	private static Rcon rcon;
	
	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		try {
			new RconBot();
		} catch (LoginException | IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public RconBot() throws LoginException, IllegalArgumentException {
		
		Config.load("config.json");
		
		INSTANCE = this;
		
		LiteSQL.connect();
		SQLManager.onCreate();
		
		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(Config.getInstance().TOKEN);

		builder.setActivity(Activity.playing(""));
		builder.setStatus(OnlineStatus.ONLINE);
		
		cmdMan = new CommandManager();
		
		builder.addEventListeners(new CommandListener());
		builder.addEventListeners(new ReactionListener());
		
		builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
		builder.setMemberCachePolicy(MemberCachePolicy.ALL);
		
		builder.enableIntents(GatewayIntent.GUILD_MESSAGES);
		
		this.shardMan = builder.build();
		System.out.println("Bot online");
		
		try {
			rcon = new Rcon(Config.getInstance().IP, Config.getInstance().PORT, Config.getInstance().PASSWORD.getBytes());
			System.out.println("RCON Connection established");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("RCON Connection failed");
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("RCON Connection failed");
		}
		
		shutdown();
		//runLoop(); 
		
	}
	
	public void shutdown() {
		
		new Thread(() -> {
			
			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			
			try {
				
				while((line=reader.readLine()) != null) {
					
					if(line.equalsIgnoreCase("exit")) {
						if(shardMan != null) {
							shardMan.setStatus(OnlineStatus.OFFLINE);
							shardMan.shutdown();
							LiteSQL.disconnect();
							System.out.println("Bot offline");
						}
						/*
						if(loop != null) {
							loop.interrupt(); //Entkommentieren falls man wechselnde Statusmeldungen m�chte
						}
						*/
						reader.close();
						break;
					}
					else {
						
						System.out.println("Use exit to shutdown");
					}
				}
				
			}catch (IOException e) {
				e.printStackTrace();
			}
			
		}).start();
		
	}
	
	//Wechselnde Statusmeldungen
	public void runLoop() {
		this.loop = new Thread(() -> {
			
			long time = System.currentTimeMillis();
			
			while(true) {
				if(System.currentTimeMillis() >= time + 1000) {
					time = System.currentTimeMillis();
					
					onSecond();
				}
			}
		});
		
		this.loop.setName("Loop");
		this.loop.start();
	}
	
	String [] status = new String[] {"Galaxy-V", "?help | coded by ervk#1234"};
	
	int next = 15;
	
	//Wechselnde Statusmeldungen
	public void onSecond() {
		if(next <= 0) {
			Random rand = new Random();
			int i = rand.nextInt(status.length);
			
			shardMan.getShards().forEach(jda -> {
				String text = status[i].replaceAll("%members", "" + jda.getUsers().size());
				
				jda.getPresence().setActivity(Activity.watching(text));
			});
			next = 15;
		}
		else {
			next--;
		}
		
	}
	
	public CommandManager getCmdMan() {
		return cmdMan;
		
	}
	
	public static Rcon getRcon() {
		return rcon;
	}
}
