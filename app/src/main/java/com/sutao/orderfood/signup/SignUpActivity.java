package com.sutao.orderfood.signup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.sutao.base.ActivityController;
import com.sutao.base.BaseActivity;
import com.sutao.orderfood.R;
import com.sutao.orderfood.leanclound.LeanCloundHelper;
import com.sutao.orderfood.utils.ToastUtils;

/**
 * Created by Administrator on 2017/3/23.
 */
public class SignUpActivity extends BaseActivity {

    private EditText mAccountEdit;
    private EditText mPasswordEdit;
    private EditText mConfirmPasswordEdit;
    private EditText mSellerEdit;
    private RadioGroup mAccountType;
    private Button mSignup;
    private boolean mIsSeller;
    private String mAccount;
    private String mPassword;
    private String mSellerName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        initEvent();
    }

    private void initView() {
        mAccountEdit = findView(R.id.edit_account);
        mPasswordEdit = findView(R.id.edit_password);
        mConfirmPasswordEdit = findView(R.id.edit_password_confirm);
        mAccountType = findView(R.id.radio_type);
        mSignup = findView(R.id.btn_signup);
        mSellerEdit = findView(R.id.edit_seller_name);

        RadioButton buyer = findView(R.id.btn_buyer);
        buyer.setChecked(true);
        mSellerEdit.setVisibility(View.GONE);
    }

    private void initEvent() {
        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputValid()) {
                    SignUpCallback callback = new SignUpCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                ToastUtils.makeShortToast(SignUpActivity.this,"注册成功");
                                login();
                            } else {
                                ToastUtils.makeShortToast(SignUpActivity.this,e.toString());
                            }
                        }
                    };
                    if (mIsSeller) {
                        LeanCloundHelper.signupSeller(mAccount, mPassword,mSellerName,callback);
                    } else {
                        LeanCloundHelper.signupBuyer(mAccount, mPassword, callback);
                    }
                }
            }
        });
        mAccountType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkId) {
                if (checkId == R.id.btn_seller) {
                    mIsSeller = true;
                    mSellerEdit.setVisibility(View.VISIBLE);
                } else if (checkId == R.id.btn_buyer) {
                    mIsSeller = false;
                    mSellerEdit.setVisibility(View.GONE);
                }
            }
        });
    }

    private boolean checkInputValid() {
        mAccount = mAccountEdit.getText().toString();
        if (TextUtils.isEmpty(mAccount)) {
            ToastUtils.makeShortToast(this, "账号输入不合法");
            return false;
        }
        mPassword = mPasswordEdit.getText().toString();
        String passwordConfirm = mConfirmPasswordEdit.getText().toString();
        if (mPassword.equals(passwordConfirm)&&TextUtils.isEmpty(mPassword)) {
            ToastUtils.makeShortToast(this, "密码输入不合法");
            return false;
        }
        mSellerName = mSellerEdit.getText().toString();
        if (mIsSeller&&TextUtils.isEmpty(mSellerName)) {
            ToastUtils.makeShortToast(this, "请输入店铺名称");
            return false;
        }
        return true;
    }

    private void login(){
        AVUser.logInInBackground(mAccount, mPassword, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    if (mIsSeller) {
                        ActivityController.showUploadActivity(SignUpActivity.this);
                    } else {
                        ActivityController.showBuyerMainActivity(SignUpActivity.this);
                    }
                    SignUpActivity.this.finish();
                } else {
                    ToastUtils.makeShortToast(SignUpActivity.this,e.toString());
                }
            }
        });
    }
}
