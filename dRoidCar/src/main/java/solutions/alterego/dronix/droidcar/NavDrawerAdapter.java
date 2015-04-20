package solutions.alterego.dronix.droidcar;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import solutions.alterego.dronix.droidcar.interfaces.ItemClickListener;

public class NavDrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemClickListener {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private String mNavItems[];
    private int mIcons[];
    private ItemClickListener mItemListener;
    private int mSelectedPosition;

    public static class ViewHolderItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mIcon;
        private TextView mItem;
        private ItemClickListener mItemClickListener;
        private View mViev;

        public ViewHolderItem(View v, ItemClickListener itemClickListener) {
            super(v);
            mViev = v;
            mViev.setOnClickListener(this);
            mItem = (TextView) v.findViewById(R.id.nav_drawer_item_label);
            mIcon = (ImageView) v.findViewById(R.id.nav_drawer_item_icon);
            mItemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            mViev.setSelected(true);
            if(mItemClickListener != null)
                mItemClickListener.onItemClickListener(getPosition());
        }

        public void setSelection(boolean value) {
            mViev.setSelected(value);
        }
    }

    public static class ViewHolderHeader extends RecyclerView.ViewHolder{

        public ViewHolderHeader(View v, Resources res) {
            super(v);
        }
    }

    public NavDrawerAdapter(String Titles[], int Icons[]){
        mNavItems = Titles;
        mIcons = Icons;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return viewType == TYPE_HEADER  ? new ViewHolderHeader(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_drawer_header,parent,false), parent.getResources())
                : new ViewHolderItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_drawer_item,parent,false), this);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolderHeader) {


        }
        else {
            ViewHolderItem vhItem = (ViewHolderItem) holder;
            vhItem.mItem.setText(mNavItems[position - 1]);
            vhItem.mIcon.setImageResource(mIcons[position - 1]);

            vhItem.setSelection(mSelectedPosition == position);

        }
    }

    @Override
    public int getItemCount() {
        return mNavItems.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    public void addOnItemClickListener(ItemClickListener itemClickListener){
        mItemListener = itemClickListener;
    }

    @Override
    public void onItemClickListener(int position) {
        int oldPosition = mSelectedPosition;
        mSelectedPosition = position;
        notifyItemChanged(oldPosition);
        if(mItemListener != null)
            mItemListener.onItemClickListener(position - 1);
    }

    public void setSelectedPosition(int position){
        mSelectedPosition = position + 1;
        notifyDataSetChanged();
    }
}