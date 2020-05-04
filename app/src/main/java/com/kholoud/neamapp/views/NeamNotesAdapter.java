package com.kholoud.neamapp.views;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kholoud.neamapp.R;
import com.kholoud.neamapp.data.NeamNote;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class NeamNotesAdapter extends RecyclerView.Adapter<NeamNotesAdapter.NoteViewHolder> {
    final private ListItemClickListener mOnClickListener;


    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private final TextView noteDes;
        private final TextView noteDay;
        private final TextView noteMonth;
        private final TextView noteYear;
        private final ImageView noteImage;



        private NoteViewHolder(View itemView) {
            super(itemView);
            noteDes = itemView.findViewById(R.id.note_des);
            noteDay = itemView.findViewById(R.id.note_day);
            noteMonth = itemView.findViewById(R.id.note_month);
            noteYear = itemView.findViewById(R.id.note_year);
            noteImage= itemView.findViewById(R.id.note_item_image);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            NeamNote clickedNote = mNotes.get(getAdapterPosition());
            mOnClickListener.onListItemClick(clickedNote);
        }
    }

    private final LayoutInflater mInflater;
    private List<NeamNote> mNotes; // Cached copy of words

    NeamNotesAdapter(Context context, ListItemClickListener onClickListener) {
        mInflater = LayoutInflater.from(context);
        mOnClickListener = onClickListener;

    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new NoteViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        if (mNotes != null) {
            NeamNote note = mNotes.get(position);
            holder.noteDes.setText(note.getDescription());
            Calendar noteCal = note.getCreated_at();
            holder.noteDay.setText(String.valueOf(noteCal.get(Calendar.DAY_OF_MONTH)));
            String monthName= new SimpleDateFormat("MMM").format(noteCal.getTime());
            holder.noteMonth.setText(String.valueOf(monthName));
            holder.noteYear.setText(String.valueOf(noteCal.get(Calendar.YEAR)));
            //holder.noteImage.setImageURI(Uri.fromFile(new File(note.getNeamNoteImage())));

            holder.noteImage.setImageBitmap(BitmapFactory.decodeFile(note.getNeamNoteImage()));


        } else {
            // Covers the case of data not being ready yet.
            holder.noteDes.setText("No Word");
        }
    }


    void setNotes(List<NeamNote> words){
        mNotes = words;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mNotes != null)
            return mNotes.size();
        else return 0;
    }
    public interface ListItemClickListener {
        void onListItemClick(NeamNote clickedMovie);
    }

}
