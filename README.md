VoronImageView
==============

Rotate, Zoom, Drag ImageView with Two Finger Gestures


This is a simple ImageView Library with support of Two Finger Gestures for Zoom, Drag and Rotate the Bitmap in the View. I don’t know if this is the best approach or the most efficient way, but my first goal was to get my hands dirty with Design Patterns. I used Factory Pattern to implement 3 different types of ImageView. So based on the custom attribute options The Factory creates the corresponding ImageView type, which are: 
1.	ImageView with Zoom, Drag and Rotate only by 90 degree. This is the most sophisticated view type. It always fills the bounds of the view. For example, if you rotate the image by 90 degree and if the Bitmap height is less than the Bitmap width, then the Bitmap slightly scales up to fill the image view width. When the image is loaded, the view calculates the min and the max zoom scale factor. The min scale factor is as much as is needed to fill the whole view, and the max scale factor is the real size of the image (so you may be never get the OOM issues). The other cool feature of the view is its ability to preserve its state when orientation change occurs. And even the cooler thing than that is the ability of the view to automatically center the current state to views with different size on portrai ant on landscape. For example, if you have on portrait imageview with 400x300 with and height and on landscape 500x200 the view will center the image and shows up by 50 pix on its left and right site and will cover by 50 pix from top and bottom accordingly. And of course it will scale if is needed to fill the view.
2.	ImageView with Zoom and Drag, No Rotation.
3.	ImageView with Zoom, Drag and Free Rotation.

<b>How to implement:</b>

        <org.voron.voronimageview.VoronImageView
        xmlns:android="http://schemas.android.com/apk/res/android"
    	xmlns:tools="http://schemas.android.com/tools"
    	xmlns:voron="http://schemas.android.com/apk/res-auto" 
       android:id="@+id/vImageView"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       voron:rotation="by90degree"/>
       
The custom attributes are:  "by90degree","disable","rotation".

<b>And in the Activity:</b>

    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.your-bitmap);
    VoronImageView vImageView = (VoronImageView)view.findViewById(R.id.vImageView);
    vImageView.setImageBitmap(bitmap);

