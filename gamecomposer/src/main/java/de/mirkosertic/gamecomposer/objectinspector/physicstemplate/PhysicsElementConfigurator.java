package de.mirkosertic.gamecomposer.objectinspector.physicstemplate;

import de.mirkosertic.gamecomposer.MessageBox;
import de.mirkosertic.gamecomposer.objectinspector.ActionPropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.utils.FloatPropertyEditor;
import de.mirkosertic.gameengine.physic.Physics;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import org.controlsfx.control.PropertySheet;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ObjectInspectorElementConfiguratorType(clazz = Physics.class)
public class PhysicsElementConfigurator implements ObjectInspectorElementConfigurator<Physics> {

    private static final String CATEGORY_NAME = "30 Physics";

    @Inject
    MessageBox messageBox;

    @Override
    public List<PropertySheet.Item> getItemsFor(Physics aObject) {
        List<PropertySheet.Item> theResult = new ArrayList<>();
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.activeProperty(), "Active", "Are physics enabled for this object?"));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.fixedRotationProperty(), "Fixed rotation", "Is rotation disabled for this object?"));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.densityProperty(), "Density", "The density of this object", Optional.of(FloatPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.frictionProperty(), "Friction", "The friction of this object", Optional.of(FloatPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.restitutionProperty(), "Restitution", "The restitution of this object", Optional.of(FloatPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.gravityScaleProperty(), "Gravity", "The gravity of this object", Optional.of(FloatPropertyEditor.class)));

        ActionPropertyEditorItem theActions = new ActionPropertyEditorItem(CATEGORY_NAME, "", "Available actions");
        theActions.addAction(new ActionPropertyEditorItem.Action("Delete behavior...", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent aEvent) {
                if (messageBox.showMessageBox((Node) aEvent.getSource(), "Delete behavior", "Do you really want to delete this behavior?", MessageBox.ButtonType.YES, MessageBox.ButtonType.NO) == MessageBox.ButtonType.YES) {
                    aObject.delete();
                }
            }
        }));
        theResult.add(theActions);

        return theResult;
    }
}