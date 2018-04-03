package infoex.cn.xbc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import infoex.cn.xbc.R;
import infoex.cn.xbc.bean.Record;

/**
 * Created by ibm on 2017/9/6.
 */
public class RecordAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<Record> mList;

    public RecordAdapter(Context mContext, ArrayList<Record> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }
    public void initData(ArrayList<Record> list){
        if (mList!=null){
            mList.clear();
        }
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view = null;
        view = View.inflate(mContext, R.layout.item_record, null);
        holder = new RecordViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Record record = mList.get(position);
        RecordViewHolder recordViewHolder = (RecordViewHolder) holder;
        recordViewHolder.mTvdoorway.setText(record.getGateName());
        recordViewHolder.mTvtime.setText(record.getTime());
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }

    class RecordViewHolder extends RecyclerView.ViewHolder {

        TextView mTvtime;
        TextView mTvdoorway;

        public RecordViewHolder(View view) {
            super(view);
            mTvtime = (TextView) view.findViewById(R.id.mtv_re_time);
            mTvdoorway = (TextView) view.findViewById(R.id.mtv_re_doorway);
        }
    }
}
