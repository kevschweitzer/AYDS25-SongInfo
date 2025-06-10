package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.LastFmBiography
import ayds.artist.external.lastfm.LastFmService
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository

class OtherInfoRepositoryImpl(
    private val otherInfoLocalStorage: OtherInfoLocalStorage,
    private val lastFmService: LastFmService
): OtherInfoRepository {

    override fun getArtistInfo(artistName: String): ArtistBiography {

        val dbArticle = otherInfoLocalStorage.getArticle(artistName)

        val artistBiography: ArtistBiography

        if (dbArticle != null) {
            artistBiography = dbArticle.markItAsLocal()
        } else {
            artistBiography = lastFmService.getArticle(artistName).toArtistBiography()
            if (artistBiography.biography.isNotEmpty()) {
                otherInfoLocalStorage.insertArtist(artistBiography)
            }
        }
        return artistBiography
    }

    private fun ArtistBiography.markItAsLocal() = copy(isLocallyStored = true)

    private fun LastFmBiography.toArtistBiography() =
        ArtistBiography(artistName, biography, articleUrl)
}