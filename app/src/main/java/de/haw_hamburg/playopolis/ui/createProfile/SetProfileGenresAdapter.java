package de.haw_hamburg.playopolis.ui.createProfile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.haw_hamburg.playopolis.BR;
import de.haw_hamburg.playopolis.R;
import de.haw_hamburg.playopolis.databinding.TagViewItemBinding;

public class SetProfileGenresAdapter extends RecyclerView.Adapter<SetProfileGenresAdapter.ViewHolder> {
    private int color;
    private List<String> dataModelList;
    private Context context;
    private View.OnClickListener onClickListener;

    public SetProfileGenresAdapter(int color, List<String> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        this.color = color;
        context = ctx;
    }

    @Override
    public SetProfileGenresAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TagViewItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.tag_view_item,
                parent,
                false
        );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String dataModel = dataModelList.get(position);
        holder.bind(dataModel);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener{
        void onClick(int position, String dataModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TagViewItemBinding itemRowBinding;

        public ViewHolder(TagViewItemBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.tagName, obj);
            itemRowBinding.setVariable(BR.color, color);
            itemRowBinding.setVariable(BR.enabled, true);
            itemRowBinding.executePendingBindings();
        }
    }

    public void cardClicked(String f) {
        Toast.makeText(context, "You clicked " + f,
                Toast.LENGTH_LONG).show();
    }
}