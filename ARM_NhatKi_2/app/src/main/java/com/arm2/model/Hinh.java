package com.arm2.model;

import java.io.Serializable;

/**
 * Created by ARM on 11-May-17.
 */

public class Hinh implements Serializable {
    String path;

    String pathFull;
    public Hinh() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPathFull() {
        return pathFull;
    }

    public void setPathFull(String pathFull) {
        this.pathFull = pathFull;
    }
}
