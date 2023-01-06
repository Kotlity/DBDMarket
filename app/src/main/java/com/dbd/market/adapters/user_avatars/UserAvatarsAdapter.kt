package com.dbd.market.adapters.user_avatars

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbd.market.R
import com.dbd.market.room.entity.UserAvatarEntity
import com.dbd.market.utils.OnRecyclerViewItemClickInterface

class UserAvatarsAdapter: RecyclerView.Adapter<UserAvatarsAdapter.UserAvatarsViewHolder>(), OnRecyclerViewItemClickInterface {

    inner class UserAvatarsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val userAvatarImageView = itemView.findViewById<ImageView>(R.id.userAvatarImageView)
        private val userAvatarIdTextView = itemView.findViewById<TextView>(R.id.userAvatarIdTextView)
        val userSetAvatarImageView = itemView.findViewById<ImageView>(R.id.userSetAvatarImageView)

        fun bind(userAvatarEntity: UserAvatarEntity) {
            Glide.with(itemView).load(userAvatarEntity.userAvatar).into(userAvatarImageView)
            userAvatarIdTextView.text = userAvatarEntity.id.toString()
        }
    }

    private val differCallback = object: DiffUtil.ItemCallback<UserAvatarEntity>() {

        override fun areItemsTheSame(oldItem: UserAvatarEntity, newItem: UserAvatarEntity) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: UserAvatarEntity, newItem: UserAvatarEntity) = oldItem == newItem

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAvatarsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_avatars_recycler_view_item_layout, parent, false)
        return UserAvatarsViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserAvatarsViewHolder, position: Int) {
        val currentUserAvatar = differ.currentList[position]
        holder.bind(currentUserAvatar)

        holder.userSetAvatarImageView.setOnClickListener {
            onUserSetAvatarImageViewClick?.let { it(currentUserAvatar.userAvatar) }
        }
    }

    override fun getItemCount() = differ.currentList.size

    private var onUserSetAvatarImageViewClick: ((Uri) -> Unit)? = null

    override fun onRecyclerViewItemClick(onClick: (T: Any) -> Unit) {
        onUserSetAvatarImageViewClick = onClick
    }

}