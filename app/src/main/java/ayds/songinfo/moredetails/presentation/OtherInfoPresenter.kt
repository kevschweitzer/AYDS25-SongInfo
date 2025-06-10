package ayds.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.OtherInfoRepository

interface OtherInfoPresenter {
    val cardObservable: Observable<CardsUiState>
    fun updateCard(artistName: String)

}

internal class OtherInfoPresenterImpl(
    private val repository: OtherInfoRepository,
    private val cardDescriptionHelper: CardDescriptionHelper
) : OtherInfoPresenter {

    override val cardObservable = Subject<CardsUiState>()

    override fun updateCard(artistName: String) {
        val cards = repository.getCard(artistName)

        val uiState = CardsUiState(cards.toUiState())

        cardObservable.notify(uiState)
    }

    private fun List<Card>.toUiState() = this.map {
        CardUiState(
            it.artistName,
            cardDescriptionHelper.getDescription(it),
            it.url,
            it.logoUrl
        )
    }

}