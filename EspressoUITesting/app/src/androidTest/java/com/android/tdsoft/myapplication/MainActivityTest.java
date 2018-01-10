package com.android.tdsoft.myapplication;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Admin on 4/19/2016.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private String mFirstName, mLastName, mEmail, mAge;


    @Rule
    public final ActivityRule<MainActivity> main = new ActivityRule<>(MainActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        mFirstName = "Tharindu";
        mLastName = "Damintha";
        mEmail = "tharidu@eight25media.com";
        mAge = "25";
    }

    @Test
    public void shouldBeAbleToLaunchScreen() {
        onView(withId(R.id.etName)).perform(typeText(mFirstName), closeSoftKeyboard());
        onView(withId(R.id.etLastName)).perform(typeText(mLastName), closeSoftKeyboard());
        onView(withId(R.id.etAge)).perform(typeText(mAge), closeSoftKeyboard());
        onView(withId(R.id.etEmail)).perform(typeText(mEmail), closeSoftKeyboard());
        onView(withId(R.id.btnOk)).perform(click());

        secondScreenTest();
    }


    public void secondScreenTest(){
        onView(withId(R.id.btnSecOk)).perform(click());
    }
}
