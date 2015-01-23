package com.project.handyproject;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.*;
import android.util.DisplayMetrics;

public class SimpleGestureFilter extends SimpleOnGestureListener{
      
         public final static int SWIPE_NORTH = 2;
         public final static int SWIPE_NORTH_EAST  = 3;
         public final static int SWIPE_EAST  = 4;
         public final static int SWIPE_SOUTH_EAST = 5;
         public final static int SWIPE_SOUTH  = 6;
         public final static int SWIPE_SOUTH_WEST  = 7;
         public final static int SWIPE_WEST  = 8;
         public final static int SWIPE_NORTH_WEST = 1;
          
         public final static int MODE_TRANSPARENT = 0;
         public final static int MODE_SOLID       = 1;
         public final static int MODE_DYNAMIC     = 2;
          
         private final static int ACTION_FAKE = -13; //just an unlikely number
         private int swipe_Min_Distance = 100;
         private int swipe_Max_Distance = 700;
         private int swipe_Min_Velocity = 100;
      
     private int mode             = MODE_DYNAMIC;
     private boolean running      = true;
     private boolean tapIndicator = false;
      
     private Activity context;
     private GestureDetector detector;
     private SimpleGestureListener listener;
      
     public SimpleGestureFilter(Activity context,SimpleGestureListener sgl) {
      
      this.context = context;
      this.detector = new GestureDetector(context, this);
      this.listener = sgl;
     }
      
     public void onTouchEvent(MotionEvent event){
      
       if(!this.running)
      return; 
      
       boolean result = this.detector.onTouchEvent(event);
      
       if(this.mode == MODE_SOLID)
        event.setAction(MotionEvent.ACTION_CANCEL);
       else if (this.mode == MODE_DYNAMIC) {
      
         if(event.getAction() == ACTION_FAKE)
           event.setAction(MotionEvent.ACTION_UP);
         else if (result)
           event.setAction(MotionEvent.ACTION_CANCEL);
         else if(this.tapIndicator){
          event.setAction(MotionEvent.ACTION_DOWN);
          this.tapIndicator = false;
         }
      
       }
       //else just do nothing, it's Transparent
     }
      
     public void setMode(int m){
      this.mode = m;
     }
      
     public int getMode(){
      return this.mode;
     }
      
     public void setEnabled(boolean status){
      this.running = status;
     }
      
     public void setSwipeMaxDistance(int distance){
      this.swipe_Max_Distance = distance;
     }
      
     public void setSwipeMinDistance(int distance){
      this.swipe_Min_Distance = distance;
     }
      
     public void setSwipeMinVelocity(int distance){
      this.swipe_Min_Velocity = distance;
     }
      
     public int getSwipeMaxDistance(){
      return this.swipe_Max_Distance;
     }
      
     public int getSwipeMinDistance(){
      return this.swipe_Min_Distance;
     }
      
     public int getSwipeMinVelocity(){
      return this.swipe_Min_Velocity;
     }
      
     @Override
         public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
           float velocityY) {
          
          final float xDistance = Math.abs(e1.getX() - e2.getX());
          final float yDistance = Math.abs(e1.getY() - e2.getY());
          
          if(xDistance > this.swipe_Max_Distance || yDistance > this.swipe_Max_Distance)
           return false;
          
          velocityX = Math.abs(velocityX);
          velocityY = Math.abs(velocityY);
                boolean result = false;
          
         if(velocityX > this.swipe_Min_Velocity && xDistance > this.swipe_Min_Distance && velocityY > this.swipe_Min_Velocity && yDistance > this.swipe_Min_Distance)
         { 
            /*	 DisplayMetrics displaymetrics = new DisplayMetrics();
        	 ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        	 float height = displaymetrics.heightPixels;
        	 float width = displaymetrics.widthPixels;
        	*/
 //       	 float height = 30;
   //     	 float width = 20;
        	 
        	 float x1 = e1.getX();
        	 float y1 = -e1.getY();
        	 float x2 = e2.getX() - x1;
        	 float y2 = -e2.getY() - y1;
        	 float slope = (y2) / (x2) ;
        	 x1 = 0;
        	 y1 = 0;
        	 if(slope > -0.614 && slope <= 0.614)
        	 {
        		 if(x2>x1)
        			 this.listener.onSwipe(SWIPE_EAST, slope);
        		 else
        			 this.listener.onSwipe(SWIPE_WEST, slope);
        	 }
        	 else if(slope > 0.614 && slope <= 2)
        	 {
        		 if(x2>x1)
        			 this.listener.onSwipe(SWIPE_NORTH_EAST, slope);
        		 else
        			 this.listener.onSwipe(SWIPE_SOUTH_WEST, slope);

        	 }
        	 else if(slope <= -2 || slope > 2)
        	 {
        		 if(y2>y1)
        			 this.listener.onSwipe(SWIPE_NORTH, slope);
        		 else
        			 this.listener.onSwipe(SWIPE_SOUTH, slope);
        	 }
        	 else if(slope > -2 && slope <= -0.614)
        	 {
        		 if(y2>y1)
        			 this.listener.onSwipe(SWIPE_NORTH_WEST, slope);
        		 else
        			 this.listener.onSwipe(SWIPE_SOUTH_EAST, slope);
        	 }
        	 else;

         }
          
           return result;
         }
      
     @Override
     public boolean onSingleTapUp(MotionEvent e) {
      this.tapIndicator = true;
      return false;
     }
      
     @Override
     public boolean onDoubleTap(MotionEvent arg) {
      this.listener.onDoubleTap();;
      return true;
     }
      
     @Override
     public boolean onDoubleTapEvent(MotionEvent arg) {
      return true;
     }
      
     @Override
     public boolean onSingleTapConfirmed(MotionEvent arg) {
      
      if(this.mode == MODE_DYNAMIC){        // we owe an ACTION_UP, so we fake an
         arg.setAction(ACTION_FAKE);      //action which will be converted to an ACTION_UP later.
         this.context.dispatchTouchEvent(arg);
      }  
      
      return false;
     }
      
        static interface SimpleGestureListener{
        void onDoubleTap();
		void onSwipe(int direction, float slope);
     }
      
    }
