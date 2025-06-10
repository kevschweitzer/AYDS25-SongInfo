package ayds.songinfo.moredetails.data.broker

import ayds.artist.external.lastfm.LastFmBiography
import ayds.artist.external.lastfm.LastFmService
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

internal class LastFMProxy(
    private val lastFmService: LastFmService,
) : CardProxy{

    override fun getCard(artistName: String): Card? {
        val article = lastFmService.getArticle(artistName)
        if (article.biography.isNotEmpty()) {
            return article.toCard()
        }

        return null
    }

    private fun LastFmBiography.toCard() =
        Card(artistName, biography, articleUrl, sourceLogoUrl, CardSource.LAST_FM)

}