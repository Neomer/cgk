package my.neomer.sixtyseconds.model;

import java.util.List;

public class UserRating {

    private long points;
    private int place;
    private List<LeaderboardRecord> leaderboard;

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public List<LeaderboardRecord> getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(List<LeaderboardRecord> leaderboard) {
        this.leaderboard = leaderboard;
    }
}
