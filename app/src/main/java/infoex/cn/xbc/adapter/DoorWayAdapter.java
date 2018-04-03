package infoex.cn.xbc.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import infoex.cn.xbc.R;
import infoex.cn.xbc.bean.Doorway;


/**
 * Created by ibm on 2017/9/5.
 */
public class DoorWayAdapter extends BaseAdapter {
    private List<Doorway> mList;
    private Context mContext;

    public DoorWayAdapter(Context pContext, List<Doorway> pList) {
        this.mContext = pContext;
        this.mList = pList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Doorway getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     * 下面是重要代码
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater=LayoutInflater.from(mContext);
        convertView=_LayoutInflater.inflate(R.layout.item_spinner, null);
        if(convertView!=null) {
            TextView _TextView1=(TextView)convertView.findViewById(R.id.tx_name);
            _TextView1.setText(mList.get(position).getName());
        }
        return convertView;
    }

    public void initData(ArrayList<Doorway> doorways) {
        if (mList!=null){
            mList.clear();
        }
        mList = doorways;
        notifyDataSetChanged();
    }
}
