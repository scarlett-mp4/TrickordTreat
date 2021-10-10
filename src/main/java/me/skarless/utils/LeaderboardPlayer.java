package me.skarless.utils;

import net.dv8tion.jda.api.entities.User;

import java.util.Objects;

public class LeaderboardPlayer {
    private final User user;
    private final int score;

    public LeaderboardPlayer(User user, int score) {
        this.user = user;
        this.score = score;
    }

    public User user() {
        return user;
    }

    public int score() {
        return score;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (LeaderboardPlayer) obj;
        return Objects.equals(this.user, that.user) &&
                this.score == that.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, score);
    }

    @Override
    public String toString() {
        return "SortedPlayer[" +
                "user=" + user + ", " +
                "score=" + score + ']';
    }

}
