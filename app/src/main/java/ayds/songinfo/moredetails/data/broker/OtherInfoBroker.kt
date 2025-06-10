package ayds.songinfo.moredetails.data.broker

import ayds.songinfo.moredetails.domain.Card

interface OtherInfoBroker {

    fun getCards(artistName: String): List<Card>
}

internal class OtherInfoBrokerImpl(
   private val otherInfoProxies: List<CardProxy>
) : OtherInfoBroker {

    override fun getCards(artistName: String): List<Card> {
        return otherInfoProxies.mapNotNull { it.getCard(artistName) }
    }

}


