package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.LastFmBiography
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

    private val otherInfoLocalStorage: OtherInfoLocalStorage = mockk()
    private val lastFMService: ayds.artist.external.lastfm.LastFmService = mockk()
    private val otherInfoRepository: OtherInfoRepository = OtherInfoRepositoryImpl(otherInfoLocalStorage, lastFMService)

    @Test
    fun `on getArtistInfo call getArticle from local storage`() {
        val card = Card("artist", "biography", "url", CardSource.LAST_FM, false)
        every { otherInfoLocalStorage.getCard("artist") } returns card
        val cardAfterCall = card.copy(isLocallyStored = true)

        val result = otherInfoRepository.getCard("artist")

        Assert.assertEquals(cardAfterCall, result)
        Assert.assertTrue(result.isLocallyStored)
    }

    @Test
    fun `on getArtistInfo call getArticle from service`() {
        val card = Card("artist", "biography", "url", CardSource.LAST_FM, false)
        val lastFmBiography = LastFmBiography("artist", "biography", "url")
        every { otherInfoLocalStorage.getCard("artist") } returns null
        every { lastFMService.getArticle("artist") } returns lastFmBiography
        every { otherInfoLocalStorage.insertCard(card) } returns Unit

        val result = otherInfoRepository.getCard("artist")

        Assert.assertEquals(card, result)
        Assert.assertFalse(result.isLocallyStored)
        verify { otherInfoLocalStorage.insertCard(card) }
    }

    @Test
    fun `on empty bio, getArtistInfo call getArticle from service`() {
        val lastFmBiography = LastFmBiography("artist", "", "url")
        val card = Card("artist", "", "url", CardSource.LAST_FM, false)
        every { otherInfoLocalStorage.getCard("artist") } returns null
        every { lastFMService.getArticle("artist") } returns lastFmBiography

        val result = otherInfoRepository.getCard("artist")

        Assert.assertEquals(card, result)
        Assert.assertFalse(result.isLocallyStored)
        verify(inverse = true) { otherInfoLocalStorage.insertCard(card) }
    }
}