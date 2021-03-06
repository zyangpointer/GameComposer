package de.mirkosertic.gameengine.physic;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.Behavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.type.Reflectable;

public class StaticBehavior implements Behavior, Static, Reflectable<StaticClassInformation> {

    private static final StaticClassInformation CIINSTANCE = new StaticClassInformation();
    
    static final String TYPE = "Static";

    private final GameObjectInstance objectInstance;

    StaticBehavior(GameObjectInstance aObjectInstance) {
        objectInstance = aObjectInstance;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public StaticClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE);
        return theResult;
    }

    @Override
    public StaticBehaviorTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getBehaviorTemplate(StaticBehaviorTemplate.class);
    }

    @Override
    public void delete() {
        objectInstance.getOwnerGameObject().getGameScene().removeBehaviorFrom(objectInstance.getOwnerGameObject(), this);
    }

    @Override
    public GameObjectInstance getInstance() {
        return objectInstance;
    }

    @Override
    public void markAsRemoteObject() {
    }

    public static StaticBehavior deserialize(GameObjectInstance aObjectInstance) {
        return new StaticBehavior(aObjectInstance);
    }
}
