package de.mirkosertic.gameengine.camera;

import java.util.Map;

import de.mirkosertic.gameengine.core.BehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.event.GameEventManager;

public class CameraBehaviorTemplateUnmarshaller implements BehaviorTemplateUnmarshaller<CameraBehaviorTemplate> {

    @Override
    public String getTypeKey() {
        return CameraBehavior.TYPE;
    }

    @Override
    public CameraBehaviorTemplate deserialize(GameEventManager aEventmanager, GameObject aOwner, Map<String, Object> aSerializedData) {
        return CameraBehaviorTemplate.deserialize(aEventmanager, aOwner, aSerializedData);
    }
}
