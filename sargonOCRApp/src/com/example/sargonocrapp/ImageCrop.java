package com.example.sargonocrapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

@TargetApi(19)
public class ImageCrop extends ImageView{

	    Paint paint = new Paint();
	    private int initial_size = 500;
	    private static Point leftTop, rightBottom,  center, previous;
	    private static Point oTop,oBottom;

	    private static final int DRAG= 0;
	    private static final int LEFT= 1;
	    private static final int TOP= 2;
	    private static final int RIGHT= 3;
	    private static final int BOTTOM= 4;
        private int leftImageSide;
        private int topImageSide;
        private int rightImageSide; 
        private int bottomImageSide;
        private float realH;
        private float realW;
        private float ratio;
        private int xbuffer;
        private int ybuffer;
        private float imageH;
        private float imageW;

	    // Adding parent class constructors   
	    public ImageCrop(Context context) {
	        super(context);
	        initCropView();
	    }

	    public ImageCrop(Context context, AttributeSet attrs) {
	        super(context, attrs, 0);
	        initCropView();
	    }

	    public ImageCrop(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	        initCropView();
	    }

	    @Override
	    protected void onDraw(Canvas canvas)
	    {
	        super.onDraw(canvas);
	       
	        //finding edges of image
	        float iviewH = this.getMeasuredHeight();//height of imageView
	        float iviewW = this.getMeasuredWidth();
	        imageH = this.getDrawable().getIntrinsicHeight();//original height of underlying image
	        imageW = this.getDrawable().getIntrinsicWidth();
	        //find aspect ratio
	        ratio = imageW/imageH;
	        //draw a box around image
	        	if(imageH > iviewH && imageW < iviewW)///Image height does not fit in imageview
	        	{
	        		realH = iviewH;
	        		realW = realH * ratio;
	        	}
	        	else if(imageW > iviewW && imageH < iviewH)//Image width does not fit in imageview
	        	{
	        		realW = iviewW;
	        		realH = realW / ratio;
	        	}
	        	else
	        	{
	        		realH = iviewH;
	        		realW = realH * ratio;
	        	}
	        	oTop.x = (int)(iviewW - realW)/2;
	        	oTop.y = (int)(iviewH - realH)/2;
	        	oBottom.x = (int)(oTop.x + realW);
	        	oBottom.y = (int)(oTop.y + realH);
	        	canvas.drawRect(oTop.x,oTop.y,oBottom.x,oBottom.y, paint);
	        	xbuffer = (int)((iviewW - realW)/2);
	        	ybuffer = (int)((iviewH - realH)/2);
	        	
	        //set inital crop box location	
	        if(leftTop.equals(0, 0))
	        {
	        	leftTop.x = (int)((realW/4) + xbuffer);
	        	leftTop.y = (int)((realH/4) + ybuffer);
	        	rightBottom.x = (int)(((realW/4)*3)+xbuffer);
	        	rightBottom.y = (int)(((realH/4)*3)+ybuffer);
	        }
	        //establish working area for crop, is equal to image edges
	        leftImageSide = (int)((iviewW - xbuffer) - realW);
	        topImageSide = (int)((iviewH - ybuffer) - realH);
	        rightImageSide = (int)(iviewW - xbuffer);
	        bottomImageSide = (int)(iviewH - ybuffer);
	        
	        //ensure crop box is never outside working area
	        if(leftTop.x < leftImageSide)
	        	leftTop.x = leftImageSide;
	        if(leftTop.y < topImageSide)
	        	leftTop.y = topImageSide;
	        if(rightBottom.x > rightImageSide)
	        	rightBottom.x = rightImageSide;
	        if(rightBottom.y > bottomImageSide)
	        	rightBottom.y = bottomImageSide;
	        
	        //draw crop box
	        canvas.drawRect(leftTop.x, leftTop.y, rightBottom.x, rightBottom.y, paint);
	        //draw cirle shapes (handles)
	        int leftcx, leftcy, bottomcx, bottomcy, rightcx, rightcy, topcx, topcy;
	        leftcx = leftTop.x;
	        leftcy = leftTop.y + (rightBottom.y - leftTop.y)/2;
	        rightcx = rightBottom.x;
	        rightcy = rightBottom.y - (rightBottom.y - leftTop.y)/2;
	        topcy = leftTop.y;
	        topcx = leftTop.x + (rightBottom.x - leftTop.x)/2;
	        bottomcy = rightBottom.y;
	        bottomcx = leftTop.x + (rightBottom.x - leftTop.x)/2;
	        paint.setStyle(Style.FILL);
	        canvas.drawCircle(leftcx, leftcy, 25, paint);
	        canvas.drawCircle(rightcx, rightcy, 25, paint);
	        canvas.drawCircle(topcx, topcy, 25, paint);
	        canvas.drawCircle(bottomcx, bottomcy, 25, paint);
	        paint.setStyle(Style.STROKE);
	        	
	    }
	    
