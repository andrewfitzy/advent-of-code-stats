import java.io.File
import kotlin.system.exitProcess

class Main {
    val downloader = Downloader()
    val processor = Processor()

    private fun downloadFiles(outputDir: File, year:Int, cookie: String) {
        for(day in 1 .. 25) {
            val paddedDay = "%02d".format(day)
            val outputFile = File(outputDir.absolutePath+File.separator+"Day$paddedDay.html")
            val target = "https://adventofcode.com/$year/leaderboard/day/$day"

            val doc = downloader.getDocument(
                target,
                cookie
            )
            downloader.toSanitisedFile(doc, outputFile)
            Thread.sleep(5000)
        }
    }

    private fun createStats(outputDir: File) {
        val outputFile = File(outputDir.absolutePath+File.separator+"stats.csv")
        val statsForYear = processor.getStatsForYear(outputDir)
        processor.toFile(statsForYear, outputFile)
    }

    fun process(year: Int, cookie: String) {
        val outputDir = File("output/$year")
        outputDir.mkdirs()
        if(cookie != "LOCAL") {
            downloadFiles(outputDir, year, cookie)
        }
        createStats(outputDir)
    }
}


fun main(args: Array<String>) {
    if(args.size != 2) {
        val errorMessage = StringBuilder()
        errorMessage.append("Program Usage:\n")
        errorMessage.append("\tjava -jar aoc-stats.jar [year] [cookie]\n")
        errorMessage.append("Where:\n")
        errorMessage.append("\tyear - the year to download stats for\n")
        errorMessage.append("\tcookie - the session cookie to use for accessing the site. ")
        errorMessage.append("\"If this is set to LOCAL then a local cache of files will be used\n")
        println(errorMessage)
        exitProcess(0)
    }
    val year = args[0].toInt()
    val cookie = args[1]
    val main = Main()
    main.process(year,cookie)
}