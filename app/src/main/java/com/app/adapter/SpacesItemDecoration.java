package com.app.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created   on 2016/9/21.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        outRect.top = space;
        if( parent.getChildLayoutPosition(view)%3 == 0){
            outRect.left = space;
            outRect.right = 0;
        }else if( parent.getChildLayoutPosition(view)%3 == 1){
            outRect.left = space;
            outRect.right = space;
        }else if(parent.getChildLayoutPosition(view)%3 == 2){
            outRect.left = 0;
            outRect.right = space;
        }
    }
}
