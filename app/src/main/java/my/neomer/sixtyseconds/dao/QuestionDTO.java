package my.neomer.sixtyseconds.dao;

import android.os.Parcel;
import android.os.Parcelable;

import my.neomer.sixtyseconds.model.Question;

public class QuestionDTO implements Parcelable {

    private long id;
    private String text;
    private String answer;
    private String comment;
    private int level;

    public QuestionDTO(Question question) {
        this.id = question.getId();
        this.text = question.getText();
        this.answer = question.getAnswer();
    }

    public Question toQuestion() {
        Question question = new Question(id, text, answer, comment);
        switch (level) {
            case 0: question.setDifficulty(Question.Difficulty.Easiest); break;
            case 1: question.setDifficulty(Question.Difficulty.Normal); break;
            case 2: question.setDifficulty(Question.Difficulty.Moderate); break;
            case 3: question.setDifficulty(Question.Difficulty.Professional); break;
            case 4: question.setDifficulty(Question.Difficulty.Hardest); break;
            default: question.setDifficulty(Question.Difficulty.Unknown); break;
        }
        return question;
    }

    protected QuestionDTO(Parcel in) {
        id = in.readLong();
        text = in.readString();
        answer = in.readString();
        comment = in.readString();
        level = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(text);
        dest.writeString(answer);
        dest.writeString(comment);
        dest.writeInt(level);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuestionDTO> CREATOR = new Creator<QuestionDTO>() {
        @Override
        public QuestionDTO createFromParcel(Parcel in) {
            return new QuestionDTO(in);
        }

        @Override
        public QuestionDTO[] newArray(int size) {
            return new QuestionDTO[size];
        }
    };
}
