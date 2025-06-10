package ayds.songinfo.moredetails.injector

import android.content.Context
import androidx.room.Room
import ayds.artist.external.lastfm.LastFmInjector
import ayds.artist.external.newyorktimes.injector.NYTimesInjector
import ayds.artist.external.wikipedia.injector.WikipediaInjector
import ayds.songinfo.moredetails.data.OtherInfoRepositoryImpl
import ayds.songinfo.moredetails.data.broker.LastFMProxy
import ayds.songinfo.moredetails.data.broker.NYTimesProxy
import ayds.songinfo.moredetails.data.broker.OtherInfoBrokerImpl
import ayds.songinfo.moredetails.data.broker.WikipediaProxy
import ayds.songinfo.moredetails.data.local.CardDatabase
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorageImpl
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import ayds.songinfo.moredetails.presentation.CardDescriptionHelperImpl
import ayds.songinfo.moredetails.presentation.OtherInfoPresenter
import ayds.songinfo.moredetails.presentation.OtherInfoPresenterImpl

private const val ARTICLE_BD_NAME = "database-article"

object OtherInfoInjector {

    lateinit var presenter: OtherInfoPresenter

    fun initGraph(context: Context) {

        LastFmInjector.init()

        val cardDatabase =
            Room.databaseBuilder(context, CardDatabase::class.java, ARTICLE_BD_NAME).build()


        val articleLocalStorage = OtherInfoLocalStorageImpl(cardDatabase)

        val lastFMProxy = LastFMProxy(LastFmInjector.lastFmService)
        val wikipediaProxy = WikipediaProxy(WikipediaInjector.wikipediaTrackService)
        val nyTimesProxy = NYTimesProxy(NYTimesInjector.nyTimesService)
        val otherInfoBroker = OtherInfoBrokerImpl(listOf(lastFMProxy, wikipediaProxy, nyTimesProxy))
        val repository: OtherInfoRepository = OtherInfoRepositoryImpl(articleLocalStorage, otherInfoBroker)

        val artistBiographyDescriptionHelper = CardDescriptionHelperImpl()

        presenter = OtherInfoPresenterImpl(repository, artistBiographyDescriptionHelper)
    }
}