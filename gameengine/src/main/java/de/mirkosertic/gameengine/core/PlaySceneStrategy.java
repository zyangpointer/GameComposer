package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.SystemException;
import de.mirkosertic.gameengine.input.DefaultGestureDetector;
import de.mirkosertic.gameengine.network.DefaultEventInterpreter;
import de.mirkosertic.gameengine.network.EventInterpreter;
import de.mirkosertic.gameengine.network.NetworkConnector;
import de.mirkosertic.gameengine.network.NetworkGameView;
import de.mirkosertic.gameengine.network.NetworkGameViewFactory;
import de.mirkosertic.gameengine.network.NewGameInstance;
import de.mirkosertic.gameengine.type.Size;
import de.mirkosertic.gameengine.type.UUID;

public abstract class PlaySceneStrategy {

    private GameLoop runningGameLoop;

    private final AbstractGameRuntimeFactory runtimeFactory;
    private final GameLoopFactory gameLoopFactory;
    private final NetworkConnector networkConnector;

    protected PlaySceneStrategy(AbstractGameRuntimeFactory aRuntimeFactory, GameLoopFactory aGameLoopFactory, NetworkConnector aNetworkConnector) {
        runtimeFactory = aRuntimeFactory;
        gameLoopFactory = aGameLoopFactory;
        networkConnector = aNetworkConnector;
    }

    public boolean hasGameLoop() {
        return runningGameLoop != null;
    }

    public GameLoop getRunningGameLoop() {
        return runningGameLoop;
    }

    protected abstract void loadOtherScene(String aSceneId);

    protected abstract Size getScreenSize();

    protected abstract GameView getOrCreateCurrentGameView(GameRuntime aGameRuntime, CameraBehavior aCamera, GestureDetector aGestureDetector);

    public abstract void handleResize();

    protected void loadingFinished(GameScene aGameScene) {
        runtimeFactory.loadingFinished(aGameScene);
    }

    protected GestureDetector createGestureDetectorFor(GameEventManager aEventManager, CameraBehavior aCamera) {
        return new DefaultGestureDetector(aEventManager, aCamera);
    }

    protected EventInterpreter createEventInterpreter() {
        return new DefaultEventInterpreter();
    }

    protected void handleSystemException(SystemException e) {
    }

    public void playScene(final GameScene aGameScene) {
        if (runningGameLoop != null) {
            runningGameLoop.shutdown();
        }

        GameRuntime theRuntime = aGameScene.getRuntime();
        GameEventManager theEventManager = theRuntime.getEventManager();

        theEventManager.register(null, SystemException.class, new GameEventListener<SystemException>() {
            @Override
            public void handleGameEvent(SystemException aEvent) {
                handleSystemException(aEvent);
            }
        } );

        loadingFinished(aGameScene);

        GameObject theCameraObject = aGameScene.cameraObjectProperty().get();
        GameObjectInstance theCameraObjectInstance = aGameScene.createFrom(theCameraObject);
        CameraBehavior theCameraBehavior = theCameraObjectInstance.getBehavior(CameraBehavior.class);

        GameObjectInstance thePlayerInstance = null;
        for (GameObjectInstance theInstance : aGameScene.getInstances()) {
            if (theInstance.getOwnerGameObject() == aGameScene.defaultPlayerProperty().get()) {
                thePlayerInstance = theInstance;
            }
        }

        // If there is a networked game
        // we need unique player instance ids
        // After loading they are the same on every instance
        if (thePlayerInstance != null) {
            thePlayerInstance.uuidProperty().set(UUID.randomUID());
        }

        // This is our hook to load new scenes
        theEventManager.register(null, RunScene.class, new GameEventListener<RunScene>() {
            @Override
            public void handleGameEvent(RunScene aEvent) {
                loadOtherScene(aEvent.sceneId);
            }
        });

        GestureDetector theGestureDetector = createGestureDetectorFor(theRuntime.getEventManager(), theCameraBehavior);
        GameView theGameView = getOrCreateCurrentGameView(theRuntime, theCameraBehavior, theGestureDetector);

        GameLoop theLoop = gameLoopFactory.create(aGameScene, theGameView, theRuntime);

        Size theScreenResolution = getScreenSize();
        theEventManager.fire(new SetScreenResolution(theScreenResolution));

        runningGameLoop = theLoop;

        theCameraBehavior.initializeFor(aGameScene, thePlayerInstance);

        // Now initialize the networking
        EventInterpreter theInterpreter = createEventInterpreter();

        final NetworkGameViewFactory theNetworkFactory = new NetworkGameViewFactory(networkConnector, theInterpreter);
        final NetworkGameView theNetworkGameView = theNetworkFactory.createNetworkViewFor(theEventManager);

        runningGameLoop.addGameView(theNetworkGameView);

        // Finally notify the other game instances that there is a new player on the field
        // This event will we sent to the other game instances
        // And will trigger there a creation of the new remote player
        theNetworkGameView.handleGameEvent(new NewGameInstance(thePlayerInstance));

        if (thePlayerInstance != null) {

            final GameObjectInstance theFinalPlayer = thePlayerInstance;

            theEventManager.register(null, NewGameInstance.class, new GameEventListener<NewGameInstance>() {
                @Override
                public void handleGameEvent(NewGameInstance aEvent) {
                    // Inform the other instances about the current player
                    theNetworkGameView.handleGameEvent(new GameObjectInstanceAddedToScene(theFinalPlayer));
                }
            });
        }
    }
}