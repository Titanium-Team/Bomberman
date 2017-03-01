package bomberman.view.engine;


import bomberman.view.engine.rendering.Batch;

public class TestView extends View {

    private float rotationRadians = 0;

    public TestView(float width, float height, ViewManager viewManager){
        super(width, height, viewManager);
    }

    @Override
    public void render(float deltaTime, Batch batch) {
        batch.draw(ViewManager.getTexture("test0.png"), 100, 100, 100, 100);

        for (int i = 100; i < 600; i += 50) {
            batch.draw(ViewManager.getTexture("test0.png"),(i), (450), (25), (25), 12, 12, rotationRadians, 1f, 1f, 1f, 1f);
        }
        ViewManager.font.drawText(batch, "Hallo Bomberman!     abcdefghijklmnopqrstuvwxyzß", 100, 400);

        rotationRadians += Math.toRadians(deltaTime * 50);

        super.render(deltaTime, batch);
    }

    @Override
    public void layout(float width, float height) {
        super.layout(width, height);
    }

}
