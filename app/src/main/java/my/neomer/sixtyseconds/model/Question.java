package my.neomer.sixtyseconds.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {

    private long id;
    private String text;
    private String answer;

    public Question() {
    }

    public Question(long id, String text, String answer) {
        this.id = id;
        this.text = text;
        this.answer = answer;
    }

    protected Question(Parcel in) {
        id = in.readLong();
        text = in.readString();
        answer = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(text);
        dest.writeString(answer);
    }
}
