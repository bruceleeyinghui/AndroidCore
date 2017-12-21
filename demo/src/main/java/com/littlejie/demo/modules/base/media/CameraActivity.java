package com.littlejie.demo.modules.base.media;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.modules.base.media.interfaces.OnImageDataListener;
import com.littlejie.demo.modules.base.media.view.Camera1SurfaceView;

import butterknife.BindView;
import butterknife.OnClick;

@Description(description = "使用 Camera 实现预览")
public class CameraActivity extends BaseActivity {

    @BindView(R.id.surface_view)
    Camera1SurfaceView mSurfaceView;
    @BindView(R.id.iv_photo)
    ImageView mIvPhoto;
    @BindView(R.id.btn_take_photo)
    Button mBtnTakePhoto;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_camera;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initViewListener() {
        mSurfaceView.setOnImageDataListener(new OnImageDataListener() {
            @Override
            public void onImageData(final byte[] data) {
                mSurfaceView.setVisibility(View.GONE);
                mIvPhoto.setVisibility(View.VISIBLE);
                mBtnTakePhoto.setText("预览");
                //将 data 数据转换成 Bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                //将 Bitmap 旋转 90 度
                Matrix matrix = new Matrix();
                matrix.setRotate(90);
                mIvPhoto.setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true));
            }
        });
    }

    @OnClick(R.id.btn_take_photo)
    void takePhoto() {
        if (mIvPhoto.getVisibility() == View.VISIBLE) {
            mSurfaceView.setVisibility(View.VISIBLE);
            mIvPhoto.setVisibility(View.GONE);
            mBtnTakePhoto.setText("拍照");
        } else {
            mSurfaceView.takePicture();
        }
    }

    @Override
    protected void process() {

    }
}