package bomberman.view.engine.rendering;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.util.List;

public class VertexBuffer implements VertexData {

    protected VertexAttrib[] attributes;

    private int totalNumComponents;
    private int stride;
    private FloatBuffer buffer;
    private int vertCount;

    public VertexBuffer(int vertCount, VertexAttrib... attributes) {
        this.attributes = attributes;
        for (VertexAttrib a : attributes)
            totalNumComponents += a.numComponents;
        this.vertCount = vertCount;

        this.buffer = BufferUtils.createFloatBuffer(vertCount * totalNumComponents);
    }

    public VertexBuffer(int vertCount, List<VertexAttrib> attributes) {
        this(vertCount, attributes.toArray(new VertexAttrib[attributes.size()]));
    }

    public VertexBuffer clear() {
        buffer.clear();
        return this;
    }

    public VertexBuffer flip() {
        buffer.flip();
        return this;
    }

    public VertexBuffer put(float[] verts, int offset, int length) {
        buffer.put(verts, offset, length);
        return this;
    }

    public VertexBuffer put(float f) {
        buffer.put(f);
        return this;
    }

    public FloatBuffer buffer() {
        return buffer;
    }

    public int getTotalNumComponents() {
        return totalNumComponents;
    }

    public int getVertexCount() {
        return vertCount;
    }

    public void bind() {
        int offset = 0;
        //4 bytes per float
        int stride = totalNumComponents * 4;

        for (int i = 0; i < attributes.length; i++) {
            VertexAttrib a = attributes[i];
            buffer.position(offset);
            GL20.glEnableVertexAttribArray(a.location);
            GL20.glVertexAttribPointer(a.location, a.numComponents, false, stride, buffer);
            offset += a.numComponents;
        }
    }

    public void draw(int geom, int first, int count) {
        GL11.glDrawArrays(geom, first, count);
    }

    public void unbind() {
        for (int i = 0; i < attributes.length; i++) {
            VertexAttrib a = attributes[i];
            GL20.glDisableVertexAttribArray(a.location);
        }
    }

}
