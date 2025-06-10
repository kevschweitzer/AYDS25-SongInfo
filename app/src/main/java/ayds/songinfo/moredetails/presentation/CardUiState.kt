package ayds.songinfo.moredetails.presentation

data class CardsUiState(
    val cards: List<CardUiState>
)

data class CardUiState(
    val artistName: String,
    val contentHtml: String,
    val url: String,
    val imageUrl: String
)
