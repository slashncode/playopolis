package de.haw_hamburg.playopolis.ui.createProfile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import de.haw_hamburg.playopolis.BR;
import de.haw_hamburg.playopolis.R;
import de.haw_hamburg.playopolis.databinding.TagViewItemBinding;

public class SetProfileGenresAdapter extends RecyclerView.Adapter<SetProfileGenresAdapter.ViewHolder> implements CustomClickListener{
    private final int color;
    private final List<ProfileGenre> dataModelList;

    public SetProfileGenresAdapter(int color, List<String> inputList) {
        this.dataModelList = new ArrayList<>();

        for(String name : inputList){
            dataModelList.add(new ProfileGenre(name));
        }

        this.color = color;
    }

    @NonNull
    @Override
    public SetProfileGenresAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TagViewItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.tag_view_item,
                parent,
                false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProfileGenre dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.setItemClickListener(this);
    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TagViewItemBinding itemRowBinding;

        public ViewHolder(TagViewItemBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            ProfileGenre input = (ProfileGenre) obj;

            itemRowBinding.setVariable(BR.tagName, input.getTagName());
            itemRowBinding.setVariable(BR.color, color);
            itemRowBinding.setVariable(BR.enabled, input.getEnabled());
            itemRowBinding.executePendingBindings();
        }

    }

    public void tagClicked(String f) {
        for(int i = 0; i < dataModelList.size(); i++){
            if(dataModelList.get(i).getTagName().equals(f)){
                dataModelList.get(i).toggleEnabled();
                notifyItemChanged(i);
                break;
            }
        }
    }

    public List<String> getEnabledTags() {
        List<String> enabledTags = new ArrayList<>();

        for (ProfileGenre genre : dataModelList) {
            if (genre.getEnabled()) {
                enabledTags.add("\"" + genre.getTagName() +"\"");
            }
        }

        return enabledTags;
    }

    public void enableTags(List<String> enabledTags) {
        for (ProfileGenre genre : dataModelList) {
            if (enabledTags.contains(genre.getTagName())) {
                genre.toggleEnabled();
            }
        }
    }
}