package infoex.cn.xbc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import infoex.cn.xbc.R;
import infoex.cn.xbc.bean.Doorway;
import infoex.cn.xbc.bean.Project;

/**
 * Created by ibm on 2017/9/5.
 */
public class ProjectAdapter extends BaseAdapter {
    private List<Project> mList;
    private Context mContext;

    public ProjectAdapter(Context pContext, List<Project> pList) {
        this.mContext = pContext;
        this.mList = pList;
    }

    public void initData(ArrayList<Project> list){
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Project getItem(int position) {
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
            final Project project = mList.get(position);
            _TextView1.setText(project.getName());
        }
        return convertView;
    }


}