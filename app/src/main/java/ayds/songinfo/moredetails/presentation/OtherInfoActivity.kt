package ayds.songinfo.moredetails.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import ayds.songinfo.R
import ayds.songinfo.moredetails.injector.OtherInfoInjector
import com.squareup.picasso.Picasso

class OtherInfoActivity : Activity() {
    private lateinit var cardContent1TextView: TextView
    private lateinit var openUrl1Button: Button
    private lateinit var source1ImageView: ImageView
    private lateinit var cardContent2TextView: TextView
    private lateinit var openUrl2Button: Button
    private lateinit var sourceImage2View: ImageView
    private lateinit var cardContent3TextView: TextView
    private lateinit var openUrl3Button: Button
    private lateinit var sourceImage3View: ImageView

    private lateinit var presenter: OtherInfoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initViewProperties()
        initPresenter()

        observePresenter()
        getArtistCardAsync()
    }

    private fun initPresenter() {
        OtherInfoInjector.initGraph(this)
        presenter = OtherInfoInjector.presenter
    }

    private fun observePresenter() {
        presenter.cardObservable.subscribe { artistBiography ->
            updateUi(artistBiography)
        }
    }

    private fun initViewProperties() {
        cardContent1TextView = findViewById(R.id.cardContent1TextView)
        openUrl1Button = findViewById(R.id.openUrl1Button)
        source1ImageView = findViewById(R.id.sourceImage1ImageView)
        cardContent2TextView = findViewById(R.id.cardContent2TextView)
        openUrl2Button = findViewById(R.id.openUrl2Button)
        sourceImage2View = findViewById(R.id.sourceImage2ImageView)
        cardContent3TextView = findViewById(R.id.cardContent3TextView)
        openUrl3Button = findViewById(R.id.openUrl3Button)
        sourceImage3View = findViewById(R.id.sourceImage3ImageView)
    }

    private fun getArtistCardAsync() {
        Thread {
            getArtistCard()
        }.start()
    }

    private fun getArtistCard() {
        val artistName = getArtistName()
        presenter.updateCard(artistName)
    }

    private fun updateUi(uiState: CardsUiState) {
        runOnUiThread {
            uiState.cards.getOrNull(0)?.let { updateCard1(it) }
            uiState.cards.getOrNull(1)?.let { updateCard2(it) }
            uiState.cards.getOrNull(2)?.let { updateCard3(it) }
        }
    }

    private fun updateCard1(card: CardUiState) {
        updateOpenUrlButton(openUrl1Button, card.url)
        updateSourceLogo(source1ImageView, card.imageUrl)
        updateCardText(cardContent1TextView, card.contentHtml)
    }

    private fun updateCard2(card: CardUiState) {
        updateOpenUrlButton(openUrl2Button, card.url)
        updateSourceLogo(sourceImage2View, card.imageUrl)
        updateCardText(cardContent2TextView, card.contentHtml)
    }

    private fun updateCard3(card: CardUiState) {
        updateOpenUrlButton(openUrl3Button, card.url)
        updateSourceLogo(sourceImage3View, card.imageUrl)
        updateCardText(cardContent3TextView, card.contentHtml)
    }

    private fun updateOpenUrlButton(openUrlButton: Button, url: String) {
        openUrlButton.setOnClickListener {
            navigateToUrl(url)
        }
    }

    private fun navigateToUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }

    private fun updateSourceLogo(sourceImageView: ImageView, url: String) {
        Picasso.get().load(url).into(sourceImageView)
    }

    private fun getArtistName() =
        intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

    private fun updateCardText(cardContentTextView: TextView,infoHtml: String) {
        cardContentTextView.text = Html.fromHtml(infoHtml)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}


