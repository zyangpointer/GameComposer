package de.mirkosertic.gamecomposer.contentarea.eventsheet.playsound;

import de.mirkosertic.gamecomposer.GameAssetSelector;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionController;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.sound.PlaySoundAction;
import de.mirkosertic.gameengine.type.ResourceName;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class PlaySoundEditorController implements ActionController {

    @Inject
    GameAssetSelector gameAssetSelector;

    @FXML
    TextField resourceName;

    private Node view;
    private PlaySoundAction action;
    private GameScene gameScene;

    PlaySoundEditorController initialize(Node aView, GameScene aGameScene, PlaySoundAction aAction) {
        view = aView;
        action = aAction;
        gameScene = aGameScene;

        ResourceName theResourceName = action.resourceNameProperty().get();
        if (theResourceName != null) {
            resourceName.setText(theResourceName.name);
        }

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }

    @FXML
    public void onSelectResource() {
        ResourceName theNewResourceName = gameAssetSelector.selectAudioAssetFrom(gameScene, view.getScene().getWindow());
        if (theNewResourceName != null) {
            resourceName.setText(theNewResourceName.name);
            action.resourceNameProperty().set(theNewResourceName);
        }
    }
}
