package com.getrent.smartsecurity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.getrent.smartsecurity.DetailsActivity;
import com.getrent.smartsecurity.R;
import com.getrent.smartsecurity.modelClass.ImageList;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.getrent.smartsecurity.helper.Apis.baseUrl;

public class ImageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ImageList> imageList;

    public ImageListAdapter(Context mContext, List<ImageList> List) {
        this.mContext = mContext;
        this.imageList = List;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.image_item_views, parent, false);
        return new ViewHolde(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ImageList list = imageList.get(position);

        final String id = list.getId();
        final String dateTime = list.getDateTime();
        final String imgName = list.getImgUrl();
        final String imgUrl = baseUrl+imgName;

        ViewHolde viewHolde = (ViewHolde) holder;
        String tt[] = dateTime.split("-");
        viewHolde.tvDateTime.setText(tt[0]+"\n"+tt[1]);
        Picasso.get().load(imgUrl).placeholder(R.drawable.gallery).fit().into(viewHolde.ivImageView);

        viewHolde.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra("Position",String.valueOf(position));
                intent.putExtra("ID",id);
                intent.putExtra("ImageName",dateTime);
                intent.putExtra("ImageUrl",imgName);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolde extends RecyclerView.ViewHolder {
        private ImageView ivImageView;
        private TextView tvDateTime;
        public ViewHolde(@NonNull View itemView) {
            super(itemView);

            ivImageView = itemView.findViewById(R.id.ivImageView);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
        }
    }


}