	    public Bitmap getCroppedImage()
	    {
	        BitmapDrawable drawable = (BitmapDrawable)this.getDrawable();
	    	Bitmap source = drawable.getBitmap();
	    	int convertedTopx;
	    	int convertedTopy;
	    	int convertedBottomx;
	    	int convertedBottomy;
	    	float imageRatioH = imageH/realH;
	    	float imageRatioW = imageW/realW;
	    	convertedTopx = (int)((float)(leftTop.x-xbuffer)*imageRatioW);
	    	convertedTopy = (int)((float)(leftTop.y-ybuffer)*imageRatioH);
	    	convertedBottomx =(int)((float)(rightBottom.x-leftTop.x)*imageRatioW);
	    	convertedBottomy = (int)((float)(rightBottom.y-leftTop.y)*imageRatioH);
	    	Bitmap cropped = Bitmap.createBitmap(source, convertedTopx,convertedTopy,convertedBottomx,convertedBottomy);
	        return cropped;
	    }
	    @Override
	    public boolean onTouchEvent(MotionEvent event) {
	        int eventaction = event.getAction();
	        switch (eventaction) { 
	            case MotionEvent.ACTION_DOWN:
	                previous.set((int)event.getX(), (int)event.getY());
	                break; 
	            case MotionEvent.ACTION_MOVE: 
	                if(isActionInsideRectangle(event.getX(), event.getY())) 
	                {
	                    adjustRectangle((int)event.getX(), (int)event.getY());
	                    invalidate(); // redraw rectangle
	                    previous.set((int)event.getX(), (int)event.getY());
	                }
	                break; 
	            case MotionEvent.ACTION_UP: 
	                previous = new Point();
	                break;
	        }         
	        return true;
	    }
	    
	    private void initCropView() {
	        paint.setColor(Color.BLUE);
	        paint.setStyle(Style.STROKE);
	        paint.setStrokeWidth(8);
	        leftTop = new Point();
	        rightBottom = new Point();
	        center = new Point();
	        previous = new Point();
	        oTop = new Point();
	        oBottom = new Point();
	    }

	    public void resetPoints() {
	        center.set(getWidth()/2, getHeight()/2);
	        leftTop.set((getWidth()-initial_size)/2,(getHeight()-initial_size)/2);
	        rightBottom.set(leftTop.x+initial_size, leftTop.y+initial_size);
	    }

	    private static boolean isActionInsideRectangle(float x, float y) {
	        int buffer = 60;
	        return (x>=(leftTop.x-buffer)&&x<=(rightBottom.x+buffer)&& y>=(leftTop.y-buffer)&&y<=(rightBottom.y+buffer))?true:false;
	    }

	    private void adjustRectangle(int x, int y) {
	        int movement;
	        switch(getAffectedSide(x,y)) {
	            case LEFT:
	                movement = x-leftTop.x;
	                leftTop.set(leftTop.x+movement,leftTop.y);
	                break;
	            case TOP:
	                movement = y-leftTop.y;
	                    leftTop.set(leftTop.x,leftTop.y+movement);
	                break;
	            case RIGHT:
	                movement = x-rightBottom.x;
	                    rightBottom.set(rightBottom.x+movement,rightBottom.y);
	                break;
	            case BOTTOM:
	                movement = y-rightBottom.y;
	                    rightBottom.set(rightBottom.x,rightBottom.y+movement);
	                break;      
	            case DRAG:
	                movement = x-previous.x;
	                int movementY = y-previous.y;
	                leftTop.set(leftTop.x+movement,leftTop.y+movementY);
	                rightBottom.set(rightBottom.x+movement,rightBottom.y+movementY);	                
	                break;
	        }
	    }

	    private static int getAffectedSide(float x, float y) {
	        int buffer = 45;
	        if(x>=(leftTop.x-buffer)&&x<=(leftTop.x+buffer))
	            return LEFT;
	        else if(y>=(leftTop.y-buffer)&&y<=(leftTop.y+buffer))
	            return TOP;
	        else if(x>=(rightBottom.x-buffer)&&x<=(rightBottom.x+buffer))
	            return RIGHT;
	        else if(y>=(rightBottom.y-buffer)&&y<=(rightBottom.y+buffer))
	            return BOTTOM;
	        else
	            return DRAG;
	    }
	    

}


