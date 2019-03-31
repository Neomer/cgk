package my.neomer.sixtyseconds.dao;

import android.os.Parcel;
import android.os.Parcelable;

import my.neomer.sixtyseconds.model.Question;

public class QuestionDTO implements Parcelable {

    private long id;
    private String text;
    private String answer;
    private String comment;

    public QuestionDTO(Question question) {
        this.id = question.getId();
        this.text = question.getText();
        this.answer = question.getAnswer();
    }

    public Question toQuestion() {
        return new Question(id, text, answer, comment);
    }

    protected QuestionDTO(Parcel in) {
        id = in.readLong();
        text = in.readString();
        answer = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(text);
        dest.writeString(answer);
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
