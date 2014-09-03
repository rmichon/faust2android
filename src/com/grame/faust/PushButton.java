package com.grame.faust;

import com.grame.faust_dsp.faust_dsp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;

// TODO: there are still some issues with the listeners with the release action

class PushButton{
	int id = 0;
	String address = "";
	Button button;
	
	/*
	 * The constructor.
	 * addr: the tree address of the parameter controlled by the slider
	 * currentParameterID: the current parameter id in the parameters tree
	 * width: width of the view in pxs
	 * backgroundColor: grey level of the background of the view (0-255)
	 * label: the parameter's name
	 */
	public PushButton(Context c, String addr, int currentParameterID,
			int width, int backgroundColor, String label){
		id = currentParameterID;
		address = addr;
		
		button = new Button(c);
		button.setLayoutParams(new ViewGroup.LayoutParams(
				width, ViewGroup.LayoutParams.WRAP_CONTENT));
		button.setText(label);
		button.setTextColor(Color.WHITE);
	}
	
	/*
	 * Add the checkbox to group
	 */
	public void addTo(LinearLayout group){
		group.addView(button);
	}
	
	/*
	 * Set the checkbox's listeners
	 */
	public void linkTo(final ParametersInfo parametersInfo){
		button.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            	int x = (int)event.getRawX();
	            int y = (int)event.getRawY();
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                	parametersInfo.values[id] = 1.f;
                } else if (event.getAction() == MotionEvent.ACTION_UP || !inViewBounds(v, x, y)) {
                	parametersInfo.values[id] = 0.f;
                }
                faust_dsp.setParam(address, parametersInfo.values[id]);
	          	return true;
            }
        });
	}
	
	private boolean inViewBounds(View view, int x, int y){
    	Rect outRect = new Rect();
        int[] location = new int[2];
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }
}