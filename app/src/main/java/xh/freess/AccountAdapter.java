package xh.freess;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import xh.freess.databinding.ItemRvFreeAccountBinding;

/**
 * Created by G1494458 on 2017/7/25.
 */

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private List<FreeSSAccount> accounts;
    private Callback callback;
    private Context context;

    public AccountAdapter(Context context, List<FreeSSAccount> accounts, Callback callback) {
        this.context = context;
        this.accounts = accounts;
        this.callback = callback;
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRvFreeAccountBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.item_rv_free_account, parent, false);
        return new AccountViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position) {
        holder.bindData(context, accounts.get(position), callback);
    }

    @Override
    public int getItemCount() {
        return accounts != null ? accounts.size() : 0;
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder {

        ItemRvFreeAccountBinding vhBinding;


        public AccountViewHolder(ItemRvFreeAccountBinding binding) {
            super(binding.getRoot());
            vhBinding = binding;
        }

        public void bindData(Context ctx, final FreeSSAccount account, final Callback callback) {
            vhBinding.setAccount(account);
            if (account.getItemBg() != null && !account.getItemBg().equals("")) {
                Glide.with(ctx)
                        .load(account.getItemBg())
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .centerCrop()
                        .into(vhBinding.itemBg);
            } else {
                Glide.with(ctx)
                        .load(R.drawable.simple_blue_background)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .centerCrop()
                        .into(vhBinding.itemBg);
            }

            vhBinding.setCallback(callback);
//            vhBinding.itemContainer.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    return false;
//                }
//            });
//            vhBinding.accountToBase64.set(new View.OnLongClickListener() {
//                @Override
//                public boolean onClick(View v) {
//                    callback.onClick(v, account);
//                    return true;
//                }
//            });
        }

    }

    public interface Callback {
        void onCopyClick(View v, FreeSSAccount account);
        void onQRCodeClick(String imgUrl);
        boolean onLongClick(View v);
    }
}
