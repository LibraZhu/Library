package com.libra.muti_image_selector;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.libra.R;
import com.libra.utils.URIUtil;
import com.libra.view.base.BaseActivity;
import java.io.File;
import java.util.ArrayList;

public class MultiImageSelectorActivity extends BaseActivity
        implements MultiImageSelectorFragment.Callback {

    /** ReuqestCode */
    public static final int REQUEST_CODE = 0x100;
    /** 最大图片选择次数，int类型，默认9 */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /** 图片选择模式，默认多选 */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /** 是否显示相机，默认显示 */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /** 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合 */
    public static final String EXTRA_RESULT = "select_result";
    /** 默认选择集 */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

    /** 单选 */
    public static final int MODE_SINGLE = 0;
    /** 多选 */
    public static final int MODE_MULTI = 1;

    private ArrayList<String> resultList = new ArrayList<>();
    private Button mSubmitButton;
    private int mDefaultCount;


    /**
     * 跳转
     *
     * @param activity activity
     */
    public static void startFromForResultWithDefaultArg(Activity activity) {
        activity.startActivityForResult(
                new Intent(activity, MultiImageSelectorActivity.class),
                REQUEST_CODE);
    }


    /**
     * 跳转
     *
     * @param activity activity
     * @param isShowCamera 是否显示照相
     * @param selectCount 总共多选几张
     * @param selectMode 选择模式。0:单选  1:多选
     * @param defaultDataArray 初始数据列表
     */
    public static void startFromForResult(Activity activity, boolean isShowCamera, int selectCount, int selectMode, ArrayList<String> defaultDataArray) {
        Intent intent = new Intent(activity, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA,
                isShowCamera);
        if (selectCount > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT,
                    selectCount);
        }
        else {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
        }
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
                selectMode);
        intent.putStringArrayListExtra(
                MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
                defaultDataArray);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_image_selector);

        Intent intent = getIntent();
        mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 9);
        int mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
        boolean isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        if (mode == MODE_MULTI &&
                intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
            resultList = intent.getStringArrayListExtra(
                    EXTRA_DEFAULT_SELECTED_LIST);
        }

        Bundle bundle = new Bundle();
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT,
                mDefaultCount);
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
        bundle.putStringArrayList(
                MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST,
                resultList);

        getSupportFragmentManager().beginTransaction()
                                   .add(R.id.image_grid,
                                           Fragment.instantiate(this,
                                                   MultiImageSelectorFragment.class
                                                           .getName(), bundle))
                                   .commit();

        // 返回按钮
        $(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        // 完成按钮
        mSubmitButton = (Button) $(R.id.commit);
        if (resultList == null || resultList.size() <= 0) {
            mSubmitButton.setText(R.string.action_done);
            mSubmitButton.setEnabled(false);
        }
        else {
            updateDoneText();
            mSubmitButton.setEnabled(true);
        }
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (resultList != null && resultList.size() > 0) {
                    // 返回已选择的图片数据
                    Intent data = new Intent();
                    data.putStringArrayListExtra(EXTRA_RESULT, resultList);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
    }


    private void updateDoneText() {
        mSubmitButton.setText(
                String.format("%s(%d/%d)", getString(R.string.action_done),
                        resultList.size(), mDefaultCount));
    }


    @Override public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        resultList.add(path);
        data.putStringArrayListExtra(EXTRA_RESULT, resultList);
        setResult(RESULT_OK, data);
        finish();
    }


    @Override public void onImageSelected(String path) {
        if (!resultList.contains(path)) {
            resultList.add(path);
        }
        // 有图片之后，改变按钮状态
        if (resultList.size() > 0) {
            updateDoneText();
            if (!mSubmitButton.isEnabled()) {
                mSubmitButton.setEnabled(true);
            }
        }
    }


    @Override public void onImageUnselected(String path) {
        if (resultList.contains(path)) {
            resultList.remove(path);
        }
        updateDoneText();
        // 当为选择图片时候的状态
        if (resultList.size() == 0) {
            mSubmitButton.setText(R.string.action_done);
            mSubmitButton.setEnabled(false);
        }
    }


    @Override public void onCameraShot(File imageFile) {
        if (imageFile != null) {

            // notify system
            Uri contentUri = URIUtil.fromFile(this, imageFile);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    contentUri));

            Intent data = new Intent();
            resultList.add(imageFile.getAbsolutePath());
            data.putStringArrayListExtra(EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }


    public void setSubmitButtonBackground(int resid) {
        mSubmitButton.setBackgroundResource(resid);
    }


    public void setSubmitButtonTextColor(int color) {
        mSubmitButton.setTextColor(color);
    }


    public void setCustomTitleColor(int color) {
        ((TextView) $(R.id.text)).setTextColor(color);
    }


    public void setCustomBackImage(int resId) {
        ((ImageView) $(R.id.btn_back)).setImageResource(resId);
    }
}
