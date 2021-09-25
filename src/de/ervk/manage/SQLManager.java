package de.ervk.manage;

public class SQLManager {
	
	public static void onCreate() {
		
		// id	guildid		channelid		messageid		emote		roleid		
		LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS reactroles(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, guildid INTEGER, channelid INTEGER, messageid INTEGER, emote VARCHAR, roleid INTEGER)");
		
		// id guildid userid channelid messageid emote
		LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS tickets(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, guildid INTEGER, userid INTEGER, channelid INTEGER, messageid INTEGER, emote VARCHAR)");
		
	}
	
}
