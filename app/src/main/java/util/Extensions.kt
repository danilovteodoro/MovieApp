package util

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.onItemTouch(callback:(position:Int,view: View)->Unit){
    val detector =  GestureDetector(this.context,object : GestureDetector.SimpleOnGestureListener(){
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            findChildViewUnder(e.x,e.y)?.let { child->
                val position = getChildAdapterPosition(child)
                callback(position,child)
            }
            return true
        }

    })
    addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener(){
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            detector.onTouchEvent(e)
            return false
        }
    })
}