package my.neomer.sixtyseconds.dao;

import android.os.Parcel;
import android.os.Parcelable;

import my.neomer.sixtyseconds.helpers.DifficultyHelper;
import my.neomer.sixtyseconds.model.Question;

public class QuestionDTO implements Parcelable {

    private long id;
    private int level;
    private String text;
    private int vote;

    public Question toQuestion() {
        Question question = new Question();
        question.setId(id);
        question.setText(text);
        question.setVote(vote);
        question.setDifficulty(DifficultyHelper.FromInt(level));
        return question;
    }

    protected QuestionDTO(Parcel in) {
        id = in.readLong();
        level = in.readInt();
        vote = in.readInt();
        text = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(level);
        dest.writeInt(vote);
        dest.writeString(text);
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
