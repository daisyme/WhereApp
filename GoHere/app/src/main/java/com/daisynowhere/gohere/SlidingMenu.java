package com.daisynowhere.gohere;

/**
 * Created by Administrator on 2016/5/23.
 */

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class SlidingMenu extends ViewGroup{
    private static final String TAG = SlidingMenu.class.getName();
    private enum  Scroll_State {Scroll_to_Open, Scroll_to_Close; }
    private Scroll_State state;
    private int mMostRecentX;
    private int downX;
    private boolean isOpen = false;

    private View menu;
    private View mainView;
    private Scroller mScroller;

    private void init(Context context){
        mScroller = new Scroller(context);
    }

    public void setMainView(View view){
        mainView = view;
        addView(mainView);
    }

    public void setMenu(View view){
        menu = view;
        addView(menu);
    }

    public SlidingMenu(Context context, View main, View menu){
        super(context); // TODO Auto-generated constructor stub
        setMainView(main);
        setMenu(menu);
        init(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mainView.measure(widthMeasureSpec, heightMeasureSpec);
        menu.measure(widthMeasureSpec - 150, heightMeasureSpec);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMostRecentX = (int) event.getX();
                downX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int deltaX = mMostRecentX - moveX; // ����ڲ˵���ʱ���һ������˵��ر�ʱ���󻬶����ᴥ��Scroll�¼�
                if ((!isOpen && (downX - moveX) < 0) || (isOpen && (downX - moveX) > 0)) {
                    scrollBy(deltaX / 2, 0);
                }
                mMostRecentX = moveX;
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int dx = upX - downX;
                if (!isOpen) {
                // �˵��ر�ʱ
                // ���һ�������menuһ���ȲŻ�򿪲˵�
                if (dx > menu.getMeasuredWidth() / 3) {
                    state = Scroll_State.Scroll_to_Open;
                }
                else {
                    state = Scroll_State.Scroll_to_Close;
                } }
                else {
                // �˵���ʱ
                // ������ʱ�Ĵ�������menu����ʱ��ֻ�����󻬶�����menu��һ�룬�Ż�ر�
                // ������ʱ�Ĵ�������main����ʱ���������ر�
                if (downX < menu.getMeasuredWidth()) {
                    if (dx < -menu.getMeasuredWidth() / 3) {
                        state = Scroll_State.Scroll_to_Close; }
                    else {
                        state = Scroll_State.Scroll_to_Open; }
                } else {
                    state = Scroll_State.Scroll_to_Close;
                }
                }
                smoothScrollto();
                break;
            default:
                break;
        }
        return true;
    }

    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
        }
        invalidate();
    }

    public void open() {
        state = Scroll_State.Scroll_to_Open;
        smoothScrollto();
    }

    public void close() {
        state = Scroll_State.Scroll_to_Close;
        smoothScrollto();
    }

    private void smoothScrollto() {
        int scrollx = getScrollX();
        switch (state) {
            case Scroll_to_Close:
                mScroller.startScroll(scrollx, 0, -scrollx, 0, 500);
                isOpen = false;
                break;
            case Scroll_to_Open:
                mScroller.startScroll(scrollx, 0, -scrollx - menu.getMeasuredWidth(), 0, 500);
                isOpen = true;
                break;
            default:
                break;
        }
    }

    protected void onLayout(boolean arg0, int l, int t, int r, int b) {
        // TODO Auto-generated method stub
        mainView.layout(l, t, r, b);
        menu.layout(-menu.getMeasuredWidth(), t, 0, b);
    }

}
