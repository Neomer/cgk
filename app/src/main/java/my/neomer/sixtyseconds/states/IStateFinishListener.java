package my.neomer.sixtyseconds.states;

import java.io.Serializable;

public interface IStateFinishListener extends Serializable {

    void onFinish(IState state);

}
