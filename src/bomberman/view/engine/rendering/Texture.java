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

	protected int handle;
	protected int width;
	protected int height;
	protected int samples = 1;
	protected ByteBuffer data;

	public static final int DEFAULT_FILTER = GL11.GL_LINEAR;
	public static final int DEFAULT_WRAP = GL11.GL_REPEAT;

	public Texture(int width, int height) {
		this(width, height, DEFAULT_FILTER);
	}

	public Texture(int width, int height, int filter) {
		this(width, height, filter, DEFAULT_WRAP);
	}

	public Texture(int width, int height, int filter, int wrap) {
		this(width, height, filter, wrap, 1);
	}

	public Texture(int width, int height, int filter, int wrap, int samples) {
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

		setFilter(filter);
		setWrap(wrap);

		ByteBuffer buf = BufferUtils.createByteBuffer(width * height * 4);

		this.data = buf;
		upload(GL11.GL_RGBA);
	}

	public Texture(URL pngRef) {
		this(pngRef, DEFAULT_FILTER);
	}

	public Texture(URL pngRef, int filter) {
		this(pngRef, filter, DEFAULT_WRAP);
	}

	public Texture(URL pngRef, int filter, int wrap) {
		this(pngRef, filter, filter, wrap, false);
	}

	public Texture(URL pngRef, int filter, boolean genMipmap) {
		this(pngRef, filter, filter, DEFAULT_WRAP, genMipmap);
	}

	public Texture(URL pngRef, int minFilter, int magFilter, int wrap, boolean genMipmap) {
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
			setFilter(minFilter, magFilter);
			setWrap(wrap);

			this.data = buf;
			upload(GL11.GL_RGBA);

			//use EXT since we are targeting 2.0+
			if (genMipmap) {
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

	public void setFilter(int filter) {
		setFilter(filter, filter);
	}

	public void setFilter(int minFilter, int magFilter) {
		bind();
		GL11.glTexParameteri(getTarget(), GL11.GL_TEXTURE_MIN_FILTER, minFilter);
		GL11.glTexParameteri(getTarget(), GL11.GL_TEXTURE_MAG_FILTER, magFilter);
	}

	public void setWrap(int wrap) {
		bind();
		GL11.glTexParameteri(getTarget(), GL11.GL_TEXTURE_WRAP_S, wrap);
		GL11.glTexParameteri(getTarget(), GL11.GL_TEXTURE_WRAP_T, wrap);
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
