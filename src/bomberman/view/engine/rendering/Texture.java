package bomberman.view.engine.rendering;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBTextureMultisample;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

public class Texture implements ITexture {

    public static boolean isMultisamplingSupported() {
        return GLContext.getCapabilities().GL_ARB_multisample;
    }

    public enum Filter {
        Linear(GL11.GL_LINEAR, GL11.GL_LINEAR, false),
        Bilinear(GL11.GL_LINEAR_MIPMAP_NEAREST, GL11.GL_LINEAR, true),
        Trilinear(GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR, true);

        public final int min;
        public final int max;
        public final boolean genMipmaps;

        Filter(int min, int max, boolean genMipmaps) {
            this.min = min;
            this.max = max;
            this.genMipmaps = genMipmaps;
        }
    }

    protected int handle;
    protected int width;
    protected int height;
    protected int samples = 1;
    private Filter filter = Filter.Linear;
    protected ByteBuffer data;

    public Texture(int width, int height) {
        this(width, height, Filter.Linear);
    }

    public Texture(int width, int height, Filter filter) {
        this(width, height, filter, 1);
    }

    public Texture(int width, int height, Filter filter, int samples) {
        handle = GL11.glGenTextures();
        this.width = width;
        this.height = height;

        if (!isMultisamplingSupported()) {
            this.samples = 1;
        } else {
            this.samples = Math.min(samples, GL11.glGetInteger(ARBTextureMultisample.GL_MAX_COLOR_TEXTURE_SAMPLES));
        }
        GL11.glEnable(getTarget());

        bind();
        setup();

        ByteBuffer buf = BufferUtils.createByteBuffer(width * height * 4);

        this.data = buf;
        upload(GL11.GL_RGBA);
    }

    public Texture(URL pngRef) {
        InputStream input = null;
        try {
            input = pngRef.openStream();
            PNGDecoder dec = new PNGDecoder(input);

            width = dec.getWidth();
            height = dec.getHeight();
            ByteBuffer buf = BufferUtils.createByteBuffer(4 * width * height);
            dec.decode(buf, width * 4, PNGDecoder.Format.RGBA);
            buf.flip();

            GL11.glEnable(getTarget());
            handle = GL11.glGenTextures();

            bind();
            this.filter = Filter.Bilinear;
            setup();

            this.data = buf;
            upload(GL11.GL_RGBA);

            //use EXT since we are targeting 2.0+
            if (filter.genMipmaps) {
                EXTFramebufferObject.glGenerateMipmapEXT(getTarget());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private void setup() {
        GL11.glTexParameteri(getTarget(), GL11.GL_TEXTURE_MIN_FILTER, filter.min);
        GL11.glTexParameteri(getTarget(), GL11.GL_TEXTURE_MAG_FILTER, filter.max);

        GL11.glTexParameteri(getTarget(), GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(getTarget(), GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
    }

    public int getTarget() {
        return samples > 1 ? ARBTextureMultisample.GL_TEXTURE_2D_MULTISAMPLE : GL11.GL_TEXTURE_2D;
    }

    public int getID() {
        return handle;
    }

    public int getSamples() {
        return samples;
    }

    protected void setUnpackAlignment() {
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
    }

    public void upload(int dataFormat) {
        bind();
        setUnpackAlignment();

        if (samples > 1) {
            ARBTextureMultisample.glTexImage2DMultisample(getTarget(), samples, GL11.GL_RGBA, width, height, true);
        } else {
            GL11.glTexImage2D(getTarget(), 0, GL11.GL_RGBA, width, height, 0, dataFormat, GL11.GL_UNSIGNED_BYTE, data);
        }
    }

    public void bind() {
        if (!valid())
            throw new IllegalStateException("trying to bind a texture that was disposed");
        GL11.glBindTexture(getTarget(), handle);
    }

    public void dispose() {
        if (valid()) {
            GL11.glDeleteTextures(handle);
            handle = 0;
        }
    }

    public boolean valid() {
        return handle != 0;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getU() {
        return 0f;
    }

    public float getV() {
        return 0f;
    }

    public float getU2() {
        return 1f;
    }

    public float getV2() {
        return 1f;
    }

    public Texture getTexture() {
        return this;
    }

}
