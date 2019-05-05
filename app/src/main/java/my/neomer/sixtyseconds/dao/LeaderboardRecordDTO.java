package my.neomer.sixtyseconds.dao;

import my.neomer.sixtyseconds.model.LeaderboardRecord;

public class LeaderboardRecordDTO {

    private String user;
    private int points;

    public LeaderboardRecord toLeaderBoardRecord() {
        LeaderboardRecord record = new LeaderboardRecord();
        record.setPoints(points);
        return record;
    }


}
