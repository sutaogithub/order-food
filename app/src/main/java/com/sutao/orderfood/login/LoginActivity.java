package com.sutao.orderfood.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.sutao.base.ActivityController;
import com.sutao.base.BaseActivity;
import com.sutao.orderfood.R;
import com.sutao.orderfood.global.Global;
import com.sutao.orderfood.leanclound.LeanCloundHelper;
import com.sutao.orderfood.utils.ToastUtils;

/**
 * Created by Administrator on 2017/3/22.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText mAccount;
    private EditText mPassword;
    private RadioGroup mAccountType;
    private boolean mIsSeller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkHasLogin();
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
    }

    private void checkHasLogin() {
        if (AVUser.getCurrentUser() != null){
            if (LeanCloundHelper.isSeller()) {
                ActivityController.showSellerMainActivity(this);
            } else {
                if (Global.getSellerInfo() != null) {
                    ActivityController.showBuyerMainActivity(this);
                } else {
                    ActivityController.showCaptureActivity(this);
                }
            }
            finish();
        }
    }

    private void initEvent() {
        mAccountType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btn_seller) {
                    mIsSeller = true;
                } else if (checkedId == R.id.btn_buyer) {
                    mIsSeller = false;
                }
            }
        });
    }

    private void initView() {
        findView(R.id.btn_signup).setOnClickListener(this);
        findView(R.id.btn_login).setOnClickListener(this);

        RadioButton buyer = findView(R.id.btn_buyer);
        buyer.setChecked(true);

        mAccount = findView(R.id.edit_account);
        mPassword = findView(R.id.edit_password);
        mAccountType = findView(R.id.radio_type);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_signup:
                ActivityController.showSignUpActivity(this);
//                ArrayList<Food> foods = new ArrayList<>();
//                Food food1 = new Food();
//                food1.setName("yy");
//                food1.setType(Classify.SNACK);
//                food1.setSize(1);
//
//                Food food2 = new Food();
//                food2.setName("aa");
//                food2.setType(Classify.SNACK);
//                food2.setSize(2);
//
//                foods.add(food1);
//                foods.add(food2);
//                LeanCloundHelper.pushMessage(this,JsonUtils.getUserOrderJson(foods,"728"));
                break;
        }
    }

    private void login() {
        String account = mAccount.getText().toString();
        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(account) && TextUtils.isEmpty(password)) {
            ToastUtils.makeShortToast(this, "账号密码不能为空");
            return;
        }
        showLoadingDialog();
        AVUser.logInInBackground(account, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                dismissDialog();
                if (e == null) {
                    boolean isSeller = (boolean) avUser.get(LeanCloundHelper.KEY_IS_SELLER);
                    if (isSeller != mIsSeller) {
                        ToastUtils.makeShortToast(LoginActivity.this,"账户类型不对");
                        AVUser.logOut();
                        return;
                    }
                    if (isSeller) {
                        ActivityController.showSellerMainActivity(LoginActivity.this);
                    } else {
                        ActivityController.showCaptureActivity(LoginActivity.this);
                    }
                    LoginActivity.this.finish();
                } else {
                    ToastUtils.makeShortToast(LoginActivity.this, e.toString());
                }
            }
        });
    }

}