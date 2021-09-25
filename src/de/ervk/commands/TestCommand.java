package de.ervk.commands;

import java.io.IOException;

import de.ervk.RconBot;
import de.ervk.commands.types.ServerCommand;
import de.ervk.rcon.Rcon;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class TestCommand implements ServerCommand{

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		// TODO Auto-generated method stub
		sendRconMessage("test");		
	}
	
	public String sendRconMessage(String msg) {
		Rcon rcon = RconBot.getRcon();
		
		String result = "";
		try {
			result = rcon.command("test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
}
