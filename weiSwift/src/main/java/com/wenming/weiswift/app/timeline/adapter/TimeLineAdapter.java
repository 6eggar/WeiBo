package com.wenming.weiswift.app.timeline.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.FillContent;
import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.timeline.constants.Constants;
import com.wenming.weiswift.app.timeline.data.viewholder.origin.OriginViewHolder;
import com.wenming.weiswift.app.timeline.data.viewholder.retweet.RetweetViewHolder;
import com.wenming.weiswift.app.weibodetail.activity.OriginPicTextCommentDetailSwipeActivity;
import com.wenming.weiswift.app.weibodetail.activity.RetweetPicTextCommentDetailSwipeActivity;

import java.util.List;

/**
 * Created by wenmingvs on 2017/7/23.
 */

public class TimeLineAdapter extends BaseMultiItemQuickAdapter<Status, BaseViewHolder> {

    public TimeLineAdapter(@Nullable List<Status> data) {
        super(data);
        addItemType(Constants.TYPE_ORINGIN_ITEM, R.layout.timeline_original_pictext);
        addItemType(Constants.TYPE_RETWEET_ITEM, R.layout.timeline_retweet_pictext);
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Constants.TYPE_ORINGIN_ITEM:
                return new OriginViewHolder(getItemView(R.layout.timeline_original_pictext, parent));
            case Constants.TYPE_RETWEET_ITEM:
                return new RetweetViewHolder(getItemView(R.layout.timeline_retweet_pictext, parent));
            default:
                return super.onCreateDefViewHolder(parent, viewType);
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, final Status status) {
        switch (holder.getItemViewType()) {
            case Constants.TYPE_ORINGIN_ITEM:
                onBindOriginHolder((OriginViewHolder) holder, status);
                break;
            case Constants.TYPE_RETWEET_ITEM:
                onBindRetweetHolder((RetweetViewHolder) holder, status);
                break;
        }
    }

    private void onBindRetweetHolder(final RetweetViewHolder holder, final Status status) {
        FillContent.fillTitleBar(mContext, status, holder.profile_img, holder.profile_verified, holder.profile_name, holder.profile_time, holder.weibo_comefrom);
        FillContent.fillRetweetContent(status, mContext, holder.origin_nameAndcontent);
        FillContent.fillWeiBoContent(status.text, mContext, holder.retweet_content);
        FillContent.fillButtonBar(mContext, status, holder.bottombar_retweet, holder.bottombar_comment, holder.bottombar_attitude, holder.comment, holder.redirect, holder.feedlike);
        FillContent.fillWeiBoImgList(status.retweeted_status, mContext, holder.retweet_imageList);

        //点击转发的内容
        holder.retweetStatus_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.retweeted_status.user != null) {
                    Intent intent = new Intent(mContext, OriginPicTextCommentDetailSwipeActivity.class);
                    intent.putExtra("weiboitem", status.retweeted_status);
                    mContext.startActivity(intent);
                }
            }
        });

        holder.popover_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.retweet_weibo_layout.setDrawingCacheEnabled(true);
                holder.retweet_weibo_layout.buildDrawingCache(true);
                //arrowClick(status, position, holder.retweet_weibo_layout.getDrawingCache());
            }
        });

        //微博背景的点击事件
        holder.retweet_weibo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RetweetPicTextCommentDetailSwipeActivity.class);
                intent.putExtra("weiboitem", status);
                mContext.startActivity(intent);
            }
        });
    }

    private void onBindOriginHolder(final OriginViewHolder holder, final Status status) {

        //如果这条原创微博没有被删除
        if (status.user != null) {
            holder.mTopBarContainerLl.setVisibility(View.VISIBLE);
            holder.mBottomBarContainerLl.setVisibility(View.VISIBLE);
            holder.splitLine.setVisibility(View.GONE);
            holder.favoritedelete.setVisibility(View.GONE);
            FillContent.fillTitleBar(mContext, status, holder.mTopBarAvatarIv, holder.mTopBarAvatarIdenIv, holder.mTopBarNickNameTv, holder.mTopBarTimeTv, holder.weibo_comefrom);
            FillContent.fillWeiBoContent(status.text, mContext, holder.weibo_content);
            FillContent.fillButtonBar(mContext, status, holder.mBottomBarRetweetLl, holder.mBottomBarCommentLl, holder.mBottomBarLikeLl, holder.comment, holder.redirect, holder.feedlike);
            FillContent.fillWeiBoImgList(status, mContext, holder.imageList);

            holder.mBottomBarContainerLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.mTopBarStatusMoreIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.origin_weibo_layout.setDrawingCacheEnabled(true);
                    holder.origin_weibo_layout.buildDrawingCache(true);
                    //arrowClick(status, position, ((OriginViewHolder) holder).origin_weibo_layout.getDrawingCache());
                }
            });

            //微博背景的点击事件
            holder.origin_weibo_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, OriginPicTextCommentDetailSwipeActivity.class);
                    intent.putExtra("weiboitem", status);
                    mContext.startActivity(intent);
                }
            });
        }
        //如果这条原创微博被删除
        else {
            holder.mTopBarContainerLl.setVisibility(View.GONE);
            holder.mBottomBarContainerLl.setVisibility(View.GONE);
            holder.imageList.setVisibility(View.GONE);
            holder.splitLine.setVisibility(View.VISIBLE);
            holder.favoritedelete.setVisibility(View.VISIBLE);
            FillContent.fillWeiBoContent(status.text, mContext, ((OriginViewHolder) holder).weibo_content);
//            holder.favoritedelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    WeiBoArrowPresent weiBoArrowPresent = new WeiBoArrowPresenterImp(WeiboAdapter.this);
//                    weiBoArrowPresent.cancalFavorite(position, status, mContext, true);
//                }
//            });
        }
    }
}
