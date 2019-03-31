package my.neomer.sixtyseconds;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.security.PrivateKey;
import java.util.Observable;

import my.neomer.sixtyseconds.model.Question;

public class QuestionFragment extends Fragment {

    private QuestionFragmentViewModel mViewModel;
    private TextView txtQuestion;
    private TextView txtQuestionNumber;
    private ProgressBar progressBar;

    public static QuestionFragment newInstance() {
        return new QuestionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.question_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        txtQuestion = getView().findViewById(R.id.txtQuestion);
        txtQuestion.setMovementMethod(new ScrollingMovementMethod());

        txtQuestionNumber = getView().findViewById(R.id.txtQuestionNumber);
        progressBar = getView().findViewById(R.id.updateProgressBar);

        mViewModel = ViewModelProviders.of(getActivity()).get(QuestionFragmentViewModel.class);
        mViewModel.getQuestion().observe(this, new Observer<Question>() {
            @Override
            public void onChanged(@Nullable Question question) {
                update(question);
            }
        });
    }

    public void update(Question question) {
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
        }
        txtQuestion.setText(question.getText());

        SpannableString str;
        txtQuestionNumber.setText("#" + String.valueOf(question.getId()));
        txtQuestion.setText(question.getText());
    }

    public void clear() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (txtQuestion != null) {
            txtQuestionNumber.setText("");
            txtQuestion.setText("");
        }
    }

    public void displayAnswer() {
        Question question = mViewModel.getQuestion().getValue();
        if (question == null) {
            return;
        }

        String html = question.getText() + "<br /><br /><strong><u>" +
                getResources().getString(R.string.answer_label) + "</u> " +
                question.getAnswer() +"</strong>" +
                (question.getComment() != null && !question.getComment().isEmpty() ? "<br /><br /><strong><u>" +
                    getResources().getString(R.string.comment_label) + "</u> " +
                    question.getComment() +"</strong>" : "");

        txtQuestion.setText(Html.fromHtml(html));
    }
}
