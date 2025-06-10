package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.broker.OtherInfoBroker
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.OtherInfoRepository

internal class OtherInfoRepositoryImpl(
    private val otherInfoLocalStorage: OtherInfoLocalStorage,
    private val otherInfoBroker: OtherInfoBroker
) : OtherInfoRepository {

    override fun getCard(artistName: String): List<Card> {
        val dbCard = otherInfoLocalStorage.getCard(artistName)

        if (dbCard.isNotEmpty()) {
            return dbCard.apply { markItAsLocal() }
        } else {
            val cards = otherInfoBroker.getCards(artistName)

            cards.forEach { otherInfoLocalStorage.insertCard(it) }

            return cards
        }
    }

    private fun List<Card>.markItAsLocal() {
        this.forEach { it.isLocallyStored = true }

    }
}
