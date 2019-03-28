package my.neomer.sixtyseconds;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Observable;

import my.neomer.sixtyseconds.model.Question;

public class QuestionFragment extends Fragment {

    private QuestionFragmentViewModel mViewModel;
    private TextView txtQuestion;
    private TextView txtQuestionNumber;

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
        txtQuestionNumber = getView().findViewById(R.id.txtQuestionNumber);

        mViewModel = ViewModelProviders.of(getActivity()).get(QuestionFragmentViewModel.class);
        mViewModel.getQuestion().observe(this, new Observer<Question>() {
            @Override
            public void onChanged(@Nullable Question question) {
                update(question);
            }
        });
    }

    public void update(Question question) {
        txtQuestion.setText(question.getText());
        txtQuestionNumber.setText("#" + String.valueOf(question.getId()));
    }
}
