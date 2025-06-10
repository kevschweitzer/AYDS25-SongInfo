package ayds.artist.external.lastfm

data class LastFmBiography(
    val artistName: String,
    val biography: String,
    val articleUrl: String,
    val sourceLogoUrl: String = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
)
