package com.muse.editor.redevelop.gui.manager;


import com.muse.editor.redevelop.event.EventBus;
import com.muse.editor.redevelop.event.view.ChangeViewEvent;
import com.muse.editor.redevelop.event.view.ViewChangedEvent;
import com.muse.editor.redevelop.gui.model.Viewable;
import com.muse.editor.redevelop.gui.view.CreateProjectView;
import com.muse.editor.redevelop.gui.view.HomeView;

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
    private final Map<Viewable.Name, Viewable> views;
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

    private void initViews() {
        addView(Viewable.Name.HOME, new HomeView());
        addView(Viewable.Name.CREATE_PROJECT, new CreateProjectView());
    }

    private void addView(Viewable.Name name, Viewable view) {
        if (view != null && !views.containsKey(name)) {
            views.put(name, view);
        }
    }

    public void changeView(Viewable.Name viewName) {
        final Viewable newView = views.get(viewName);

        if (newView != null) {
            currentView = newView;

            EventBus.getInstance().publish(new ViewChangedEvent(currentView));
        }
    }
}
