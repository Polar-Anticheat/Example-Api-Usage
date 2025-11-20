package top.polar.example.commands;

import top.polar.api.command.Subcommand;
import top.polar.api.user.User;

import java.util.List;

public class LogsSubcommand extends Subcommand {

    public LogsSubcommand(String label, boolean hide, String permission) {
        super(label, hide, permission);
    }

    @Override
    public void execute(User user, String s, String[] strings) {
        user.bukkitPlayer().ifPresent(player -> {
            player.sendMessage("This is a test command!");
        });
    }

    @Override
    public List<String> onTabComplete(User user, String[] strings) {
        return List.of();
    }
}
