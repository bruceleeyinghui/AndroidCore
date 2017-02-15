package com.littlejie.filemanager.modules.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.littlejie.core.ui.BaseImageView;
import com.littlejie.core.util.FileUtil;
import com.littlejie.core.util.MediaUtil;
import com.littlejie.core.util.PackageUtil;
import com.littlejie.core.util.SpaceUtil;
import com.littlejie.core.util.TimeUtil;
import com.littlejie.core.util.ToastUtil;
import com.littlejie.filemanager.R;
import com.littlejie.filemanager.manager.TintDrawableManager;
import com.littlejie.filemanager.util.Constant;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by littlejie on 2017/1/10.
 */

public class FileAdapter extends BaseAdapter {

    private static final String TAG = FileAdapter.class.getSimpleName();
    private static Map<String, Integer> sIntegerMap = new HashMap<>();

    private Context mContext;
    private File[] mFiles;
    private FileFilter mFileFilter;
    private boolean showCheckBox;

    public FileAdapter(Context context) {
        this(context, Constant.HIDDEN_FILE_FILTER);
    }

    public FileAdapter(Context context, boolean showCheckBox) {
        this(context, showCheckBox, Constant.HIDDEN_FILE_FILTER);
    }

    public FileAdapter(Context context, FileFilter filter) {
        this(context, false, filter);
    }

    public FileAdapter(Context context, boolean showCheckBox, FileFilter filter) {
        mContext = context;
        mFileFilter = filter;
        this.showCheckBox = showCheckBox;
    }

    public void setData(File[] files) {
        mFiles = files;
        notifyDataSetChanged();
    }

    public void setShowCheckBox(boolean showCheckBox) {
        this.showCheckBox = showCheckBox;
    }

    @Override
    public int getCount() {
        return mFiles == null ? 0 : mFiles.length;
    }

    @Override
    public Object getItem(int position) {
        return mFiles == null ? null : mFiles[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_file, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }
        vh = (ViewHolder) convertView.getTag();
        vh.checkbox.setVisibility(showCheckBox ? View.VISIBLE : View.GONE);
        File file = (File) getItem(position);
        showIcon(vh.icon, file);
        showInfo(vh.info, file);
        vh.name.setText(file.getName());
        return convertView;
    }

    /**
     * 显示文件图标
     *
     * @param icon
     * @param file
     */
    private void showIcon(BaseImageView icon, File file) {
        FileUtil.FileType mimeType = FileUtil.FileType.getFileType(file);
        String path = file.getAbsolutePath();
        Log.d(TAG, "showIcon: file = " + path + "; mimeType = " + mimeType);
        switch (mimeType) {
            case DIRECTORY:
                icon.setImage(TintDrawableManager.getFolderDrawable());
                break;
            case APK:
                icon.setImage(PackageUtil.getApkIcon(mContext, path));
                break;
            case IMAGE:
            case VIDEO:
                icon.setImage("file://" + path);
                //此处代码效率得优化
                //icon.setImageBitmap(MediaUtil.getThumbnail(mContext, path, MediaStore.Images.Thumbnails.MICRO_KIND, null));
                break;
            case AUDIO:
                icon.setImage(MediaUtil.getAlbumCover(path));
                break;
            case TXT:
                icon.setImage(TintDrawableManager.getTxtDrawable());
                break;
            default:
                icon.setImage(TintDrawableManager.getUnknownDrawable());
                break;

        }
    }

    private void showInfo(TextView info, File file) {
        String time = TimeUtil.parse2TimeDetail(file.lastModified());
        if (file.isDirectory()) {
            // 2017/2/12 优化此处性能，当文件夹内包含大量文件时，此处会造成GC抖动，导致UI卡顿
            // 故此处使用 Map 对文件夹内 Item 数量进行缓存
            int inner;
            String path = file.getAbsolutePath();
            if (sIntegerMap.containsKey(path)) {
                inner = sIntegerMap.get(path);
            } else {
                inner = file.listFiles(mFileFilter).length;
                sIntegerMap.put(path, inner);
            }
            info.setText(inner + "项 | " + time);
        } else {
            info.setText(SpaceUtil.getSpace(file.length()) + " | " + time);
        }
    }

    static class ViewHolder {
        @BindView(R.id.iv_icon)
        BaseImageView icon;
        @BindView(R.id.cbx_checked)
        CheckBox checkbox;
        @BindView(R.id.tv_name)
        TextView name;
        @BindView(R.id.tv_info)
        TextView info;

        @OnClick(R.id.cbx_checked)
        void onCheckBoxClick() {
            ToastUtil.showDefaultToast("哈哈");
        }

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
