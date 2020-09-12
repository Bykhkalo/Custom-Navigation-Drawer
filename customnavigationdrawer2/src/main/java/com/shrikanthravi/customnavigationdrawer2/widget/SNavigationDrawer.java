package com.shrikanthravi.customnavigationdrawer2.widget;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;

import androidx.annotation.IntDef;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shrikanthravi.customnavigationdrawer2.R;
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class SNavigationDrawer extends RelativeLayout {


    //Context
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    //Layouts
    protected List<MenuItem> menuItemList;
    protected RelativeLayout rootLayout;
    protected RelativeLayout appbarRL;
    protected CardView containerCV;
    protected TextView appbarTitleTV;
    protected ImageView menuIV;
    protected ScrollView menuSV;
    protected LinearLayout menuLL;
    protected LinearLayout containerLL;



    //Defaults
    private static final int DEFAULT_ANIMATION_DURATION = 500;
    private static final int DEFAULT_TEXT_SIZE = 20;
    private static final int DEFAULT_MENU_ICON_SIZE = 20;
    private static final int DEFAULT_RADIUS = 60;
    private static final float DEFAULT_DRAWER_ANIM_DISTANCE = 0.625f;

    //Customization Variables
    private int appbarColor = R.color.White;
    private int appbarTitleTextColor = R.color.Black;
    private int menuItemSemiTransparentColor = getResources().getColor(R.color.transparent_black_percent_60);
    private int navigationDrawerBackgroundColor = getResources().getColor(R.color.White);
    private int primaryMenuItemTextColor = getResources().getColor(R.color.White);
    private int secondaryMenuItemTextColor = getResources().getColor(R.color.Black);
    private int menuIconTintColor = getResources().getColor(R.color.Black);
    private float menuIconSize = DEFAULT_MENU_ICON_SIZE;
    private float appbarTitleTextSize = DEFAULT_TEXT_SIZE;
    private float primaryMenuItemTextSize = DEFAULT_TEXT_SIZE;
    private float secondaryMenuItemTextSize = DEFAULT_TEXT_SIZE;

    private int appbarHeight = LayoutParams.WRAP_CONTENT;
    private int openCloseAnimDuration = DEFAULT_ANIMATION_DURATION;
    private int radius = DEFAULT_RADIUS;
    private int menuAnimDuration = DEFAULT_ANIMATION_DURATION;
    private float drawerAnimDistance = DEFAULT_DRAWER_ANIM_DISTANCE;


    //Other stuff
    private boolean navOpen = false;
    private int currentPos = 0;
    float centerX, centerY;

    @IntDef({STATE_OPEN, STATE_CLOSED, STATE_OPENING, STATE_CLOSING})
    @Retention(RetentionPolicy.SOURCE)
    private @interface State {
    }

    //Indicates that any drawer is open. No animation is in progress.
    public static final int STATE_OPEN = 0;

    //Indicates that any drawer is closed. No animation is in progress.
    public static final int STATE_CLOSED = 1;

    //Indicates that a drawer is in the process of opening.
    public static final int STATE_OPENING = 2;

    //Indicates that a drawer is in the process of closing.
    public static final int STATE_CLOSING = 3;

    //Listeners
    private OnHamMenuClickListener onHamMenuClickListener;
    private OnMenuItemClickListener onMenuItemClickListener;
    private DrawerListener drawerListener;


    public SNavigationDrawer(Context context) {
        super(context);
    }

    public SNavigationDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SNavigationDrawer,
                0, 0);
        setAttributes(a);
        a.recycle();

    }

    //Adding the child views inside CardView LinearLayout
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (containerLL == null) {
            super.addView(child, index, params);
        } else {
            //Forward these calls to the content view
            containerLL.addView(child, index, params);
        }
    }

    //Initialization
    @SuppressLint("ClickableViewAccessibility")
    public void init(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        //Load RootView from xml
        View rootView = mLayoutInflater.inflate(R.layout.widget_navigation_drawer, this);
        rootLayout = (RelativeLayout) rootView;
        appbarRL = rootView.findViewById(R.id.appBarRL);
        containerCV = rootView.findViewById(R.id.containerCV);
        appbarTitleTV = rootView.findViewById(R.id.appBarTitleTV);
        menuIV = rootView.findViewById(R.id.menuIV);
        menuSV = rootView.findViewById(R.id.menuSV);
        menuLL = rootView.findViewById(R.id.menuLL);
        containerLL = rootView.findViewById(R.id.containerLL);

        menuItemList = new ArrayList<>();


        menuIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hamMenuClicked();
                if (navOpen) {
                    closeDrawer();
                } else {
                    openDrawer();
                }
            }
        });


        //Adding swipe event handling
        containerLL.setOnTouchListener(new OnTouchListener() {

            int downX, upX;

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    downX = (int) event.getX();
                    //Log.d("tag", " downX " + downX);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    upX = (int) event.getX();
                    //Log.d("tag", " upX " + upX);
                    if (upX - downX > 100) {
                        if (!navOpen)
                            openDrawer();
                    } else if (downX - upX > -100) {
                        if (navOpen)
                            closeDrawer();
                        // swipe left
                    }
                    return true;

                }
                return false;
            }
        });

    }

    protected void initMenu() {
        for (int i = 0; i < menuItemList.size(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.menu_row_item, null);

            TextView titleTV = view.findViewById(R.id.titleTV);
            TextView titleTV1 = view.findViewById(R.id.titleTV1);
            ImageView backgroundIV = view.findViewById(R.id.backgroundIV);
            CardView backgroundCV = view.findViewById(R.id.backgroundCV);
            View tintView = (View) view.findViewById(R.id.tintView);
            tintView.setBackgroundColor(menuItemSemiTransparentColor);
            titleTV.setTextColor(secondaryMenuItemTextColor);
            titleTV1.setTextColor(primaryMenuItemTextColor);
            titleTV.setTextSize(secondaryMenuItemTextSize);
            titleTV1.setTextSize(primaryMenuItemTextSize);
            final RelativeLayout rootRL = view.findViewById(R.id.rootRL);
            backgroundCV.setTag("cv" + i);
            System.out.println("Testing " + backgroundCV.getTag());
            titleTV.setTag("tv" + i);

            rootRL.setTag(i);
            rootRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //fix to avoid handling multiple clicks in one animation
                    if (!navOpen) return;

                    if (currentPos != Integer.parseInt(view.getTag().toString())) {
                        final CardView backCV1 = (CardView) menuLL.findViewWithTag("cv" + currentPos);
                        final TextView title1 = (TextView) menuLL.findViewWithTag("tv" + currentPos);

                        //Log.d("tag", "onClick: rootRL X: " + rootRL.getX() + " backCV1 Width: " + backCV1.getWidth());
                        backCV1.animate().translationX(rootRL.getX() - backCV1.getWidth()).setDuration(menuAnimDuration).start();

                        currentPos = Integer.parseInt(view.getTag().toString());
                        menuItemClicked(currentPos);


                        appbarTitleTV.setText(menuItemList.get(currentPos).getTitle());

                        final CardView backCV = (CardView) menuLL.findViewWithTag("cv" + currentPos);
                        final TextView title = (TextView) menuLL.findViewWithTag("tv" + currentPos);
                        backCV.setVisibility(View.VISIBLE);
                        // Log.d("tag", "onClick: Drawer Testing: " + backCV.getTag());
                        Log.d("tag", "anim: visibility:" + backCV.getVisibility());

                        Log.d("tag", "firstAnim: rootRL.getX(): " + rootRL.getX() + " backCV.getWidth(): " + backCV.getWidth());
                        //backCV.animate().translationX(rootRL.getX() - backCV.getWidth()).setDuration(1300).start();

                        Log.d("tag", "secondAnim: rootRL.getX(): " + rootRL.getX() + " backCV.getWidth(): " + backCV.getWidth());
                        backCV.animate().translationX(rootRL.getX()).setDuration(menuAnimDuration)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        title1.setVisibility(VISIBLE);
                                        backCV1.setVisibility(GONE);
                                    }
                                })
                                .start();

                        backCV.setVisibility(View.VISIBLE);
                        title.setVisibility(View.GONE);




                        //Close Navigation Drawer
                        closeDrawer();
                    } else {
                        menuItemClicked(currentPos);
                        closeDrawer();
                    }
                }
            });
            backgroundIV.setImageDrawable(ContextCompat.getDrawable(getContext(), menuItemList.get(i).getImageId()));
            titleTV.setText(menuItemList.get(i).getTitle());
            titleTV1.setText(menuItemList.get(i).getTitle());
            menuLL.addView(view);

            if (i >= 1) {
                //Cleaning items of their backgroundCVs
                backgroundCV.setVisibility(View.GONE);
                Log.d("tag", "INITIAL: rootRL.getX(): " + rootRL.getX() + " backgroundCV.getWidth(): " + backgroundCV.getWidth());
                //backgroundCV.animate().translationX(rootRL.getX() - backgroundCV.getWidth()).setDuration(1).start();


                /*This is an approximate calculation of the default backCV1 width
                  I cant get it from backCV1.getWidth() because it always return 0.
                  It will return real width after measuring.*/
                float point = Resources.getSystem().getDisplayMetrics().widthPixels * 0.6f;

                Log.d("tag", "initMenu: width: " + Resources.getSystem().getDisplayMetrics().widthPixels + " X: " + point);

                final CardView backCV1 = (CardView) menuLL.findViewWithTag("cv" + i);
                backCV1.animate().translationX(rootRL.getX() - point).setDuration(1).start();


               titleTV.setVisibility(View.VISIBLE);
            }
        }
    }

    //Hamburger button Click Listener
    public interface OnHamMenuClickListener {

        public void onHamMenuClicked();

    }

    //Listener for menu item click
    public interface OnMenuItemClickListener {

        public void onMenuItemClicked(int position);

    }

    //Listener for monitoring events about drawer.
    public interface DrawerListener {

        //Called when a drawer is opening.
        void onDrawerOpening();

        //Called when a drawer is closing.
        void onDrawerClosing();

        //Called when a drawer has settled in a completely open state.
        void onDrawerOpened();

        //Called when a drawer has settled in a completely closed state.
        void onDrawerClosed();

        //Called when the drawer motion state changes. The new state will
        void onDrawerStateChanged(@State int newState);

    }

    public OnHamMenuClickListener getOnHamMenuClickListener() {
        return onHamMenuClickListener;
    }

    public void setOnHamMenuClickListener(OnHamMenuClickListener onHamMenuClickListener) {
        this.onHamMenuClickListener = onHamMenuClickListener;
    }

    public OnMenuItemClickListener getOnMenuItemClickListener() {
        return onMenuItemClickListener;
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    public DrawerListener getDrawerListener() {
        return drawerListener;
    }

    public void setDrawerListener(DrawerListener drawerListener) {
        this.drawerListener = drawerListener;
    }

    protected void hamMenuClicked() {
        if (onHamMenuClickListener != null) {
            onHamMenuClickListener.onHamMenuClicked();
        }
    }

    protected void menuItemClicked(int position) {
        if (onMenuItemClickListener != null) {
            onMenuItemClickListener.onMenuItemClicked(position);

        }
    }

    protected void drawerOpened() {
        if (drawerListener != null) {
            drawerListener.onDrawerOpened();
            drawerListener.onDrawerStateChanged(STATE_OPEN);
        }
    }

    protected void drawerClosed() {
        System.out.println("Drawer Closing");
        if (drawerListener != null) {
            drawerListener.onDrawerClosed();
            drawerListener.onDrawerStateChanged(STATE_CLOSED);
        }
    }

    protected void drawerOpening() {
        if (drawerListener != null) {
            drawerListener.onDrawerOpening();
            drawerListener.onDrawerStateChanged(STATE_OPENING);
        }
    }

    protected void drawerClosing() {
        if (drawerListener != null) {
            drawerListener.onDrawerClosing();
            drawerListener.onDrawerStateChanged(STATE_CLOSING);
        }
    }

    private void animateClosing(int duration) {
        appbarTitleTV.animate().translationX(centerX).setDuration(duration).start();
        containerCV.animate().translationX(rootLayout.getX()).translationY(rootLayout.getY()).setDuration(duration).start();

        ObjectAnimator
                .ofFloat(containerCV, "radius", containerCV.getRadius(), 1)
                .setDuration(duration)
                .start();


        ObjectAnimator
                .ofFloat(containerCV, "elevation", containerCV.getCardElevation(), 0)
                .setDuration(duration)
                .start();

    }

    private void animateOpening(int duration) {

        ObjectAnimator
                .ofFloat(containerCV, "radius", containerCV.getRadius(), radius)
                .setDuration(duration)
                .start();


        ObjectAnimator
                .ofFloat(containerCV, "elevation", containerCV.getCardElevation(), 100)
                .setDuration(duration)
                .start();

        appbarTitleTV.animate().translationX(centerX + menuIV.getWidth() + menuIV.getWidth() / 4 + appbarTitleTV.getWidth() / 2 - appbarRL.getWidth() / 2).setDuration(openCloseAnimDuration).start();
        containerCV.animate().translationX(rootLayout.getX() + rootLayout.getWidth() * drawerAnimDistance).translationY(250).setDuration(openCloseAnimDuration).start();

    }

    //Closes drawer
    public void closeDrawer() {
        drawerClosing();
        navOpen = false;
        final int[] stateSet = {android.R.attr.state_checked * (navOpen ? 1 : -1)};
        menuIV.setImageState(stateSet, true);
        animateClosing(openCloseAnimDuration);
    }

    //Opens Drawer
    public void openDrawer() {

        drawerOpening();
        navOpen = true;
        final int[] stateSet = {android.R.attr.state_checked * (navOpen ? 1 : -1)};
        menuIV.setImageState(stateSet, true);
        animateOpening(openCloseAnimDuration);
        drawerOpened();
    }

    //set Attributes from xml
    protected void setAttributes(TypedArray attrs) {

        setAppbarColor(attrs.getColor(R.styleable.SNavigationDrawer_appbarColor, getResources().getColor(appbarColor)));
        setAppbarTitleTextColor(attrs.getColor(R.styleable.SNavigationDrawer_appbarTitleTextColor, getResources().getColor(appbarTitleTextColor)));
        setMenuiconTintColor(attrs.getColor(R.styleable.SNavigationDrawer_HamMenuIconTintColor, menuIconTintColor));
        setMenuItemSemiTransparentColor(attrs.getColor(R.styleable.SNavigationDrawer_HamMenuItemSemiTransparentColor, menuItemSemiTransparentColor));
        setNavigationDrawerBackgroundColor(attrs.getColor(R.styleable.SNavigationDrawer_navigationDrawerBackgroundColor, navigationDrawerBackgroundColor));
        setPrimaryMenuItemTextColor(attrs.getColor(R.styleable.SNavigationDrawer_navigationDrawerBackgroundColor, primaryMenuItemTextColor));
        setSecondaryMenuItemTextColor(attrs.getColor(R.styleable.SNavigationDrawer_secondaryMenuItemTextColor, secondaryMenuItemTextColor));
        setAppbarTitleTextSize(attrs.getDimension(R.styleable.SNavigationDrawer_appbarTitleTextSize, DEFAULT_TEXT_SIZE));
        setPrimaryMenuItemTextSize(attrs.getDimension(R.styleable.SNavigationDrawer_primaryMenuItemTextSize, DEFAULT_TEXT_SIZE));
        setSecondaryMenuItemTextSize(attrs.getDimension(R.styleable.SNavigationDrawer_secondaryMenuItemTextSize, DEFAULT_TEXT_SIZE));
        setMenuIconSize(attrs.getDimension(R.styleable.SNavigationDrawer_HamMenuIconSize, DEFAULT_MENU_ICON_SIZE));

        drawerAnimDistance = attrs.getFraction(R.styleable.SNavigationDrawer_drawerAnimDistance, 1, 1, DEFAULT_DRAWER_ANIM_DISTANCE);
        radius = (int) attrs.getDimension(R.styleable.SNavigationDrawer_radius, DEFAULT_RADIUS);
        openCloseAnimDuration = attrs.getInteger(R.styleable.SNavigationDrawer_openCloseAnimationDuration, DEFAULT_ANIMATION_DURATION);
        menuAnimDuration = attrs.getInteger(R.styleable.SNavigationDrawer_menuAnimationDuration, DEFAULT_ANIMATION_DURATION);

        appbarRL.getLayoutParams().height = (int) attrs.getDimension(R.styleable.SNavigationDrawer_appbarHeight, LayoutParams.WRAP_CONTENT);

    }


    //To change the AppBar Title
    public void setAppbarTitleTV(String name) {
        appbarTitleTV.setText(name);
    }

    //To check if drawer is open or not
    public boolean isDrawerOpen() {
        return navOpen;
    }

    //Adding menu to drawer
    public void addMenuItem(MenuItem menuItem) {
        if (menuItemList != null) {
            menuItemList.add(menuItem);
        }
    }

    //Getting the list of Menu Items
    public List<MenuItem> getMenuItemList() {
        return menuItemList;
    }

    //Setting the list of Menu Items
    public void setMenuItemList(List<MenuItem> menuItemList) {
        this.menuItemList = menuItemList;
        initMenu();
    }

    /*
     *
     * Customization :)
     *
     */


    public int getMenuAnimDuration() {
        return menuAnimDuration;
    }

    public void setMenuAnimDuration(int menuAnimDuration) {
        this.menuAnimDuration = menuAnimDuration;
    }

    public float getDrawerAnimDistance() {
        return drawerAnimDistance;
    }

    public void setDrawerAnimDistance(float drawerAnimDistance) {
        this.drawerAnimDistance = drawerAnimDistance;
    }

    public int getOpenCloseAnimDuration() {
        return openCloseAnimDuration;
    }

    public void setOpenCloseAnimDuration(int openCloseAnimDuration) {
        this.openCloseAnimDuration = openCloseAnimDuration;
    }

    public int getAppbarHeight() {
        return appbarHeight;
    }

    public void setAppbarHeight(int appbarHeight) {
        this.appbarHeight = appbarHeight;
    }



    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getAppbarColor() {
        return appbarColor;
    }

    public void setAppbarColor(int appbarColor) {
        this.appbarColor = appbarColor;
        appbarRL.setBackgroundColor(appbarColor);
    }

    public int getAppbarTitleTextColor() {
        return appbarTitleTextColor;
    }

    public void setAppbarTitleTextColor(int appbarTitleTextColor) {
        this.appbarTitleTextColor = appbarTitleTextColor;
        appbarTitleTV.setTextColor(appbarTitleTextColor);
    }

    public float getAppbarTitleTextSize() {
        return appbarTitleTextSize;
    }

    public void setAppbarTitleTextSize(float appbarTitleTextSize) {
        this.appbarTitleTextSize = appbarTitleTextSize;
        appbarTitleTV.setTextSize(appbarTitleTextSize);
    }

    public int getMenuiconTintColor() {
        return menuIconTintColor;
    }

    public void setMenuiconTintColor(int menuIconTintColor) {
        this.menuIconTintColor = menuIconTintColor;
        menuIV.setColorFilter(menuIconTintColor);
    }

    public float getMenuIconSize() {
        return menuIconSize;
    }

    public void setMenuIconSize(float menuIconSize) {
        //Todo Change Icon Size
        this.menuIconSize = menuIconSize;
    }

    public int getMenuItemSemiTransparentColor() {
        return menuItemSemiTransparentColor;
    }

    public void setMenuItemSemiTransparentColor(int menuItemSemiTransparentColor) {
        this.menuItemSemiTransparentColor = menuItemSemiTransparentColor;
        invalidate();
    }

    public int getNavigationDrawerBackgroundColor() {
        return navigationDrawerBackgroundColor;
    }

    public void setNavigationDrawerBackgroundColor(int navigationDrawerBackgroundColor) {
        rootLayout.setBackgroundColor(navigationDrawerBackgroundColor);
        this.navigationDrawerBackgroundColor = navigationDrawerBackgroundColor;
    }

    public int getPrimaryMenuItemTextColor() {
        return primaryMenuItemTextColor;
    }

    public void setPrimaryMenuItemTextColor(int primaryMenuItemTextColor) {
        this.primaryMenuItemTextColor = primaryMenuItemTextColor;
        invalidate();
    }

    public int getSecondaryMenuItemTextColor() {
        return secondaryMenuItemTextColor;
    }

    public void setSecondaryMenuItemTextColor(int secondaryMenuItemTextColor) {
        this.secondaryMenuItemTextColor = secondaryMenuItemTextColor;
        invalidate();
    }

    public float getPrimaryMenuItemTextSize() {
        return primaryMenuItemTextSize;

    }

    public void setPrimaryMenuItemTextSize(float primaryMenuItemTextSize) {
        this.primaryMenuItemTextSize = primaryMenuItemTextSize;
        invalidate();
    }

    public float getSecondaryMenuItemTextSize() {
        return secondaryMenuItemTextSize;
    }

    public void setSecondaryMenuItemTextSize(float secondaryMenuItemTextSize) {
        this.secondaryMenuItemTextSize = secondaryMenuItemTextSize;
        invalidate();
    }

    //to change the typeface of appbar title
    public void setAppbarTitleTypeface(Typeface titleTypeface) {
        appbarTitleTV.setTypeface(titleTypeface);
    }
}
