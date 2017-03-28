package bomberman.view.engine.components;

import bomberman.view.engine.View;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.rendering.ITexture;


public class Image extends ViewComponent {

    private ITexture texture;

    public Image(LayoutParams params, View v, String textureKey) {
        this(params, v, ViewManager.getTexture(textureKey));
    }

    public Image(LayoutParams params, View v, ITexture texture) {
        super(params, v);

        this.texture = texture;
    }

    @Override
    public void draw(Batch batch) {
        if (texture != null)
            batch.draw(texture, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    public ITexture getTexture() {
        return texture;
    }
}
