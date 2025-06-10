package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class OtherInfoPresenterTest {

    private val otherInfoRepository: OtherInfoRepository = mockk()
    private val cardDescriptionHelper: CardDescriptionHelper = mockk()
    private val otherInfoPresenter: OtherInfoPresenter =
        OtherInfoPresenterImpl(otherInfoRepository, cardDescriptionHelper)

    @Test
    fun `getArtistInfo should return artist biography ui state`() {
        val card = Card("artistName", "biography", "articleUrl", CardSource.LAST_FM)
        every { otherInfoRepository.getCard("artistName") } returns card
        every { cardDescriptionHelper.getDescription(card) } returns "description"
        val artistBiographyTester: (CardUiState) -> Unit = mockk(relaxed = true)

        otherInfoPresenter.cardObservable.subscribe(artistBiographyTester)
        otherInfoPresenter.updateCard("artistName")

        val result = CardUiState("artistName", "description", "articleUrl")
        verify { artistBiographyTester(result) }
    }
}