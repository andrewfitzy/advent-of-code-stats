import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.File

class Downloader {

    fun getDocument(target: String, sessionCookie: String): Document {
        val doc: Document = Jsoup.connect(target)
            .userAgent("Mozilla")
            .timeout(5000)
            .cookie("session", sessionCookie)
            .get()

        return doc
    }

    fun toSanitisedFile(doc: Document, target: File) {
        sanitisePage(doc)
        target.writeText(doc.html())
    }

    private fun sanitisePage(doc: Document) {
        val entries: Elements = doc.getElementsByClass("leaderboard-entry")
        for (entry in entries) {
            val position = entry.getElementsByClass("leaderboard-position").first()?.clone()
            val time = entry.getElementsByClass("leaderboard-time").first()?.clone()
            entry.text("")
            if (position != null) {
                entry.appendChild(position)
            }
            if (time != null) {
                entry.appendChild(time)
            }
        }
    }
}