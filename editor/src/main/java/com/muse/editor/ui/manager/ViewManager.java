package com.muse.editor.ui.manager;

import com.muse.editor.core.EventBus;
import com.muse.editor.model.dto.internal.ViewRequest;
import com.muse.editor.model.dto.internal.ViewResponse;
import com.muse.editor.model.event.ChangeViewRequestedEvent;
import com.muse.editor.model.event.ViewChangedEvent;
import com.muse.editor.ui.model.ViewName;
import com.muse.editor.ui.model.Viewable;
import com.muse.editor.ui.view.CollectionView;
import com.muse.editor.ui.view.HomeView;
import com.muse.editor.ui.view.NewProjectView;
import com.muse.editor.ui.view.ProjectView;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton pattern ViewManager for storing and switching application views
 *
 * @see <a href="https://github.com/norbjdk/muse-editor">GitHub</a>
 * @author norbjdk
 */

public class ViewManager {
    private static ViewManager instance;
    private final Map<ViewName, Viewable> views;
    private Viewable currentView;

    private ViewManager() {
        views = new HashMap<>();

        initViews();
        setupEventListener();
    }

    public static ViewManager getInstance() {
        if (instance == null) {
            instance = new ViewManager();
        }

        return instance;
    }

    /**
     *  Method that initially adds all possible views to views Map of the manager
     */

    private void initViews() {
        addView(ViewName.HOME, new HomeView());
        addView(ViewName.NEW_PROJECT, new NewProjectView());
        addView(ViewName.PROJECT, new ProjectView());
        addView(ViewName.COLLECTION, new CollectionView());
    }

    /**
     *
     * Method that adds specific view to views Map
     *
     * @param name ViewName enum requested type
     * @param view Viewable interface that is implemented for a view
     */

    private void addView(ViewName name, Viewable view) {
        if (view != null && !views.containsKey(name)) {
            views.put(name, view);
        }
    }

    private void setupEventListener() {
        EventBus.getInstance().subscribe(ChangeViewRequestedEvent.class, changeViewRequestedEvent -> {
            if (changeViewRequestedEvent.getRequest() != null) {
                changeView(changeViewRequestedEvent.getRequest());
            }
        });
    }

    /**
     *
     * @param viewRequest request that contains ViewName which is going to be direction of the new view
     */

    public void changeView(ViewRequest viewRequest) {
        final Viewable newView = views.get(viewRequest.getViewName());

        if (newView != null) {
            currentView = newView;

            final ViewResponse response = new ViewResponse(currentView);
            EventBus.getInstance().publish(new ViewChangedEvent(response));
        }
    }
}
