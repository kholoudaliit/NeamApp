package com.kholoud.neamapp.views;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.kholoud.neamapp.R;
import com.kholoud.neamapp.data.NeamNote;
import com.kholoud.neamapp.data.NeamNoteRepoistory;
import com.kholoud.neamapp.viewsmodels.NeamNotesVM;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class NeamAppWidget extends AppWidgetProvider {
    public final static String NeamNoteStr ="NeamNote_widget";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        String NeamNote = context.getSharedPreferences(NeamNoteStr,Context.MODE_PRIVATE).getString(NeamNoteStr , "No Note");
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.neam_app_widget);
        views.setTextViewText(R.id.appwidget_list, NeamNote);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

