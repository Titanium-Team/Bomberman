package bomberman.gameplay;

/**
 * Created by Daniel on 05.03.2017.
 */
public enum BombType {

    Dummybomb(1.0f,1,1,0);

    float fuse;
    int explosionX;
    int explosionY;
    int cluster;

    BombType(float fuse, int explosionX, int explosionY, int cluster) {
        this.fuse = fuse;
        this.explosionX = explosionX;
        this.explosionY = explosionY;
        this.cluster = cluster;
    }

    public float getFuse() {
        return fuse;
    }

    public int getExplosionX() {
        return explosionX;
    }

    public int getExplosionY() {
        return explosionY;
    }

    public int getCluster() {
        return cluster;
    }
}
