package woowacourse.shopping.productdetail

import woowacourse.shopping.common.model.ProductModel
import woowacourse.shopping.common.model.mapper.ProductMapper.toDomainModel
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.RecentProductRepository

class ProductDetailPresenter(
    private val view: ProductDetailContract.View,
    private val product: ProductModel,
    private val recentProduct: ProductModel?,
    private val recentProductRepository: RecentProductRepository,
    private val cartRepository: CartRepository
) : ProductDetailContract.Presenter {
    init {
        view.initRecentProduct(recentProduct)
        view.updateProductDetail(product)
    }

    override fun showRecentProductDetail() {
        if (recentProduct == null) return

        val recentProducts = recentProductRepository.selectAll()
        val madeRecentProduct = recentProducts.makeRecentProduct(product.toDomainModel())

        recentProductRepository.insertRecentProduct(madeRecentProduct)

        view.showProductDetail(recentProduct)
    }

    override fun addToCart() {
        cartRepository.plusCartProduct(product.toDomainModel())
        view.showCart()
    }
}
