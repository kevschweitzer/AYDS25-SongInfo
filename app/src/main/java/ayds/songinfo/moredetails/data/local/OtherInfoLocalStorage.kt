package ayds.songinfo.moredetails.data.local

import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

interface OtherInfoLocalStorage {
    fun getCard(artistName: String): List<Card>
    fun insertCard(card: Card)
}

internal class OtherInfoLocalStorageImpl(
    private val cardDatabase: CardDatabase,
) : OtherInfoLocalStorage {

    override fun getCard(artistName: String): List<Card> {
        val cardsEntity = cardDatabase.CardDao().getCardsByArtistName(artistName)
        return cardsEntity.map {
                Card(
                    artistName,
                    it.content,
                    it.url,
                    it.logoUrl,
                    CardSource.entries[it.source]
                )
        }
    }

    override fun insertCard(card: Card) {
        cardDatabase.CardDao().insertCard(
            CardEntity(
                card.artistName, card.text, card.url, card.logoUrl, card.source.ordinal
            )
        )
    }
}