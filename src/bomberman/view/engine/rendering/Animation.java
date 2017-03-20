package bomberman.view.engine.rendering;

import java.util.ArrayList;
import java.util.List;

public class Animation implements ITexture {

    private List<TextureRegion> frames = new ArrayList<>();
    private int animationIndex = 0;
    private float frameTime;
    private float currentFrameTime = 0f;

    public Animation(Texture sheet, int frameWidth, int frameHeight, float frameTime) {
        this.frameTime = frameTime;
        for (int y = 0; y < sheet.getHeight(); y += frameHeight) {
            for (int x = 0; x < sheet.getWidth(); x += frameWidth) {
                this.frames.add(new TextureRegion(sheet, x, y, frameWidth, frameHeight));
            }
        }
    }

    public void update(float deltaTime) {
        this.currentFrameTime += deltaTime;

        if (currentFrameTime >= frameTime) {
            currentFrameTime -= frameTime;

            animationIndex++;
            if (animationIndex >= frames.size()) {
                animationIndex = 0;
            }
        }
    }

    private TextureRegion getCurrentRegion() {
        return frames.get(animationIndex);
    }

    @Override
    public Texture getTexture() {
        return getCurrentRegion().getTexture();
    }

    @Override
    public int getWidth() {
        return getCurrentRegion().getWidth();
    }

    @Override
    public int getHeight() {
        return getCurrentRegion().getHeight();
    }

    @Override
    public float getU() {
        return getCurrentRegion().getU();
    }

    @Override
    public float getV() {
        return getCurrentRegion().getV();
    }

    @Override
    public float getU2() {
        return getCurrentRegion().getU2();
    }

    @Override
    public float getV2() {
        return getCurrentRegion().getV2();
    }
}
