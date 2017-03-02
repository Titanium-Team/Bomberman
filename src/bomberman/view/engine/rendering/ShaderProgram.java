package bomberman.view.engine.rendering;

import bomberman.view.engine.utility.Matrix4;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class ShaderProgram {

    private static String loadShader(File file) {
        StringBuilder result = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String buffer = "";
            while ((buffer = bufferedReader.readLine()) != null)
                result.append(buffer + "\n");
            bufferedReader.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return result.toString();
    }

    protected static FloatBuffer buf16Pool;

    public static void unbind() {
        GL20.glUseProgram(0);
    }

    public final int program;
    public final int vertex;
    public final int fragment;
    protected String log;

    protected HashMap<String, Integer> uniforms = new HashMap<String, Integer>();

    public ShaderProgram(File vertexShader, File fragmentShader) {
        this(vertexShader, fragmentShader, null);
    }

    public ShaderProgram(File vertexShader, File fragmentShader, Map<Integer, String> attributes) {
        this(loadShader(vertexShader), loadShader(fragmentShader), attributes);
    }

    public ShaderProgram(String vertexSource, String fragmentSource) {
        this(vertexSource, fragmentSource, null);
    }

    public ShaderProgram(String vertexShader, String fragmentShader, Map<Integer, String> attributes) {
        vertex = compileShader(vertexShader, GL20.GL_VERTEX_SHADER);
        fragment = compileShader(fragmentShader, GL20.GL_FRAGMENT_SHADER);

        program = GL20.glCreateProgram();

        GL20.glAttachShader(program, vertex);
        GL20.glAttachShader(program, fragment);

        if (attributes != null)
            for (Map.Entry<Integer, String> e : attributes.entrySet())
                GL20.glBindAttribLocation(program, e.getKey(), e.getValue());

        GL20.glLinkProgram(program);

        String infoLog = GL20.glGetProgramInfoLog(program, GL20.glGetProgrami(program, GL20.GL_INFO_LOG_LENGTH));

        if (infoLog != null && infoLog.trim().length() != 0)
            log += infoLog;

        if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == GL11.GL_FALSE)
            System.err.println("Failure in linking program. Error log:\n" + infoLog);

        GL20.glDetachShader(program, vertex);
        GL20.glDetachShader(program, fragment);
        GL20.glDeleteShader(vertex);
        GL20.glDeleteShader(fragment);
    }

    protected int compileShader(String source, int type) {
        int shader = GL20.glCreateShader(type);
        GL20.glShaderSource(shader, source);
        GL20.glCompileShader(shader);

        String infoLog = GL20.glGetShaderInfoLog(shader, GL20.glGetShaderi(shader, GL20.GL_INFO_LOG_LENGTH));
        if (infoLog != null && infoLog.trim().length() != 0)
            log += getName(type) + ": " + infoLog + "\n";

        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
            System.err.println("Failure in compiling " + getName(type) + ". Error log:\n" + infoLog);

        return shader;
    }

    protected String getName(int shaderType) {
        if (shaderType == GL20.GL_VERTEX_SHADER)
            return "GL_VERTEX_SHADER";
        if (shaderType == GL20.GL_FRAGMENT_SHADER)
            return "GL_FRAGMENT_SHADER";
        else
            return "shader";
    }

    public void use() {
        GL20.glUseProgram(program);
    }

    public void dispose() {
        GL20.glDeleteProgram(program);
    }

    private void fetchUniforms() {
        int len = GL20.glGetProgrami(program, GL20.GL_ACTIVE_UNIFORMS);

        int strLen = GL20.glGetProgrami(program, GL20.GL_ACTIVE_UNIFORM_MAX_LENGTH);
        for (int i = 0; i < len; i++) {
            String name = GL20.glGetActiveUniform(program, i, strLen);
            int id = GL20.glGetUniformLocation(program, name);
            uniforms.put(name, new Integer(id));
        }
    }

    public int getUniformLocation(String name) {
        int location = -1;
        Integer locI = uniforms.get(name);
        if (locI == null) {
            location = GL20.glGetUniformLocation(program, name);
            uniforms.put(name, location);
        } else
            location = locI.intValue();

        return location;
    }

    public void setUniformf(int loc, float f) {
        if (loc == -1) return;
        GL20.glUniform1f(loc, f);
    }

    public void setUniformf(int loc, float a, float b) {
        if (loc == -1) return;
        GL20.glUniform2f(loc, a, b);
    }

    public void setUniformf(int loc, float a, float b, float c) {
        if (loc == -1) return;
        GL20.glUniform3f(loc, a, b, c);
    }

    public void setUniformf(int loc, float a, float b, float c, float d) {
        if (loc == -1) return;
        GL20.glUniform4f(loc, a, b, c, d);
    }

    public void setUniformi(int loc, int i) {
        if (loc == -1) return;
        GL20.glUniform1i(loc, i);
    }

    public void setUniformi(int loc, int a, int b) {
        if (loc == -1) return;
        GL20.glUniform2i(loc, a, b);
    }

    public void setUniformi(int loc, int a, int b, int c) {
        if (loc == -1) return;
        GL20.glUniform3i(loc, a, b, c);
    }

    public void setUniformi(int loc, int a, int b, int c, int d) {
        if (loc == -1) return;
        GL20.glUniform4i(loc, a, b, c, d);
    }

    public void setUniformf(String name, float f) {
        setUniformf(getUniformLocation(name), f);
    }

    public void setUniformf(String name, float a, float b) {
        setUniformf(getUniformLocation(name), a, b);
    }

    public void setUniformf(String name, float a, float b, float c) {
        setUniformf(getUniformLocation(name), a, b, c);
    }

    public void setUniformf(String name, float a, float b, float c, float d) {
        setUniformf(getUniformLocation(name), a, b, c, d);
    }

    public void setUniformi(String name, int i) {
        setUniformi(getUniformLocation(name), i);
    }

    public void setUniformi(String name, int a, int b) {
        setUniformi(getUniformLocation(name), a, b);
    }

    public void setUniformi(String name, int a, int b, int c) {
        setUniformi(getUniformLocation(name), a, b, c);
    }

    public void setUniformi(String name, int a, int b, int c, int d) {
        setUniformi(getUniformLocation(name), a, b, c, d);
    }

    public void setUniformMatrix(int loc, boolean transposed, Matrix4 mat) {
        if (loc == -1) return;
        if (buf16Pool == null)
            buf16Pool = BufferUtils.createFloatBuffer(16);
        buf16Pool.clear();
        mat.store(buf16Pool);
        buf16Pool.flip();
        GL20.glUniformMatrix4(loc, transposed, buf16Pool);
    }

}