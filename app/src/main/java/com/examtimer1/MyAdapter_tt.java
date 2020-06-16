package com.examtimer1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
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

public class MyAdapter_tt extends RecyclerView.Adapter<MyAdapter_tt.MyViewHolder> {
    private static Context mContext;
    private ArrayList<TimerRecordItem_tt> rvList;
    int position;
    private DBHelper_tt mDBHelper;
//    private String tableName;

    private static OnItemClickListener mListener=null;

    public void setOnClickListener(OnItemClickListener listener){
        this.mListener=listener;
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitle, mDate, mMenu;
//        public Menu mMenu;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTitle=itemView.findViewById(R.id.TextView_recyclerview_item_title_tt);
            mDate=itemView.findViewById(R.id.TextView_recyclerview_item_date_tt);
            mMenu=itemView.findViewById(R.id.TextView_menu_recyclerview_item_tt);
//            mMenu=itemView.findViewById(R.id.menu_recyclerview_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent=new Intent(mContext, Record_total_specific.class);
//                    mContext.startActivity(intent);
                    int pos=getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION)
                        if(mListener!=null) {
                            mListener.onItemClick(v, pos);
                            Log.d("TAG", "클릭되는거냐이거22");
                        }
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter_tt(ArrayList<TimerRecordItem_tt> items, Context context) {
        rvList=items;
        mContext=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter_tt.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_subject_tt, parent, false);
//            ...
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.mTitle.setText(rvList.get(position).getTitle());
        holder.mDate.setText(rvList.get(position).getDate());
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
                                mDBHelper=new DBHelper_tt(mContext);
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
