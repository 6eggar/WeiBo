package com.wenming.weiswift.mvp.presenter.imp;

import android.content.Context;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.entity.User;
import com.wenming.weiswift.entity.list.StatusList;
import com.wenming.weiswift.mvp.model.FavoriteListModel;
import com.wenming.weiswift.mvp.model.FriendShipModel;
import com.wenming.weiswift.mvp.model.StatusListModel;
import com.wenming.weiswift.mvp.model.imp.FavoriteListModelImp;
import com.wenming.weiswift.mvp.model.imp.FriendShipModelImp;
import com.wenming.weiswift.mvp.model.imp.StatusListModelImp;
import com.wenming.weiswift.mvp.presenter.WeiBoArrowPresent;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.common.login.Constants;
import com.wenming.weiswift.ui.login.fragment.home.weiboitem.WeiboAdapter;
import com.wenming.weiswift.utils.SDCardUtil;

/**
 * Created by xiangflight on 2016/4/22.
 */
public class WeiBoArrowPresenterImp implements WeiBoArrowPresent {
    private StatusListModel statusListModel;
    private FriendShipModel friendShipModel;
    private FavoriteListModel favoriteListModel;
    private PopupWindow mPopupWindows;
    private WeiboAdapter mAdapter;
    private Context mContext;

    public WeiBoArrowPresenterImp(PopupWindow popupWindow) {
        statusListModel = new StatusListModelImp();
        friendShipModel = new FriendShipModelImp();
        favoriteListModel = new FavoriteListModelImp();
        this.mPopupWindows = popupWindow;
    }

    public WeiBoArrowPresenterImp(PopupWindow popupWindow, WeiboAdapter adapter) {
        statusListModel = new StatusListModelImp();
        friendShipModel = new FriendShipModelImp();
        favoriteListModel = new FavoriteListModelImp();
        this.mPopupWindows = popupWindow;
        this.mAdapter = adapter;
    }


    /**
     * 删除一条微博
     *
     * @param id
     * @param context
     */
    public void weibo_destroy(long id, Context context, final int position, final String weiboGroup) {
        mContext = context;
        mPopupWindows.dismiss();
        statusListModel.weibo_destroy(id, context, new StatusListModel.OnRequestListener() {
            @Override
            public void onSuccess() {
                //内存删除
                mAdapter.removeDataItem(position);
                mAdapter.notifyItemRemoved(position);//显示动画效果
                int rangeChangeCount = mAdapter.getItemCount() - (1 + position);
                mAdapter.notifyItemRangeChanged(position, rangeChangeCount);//notifyItemRangeChanged：对于被删掉的位置及其后range大小范围内的view进行重新onBindViewHolder
                //本地删除
                updateLocalFile(weiboGroup, position);
            }

            @Override
            public void onError(String error) {

            }
        });
    }


    public void updateLocalFile(String weiboGroup, int position) {
        String response = null;
        switch (weiboGroup) {
            case Constants.DELETE_WEIBO_TYPE1:
                response = SDCardUtil.get(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/home", "全部微博" + AccessTokenKeeper.readAccessToken(mContext).getUid() + ".txt");
                break;
            case Constants.DELETE_WEIBO_TYPE2:
                response = SDCardUtil.get(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/profile", "我的全部微博" + AccessTokenKeeper.readAccessToken(mContext).getUid() + ".txt");
                break;
            case Constants.DELETE_WEIBO_TYPE3:
                response = SDCardUtil.get(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/profile", "我的原创微博" + AccessTokenKeeper.readAccessToken(mContext).getUid() + ".txt");
                break;
            case Constants.DELETE_WEIBO_TYPE4:
                response = SDCardUtil.get(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/profile", "我的图片微博" + AccessTokenKeeper.readAccessToken(mContext).getUid() + ".txt");
                break;
        }
        StatusList statusList = StatusList.parse(response);
        if (statusList != null && statusList.statuses.size() > 0 && position < statusList.statuses.size()) {
            statusList.statuses.remove(position);
            switch (weiboGroup) {
                case Constants.DELETE_WEIBO_TYPE1:
                    SDCardUtil.put(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/home", "全部微博" + AccessTokenKeeper.readAccessToken(mContext).getUid() + ".txt", new Gson().toJson(statusList));
                    break;
                case Constants.DELETE_WEIBO_TYPE2:
                    SDCardUtil.put(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/profile", "我的全部微博" + AccessTokenKeeper.readAccessToken(mContext).getUid() + ".txt", new Gson().toJson(statusList));
                    break;
                case Constants.DELETE_WEIBO_TYPE3:
                    SDCardUtil.put(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/profile", "我的原创微博" + AccessTokenKeeper.readAccessToken(mContext).getUid() + ".txt", new Gson().toJson(statusList));
                    break;
                case Constants.DELETE_WEIBO_TYPE4:
                    SDCardUtil.put(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/profile", "我的图片微博" + AccessTokenKeeper.readAccessToken(mContext).getUid() + ".txt", new Gson().toJson(statusList));
                    break;
            }
        }
    }

    /**
     * 取消关注某一用户
     *
     * @param context
     */
    public void user_destroy(User user, Context context) {
        mContext = context;
        mPopupWindows.dismiss();
        friendShipModel.user_destroy(user, context, new FriendShipModel.OnRequestListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public void user_create(User user, Context context) {
        mContext = context;
        mPopupWindows.dismiss();
        friendShipModel.user_create(user, context, new FriendShipModel.OnRequestListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    /**
     * 收藏一条微博
     *
     * @param status
     * @param context
     */
    public void createFavorite(Status status, Context context) {
        mContext = context;
        mPopupWindows.dismiss();
        favoriteListModel.createFavorite(status, context, new FavoriteListModel.OnRequestUIListener() {
            @Override
            public void onSuccess() {


            }

            @Override
            public void onError(String error) {

            }
        });
    }

    /**
     * 取消收藏一条微博
     *
     * @param status
     * @param context
     */
    @Override
    public void cancalFavorite(Status status, Context context) {
        mContext = context;
        mPopupWindows.dismiss();
        favoriteListModel.cancelFavorite(status, context, new FavoriteListModel.OnRequestUIListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(String error) {

            }
        });
    }
}