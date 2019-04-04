package my.neomer.sixtyseconds.transport;

public class TransportConfiguration {

    private String user;
    private int difficulty;

    public TransportConfiguration() {
    }

    public TransportConfiguration(String user, int difficulty) {
        this.user = user;
        this.difficulty = difficulty;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
