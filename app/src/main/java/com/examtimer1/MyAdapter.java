package com.examtimer1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.examtimer1.examtimer.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<TimerRecordItem> rvList;
    int position;
    private DBHelper mDBHelper;
   private String tableName;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitle, mDate, mTime, mMenu;
//        public Menu mMenu;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTitle=itemView.findViewById(R.id.TextView_recyclerview_item_title);
            mDate=itemView.findViewById(R.id.TextView_recyclerview_item_date);
            mTime=itemView.findViewById(R.id.TextView_recyclerview_item_time);
            mMenu=itemView.findViewById(R.id.TextView_menu_recyclerview_item);
//            mMenu=itemView.findViewById(R.id.menu_recyclerview_item);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<TimerRecordItem> items, Context context, String tableName) {
        rvList=items;
        mContext=context;
        this.tableName=tableName;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_subject, parent, false);
//            ...
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.mTitle.setText(rvList.get(position).getTitle());
        holder.mDate.setText(rvList.get(position).getDate());
        holder.mTime.setText(rvList.get(position).getTime());
        holder.mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup=new PopupMenu(mContext, holder.mMenu);
                popup.inflate(R.menu.recyclerview_item_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.recyclerview_menu_delete:
//                                삭제 버튼을 누르면 item 삭제
                                mDBHelper=new DBHelper(mContext, tableName);
                                SQLiteDatabase db=mDBHelper.getReadableDatabase();
//                                Cursor cursor = mDBHelper.LoadSQLiteDBCursor();
                                mDBHelper.dbDelete(db, rvList.get(position).getId());
                                rvList.remove(position);
//                                삭제된 아이템 반영
                                notifyDataSetChanged();
//                                notifyItemChanged(position);
//                                notifyItemRangeChanged(position, rvList.size());
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

//    // Replace the contents of a view (invoked by the layout manager)
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//        // - get element from your dataset at this position
//        // - replace the contents of the view with that element
////        holder.textView.setText(mDataset[position]);
//
//    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return rvList.size();
    }

}
