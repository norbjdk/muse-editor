package com.norbjdk.museeditor.ui.manager;

import com.norbjdk.museeditor.model.dto.internal.ViewRequest;
import com.norbjdk.museeditor.model.dto.internal.ViewResponse;
import com.norbjdk.museeditor.ui.model.ViewName;
import com.norbjdk.museeditor.ui.model.Viewable;
import com.norbjdk.museeditor.ui.view.HomeView;
import com.norbjdk.museeditor.ui.view.NewProjectView;

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

    /**
     *
     * @param viewRequest request that contains ViewName which is going to be direction of the new view
     */

    public void changeView(ViewRequest viewRequest) {
        final Viewable newView = views.get(viewRequest.getViewName());

        if (newView != null) {
            currentView = newView;

            final ViewResponse response = new ViewResponse(currentView);
        }
    }
}
