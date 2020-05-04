package com.kholoud.neamapp.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kholoud.neamapp.R;
import com.kholoud.neamapp.data.NeamNote;
import com.kholoud.neamapp.viewsmodels.NeamNotesVM;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NeamNotesAdapter.ListItemClickListener {

    private NeamNotesVM mNoteViewModel;
    @BindView(R.id.emptylist_img)
    ImageView emptyListImg;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Parcelable savedRecyclerLayoutState;
    private static final String RV_POSITION_INSTANCESTATE = "NOTES_RECYCLE_STATE";
    public final static String NeamNoteStr ="NeamNote_widget";
    List<NeamNote> notesList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            savedRecyclerLayoutState = savedInstanceState.getParcelable(RV_POSITION_INSTANCESTATE);
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);


            }
        });

        // init VM
        mNoteViewModel = new ViewModelProviders().of(this).get(NeamNotesVM.class);

        //Add RecyclerView
        recyclerView = findViewById(R.id.recyclerview);
        final NeamNotesAdapter adapter = new NeamNotesAdapter(this,MainActivity.this );
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_slide_right);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.setAdapter(adapter);

        mNoteViewModel.getAllNotes().observe(this, notes -> {
            // Update the cached copy of the words in the adapter.
            if (notes.size()>0) {
                notesList=notes;
                adapter.setNotes(notes);
                LayoutAnimationController animationController =
                        AnimationUtils.loadLayoutAnimation(recyclerView.getContext(), R.anim.layout_animation_slide_right);
                recyclerView.setLayoutAnimation(animationController);
                runLayoutAnimation(recyclerView);
                if (savedRecyclerLayoutState != null) {
                    mLayoutManager.onRestoreInstanceState(savedRecyclerLayoutState);
                }
                emptyListImg.setVisibility(View.INVISIBLE);

            }else
                emptyListImg.setVisibility(View.VISIBLE);
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addto_widget) {
            SharedPreferences sf = getSharedPreferences(NeamNoteStr , Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sf.edit();
            editor.putString(NeamNoteStr ,getNoteList(notesList));
            editor.apply();

            Snackbar.make(findViewById(android.R.id.content), getString(R.string.widget_add_message), Snackbar.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }


// to handle
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RV_POSITION_INSTANCESTATE, recyclerView.getLayoutManager().onSaveInstanceState());
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_right);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    public void onListItemClick(NeamNote selectedNote) {
        Intent NoteIntent = new Intent(this, AddNoteActivity.class);
        NoteIntent.putExtra("note",selectedNote);
        startActivity(NoteIntent);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);


    }

    public String getNoteList(List<NeamNote> notesList){
        StringBuilder NoteListStr = new StringBuilder();
        for (NeamNote ing: notesList) {
            NoteListStr.append(" ❥ " +ing.getDescription()+"\n");
        }
        return NoteListStr.toString();
    }

}
