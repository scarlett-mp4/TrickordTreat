package me.skarless.utils;

public enum MonsterTypes {
    FAIRY("https://i.imgur.com/PaMTS3l.png", 5),
    GRIM("https://i.imgur.com/2yhL59Y.png", 4),
    VAMPIRE("https://i.imgur.com/cvKu2YX.png", 3),
    WITCH("https://i.imgur.com/pm3M9pU.png", 2),
    ZOMBIE("https://i.imgur.com/YS0LJbB.png", 1);

    private final String urlString;
    private final int id;

    MonsterTypes(String urlString, int id) {
        this.urlString = urlString;
        this.id = id;
    }

    public String getUrlString() {
        return urlString;
    }

    public int getId() {
        return id;
    }
}
