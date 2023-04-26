package com.example.shtreet;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)
public class ShtreetInstrumentedTest {

    @Rule
    public ActivityTestRule<ResultActivity> activityRule
            = new ActivityTestRule<>(
            ResultActivity.class,
            true,     // initialTouchMode
            false);


    /**
     * This tests the outcomes with a "easy" image to analyse
     */
    @Test
    public void testEasySuccessCase() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.shtreet", appContext.getPackageName());

        Intent intent = new Intent();
        //Get the image to test
        Uri uri = Uri.parse("android.resource://com.example.shtreet/drawable/paix");

        intent.putExtra("uri", uri.toString());

        //Launch the activity
        activityRule.launchActivity(intent);

        //Wait at least two seconds so the app can do the api request in time
        onView(isRoot()).perform(waitFor(2000));

        //Expected outcomes
        String expectedStreetName = "Votre texte extrait : RUE de la Paix";
        String expectedStreetDetails = "Histoire : Précédemment, rue Napoléon.\n\nDescription : Substitué à celui de Napoléon, en 1814, après la signature du traité de paix.\n";

        //Text Views with "recognizedTextView" and "history" ids matches the expected outcomes
        onView(withId(R.id.recognizedTextView)).check(matches(withText(expectedStreetName)));
        onView(withId(R.id.history)).check(matches(withText(expectedStreetDetails)));
        //The error Text View is not displayed
        onView(withId(R.id.errorTextView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));


    }

    /**
     * This tests the outcomes with a "hard" image to analyse
     */
    @Test
    public void testHardSuccessCase() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.shtreet", appContext.getPackageName());

        Intent intent = new Intent();
        //Get the image to test
        Uri uri = Uri.parse("android.resource://com.example.shtreet/drawable/montparnasse");

        intent.putExtra("uri", uri.toString());

        //Launch the activity
        activityRule.launchActivity(intent);

        //Wait at least two seconds so the app can do the api request in time
        onView(isRoot()).perform(waitFor(2000));

        //Expected outcomes
        String expectedStreetName = "Votre texte extrait : L4AEME ARR BOULEVAAD MONTPARNASSE 14.ARR BOULEVARD DU MONTPARNASSE";
        String expectedStreetDetails = "Histoire : Il est indiqué en partie sur le plan de Lacaille (1714), avec une interruption au droit de la butte du Montparnasse.\n\nDescription : Ouvert sur l'emplacement de la butte Montparnasse, ainsi nommée parce que les écoliers de l'Université s'y assemblaient pour y faire des lectures poétiques.\n";

        //Text Views with "recognizedTextView" and "history" ids matches the expected outcomes
        onView(withId(R.id.recognizedTextView)).check(matches(withText(expectedStreetName)));
        onView(withId(R.id.history)).check(matches(withText(expectedStreetDetails)));
        //The error Text View is not displayeds
        onView(withId(R.id.errorTextView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    /**
     * This tests the outcomes with an image that is not a road.
     */
    @Test
    public void testFailureCase() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.shtreet", appContext.getPackageName());

        Intent intent = new Intent();
        //Get the image to test
        Uri uri = Uri.parse("android.resource://com.example.shtreet/drawable/choix");

        intent.putExtra("uri", uri.toString());

        //Launch the activity
        activityRule.launchActivity(intent);

        //Wait at least two seconds so the app can do the api request in time
        onView(isRoot()).perform(waitFor(2000));

        //Expected outcome
        StringBuilder expectedErrorMessage = new StringBuilder();
        expectedErrorMessage.append("Une erreur s'est produite ! Cela peut être dû à plusieurs choses : \n");
        expectedErrorMessage.append("\n");
        expectedErrorMessage.append("1. Le texte reconnu n'est pas une rue, pour rappel voici les types de rues pris en compte : \n ");
        expectedErrorMessage.append("\n");
        int count = 0;
        for (String r : activityRule.getActivity().roadTypes) {
            if (count < 4) {
                expectedErrorMessage.append(" ").append(r);
                count++;
            } else {
                expectedErrorMessage.append(" ").append(r).append(" \n");
                count = 0;
            }
        }
        expectedErrorMessage.append("\n 2. Cette erreur peut également se produire si la rue n'est pas une rue de Paris. \n");
        expectedErrorMessage.append("\n");
        expectedErrorMessage.append("3. Nous n'arrivons pas à reconnaitre le texte de votre photo. \n");
        expectedErrorMessage.append("Votre texte extrait : \n");
        expectedErrorMessage.append("TEXTE\nAU CHOIX");


        //Text View with "errorTextView" id matches the expected outcome
        onView(withId(R.id.errorTextView)).check(matches(withText(expectedErrorMessage.toString())));
        //Text View with "recognizedTextView" id is not displayed
        onView(withId(R.id.recognizedTextView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        //The copy button is not displayed
        onView(withId(R.id.copyButton)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    /**
     * wait for a certain delay
     *
     * @param delay the delay
     * @return
     */
    public static ViewAction waitFor(final long delay) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for " + delay + " milliseconds";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(delay);
            }
        };
    }
}