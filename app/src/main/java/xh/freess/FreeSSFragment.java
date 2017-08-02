package xh.freess;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import xh.freess.databinding.BottomSheetCopyBinding;
import xh.freess.databinding.FragmentMainBinding;
import xh.freess.databinding.ItemDialogQrcodeBinding;
import xh.freess.databinding.ItemDialogSiteSelectorBinding;
import xh.freess.databinding.PopWindowBinding;

/**
 * A placeholder fragment containing a simple view.
 */
public class FreeSSFragment extends Fragment {

    private static final String TAG = "FreeSSFragment";

    private Activity parentActivity;
    private NetworkRequest networkRequest;
    private FragmentMainBinding binding;
    private AccountAdapter adapter;
    private List<FreeSSAccount> accounts = new ArrayList<>();
    private PopupWindow popupWindow;
    private int currentSite;

    private FreeSSFragListener listener;
    AlertDialog dialogSelectSite;

    public FreeSSFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (Activity) context;
        if (context instanceof FreeSSFragListener) {
            listener = (FreeSSFragListener) context;
        } else {
            throw new IllegalArgumentException("Activity must implements FreeSSFragListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkRequest = new NetworkRequest();
        currentSite = NetworkRequest.SITE_ISHADOWX;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        adapter = new AccountAdapter(parentActivity, accounts, new AccountAdapter.Callback() {
            @Override
            public void onCopyClick(View v, FreeSSAccount account) {
                String profile = accountToBase64(account);
                if (copyToClipBoard(profile)) {
                    showCopyResult(profile);
                } else {
                    Toast.makeText(parentActivity, "剪贴板不能访问", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onQRCodeClick(String imgUrl) {
                showQRCode(imgUrl);
            }

            @Override
            public boolean onLongClick(View v) {
//                showPopup(v);
                return true;
            }
        });
        binding.accountList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.accountList.setAdapter(adapter);

        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFreeAccount(currentSite);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        loadFreeAccount(currentSite);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (listener != null) {
            listener = null;
        }
    }

    public void loadFreeAccount(int site) {
        binding.refreshLayout.setRefreshing(true);
        switch (site) {
            case NetworkRequest.SITE_ISHADOWX:
                networkRequest.getIshadowFreeAccounts()
                        .toList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                binding.refreshLayout.setRefreshing(false);
                            }
                        })
                        .subscribe(new SingleObserver<List<FreeSSAccount>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(List<FreeSSAccount> value) {
                                accounts.clear();
                                accounts.addAll(value);
                                adapter.notifyDataSetChanged();
                                Log.d(TAG, "onNext: "+new Gson().toJson(value));
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError: "+e.getLocalizedMessage());
                            }

                        });
                break;
            case NetworkRequest.SITE_FREESSR:
                networkRequest.getFreeSSRSiteAccount()
                        .toList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                binding.refreshLayout.setRefreshing(false);
                            }
                        })
                        .subscribe(new SingleObserver<List<FreeSSAccount>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(List<FreeSSAccount> value) {
                                accounts.clear();
                                accounts.addAll(value);
                                adapter.notifyDataSetChanged();
                                Log.d(TAG, "onNext: "+new Gson().toJson(value));
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError: "+e.getLocalizedMessage());
                            }

                        });
                break;
        }

    }

    public void showPopup(View rootView) {
        PopWindowBinding popWindowBinding = DataBindingUtil.inflate(parentActivity.getLayoutInflater(), R.layout.pop_window, null, false);
        popWindowBinding.btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                accountToBase64(v);
            }
        });
        popWindowBinding.base64.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                accountToBase64(account);
                Snackbar.make(binding.getRoot(), "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        popWindowBinding.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (popupWindow.isShowing())
//                    popupWindow.dismiss();
            }
        });
        popupWindow = new PopupWindow(parentActivity);//初始化PopupWindow对象
        popupWindow.setContentView(popWindowBinding.getRoot());//设置PopupWindow布局文件
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);//设置PopupWindow宽
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);//设置PopupWindow高
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(rootView,
                Gravity.NO_GRAVITY,
                (int) rootView.getX() + 30,
                (int) rootView.getY() + rootView.getHeight() / 2);
    }

    public void showQRCode(String imgUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        ItemDialogQrcodeBinding qrcodeBinding =
                DataBindingUtil.inflate(parentActivity.getLayoutInflater(), R.layout.item_dialog_qrcode, null, false);
        builder.setView(qrcodeBinding.getRoot());
        Glide.with(parentActivity)
                .load(imgUrl)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.mipmap.ic_launcher)
                .into(qrcodeBinding.qrCode);
        builder.show();
    }

    public String accountToBase64(FreeSSAccount account) {
        String uri = account.getMethod()+":"+account.getPassword()+"@"+account.getProxyServer()+":"+account.getPort();
        String encodeStr = null;
        try {
            encodeStr = new String(Base64.encode(uri.getBytes(), Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onClick: "+encodeStr);
        String profile = "ss://"+encodeStr+"@"+account.getProxyServer()+":"+account.getPort();
        return profile;
    }

    private boolean copyToClipBoard(String profile) {
        ClipboardManager clipboard = (ClipboardManager) parentActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("profile", profile);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            return true;
        } else {
            return false;
        }
    }

    private void showCopyResult(final String profile) {
        BottomSheetDialog dialog = new BottomSheetDialog(parentActivity);
        final BottomSheetCopyBinding bottomSheetBinding =
                DataBindingUtil.inflate(parentActivity.getLayoutInflater(), R.layout.bottom_sheet_copy, null, false);
        bottomSheetBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = bottomSheetBinding.name.getText().toString().trim();
                String newProfile = profile + "#" + name;
                bottomSheetBinding.setProfile(newProfile);
                copyToClipBoard(newProfile);
            }
        });
        bottomSheetBinding.setProfile(profile);
        dialog.setContentView(bottomSheetBinding.getRoot());
        dialog.show();

    }

    public void copyAll() {
        Toast.makeText(parentActivity, "copy all", Toast.LENGTH_SHORT).show();
        StringBuilder result = new StringBuilder();
        List<String> profiles = new ArrayList<>();
        for (FreeSSAccount account : accounts) {
            String profile = accountToBase64(account);
            profiles.add(profile);
            result.append(profile).append("\n");
        }
        if (copyToClipBoard(result.toString())) {
            showCopyAll(profiles);
        } else {

        }
    }

    private void showCopyAll(List<String> profiles) {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(parentActivity);
        View rootView = parentActivity.getLayoutInflater()
                        .inflate(R.layout.bottom_sheet_copy_all, null);
        LinearLayout container = (LinearLayout) rootView.findViewById(R.id.copy_result_content);
        for (String profile : profiles) {
            View child = parentActivity.getLayoutInflater().inflate(R.layout.item_bottom_sheet_copy_all, null);
            TextView tv = (TextView) child.findViewById(R.id.txt);
            tv.setText(profile);
            container.addView(child);
        }
        sheetDialog.setContentView(rootView);
        sheetDialog.show();
    }

    public void accountToJsonAll() {
        Toast.makeText(parentActivity, "accountToJsonAll", Toast.LENGTH_SHORT).show();
    }

    public void changeSite() {
        showSiteSelector();
    }

    private void showSiteSelector() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        ItemDialogSiteSelectorBinding binding = ItemDialogSiteSelectorBinding
                .inflate(parentActivity.getLayoutInflater(), null, false);
        binding.setSiteOne(NetworkRequest.SITE_HOST_ISHADOW);
        binding.setSiteTwo(NetworkRequest.SITE_HOST_FREE_SSR);
        binding.siteSelector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.site_ishadowx:
                        listener.setSiteTag("ishadowx");
                        currentSite = NetworkRequest.SITE_ISHADOWX;
                        break;
                    case R.id.site_freessr:
                        listener.setSiteTag("freessr");
                        currentSite = NetworkRequest.SITE_FREESSR;
                        break;
                }
                loadFreeAccount(currentSite);
                dialogSelectSite.dismiss();
            }
        });
        builder.setView(binding.getRoot());
        dialogSelectSite = builder.create();
        dialogSelectSite.show();
    }

    public int getCurrentSite() {
        return currentSite;
    }

    public interface FreeSSFragListener {
        void setSiteTag(String tag);
    }

}
