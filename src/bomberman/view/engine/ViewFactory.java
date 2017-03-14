package bomberman.view.engine;

import java.lang.reflect.Constructor;

public class ViewFactory {

    private static ViewFactory factory;

    public static ViewFactory instance() {
        if (factory == null) {
            factory = new ViewFactory();
        }

        return factory;
    }

    private ViewFactory() {
    }

    public View createView(Class<? extends View> clazz, ViewManager viewManager) {
        try {
            Constructor<? extends View> constructor = clazz.getConstructor(int.class, int.class, ViewManager.class);

            return constructor.newInstance(viewManager.getViewportWidth(), viewManager.getViewportHeight(), viewManager);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
