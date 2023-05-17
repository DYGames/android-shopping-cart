package woowacourse.shopping.cart

import android.view.View
import woowacourse.shopping.common.model.CartOrdinalProductModel
import woowacourse.shopping.common.model.PageNavigatorModel
import woowacourse.shopping.common.model.mapper.CartOrdinalProductMapper.toViewModel
import woowacourse.shopping.data.repository.CartRepository

class CartPresenter(
    private val view: CartContract.View,
    private val cartRepository: CartRepository,
    private var currentPage: Int = 0,
    private val countPerPage: Int
) : CartContract.Presenter {
    init {
        updateCartPage()
    }

    override fun removeCartProduct(cartOrdinalProductModel: CartOrdinalProductModel) {
        cartRepository.deleteCartProductByOrdinal(cartOrdinalProductModel.ordinal)
        updateCartPage()
    }

    override fun loadPreviousPage() {
        currentPage--
        updateCartPage()
    }

    override fun loadNextPage() {
        currentPage++
        updateCartPage()
    }

    private fun updateCartPage() {
        val maxPage = calculateMaxPage()
        val cart = cartRepository.selectPage(currentPage, countPerPage)
        view.updateCart(cartProductsModel = cart.products.map { it.toViewModel() })
        view.updateNavigator(
            PageNavigatorModel(
                isPagingAvailable(maxPage),
                !isFirstPage(currentPage),
                !isLastPage(currentPage, maxPage),
                currentPage
            )
        )
    }

    private fun isLastPage(currentPage: Int, maxPage: Int) = currentPage == maxPage

    private fun isFirstPage(currentPage: Int) = currentPage == 0

    private fun isPagingAvailable(maxPage: Int) = if (maxPage < 1) View.GONE else View.VISIBLE

    private fun calculateMaxPage(): Int = (cartRepository.selectAllCount() - 1) / countPerPage
}
