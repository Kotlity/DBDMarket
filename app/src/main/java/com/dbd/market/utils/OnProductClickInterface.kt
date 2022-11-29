package com.dbd.market.utils

import com.dbd.market.helpers.products_adder.data.Product

interface OnProductClickInterface {

    fun onProductClick(onClick: (Product) -> Unit)
}