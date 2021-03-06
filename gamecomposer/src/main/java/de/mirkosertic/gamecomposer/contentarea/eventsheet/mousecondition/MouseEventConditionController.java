package de.mirkosertic.gamecomposer.contentarea.eventsheet.mousecondition;

import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gamecomposer.StringConverterFactory;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.input.MouseEventCondition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

public class MouseEventConditionController implements Controller {

    @Inject
    StringConverterFactory stringConverterFactory;

    @FXML
    ComboBox eventType;

    @FXML
    ComboBox filterType;

    @FXML
    ComboBox objectType;

    private GameScene gameScene;
    private MouseEventCondition condition;
    private Node view;

    MouseEventConditionController initialize(Node aView, GameScene aGameScene, MouseEventCondition aCondition) {
        gameScene = aGameScene;
        view = aView;
        condition = aCondition;

        List<GameObject> theSortedGameObjects = Arrays.asList(aGameScene.getObjects());
        Collections.sort(theSortedGameObjects, (o1, o2) -> o1.nameProperty().get().compareTo(o2.nameProperty().get()));

        eventType.getItems().clear();
        eventType.getItems().addAll(MouseEventCondition.MouseEventType.values());
        eventType.setConverter(stringConverterFactory.createEnumStringConverter());

        filterType.getItems().clear();
        filterType.getItems().addAll(MouseEventCondition.FilterType.values());
        filterType.setConverter(stringConverterFactory.createEnumStringConverter());

        objectType.getItems().clear();
        objectType.getItems().addAll(theSortedGameObjects);
        objectType.setConverter(stringConverterFactory.createGameObjectStringConverter());

        eventType.getSelectionModel().select(aCondition.eventTypeProperty().get());
        objectType.getSelectionModel().select(aCondition.objectTypeProperty().get());
        filterType.getSelectionModel().select(aCondition.filterTypeProperty().get());

        return this;
    }

    public void setEventType() {
        condition.eventTypeProperty().set((MouseEventCondition.MouseEventType) eventType.getValue());
    }

    public void setFilterType() {
        condition.filterTypeProperty().set((MouseEventCondition.FilterType) filterType.getValue());
    }

    public void setObjectType() {
        condition.objectTypeProperty().set((GameObject) objectType.getValue());
    }

    @Override
    public Node getView() {
        return view;
    }
}