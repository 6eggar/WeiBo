package com.wenming.weiswift.app.timeline.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.FillContent;
import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.mvp.presenter.WeiBoArrowPresent;
import com.wenming.weiswift.app.mvp.presenter.imp.WeiBoArrowPresenterImp;
import com.wenming.weiswift.app.timeline.data.viewholder.origin.OriginViewHolder;
import com.wenming.weiswift.app.timeline.data.viewholder.retweet.RetweetViewHolder;
import com.wenming.weiswift.app.timeline.weiboitem.NewPauseOnScrollListener;
import com.wenming.weiswift.app.weibodetail.activity.OriginPicTextCommentDetailSwipeActivity;
import com.wenming.weiswift.app.weibodetail.activity.RetweetPicTextCommentDetailSwipeActivity;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 2015/12/29.
 */
public abstract class WeiboAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final int TYPE_ORINGIN_ITEM = 0;
    private static final int TYPE_RETWEET_ITEM = 3;
    private ArrayList<Status> mDatas;
    private Context mContext;

    public WeiboAdapter(ArrayList<Status> datas, Context context) {
        this.mDatas = datas;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ORINGIN_ITEM) {
            onCreateOriginViewHolder(parent);
        } else if (viewType == TYPE_RETWEET_ITEM) {
            onCreateRetweetViewHolder(parent);
        }
        return null;
    }

    private OriginViewHolder onCreateOriginViewHolder(ViewGroup parent) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.timeline_original_pictext, parent, false);
        OriginViewHolder originViewHolder = new OriginViewHolder(rootView);
        originViewHolder.imageList.addOnScrollListener(new NewPauseOnScrollListener(ImageLoader.getInstance().getInstance(), true, true));
        return originViewHolder;
    }

    private RetweetViewHolder onCreateRetweetViewHolder(ViewGroup parent) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.timeline_retweet_pictext, parent, false);
        RetweetViewHolder retweetViewHolder = new RetweetViewHolder(rootView);
        retweetViewHolder.retweet_imageList.addOnScrollListener(new NewPauseOnScrollListener(ImageLoader.getInstance().getInstance(), true, true));
        return retweetViewHolder;
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder instanceof OriginViewHolder) {
            //如果这条原创微博没有被删除
            if (mDatas.get(position).user != null) {
                ((OriginViewHolder) holder).mTopBarContainerLl.setVisibility(View.VISIBLE);
                ((OriginViewHolder) holder).mBottomBarContainerLl.setVisibility(View.VISIBLE);
                ((OriginViewHolder) holder).splitLine.setVisibility(View.GONE);
                ((OriginViewHolder) holder).favoritedelete.setVisibility(View.GONE);
                FillContent.fillTitleBar(mContext, mDatas.get(position), ((OriginViewHolder) holder).mTopBarAvatarIv, ((OriginViewHolder) holder).mTopBarAvatarIdenIv, ((OriginViewHolder) holder).mTopBarNickNameTv, ((OriginViewHolder) holder).mTopBarTimeTv, ((OriginViewHolder) holder).weibo_comefrom);
                FillContent.fillWeiBoContent(mDatas.get(position).text, mContext, ((OriginViewHolder) holder).weibo_content);
                FillContent.fillButtonBar(mContext, mDatas.get(position), ((OriginViewHolder) holder).mBottomBarRetweetLl, ((OriginViewHolder) holder).mBottomBarCommentLl, ((OriginViewHolder) holder).mBottomBarLikeLl, ((OriginViewHolder) holder).mBottomBarCommentTv, ((OriginViewHolder) holder).mBottomBarRetweetTv, ((OriginViewHolder) holder).mBottomBarLikeTv);
                FillContent.fillWeiBoImgList(mDatas.get(position), mContext, ((OriginViewHolder) holder).imageList);

                ((OriginViewHolder) holder).mBottomBarContainerLl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                ((OriginViewHolder) holder).mTopBarStatusMoreIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((OriginViewHolder) holder).origin_weibo_layout.setDrawingCacheEnabled(true);
                        ((OriginViewHolder) holder).origin_weibo_layout.buildDrawingCache(true);
                        arrowClick(mDatas.get(position), position, ((OriginViewHolder) holder).origin_weibo_layout.getDrawingCache());
                    }
                });

                //微博背景的点击事件
                ((OriginViewHolder) holder).origin_weibo_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, OriginPicTextCommentDetailSwipeActivity.class);
                        intent.putExtra("weiboitem", mDatas.get(position));
                        mContext.startActivity(intent);
                    }
                });
            }
            //如果这条原创微博被删除
            else {
                ((OriginViewHolder) holder).mTopBarContainerLl.setVisibility(View.GONE);
                ((OriginViewHolder) holder).mBottomBarContainerLl.setVisibility(View.GONE);
                ((OriginViewHolder) holder).imageList.setVisibility(View.GONE);
                ((OriginViewHolder) holder).splitLine.setVisibility(View.VISIBLE);
                ((OriginViewHolder) holder).favoritedelete.setVisibility(View.VISIBLE);
                FillContent.fillWeiBoContent(mDatas.get(position).text, mContext, ((OriginViewHolder) holder).weibo_content);

                ((OriginViewHolder) holder).favoritedelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WeiBoArrowPresent weiBoArrowPresent = new WeiBoArrowPresenterImp(WeiboAdapter.this);
                        weiBoArrowPresent.cancalFavorite(position, mDatas.get(position), mContext, true);
                    }
                });
            }


        } else if (holder instanceof RetweetViewHolder) {
            FillContent.fillTitleBar(mContext, mDatas.get(position), ((RetweetViewHolder) holder).profile_img, ((RetweetViewHolder) holder).profile_verified, ((RetweetViewHolder) holder).profile_name, ((RetweetViewHolder) holder).profile_time, ((RetweetViewHolder) holder).weibo_comefrom);
            FillContent.fillRetweetContent(mDatas.get(position), mContext, ((RetweetViewHolder) holder).origin_nameAndcontent);
            FillContent.fillWeiBoContent(mDatas.get(position).text, mContext, ((RetweetViewHolder) holder).retweet_content);
            FillContent.fillButtonBar(mContext, mDatas.get(position), ((RetweetViewHolder) holder).bottombar_retweet, ((RetweetViewHolder) holder).bottombar_comment, ((RetweetViewHolder) holder).bottombar_attitude, ((RetweetViewHolder) holder).comment, ((RetweetViewHolder) holder).redirect, ((RetweetViewHolder) holder).feedlike);
            FillContent.fillWeiBoImgList(mDatas.get(position).retweeted_status, mContext, ((RetweetViewHolder) holder).retweet_imageList);

            //点击转发的内容
            ((RetweetViewHolder) holder).retweetStatus_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDatas.get(position).retweeted_status.user != null) {
                        Intent intent = new Intent(mContext, OriginPicTextCommentDetailSwipeActivity.class);
                        intent.putExtra("weiboitem", mDatas.get(position).retweeted_status);
                        mContext.startActivity(intent);
                    }
                }
            });

            ((RetweetViewHolder) holder).popover_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((RetweetViewHolder) holder).retweet_weibo_layout.setDrawingCacheEnabled(true);
                    ((RetweetViewHolder) holder).retweet_weibo_layout.buildDrawingCache(true);
                    arrowClick(mDatas.get(position), position, ((RetweetViewHolder) holder).retweet_weibo_layout.getDrawingCache());
                }
            });

            //微博背景的点击事件
            ((RetweetViewHolder) holder).retweet_weibo_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, RetweetPicTextCommentDetailSwipeActivity.class);
                    intent.putExtra("weiboitem", mDatas.get(position));
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.get(position).retweeted_status != null) {
            return TYPE_RETWEET_ITEM;
        } else {
            return TYPE_ORINGIN_ITEM;
        }
    }

    public void setData(ArrayList<Status> data) {
        this.mDatas = data;
    }

    public abstract void arrowClick(Status status, int position, Bitmap bitmap);

    public void removeDataItem(int position) {
        mDatas.remove(position);
    }
}
