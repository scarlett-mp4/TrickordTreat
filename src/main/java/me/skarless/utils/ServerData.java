package me.skarless.utils;

public class ServerData {
    private final String serverID;
    private final long messageID;
    private final MonsterTypes monster;
    private final String type;

    public ServerData(String serverID, long messageID, MonsterTypes monster, String type) {
        this.serverID = serverID;
        this.messageID = messageID;
        this.monster = monster;
        this.type = type;
    }

    public String getServerID() {
        return serverID;
    }

    public long getMessageID() {
        return messageID;
    }

    public MonsterTypes getMonster() {
        return monster;
    }

    public String getType() {
        return type;
    }
}
