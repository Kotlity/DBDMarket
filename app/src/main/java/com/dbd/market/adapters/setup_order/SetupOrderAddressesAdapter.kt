package com.dbd.market.adapters.setup_order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dbd.market.R
import com.dbd.market.data.Address
import com.dbd.market.utils.OnRecyclerViewItemClickInterface

class SetupOrderAddressesAdapter: RecyclerView.Adapter<SetupOrderAddressesAdapter.SetupOrderAddressesViewHolder>(), OnRecyclerViewItemClickInterface {

    inner class SetupOrderAddressesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val setupOrderAddressesTypeTextView = itemView.findViewById<TextView>(R.id.setupOrderAddressesTypeTextView)
        private val setupOrderAddressesStreetTextView = itemView.findViewById<TextView>(R.id.setupOrderAddressesStreetTextView)
        val setupOrderAddressesRecyclerViewLayout = itemView.findViewById<LinearLayout>(R.id.setupOrderAddressesRecyclerViewLayout)

        fun bind(address: Address) {
            setupOrderAddressesTypeTextView.text = address.type
            setupOrderAddressesStreetTextView.text = address.street
        }
    }

    private val differCallback = object: DiffUtil.ItemCallback<Address>() {

        override fun areItemsTheSame(oldItem: Address, newItem: Address) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Address, newItem: Address) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetupOrderAddressesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.setup_order_addresses_recycler_view_layout, parent, false)
        return SetupOrderAddressesViewHolder(view)
    }

    override fun onBindViewHolder(holder: SetupOrderAddressesViewHolder, position: Int) {
        val currentAddress = differ.currentList[position]

        addressIsSelected(position, holder)

        holder.itemView.setOnClickListener {
            notifyAdapterToChangeSelectedAddressPosition(holder)
            clickOnAddress?.let { it(currentAddress) }
        }
    }

    override fun getItemCount() = differ.currentList.size

    private fun addressIsSelected(adapterPosition: Int, holder: SetupOrderAddressesViewHolder) {
        if (selectedAddressPosition == adapterPosition) holder.setupOrderAddressesRecyclerViewLayout.setBackgroundResource(R.drawable.selected_setup_order_addresses_recycler_view_item_background)
        else holder.setupOrderAddressesRecyclerViewLayout.setBackgroundResource(R.drawable.unselected_setup_order_addresses_recycler_view_item_background)
    }

    private fun notifyAdapterToChangeSelectedAddressPosition(holder: SetupOrderAddressesViewHolder) {
        if(selectedAddressPosition >= 0) notifyItemChanged(selectedAddressPosition)
        selectedAddressPosition = holder.adapterPosition
        notifyItemChanged(selectedAddressPosition)
    }

    private var selectedAddressPosition = -1

    private var clickOnAddress: ((Address) -> Unit)? = null

    override fun onRecyclerViewItemClick(onClick: (T: Any) -> Unit) { clickOnAddress = onClick }
}