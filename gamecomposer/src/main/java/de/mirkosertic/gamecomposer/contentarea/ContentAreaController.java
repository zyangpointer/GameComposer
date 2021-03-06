package de.mirkosertic.gamecomposer.contentarea;

import de.mirkosertic.gamecomposer.*;
import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.PropertyChanged;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class ContentAreaController implements Controller {

    @FXML
    TabPane editorTabPane;

    @FXML
    BorderPane welcomeBorderPane;

    @Inject
    @Any
    Instance<Object> singleObjectFactory;

    private Node view;
    private final Map<ContentController, Tab> activeTabs;

    public ContentAreaController() {
        activeTabs = new HashMap<>();
    }

    ContentAreaController initialize(Node aView) {
        view = aView;
        editorTabPane.setOnKeyPressed(aKeyEvent -> {
            Tab theSelectedTab = editorTabPane.getSelectionModel().getSelectedItem();
            if (theSelectedTab != null) {
                for (Map.Entry<ContentController, Tab> theTab : activeTabs.entrySet()) {
                    if (theTab.getValue() == theSelectedTab) {
                        theTab.getKey().processKeyPressedEvent(aKeyEvent);
                    }
                }
            }
        });
        editorTabPane.setOnKeyReleased(aKeyEvent -> {
            Tab theSelectedTab = editorTabPane.getSelectionModel().getSelectedItem();
            if (theSelectedTab != null) {
                for (Map.Entry<ContentController, Tab> theTab : activeTabs.entrySet()) {
                    if (theTab.getValue() == theSelectedTab) {
                        theTab.getKey().processKeyReleasedEvent(aKeyEvent);
                    }
                }
            }
        });
        return this;
    }

    @Override
    public Node getView() {
        return view;
    }

    public void onApplicationStarted(@Observes ApplicationStartedEvent aEvent) {
        editorTabPane.getTabs().clear();
        editorTabPane.setVisible(false);
        welcomeBorderPane.setVisible(true);
    }

    public void onNewGameEvent(@Observes NewGameEvent aEvent) {
        for (Map.Entry<ContentController, Tab> theTabEntry : activeTabs.entrySet()) {
            theTabEntry.getKey().removed();
            editorTabPane.getTabs().remove(theTabEntry.getValue());
        }
        editorTabPane.setVisible(false);
        welcomeBorderPane.setVisible(true);
    }

    void addTab(final Tab aTab) {
        editorTabPane.setVisible(true);
        welcomeBorderPane.setVisible(false);
        editorTabPane.getTabs().add(aTab);
        aTab.setClosable(true);
        aTab.setOnClosed(aEvent -> onTabClose(aTab));
        editorTabPane.getSelectionModel().select(aTab);
    }

    void onTabClose(Tab aTab) {
        for (Map.Entry<ContentController, Tab> theTabEntry : activeTabs.entrySet()) {
            if (theTabEntry.getValue() == aTab) {
                theTabEntry.getKey().removed();
                activeTabs.remove(theTabEntry.getKey());
                return;
            }
        }
    }

    public void onObjectUpdated(@Observes PropertyChanged aEvent) {
        for (Map.Entry<ContentController, Tab> theTabEntry : activeTabs.entrySet()) {
            theTabEntry.getKey().onObjectUpdated(theTabEntry.getValue(), aEvent);
        }
    }

    public void onShutdown(@Observes ShutdownEvent aEvent) {
        for (Map.Entry<ContentController, Tab> theTabEntry : activeTabs.entrySet()) {
            theTabEntry.getKey().onShutdown(aEvent);
        }
    }

    public void onObjectSelected(@Observes ObjectSelectedEvent aEvent) {
        for (Map.Entry<ContentController, Tab> theTabEntry : activeTabs.entrySet()) {
            theTabEntry.getKey().onObjectSelected(aEvent);
        }
    }

    private ContentAreaFactoryType createQualifier(final Class aClass) {
        return new ContentAreaFactoryType() {
            @Override
            public Class clazz() {
                return aClass;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return ContentAreaFactoryType.class;
            }
        };
    }

    public void onEventSheetSelected(@Observes EventSheetSelectedEvent aEvent) {
        EventSheet theSheet = aEvent.getEventSheet();
        for (Map.Entry<ContentController, Tab> theTabEntry : activeTabs.entrySet()) {
            if (theTabEntry.getKey().getEditingObject() == theSheet) {
                editorTabPane.getSelectionModel().select(theTabEntry.getValue());
                return;
            }
        }

        ContentController theController = (ContentController) ((ContentAreaFactory) singleObjectFactory.select(createQualifier(EventSheet.class)).get()).create(theSheet);
        Tab theTab = new Tab(theSheet.nameProperty().get());
        theTab.setContent(theController.getView());

        addTab(theTab);

        activeTabs.put(theController, theTab);

        theController.addedAsTab();
    }

    public void onGameSceneSelected(@Observes GameSceneSelectedEvent aEvent) {
        GameScene theScene = aEvent.getScene();
        for (Map.Entry<ContentController, Tab> theTabEntry : activeTabs.entrySet()) {
            if (theTabEntry.getKey().getEditingObject() == theScene) {
                editorTabPane.getSelectionModel().select(theTabEntry.getValue());
                return;
            }
        }

        ContentController theSceneEditorController = (ContentController) ((ContentAreaFactory) singleObjectFactory.select(createQualifier(GameScene.class)).get()).create(theScene);
        Tab theTab = new Tab(theScene.nameProperty().get());
        theTab.setContent(theSceneEditorController.getView());

        addTab(theTab);

        activeTabs.put(theSceneEditorController, theTab);

        theSceneEditorController.addedAsTab();
    }

    public void onFlushResourceCache(@Observes FlushResourceCacheEvent aEvent) {
        for (Map.Entry<ContentController, Tab> theTabEntry : activeTabs.entrySet()) {
            theTabEntry.getKey().onFlushResourceCache(aEvent);
        }
    }
}