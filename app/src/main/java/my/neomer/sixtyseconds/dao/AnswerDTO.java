package my.neomer.sixtyseconds.dao;

import my.neomer.sixtyseconds.model.Answer;

public class AnswerDTO {

    private int result;
    private String answer;
    private String comment;

    public int isCorrect() {
        return result;
    }

    public void setCorrect(int correct) {
        this.result = correct;
    }

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

    public Answer toAnswer() {
        Answer answer = new Answer();
        answer.setAnswer(this.answer);
        answer.setComment(this.comment);
        answer.setCorrect(result == 1);
        return answer;
    }
}
