package me.skarless.utils;

import java.util.ArrayList;
import java.util.List;

public class Cache {
    public static List<ServerData> monsterList = new ArrayList<>();
    public final List<Command> commands = new ArrayList<>();
    public final List<QueuedCommand> commandQueue = new ArrayList<>();
}