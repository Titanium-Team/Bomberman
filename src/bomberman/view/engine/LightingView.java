package bomberman.view.engine;

import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.rendering.FrameBuffer;
import bomberman.view.engine.rendering.ShaderProgram;
import bomberman.view.engine.rendering.VertexAttrib;
import bomberman.view.engine.utility.Camera;
import org.lwjgl.opengl.GL11;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class LightingView extends View {

    private List<Light> lights = new ArrayList<Light>();
    private float ambientIntensity = 0.1f;

    private ShaderProgram lightingShader;
    private ShaderProgram shadowMapRenderShader;

    public LightingView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);

        createShaders();
    }

    private void createShaders() {
        try {
            Map<Integer, String> map = new HashMap<>();
            for (VertexAttrib attrib : Batch.VERTEX_ATTRIBS) {
                map.put(attrib.location, attrib.name);
            }

            {
                InputStream vert = Batch.class.getResourceAsStream("/bomberman/resources/shaders/lighting.vert");
                InputStream frag = Batch.class.getResourceAsStream("/bomberman/resources/shaders/lighting.frag");
                this.lightingShader = new ShaderProgram(vert, frag, map);
            }
            {
                InputStream vert = Batch.class.getResourceAsStream("/bomberman/resources/shaders/shadowMapRender.vert");
                InputStream frag = Batch.class.getResourceAsStream("/bomberman/resources/shaders/shadowMapRender.frag");
                this.shadowMapRenderShader = new ShaderProgram(vert, frag, map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void layout(int width, int height) {
        super.layout(width, height);

        for (Light light : lights) {
            light.createFrameBuffersIfNecessary();
        }
    }

    public abstract void renderOccluders(Batch batch, Camera camera);

    public abstract void renderNonOccluders(Batch batch, Camera camera);

    @Override
    public void renderUI(Batch batch) {
        super.renderUI(batch);

        // debug
        /*for (int i = 0; i < lights.size(); i++) {
            Light light = lights.getMap(i);

            batch.draw(light.getShadowMap(), 0, 100 + i * 100, 100, 100);
        }*/
    }

    @Override
    public final void renderScene(Batch batch) {
        batch.end();

        updateShadowMaps(batch);

        GL11.glClearColor(ambientIntensity, ambientIntensity, ambientIntensity, 1f - ambientIntensity);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.setCombinedMatrix(getSceneCamera().getCombined());
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        renderNonOccluders(batch, this.getSceneCamera());
        batch.end();

        batch.begin(lightingShader);
        batch.setCombinedMatrix(getSceneCamera().getCombined());
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        for (int i = 0; i < lights.size(); i++) {
            Light light = lights.get(i);
            float x = light.getX();
            float y = light.getY();
            int r = light.getRadius();
            int r2 = r / 2;

            lightingShader.setUniformf("u_resolution", light.getRadius() * 2);
            batch.draw(light.getShadowMap(), x - r2, y - r2, r, r, light.getR(), light.getG(), light.getB(), 1.0f);
            batch.flush();
        }
        batch.end();

        batch.begin();
        batch.setCombinedMatrix(getSceneCamera().getCombined());
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        renderOccluders(batch, this.getSceneCamera());
    }

    private void updateShadowMaps(Batch batch) {
        for (int i = 0; i < lights.size(); i++) {
            Light light = lights.get(i);
            Camera lightCamera = light.getLightCamera();

            FrameBuffer occludersMap = light.getOccludersMap();

            occludersMap.begin();
            GL11.glClearColor(0f, 0f, 0f, 0f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            batch.begin();
            batch.setCombinedMatrix(lightCamera.getCombined());//

            renderOccluders(batch, lightCamera);

            batch.end();
            occludersMap.end();

            //

            FrameBuffer shadowMap = light.getShadowMap();

            shadowMap.begin();
            GL11.glClearColor(0f, 0f, 0f, 0f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            batch.begin(shadowMapRenderShader);
            batch.setCombinedMatrix(lightCamera.getCombined());
            shadowMapRenderShader.setUniformf("u_resolution", light.getRadius());

            batch.draw(occludersMap, light.getX() - light.getRadius(), light.getY() - light.getRadius(), light.getRadius() * 2, light.getRadius() * 2);

            batch.end();
            shadowMap.end();
        }
    }

    public void addLight(Light light) {
        this.lights.add(light);
    }

    public void removeLight(Light light) {
        light.cleanUp();
        this.lights.remove(light);
    }

    public void clearLights() {
        for (int i = 0; i < lights.size(); i++) {
            lights.get(i).cleanUp();
        }
        lights.clear();
    }

    public void onDestroy() {
        for (int i = 0; i < lights.size(); i++) {
            lights.get(i).cleanUp();
        }
    }

}
