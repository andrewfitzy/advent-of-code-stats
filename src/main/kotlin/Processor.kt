import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period
import java.time.format.DateTimeFormatter

class Processor {
    fun getStatsForYear(inputDir: File): String {
        val files = inputDir.listFiles { file ->
            file.length() > 0 && file.name.endsWith(".html")
        }

        val statsMap = mutableMapOf<Int, List<String>>()
        for(file in files!!) {
            val doc: Document = Jsoup.parse(file)
            val day = getDay(doc)
            val stats = getDaysStats(doc)
            statsMap[day] = stats
        }

        val builder = StringBuilder()
        for(day in 1 .. statsMap.size) {
            builder.append("Day ${"%02d".format(day)},,,")
        }
        builder.deleteCharAt(builder.length-1)
        builder.append("\n")
        for(day in 1 .. statsMap.size) {
            builder.append("Task 01,Task 02,Total Time,")
        }
        builder.deleteCharAt(builder.length-1)
        builder.append("\n")
        for(entry in 0 ..< 100) {
            val rowBuilder = StringBuilder()
            for(day in 1 .. statsMap.size) {
                rowBuilder.append(statsMap[day]!![entry])
                rowBuilder.append(",")
            }
            rowBuilder.deleteCharAt(rowBuilder.length-1)
            rowBuilder.append("\n")
            builder.append(rowBuilder.toString())
        }

        return builder.toString()
    }

    private fun getDay(doc: Document): Int {
        val element = doc.getElementsByClass("leaderboard-daylinks-selected").first()
        return element!!.html().toInt()
    }

    private fun getDaysStats(doc: Document): List<String> {
        val result = mutableListOf<String>()
        val firstTask = mutableMapOf<String, Pair<String, String>>()
        val secondTask = mutableMapOf<String, Pair<String, String>>()
        var mapToUse = secondTask

        val mainElement = doc.getElementsByTag("main").first()
        if (mainElement != null) {
            for(element in mainElement.children()) {
                if(element.tag().name == "p") {
                    for(child in element.children()) {
                        if(child.hasClass("leaderboard-daydesc-first")) {
                            mapToUse = firstTask
                        }
                    }
                }

                if(element.classNames().contains("leaderboard-entry")) {
                    val userId = element.attribute("data-user-id").value
                    val position = element.getElementsByClass("leaderboard-position").first().html().trim().replace(")","")
                    val time = element.getElementsByClass("leaderboard-time").first().html()
                    mapToUse[userId] = Pair(position, time)
                }
            }
        }

        val intersection = firstTask.keys.intersect(secondTask.keys)
        var count = 0
        for(user in intersection) {
            //Dec 01 00:05:38
            val task01Time = firstTask[user]?.second!!.split(" ")[2]

            //Dec 01 00:10:55
            val task02Time = secondTask[user]?.second!!.split(" ")[2]

            val delta = getTask02Time(task01Time, task02Time)
            result.add("$task01Time,$delta,$task02Time")
            count++
        }
        while(count < 100) {
            result.add(",,")
            count++
        }
        return result
    }

    private fun getTask02Time(task01Time: String, totalTime: String): String {
        val from = LocalTime.parse(task01Time)
        val to = LocalTime.parse(totalTime)

        val period = Duration.between(from, to)

        val hours = period.toHours()
        val minusMinutes = hours * 60
        var minusSeconds = hours * 3600

        val minutes = period.toMinutes() - minusMinutes
        minusSeconds += (minutes * 60)

        val seconds = period.toSeconds() - minusSeconds
        val delta = "${"%02d".format(hours)}:${"%02d".format(minutes)}:${"%02d".format(seconds)}"
        return delta
    }

    fun toFile(output: String, outputFile: File) {
        outputFile.writeText(output)
    }
}