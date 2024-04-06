import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import java.net.URL
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.test.*


class TestDownloader {

    private fun getTestDocumentFromFile(): Document {
        val testLocation = "src/test/resources/year/day.html"
        val file = File(testLocation)
        val doc: Document = Jsoup.parse(file)
        return doc
    }

    @Test
    fun testDownloader() {
        // Given
        val downloader = Downloader()
        val doc: Document = getTestDocumentFromFile()
        val target = Files.createTempFile("test_file", "").toFile()
        target.deleteOnExit()

        // When
        downloader.toSanitisedFile(doc, target)

        // Then
        assertNotNull(target)
        assertNotEquals(0, target.length())
    }
}