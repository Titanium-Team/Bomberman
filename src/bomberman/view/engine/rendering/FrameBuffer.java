package bomberman.view.engine.rendering;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;

import static org.lwjgl.opengl.EXTFramebufferObject.*;

public class FrameBuffer implements ITexture {

    public static boolean isSupported() {
        return GLContext.getCapabilities().GL_EXT_framebuffer_object;
    }

    protected int handle;
    protected Texture texture;
    protected boolean ownsTexture;

    public FrameBuffer(Texture texture, boolean ownsTexture) throws LWJGLException {
        this.texture = texture;
        this.ownsTexture = ownsTexture;
        if (!isSupported()) {
            throw new LWJGLException("FBO extension not supported by hardware");
        }
        texture.bind();
        handle = glGenFramebuffersEXT();
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, handle);
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, texture.getTarget(), texture.getID(), 0);

        int result = glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT);
        if (result != GL_FRAMEBUFFER_COMPLETE_EXT) {
            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
            GL30.glDeleteFramebuffers(handle);
            throw new LWJGLException("exception " + result + " when checking FBO status");
        }
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }

    public FrameBuffer(Texture texture) throws LWJGLException {
        this(texture, false);
    }

    public FrameBuffer(int width, int height, int filter, int wrap, int samples) throws LWJGLException {
        this(new Texture(width, height, filter, wrap, samples), true);
    }

    public FrameBuffer(int width, int height, int filter, int wrap) throws LWJGLException {
        this(width, height, filter, wrap, 1);
    }

    public FrameBuffer(int width, int height, int filter) throws LWJGLException {
        this(width, height, filter, Texture.DEFAULT_WRAP);
    }

    public FrameBuffer(int width, int height) throws LWJGLException {
        this(width, height, Texture.DEFAULT_FILTER, Texture.DEFAULT_WRAP);
    }

    public int getHandle() {
        return handle;
    }

    public int getWidth() {
        return texture.getWidth();
    }

    public int getHeight() {
        return texture.getHeight();
    }

    public Texture getTexture() {
        return texture;
    }

    public void begin() {
        if (handle == 0)
            throw new IllegalStateException("can't use FBO as it has been destroyed..");
        GL11.glViewport(0, 0, getWidth(), getHeight());
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, handle);
    }

    public void end() {
        if (handle == 0)
            return;
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }

    public void dispose() {
        if (handle == 0)
            return;
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        glDeleteFramebuffersEXT(handle);
        if (ownsTexture)
            texture.dispose();
        handle = 0;
    }

    @Override
    public float getU() {
        return 0;
    }

    @Override
    public float getV() {
        return 1f;
    }

    @Override
    public float getU2() {
        return 1f;
    }

    @Override
    public float getV2() {
        return 0;
    }

}
