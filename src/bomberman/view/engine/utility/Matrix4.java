package bomberman.view.engine.utility;

import java.nio.FloatBuffer;

public class Matrix4 {

    private float[][] components ;

    public Matrix4(){
        components = new float[4][4];
    }

    public void setField(float content, int i, int j){
        if(i>=0 && i<=4 && j>=0 &&j <=4) {
            components[i][j]= content;
        }
    }

    public float[][] getMatrix() {
        return components;
    }

    public float getField(int i, int j){
        if(i>=0 && i<=4 && j>=0 &&j <=4) {
            return components[i][j];
        }else{
            throw new IllegalArgumentException();
        }
    }

    public Matrix4 store(FloatBuffer buf) {
        for(int i = 0; i < components.length;i++){
            for(int j = 0 ; j < components[i].length;j++){
                buf.put(components[i][j]);
            }
        }

        return this;
    }

    public static Matrix4 getOrthographicCamera(float left, float right, float bottom, float top, float near, float far){
        Matrix4 orthoMatrix = new Matrix4();
        orthoMatrix.setField(2/(right-left),0,0);
        orthoMatrix.setField(2/(top-bottom),1,1);
        orthoMatrix.setField(-2/(far-near),2,2);
        orthoMatrix.setField(1,3,3);
        orthoMatrix.setField(-((right+left)/(right-left)),0,3);
        orthoMatrix.setField(-((top+bottom)/(top-bottom)),1,3);
        orthoMatrix.setField(-((far+near)/(far-near)),2,3);

        return orthoMatrix;
    }

}
