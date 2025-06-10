package ayds.songinfo.moredetails.data.broker

import ayds.artist.external.wikipedia.data.WikipediaArticle
import ayds.artist.external.wikipedia.data.WikipediaTrackService
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

internal class WikipediaProxy(
    private val wikipediaService: WikipediaTrackService
) : CardProxy {

    override fun getCard(artistName: String): Card? {
        val article = wikipediaService.getInfo(artistName)

        return article?.toCard(artistName)
    }

    private fun WikipediaArticle.toCard(artistName: String) =
        Card(artistName, description, wikipediaURL, wikipediaLogoURL, CardSource.WIKIPEDIA)
}