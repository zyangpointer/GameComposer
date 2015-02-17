package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.RemoteEvent;

@InheritedClassInformation
public class GameObjectInstanceAddedToScene extends GameEvent implements RemoteEvent {

    private static final GameObjectInstanceAddedToSceneClassInformation CIINSTANCE = new GameObjectInstanceAddedToSceneClassInformation();

    @ReflectiveField
    public final GameScene scene;

    @ReflectiveField
    public final GameObjectInstance instance;

    public GameObjectInstanceAddedToScene(GameScene aScene, GameObjectInstance aInstance) {
        super("GameObjectInstanceAddedToScene");
        scene = aScene;
        instance = aInstance;
    }

    @Override
    public GameObjectInstanceAddedToSceneClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}