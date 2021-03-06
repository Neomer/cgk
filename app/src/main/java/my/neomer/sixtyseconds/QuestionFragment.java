package my.neomer.sixtyseconds;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.security.PrivateKey;
import java.util.Observable;

import my.neomer.sixtyseconds.gamemodes.BaseGameContext;
import my.neomer.sixtyseconds.model.Answer;
import my.neomer.sixtyseconds.model.Question;

public class QuestionFragment extends Fragment {

    private BaseGameContext mViewModel;
    private TextView txtQuestion;
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

        progressBar = getView().findViewById(R.id.updateProgressBar);

        mViewModel = ViewModelProviders.of(getActivity()).get(BaseGameContext.class);
    }

    private String translatedDifficulty(Question.Difficulty difficulty) {
        switch (difficulty) {
            case Easiest: return getResources().getString(R.string.difficult_easiest);
            case Normal: return getResources().getString(R.string.difficult_normal);
            case Moderate: return getResources().getString(R.string.difficult_moderate);
            case Professional: return getResources().getString(R.string.difficult_Professional);
            case Hardest: return getResources().getString(R.string.difficult_Hardest);
            case Unknown: return getResources().getString(R.string.difficult_Unknown);
            default: return "";
        }
    }

    private CharSequence getText(int id, Object... args) {
        for(int i = 0; i < args.length; ++i) {
            args[i] = args[i] instanceof String ? TextUtils.htmlEncode((String) args[i]) : args[i];
        }
        CharSequence cs = getResources().getText(id);
        String htmlString = Html.toHtml(new SpannableString(cs));
        String s  = String.format(htmlString, args);
        return Html.fromHtml(s);
    }

    public void displayQuestion(Question question) {
        txtQuestion.scrollTo(0, 0);
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
        }
        if (question == null) {
            txtQuestion.setText(R.string.answer_not_received_error);
        } else {
            String txt = getString(R.string.question_label,
                    question.getId(),
                    translatedDifficulty(question.getDifficulty()),
                    question.getVote(),
                    question.getText());
            txtQuestion.setText(Html.fromHtml(txt));
        }
    }

    public void displayAnswer(Answer answer) {
        txtQuestion.scrollTo(0, 0);
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
        }
        if (answer == null) {
            txtQuestion.setText(R.string.answer_not_received_error);
        } else {
            if (answer.getComment() != null && !answer.getComment().isEmpty()) {
                txtQuestion.setText(Html.fromHtml(
                        getResources().getString(R.string.answer_and_comment_label,
                                answer.getAnswer(),
                                answer.getComment())));
            } else {
                txtQuestion.setText(Html.fromHtml(
                        getResources().getString(R.string.answer_label,
                                answer.getAnswer())));
            }
        }
    }

    public void clear() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (txtQuestion != null) {
            txtQuestion.setText("");
        }
    }
}

