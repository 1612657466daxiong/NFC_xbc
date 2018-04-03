package infoex.cn.xbc.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import infoex.cn.xbc.R;
import infoex.cn.xbc.bean.BaseBean;
import infoex.cn.xbc.bean.Project;

/**
 * Author:Doraemon_xqw
 * Time:18.3.3
 * FileName:ListProjectUtils
 * Project:XBC
 * Package:infoex.cn.xbc.util
 * Company:YawooAI
 */
public class ListProjectUtils {
    private AlertDialog alertDialog;
    Context mContext;
    private Context dialogContext;
    private LayoutInflater dialogInflater;
    private ArrayAdapter<BaseBean> adapter;
    private ArrayAdapter<Project> adapterProject;

    public ListProjectUtils(Context mContext) {
        this.mContext = mContext;
    }
    public void dissmiss(){
        if (alertDialog!=null){
            alertDialog.dismiss();
        }
    }

    public interface OnItemProjectClickListener{
        void onItemProjectClick(Project baseBean);
    }

    OnItemProjectClickListener onItemProjectClickListener;

    public void setOnItemProjectClickListener(OnItemProjectClickListener onItemClickListener) {
        this.onItemProjectClickListener = onItemClickListener;
    }


    public  ListProjectUtils showProject(String title, ArrayList<Project> beanArrayList){
        dialogContext = new ContextThemeWrapper(mContext,android.R.style.Theme_Light);
        dialogInflater = (LayoutInflater) dialogContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapterProject = new ArrayAdapter<Project>(mContext, R.layout.item_list_dialog,beanArrayList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView==null){
                    convertView = dialogInflater.inflate(
                            R.layout.item_list_dialog, parent, false);
                }
                TextView textView = (TextView) convertView.findViewById(R.id.tv_dialog_list);
                String name = this.getItem(position).getName();
                textView.setText(name);
                return convertView;
            }
        };
        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onItemProjectClickListener.onItemProjectClick(adapterProject.getItem(which));
            }
        };
        alertDialog = new AlertDialog.Builder(mContext).setTitle(title)
                .setSingleChoiceItems(adapter, 0, clickListener).create();
        alertDialog.show();
        return this;
    }
}
