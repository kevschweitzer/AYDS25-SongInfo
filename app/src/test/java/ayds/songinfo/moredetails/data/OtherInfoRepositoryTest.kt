package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.broker.OtherInfoBroker
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test


class OtherInfoRepositoryTest {

    private val otherInfoLocalStorage: OtherInfoLocalStorage = mockk(relaxUnitFun = true)
    private val broker: OtherInfoBroker = mockk()
    private val otherInfoRepository: OtherInfoRepository = OtherInfoRepositoryImpl(otherInfoLocalStorage, broker)

    @Test
    fun `on getArtistInfo call getArticle from local storage`() {
        val card = Card("artist", "biography", "url", "logoUrl", CardSource.LAST_FM, false)
        every { otherInfoLocalStorage.getCard("artist") } returns listOf(card)
        val cardsAfterCall = listOf(card.copy(isLocallyStored = true))

        val result = otherInfoRepository.getCard("artist")

        Assert.assertEquals(cardsAfterCall, result)
    }

    @Test
    fun `on getArtistInfo call getArticle from service`() {
        val card = Card("artist", "biography", "url", "logoUrl", CardSource.LAST_FM, false)
        val cards = listOf(card)
        every { otherInfoLocalStorage.getCard("artist") } returns emptyList()
        every { broker.getCards("artist") } returns cards
        every { otherInfoLocalStorage.insertCard(any()) } returns Unit

        val result = otherInfoRepository.getCard("artist")

        Assert.assertEquals(cards, result)
        verify { otherInfoLocalStorage.insertCard(card) }
    }

    @Test
    fun `on empty bio, getArtistInfo call getArticle from service`() {
        val card = Card("artist", "", "url", "logoUrl", CardSource.LAST_FM, false)
        val cards = listOf(card)
        every { otherInfoLocalStorage.getCard("artist") } returns emptyList()
        every { broker.getCards("artist") } returns cards

        val result = otherInfoRepository.getCard("artist")

        Assert.assertEquals(cards, result)
        verify { otherInfoLocalStorage.insertCard(card) }
    }
}