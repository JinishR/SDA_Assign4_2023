package com.example.sdaassign4_2022;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class LibraryViewAdapter extends RecyclerView.Adapter<LibraryViewAdapter.ViewHolder> {

    private static final String TAG = "LibraryViewAdapter";
    private Context mNewContext;

    // Add arrays for each item
    private ArrayList<String> mAuthor;
    private ArrayList<String> mTitle;
    private ArrayList<String> mImageUrls;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;

    LibraryViewAdapter(Context mNewContext, ArrayList<String> author, ArrayList<String> title, ArrayList<String> imageUrls) {
        this.mNewContext = mNewContext;
        this.mAuthor = author;
        this.mTitle = title;
        this.mImageUrls = imageUrls;
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference();
    }





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder: was called");

        viewHolder.authorText.setText(mAuthor.get(position));
        viewHolder.titleText.setText(mTitle.get(position));
        String imageUrl = mImageUrls.get(position);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            StorageReference imageRef = mStorageReference.child(imageUrl);
            Glide.with(mNewContext).load(imageRef).into(viewHolder.imageItem);
        } else {
            viewHolder.imageItem.setImageResource(R.drawable.sku10001);
        }

        viewHolder.checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Toast.makeText(mNewContext, mTitle.get(position), Toast.LENGTH_SHORT).show();
                Intent myOrder = new Intent (mNewContext, CheckOut.class);
                mNewContext.startActivity(myOrder);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAuthor.size();
    }

    // View holder class for recycler_list_item.xml
    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageItem;
        TextView authorText;
        TextView titleText;
        Button checkOut;
        RelativeLayout itemParentLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Grab the image, the text, and the layout ids
            imageItem = itemView.findViewById(R.id.bookImage);
            authorText = itemView.findViewById(R.id.authorText);
            titleText = itemView.findViewById(R.id.bookTitle);
            checkOut = itemView.findViewById(R.id.out_button);
            itemParentLayout = itemView.findViewById(R.id.listItemLayout);

        }
    }
}
