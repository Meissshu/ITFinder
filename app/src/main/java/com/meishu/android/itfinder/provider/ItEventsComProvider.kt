package com.meishu.android.itfinder.provider

import android.util.Log
import com.meishu.android.itfinder.model.Post
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.text.SimpleDateFormat
import java.util.*

class ItEventsComProvider: Provider{

    companion object {
        const val CSS_Q_SECTION_CLASS = ".section"
        const val CSS_Q_CONTAINER_CLASS = ".container"
        const val CSS_Q_IMAGE_PART_CLASS = ".event-list-item__image"
        const val CSS_Q_TEXT_PART_CLASS = ".col-10"
        const val URL_TO_CONNECT = "https://it-events.com"
        const val CSS_Q_TITLE_CLASS = ".event-list-item__title"
        const val CSS_Q_INFO_CLASS = ".event-list-item__info"
        const val CSS_Q_EVENTS_CLASS = ".event-list-item"
        const val PAGING = "/events?page="
    }

    private var templates = ArrayList<Post>()

    private fun provide(query: String?, threshold: Int): List<Post> {
        var page = 0
        var isDone = false
        templates = ArrayList()

        while (!isDone) {
            try {
                val doc = Jsoup.connect(URL_TO_CONNECT + PAGING + (++page)).get()
                Log.d("IT EVENTS", "GOT!")
                val container = doc.select(CSS_Q_CONTAINER_CLASS)[2]
                val section = container.select(CSS_Q_SECTION_CLASS).first()
                val events = section.select(CSS_Q_EVENTS_CLASS)

                if (events.size == 0) {
                    break
                }

                for (event in events) {
                    if (templates.size >= threshold) {
                        isDone = true
                        break
                    }

                    val post = generateTemplateFromEvent(event)

                    when {
                        query != null -> when {
                            postMatches(post, query) -> templates.add(post)
                        }
                        else -> templates.add(post)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                break
            }
        }

        Log.d("IT EVENTS", templates.size.toString())
        return templates
    }

    private fun postMatches(post: Post, query: String): Boolean =
            post.title.contains(query, true) ||
            post.source.contains(query, true) ||
            post.place.contains(query, true)

    override fun fetchPosts(threshold: Int): List<Post> {
        return provide(null, threshold)
    }

    override fun searchPosts(query: String, threshold: Int): List<Post> {
        return provide(query, threshold)
    }

    private fun generateTemplateFromEvent(event: Element): Post {
        val template = Post()
        fillImagePart(event, template)
        fillTextPart(event, template)
        return template
    }

    private fun fillTextPart(event: Element, template: Post) {
        try {
            val textPart = event.select(CSS_Q_TEXT_PART_CLASS).first()
            val title = textPart.select(CSS_Q_TITLE_CLASS).first().childNode(0).toString()
            val date: Date = parseDate(textPart.select(CSS_Q_INFO_CLASS).first())
            val node = textPart.select(CSS_Q_INFO_CLASS).last().childNode(0)
            val place = node.toString().replace("\n", "")
            template.title = title
            template.time = date.time
            template.place = place
            template.source = URL_TO_CONNECT
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun fillImagePart(event: Element, template: Post) {
        try {
            val imagePart = event.select(CSS_Q_IMAGE_PART_CLASS).first()
            val href = URL_TO_CONNECT + imagePart.attr("href")
            var attr = imagePart.attr("style")
            attr = attr.substring(attr.indexOf("(") + 1, attr.indexOf(")"))
            attr = URL_TO_CONNECT + attr
            template.href = href
            template.imageUrl = attr
        } catch (e: Exception) {
            System.out.println("RUNTIME! №: " + templates.size)
        }
    }

    private fun parseDate(element: Element): Date {
        var date = element.childNode(0).toString()
        date = date.replace("\n", "")
        val regex = " - "
        if (date.contains(regex)) {
            val arr = date.split(regex)
            date = if (arr[0].length <= 3) {
                val deleteFrom = indexOfFirstDigit(arr[1]) + arr[0].length
                val deleteTo = indexOfFirstLetter(arr[1]) + arr[0].length + regex.length
                date.replaceFirst(date.substring(deleteFrom, deleteTo), "")
            } else {
                arr[0]
            }
        }
        val df = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
        return df.parse(date)
    }

    private fun indexOfFirstLetter(source: String): Int {
        val arr = source.toCharArray()
        for (i in arr.indices) {
            if ((arr[i] < '0') || (arr[i] > '9')) {
                return i
            }
        }
        return -1
    }

    private fun indexOfFirstDigit(source: String): Int {
        val arr = source.toCharArray()
        for (i in arr.indices) {
            if ((arr[i] >= '0') && (arr[i] <= '9')) {
                return i
            }
        }
        return -1
    }
}
