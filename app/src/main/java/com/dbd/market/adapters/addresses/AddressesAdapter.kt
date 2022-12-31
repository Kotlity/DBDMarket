package com.dbd.market.adapters.addresses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dbd.market.R
import com.dbd.market.data.Address
import com.dbd.market.utils.formatString

class AddressesAdapter: RecyclerView.Adapter<AddressesAdapter.AddressesViewHolder>() {

    inner class AddressesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val addressesTypeTextView = itemView.findViewById<TextView>(R.id.addressesTypeTextView)
        private val addressesFirstnameTextView = itemView.findViewById<TextView>(R.id.addressesFirstnameTextView)
        private val addressesLastnameTextView = itemView.findViewById<TextView>(R.id.addressesLastnameTextView)
        private val addressesCountryTextView = itemView.findViewById<TextView>(R.id.addressesCountryTextView)
        private val addressesCityTextView = itemView.findViewById<TextView>(R.id.addressesCityTextView)
        private val addressesPhoneNumberTextView = itemView.findViewById<TextView>(R.id.addressesPhoneNumberTextView)
        private val addressesStreetTextView = itemView.findViewById<TextView>(R.id.addressesStreetTextView)
        private val addressesIdTextView = itemView.findViewById<TextView>(R.id.addressesIdTextView)

        fun bind(address: Address) {
            val addressType = address.type
            addressesTypeTextView.text = formatString(addressType)
            addressesFirstnameTextView.text = address.firstName
            addressesLastnameTextView.text = address.lastName
            addressesCountryTextView.text = address.country.plus(",")
            addressesCityTextView.text = address.city
            val addressPhoneNumber = address.phoneNumber
            addressesPhoneNumberTextView.text = formatString(addressPhoneNumber)
            addressesStreetTextView.text = address.street
            addressesIdTextView.text = address.id.toString()
        }
    }

    private val differCallback = object: DiffUtil.ItemCallback<Address>() {

        override fun areItemsTheSame(oldItem: Address, newItem: Address) = oldItem.id == newItem.id && oldItem.phoneNumber == newItem.phoneNumber

        override fun areContentsTheSame(oldItem: Address, newItem: Address) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.addresses_recycler_view_item_layout, parent, false)
        return AddressesViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressesViewHolder, position: Int) {
        val currentAddress = differ.currentList[position]
        holder.bind(currentAddress)
    }

    override fun getItemCount() = differ.currentList.size
}