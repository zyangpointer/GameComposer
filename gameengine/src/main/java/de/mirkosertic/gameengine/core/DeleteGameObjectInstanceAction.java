package de.mirkosertic.gameengine.core;

import java.util.HashMap;
import java.util.Map;

public class DeleteGameObjectInstanceAction implements Action {

    public static final String TYPE_VALUE = "DeleteGameObjectInstanceAction";

    @UsedByReflection
    public DeleteGameObjectInstanceAction() {
    }

    @Override
    public void invoke(GameScene aScene, ConditionResult aResult) {
        for (GameObjectInstance theInstance : aResult.getAffectedInstances()) {
            aScene.removeGameObjectInstance(theInstance);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        return theResult;
    }

    public static DeleteGameObjectInstanceAction unmarshall() {
        return new DeleteGameObjectInstanceAction();
    }
}