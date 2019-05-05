package my.neomer.sixtyseconds.dao;

import java.util.ArrayList;
import java.util.List;

import my.neomer.sixtyseconds.model.LeaderboardRecord;
import my.neomer.sixtyseconds.model.UserRating;

public class UserRatingDTO {

    private String user_id;
    private long points;
    private int place;
    private List<LeaderboardRecordDTO> leaderboard;

    public UserRating toUserRating() {
        UserRating userRating = new UserRating();
        userRating.setPlace(place);
        userRating.setPoints(points);

        List<LeaderboardRecord> list = new ArrayList<>();
        if (leaderboard != null) {
            for (LeaderboardRecordDTO rec : leaderboard) {
                list.add(rec.toLeaderBoardRecord());
            }
        }
        userRating.setLeaderboard(list);
        return userRating;
    }


}
