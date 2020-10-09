package com.leanplum.customtemplates;

import android.app.Activity;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.leanplum.ActionArgs;
import com.leanplum.ActionContext;
import com.leanplum.Leanplum;
import com.leanplum.LeanplumActivityHelper;
import com.leanplum.callbacks.ActionCallback;
import com.leanplum.callbacks.PostponableAction;

/**
 * Registers a Leanplum action that show the app rating flow for Google Play Store.
 */
public class AppRating {

  private static final String ACTION = "Request App Rating";

  public static void register() {
    Leanplum.defineAction(
        ACTION,
        Leanplum.ACTION_KIND_ACTION,
        new ActionArgs(),
        new ActionCallback() {
          @Override
          public boolean onResponse(ActionContext context) {
            LeanplumActivityHelper.queueActionUponActive(
                new PostponableAction() {
                  @Override
                  public void run() {
                    requestAppRating();
                  }
                });
            return true;
          }
        });
  }

  private static void requestAppRating() {
    Activity activity = LeanplumActivityHelper.getCurrentActivity();
    if (activity == null || activity.isFinishing())
      return;

    ReviewManager manager = ReviewManagerFactory.create(activity);
    Task<ReviewInfo> request = manager.requestReviewFlow();

    request.addOnCompleteListener(requestTask -> {
      if (!requestTask.isSuccessful()) {
        // There is a problem. Probably Google Play Store is missing.
        // If you need the exception call requestTask.getException().
        return;
      }

      Task<Void> flow = manager.launchReviewFlow(activity, requestTask.getResult());
      flow.addOnCompleteListener((reviewTask) -> {
        // The flow has finished. The API does not indicate whether the user
        // reviewed or not, or even whether the review dialog was shown.
      });
    });
  }
}
