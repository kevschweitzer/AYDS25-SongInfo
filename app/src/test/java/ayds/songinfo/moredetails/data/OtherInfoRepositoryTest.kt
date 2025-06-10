package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.LastFmBiography
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.ArtistBiography
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
        val artistBiography = ArtistBiography("artist", "biography", "url", false)
        every { otherInfoLocalStorage.getArticle("artist") } returns artistBiography

        val result = otherInfoRepository.getArtistInfo("artist")

        Assert.assertEquals(artistBiography.copy(isLocallyStored = true), result)
        Assert.assertTrue(result.isLocallyStored)
    }

    @Test
    fun `on getArtistInfo call getArticle from service`() {
        val lastFmBiography = LastFmBiography("artist", "biography", "url")
        val artistBiography = ArtistBiography("artist", "biography", "url", false)
        every { otherInfoLocalStorage.getArticle("artist") } returns null
        every { lastFMService.getArticle("artist") } returns lastFmBiography
        every { otherInfoLocalStorage.insertArtist(artistBiography) } returns Unit

        val result = otherInfoRepository.getArtistInfo("artist")

        Assert.assertEquals(artistBiography, result)
        Assert.assertFalse(result.isLocallyStored)
        verify { otherInfoLocalStorage.insertArtist(artistBiography) }
    }

    @Test
    fun `on empty bio, getArtistInfo call getArticle from service`() {
        val lastFmBiography = LastFmBiography("artist", "", "url")
        val artistBiography = ArtistBiography("artist", "", "url", false)
        every { otherInfoLocalStorage.getArticle("artist") } returns null
        every { lastFMService.getArticle("artist") } returns lastFmBiography

        val result = otherInfoRepository.getArtistInfo("artist")

        Assert.assertEquals(artistBiography, result)
        Assert.assertFalse(result.isLocallyStored)
        verify(inverse = true) { otherInfoLocalStorage.insertArtist(any()) }
    }
}