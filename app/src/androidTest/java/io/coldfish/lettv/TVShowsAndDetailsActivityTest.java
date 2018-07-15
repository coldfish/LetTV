package io.coldfish.lettv;

import android.app.Activity;
import android.arch.core.executor.testing.CountingTaskExecutorRule;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


// Disable animations for accurate test results.
public class TVShowsAndDetailsActivityTest {

    @Rule
    public ActivityTestRule<TVShowsActivity> rule = new ActivityTestRule<>(TVShowsActivity.class);
    @Rule
    public CountingTaskExecutorRule testRule = new CountingTaskExecutorRule();
    private Activity activity;

    @Before
    public void disableRecyclerViewAnimations() {
        // Disable RecyclerView animations
        EspressoTestUtil.disableAnimations(rule);
    }

    @Before
    public void setUp() throws Exception {
        Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), TVShowsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity = InstrumentationRegistry.getInstrumentation().startActivitySync(intent);
        drain();
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }

    @Test
    public void checkEmptyView() {
        LinearLayout viewById = activity.findViewById(R.id.noData);
        assertThat(viewById, instanceOf(LinearLayout.class));
        assertEquals(viewById.getVisibility(), View.GONE);
    }

    @Test
    public void checkRecyclerView() {
        final RecyclerView recyclerView = activity.findViewById(R.id.tv_shows_list);
        assertThat(recyclerView, instanceOf(RecyclerView.class));
        assertEquals(recyclerView.getVisibility(), View.VISIBLE);
    }

    @Test
    public void showItemsOnTheList() throws InterruptedException {
        final RecyclerView recyclerView = activity.findViewById(R.id.tv_shows_list);
        waitForAdapterChange(recyclerView);
        assertThat(recyclerView.getAdapter(), notNullValue());
        waitForAdapterChange(recyclerView);
        assertThat(recyclerView.getAdapter().getItemCount() > 0, is(true));
    }

    private void waitForAdapterChange(final RecyclerView recyclerView) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                recyclerView.getAdapter().registerAdapterDataObserver(
                        new RecyclerView.AdapterDataObserver() {
                            @Override
                            public void onItemRangeInserted(int positionStart, int itemCount) {
                                latch.countDown();
                            }

                            @Override
                            public void onChanged() {
                                latch.countDown();
                            }
                        });
            }
        });
        if (recyclerView.getAdapter().getItemCount() > 0) {
            return;
        }
        assertThat(latch.await(10, TimeUnit.SECONDS), is(true));
    }

    @Test
    public void scrollTVShowsAndOpenDetails() throws Throwable {
        final RecyclerView recyclerView = activity.findViewById(R.id.tv_shows_list);
        waitForAdapterChange(recyclerView);
        onView(withId(R.id.noData)).check(matches(not(isDisplayed())));
        drain();
        Matcher<View> viewMatcher = withId(R.id.tv_shows_list);
        onView(viewMatcher).check(matches(isDisplayed()));
        drain();
        onView(viewMatcher).perform(RecyclerViewActions.scrollToPosition(17));
        Thread.sleep(5000);
        onView(viewMatcher).perform(RecyclerViewActions.scrollToPosition(25));
        drain();
        onView(viewMatcher).perform(RecyclerViewActions.actionOnItemAtPosition(25, click()));
        drain();
        onView(withId(R.id.view_pager))
                .check(matches(isDisplayed()));
    }

    @Test
    public void enterDetailsActivityAndChecks() throws Throwable {
        final RecyclerView recyclerView = activity.findViewById(R.id.tv_shows_list);
        waitForAdapterChange(recyclerView);
        onView(withId(R.id.tv_shows_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        drain();
        Matcher<View> viewMatcher = withId(R.id.view_pager);
        onView(viewMatcher).check(matches(isDisplayed()));
        drain();
        onView(viewMatcher).perform(swipeLeft());
        drain();
        onView(allOf(withId(R.id.header_image), isDisplayed()));
        onView(allOf(withId(R.id.vote_count), isDisplayed()));
        onView(allOf(withId(R.id.vote_average), isDisplayed()));
        onView(allOf(withId(R.id.overview), isDisplayed()));
        onView(viewMatcher).perform(swipeLeft());
        onView(viewMatcher).perform(swipeRight());
        onView(viewMatcher).perform(swipeRight());
        Espresso.pressBack();
    }


    private void drain() throws TimeoutException, InterruptedException {
        testRule.drainTasks(10, TimeUnit.SECONDS);
    }
}