package de.mirkosertic.gameengine.process;

import de.mirkosertic.gameengine.core.Action;
import de.mirkosertic.gameengine.core.ConditionResult;
import de.mirkosertic.gameengine.core.GameScene;

public class InvokeActionProcess extends AbstractGameProcess {

    private final GameScene gameScene;
    private final ConditionResult conditionResult;
    private final Action action;

    public InvokeActionProcess(GameScene aGameScene, ConditionResult aConditionResult, Action aAction) {
        gameScene = aGameScene;
        conditionResult = aConditionResult;
        action = aAction;
    }

    @Override
    public ProceedResult proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {
        action.invoke(gameScene, conditionResult);
        return ProceedResult.STOPPED;
    }
}
