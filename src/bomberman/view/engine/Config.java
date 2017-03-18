package bomberman.view.engine;

public class Config {

    private boolean vSync = true;
    private boolean showFPS = false;

    public Config() {
    }

    public boolean isvSync() {
        return vSync;
    }

    public void setvSync(boolean vSync) {
        this.vSync = vSync;
    }

    public boolean isShowFPS() {
        return showFPS;
    }

    public void setShowFPS(boolean showFPS) {
        this.showFPS = showFPS;
    }
}
