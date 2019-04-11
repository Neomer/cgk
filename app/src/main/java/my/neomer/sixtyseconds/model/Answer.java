package my.neomer.sixtyseconds.model;

import android.support.annotation.NonNull;

public class Answer {

    private String answer;
    private String comment;
    private boolean correct;

    @NonNull
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @NonNull
    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
