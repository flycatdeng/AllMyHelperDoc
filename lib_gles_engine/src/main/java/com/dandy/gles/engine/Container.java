package com.dandy.gles.engine;

import android.content.Context;

public class Container extends Group {

    public Container(Context context) {
        super(context);
    }

    @Override
    protected void onChildAdded(Actor child) {

    }

    /**
     * Add the actors into this container.
     *
     * @param actors actors to add as children
     */
    public void add(Actor... actors) {
        addChild(actors);
    }
}
